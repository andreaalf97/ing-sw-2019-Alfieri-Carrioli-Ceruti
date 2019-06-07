package it.polimi.ingsw.model.cards;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;

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

    public PowerUpDeck(JsonObject jsonPowerUpDeck){
        JsonArray jsonPowerUpList = jsonPowerUpDeck.get("powerUpList").getAsJsonArray();

        this.powerUpList = new ArrayList<>();
        for(int i = 0; i < jsonPowerUpList.size(); i++){
            PowerUp p = new PowerUp(jsonPowerUpList.get(i).getAsJsonObject());
            this.powerUpList.add(p);
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
