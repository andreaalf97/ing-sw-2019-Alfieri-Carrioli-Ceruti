package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private String nickname;
    private boolean isDead;
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

    public Player (){}

    public void checkDamage(){}                             /* Check if this.players has more than 12 damages*/

    public Arraylist<String> getOffendersRanking(){}        /*return players who gave damages to this.player in order(in 0 highest damage)*/

    public boolean canAttack(Player p,Weapon w){}           /* return true if this.player can attack player p*/

    public void giveDamage(String s,int i){}                /*assign damage to this.player*/


    public void giveMarks(String s,int i){}                  /*assign marks to this.player*/

    public void givePoints(int n){}                         /*give points to this.player at the end of turn if the player dead has at least one damage from this.player*/




}
