package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Player {
    /**
     * The nickname of the player
     */
    private String nickname;

    /**
     * Amount of red active ammos
     */
    private int nRedAmmo;

    /**
     * Amount of blue active ammos
     */
    private int nBlueAmmo;

    /**
     * Amount of yellow active ammos
     */
    private int nYellowAmmo;

    /**
     * Amount of points acquired by playing
     */
    private int points;

    /**
     * The list of the player's weapons
     */
    private ArrayList<Weapon> weaponList;

    /**
     * The list of the player's powerups
     */
    private ArrayList<PowerUp> powerUpList;

    /**
     * The list of player's nickanames who hurt this player
     */
    private ArrayList<String> damages;

    /**
     * The list of player's nicknames who marked this player
     */
    private ArrayList<String> marks;

    /**
     * The amount of deaths of this player in this game
     */
    private int nDeaths;

    /**
     * The player's X spot coordinate
     */
    private int xPosition;

    /**
     * The player's Y spot coordinate
     */
    private int yPosition;

    /**
     * True if the player is currently dead
     */
    private boolean isDead;

    /**
     * Amount of moves this player can do before grabbing (increases as the damages increase)
     */
    private int nMovesBeforeGrabbing;

    /**
     * Amount of moves this player can do before shooting (increases as the damages increase)
     */
    private int nMovesBeforeShooting;




    /**
     * Constructor
     * @param nickname Player's nickname
     * @param xPosition Initial spot x
     * @param yPosition Initial spot y
     */
    public Player(String nickname, int xPosition, int yPosition){
        this.nickname = nickname;
        this.nRedAmmo = 1;
        this.nYellowAmmo = 1;
        this.nBlueAmmo = 1;
        this.points = 0;
        this.weaponList = new ArrayList<>(3);
        this.powerUpList = new ArrayList<>(3);
        this.damages = new ArrayList<>(12);
        this.marks = new ArrayList<>(12);
        this.nDeaths = 0;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.isDead = true;
        this.nMovesBeforeGrabbing = 1;
        this.nMovesBeforeShooting = 0;
    }

    /**
     * Constructs a new Player with -1 as X and Y position
     * @param nickname Player's nickname
     */
    public Player(String nickname){
        this(nickname, -1, -1);
    }

    //SETS AND GETS
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------*/
    protected void setIsDead(boolean isDead){ this.isDead = isDead; }
    protected void setDamages(ArrayList<String> damages){ this.damages = damages; }
    protected ArrayList<String> getDamages(){ return this.damages; }
    protected void setMarks(ArrayList<String> marks){ this.marks = marks; }
    protected ArrayList<String> getMarks(){ return this.marks; }
    protected void setPoints(int points){ this.points = points; }
    protected int getPoints(){ return this.points; }
    protected String getNickname() {
        return nickname;
    }
    public int getnRedAmmo() {
        return nRedAmmo;
    }
    protected void setnRedAmmo(int nRedAmmo) {
        this.nRedAmmo = nRedAmmo;
    }
    public int getnBlueAmmo() {
        return nBlueAmmo;
    }
    protected void setnBlueAmmo(int nBlueAmmo) {
        this.nBlueAmmo = nBlueAmmo;
    }
    public int getnYellowAmmo() {
        return nYellowAmmo;
    }
    protected void setnYellowAmmo(int nYellowAmmo) {
        this.nYellowAmmo = nYellowAmmo;
    }
    protected ArrayList<Weapon> getWeaponList() {
        return weaponList;
    }
    public ArrayList<PowerUp> getPowerUpList() {
        return new ArrayList<>(powerUpList);
    }
    protected int getnDeaths() {
        return nDeaths;
    }
    protected int getxPosition() {
        return xPosition;
    }
    protected int getyPosition() {
        return yPosition;
    }
    protected int getnMovesBeforeGrabbing() {
        return nMovesBeforeGrabbing;
    }
    protected int getnMovesBeforeShooting() {
        return nMovesBeforeShooting;
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------*/

    //TESTED
    /**
     * Return the list of offenders nicknames ordered by the amount of damages
     * @return The list of offenders
     */
    public ArrayList<String> getOffendersRanking(){
        if(damages.isEmpty())
            return new ArrayList<>();

        ArrayList<String> tempNames = new ArrayList<>();
        int[] ricorrenze = new int[12];
        //TODO Do this with an HashMap like in KillShotTrack.getRanking()
        for(int i = 0; i < 12; i++)
            ricorrenze[i] = 0;

        Iterator i = damages.iterator();

        while(i.hasNext()){
            String t = (String)i.next();
            if(tempNames.contains(t)){
                ricorrenze[tempNames.indexOf(t)]++;
            }
            else{
                tempNames.add(t);
                ricorrenze[tempNames.indexOf(t)]++;
            }
        }

        return tempNames;
    }        /*return players who gave damages to this.player in order(in 0 highest damage)*/

    //TESTED
    /**
     * Gives the required amount of damages to this player
     * @param player The offender's nickname
     * @param nDamages The amount of damages
     */
    public void giveDamage(String player,int nDamages){
        for(int j = 0; j < nDamages; j++){
            damages.add(player);
        }
    }

    //TESTED
    /**
     * Returns true only if a player can give n marks to this player.
     * (Every player can only have 3 marks by another player)
     * @param player The offender
     * @param nMarks The amount of marks
     * @return true if player is allowed
     */
    public boolean canGiveMarks(String player, int nMarks){
        Iterator i = this.marks.iterator();
        String temp;
        int counter = 0;

        while(i.hasNext()){
            temp = (String)i.next();
            if(temp.equals(player))
                counter++;
        }

        return (counter + nMarks <= 3);
    }

    //TESTED
    /**
     * Gives marks to this player
     * @param player the offender
     * @param nMarks the amount of marks
     */
    //TODO test the exception
    public void giveMarks(String player,int nMarks)  {
        /*assign marks to this player*/
        if(nMarks <= 0 || nMarks > 12) {
            throw new RuntimeException("i can only be > 0 && < 12");
        }
        for(int j = 0; j < nMarks; j++)
            marks.add(player);
    }

    //TESTED
    /**
     * Gives points to this player
     * @param n The amount of points
     */
    public void givePoints(int n)  {
        if(n < 0)
            throw new RuntimeException("n can't be negative");
        points += n;
    }

    //TESTED
    /**
     * True if this player is dead
     * @return true if this player is dead
     */
    public boolean isDead(){return this.isDead;}

    //TESTED
    /**
     * Adds the weapon to the player's weaponList
     * @param weapon Weapon to add
     */
    public void giveWeapon(Weapon weapon){
        if(weaponList.size() > 2)
            throw new RuntimeException("This player can't receive any more weapons");
        weaponList.add(weapon);
    }

    //TESTED
    /**
     * Gives ammos to this player
     * @param ammos An array of Colors to be given to this player
     */
    public void giveAmmos(ArrayList<Color> ammos){
        Iterator i = ammos.iterator();

        while(i.hasNext()){
            Color temp = (Color)i.next();
            if(temp == Color.RED){
                if(nRedAmmo < 3) nRedAmmo++;
            }
            else if(temp == Color.BLUE){
                if(nBlueAmmo < 3) nBlueAmmo++;
            }
            else if(temp == Color.YELLOW){
                if(nYellowAmmo < 3) nYellowAmmo++;
            }
        }
    }

    //TESTED
    /**
     * Gives a powerup to this player
     * @param powerup the powerup to be given
     */
    public void givePowerUp(PowerUp powerup){

        if(powerUpList.size() > 2)
            throw new RuntimeException("This player already has 3 power up");

        powerUpList.add(powerup);
    }

    //TESTED
    /**
     * Discards a powerup by the index and return its color
     * @param powerUpIndexToDiscard the index of the powerup in the array
     * @return the color of the powerup
     */
    public Color discardPowerUpByIndex(int powerUpIndexToDiscard) {

        if(powerUpIndexToDiscard < 0 || powerUpIndexToDiscard >= this.powerUpList.size())
            throw new RuntimeException("powerUpIndexToDiscard is out of bound");

        //Extracts the powerup from the list by its index
        PowerUp powerUpToDiscard = this.powerUpList.get(powerUpIndexToDiscard);

        //Reads the color from this powerup
        Color returnColor = powerUpToDiscard.getColor();

        //Removing the powerup from the list
        this.powerUpList.remove(powerUpToDiscard);

        return returnColor;
    }

    //TESTED
    public void revive() {
        if(!this.isDead)
            throw new RuntimeException("This player needs to be dead to be revived!");

        this.isDead = false;
    }

    //TESTED
    /**
     * Kills this player if he's not already dead
     */
    public void kill(){
        if(isDead == true)
            throw new RuntimeException("This player is already dead!");
        this.isDead = true;
    }

    //TODO test
    /**
     * Tells if the player has a turn power up in his hand
     */
    public boolean hasTurnPowerup() {

        for(PowerUp i : powerUpList)
            if(i.isTurnPowerup())
                return true;

        return false;
    }

    //TODO test
    /**
     * this method reload a weapon that i am sure i can reload, see reloadWeapons in controller.java
     * @param weaponToReload the weapon that the player wants to reload
     */
    public void reloadWeapon (Weapon weaponToReload){
        ArrayList<Color> cost = weaponToReload.getCost();

        for (int i = 0; i < this.getWeaponList().size(); i++) {

            if (weaponList.get(i).equals(weaponToReload)) { // this is the weapons that i want to reload

                //first I take the ammo from the player
                this.nBlueAmmo -= Collections.frequency(cost, Color.BLUE);
                this.nRedAmmo -= Collections.frequency(cost, Color.RED);
                this.nYellowAmmo -= Collections.frequency(cost, Color.YELLOW);

                //reload the weapon
                this.weaponList.get(i).reload(); //todo is this right?
            }
        }
    }

    protected void moveTo(int x, int y) {

        if(x < 0 || x > 3)
            throw new IllegalArgumentException("x is out of bound");

        if(y < 0 || y > 2)
            throw new IllegalArgumentException("y is out of bound");

        this.xPosition = x;
        this.yPosition = y;
    }
}
