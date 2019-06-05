package it.polimi.ingsw.model.cards;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.MyJsonParser;
import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.model.Color;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;


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
     * if a powerup is a "turnPowerUp" i can use it whenever i want in my turn, for example: i can use a tagback grenade only when a player is attacking me.
     */
    private boolean isTurnPowerup;


    /**
     *simple constructor used in the tests
     */
    public PowerUp(){
        this.isTurnPowerup = false;
        this.powerUpName = null;
        this.color = null;
        this.effect = null;
    }

    /**
     * Constructor only used in tests
     */
    public PowerUp(Color color){
        this.isTurnPowerup = false;
        this.powerUpName = null;
        this.color = color;
        this.effect = null;
    }

    /**
     * Constructor only used in tests
     */
    public PowerUp(String name, Color color){
        this.isTurnPowerup = false;
        this.powerUpName = name;
        this.color = color;
        this.effect = null;
    }

    public static PowerUp getPowerUp(String powerUpName) {
         return MyJsonParser.getPowerUpByName(powerUpName);
    }

    /**
     * Getter
     * @return this.effect
     */
    public Effect getEffect() {
        return new Effect(this.effect);
    }

    public boolean isTurnPowerup(){ return this.isTurnPowerup; }

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

        this.isTurnPowerup = jsonPowerupLoaded.get("isTurnPowerup").getAsBoolean();
        this.powerUpName = powerUpName;
        this.effect = effectTemp;
        this.color = Color.randomColor();
    }

    /**
     * tells if this powerUp is a damagePowerUp or a movementPowerUp
     * @return true if it's a damagePowerUp
     */
    public boolean isDamagePowerUp(){
        if(this.getEffect().getnDamages() > 0)
            return true;
        return false;
    }

    /**
     * creates a powerup by reading the json file
     * @param jsonPowerUp the json powerup
     */
    public PowerUp(JsonObject jsonPowerUp){
        this.isTurnPowerup = jsonPowerUp.get("isTurnPowerup").getAsBoolean();
        this.color = Color.valueOf(jsonPowerUp.get("color").getAsString());
        this.powerUpName = jsonPowerUp.get("powerUpName").getAsString();
        this.effect = new Effect(jsonPowerUp.get("effect").getAsJsonObject());
    }

    /**
     * Returns the color of this powerup
     * @return the color of the powerup
     */
    public Color getColor() {
        return this.color;
    }

}