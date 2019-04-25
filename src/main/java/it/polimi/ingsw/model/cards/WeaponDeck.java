package it.polimi.ingsw.model.cards;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Level;

import com.google.gson.*;
import it.polimi.ingsw.model.Log;

public class WeaponDeck{

    /**
     * The list of weapons in the deck
     */
    private ArrayList<Weapon> weaponList;

    /**
     * This constructor build the complete weapon deck
     */
    public WeaponDeck(){

        this.weaponList = new ArrayList<>();

        try {
            JsonObject jsonDecks = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject();
            JsonObject jsonWeaponsDeck = jsonDecks.get("Weapons").getAsJsonObject();
            Set<String> keys = jsonWeaponsDeck.keySet();

            Iterator<String> iterator = keys.iterator();
            while(iterator.hasNext()){
                String weaponName = iterator.next();
                Weapon weaponTemp = new Weapon(weaponName, jsonWeaponsDeck);

                this.weaponList.add(weaponTemp);
            }

        }
        catch(FileNotFoundException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
        }


    }

    /**
     * Constructor only used in tests
     * @param weaponList The weapon list to put in this object
     */
    public WeaponDeck( ArrayList<Weapon> weaponList){
        this.weaponList = weaponList;
    }

    /**
     * getter for weaponList
     * @return a new copy of weaponList
     */
    public ArrayList<Weapon> getWeaponList() {
        return new ArrayList<>(weaponList);
    }


    /**
     * Draws a card from the deck
     * @return the picked card
     */
    public Weapon drawCard(){
        if (!this.getWeaponList().isEmpty()) {
            Weapon weaponToPick = weaponList.get(0);
            weaponList.remove(0);
            return weaponToPick;
        }
        else
            return null;
    }

}
