package it.polimi.ingsw.model.cards;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Level;

public class PowerUpDeck {

    /**
     * The list of powerups in this deck
     */
    private ArrayList<PowerUp> powerUpList;

    /**
     * Basic constructor used in tests
     */
    public PowerUpDeck(ArrayList<PowerUp> powerUpListTemp){
        this.powerUpList = powerUpListTemp;
    }

    /**
     * Automatically generates the power up deck from the effects.json file
     *
     */
    public PowerUpDeck()
    {
        this.powerUpList = new ArrayList<>();

        try{
            JsonObject jsonDecks = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject();
            JsonObject jsonPowerupsDeck = jsonDecks.get("Powerups").getAsJsonObject();
            Set<String> keys = jsonPowerupsDeck.keySet();

            for(int i = 0; i < 6; i++) {
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String powerupName = iterator.next();
                    this.powerUpList.add(new PowerUp(powerupName, jsonPowerupsDeck));

                }
            }

        }
        catch(FileNotFoundException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Getter
     * @return the powerup list
     */
    public ArrayList<PowerUp> getPowerUpList() {
        return new ArrayList<>(powerUpList);
    }

    //TESTED
    /**
     * Draws a card from the deck
     * @return the card picked
     */
    public PowerUp drawCard(){
            PowerUp powerUpToPick = powerUpList.get(0);
            powerUpList.remove(0);
            return powerUpToPick;
    }

}
