package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;

import java.util.ArrayList;

public class Spot {

    /**
     * The list of players' nicknames on this spot
     */
    protected ArrayList<String> playersHere;

    /**
     * A list of boolean that are true only if the is a door
     * North --> 0
     * East --> 1
     * South --> 2
     * West --> 3
     */
    protected ArrayList<Boolean> doors;

    /**
     * The room of this spot
     */
    protected Room room;

    public Spot(Spot spot){
        this.playersHere = new ArrayList<>(spot.playersHere);
        this.doors = spot.doors;
        this.room = spot.room;
    }
    /**
     * basic constructor used in tests
     */
    public Spot(){
        this.playersHere = new ArrayList<>();
        this.doors = new ArrayList<>();
        this.room = null;
    }

    /**
     * spot constructor
     * @param doors is the doors to set at the new spot
     * @param room is the room to set at the new spot
     */
    public Spot(ArrayList<Boolean> doors, Room room)
    {   this.playersHere = new ArrayList<>();
        this.doors = doors;
        this.room = room;
    }

    public void setPowerUp(PowerUp p){
        //this method called in JsonDeserializer should call only the one in AmmoSpot object
    }

    //SETS AND GETS
    /*------------------------------------------------------------------------------------------------------------------*/
    public boolean hasNorthDoor(){ return doors.get(0);}
    public boolean hasEastDoor(){ return doors.get(1); }
    public boolean hasSouthDoor(){ return doors.get(2);}
    public boolean hasWestDoor(){ return doors.get(3);}
    public Room getRoom() { return room;}
    public ArrayList<Boolean> getDoors() { return new ArrayList<>(doors);}
    public ArrayList<String> getPlayersHere() { return new ArrayList<>(playersHere); }
    /*------------------------------------------------------------------------------------------------------------------*/

    //TESTED
    /**
     * Return the color of the spot based on its room
     * @return RUBY == RED -- TOPAZ == YELLOW -- SAPPHIRE == BLUE -- OTHERS == ANY
     */
    public Color getColor() {

        switch (this.room){

            case RUBY: return Color.RED;

            case TOPAZ: return Color.YELLOW;

            case SAPPHIRE: return Color.BLUE;

            default: return Color.ANY;

        }
    }

    //TESTED
    /**
     * Tell you if the selected player is on this spot
     * @param player the asked player
     * @return true only if the player is on this spot
     */
    public boolean playerHere(String player){
        return playersHere.contains(player);
    }

    //TESTED
    /**
     * Adds a player to this spot if the player is not already on the spot
     * @param nickname the name of the player
     */
    public void addPlayer(String nickname){
        if(!playersHere.contains(nickname))
            playersHere.add(nickname);
    }

    //TESTED
    /**
     * Removes the player from this spot
     * @param nickname the nickname of the player
     */
    public void removePlayer(String nickname){
        if(playersHere.contains(nickname))
            playersHere.remove(nickname);
    }

    /**
     * Refills this spot.
     * This should never be called! A spot can only be a SpawnSpot or an AmmoSpot
     * @param objToAdd the object to add
     */
    public void refill(Object objToAdd) throws RuntimeException {
        throw new RuntimeException("This object should not exist!");
    }

    /**
     * Gives whatever's on this spot to the player.
     * This should never be called! A spot can only be a SpawnSpot or an AmmoSpot
     * @param p the player
     */
    public void grabSomething(Player p, int index) throws RuntimeException {
        throw new RuntimeException("This object should not exist!");
    }

    /**
     * Is this an ammo spot?
     * This should never be called! A spot can only be a SpawnSpot or an AmmoSpot
     * @return should never return anything
     */
    public boolean isAmmoSpot() throws RuntimeException {
        throw new RuntimeException("There should not exists any Spot objects!");
    }

    /**
     * Is this a spawn spot?
     * This should never be called! A spot can only be a SpawnSpot or an AmmoSpot
     * @return should never return anything
     * @throws RuntimeException if this method is called
     */
    public boolean isSpawnSpot() throws RuntimeException {
        throw new RuntimeException("There should not exists any Spot objects!");
    }

    /**
     * tells if this spot it's empty
     * @return boolean
     * @throws RuntimeException if this method is called
     */
    public boolean emptySpot() throws RuntimeException {
        throw new RuntimeException("This should never be called!");
    }

    /**
     * tells if this spot is full
     * @return true if spot is full
     * @throws RuntimeException if method is called
     */
    public boolean isFull() throws RuntimeException {
        throw new RuntimeException("This should never be called!");
    }

    /**
     * public getter for the spawn weapons
     * @return spot weapons list
     * @throws RuntimeException  if this method is called
     */
    public ArrayList<Weapon> getSpawnWeapons() throws RuntimeException {
        throw new RuntimeException("This should never be called!");
    }

    public ArrayList<String> getSpawnWeaponNames() {
        throw new RuntimeException("This should never be called!");
    }
}
