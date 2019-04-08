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

    public void run(){
        System.out.println("Game starting: ...");
        chooseMap(1); //TODO let someone decide which map to play on
        setupKST(5); //TODO let someone decide the # of skulls on the KST
        setupSpawnSpots();
        setupAmmoSpots();

        boolean endOfTurns = false;
        String currentPlayer = this.firstPlayer;

        while(endOfTurns == false){ //TODO decide how to handle the turn

        }

        giveKSTpoints();
        endGame();
    }

    private void endGame() { return; }

    public void chooseMap(int nMap){}

    public void setupKST(int nSkulls){}

    public void setupSpawnSpots(){}

    public void setupAmmoSpots(){}

    //Might not need the method below
    //public void chooseFirstPlayer(String player){ this.firstPlayer = player; }

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

    //TODO remove +addPlayer(String player): void from UML Game

    public void giveKSTpoints(){}

    public void usePowerup(Powerup powerup, Player playerWhoUses, Player playerWhoReceiveEffect) {

        if(powerup.getEffect().getCost() == 1){
            //ask how the player want to pay this cost and decrement number of ammo or powerups. For example he wants to pay with yellow ammo
            playerWhoUses.setnYellowAmmo(playerWhoUses.getnYellowAmmo()-1);
        }

        else if(powerup.getEffect().getnDamages() == 1){  //is the targetingscope case, we have to give one damage to playerWhoReceiveEffect
            playerWhoReceiveEffect.giveDamage(playerWhoReceiveEffect.getNickname(),1);
        }

        else if(powerup.getEffect().getnMarks() == 1){       //is the TagBackGranade, we have to give one mark to playerWhoReceiveEffect
            playerWhoReceiveEffect.giveMarks(playerWhoReceiveEffect.getNickname(),1);
        }

        else if(powerup.getEffect().isTeleporterMove()){     //is the teleporter case
            /* then ask where player wants to move and modify his x and y position. */
        }

        else if(powerup.getEffect().getnMoves() == 1 || powerup.getEffect().getnMoves() == 2){  //is the Newton case
            // then ask player in which direction he wants to move and change his x and y posotion.
        }
        else System.out.println("Some powerup effect were set wrong!!");

        playerWhoUses.getPowerupList().remove(powerup); //TODO check if it's right
    }

}
