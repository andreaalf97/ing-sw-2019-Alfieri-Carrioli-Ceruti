package it.polimi.ingsw.model.CardsPackage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Effect;

public class Powerup {

    /**
     * The name of this powerup
     */
    private String powerupName;

    /**
     * The color of this powerup (they can be discarded as ammos)
     */
    private Color color;

    /**
     * The effects this powerup can use
     */
    private Effect effect;

    //TODO finish this + Javadocs
    public Powerup(){
        powerupName = null;
        color = null;
    }

    //SETS AND GETS
    public Effect getEffect() {
        return effect;
    }
    public String getPowerupName() {
        return this.powerupName;
    }

}

