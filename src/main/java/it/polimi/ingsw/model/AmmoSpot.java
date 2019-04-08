package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

public class AmmoSpot extends Spot {

    private ArrayList<Color> ammoColorList;
    private boolean hasPowerup;

    public AmmoSpot(Room room, int idSpot, int positionX, int positionY) {
        super(room, idSpot, positionX, positionY);

        this.ammoColorList = new ArrayList<>();
        this.hasPowerup = false;
    }

    public void removeAmmo() {
        if (getAmmoColorList().isEmpty())
            System.out.println("no ammo in the spot,reload this spot at the end of the turn");
        else
            getAmmoColorList().removeAll(this.getAmmoColorList());
    }

    public void addAmmo() {
        if (getAmmoColorList().isEmpty())
            generateAmmo();
        else
            System.out.println("no ammo in the spot,reload this spot at the end of the turn");
    }

    public boolean hasPowerup(){return hasPowerup;}

    public ArrayList<Color> getAmmoColorList() { return ammoColorList; }

    private void generateAmmo() {

        double rand = Math.random();

        if (rand < 0.4722222) //probability to pick an ammo card with a powerup
                hasPowerup = true;
        else {
            hasPowerup = false;
            ammoColorList.add(Color.randomColor());
        }
        ammoColorList.add(Color.randomColor());
        ammoColorList.add(Color.randomColor());

    }

}
