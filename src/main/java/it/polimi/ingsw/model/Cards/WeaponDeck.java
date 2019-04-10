package it.polimi.ingsw.model.Cards;

import java.util.ArrayList;

public class WeaponDeck{

    private ArrayList<Weapon> weaponList;

    //COSTRUTTORE
    public WeaponDeck(){
        this.weaponList = new ArrayList<>();
    }


    public ArrayList<Weapon> getWeaponList() {
        return weaponList;
    }


    public Weapon pickCard(){
        Weapon weaponToPick = weaponList.get(weaponList.size()-1);
        weaponList.remove(weaponList.size()-1);
        return weaponToPick;
    }

}
