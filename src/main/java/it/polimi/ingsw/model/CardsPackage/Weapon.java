package it.polimi.ingsw.model.CardsPackage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Effect;

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
     *  getter for effects arraylist
     * @return a new copy of effects
     */
    public ArrayList<Effect> getEffects(){
        return new ArrayList<>(effects);
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
}
