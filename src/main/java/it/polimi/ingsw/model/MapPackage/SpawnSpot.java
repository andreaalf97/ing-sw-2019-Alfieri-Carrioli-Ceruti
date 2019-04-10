package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.CardsPackage.Weapon;

import java.util.ArrayList;

public class SpawnSpot extends Spot {
    private ArrayList<Weapon> weaponList;

    //COSTRUTTORE
    public SpawnSpot(Room room,int idSpot,int positionX, int positionY ){
        super(room,idSpot,positionX,positionY);

        this.weaponList = new ArrayList<>(3);
    }

    //GET
    public ArrayList<Weapon> getWeaponList() {
        return new ArrayList<Weapon>(weaponList);
    }

    public void setWeaponList(ArrayList<Weapon> weaponList){
        if (weaponList == null)
            throw new NullPointerException("weaponList should not be null");
        else
            this.weaponList = weaponList;

    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    public boolean addWeapon(Weapon weaponToAdd){
        if (weaponList.size() >= 3)
                return false;
        else {
            weaponList.add(weaponToAdd);
            return true;
        }

    }
    public boolean removeWeapon(Weapon weaponToRemove){
        if (weaponList.isEmpty() || !(weaponList.contains(weaponToRemove)))
            return false;
        else{
            weaponList.remove(weaponToRemove);
            return true;
        }

    }

    @Override
    public void refill(Object objToAdd) throws IllegalArgumentException, IndexOutOfBoundsException{
        if(!(objToAdd instanceof Weapon))
            throw new IllegalArgumentException("objToAdd should be a Weapon object");

        if(weaponList.size() > 2)
            throw new IndexOutOfBoundsException("There is no room for a weapon");

        weaponList.add((Weapon)objToAdd);
    }

    @Override
    public void grabSomething(Player p) {

    }

    @Override
    public boolean isAmmoSpot() {
        return super.isAmmoSpot();
    }

    @Override
    public boolean isSpawnSpot() {
        return super.isSpawnSpot();
    }
}
