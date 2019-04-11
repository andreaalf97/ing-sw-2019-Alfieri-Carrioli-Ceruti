package it.polimi.ingsw.model.CardsPackage;

import java.util.ArrayList;

public class PowerupDeck{

    /**
     * The list of powerups in this deck
     */
    private ArrayList<Powerup> powerupList;

    /**
     * Basic constructor
     */
    public PowerupDeck(){
        this.powerupList = new ArrayList<>();
    }

    //GET
    public ArrayList<Powerup> getPowerupList() {
        return powerupList;
    }

    /**
     * Draws a card from the deck
     * @return the card picked
     */
    public Powerup pickCard(){
        Powerup powerupToPick = powerupList.get(powerupList.size()-1);
        powerupList.remove(powerupList.size()-1);
        return powerupToPick;
    }
}
