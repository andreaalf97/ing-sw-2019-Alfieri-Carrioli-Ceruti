package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Game implements Runnable{

    /**
     * This ArrayList contains all the player objects
     */
    private ArrayList<Player> players;

    /**
     * This ArrayList contains the nicknames of the players
     */
    private ArrayList<String> playerNames;

    /**
     * This object initially contains all the Powerup cards
     */
    private PowerupDeck powerupDeck;

    /**
     * This object initially contains all the Weapon cards
     */
    private WeaponDeck weaponDeck;

    /**
     * This references the player that ran the first Turn
     */
    private String firstPlayer;

    /**
     * This contains the number of players in the game and must be equal to players.size() at all times
     */
    private int numOfPlayers;

    /**
     * This references the player who is currently active in the game
     */
    private String currentPlayer;

    /**
     * This variable is used to check if everybody ran their first turn
     */
    private boolean isFirstTurn;

    //TODO: might not need attribute --> private boolean isFrenzy;

    //##########################################################################################################

    /**
     * This constructor instantiates a new Game by knowing the list of nicknames and the first player
     * @param playerNames ArrayList of all the players names
     * @param firstPlayer The nickname of the first player
     */
    public Game(ArrayList<String> playerNames, String firstPlayer){
        this.players = new ArrayList<>(playerNames.size());
        this.playerNames = playerNames;
        this.powerupDeck = new PowerupDeck();
        this.weaponDeck = new WeaponDeck();
        this.firstPlayer = firstPlayer;
        this.currentPlayer = firstPlayer;
        this.isFirstTurn = true;
        //this.isFrenzy = false;
        this.numOfPlayers = playerNames.size();

        Iterator i = playerNames.iterator();

        while(i.hasNext()){
            String temp = (String)i.next();
            this.players.add(new Player(temp));
        }

    }

    /**
     * This method implements the void run() method from Runnable and is processed on a new thread
     */
    public void run(){
        System.out.println("Game starting: ...");
        chooseMap(MapName.WATER); //TODO let someone decide which map to play on
        setupKST(5); //TODO let someone decide the # of skulls on the KST
        setupSpawnSpots();
        setupAmmoSpots();

        boolean endOfTurns = false;

        while(endOfTurns == false){ //TODO decide how to handle the turn
            System.out.println("Do you want to use a powerup?");

            System.out.println("Move / Move&Grab / Attack");

            System.out.println("Do you want to use a powerup?");

            System.out.println("Move / Move&Grab / Attack");

            System.out.println("Do you want to use a powerup?");

            System.out.println("Do you want to reload?");

            checkDeaths();
            refillAmmos();
            refillSpawns();


            if(this.isFirstTurn && currentPlayer == playerNames.get(playerNames.size() - 1))
                this.isFirstTurn = false;
            currentPlayer = nextPlayer(currentPlayer);
        }

        giveKSTpoints();
        endGame();
    }

    /**
     * This method checks if any player is dead and counts their boards assigning the right amount of point to each player.
     * It is usually executed at the end of each turn
     */
    private void checkDeaths() {
        System.out.println("Checking deaths...");
        System.out.println("Done checking deaths");
    }

    /**
     * This method returns the nickname of the next player
     * @param currentPlayer The player running the current turn
     * @return The nickname of the next player
     */
    private String nextPlayer(String currentPlayer) {
        int indexOfCurrent = playerNames.indexOf(currentPlayer);
        return playerNames.get((indexOfCurrent + 1) % playerNames.size());
    }

    /**
     * This closes all connections and ends the game
     */
    private void endGame() {
        //TODO review this method
    }

    /**
     * This method is used to generate the correct map
     * @param mapName The map that need to be created
     */
    public void chooseMap(MapName mapName){}

    /**
     * This method assigns the correct amount of skulls to the Kill Shot Track
     * @param nSkulls The amount of skulls to add
     */
    public void setupKST(int nSkulls){}

    /**
     * This method initially refills all the spawn spots
     */
    public void setupSpawnSpots(){}

    /**
     * This method initially refills all the ammo spots
     */
    public void setupAmmoSpots(){}

    //TODO Might not need the method below
    //public void chooseFirstPlayer(String player){ this.firstPlayer = player; }

    /**
     * This method runs the entire turn of a single player
     * @param currentPlayer The nickname of the current player
     */
    public void runTurn(String currentPlayer){}

    /**
     * Draws a weapon from the powerup deck and gives it a player
     * @param player the nickname of player who's receiving the card
     */
    public void drawPowerupToPlayer(String player){}

    /**
     * Allows the player to discard a powerup card and choose their new Spawn spot
     * @param indexToDiscard the index in the player's ArrayList of card to be discarded
     */
    public void restartFromSpawnPoint(int indexToDiscard) {}

    /**
     * Gives the chosen weapon to a player
     * @param weapon The weapon object to be given
     * @param player The nickname of the player
     */
    public void giveWeaponToPlayer(Weapon weapon, String player){}

    /**
     * Show all the different spots where a player is allowed to move
     * @param player The nickname of the player
     * @param nMoves The amount of required moves
     * @return A boolean matrix where the <i, j> element is true only if the player is allowed to move there
     */
    public boolean[][] wherePlayerCanMove(String player, int nMoves){return new boolean[1][1];}

    /**
     * Show all the different spots where a player is allowed to move and grab
     * @param player The nickname of the player
     * @param nMoves The amount of required moves
     * @return A boolean matrix where the <i, j> element is true only if the player is allowed to move there and there is ammo to grab
     */
    public boolean[][] wherePlayerCanMoveAndGrab(String player, int nMoves){return new boolean[1][1];}

    /**
     * Tells if player P1 can shoot player P2 with the selected weapon
     * @param p1 The nickname of the first player
     * @param p2 The nickname of the second player
     * @param weapon The weapon that P1 wants to use to shoot
     * @return True only if P1 is allowed to shoot P2 with that weapon
     */
    public boolean p1CanShootp2(String p1, String p2, Weapon weapon){
        //TODO review this method, might not need it if we don't advice the player on which weapon to use
        return true;
    }

    /**
     * Lets the player pick a new powerup card and choose the new spawn spot to respawn
     * @param player the nickname of the player
     */
    public void respawn(String player){}

    /**
     * Refills all the ammo spots
     */
    public void refillAmmos(){}

    /**
     * Refills all the spawn spots with weapons
     */
    public void refillSpawns(){}

    /**
     * Moves the selected player to the new spot
     * @param player the nickname of the player
     * @param x the position of the spot on the x-axis
     * @param y the position of the spot on the y-axis
     */
    public void movePlayer(Player player, int x, int y){}

    /**
     * Reloads the selected weapon
     * @param weapon weapon to reload
     */
    public void reload(Weapon weapon){}

    /**
     * Tell if the selected weapon can be reloaded
     * @param weapon weapon to check
     * @return true only if the weapon can be reloaded
     */
    public boolean canReload(Weapon weapon){
        return true;
    }

    /**
     * Moves the player to the selected spot and grabs the ammos on it
     * @param player the nickname of the player
     * @param x the position of the spot on the x-axis
     * @param y the position of the spot on the y-axis
     */
    public void moveAndGrab(Player player, int x, int y){}

    /**
     * Shoots the defender with the weapon of the offender
     * @param offender Player who attacks
     * @param defender Attacked player
     * @param weapon Shooting weapon
     */
    public void shootPlayer(Player offender, Player defender, Weapon weapon){}

    /**
     * Clears the Kill Shot Track and gives points to all players involved
     */
    public void giveKSTpoints(){}

    //TODO Think about this method
    /* public void usePowerup(Powerup powerup, Player playerWhoUses, Player playerWhoReceiveEffect) {

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
            // then ask where player wants to move and modify his x and y position.
        }

        else if(powerup.getEffect().getnMoves() == 1 || powerup.getEffect().getnMoves() == 2){  //is the Newton case
            // then ask player in which direction he wants to move and change his x and y posotion.
        }
        else System.out.println("Some powerup effect were set wrong!!");

        playerWhoUses.getPowerupList().remove(powerup); //check if it's right
    }   */

}
