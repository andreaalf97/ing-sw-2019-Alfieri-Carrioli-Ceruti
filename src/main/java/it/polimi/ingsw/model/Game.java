package it.polimi.ingsw.model;

import it.polimi.ingsw.model.CardsPackage.Effect;
import it.polimi.ingsw.model.CardsPackage.PowerupDeck;
import it.polimi.ingsw.model.CardsPackage.Weapon;
import it.polimi.ingsw.model.CardsPackage.WeaponDeck;
import it.polimi.ingsw.model.MapPackage.GameMap;
import it.polimi.ingsw.model.MapPackage.MapBuilder;
import it.polimi.ingsw.model.MapPackage.MapName;
import it.polimi.ingsw.model.MapPackage.Visibility;

import java.util.ArrayList;

public class Game implements Runnable{

    /**
     * This ArrayList contains all the player objects
     * The players must in the same order of the round, so the first player must be in position 0 !!
     */
    private ArrayList<Player> players;

    /**
     * This ArrayList contains the nicknames of the players
     * The players must in the same order of the round & in the same order of this.players!
     */
    private ArrayList<String> playerNames;

    /**
     * The game map
     */
    private GameMap gameMap;

    /**
     * This object initially contains all the Powerup cards
     */
    private PowerupDeck powerupDeck;

    /**
     * This object initially contains all the Weapon cards
     */
    private WeaponDeck weaponDeck;

    /**
     * The Kill shot track of this game
     */
    private KillShotTrack kst;

    /**
     * This contains the number of players in the game and must be equal to players.size() at all times
     */
    private int numOfPlayers;

    //TODO: might not need attribute --> private boolean isFrenzy;

    //##########################################################################################################

    /**
     * This constructor instantiates a new Game by knowing the list of nicknames and the first player
     * @param playerNames ArrayList of all the players names
     *
     */
    public Game(ArrayList<String> playerNames, MapName chosenMap, int nSkulls){
        this.players = new ArrayList<>();
        this.playerNames = playerNames;
        this.powerupDeck = new PowerupDeck();
        this.weaponDeck = new WeaponDeck();
        this.numOfPlayers = playerNames.size();

        for(String name : this.playerNames)
            this.players.add(new Player(name));

        this.gameMap = MapBuilder.generateMap(chosenMap, weaponDeck, powerupDeck);
        this.kst = new KillShotTrack(nSkulls);
    }

    /**
     * This method implements the void run() method from Runnable and is processed on a new thread
     */
    public void run(){
        Log.LOGGER.info("Game starting");
        Log.LOGGER.warning("The map has been chosen by polling");
        Log.LOGGER.warning("The KST has been set up after polling");

        boolean endOfTurns = false;

        //Runs the first turn for all players
        for(String currentPlayer : this.playerNames){
            //Spawn player by choosing 2 powerups!

            //Do you want to use a powerup?

            //Move - Move&Grab - Attack

            //Do you want to use a powerup?

            //Move - Move&Grab - Attack

            //Do you want to use a powerup?

            //Do you want to reload?

            checkDeaths();
            refillAmmos();
            refillSpawns();
        }

        //Runs until the end of the regular game, will handle frenzy in the next loop
        while(!endOfTurns){

            for(String currentPlayer : this.playerNames) {

                //Do you want to use a powerup?

                //Move - Move&Grab - Attack

                //Do you want to use a powerup?

                //Move - Move&Grab - Attack

                //Do you want to use a powerup?

                //Do you want to reload?

                checkDeaths();
                refillAmmos();
                refillSpawns();

                if (this.kst.noMoreSkulls())
                    endOfTurns = true;
            }
        }

        for(String currentPlayer : this.playerNames){
            //TODO check if the round continues from the last player
            setupForFrenzy();
        }

        giveKSTpoints();
        endGame();
    }

    /**
     * This method sets up all player for the final frenzy turn
     */
    private void setupForFrenzy() {
        //TODO
    }

    /**
     * This method checks if any player is dead and counts their boards assigning the right amount of point to each player.
     * It is usually executed at the end of each turn
     */
    private void checkDeaths() {
        //TODO
        Log.LOGGER.info("Checking deaths...");
        Log.LOGGER.info("Done checking deaths");
    }

    /**
     * This closes all connections and ends the game
     */
    private void endGame() {
        //TODO review this method
    }

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
    public boolean[][] wherePlayerCanMove(String player, int nMoves){
        return this.gameMap.wherePlayerCanMove(player, nMoves);
    }

