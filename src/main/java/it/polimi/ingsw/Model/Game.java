package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.CardsPackage.*;
import it.polimi.ingsw.Model.MapPackage.GameMap;
import it.polimi.ingsw.Model.MapPackage.MapBuilder;
import it.polimi.ingsw.Model.MapPackage.MapName;
import it.polimi.ingsw.Model.MapPackage.Visibility;

import java.util.ArrayList;

/*
    THE MODEL:
        - Data related logic
        - Interaction with Database or JSON file
        - Communicates with controller (TODO how?)
        - Can sometimes update the view (depends on framework)
 */

public class Game {

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
     * Gives a randomly picked powerup to the player
     * @param player the player who's receiving the powerup
     */
    public void givePowerup(String player) {

        Player p = getPlayerByNickname(player);
        p.givePowerup(this.powerupDeck.drawCard());
    }

    /**
     * This method sets up all player for the final frenzy turn
     */
    public void setupForFrenzy() {
        //TODO
    }

    /**
     * This method checks if any player is dead and counts their boards assigning the right amount of point to each player.
     * It is usually executed at the end of each turn
     */
    public void checkDeaths() {
        //TODO
        Log.LOGGER.info("Checking deaths...");
        Log.LOGGER.info("Done checking deaths");
    }

    /**
     * This closes all connections and ends the game
     */
    public void endGame() {
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
    public void respawn(String player, int powerupIndexToDiscard){

        Player p = getPlayerByNickname(player);
        Color discardedColor = p.discardPowerupByIndex(powerupIndexToDiscard);

        movePlayerToSpawnColor(player, discardedColor);
    }

    private void movePlayerToSpawnColor(String player, Color discardedColor) {
        this.gameMap.movePlayerToColorSpawn(player, discardedColor);
    }

    /**
     * Refills all the ammo spots
     */
    public void refillAmmos(){
        this.gameMap.refillAmmos(this.powerupDeck);
    }

    /**
     * Refills all the spawn spots with weapons
     */
    public void refillSpawns(){
        this.gameMap.refillSpawns(this.weaponDeck);
    }

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

        ArrayList<Player> playersHit = new ArrayList<Player>();

        System.out.println("Choose the order you want to shoot. 0 to order.size");

        //for example the user chooses order x
        int x = 2;

        for ( int i = 0; i < order.get(x).length ; i++ ) {

            int effect_number = order.get(x)[i];   //this is the effect we have to use

            int spots_moved_on_x;
            int spots_moved_on_y;

            if (effects.get(effect_number).getnMoves() != 0) {
                System.out.println("where do you want to move? maximum weapon.getEffects(effect_number).nmoves moves");       //expect a new spot

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

                continue;
            }

            if (effects.get(effect_number).getnMovesOtherPlayer() != 0) {
                System.out.println("where do you want to move the player you attack? maximum weapon.getEffects(effect_number).nMovesOtherPlayer moves");       //expect a new spot

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

                continue;
            }

            if(effects.get(effect_number).getCost() != null){

                for ( int n = 0; n < effects.get(effect_number).getCost().size() - 1; n++ ){
                    if ( effects.get(effect_number).getCost().get(n) == Color.RED ){
                        if ( offender.getnRedAmmo() == 0 )
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else
                            offender.setnRedAmmo(offender.getnRedAmmo()-1);
                    }
                    if ( effects.get(effect_number).getCost().get(n) == Color.YELLOW ){
                        if ( offender.getnYellowAmmo() == 0 )
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else
                            offender.setnYellowAmmo(offender.getnYellowAmmo()-1);
                    }
                    if ( effects.get(effect_number).getCost().get(n) == Color.BLUE ){
                        if ( offender.getnBlueAmmo() == 0 )
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

            if( effects.get(effect_number).getnPlayerAttackable() != 0  || effects.get(effect_number).getnPlayerMarkable() != 0){

                if ( effects.get(effect_number).getVisibleByWho() == Visibility.NONE ){
                    for ( int k = 0; k < defenders.size() - 1; k++ ){   //controllo i defenders, se qualcuno non rispetta visibility lo escludo
                        if ( this.gameMap.see( offender.getxPosition(), offender.getyPosition(), defenders.get(k).getxPosition(), defenders.get(k).getyPosition()) ){
                            defenders.remove(k);
                            k -= 1;
                        }
                    }
                }
                if ( effects.get(effect_number).getVisibleByWho() == Visibility.OFFENDER ){
                    for ( int k = 0; k < defenders.size() - 1; k++ ){   //controllo i defenders, se qualcuno non rispetta visibility lo escludo
                        if ( !this.gameMap.see( offender.getxPosition(), offender.getyPosition(), defenders.get(k).getxPosition(), defenders.get(k).getyPosition()) ){
                            defenders.remove(k);
                            k -= 1;
                        }
                    }
                }
                if ( playersHit.size() != 0 ) {

                    if ( effects.get(effect_number).getVisibleByWho() == Visibility.LASTONEATTACKED ){
                        for ( int k = 0; k < defenders.size() - 1; k++ ){   //controllo i defenders, se qualcuno non rispetta visibility lo escludo
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
                    if ( effects.get(effect_number).isCanShootAnyPlayer() ){}*/
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
                /*TODO either make getSpotByIndex public or make some new method to get the player room*/
                /*if( effects.get(effect_number).mustBeOtherRoom()) {
                    for ( int k = 0; k < defenders.size() - 1; k++ ){
                        if ( this.gameMap.getSpotByIndex(offender.getxPosition(), offender.getyPosition()).getRoom() == this.gameMap.getSpotByIndex(defenders.get(k).getxPosition(), defenders.get(k).getyPosition()).getRoom()) {
                            defenders.remove(k);
                            k -= 1;
                        }
                    }
                } */
                if ( effects.get(effect_number).isLinear()) {
                    System.out.println("In which direction do you want to shoot?");
                    //for example NORTH /*TODO look for scanf.
                    // In this case if i already have the list of player i want to attack, i only have to check if someone is not in line and remove him.*/
                }

                for ( int k = 0; k < defenders.size() - 1; k++ ) {      //if a defender is not minDistance < |defender.position - offender.position| < MaxDistance remove him.

                    int spots_on_x, spots_on_y;

                    if (offender.getxPosition() <= defenders.get(k).getxPosition())
                        spots_on_x = defenders.get(k).getxPosition() - offender.getxPosition();
                    else
                        spots_on_x = offender.getxPosition() - defenders.get(k).getxPosition();

                    if (offender.getyPosition() <= defenders.get(k).getyPosition())
                        spots_on_y = defenders.get(k).getyPosition() - offender.getyPosition();
                    else
                        spots_on_y = offender.getyPosition() - defenders.get(k).getyPosition();
                    if ( spots_on_x + spots_on_y > effects.get(effect_number).getMinDistance()){
                        defenders.remove(k);
                        k -= 1;
                    }
                    if ( spots_on_x + spots_on_y < effects.get(effect_number).getMaxDistance()){
                        defenders.remove(k);
                        k -= 1;
                    }
                }
                for (int k = 0; k < (defenders.size() - 1) && k < (effects.get(effect_number).getnPlayerAttackable() - 1); k++) {
                    defenders.get(k).giveDamage(offender.getNickname(), effects.get(effect_number).getnDamages());
                    playersHit.add(defenders.get(k));
                }
                for (int k = 0; k < (defenders.size() - 1) && k < (effects.get(effect_number).getnPlayerMarkable() - 1); k++) {
                    defenders.get(k).giveMarks(offender.getNickname(), effects.get(effect_number).getnMarks());
                    playersHit.add(defenders.get(k));
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

        for (int i = 0; i < ranking.size(); i++)
                getPlayerByNickname(ranking.get(i)).givePoints(pointValues[i]);

    }

    private Player getPlayerByNickname(String nickname){

        /*
        if(!playerNames.contains(nickname))
            throw new RuntimeException("This player does not exist");
        */

        return players.get(playerNames.indexOf(nickname));
    }

    public ArrayList<String> getPlayerNames() {
        return new ArrayList<>(this.playerNames);
    }

    public boolean noMoreSkullsOnKST() {
        return this.kst.noMoreSkulls();
    }

    public ArrayList<Powerup> getPlayerPowerups(String player) {

        Player p = this.getPlayerByNickname(player);

        return p.getPowerupList();
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
