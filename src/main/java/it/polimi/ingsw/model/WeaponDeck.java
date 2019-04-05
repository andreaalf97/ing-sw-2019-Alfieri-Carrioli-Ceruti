package it.polimi.ingsw.model;

import java.util.ArrayList;

public class WeaponDeck{

    private ArrayList<Weapon> weaponList;



    public void shuffle(){

        return;
    }


    public Weapon pickCard(){
        Weapon weaponToPick = weaponList.get(weaponList.size()-1);
        weaponList.remove(weaponList.size()-1);
        return weaponToPick;
    }


}
