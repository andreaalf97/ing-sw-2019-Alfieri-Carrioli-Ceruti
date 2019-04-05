package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

public class AmmoSpot extends Spot {
    private ArrayList<Color> ammoColorList;
    private ArrayList<Boolean> isPowerupList;

    public AmmoSpot(String room, int idSpot, int positionX, int positionY) {
        super(room, idSpot, positionX, positionY);

        ArrayList<Color> ammoColorList = new ArrayList<>();
        ArrayList<Boolean> isPowerUpList = new ArrayList<>();
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
            generateAmmos(this.ammoColorList, this.isPowerupList);
    }


    public void generateAmmos(ArrayList<Color> ammoColorList, ArrayList<Boolean> isPowerupList) {

        double rand = Math.random();

        if (rand < 0.4722222) //probability to pick an ammo card with a powerup
                isPowerupList.set(0,true);
        else
                isPowerupList.set(0,false);

        isPowerupList.set(1,false);
        isPowerupList.set(2,false);

        ammoColorList.set(0, Color.randomColor()); //????
        ammoColorList.set(1, Color.randomColor());
        ammoColorList.set(2, Color.randomColor());

    }
}
