package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PowerupDeck{

    private ArrayList<Powerup> powerupList;

    public void shuffle(){
        return;
    }

    public Powerup pickCard(){
        Powerup powerupToPick = powerupList.get(powerupList.size()-1);
        powerupList.remove(powerupList.size()-1);
        return powerupToPick;
    }
}
