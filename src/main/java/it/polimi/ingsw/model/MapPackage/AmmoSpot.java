package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.CardsPackage.Powerup;

import java.util.ArrayList;
import java.lang.Math;

public class AmmoSpot extends Spot {

    /**
     * The list of ammos on this spot
     */
    private ArrayList<Color> ammoColorList;

    /**
     * The eventual powerup on this spot
     */
    private Powerup powerup;

    /**
     * Basic constructor
     * @param room the room where the spot is
     * @param doors the doors of the spot
     */
    public AmmoSpot(ArrayList<Boolean> doors, Room room) {
        super(doors, room);

        this.ammoColorList = new ArrayList<>();
        this.powerup = new Powerup();
    }

    public AmmoSpot(ArrayList<Color> ammoColorList, Powerup powerup){
        super();
        this.ammoColorList = ammoColorList;
        this.powerup = powerup;
    }


    /**
     * basic constructor without parameters
     */
    public AmmoSpot(){
        super();
        this.ammoColorList = new ArrayList<>();
        this.powerup = new Powerup();
    }

    public AmmoSpot(ArrayList<Boolean> doors, Room room, ArrayList<Color> ammoColorList, Powerup powerup){
        super();
        this.ammoColorList = ammoColorList;
        this.powerup = powerup;
    }


    /**
     * getter for ammoColorList
     * @return a new copy of ammoColorList
     */
    public ArrayList<Color> getAmmoColorList(){ return new ArrayList<Color>(ammoColorList);}

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * Removes all ammos from this spot
     */
    public void removeAmmos() {
        if (getAmmoColorList().isEmpty())
            System.out.println("no ammo in the spot,reload this spot at the end of the turn");
        else
            ammoColorList = new ArrayList<>();
    }

    /**
     * Adds ammos to this spot
     */
    public void addAmmos() {
        if (getAmmoColorList().isEmpty())
            generateAmmo();
        else
            System.out.println("no ammo in the spot,reload this spot at the end of the turn");
    }

    /**
     * Randomly generates new ammos
     */
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

    /**
     * Refills this spot
     * @param objToAdd The eventual powerup
     */
    @Override
    public void refill(Object objToAdd) {
        super.refill(objToAdd);
    }

    /**
     * Gives whatever is on the spot to a player
     * @param player the player who's receiving the object
     */
    @Override
    public void grabSomething(Player player) {
         if(this.powerup != null) player.givePowerup(powerup);
         player.giveAmmos(ammoColorList);
    }

    /**
     * Is this an ammo spot
     * @return always true
     */
    @Override
    public boolean isAmmoSpot() {
        return true;
    }

    /**
     * Is this a spawn spot
     * @return always false
     */
    @Override
    public boolean isSpawnSpot() {
        return false;
    }
}
