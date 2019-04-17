package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.CardsPackage.Weapon;

import java.util.ArrayList;

public class SpawnSpot extends Spot {
    /**
     * The list of weapons on this spot
     */
    private ArrayList<Weapon> weaponList;

    /**
     * A basic constructor
     * @param doors the doors of this spot
     * @param room the room of this spot
     */

    public SpawnSpot(ArrayList<Boolean> doors,Room room){
        super(doors,room);

        this.weaponList = new ArrayList<>();
    }

    public SpawnSpot(ArrayList<Boolean> doors,Room room, ArrayList<Weapon> weaponList){
        super(doors,room);
        this.weaponList = weaponList;
    }

    public SpawnSpot(ArrayList<Weapon> weaponList){
        super();
        this.weaponList = weaponList;
    }

    /**
     * basic constructor
     */
    public SpawnSpot(){
        super();
    }

    /**
     * getter for weaPonList
     * @return a copy of this.weaponList
     */
    public ArrayList<Weapon> getWeaponList() {
        return new ArrayList<Weapon>(weaponList);
    }


    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * Adds a weapon to this spot
     * @param weaponToAdd the weapon to add
     * @return true if the weapon was added
     */
    public boolean addWeapon(Weapon weaponToAdd){
        if (weaponList.size() >= 3)
                return false;
        else {
            weaponList.add(weaponToAdd);
            return true;
        }

    }

    /**
     * Removes a weapon from this spot
     * @param weaponToRemove the weapon to remove
     * @return true if the weapon was correctly removed
     */
    public boolean removeWeapon(Weapon weaponToRemove){
        if (weaponList.isEmpty() || !(weaponList.contains(weaponToRemove)))
            return false;
        else{
            weaponList.remove(weaponToRemove);
            return true;
        }

    }

    /**
     * Refills this spot with a weapon
     * @param objToAdd the weapon to add
     * @throws IllegalArgumentException if the object is not a weapon
     * @throws IndexOutOfBoundsException if there is no room for a new weapon
     */
    @Override
    public void refill(Object objToAdd) throws IllegalArgumentException, IndexOutOfBoundsException{
        if(!(objToAdd instanceof Weapon))
            throw new IllegalArgumentException("objToAdd should be a Weapon object");

        if(weaponList.size() > 2)
            throw new IndexOutOfBoundsException("There is no room for a weapon");

        weaponList.add((Weapon)objToAdd);
    }

    /**
     * Gives a weapon to the player
     * @param p the player who's receiving the weapon
     */
    @Override
    public void grabSomething(Player p) {

    }

    /**
     * Is this an ammo spot?
     * @return always false
     */
    @Override
    public boolean isAmmoSpot() {
        return false;
    }

    /**
     * Is this a spawn spot?
     * @return always true
     */
    @Override
    public boolean isSpawnSpot() {
        return true;
    }
}
