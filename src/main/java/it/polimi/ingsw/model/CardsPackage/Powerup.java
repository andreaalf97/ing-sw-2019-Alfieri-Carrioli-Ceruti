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
        powerupName = null;
        color = null;
        effect = null;
    }

    public Powerup(){
        powerupName = null;
        color = null;
        effect = null;
    }

    //SETS AND GETS
    public Effect getEffect() {
        return effect;
    }
    public String getPowerupName() {
        return this.powerupName;
    }

    public Powerup loadPowerupFromJson (String powerupName){


        try{
            JsonElement jsonParser = new JsonParser().parse(new FileReader("resources/effects.json"));
            JsonObject root = jsonParser.getAsJsonObject();
            JsonObject jsonPowerups = root.get("Powerups").getAsJsonObject();

            JsonObject jsonPowerupLoaded = jsonPowerups.get(powerupName).getAsJsonObject();
            JsonObject jsonPowerupEffect = jsonPowerupLoaded.get("Effect").getAsJsonObject();

            Effect effectTemp = new Effect (jsonPowerupEffect);
            return new Powerup( powerupName, effectTemp);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

        return new Powerup(powerupName);
    }


    public Powerup (String powerupName, Effect effectTemp){
        this.powerupName = powerupName;
        this.effect = effectTemp;
        this.color = Color.randomColor();

    }

}

