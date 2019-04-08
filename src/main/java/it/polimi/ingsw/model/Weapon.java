package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Weapon {

    private String weaponName;
    private ArrayList<Color> cost;
    private ArrayList<Effect> effects;
    private boolean isLoaded;
    private int order_1[];
    private int order_2[];
    private int order_3[];


    public Weapon(String weaponName){

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

    public int[] getOrder_1(){
        return this.order_1;
    }

    public int[] getOrder_2(){
        return this.order_2;
    }

    public int[] getOrder_3(){
        return this.order_3;
    }
}
