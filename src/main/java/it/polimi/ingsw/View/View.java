package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.CardsPackage.Powerup;
import it.polimi.ingsw.Model.CardsPackage.Weapon;

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

    ArrayList<String> players;

    public View(ArrayList<String> players){
        this.players = players;
    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO here the View needs to show to all clients that the model is changed by sending custom messages to each one of them
        //e.g. It shouldn't tell player X which powerups Player Y has in his hands
    }

    public int askForIndexPowerupToDiscard(String currentPlayer, ArrayList<Powerup> playerPowerups) {
        //TODO Ask the correct player which powerup he wants to discard and return the index
        return 0;
    }
    public int askForIndexPowerupToUse(String currentPlayer, ArrayList<Powerup> playerPowerups ){
        //TODO ask the correct player which powerup he wants to use and return the index
        return 0;
    }

    public int askForIndexWeaponToReload(ArrayList <Weapon> rechargeableWeapons){
        //TODO ask the correct player which weapon he wants to reload and return the index
        return 0;
    }

    public String askForPlayerNameToAttackPowerup(ArrayList<String> attackablePlayers) {
        //TODO
        return "NONE";
    }
}