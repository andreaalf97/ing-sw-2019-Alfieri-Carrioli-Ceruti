package it.polimi.ingsw.model;

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
    public void refill(Object objToAdd) {
        super.refill(objToAdd);
    }

    @Override
    public void grabSomething(Player p) {
        super.grabSomething(p);
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
