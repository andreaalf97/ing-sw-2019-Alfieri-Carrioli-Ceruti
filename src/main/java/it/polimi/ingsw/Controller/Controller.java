package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Log;
import it.polimi.ingsw.View.View;

import java.util.logging.Level;

/*
    THE CONTROLLER:
        - Receives input the view
        - Processes requests
        - Gets data from the model
        - Passes data to the view
 */

public class Controller {

    /**
     * The MODEL
     */
    Game gameModel;

    /**
     * The VIEW
     */
    View view;

    public Controller(Game gameModel, View view){
        this.gameModel = gameModel;
        this.view = view;
    }

    /**
     * This method implements the void run() method from Runnable and is used to process the entire Game by pausing when waiting for data from the view
     */
    public void runGame(){
        Log.LOGGER.info("Game starting");
        Log.LOGGER.warning("The map has been chosen by polling");
        Log.LOGGER.warning("The KST has been set up after polling");

        boolean endOfTurns = false;

        //Runs the first turn for all players
        for(String currentPlayer : gameModel.getPlayerNames()){
            Log.LOGGER.log(Level.INFO, "IT IS {0} TURN", currentPlayer);

            //Spawn player by choosing 2 powerups!
            //At this point the player should have nothing in his hands
            gameModel.givePowerup(currentPlayer);
            gameModel.givePowerup(currentPlayer);

            int chosenPowerupToDiscard = this.view.askForIndexPowerupToDiscard(currentPlayer, this.gameModel.getPlayerPowerups(currentPlayer));;

            gameModel.respawn(currentPlayer, chosenPowerupToDiscard);

            runTurn(currentPlayer);
        }

        //Runs until the end of the regular game, will handle frenzy in the next loop
        while(!endOfTurns){

            for(String currentPlayer : gameModel.getPlayerNames()) {
                Log.LOGGER.log(Level.INFO, "IT IS {0} TURN", currentPlayer);

                runTurn(currentPlayer);

                checkDeaths();
                gameModel.refillAllAmmoSpots();
                gameModel.refillAllSpawnSpots();

                if (gameModel.noMoreSkullsOnKST())
                    endOfTurns = true;
            }
        }

        //Set up each player for frenzy round
        for(String currentPlayer : gameModel.getPlayerNames()){
            Log.LOGGER.log(Level.INFO, "SETTING UP {0} FOR FRENZY", currentPlayer);

            gameModel.setupForFrenzy(currentPlayer);
        }

        //Run the frenzy round
        for(String currentPlayer : gameModel.getPlayerNames()){
            Log.LOGGER.log(Level.INFO, "IT IS {0} FRENZY TURN", currentPlayer);

            //TODO make sure he round continues from the last player
            runTurn(currentPlayer);
        }

        gameModel.giveKSTpoints();

        endGame();
    }

    /**
     * This runs the turn for the current player
     * @param currentPlayer
     */
    private void runTurn(String currentPlayer){

        //If player is dead, respawn
        if(gameModel.playerIsDead(currentPlayer)){
            gameModel.givePowerup(currentPlayer);
            int chosenPowerUpToDiscard = view.askForIndexPowerupToDiscard(currentPlayer, gameModel.getPlayerPowerups(currentPlayer));;

            gameModel.respawn(currentPlayer, chosenPowerUpToDiscard);
        }


        //Do you want to use a power up?
        useTurnPowerup(currentPlayer);

        //Move - Move&Grab - Attack
        doOneMove(currentPlayer);

        //Do you want to use a power up?
        useTurnPowerup(currentPlayer);

        //Move - Move&Grab - Attack
        doOneMove(currentPlayer);

        //Do you want to use a power up?
        useTurnPowerup(currentPlayer);

        //Do you want to reload?
        reloadWeapons(currentPlayer);

        //Checks if any player is dead
        checkDeaths();
        gameModel.refillAllAmmoSpots();
        gameModel.refillAllSpawnSpots();
    }

    /**
     * Asks the player if he wants to reload any of his weapons
     * @param player
     */
    private void reloadWeapons(String player) {
        //TODO
    }

    /**
     * Asks the player to do one move by choosing between Move / Move&Grab / Attack and executes it
     * @param player the choosing player
     */
    private void doOneMove(String player) {
        //TODO
    }

    /**
     * Asks the player if he wants to use a "turn" power up and executes it
     * A "turn" power up is one that can be used at any time during a turn (i.e. Teleport)
     * @param player the choosing player
     */
    private void useTurnPowerup(String player) {
        //TODO
    }

    /**
     * This method checks if any player is dead and counts their boards assigning the right amount of point to each player.
     * It is usually executed at the end of each turn
     */
    public void checkDeaths() {
        //TODO
    }

    /**
     * This finds the winner, and tells everyone the game is over
     */
    private void endGame() {
        //TODO
    }
}
