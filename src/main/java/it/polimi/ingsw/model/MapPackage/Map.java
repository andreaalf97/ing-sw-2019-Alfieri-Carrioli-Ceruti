package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.Player;

public class Map {

    /**
     * The matrix representing the map
     */
    private Spot[][] map;

    /**
     * A basic constructor
     * @param map the map
     */
    public Map(Spot[][] map){
        this.map = map;
    }


    /**
     * Checks if the first spot can see the second spot
     * @param spot1X spot1 X
     * @param spot1Y spot1 Y
     * @param spot2X spot2 X
     * @param spot2Y spot2 Y
     * @return true if Spot1 can see Spot2
     */
    public boolean see(int spot1X, int spot1Y, int spot2X, int spot2Y){
        Spot spotX = getSpotByIndex(spot1X, spot1Y);
        Spot spotY = getSpotByIndex(spot2X, spot2Y);

        Spot tempSpot;

        if(spotX.getRoom() == spotY.getRoom()){
            return true;
        }
        else{
            if(spotX.hasNorthDoor()){
                tempSpot = getSpotByIndex(spot1X, spot1Y - 1);
                if(tempSpot.getRoom() == spotY.getRoom())
                    return true;
            }
            if(spotX.hasEastDoor()){
                tempSpot = getSpotByIndex(spot1X + 1, spot1Y);
                if(tempSpot.getRoom() == spotY.getRoom())
                    return true;
            }
            if(spotX.hasSouthDoor()){
                tempSpot = getSpotByIndex(spot1X, spot1Y + 1);
                if(tempSpot.getRoom() == spotY.getRoom())
                    return true;
            }
            if(spotX.hasWestDoor()){
                tempSpot = getSpotByIndex(spot1X - 1, spot1Y);
                if(tempSpot.getRoom() == spotY.getRoom())
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns the spot indexed by these coordinates
     * @param x X coordinate
     * @param y Y coordinate
     * @return The spot
     */
    public Spot getSpotByIndex(int x, int y){ return map[x][y]; }


    /**
     * Moves the given player to the new spot
     * @param player the player to move
     * @param newX the new x coord
     * @param newY the new y coord
     */
    public void movePlayer(String player, int newX, int newY){ return; }

    /**
     * Refills this spot
     * @param x the x coord
     * @param y the y coord
     * @param objToAdd The object to add
     */
    public void refill(int x,int y, Object objToAdd){
        map[x][y].refill(objToAdd);
    }

    /**
     * Gives whatever is on the spot to a given player
     * @param x the x coord
     * @param y the y coord
     * @param p the player
     */
    public void grabSomething(int x, int y, Player p) {
        map[x][y].grabSomething(p);
    }

    /**
     * Is this an ammo spot?
     * @param x x coord
     * @param y y coord
     * @return true if this is an ammo spot
     */
    public boolean isAmmoSpot(int x, int y){
        return map[x][y].isAmmoSpot();
    }

    /**
     * Is this a spawn spot?
     * @param x the x coord
     * @param y the y coord
     * @return true if this is a spawn spot
     */
    public boolean isSpawnSpot(int x, int y){
        return map[x][y].isSpawnSpot();
    }


}
