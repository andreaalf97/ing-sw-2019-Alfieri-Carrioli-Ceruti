package it.polimi.ingsw.Model.CardsPackage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import com.google.gson.*;

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
            e.printStackTrace();
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
        Weapon weaponToPick = weaponList.get(weaponList.size() - 1);
        weaponList.remove(weaponList.size()-1);
        return weaponToPick;
    }

}
