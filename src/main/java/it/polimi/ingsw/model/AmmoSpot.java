package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

public class AmmoSpot extends Spot {
    private ArrayList<Color> ammoColorList;
    private boolean containsPowerup;

    public AmmoSpot(Room room, int idSpot, int positionX, int positionY) {
        super(room, idSpot, positionX, positionY);

        ArrayList<Color> ammoColorList = new ArrayList<>();
        this.containsPowerup = false;
    }

    public void pickAmmos() {
        if (ammoColorList.isEmpty())
            System.out.println("no ammo in the spot,reload this spot at the end of the turn");
        else
            ammoColorList.removeAll(ammoColorList);
        //player needed as a parameter to add the picked ammo in his hands

    }

    public void addAmmos() {
        if (ammoColorList.isEmpty())
            System.out.println("no ammo in the spot,reload this spot at the end of the turn");
        else
            generateAmmos();
    }


    public void generateAmmos() {

        double rand = Math.random();

        if (rand < 0.4722222) //probability to pick an ammo card with a powerup
                containsPowerup = true;
        else {
            containsPowerup = false;
            ammoColorList.set(2, Color.randomColor());
        }
        ammoColorList.set(0, Color.randomColor());
        ammoColorList.set(1, Color.randomColor());

    }
}
