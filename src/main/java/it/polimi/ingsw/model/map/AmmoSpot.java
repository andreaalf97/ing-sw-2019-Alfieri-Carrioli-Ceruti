package it.polimi.ingsw.model.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.cards.AmmoCard;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUpDeck;
import it.polimi.ingsw.model.cards.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmmoSpot extends Spot {

    public AmmoCard getAmmoCard() {
        return ammoCard;
    }

    /**
     * the ammocard in the spot
     */
    private AmmoCard ammoCard;

    /**
     * A random seed for refilling this spot
     */
    private transient Random rand = new Random();

    /**
     * Basic constructor used when building a new map
     * @param room the room where the spot is
     * @param doors the doors of the spot
     */
    public AmmoSpot(ArrayList<Boolean> doors, Room room) {
        super(doors, room);

        this.ammoCard = new AmmoCard();
    }

    /**
     * Constructor used in tests
     * @param ammoColorList the list of ammos in the spot
     */
    public AmmoSpot(ArrayList<Color> ammoColorList){
        super();
        this.ammoCard = new AmmoCard();
        this.ammoCard.setAmmoColorList(ammoColorList);
    }

    /**
     * basic constructor
     */
    public AmmoSpot(){
        super();
        this.ammoCard = new AmmoCard();
    }

    /**
     * creates an ammospot by reading it from jsonObject
     * @param jsonSpot the spot to deserialize
     */
    public AmmoSpot(JsonObject jsonSpot){
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

        JsonObject jsonAmmoCard = jsonSpot.get("ammoCard").getAsJsonObject();

        ArrayList<Color> ammoColorsList = new ArrayList<>();
        JsonArray jsonAmmo = jsonAmmoCard.get("ammoColorList").getAsJsonArray();
        for (int i = 0; i < jsonAmmo.size(); i++){
            Color c = Color.valueOf(jsonAmmo.get(i).getAsString());
            ammoColorsList.add(c);
        }
        String ammoImagePath = jsonAmmoCard.get("ammoCardImagePath").getAsString();
        Boolean hasPowerUp = jsonAmmoCard.get("hasPowerUp").getAsBoolean();
        this.ammoCard = new AmmoCard(ammoImagePath, ammoColorsList, hasPowerUp);

    }

    /**
     * getter for ammoColorList
     * @return a new copy of ammoColorList
     */
    public List<Color> getAmmoColorList(){ return new ArrayList<>(ammoCard.getAmmoColorList());}


    public boolean hasPowerUp(){return ammoCard.hasPowerUp();}
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    //TESTED
    /**
     * Removes all ammos from this spot
     */
    public void removeAmmo() {
        if (getAmmoColorList().isEmpty())
            throw new RuntimeException("This spot was empty");
        else
            ammoCard.setAmmoColorList(new ArrayList<>());
    }

    /**
     * set ammoColorListInTheSpot
     * @param colors
     */
    public void setAmmo(ArrayList<Color> colors){
        ammoCard.setAmmoColorList(colors);
    }

    //TESTED
    /**
     * Refills this spot
     * @param objToAdd The eventual powerup -- can also be null
     */
    @Override
    public void refill(Object objToAdd){
        if(!this.isFull()) {
            AmmoCard ammoCardDrawn = (AmmoCard) objToAdd;

            this.ammoCard = ammoCardDrawn;
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

         player.giveAmmos(ammoCard.getAmmoColorList());
         ammoCard.setAmmoColorList(new ArrayList<>());
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
        return (this.ammoCard.getAmmoColorList().isEmpty() );
    }

    //TESTED
    /**
     * Tells you if this spot is full
     * @return true or false depending on the presence of a powerup and some ammos
     */
    @Override
    public boolean isFull() {
        return ((this.ammoCard.getAmmoColorList().size() > 1));
    }

    //TESTED
    @Override
    public ArrayList<Weapon> getSpawnWeapons() throws RuntimeException{
        throw new RuntimeException("This should never be called!");
    }

    public ArrayList<String> getSpawnWeaponNames() {
        throw new RuntimeException("This should never be called!");
    }
}
