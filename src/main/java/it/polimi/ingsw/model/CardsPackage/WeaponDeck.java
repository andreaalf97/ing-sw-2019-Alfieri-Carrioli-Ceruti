package it.polimi.ingsw.model.CardsPackage;

import java.util.ArrayList;

public class WeaponDeck{

    /**
     * The list of weapons in the deck
     */
    private ArrayList<Weapon> weaponList;

    /**
     * Basic constructor
     */
    public WeaponDeck(){
        this.weaponList = new ArrayList<>();
    }

    public ArrayList<Weapon> getWeaponList() {
        return weaponList;
    }

    /**
     * Draws a card from the deck
     * @return the picked card
     */
    public Weapon pickCard(){
        Weapon weaponToPick = weaponList.get(weaponList.size()-1);
        weaponList.remove(weaponList.size()-1);
        return weaponToPick;
    }

}
