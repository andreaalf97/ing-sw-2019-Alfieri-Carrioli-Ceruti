package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

/*
    THE VIEW:
        - What the user sees (UI)
        - In Web Development consists of HTML & CSS, for us it's the CLI & the GUI
        - Communicates with the controller
        - Can be passed dynamic values from the controller
        - Template Engines
 */

@Deprecated
public class VirtualView extends Observable implements Observer, Runnable {

    /**
     * Player nicknames
     */
    ArrayList<String> players;

    /**
     * Open connections
     */
    ArrayList<Socket> sockets;

    /**
     * Only constructor
     * @param players players nicknames
     * @param sockets list of all open connections
     */
    public VirtualView(ArrayList<String> players, ArrayList<Socket> sockets){
        this.players = players;
        this.sockets = sockets;
    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO here the view needs to show to all clients that the model is changed by sending custom messages to each one of them
        //e.g. It shouldn't tell player X which powerups Player Y has in his hands

        if(!(o instanceof GameView))
            throw new IllegalArgumentException("Illegal update argument for the VirtualView");

        GameView gameView = (GameView) o;

        /*
            I can now work with:
                gameView.gameMap;
                gameView.kst;
                gameView.players;
        */


        //TODO implement network logic

    }

    public int askForIndexPowerupToDiscard(String currentPlayer, ArrayList<PowerUp> playerPowerUps) {
        //TODO Ask the correct player which powerup he wants to discard and return the index
        return 0;
    }
    public int askForIndexPowerupToUse(String currentPlayer, ArrayList<PowerUp> playerPowerUps){
        //TODO ask the correct player which powerup he wants to use and return the index
        return 0;
    }

    public int askForIndexWeaponToReload(ArrayList <Weapon> rechargeableWeapons){
        //TODO  return -1 if i don't want to reload, otherwise i return the index of the weapon that i want to reload
        return 0;
    }

    public String askForPlayerNameToAttackPowerup(ArrayList<String> attackablePlayers) {
        //TODO
        return "NONE";
    }

    public int askForIndexMoveToDo(String player){
        //TODO ask the move to do to the player and it will be better to do a representation of the map and the players on the map
        return 0;
    }

    @Override
    public void run() {
        Log.LOGGER.log(Level.INFO, "The Virtual View is up and running");
    }
}