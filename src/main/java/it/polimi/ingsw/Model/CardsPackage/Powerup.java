package it.polimi.ingsw.Model.CardsPackage;

import com.google.gson.JsonObject;
import it.polimi.ingsw.Model.Color;


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


    /**
     *simple constructor used in the tests
     */
    public Powerup(){
        powerupName = null;
        color = null;
        effect = null;
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
     * @return this.powerupName
     */
    public String getPowerupName() {
        //Can return this.powerupName because String is an immutable object
        return this.powerupName;
    }

    /**
     * this method return a powerup from the input JsonObject
     * @param powerupName is the name of the powerup
     * @return an instance of the chosen powerup
     */
    public Powerup(String powerupName, JsonObject jsonPowerupsDeck){

        JsonObject jsonPowerupLoaded = jsonPowerupsDeck.get(powerupName).getAsJsonObject(); //jSonPowerupLoaded contains my powerup
        JsonObject jsonPowerupEffect = jsonPowerupLoaded.get("Effect").getAsJsonObject();

        Effect effectTemp = new Effect(jsonPowerupEffect); //here i load an effect loading all its attributes from the json

        this.powerupName = powerupName;
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
}