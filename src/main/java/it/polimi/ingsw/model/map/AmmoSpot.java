package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Weapon;

import java.util.ArrayList;
import java.util.List;
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
    private Random rand = new Random();

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
    public List<Color> getAmmoColorList(){ return new ArrayList<>(ammoColorList);}

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    //TESTED
    /**
     * Removes all ammos from this spot
     */
    public void removeAmmo() {
        if (getAmmoColorList().isEmpty())
            Log.LOGGER.info("no ammo in the spot,reload this spot at the end of the turn");
        else
            ammoColorList = new ArrayList<>();
    }

    //TESTED
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

    //TESTED
    /**
     * Gives whatever is on the spot to a player
     * @param player the player who's receiving the object
     */
    @Override
    public void grabSomething(Player player, int index) throws RuntimeException {

        if(index != -1)
            throw new RuntimeException("Index must be -1 when calling grabSomething in AmmoSpot");

         if(powerup != null) {
             player.givePowerUp(powerup);
             powerup = null;
         }
         player.giveAmmos(ammoColorList);
    }

    //TESTED
    /**
     * Is this an ammo spot
     * @return always true
     */
    @Override
    public boolean isAmmoSpot() {
        return true;
    }

    //TESTED
    /**
     * Is this a spawn spot
     * @return always false
     */
    @Override
    public boolean isSpawnSpot() {
        return false;
    }

    //TESTED
    /**
     * this method check if the spot is empty or not
     * @return true if the ammospot is empty
     */
    @Override
    public boolean emptySpot() {
        return ( this.powerup == null && this.ammoColorList.isEmpty() );
    }

    //TESTED
    /**
     * Tells you if this spot is full
     * @return true or false depending on the presence of a powerup and some ammos
     */
    @Override
    public boolean isFull() {
        return ((this.powerup == null && this.ammoColorList.size() == 3) || (this.powerup != null && this.ammoColorList.size() == 2));
    }

    //TESTED
    @Override
    public ArrayList<Weapon> getSpawnWeapons() throws RuntimeException{
        throw new RuntimeException("This should never be called!");
    }
}
