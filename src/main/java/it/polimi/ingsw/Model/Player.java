package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.CardsPackage.Powerup;
import it.polimi.ingsw.Model.CardsPackage.Weapon;

import java.util.ArrayList;
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
    private ArrayList<Powerup> powerupList;

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
        this.powerupList = new ArrayList<>(3);
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
    public void setDamages(ArrayList<String> damages){ this.damages = damages; }
    public ArrayList<String> getDamages(){ return this.damages; }
    public void setMarks(ArrayList<String> marks){ this.marks = marks; }
    public ArrayList<String> getMarks(){ return this.marks; }
    public void setPoints(int points){ this.points = points; }
    public int getPoints(){ return this.points; }
    public String getNickname() {
        return nickname;
    }
    public int getnRedAmmo() {
        return nRedAmmo;
    }
    public void setnRedAmmo(int nRedAmmo) {
        this.nRedAmmo = nRedAmmo;
    }
    public int getnBlueAmmo() {
        return nBlueAmmo;
    }
    public void setnBlueAmmo(int nBlueAmmo) {
        this.nBlueAmmo = nBlueAmmo;
    }
    public int getnYellowAmmo() {
        return nYellowAmmo;
    }
    public void setnYellowAmmo(int nYellowAmmo) {
        this.nYellowAmmo = nYellowAmmo;
    }
    public ArrayList<Weapon> getWeaponList() {
        return weaponList;
    }
    public ArrayList<Powerup> getPowerupList() {
        return new ArrayList<>(powerupList);
    }
    public int getnDeaths() {
        return nDeaths;
    }
    public int getxPosition() {
        return xPosition;
    }
    public int getyPosition() {
        return yPosition;
    }
    public int getnMovesBeforeGrabbing() {
        return nMovesBeforeGrabbing;
    }
    public int getnMovesBeforeShooting() {
        return nMovesBeforeShooting;
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------*/

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

    /**
     * Gives points to this player
     * @param n The amount of points
     */
    public void givePoints(int n)  {
        if(n < 0)
            throw new RuntimeException("n can't be negative");
        points += n;
    }

    /**
     * True if this player is dead
     * @return true if this player is dead
     */
    public boolean isDead(){return this.isDead;}

    /**
     * Adds the weapon to the player's weaponList
     * @param weapon Weapon to add
     */
    public void giveWeapon(Weapon weapon){
        weaponList.add(weapon);
    }

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

    /**
     * Gives a powerup to this player
     * @param powerup the powerup to be given
     */
    public void givePowerup(Powerup powerup){
        if(powerupList.size() < 3) powerupList.add(powerup);
    }

    /**
     * Discards a powerup by the index and return its color
     * @param powerupIndexToDiscard the index of the powerup in the array
     * @return the color of the powerup
     */
    public Color discardPowerupByIndex(int powerupIndexToDiscard) {

        //Extracts the powerup from the list by its index
        Powerup powerupToDiscard = this.powerupList.get(powerupIndexToDiscard);

        //Reads the color from this powerup
        Color returnColor = powerupToDiscard.getColor();

        //Removing the powerup from the list
        this.powerupList.remove(powerupToDiscard);

        return returnColor;
    }

    public void revive() {
        if(!this.isDead)
            throw new RuntimeException("This player needs to be dead to be revived!");

        this.isDead = false;
    }
}
