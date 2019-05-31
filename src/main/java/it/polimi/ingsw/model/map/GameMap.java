package it.polimi.ingsw.model.map;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.cards.PowerUpDeck;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.cards.WeaponDeck;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Random;

public class GameMap {

    /**
     * The matrix representing the map
     */
    protected  Spot[][] map;

    private static Random rand;

    /**
     * creates a gameMap by reading it from json
     * @param jsonMap the map to create
     */
    public GameMap(JsonObject jsonMap){
        this.rand = new Random();

        Spot[][] spotMatrix = new Spot[3][4];

        for(int i = 0; i < spotMatrix.length; i++) {
            JsonObject jsonRow = jsonMap.get("row" + i).getAsJsonObject();

            for (int j = 0; j < spotMatrix[i].length; j++) {
                JsonObject jsonSpot = jsonRow.get("col" + j).getAsJsonObject().get("spot").getAsJsonObject(); // <== jsonSpot

                if(!jsonSpot.toString().equals("{}")){ //appena sono sicuro di avere uno spot valido nel json posso istanziare uno spawnspot o un ammo spot

                    if(jsonSpot.get("ammoColorList") != null){
                        //devo istanziare un AmmoSpot
                        spotMatrix[i][j] = new AmmoSpot(jsonSpot);
                    }
                    else //devo istanziare uno spawnSpot
                        spotMatrix[i][j] = new SpawnSpot(jsonSpot);

                }

            }
        }

        this.map = spotMatrix;


    }

    /**
     * Only constructor
     * @param spotMatrix the matrix corresponding to gameMap.map
     */
    protected GameMap(Spot[][] spotMatrix){
        this.map = spotMatrix;
        this.rand = new Random();
    }

    /**
     * creates a GameMap by parsing the gameMap parameter
     * @param gameMap the map to copy
     */
    public GameMap(GameMap gameMap){

        this.map = new Spot[3][4];

        for(int i = 0; i < gameMap.map.length; i++) {
            for (int j = 0; j < gameMap.map[i].length; j++) {
                if(gameMap.map[i][j] != null)
                    this.map[i][j] = new Spot(gameMap.map[i][j]);
                else
                    this.map[i][j] = null;
            }
        }

        this.rand = new Random();
    }

    @Override
    public GameMap clone(){
        return new GameMap(this.map.clone());
    }
    //TESTED
    /**
     * Returns the spot indexed by these coordinates
     * @param x X coordinate
     * @param y Y coordinate
     * @return The spot
     */
    public Spot getSpotByIndex(int x, int y){ return map[x][y]; }

    //TESTED
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
        if( spotX != null && spotY != null) {
            if (spotX.getRoom() == spotY.getRoom()) {
                return true;
            } else {
                if (spotX.hasNorthDoor()) {
                    tempSpot = getSpotByIndex(spot1X - 1, spot1Y);
                    if (tempSpot.getRoom() == spotY.getRoom())
                        return true;
                }
                if (spotX.hasEastDoor()) {
                    tempSpot = getSpotByIndex(spot1X, spot1Y + 1);
                    if (tempSpot.getRoom() == spotY.getRoom())
                        return true;
                }
                if (spotX.hasSouthDoor()) {
                    tempSpot = getSpotByIndex(spot1X + 1, spot1Y);
                    if (tempSpot.getRoom() == spotY.getRoom())
                        return true;
                }
                if (spotX.hasWestDoor()) {
                    tempSpot = getSpotByIndex(spot1X, spot1Y - 1);
                    if (tempSpot.getRoom() == spotY.getRoom())
                        return true;
                }
            }

            return false;
        }

