package it.polimi.ingsw.model;

import it.polimi.ingsw.model.CardsPackage.Powerup;
import it.polimi.ingsw.model.CardsPackage.Weapon;

import java.util.ArrayList;
import java.util.Iterator;

public class Player {
    private String nickname;
    private int nRedAmmo;
    private int nBlueAmmo;
    private int nYellowAmmo;
    private int points;
    private ArrayList<Weapon> weaponList;
    private ArrayList<Powerup> powerupList;
    private ArrayList<String> damages;
    private ArrayList<String> marks;
    private int nDeaths;
    private int xPosition;
    private int yPosition;
    private boolean isDead;
    private int nMovesBeforeGrabbing;
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
        this.isDead = false;
        this.nMovesBeforeGrabbing = 1;
        this.nMovesBeforeShooting = 0;
    }

    /**
     * Second constructor
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
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
    public void setWeaponList(ArrayList<Weapon> weaponList) {
        this.weaponList = weaponList;
    }
    public ArrayList<Powerup> getPowerupList() {
        return powerupList;
    }
    public void setPowerupList(ArrayList<Powerup> powerupList) {
        this.powerupList = powerupList;
    }
    public int getnDeaths() {
        return nDeaths;
    }
    public void setnDeaths(int nDeaths) {
        this.nDeaths = nDeaths;
    }
    public int getxPosition() {
        return xPosition;
    }
    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }
    public int getyPosition() {
        return yPosition;
    }
    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
    public void setDead(boolean dead) {
        isDead = dead;
    }
    public int getnMovesBeforeGrabbing() {
        return nMovesBeforeGrabbing;
    }
    public void setnMovesBeforeGrabbing(int nMovesBeforeGrabbing) {
        this.nMovesBeforeGrabbing = nMovesBeforeGrabbing;
    }
    public int getnMovesBeforeShooting() {
        return nMovesBeforeShooting;
    }
    public void setnMovesBeforeShooting(int nMovesBeforeShooting) {
        this.nMovesBeforeShooting = nMovesBeforeShooting;
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------*/

    //TESTED
    public ArrayList<String> getOffendersRanking(){
        if(damages.isEmpty())
            return new ArrayList<>();

        ArrayList<String> tempNames = new ArrayList<>();
        int[] ricorrenze = new int[12];
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
    public void giveDamage(String player,int nDamages){                    /*assign damage to this.player*/
        for(int j = 0; j < nDamages; j++){
            damages.add(player);
        }
    }

    //TESTED
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
    //TODO test the exception
    public void giveMarks(String player,int nMarks) throws IllegalArgumentException {
        /*assign marks to this player*/
        if(nMarks <= 0 || nMarks > 12) {
            throw new IllegalArgumentException("i can only be > 0 && < 12");
        }
        for(int j = 0; j < nMarks; j++)
            marks.add(player);
    }

    //TESTED
    public void givePoints(int n) throws IllegalArgumentException {
        if(n < 0)
            throw new IllegalArgumentException("n can't be negative");
        points += n;
    }

    public boolean isDead(){return this.isDead;}

    /**
     * Adds the weapon to the player's weaponList
     * @param weapon Weapon to add
     */
    public void giveWeapon(Weapon weapon){
        weaponList.add(weapon);
    }

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

    public void givePowerup(Powerup powerup){
        if(powerupList.size() < 3) powerupList.add(powerup);
    }
}
