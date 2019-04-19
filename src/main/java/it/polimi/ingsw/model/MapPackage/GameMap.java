package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.CardsPackage.PowerupDeck;
import it.polimi.ingsw.model.CardsPackage.WeaponDeck;
import it.polimi.ingsw.model.Player;

import java.util.Random;

public class GameMap {

    /**
     * The matrix representing the map
     */
    private Spot[][] map;

    /**
     * Only constructor
     * @param spotMatrix
     */
    protected GameMap(Spot[][] spotMatrix){
        this.map = spotMatrix;
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
    private Spot getSpotByIndex(int x, int y){ return map[x][y]; }

    /**
     * Return the coordinates of the spot where the selected player is
     * @param player the player to find
     * @return an array where [0] = X, [1] = Y
     */
    private int[] getPlayerSpotCoord(String player){
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
     * Removes the player from the map by removing their name from the spot, e.g. when the player dies
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


    //TODO This method is obsolete --> The spawn are now refilled as we create them
    /**
     * Refills every spot on the map.
     * Should only be used initially when all spots are empty
     * @param weaponDeck the weaponDeck
     * @param powerupDeck the powerupDeck
     */
    public void refillAllWhenEmpty(WeaponDeck weaponDeck, PowerupDeck powerupDeck){

        for(int i = 0; i < this.map.length; i++)

            for(int j = 0; j < this.map[i].length; j++){ //For every spot in this map

                if(this.map[i][j] != null && this.map[i][j].isAmmoSpot()){ //if this is an ammo spot
                    Random rand = new Random();

                    if(rand.nextInt(1) == 0){
                        this.map[i][j].refill(powerupDeck.drawCard()); //Refills with a powerup
                    }
                    else{
                        this.map[i][j].refill(null); //Refills only ammos
                    }
                }


                if(this.map[i][j] != null && this.map[i][j].isSpawnSpot()){
                    this.map[i][j].refill(weaponDeck.pickCard());
                    this.map[i][j].refill(weaponDeck.pickCard());
                    this.map[i][j].refill(weaponDeck.pickCard());
                }

                //I am using 2 if statement in case we decide to add clean spots later (no ammo and no spawn)
            }
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

        int[] playerSpot = getPlayerSpotCoord(player); //This array contains the X coord in 0 and the Y coord in 1

        int playerSpotX = playerSpot[0]; //The X coord
        int playerSpotY = playerSpot[1]; //The Y coord

        boolean[][] temp = new boolean[4][3]; //The matrix I will return once completed

        //Initialize the matrix to false for clearness
        for(int i = 0; i < temp.length; i++)
            for(int j = 0; j < temp[i].length; j++) //for every spot
                temp[i][j] = false; //initialize to false

        //Check all spots
        for(int i = 0; i < temp.length; i++)
            for(int j = 0; j < temp[i].length; j++) //for every spot
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
    public boolean canMoveFromTo(int spot1X, int spot1Y, int spot2X, int spot2Y, int nMoves) throws IllegalArgumentException{
        if(spot1X < 0 || spot1X > 4 || spot1Y < 0 || spot1Y > 3 || spot2X < 0 || spot2X > 4 || spot2Y < 0 || spot2Y > 3)
            throw new IllegalArgumentException("This is not a valid spot");

        if(spot1X == spot2X && spot1Y == spot2Y)
            return true;

        if(nMoves == 0)
            return false;

        Spot spot1 = getSpotByIndex(spot1X, spot1Y);

        //North == true if the player can move north
        boolean north = false, east = false, west = false, south = false;

        if(spot1.hasNorthDoor())
            north = canMoveFromTo(spot1X, spot1Y - 1, spot2X, spot2Y, nMoves - 1);

        if(spot1.hasEastDoor())
            east = canMoveFromTo(spot1X + 1, spot1Y, spot2X, spot2Y, nMoves - 1);

        if(spot1.hasSouthDoor())
            south = canMoveFromTo(spot1X, spot1Y + 1, spot2X, spot2Y, nMoves - 1);

        if(spot1.hasWestDoor())
            west = canMoveFromTo(spot1X - 1, spot1Y, spot2X, spot2Y, nMoves - 1);

        return north || east || south || west; // ==> If there is at least one path throught one of these doors
    }


    public boolean[][] wherePlayerCanMoveAndGrab(String player, int nMoves) {
        int[] playerSpot = getPlayerSpotCoord(player); //This array contains the X coord in 0 and the Y coord in 1

        int playerSpotX = playerSpot[0]; //The X coord
        int playerSpotY = playerSpot[1]; //The Y coord

        boolean[][] temp = new boolean[4][3]; //The matrix I will return once completed

        //Initialize the matrix to false for clearness
        for(int i = 0; i < temp.length; i++)
            for(int j = 0; j < temp[i].length; j++) //for every spot
                temp[i][j] = false; //initialize to false

        //Check all spots
        for(int i = 0; i < temp.length; i++)
            for(int j = 0; j < temp[i].length; j++) //for every spot
                if(canMoveFromTo(playerSpotX, playerSpotY, i, j, nMoves) && !emptySpot(i, j)) //if the player can move from his spot to the <i, j> spot
                    temp[i][j] = true; //temp<i, j> is true --> the player can move there

        return temp;
    }

    private boolean emptySpot(int i, int j) {
        return map[i][j].emptySpot();
    }
}
