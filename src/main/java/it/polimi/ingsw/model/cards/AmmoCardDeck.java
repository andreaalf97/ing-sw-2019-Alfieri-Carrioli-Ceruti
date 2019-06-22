package it.polimi.ingsw.model.cards;

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
        //todo per persistenza, guarda PU deck
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
