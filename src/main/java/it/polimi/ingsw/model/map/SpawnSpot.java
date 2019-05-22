package it.polimi.ingsw.model.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Weapon;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SpawnSpot extends Spot {
    /**
     * The list of weapons on this spot
     */
    protected ArrayList<Weapon> weaponList;

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
     * creates a spawnspot by reading from json
     * @param jsonSpot the spot to deserialize
     */
    public SpawnSpot(JsonObject jsonSpot){
        this.weaponList = new ArrayList<>();
        JsonArray jsonWeaponList = jsonSpot.get("weaponList").getAsJsonArray();
        for (int i = 0; i < jsonWeaponList.size(); i++){
            Weapon w = new Weapon(jsonWeaponList.get(i).getAsJsonObject());
            this.weaponList.add(w);
        }

        this.doors = new ArrayList<>();
        JsonArray jsonDoors = jsonSpot.get("doors").getAsJsonArray();
        for (int i = 0; i < jsonDoors.size(); i++){
            Boolean b = jsonDoors.get(i).getAsBoolean();
            this.doors.add(b);
        }

        this.playersHere = new ArrayList<>();
        JsonArray jsonPlayersHere = jsonSpot.get("playersHere").getAsJsonArray();
        for (int i = 0; i < jsonPlayersHere.size(); i++){
            String s = jsonPlayersHere.get(i).getAsString();
            this.playersHere.add(s);
        }

        this.room = Room.valueOf(jsonSpot.get("room").getAsString());
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


    //TODO should addWeapon and removeWeapon be boolean?
    //TESTED
    /**
     * Adds a weapon to list to this spot weaponlist
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

    //TESTED
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

    //TESTED --> no exceptions tested
    /**
     * Refills this spot with a weapon
     * @param objToAdd the weapon to add
     */
    @Override
    public void refill(Object objToAdd) {
        if(!(objToAdd instanceof Weapon)) //TODO shouldn't use instanceof
            throw new RuntimeException("objToAdd should be a Weapon object");

        if(weaponList.size() > 2) //The caller must check before calling
            throw new RuntimeException("There is no room for a weapon");

        weaponList.add((Weapon)objToAdd);
    }

    //TESTED
    /**
     * Gives a weapon to the player
     * @param p the player who's receiving the weapon
     */
    @Override
    public void grabSomething(Player p, int index) {

        p.giveWeapon(weaponList.get(index));

        weaponList.remove(weaponList.get(index));
    }

    //TESTED
    /**
     * Is this an ammo spot?
     * @return always false
     */
    @Override
    public boolean isAmmoSpot() {
        return false;
    }

    //TESTED
    /**
     * Is this a spawn spot?
     * @return always true
     */
    @Override
    public boolean isSpawnSpot() {
        return true;
    }

    //TESTED
    @Override
    public boolean emptySpot() {
        if(this.weaponList.isEmpty())
            return true;
        return false;
    }

    //TESTED
    @Override
    public boolean isFull() {
        return (this.weaponList.size() == 3);
    }

    //TESTED
    @Override
    public ArrayList<Weapon> getSpawnWeapons(){
        return new ArrayList<>(weaponList);
    }

    public ArrayList<String> getSpawnWeaponNames() {
        ArrayList<String> weaponNames = new ArrayList<>();

        for(Weapon w : weaponList)
            weaponNames.add(w.getWeaponName());

        return weaponNames;
    }

    /**
     * Searches for the weapon in the list and return the index
     * @param weaponName the name of the weapon
     * @return the index
     */
    public int indexOfWeapon(String weaponName) {

        for(int i = 0; i < weaponList.size(); i++)
            if( weaponList.get(i).getWeaponName().equals(weaponName) )
                return i;

        throw new RuntimeException("This weapon is not on this spot");

    }
}
