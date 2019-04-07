package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Game implements Runnable{
    private ArrayList<Player> players;
    private ArrayList<String> playerNames;
    private PowerupDeck powerupDeck;
    private WeaponDeck weaponDeck;
    private Board board;
    private String firstPlayer;
    private int numOfPlayers;

    //#####################################################################

    public Game(ArrayList<String> playerNames, String firstPlayer){
        this.players = new ArrayList<>(playerNames.size());
        this.playerNames = playerNames;
        this.powerupDeck = new PowerupDeck();
        this.weaponDeck = new WeaponDeck();
        this.board = new Board();
        this.firstPlayer = firstPlayer;
        this.numOfPlayers = playerNames.size();

        Iterator i = playerNames.iterator();

        while(i.hasNext()){
            String temp = (String)i.next();
            this.players.add(new Player(temp));
        }

    }

    public void run(){}

    public void chooseMap(int nMap){}

    public void setupKST(int nSkulls){}

    public void setupSpawnSpots(){}

    public void setupAmmoSpots(){}

    public void chooseFirstPlayer(String player){ this.firstPlayer = player; }

    public void runTurn(String currentPlayer){}

    public void drawPowerupToPlayer(String player){}

    public void resartFromSpawnPoint(int indexToDiscard) {}

    public void giveWeaponToPlayer(Weapon weapon, String player){}

    public boolean[][] wherePlayerCanMove(String player){return new boolean[1][1];}

    public boolean[][] wherePlayerCanMoveAndGrab(String player){return new boolean[1][1];}

    public boolean p1CanShootp2(String p1, String p2, Weapon weapon){return true;}

    public void respawn(String player){}

    public void refillAmmos(){}

    public void refillSpawns(){}

    public void movePlayer(Player player, int x, int y){}

    public void reload(Weapon weapon){}

    public void moveAndGrab(Player player, int x, int y){}

    public void shootPlayer(Player offender, Player defender, Weapon weapon){}

    public boolean addPlayer(String newPlayer){
        if(playerNames.contains(newPlayer)){
            return false;
        }

        players.add(new Player(newPlayer));
        return true;
    }

    public void giveKSTpoints(){}

}
