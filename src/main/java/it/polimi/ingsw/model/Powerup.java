package it.polimi.ingsw.model;

public class Powerup {

    private String powerupName;
    private Color color;
    private Effect effect;

    public Effect getEffect() {
        return effect;
    }

    public String getPowerupName() {
        return this.powerupName;
    }

    public Powerup(String powerupName){
        //here we have to read from Json file and set the right effects
    }
}

