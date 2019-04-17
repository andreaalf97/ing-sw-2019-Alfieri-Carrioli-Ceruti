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
    public PowerupDeck(ArrayList<Powerup> powerUpListTemp){
        this.powerupList = powerUpListTemp;
    }

    public PowerupDeck(){
        this.powerupList = new ArrayList<>();
    }

    //GET
    public ArrayList<Powerup> getPowerupList() {
        return new ArrayList<>(powerupList);
    }

    /**
     * Draws a card from the deck
     * @return the card picked
     */
    public Powerup pickCard(){
        Powerup powerupToPick = powerupList.get(0);
        powerupList.remove(0);
        return powerupToPick;
    }
}
