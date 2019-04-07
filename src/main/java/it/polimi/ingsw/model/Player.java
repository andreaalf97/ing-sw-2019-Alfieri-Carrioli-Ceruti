package it.polimi.ingsw.model;

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

    public Player(String nickname){
        this(nickname, -1, -1);
    }

    public void setDamages(ArrayList<String> damages){ this.damages = damages; }
    public ArrayList<String> getDamages(){ return this.damages; }

    public void setMarks(ArrayList<String> marks){ this.marks = marks; }
    public ArrayList<String> getMarks(){ return this.marks; }

    public void setPoints(int points){ this.points = points; }
    public int getPoints(){ return this.points; }

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
    public void giveDamage(String s,int i){                    /*assign damage to this.player*/
        for(int j = 0; j < i; j++){
            damages.add(s);
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
    public void giveMarks(String s,int i) throws IllegalArgumentException {
        /*assign marks to this player*/
        if(i <= 0 || i > 12) {
            throw new IllegalArgumentException("i can only be > 0 && < 12");
        }
        for(int j = 0; j < i; j++)
            marks.add(s);
    }

    //TESTED
    public void givePoints(int n) throws IllegalArgumentException {
        if(n < 0)
            throw new IllegalArgumentException("n can't be negative");
        points += n;
    }

    public boolean isDead(){return this.isDead;}
}
