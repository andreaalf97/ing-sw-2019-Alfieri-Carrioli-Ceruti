package it.polimi.ingsw.model.CardsPackage;

import com.google.gson.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Effect;
import it.polimi.ingsw.model.MapPackage.Visibility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Weapon {

    /**
     * The name of the weapon as addressed on the JSON file
     */
    private String weaponName;

    /**
     * The cost of this weapon
     */
    private ArrayList<Color> cost;

    /**
     * The list of the weapon's effects
     */
    private ArrayList<Effect> effects;

    /**
     * True if the weapon is able to shoot
     */
    private boolean isLoaded;

    /**
     * The list of possible order effects
     */
    private ArrayList<Integer[]> order;

    /**
     * A basic constructor
     * @param weaponName The name of the weapon to read from the JSON file
     */
    public Weapon(String weaponName){
        this.weaponName = weaponName;
        isLoaded = false;
        this.cost = new ArrayList<>();
        this.effects = new ArrayList<>();
        this.order = new ArrayList<>();
    }

    /**
     * this method return the weapon loaded from the json file
     * @param weaponName the name of the weapon to load
     * @return the weapon correctly filled
     */
    public Weapon loadWeaponFromJson(String weaponName)
    {

        //support variables
        Weapon weaponTemp = new Weapon(weaponName);
        ArrayList<Effect> effectsListTemp = new ArrayList<>();
        ArrayList<Integer []> ordersTemp = new ArrayList<>();
        Integer [] orderThis = new Integer[10];
        ArrayList<Color> colorsTemp = new ArrayList<>();

        try{
            JsonElement jsonElement = new JsonParser().parse(new FileReader("resources/effects.json"));
            JsonObject root = jsonElement.getAsJsonObject();
            JsonObject jsonWeapons = root.get("Weapons").getAsJsonObject();
            JsonObject jsonWeaponLoaded = jsonWeapons.get(weaponName).getAsJsonObject();

            JsonObject jsonEffect = jsonWeaponLoaded.get("Effects").getAsJsonObject();

            for (int i = 0; i <= jsonEffect.size() - 1; i++){
                Effect effectTemp = new Effect();
                JsonObject jsonEffectThis = jsonEffect.get("Effect" + i).getAsJsonObject();

                effectTemp.setEffectFromJsonWithoutCost(jsonEffectThis);

                ArrayList<Color> costTemp = new ArrayList<>();
                JsonArray jsonCost = jsonEffectThis.get("cost").getAsJsonArray();
                for(int j = 0; j <= jsonCost.size() - 1; j++)
                    costTemp.add( Color.valueOf(jsonCost.get(j).getAsString().toUpperCase()));
                effectTemp.setCost(costTemp);


                effectsListTemp.add(effectTemp);
            }
            weaponTemp.setEffects(effectsListTemp);  //finish loading of effects arrayList


            JsonObject jsonOrders = jsonWeaponLoaded.get("Orders").getAsJsonObject();
            for(int k = 0; k <= jsonOrders.size() - 1; k++){

                JsonArray jsonOrderThis = jsonOrders.get("Order" + k).getAsJsonArray();

                for(int cont = 0; cont <= jsonOrderThis.size() - 1; cont++) {
                    orderThis[cont] = jsonOrderThis.get(cont).getAsInt();
                }
                //TODO devo dichiarare la lunghezza di un array, quindi devo per forza riempirlo per evitare errori a run time e fare controlli a più alto livello + commenti
                for (int cont = jsonOrderThis.size() - 1 ; cont <= orderThis.length - 1; cont ++){
                    orderThis[cont] = -1;
                }
                ordersTemp.add(orderThis);
            }
            weaponTemp.setOrders(ordersTemp);


            JsonArray jsonCost = jsonWeaponLoaded.get("Cost").getAsJsonArray();
            for (int i = 0; i <= jsonCost.size() - 1; i++){
                colorsTemp.add( Color.valueOf(jsonCost.get(i).getAsString().toUpperCase()) );
            }
            weaponTemp.setColors(colorsTemp);

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return weaponTemp;
    }

    /**
     *  getter for effects arraylist
     * @return a new copy of effects
     */
    public ArrayList<Effect> getEffects(){
        return new ArrayList<>(effects);
    }

    /**
     * setter for cost
     * @param colorsTemp the arrayList to set
     * @throws NullPointerException if param is null
     */
    public void setColors (ArrayList<Color> colorsTemp) throws NullPointerException{
        if (colorsTemp == null)
            throw new NullPointerException();
        else
            this.cost = colorsTemp;
    }

    /**
     * setter for order
     * @param ordersTemp the arrayList to set
     * @throws NullPointerException if param is null
     */
    public void setOrders (ArrayList<Integer []> ordersTemp) throws NullPointerException{
        if (ordersTemp == null)
            throw new NullPointerException();
        else
            this.order = ordersTemp;
    }

    /**
     * setter for effects
     * @param effectsTemp the ArrayList to set
     * @throws NullPointerException if param is null
     */
    public void setEffects(ArrayList<Effect> effectsTemp) throws NullPointerException{
        if ( effectsTemp == null) {
            throw new NullPointerException();
        }
        else{
            this.effects = effectsTemp;
        }
    }

    /**
     * getter for order arraylist
     * @return a new copy of order
     */
    public ArrayList<Integer[]> getOrder(){ return new ArrayList<>(order);
    }

    /**
     * Return true if the weapon can shoot
     * @return if the weapon is loaded
     */
    public boolean isLoaded(){
        return this.isLoaded;
    }

    /**
     * Reloads this weapon
     */
    public void reload(){
        this.isLoaded = true;
        return;
    }


    public void unload() {
        this.isLoaded = false;
    }
}