    /**
     * Show all the different spots where a player is allowed to move and grab
     * @param player The nickname of the player
     * @param nMoves The amount of required moves
     * @return A boolean matrix where the <i, j> element is true only if the player is allowed to move there and there is ammo to grab
     */
    public boolean[][] wherePlayerCanMoveAndGrab(String player, int nMoves){
        return this.gameMap.wherePlayerCanMoveAndGrab(player, nMoves);
    }

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
    public void reload(Weapon weapon){
        weapon.reload();
    }

    /**
     * Tell if the selected weapon can be reloaded
     * @param weapon weapon to check
     * @return true only if the weapon can be reloaded
     */
    public boolean canReload(Weapon weapon){
        //TODO
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
     * @param defenders Attacked player
     * @param weapon Shooting weapon
     */
    public void shootPlayer(Player offender, ArrayList<Player> defenders, Weapon weapon){

        ArrayList<Integer[]> order = weapon.getOrder();

        ArrayList<Effect> effects = weapon.getEffects();

        ArrayList<Player> playersHit = new ArrayList<>();

        System.out.println("Choose the order you want to shoot. 0 to order.size");

        //for example the user chooses order x
        int x = 2;

        //TODO need to check if player wants to stop at any point in the order
        for ( int i = 0; i < order.get(x).length ; i++ ) {

            int effect_number = order.get(x)[i];   //this is the effect we have to use

            //TODO what are these two?
            int spots_moved_on_x;
            int spots_moved_on_y;

            if (effects.get(effect_number).getnMoves() != 0) {
                System.out.println("where do you want to move? maximum weapon.getEffects(effect_number).nmoves moves");       //expect a new spot

                //TODO we already have a method to check if a player can move from a spot to another --> GameMap.canMoveFromTo(int p1x, int p1y, int p2x, int p2y, int nMoves)
                int xPos = 1, yPos = 1;     //spot where the user wants to move in

                if (offender.getxPosition() <= xPos)
                    spots_moved_on_x = xPos - offender.getxPosition();
                else
                    spots_moved_on_x = offender.getxPosition() - xPos;

                if (offender.getyPosition() <= yPos)
                    spots_moved_on_y = xPos - offender.getyPosition();
                else
                    spots_moved_on_y = offender.getyPosition() - xPos;

                if (spots_moved_on_x + spots_moved_on_y > effects.get(effect_number).getnMoves())
                    System.out.println("can't move here");
                else
                    movePlayer(offender, xPos, yPos);

                //TODO why are we skipping one cycle here?
                //Is it because we don't check anything alse if nMoves > 0 ? ---> Use comments
                continue;
            }

            if (effects.get(effect_number).getnMovesOtherPlayer() != 0) {
                System.out.println("where do you want to move the player you attack? maximum weapon.getEffects(effect_number).nMovesOtherPlayer moves");       //expect a new spot

                //TODO same as the loop before
                int xPos= 1, yPos = 1;     //spot where the user wants to move in

                if (defenders.get(0).getxPosition() <= xPos)
                    spots_moved_on_x = xPos - defenders.get(0).getxPosition();
                else
                    spots_moved_on_x = defenders.get(0).getxPosition() - xPos;

                if (offender.getyPosition() <= yPos)
                    spots_moved_on_y = xPos - defenders.get(0).getyPosition();
                else
                    spots_moved_on_y = defenders.get(0).getyPosition() - xPos;

                if (spots_moved_on_x + spots_moved_on_y > effects.get(effect_number).getnMovesOtherPlayer())
                    System.out.println("can't move this player here");
                else
                    movePlayer(defenders.get(0), xPos, yPos);

                //TODO why are we skipping one cycle
                continue;
            }

            if(effects.get(effect_number).getCost() != null){

                for ( int n = 0; n < effects.get(effect_number).getCost().size() - 1; n++){
                    //TODO calls like "effects.get.getCost.get" might not be the best way
                    //TODO e.g. create a temporary array and work on it

                    //TODO don't hardcode checks on ammos here, create a new method instead (e.g. a boolean method)
                    if ( effects.get(effect_number).getCost().get(n) == Color.RED ){
                        if ( offender.getnRedAmmo() == 0)
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else
                            offender.setnRedAmmo(offender.getnRedAmmo()-1);

                        //TODO you are repeating the same code for every color!!
                    }
                    if ( effects.get(effect_number).getCost().get(n) == Color.YELLOW ){
                        if ( offender.getnYellowAmmo() == 0)
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else
                            offender.setnYellowAmmo(offender.getnYellowAmmo()-1);
                    }
                    if ( effects.get(effect_number).getCost().get(n) == Color.BLUE ){
                        if ( offender.getnBlueAmmo() == 0)
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else
                            offender.setnBlueAmmo(offender.getnBlueAmmo()-1);
                    }
                    if ( effects.get(effect_number).getCost().get(n) == Color.ANY ){
                        if ( offender.getnRedAmmo() == 0 && offender.getnYellowAmmo() == 0 && offender.getnBlueAmmo() == 0 )
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else{
                            System.out.println( "Choose the color you want to pay with" );
                            /*TODO scale one of the color the user chose*/
                        }
                    }
                }
            }

            if( effects.get(effect_number).getnPlayerAttackable() != 0 ){

                if ( effects.get(effect_number).getVisibleByWho() == Visibility.NONE ){
                    /*TODO use a foreach loop to avoid k -= 1 !! --> for(Player defender : defenders)
                     https://stackoverflow.com/questions/85190/how-does-the-java-for-each-loop-work
                     */

                    for ( int k = 0; k < defenders.size() - 1; k++ ){   //controllo i defenders, se qualcuno non rispetta visibiluty lo escludo
                        if ( this.gameMap.see( offender.getxPosition(), offender.getyPosition(), defenders.get(k).getxPosition(), defenders.get(k).getyPosition()) ){
                            defenders.remove(k);
                            k -= 1;
                        }
                    }
                }
                if ( effects.get(effect_number).getVisibleByWho() == Visibility.OFFENDER ){
                    //TODO use foreach
                    for ( int k = 0; k < defenders.size() - 1; k++ ){   //controllo i defenders, se qualcuno non rispetta visibiluty lo escludo
                        if ( !this.gameMap.see( offender.getxPosition(), offender.getyPosition(), defenders.get(k).getxPosition(), defenders.get(k).getyPosition()) ){
                            defenders.remove(k);
                            k -= 1;
                        }
                    }
                }
                if ( playersHit.size() != 0 ) {

                    if ( effects.get(effect_number).getVisibleByWho() == Visibility.LASTONEATTACKED ){
                        for ( int k = 0; k < defenders.size() - 1; k++ ){   //controllo i defenders, se qualcuno non rispetta visibiluty lo escludo
                            if ( !this.gameMap.see( playersHit.get(playersHit.size() - 1).getxPosition(), playersHit.get(playersHit.size() - 1).getyPosition(), defenders.get(k).getxPosition(), defenders.get(k).getyPosition()) ){
                                defenders.remove(k);
                                k -= 1;
                            }
                        }
                    }
                    if ( effects.get(effect_number).mustShootOtherPlayers() ){
                        for ( int j = 0; j < playersHit.size() - 1; j++ ){
                            for ( int k = 0; k < defenders.size() - 1; k++){
                                if ( defenders.get(k) == playersHit.get(j) ){
                                    defenders.remove(k);
                                    k -= 1;
                                }
                            }
                        }
                    }
                    /*TODO may delete this effect attribute
                    if ( effects.get(effect_number).canShootAnyPlayer() ){}*/
                }

                if( effects.get(effect_number).mustBeDifferentSpots() ){
                    for ( int k = 0; k < defenders.size() - 1; k++ ) {
                        for ( int j = 0; j < (defenders.size() - 1) && j != k; j++ ) {
                            if ( defenders.get(k).getxPosition() == defenders.get(j).getxPosition() && defenders.get(k).getyPosition() == defenders.get(j).getyPosition() ){
                                System.out.println("which player do you want to hit between defenders.get(k).getPlayerName and defenders.get(j).getPlayerName");
                                //want to hit j
                                defenders.remove(k);
                                k -= 1;
                                j -= 1;
                            }
                        }
                    }
                }

            }




        }
        weapon.unload();
    }

    /**
     * Clears the Kill Shot Track and gives points to all players involved
     */
    public void giveKSTpoints(){
        int[] pointValues = {8, 6, 4, 2, 1, 1};

        ArrayList<String> ranking = kst.getRanking();

        try {
            for (int i = 0; i < ranking.size(); i++)
                getPlayerByNickname(ranking.get(i)).givePoints(pointValues[i]);
        }
        catch (NoSuchPlayerException e) {
            e.printStackTrace();
        }

    }

    private Player getPlayerByNickname(String nickname) throws NoSuchPlayerException{
        if(!playerNames.contains(nickname))
            throw new NoSuchPlayerException("This player does not exist");

        return players.get(playerNames.indexOf(nickname));
    }

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
