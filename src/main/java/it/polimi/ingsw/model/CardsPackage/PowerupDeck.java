package it.polimi.ingsw.model.CardsPackage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class PowerupDeck{

    /**
     * The list of powerups in this deck
     */
    private ArrayList<Powerup> powerupList;

    /**
     * Basic constructor used in tests
     */
    public PowerupDeck(ArrayList<Powerup> powerUpListTemp){
        this.powerupList = powerUpListTemp;
    }

    /**
     * Automatically generates the power up deck from the effects.json file
     *
     */
    public PowerupDeck()
    {
        this.powerupList = new ArrayList<>();

        try{
            JsonObject jsonDecks = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject();
            JsonObject jsonPowerupsDeck = jsonDecks.get("Powerups").getAsJsonObject();
            Set<String> keys = jsonPowerupsDeck.keySet();

            for(int i = 0; i < 6; i++) {
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String powerupName = iterator.next();
                    this.powerupList.add(new Powerup(powerupName, jsonPowerupsDeck));

                }
            }

        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Getter
     * @return the powerup list
     */
    public ArrayList<Powerup> getPowerupList() {
        return new ArrayList<>(powerupList);
    }

    /**
     * Draws a card from the deck
     * @return the card picked
     */
    public Powerup drawCard(){
        Powerup powerupToPick = powerupList.get(0);
        powerupList.remove(0);
        return powerupToPick;
    }
}
