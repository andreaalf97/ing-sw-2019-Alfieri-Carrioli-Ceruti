package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.Player;

public class Map {

    /**
     * The matrix representing the map
     */
    public Spot[][] map;

    /**
     * set an existing spot in the map with the param spot
     * @param x is the x of the spot to replace
     * @param y is the y of the spot to replace
     * @param spot is the spot that replace the one corresponding to x and y
     */
    public void setSpot(int x,int y,Spot spot){
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
     * Return the spot where the selected player is
     * @param player the player to find
     */
    private int[] getPlayerSpot(String player){
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                if(map[i][j].playerHere(player)){
                    int[] returnValue = new int[2];
                    returnValue[0] = i;
                    returnValue[1] = j;
                    return returnValue;
                }
        return null;
    }


    /**
     * Moves the given player to the new spot
     * @param player the player to move
     * @param newX the new x coord
     * @param newY the new y coord
     */
    public void movePlayer(String player, int newX, int newY){
        removePlayerFromMap(player);
        map[newX][newY].addPlayer(player);
    }

    /**
     * Removes the player from the map by removing their name from the spot
     * @param player The player to remove
     */
    private void removePlayerFromMap(String player){
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                map[i][j].removePlayer(player);
    }

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

    /**
     * Return all the spots where the player can move with nMoves
     * @param player the player's nickname
     * @param nMoves The amount of moves available
     * @return a matrix[3][4] of boolean
     */
    public boolean[][] wherePlayerCanMove(String player, int nMoves){
        int[] playerSpot = getPlayerSpot(player);

        int playerSpotX = playerSpot[0];
        int playerSpotY = playerSpot[1];

        boolean[][] temp = new boolean[4][3];

        for(int i = 0; i < temp.length; i++)    //for every row
            for(int j = 0; j < temp[i].length; j++) //for every column in the row
                temp[i][j] = false; //initialize to false

        for(int i = 0; i < temp.length; i++) //for every row
            for(int j = 0; j < temp[i].length; j++) //for every column
                if(canMoveFromTo(playerSpotX, playerSpotY, i, j, nMoves)) //if the player can move from his spot to the <i, j> spot
                    temp[i][j] = true; //temp<i, j> is true --> the player can move there

        return temp;
    }

    /**
     * Tells you if a player in spot1 can move to spot2 with a certain amount of moves
     * @param spot1X spot 1 X coord
     * @param spot1Y spot 1 Y coord
     * @param spot2X spot 2 X coord
     * @param spot2Y spot 2 Y coord
     * @param nMoves How many moves the player can use
     * @return true if the player can move from spot1 to spot2 in nMoves
     */
    private boolean canMoveFromTo(int spot1X, int spot1Y, int spot2X, int spot2Y, int nMoves) throws IllegalArgumentException{
        if(spot1X < 0 || spot1X > 4 || spot1Y < 0 || spot1Y > 3 || spot2X < 0 || spot2X > 4 || spot2Y < 0 || spot2Y > 3)
            throw new IllegalArgumentException("This is not a valid spot");

        if(spot1X == spot2X && spot1Y == spot2Y)
            return true;

        if(nMoves == 0)
            return false;

        Spot spot1 = getSpotByIndex(spot1X, spot1Y);

        boolean north = false, east = false, west = false, south = false;

        if(spot1.hasNorthDoor())
            north = canMoveFromTo(spot1X, spot1Y - 1, spot2X, spot2Y, nMoves - 1);

        if(spot1.hasEastDoor())
            east = canMoveFromTo(spot1X + 1, spot1Y, spot2X, spot2Y, nMoves - 1);

        if(spot1.hasSouthDoor())
            south = canMoveFromTo(spot1X, spot1Y + 1, spot2X, spot2Y, nMoves - 1);

        if(spot1.hasWestDoor())
            west = canMoveFromTo(spot1X - 1, spot1Y, spot2X, spot2Y, nMoves - 1);

        return north || east || south || west;
    }



}
