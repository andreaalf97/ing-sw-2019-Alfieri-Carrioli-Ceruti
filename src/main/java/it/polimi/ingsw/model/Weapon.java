package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Vector;

public class Weapon {

    private String weaponName;
    private ArrayList<Color> cost;
    private ArrayList<Effect> effects;
    private boolean isLoaded;
    private ArrayList<Integer> order_1;
    private ArrayList<Integer> order_2;
    private ArrayList<Integer> order_3;


    public Weapon(String weaponName){
        this.weaponName = weaponName;
        isLoaded = false;
        this.cost = new ArrayList<>();
        this.effects = new ArrayList<>();
        this.order_1 = new ArrayList<>();
        this.order_2 = new ArrayList<>();
        this.order_3 = new ArrayList<>();

    }
    public boolean isLoaded(){
        return this.isLoaded;
    }

    public void reload(){
        this.isLoaded = true;
        return;
    }

    public ArrayList<Effect> getEffects(){
        return this.effects;
    }

    public ArrayList<Integer> getOrder_1(){
        return this.order_1;
    }

    public ArrayList<Integer> getOrder_2(){ return this.order_2; }

    public ArrayList<Integer> getOrder_3(){
        return this.order_3;
    }
}
