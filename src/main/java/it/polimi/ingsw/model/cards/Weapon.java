package it.polimi.ingsw.model.cards;

import com.google.gson.*;
import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.model.Color;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class Weapon {

    /**
     * The name of the weapon as addressed on the JSON file
     */
    private final String weaponName;

    /**
     * The cost of this weapon
     */
    private final ArrayList<Color> cost;

    /**
     * The list of the weapon's effects
     */
    private final ArrayList<Effect> effects;

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    /**
     * True if the weapon is able to shoot
     */
    private boolean isLoaded;

    /**
     * The list of possible order effects
     */
    private ArrayList<Integer[]> order;

    /**
     * constructor used when creating new Weapon from Json
     * @param weaponName the name of the weapon
     * @param cost the cost of the weapon
     * @param effects the effects of the weapon
     * @param order the orders to follow for the right application of my effects
     */
    public Weapon(String weaponName, ArrayList<Color> cost, ArrayList<Effect> effects, ArrayList<Integer[]> order){
        this.weaponName = weaponName;
        this.effects = effects;
        this.order = order;
        this.cost = cost;
        isLoaded = true;
    }

    public Weapon(String weaponName){

        this.weaponName = weaponName;
        this.effects = new ArrayList<>();
        this.order = new ArrayList<>();
        this.cost = new ArrayList<>();
        this.isLoaded = true;

    }

    /**
     * A basic constructor only used in tests
     * @param weaponName The name of the weapon to read from the JSON file
     */
    public static Weapon getWeapon(String weaponName){

        try {
            JsonObject jsonDecks = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject();
            JsonObject jsonWeaponsDeck = jsonDecks.get("Weapons").getAsJsonObject();

            return new Weapon(weaponName, jsonWeaponsDeck);
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while reading JSON");
            return null;
        }

    }

    /**
     * this method returns a weapon from a JsonObject
     * @param weaponName  the name of the weapon to load
     * @param jsonDeck the jsonObject that contains all the weapons
     * @return the weapon correctly filled
     */
    public Weapon(String weaponName, JsonObject jsonDeck)
    {

        //support variables
        ArrayList<Effect> effectsListTemp = new ArrayList<>();
        ArrayList<Integer []> ordersTemp = new ArrayList<>();
        ArrayList<Color> colorsTemp = new ArrayList<>();

        JsonObject jsonWeaponLoaded = jsonDeck.get(weaponName).getAsJsonObject(); //this is my weapon in json, inside i have all my attributes
        JsonObject jsonEffect = jsonWeaponLoaded.get("Effects").getAsJsonObject();

        for (int i = 0; i <= jsonEffect.size() - 1; i++){
            JsonObject jsonEffectThis = jsonEffect.get("Effect" + i).getAsJsonObject();

            Effect effectTemp = new Effect(jsonEffectThis);

            effectsListTemp.add(effectTemp);
        } //at the end of the for effectListTemp has all the effects

        JsonObject jsonOrders = jsonWeaponLoaded.get("Orders").getAsJsonObject();
        for(int k = 0; k <= jsonOrders.size() - 1; k++){
            JsonArray jsonOrderThis = jsonOrders.get("Order" + k).getAsJsonArray();

            Integer [] orderThisVector = new Integer[jsonOrderThis.size()];

            for(int cont = 0; cont <= jsonOrderThis.size() - 1; cont++) {
                orderThisVector[cont] = jsonOrderThis.get(cont).getAsInt();

            }
            ordersTemp.add(orderThisVector);
        } //at the end of the for ordersTemp contains all the orders of the weapon


        JsonArray jsonCost = jsonWeaponLoaded.get("Cost").getAsJsonArray();
        for (int i = 0; i <= jsonCost.size() - 1; i++){
            colorsTemp.add( Color.valueOf(jsonCost.get(i).getAsString().toUpperCase()) );
        }
        // at the end of the for colorsTemp contains the cost of the weapon


        this.weaponName = weaponName;
        this.isLoaded = true;
        this.cost = colorsTemp;
        this.effects = effectsListTemp;
        this.order = ordersTemp;
    }


    /**
     *  getter for effects arraylist
     * @return a new copy of effects
     */
    public ArrayList<Effect> getEffects(){
        return new ArrayList<>(effects);
    }

    public String getWeaponName(){
        return weaponName;
    }


    /**
     * getter for order arraylist
     * @return a new copy of order
     */
    public ArrayList<Integer[]> getOrder(){ return new ArrayList<>(order); }

    /**
     * getter for cost arraylist
     * @return a new copy of cost
     */
    public ArrayList<Color> getCost(){ return new ArrayList<>(cost); }

    /**
     * Return true if the weapon can shoot
     * @return if the weapon is loaded
     */
    public boolean isLoaded(){
        return this.isLoaded;
    }

    //TESTED
    /**
     * Reloads this weapon
     */
    public void reload(){
        this.isLoaded = true;
    }

    //TESTED
    /**
     * after shooting unload the weapon
     */
    public void unload() throws RuntimeException{

        if(!this.isLoaded)
            throw new RuntimeException("This weapon should be loaded");

        this.isLoaded = false;
    }
}
