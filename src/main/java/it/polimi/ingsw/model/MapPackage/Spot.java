package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.NoSuchPlayerException;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Spot {

    /**
     * The list of players' nicknames on this spot
     */
    private ArrayList<String> playersHere;

    /**
     * A list of boolean that are true only if the is a door
     * North --> 0
     * East --> 1
     * South --> 2
     * West --> 3
     */
    private ArrayList<Boolean> doors;

    /**
     * The room of this spot
     */
    private Room room;


    /**
     * The constructor
     * @param room the room of this spot
     */
    public Spot(Room room){
        this.playersHere = new ArrayList<>(5);
        this.doors = new ArrayList<>(4);
        this.room = room;
    }


    //SETS AND GETS
    /*------------------------------------------------------------------------------------------------------------------*/
    public boolean hasNorthDoor(){ return doors.get(0);}
    public boolean hasEastDoor(){ return doors.get(1); }
    public boolean hasSouthDoor(){ return doors.get(2);}
    public boolean hasWestDoor(){ return doors.get(3);}
    public Room getRoom() { return room;}

    public ArrayList<Boolean> getDoors() { return new ArrayList<>(doors);}
    public void setRoom(Room room) { this.room = room;}

    public void setDoors (ArrayList<Boolean> doors) throws NullPointerException{
        if (doors == null)
            throw new NullPointerException("doors should not be null");
        else
            this.doors = doors;}

    public ArrayList<String> getPlayersHere() { return new ArrayList<String>(playersHere); }

    //TODO remove this method
    /*public void setPlayersHere(ArrayList<String> playersHere) throws NullPointerException {
        if (playersHere == null)
            throw new NullPointerException("playersHere should not be null");
        else
            this.playersHere = playersHere; }*/
    /*------------------------------------------------------------------------------------------------------------------*/

    /**
     * Adds a player to this spot if the player is not already on the spot
     * @param nickname the name of the player
     */
    public void addPlayer(String nickname){
        if(!playersHere.contains(nickname))
            playersHere.add(nickname);
    }

    /**
     * Removes the player from this spot
     * @param nickname the nickname of the player
     * @throws NoSuchPlayerException if the player is not on this spot
     */
    public void removePlayer(String nickname){
        if(playersHere.contains(nickname))
            playersHere.remove(nickname);
    }

    /**
     * Refills this spot.
     * This should never be called! A spot can only be a SpawnSpot or an AmmoSpot
     * @param objToAdd the object to add
     * @throws ClassCastException always
     */
    public void refill(Object objToAdd) throws ClassCastException{
        throw new ClassCastException("This object should not exist!");
    }

    /**
     * Gives whatever's on this spot to the player.
     * This should never be called! A spot can only be a SpawnSpot or an AmmoSpot
     * @param p the player
     * @throws ClassCastException always
     */
    public void grabSomething(Player p) throws ClassCastException{
        throw new ClassCastException("This object should not exist!");
    }

    /**
     * Is this an ammo spot?
     * This should never be called! A spot can only be a SpawnSpot or an AmmoSpot
     * @return should never return anything
     * @throws ClassCastException always
     */
    public boolean isAmmoSpot() throws ClassCastException{
        throw new ClassCastException("There should not exists any Spot objects!");
    }

    /**
     * Is this a spawn spot?
     * This should never be called! A spot can only be a SpawnSpot or an AmmoSpot
     * @return should never return anything
     * @throws ClassCastException always
     */
    public boolean isSpawnSpot() throws ClassCastException{
        throw new ClassCastException("There should not exists any Spot objects!");
    }

}
