package it.polimi.ingsw.model.cards;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Color;


public class PowerUp {

    /**
     * The name of this powerup
     */
    private String powerUpName;

    /**
     * The color of this powerup (they can be discarded as ammos)
     */
    private Color color;

    /**
     * The effects this powerup can use
     */
    private Effect effect;


    /**
     *simple constructor used in the tests
     */
    public PowerUp(){
        powerUpName = null;
        color = null;
        effect = null;
    }

    /**
     * Constructor only used in tests
     */
    public PowerUp(Color color){
        this.powerUpName = null;
        this.color = color;
        this.effect = null;
    }

    /**
     * Getter
     * @return this.effect
     */
    public Effect getEffect() {
        return new Effect(this.effect);
    }

    /**
     * Getter
     * @return this.powerUpName
     */
    public String getPowerUpName() {
        //Can return this.powerUpName because String is an immutable object
        return this.powerUpName;
    }

    /**
     * this method return a powerup from the input JsonObject
     * @param powerUpName is the name of the powerup
     * @return an instance of the chosen powerup
     */
    public PowerUp(String powerUpName, JsonObject jsonPowerupsDeck){

        JsonObject jsonPowerupLoaded = jsonPowerupsDeck.get(powerUpName).getAsJsonObject(); //jSonPowerupLoaded contains my powerup
        JsonObject jsonPowerupEffect = jsonPowerupLoaded.get("Effect").getAsJsonObject();

        Effect effectTemp = new Effect(jsonPowerupEffect); //here i load an effect loading all its attributes from the json

        this.powerUpName = powerUpName;
        this.effect = effectTemp;
        this.color = Color.randomColor();
    }

    /**
     * Returns the color of this powerup
     * @return the color of the powerup
     */
    public Color getColor() {
        return this.color;
    }

    public boolean isTurnPowerup() {
        //TODO might have to review this

        if( powerUpName.equals("Teleporter") || powerUpName.equals("Newton") )
            return true;

        return false;
    }
}