package it.polimi.ingsw.model.cards;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Color;

import java.util.ArrayList;

public class AmmoCardDeck {

    private static final int AMMOCARDNUMBER = 36;


    /**
     * The list of powerups in this deck
     */
    private ArrayList<AmmoCard> ammoCardList;


    /**
     * Creates AmmoCardDeck with all the ammos and powerUps inside
     */
    public AmmoCardDeck(ArrayList<AmmoCard> ammoCardList){
        this.ammoCardList = ammoCardList;
    }


    public AmmoCardDeck(JsonObject jsonAmmoCardDeck){
        JsonArray jsonAmmocardList = jsonAmmoCardDeck.get("ammoCardList").getAsJsonArray();

        this.ammoCardList = new ArrayList<>();
        for(int i = 0; i < jsonAmmocardList.size(); i++){
            AmmoCard ammoCard = new AmmoCard(jsonAmmocardList.get(i).getAsJsonObject());
            this.ammoCardList.add(ammoCard);
        }
    }


    public ArrayList<AmmoCard> getAmmoCardList() {
        return ammoCardList;
    }


    public AmmoCard drawCard(){
        AmmoCard ammoCardToPick = ammoCardList.get(0);
        ammoCardList.remove(0);
        return ammoCardToPick;
    }
}
