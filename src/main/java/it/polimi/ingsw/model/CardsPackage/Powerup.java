package it.polimi.ingsw.model.CardsPackage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Effect;
import it.polimi.ingsw.model.MapPackage.Visibility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

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
     * simple constructor for the Powerup
     */
    public Powerup(String powerupName ){
        powerupName = powerupName;
        color = null;
        effect = null;
    }

    /**
     *simple constructor used in the tests
     */
    public Powerup(){
        powerupName = null;
        color = null;
        effect = null;
    }

    /**
     * constructor used for creating the powerup from json
     * @param powerupName the name of the powerup
     * @param effectTemp the effect of the powerup
     */
    public Powerup (String powerupName, Effect effectTemp){
        this.powerupName = powerupName;
        this.effect = effectTemp;
        this.color = Color.randomColor();

    }

    //SETS AND GETS
    public Effect getEffect() {
        return this.effect;
    }
    public String getPowerupName() {
        return this.powerupName;
    }

    /**
     * this method return a powerup from the json file effects.json
     * @param powerupName is the name of the powerup
     * @return an istance of the powerup chosen
     */
    public Powerup loadPowerupFromJson (String powerupName, JsonObject jsonPowerupsDeck){

        JsonObject jsonPowerupLoaded = jsonPowerupsDeck.get(powerupName).getAsJsonObject(); //jSonPowerupLoaded contains my powerup
        JsonObject jsonPowerupEffect = jsonPowerupLoaded.get("Effect").getAsJsonObject();

        Effect effectTemp = new Effect (jsonPowerupEffect); //here i load an effect loading all its attributes from the json
        Powerup powerupTemp = new Powerup( powerupName, effectTemp);


        return powerupTemp;
    }



}

