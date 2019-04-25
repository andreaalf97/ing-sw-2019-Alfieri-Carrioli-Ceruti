package it.polimi.ingsw.Model.MapPackage;

import it.polimi.ingsw.Model.CardsPackage.PowerUp;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Log;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.Random;

public class AmmoSpot extends Spot {

    /**
     * The list of ammos on this spot
     */
    private ArrayList<Color> ammoColorList;

    /**
     * The eventual powerup on this spot
     */
    private PowerUp powerup;

    /**
     * A random seed for refilling this spot
     */
    private static Random rand = new Random();
    //TODO Check if this is correct or if we should initialize it in the constructor

    /**
     * Basic constructor used when building a new map
     * @param room the room where the spot is
     * @param doors the doors of the spot
     */
    public AmmoSpot(ArrayList<Boolean> doors, Room room) {
        super(doors, room);

        this.ammoColorList = new ArrayList<>();
        this.powerup = null;
    }

    /**
     * Constructor used in tests
     * @param ammoColorList
     * @param powerup
     */
    public AmmoSpot(ArrayList<Color> ammoColorList, PowerUp powerup){
        super();
        this.ammoColorList = ammoColorList;
        this.powerup = powerup;
    }

    public AmmoSpot(){
        super();
        this.ammoColorList = new ArrayList<>();
        this.powerup = null;

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
    public void removeAmmo() {
        if (getAmmoColorList().isEmpty())
            Log.LOGGER.info("no ammo in the spot,reload this spot at the end of the turn");
        else
            ammoColorList = new ArrayList<>();
    }

    /**
     * Adds ammos to this spot
     */
    public void addAmmo() {
        if (getAmmoColorList().isEmpty())
            generateAmmo();
        else
            Log.LOGGER.info("no ammo in the spot,reload this spot at the end of the turn");
    }

    /**
     * Randomly generates new ammos
     */
    private void generateAmmo() {

        //Should be 47.2222%
        if (this.rand.nextBoolean()) { //probability to pick an ammo card with a powerup
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
     * @param objToAdd The eventual powerup -- can also be null
     */
    @Override
    public void refill(Object objToAdd){

        if(objToAdd != null) {
            this.powerup = (PowerUp)objToAdd;

            this.ammoColorList.clear();

            //Return a random number between 0 and 2
            this.ammoColorList.add(Color.values()[this.rand.nextInt(3)]);

            this.ammoColorList.add(Color.values()[this.rand.nextInt(3)]);
        }
        else{
            this.powerup = null;

            this.ammoColorList.clear();

            this.ammoColorList.add(Color.values()[this.rand.nextInt(3)]);

            this.ammoColorList.add(Color.values()[this.rand.nextInt(3)]);

            this.ammoColorList.add(Color.values()[this.rand.nextInt(3)]);
        }
    }

    /**
     * Gives whatever is on the spot to a player
     * @param player the player who's receiving the object
     */
    @Override
    public void grabSomething(Player player) {
         if(this.powerup != null) player.givePowerUp(powerup);
         player.giveAmmos(ammoColorList);

         //todo if i give something to the player i have to reload the spot
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

    /**
     *
     * @return
     */
    @Override
    public boolean emptySpot() {
        if(this.powerup == null && this.ammoColorList.isEmpty())
            return true;
        return false;
    }

    /**
     * Tells you if this spot is full
     * @return true or false depending on the presence of a powerup and some ammos
     */
    @Override
    public boolean isFull(){
        if( (this.powerup == null && this.ammoColorList.size() == 3) || (this.powerup != null && this.ammoColorList.size() == 2) )
            return true;
        return false;
    }
}
