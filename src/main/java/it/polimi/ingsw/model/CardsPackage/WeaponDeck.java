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

    /**
     * getter for weaponList
     * @return a new copy of weaponList
     */
    public ArrayList<Weapon> getWeaponList() {
        return new ArrayList<>(weaponList);
    }

    /**
     * set the weaponList of the deck
     * @param weaponListToSet the list of weapons avaliable
     * @throws NullPointerException if param is null
     */
    public void setWeaponDeck(ArrayList<Weapon> weaponListToSet) throws NullPointerException{
        if (weaponListToSet == null)
            throw new NullPointerException();
        else
            this.weaponList = weaponListToSet;
    }

    /**
     * Draws a card from the deck
     * @return the picked card
     */
    public Weapon pickCard(){
        Weapon weaponToPick = weaponList.get(weaponList.size() - 1);
        weaponList.remove(weaponList.size()-1);
        return weaponToPick;
    }

}
