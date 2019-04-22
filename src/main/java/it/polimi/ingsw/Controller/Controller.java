package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Log;
import it.polimi.ingsw.View.View;

import java.util.Observable;
import java.util.Observer;

/*
    THE CONTROLLER:
        - Receives input the view
        - Processes requests
        - Gets data from the model
        - Passes data to the view
 */

public class Controller extends Thread implements Observer {

    /**
     * The MODEL
     */
    Game gameModel;

    /**
     * The VIEW
     */
    View view;

    /**
     * This lock is used to make run() and update() work in a synchronized way
     */
    final Object lock;

    /**
     * This attribute is used by the run() method to check if the data from the view is ready or not
     */
    boolean dataIsReady;

    public Controller(Game gameModel, View view){
        this.gameModel = gameModel;
        this.view = view;
        this.lock = new Object();
        this.dataIsReady = false;
    }


    @Override
    public void update(Observable o, Object arg) {
        synchronized (this.lock){
            this.dataIsReady = true;
        }
    }

    /**
     * This method implements the void run() method from Runnable and is used to process the entire Game by pausing when waiting for data from the view
     */
    public void run(){
        Log.LOGGER.info("Game starting");
        Log.LOGGER.warning("The map has been chosen by polling");
        Log.LOGGER.warning("The KST has been set up after polling");

        boolean endOfTurns = false;

        //Runs the first turn for all players
        for(String currentPlayer : gameModel.getPlayerNames()){

            //Spawn player by choosing 2 powerups!
            //At this point the player should have nothing in his hands
            gameModel.givePowerup(currentPlayer);
            gameModel.givePowerup(currentPlayer);

            this.dataIsReady = false;
            this.view.askForIndexPowerupToDiscard(currentPlayer, this.gameModel.getPlayerPowerups(currentPlayer));

            //waits until data is ready
            while(!this.dataIsReady) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    Log.LOGGER.warning(e.getMessage());
                }
            }

            int chosenPowerupToDiscard;

            synchronized (this.lock){
                //TODO work on the data passed by the view
                chosenPowerupToDiscard = this.view.getPowerupToDiscard();

            }

            gameModel.respawn(currentPlayer, chosenPowerupToDiscard);

            //Do you want to use a powerup?

            //Move - Move&Grab - Attack

            //Do you want to use a powerup?

            //Move - Move&Grab - Attack

            //Do you want to use a powerup?

            //Do you want to reload?

            gameModel.checkDeaths();
            gameModel.refillAmmos();
            gameModel.refillSpawns();
        }

        //Runs until the end of the regular game, will handle frenzy in the next loop
        while(!endOfTurns){

            for(String currentPlayer : gameModel.getPlayerNames()) {

                //Do you want to use a powerup?

                //Move - Move&Grab - Attack

                //Do you want to use a powerup?

                //Move - Move&Grab - Attack

                //Do you want to use a powerup?

                //Do you want to reload?

                gameModel.checkDeaths();
                gameModel.refillAmmos();
                gameModel.refillSpawns();

                if (gameModel.noMoreSkullsOnKST())
                    endOfTurns = true;
            }
        }

        for(String currentPlayer : gameModel.getPlayerNames()){
            //TODO check if the round continues from the last player
            gameModel.setupForFrenzy();
        }

        gameModel.giveKSTpoints();
        gameModel.endGame();
    }
}