        return false;
    }

    //TESTED
    /**
     * Return the coordinates of the spot where the selected player is
     * @param player the player to find
     * @return an array where [0] = X, [1] = Y
     */
    public int[] getPlayerSpotCoord(String player){
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                if(map[i][j] != null && map[i][j].playerHere(player)){
                    int[] returnValue = new int[2];
                    returnValue[0] = i;
                    returnValue[1] = j;
                    return returnValue;
                }

        //a this point i am sure that player isn't on map
        return null;
    }

    //TESTED
    /**
     * Moves the given player to the new spot
     * @param player the player to move
     * @param newX the new x coord
     * @param newY the new y coord
     */
    public void movePlayer(String player, int newX, int newY){

        if(newX < 0 || newX > 2 || newY < 0 || newY > 3)
            throw new IllegalArgumentException("x or y are out of game matrix");

        if(playerIsOnMap(player))
            removePlayerFromMap(player);


        map[newX][newY].addPlayer(player);
    }

    //TESTED - PRIVATE METHOD
    /**
     * private methods, it controls if player is on map
     * @param player the player to check
     * @return true if player is present on map
     */
    private boolean playerIsOnMap(String player) {
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                if(map[i][j] != null && map[i][j].playerHere(player))
                    return true;

        return false;
    }

    //TESTED - PRIVATE METHOD
    /**
     * Removes the player from the map by removing their name from the spot, e.g. when the player dies
     * @param player The player to remove
     */
    private void removePlayerFromMap(String player){
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                if (map[i][j] != null)
                    map[i][j].removePlayer(player);
    }

    //TESTED
    /**
     * Gives whatever is on the spot to a given player
     * @param x the x coord
     * @param y the y coord
     * @param p the player
     */
    public void grabSomething(int x, int y, Player p, int index) {
        map[x][y].grabSomething(p, index);
    }

    //TESTED
    /**
     * Is this an ammo spot?
     * @param x x coord
     * @param y y coord
     * @return true if this is an ammo spot
     */
    public boolean isAmmoSpot(int x, int y){
        return map[x][y].isAmmoSpot();
    }

    //TESTED
    /**
     * Is this a spawn spot?
     * @param x the x coord
     * @param y the y coord
     * @return true if this is a spawn spot
     */
    public boolean isSpawnSpot(int x, int y){
        return map[x][y].isSpawnSpot();
    }

    //TESTED
    /**
     * Return all the spots where the player can move with nMoves
     * @param player the player's nickname
     * @param nMoves The amount of moves available
     * @return a matrix[3][4] of boolean
     */
    public boolean[][] wherePlayerCanMove(String player, int nMoves) throws RuntimeException{

        boolean[][] temp = new boolean[3][4];
        booleanMatrixInizialisation(temp);


        int[] playerSpot = getPlayerSpotCoord(player); //This array contains the X coord in 0 and the Y coord in

        if ( playerSpot == null)
            throw new RuntimeException("This array shouldn't be null, the player isn't on map");

        int playerSpotX = playerSpot[0]; //The X coord
        int playerSpotY = playerSpot[1]; //The Y coord

        //Check all spots
        for(int i = 0; i < temp.length; i++)
            for(int j = 0; j < temp[i].length; j++) {
                //for every spot
                if (validSpot(i, j)) {
                    if (canMoveFromTo(playerSpotX, playerSpotY, i, j, nMoves)) //if the player can move from his spot to the <i, j> spot
                        temp[i][j] = true; //temp<i, j> is true --> the player can move there
                }
            }

        return temp;
    }

    //TESTED
    /**
     * initialise the boolean matrix in which we will have the spots where player can do actions
     * @param temp the matrix representing the gameMap
     */
    private void booleanMatrixInizialisation(boolean [][] temp){

        //Initialize the matrix to false for clearness
        for(int i = 0; i < temp.length; i++)
            for(int j = 0; j < temp[i].length; j++) //for every spot
                temp[i][j] = false; //initialize to false

    }

    //TESTED
    /**
     * Tells you if a player in spot1 can move to spot2 with a certain amount of moves
     * @param spot1X spot 1 X coord
     * @param spot1Y spot 1 Y coord
     * @param spot2X spot 2 X coord
     * @param spot2Y spot 2 Y coord
     * @param nMoves How many moves the player can use
     * @return true if the player can move from spot1 to spot2 in nMoves
     */
    public boolean canMoveFromTo(int spot1X, int spot1Y, int spot2X, int spot2Y, int nMoves) throws  RuntimeException {
        if(spot1X < 0 || spot1X > 2 || spot1Y < 0 || spot1Y > 3 || spot2X < 0 || spot2X > 2 || spot2Y < 0 || spot2Y > 3 || !validSpot(spot1X, spot1Y) || !validSpot(spot2X, spot2Y))
            throw new RuntimeException("This is not a valid spot");

        if(spot1X == spot2X && spot1Y == spot2Y)
            return true;

        if(nMoves == 0)
            return false;

        if(nMoves > 10)
            return true;

        Spot spot1 = getSpotByIndex(spot1X, spot1Y);

        //North == true if the player can move north
        boolean north = false, east = false, west = false, south = false;

        if(spot1.hasNorthDoor() ||  (isSameRoom(spot1X , spot1Y, spot1X - 1 , spot1Y)  ))
            north = canMoveFromTo(spot1X - 1, spot1Y , spot2X, spot2Y, nMoves - 1);

        if(spot1.hasEastDoor() ||   (isSameRoom(spot1X, spot1Y , spot1X , spot1Y + 1) ))
            east = canMoveFromTo(spot1X , spot1Y + 1, spot2X, spot2Y, nMoves - 1);

        if(spot1.hasSouthDoor() ||   (isSameRoom(spot1X , spot1Y , spot1X + 1, spot1Y) ))
            south = canMoveFromTo(spot1X + 1, spot1Y , spot2X, spot2Y, nMoves - 1);

        if(spot1.hasWestDoor() ||   (isSameRoom(spot1X, spot1Y , spot1X , spot1Y - 1)))
            west = canMoveFromTo(spot1X , spot1Y - 1, spot2X, spot2Y, nMoves - 1);

        return north || east || south || west; // ==> If there is at least one path throught one of these doors
    }

    //TESTED - PRIVATE METHOD
    /**
     * tells if the spot individuated with the two couple of coordinates are in the same room
     * @param x1 spot 1 x
     * @param y1 spot 1 y
     * @param x2 spot 2 x
     * @param y2 spot 2 y
     * @return true if spot are in the same room
     */
    private boolean isSameRoom(int x1, int y1, int x2, int y2) {
        if(x1 < 0 || x1 > 2 )
            return false;
        if(x2 < 0 || x2 > 2 )
            return false;
        if (y1 < 0 || y1 > 3)
            return false;
        if (y2 < 0 || y2 > 3)
            return false;

        if (map[x1][y1] != null && map[x2][y2] != null && getSpotByIndex(x1,y1).getRoom() == getSpotByIndex(x2, y2).getRoom())
            return true;
        return false;

    }

    //TESTED
    /**
     * check if the spot is valid
     * @param x spot x
     * @param y spot y
     * @return true if spot is valid and exists
     */
    public boolean validSpot(int x, int y) {
        if (x < 0 || x > 2 || y < 0 || y > 3 || map[x][y] == null)
            return false;
        return true;
    }

    //TESTED
    /**
     * return all the spots where the player can move and grab something
     * @param player the player who moves
     * @param nMoves the moves that player can do
     * @return a boolean matrix representing the gameMap
     */
    public boolean[][] wherePlayerCanMoveAndGrab(String player, int nMoves) {
        int[] playerSpot = getPlayerSpotCoord(player); //This array contains the X coord in 0 and the Y coord in 1

        if(playerSpot == null)
            throw new RuntimeException("This array shouldn't be null");

        int playerSpotX = playerSpot[0]; //The X coord
        int playerSpotY = playerSpot[1]; //The Y coord

        boolean[][] temp = new boolean[3][4]; //The matrix I will return once completed
        booleanMatrixInizialisation(temp);

        //Check all spots
        for(int i = 0; i < temp.length; i++)
            for(int j = 0; j < temp[i].length; j++) //for every spot
                if (validSpot(i, j) && isAmmoSpot(i, j)) {
                    if (canMoveFromTo(playerSpotX, playerSpotY, i, j, nMoves) && !emptySpot(i, j)) //if the player can move from his spot to the <i, j> spot
                        temp[i][j] = true; //temp<i, j> is true --> the player can move there
                }

        return temp;
    }

    //TESTED - PRIVATE METHOD
    /**
     * tells if the spot is empty
     * @param i spot x
     * @param j spot y
     * @return true if spot is empty
     */
    private boolean emptySpot(int i, int j) {

        if(map[i][j] == null)
            throw new IllegalArgumentException("This spot does not exist -- is null!");

        return map[i][j].emptySpot();
    }

    //TESTED
    /**
     * RESPAWN METHOD!! this method is called by the game in the method respawn and it moves the player to the spawnspot of the same color as the discarded powerup
     * @param player the player that has to respawn
     * @param discardedColor the color of the spawnspot
     */
    public void movePlayerToColorSpawn(String player, Color discardedColor) {

        for(int i = 0; i < this.map.length; i++)
            for(int j = 0; j < this.map[i].length; j++)
                if(this.map[i][j] != null && this.map[i][j].isSpawnSpot() && this.map[i][j].getColor() == discardedColor){
                    this.map[i][j].addPlayer(player);
                    return;
                }

    }

    //TESTED
    /**
     * Refills this spot
     * @param x the x coord
     * @param y the y coord
     * @param objToAdd The object to add
     */
    public void refill(int x,int y, Object objToAdd){
        map[x][y].refill(objToAdd);
    }

    //TESTED
    /**
     * This method refills all the ammo spots
     * @param powerupDeck the deck to draw a card from
     */
    public void refillAllAmmo(PowerUpDeck powerupDeck) {
        for(int i = 0; i < this.map.length; i++)
            for(int j = 0; j < this.map[i].length; j++)
                if(map[i][j] != null && this.map[i][j].isAmmoSpot()){

                    //If this is not full, I refill it
                    //The refill method for ammo spots automatically clear all the arrays
                    if(!this.map[i][j].isFull()) {
                        if (rand.nextBoolean()) {
                            //It also receives a powerup

                            this.map[i][j].refill(powerupDeck.drawCard());
                        } else {
                            //It doesn't receive a powerup
                            this.map[i][j].refill(null);
                        }
                    }


                }
    }

    //TESTED
    /**
     * Refills all the spawn spots
     * @param weaponDeck the weapon deck to draw cards from
     */
    public void refillAllSpawns(WeaponDeck weaponDeck) {

        for(int i = 0; i < this.map.length; i++)
            for(int j = 0; j < this.map[i].length; j++)
                if(map[i][j] != null && map[i][j].isSpawnSpot()){

                    //Adds a new weapon until the spot is full
                    while(!this.map[i][j].isFull())
                        this.map[i][j].refill(weaponDeck.drawCard());

                }
    }

    //TESTED
    /**
     * show the weapons in the spawn spot associated to that color
     * @param roomColor the color of the spawnspot
     * @return the weapon list of the spawnspot
     */
    public ArrayList<Weapon> showSpawnSpotWeapons(Color roomColor) {

        Spot s = getSpawnSpotByRoomColor(roomColor);

        return s.getSpawnWeapons();
    }

    //TESTED - PRIVATE METHOD
    /**
     * tells the spawnspot associated to the color
     * @param roomColor spawnspot color
     * @return the Spawnspot
     */
    private Spot getSpawnSpotByRoomColor(Color roomColor){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(validSpot(i,j) && map[i][j].isSpawnSpot() && map[i][j].getColor() == roomColor) {
                        return map[i][j];
                }
            }
        }

        return null;
    }

    //TESTED
    /**
     * return the room where the player is
     * @param nickname the nickname of the player
     * @return the room where the player is if he present on the map, else null
     */
    public Room getPlayerRoom(String nickname){

        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                if(map[i][j] != null && map[i][j].playerHere(nickname)){
                    return map[i][j].getRoom();
                }

        return null;
    }

    /**
     * Returns the distance between two spots
     * @return the distance
     */
    public int distance(int x1, int y1, int x2, int y2){

        ArrayList<int[]> queue = new ArrayList<>();

        int[] temp = {x1, y1};
        queue.add(temp);

        boolean[][] visited = new boolean[3][4];
        for(int i = 0; i < visited.length; i++)
            for(int j = 0; j < visited[i].length; j++)
                visited[i][j] = false;

        int[][] distance = new int[3][4];
        for(int i = 0; i < distance.length; i++)
            for(int j = 0; j < distance[i].length; j++)
                distance[i][j] = 500;

        distance[x1][y1] = 0;

        while(!queue.isEmpty()){
            temp = queue.remove(0);

            int tempX = temp[0];
            int tempY = temp[1];

            visited[tempX][tempY] = true;

            if(distance[tempX][tempY] > distance[x2][y2])
                continue;

            if(validSpot(tempX-1, tempY) && canMoveFromTo(tempX, tempY, tempX-1, tempY, 1) && !visited[tempX - 1][tempY]){
                if(distance[tempX][tempY] + 1 < distance[tempX - 1][tempY])
                    distance[tempX - 1][tempY] = distance[tempX][tempY] + 1;
                int[] newTemp = new int[2];
                newTemp[0] = tempX - 1;
                newTemp[1] = tempY;
                queue.add(newTemp);
            }

            if(validSpot(tempX, tempY+1) && canMoveFromTo(tempX, tempY, tempX, tempY + 1, 1) && !visited[tempX][tempY + 1]){
                if(distance[tempX][tempY] + 1 < distance[tempX][tempY + 1])
                    distance[tempX][tempY + 1] = distance[tempX][tempY] + 1;
                int[] newTemp = new int[2];
                newTemp[0] = tempX;
                newTemp[1] = tempY + 1;
                queue.add(newTemp);
            }

            if(validSpot(tempX+1, tempY) && canMoveFromTo(tempX, tempY, tempX+1, tempY, 1) && !visited[tempX + 1][tempY]){
                if(distance[tempX][tempY] + 1 < distance[tempX + 1][tempY])
                    distance[tempX + 1][tempY] = distance[tempX][tempY] + 1;
                int[] newTemp = new int[2];
                newTemp[0] = tempX + 1;
                newTemp[1] = tempY;
                queue.add(newTemp);
            }

            if(validSpot(tempX, tempY-1) && canMoveFromTo(tempX, tempY, tempX, tempY-1, 1) && !visited[tempX][tempY - 1]){
                if(distance[tempX][tempY] + 1 < distance[tempX][tempY - 1])
                    distance[tempX][tempY - 1] = distance[tempX][tempY] + 1;
                int[] newTemp = new int[2];
                newTemp[0] = tempX;
                newTemp[1] = tempY - 1;
                queue.add(newTemp);
            }

        }

        return distance[x2][y2];

    }

    public int indexOfWeapon(int x, int y, String weaponName) {

        if(!isSpawnSpot(x, y))
            throw new RuntimeException("This is not a spawn spot");

        SpawnSpot spot = (SpawnSpot)getSpotByIndex(x, y);

        return spot.indexOfWeapon(weaponName);

    }

    public void addWeaponToSpawn(int x, int y, Weapon weapon) {

        if(!isSpawnSpot(x, y))
            throw new RuntimeException("This is not a spawn spot");

        SpawnSpot spawnSpot = (SpawnSpot)getSpotByIndex(x, y);

        spawnSpot.addWeapon(weapon);

    }

    /**
     * Grabs the given weapon from the spot and gives it to the player
     * @param x
     * @param y
     * @param p
     * @param weaponToPick
     */
    public void grabWeapon(int x, int y, Player p, String weaponToPick) {

        Spot s = getSpotByIndex(x, y);

        if(s.isAmmoSpot())
            throw new RuntimeException("This player is on an ammo spot");

        int indexOfWeapon = s.getSpawnWeaponNames().indexOf(weaponToPick);

        s.grabSomething(p, indexOfWeapon);

    }

    public void removePlayer(String nickname) {

        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                if(map[i][j] != null && map[i][j].playersHere.contains(nickname)) {
                    map[i][j].removePlayer(nickname);
                    return;
                }
    }
}
