package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Player {
    private String nickname;
    private boolean isDead;
    private int points;
    private int nRedAmmo;
    private int nBlueAmmo;
    private int nYellowAmmo;
    private int xPosition;
    private int yPosition;
    private ArrayList<Weapon> weaponList;
    private ArrayList<Powerup> powerupList;
    private ArrayList<String> damages;
    private ArrayList<String> marks;
    private int nDeaths;

    public Player(String nickname, int xPosition, int yPosition){
        this.nickname = nickname;
        this.isDead = false;
        this.points = 0;
        this.nRedAmmo = 1;
        this.nBlueAmmo = 1;
        this.nYellowAmmo = 1;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.weaponList = new ArrayList<Weapon>(3);
        this.powerupList = new ArrayList<Powerup>(3);
        this.damages = new ArrayList<String>(12);
        this.marks = new ArrayList<String>(12);
        this.nDeaths = 0;
    }

    public Player(String nickname){
        this(nickname, -1, -1);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public ArrayList<String> getDamages() {
        return damages;
    }

    public void setDamages(ArrayList<String> damages) {
        this.damages = damages;
    }

    public ArrayList<String> getMarks() {
        return marks;
    }

    public void setMarks(ArrayList<String> marks) {
        this.marks = marks;
    }

    public int getnDeaths() {
        return nDeaths;
    }

    public void setnDeaths(int nDeaths) {
        this.nDeaths = nDeaths;
    }

    public void checkDamage(){return;}                             /* Check if this.players has more than 12 damages*/

    public ArrayList<String> getOffendersRanking(){
        if(damages.isEmpty())
            return new ArrayList<String>();

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

    public boolean canAttack(Player p,Weapon w){return true;}           /* return true if this.player can attack player p*/

    public void giveDamage(String s,int i){                    /*assign damage to this.player*/
        for(int j = 0; j < i; j++){
            damages.add(s);
        }
    }

    public void giveMarks(String s,int i) throws IllegalArgumentException {
        /*assign marks to this player*/
        if(i <= 0 || i > 12) {
            throw new IllegalArgumentException("i can only be > 0 && < 12");
        }

        marks.add(s);
    }

    public void givePoints(int n) throws IllegalArgumentException {
        if(n < 0)
            throw new IllegalArgumentException("n can't be negative");
        points += n;
    }                         /*give points to this player*/
}
