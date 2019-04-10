package it.polimi.ingsw.model.CardsPackage;

import java.util.ArrayList;

public class PowerupDeck{

    private ArrayList<Powerup> powerupList;

    //COSTRUTTORE
    public PowerupDeck(){
        this.powerupList = new ArrayList<>();
    }

    //GET
    public ArrayList<Powerup> getPowerupList() {
        return powerupList;
    }


    public Powerup pickCard(){
        Powerup powerupToPick = powerupList.get(powerupList.size()-1);
        powerupList.remove(powerupList.size()-1);
        return powerupToPick;
    }
}
