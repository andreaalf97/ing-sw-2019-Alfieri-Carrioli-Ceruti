package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Weapon {

    private String weaponName;
    private ArrayList<Color> cost;
    private ArrayList<Effect> effects;
    private boolean isLoaded;
    private int order_1[];
    private int order_2[];
    private int oreder_3[];


    public Weapon(String weaponName){

    }
    public boolean isLoaded(){
        return this.isLoaded;
    }

    public void reload(){
        this.isLoaded = true;
        return;
    }
}
