package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

public class AmmoSpot extends Spot {

    private ArrayList<Color> ammoColorList;
    private Powerup powerup;

    public AmmoSpot(Room room, int idSpot, int positionX, int positionY, Powerup powerup) {
        super(room, idSpot, positionX, positionY);

        this.ammoColorList = new ArrayList<>();
        this.powerup = powerup;
    }

    public void setAmmoColorList (ArrayList<Color> ammoColorList) throws NullPointerException{
        if (ammoColorList == null)
            throw new NullPointerException("ammoColoList should not be null");
        else
            this.ammoColorList = ammoColorList;
    }

    public ArrayList<Color> getAmmoColorList(){ return new ArrayList<Color>(ammoColorList);}

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void removeAmmos() {
        if (getAmmoColorList().isEmpty())
            System.out.println("no ammo in the spot,reload this spot at the end of the turn");
        else
            getAmmoColorList().removeAll(this.getAmmoColorList());
    }

    public void addAmmos() {
        if (getAmmoColorList().isEmpty())
            generateAmmo();
        else
            System.out.println("no ammo in the spot,reload this spot at the end of the turn");
    }

    private void generateAmmo() {

        double rand = Math.random();

        if (rand < 0.4722222) { //probability to pick an ammo card with a powerup
            //TODO hasPowerup = true;
        }
        else {
            //TODO hasPowerup = false;
            ammoColorList.add(Color.randomColor());
        }
        ammoColorList.add(Color.randomColor());
        ammoColorList.add(Color.randomColor());

    }

    @Override
    public void refill(Object objToAdd) {
        super.refill(objToAdd);
    }

    @Override
    public void grabSomething(Player player) {
         if(this.powerup != null) player.givePowerup(powerup);
         player.giveAmmos(ammoColorList);
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
