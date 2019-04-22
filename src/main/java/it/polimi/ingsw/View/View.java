package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.CardsPackage.Powerup;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/*
    THE VIEW:
        - What the user sees (UI)
        - In Web Development consists of HTML & CSS, for us it's the CLI & the GUI
        - Communicates with the controller
        - Can be passed dynamic values from the controller
        - Template Engines
 */

public class View extends Observable implements Observer {

    boolean isWaitingForResponse;

    String playerWaitingFor;

    Response response;

    public View(){
        this.isWaitingForResponse = false;
        this.playerWaitingFor = "NONE";
    }

    /**
     * Here we handle what happens when the Observable objects (the ClientView!) calls the notifyObservers() method
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {

    }

    public void askForIndexPowerupToDiscard(String currentPlayer, ArrayList<Powerup> playerPowerups) {
        //TODO Ask the correct player which powerup he wants to discard and return the index
    }

    public int getPowerupToDiscard() {
        return this.response.responseInt;
    }
}