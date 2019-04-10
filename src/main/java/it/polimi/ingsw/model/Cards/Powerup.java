package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Effect;

public class Powerup {

    private String powerupName;
    private Color color;
    private Effect effect;

    //COSTRUTTPRE
    public Powerup(String powerupName){
        //here we have to read from Json file and set the right effects
    }

    //SETS AND GETS
    public Effect getEffect() {
        return effect;
    }
    public String getPowerupName() {
        return this.powerupName;
    }

}

