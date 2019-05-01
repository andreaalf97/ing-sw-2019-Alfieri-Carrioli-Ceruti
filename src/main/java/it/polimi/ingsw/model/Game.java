package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.map.GameMap;
import it.polimi.ingsw.model.map.MapBuilder;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.model.cards.Visibility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.logging.Level;

/*
    THE MODEL:
        - Data related logic
        - Interaction with Database or JSON file
        - Communicates with controller
        - Can sometimes update the view (depends on framework)
 */

public class Game extends Observable {

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
     * This object initially contains all the PowerUp cards
     */
    private PowerUpDeck powerupDeck;

    /**
     * This object initially contains all the Weapon cards
     */
    private WeaponDeck weaponDeck;

    /**
     * The Kill shot track of this game
     */
    private KillShotTrack kst;

    //##########################################################################################################

    /**
     * This constructor instantiates a new Game by knowing the list of nicknames and the first player
     * @param playerNames ArrayList of all the players names
     *
     */
    public Game(ArrayList<String> playerNames, MapName chosenMap, int nSkulls){
        this.players = new ArrayList<>();
        this.playerNames = playerNames;
        this.powerupDeck = new PowerUpDeck();
        this.weaponDeck = new WeaponDeck();

        for(String name : this.playerNames)
            this.players.add(new Player(name));

        this.gameMap = MapBuilder.generateMap(chosenMap, weaponDeck, powerupDeck);
        this.kst = new KillShotTrack(nSkulls);
    }

    //##########################################################################################################

    public ArrayList<String> getPlayerNames() {
        return new ArrayList<>(this.playerNames);
    }

    public ArrayList<PowerUp> getPlayerPowerUps(String player) {

        Player p = this.getPlayerByNickname(player);

        return p.getPowerUpList();
    }

    //##########################################################################################################
    private Player getPlayerByNickname(String nickname){

        if(!playerNames.contains(nickname))
            throw new IllegalArgumentException("This nickname does not exist!");

        return players.get(playerNames.indexOf(nickname));
    }

    /**
     * Gives a randomly picked powerup to the player
     * @param player the player who's receiving the powerUp
     */
    public void givePowerUp(String player) {

        Player p = getPlayerByNickname(player);
        if (!this.powerupDeck.getPowerUpList().isEmpty())
            p.givePowerUp(this.powerupDeck.drawCard());
        else {
            //if deck is empty i reload it from json instead shuffle the old one

            Log.LOGGER.log(Level.INFO, "deck is empty, reloading it from json");
            this.powerupDeck = new PowerUpDeck();
            p.givePowerUp(this.powerupDeck.drawCard());
        }
    }

    /**
     * gives a randomly picked weapon to the player
     * @param player the player who's receiving the weapon
     */
    public void giveWeapon(String player){
        Player p = getPlayerByNickname(player);

        p.giveWeapon(this.weaponDeck.drawCard());
    }

    /**
     * This method sets up all player for the final frenzy turn
     */
    public void setupForFrenzy () {

        ArrayList<String> playersWithFrenzyBoard = new ArrayList<String>();

        for(Player p : players) {
            if (p.getDamages().isEmpty())
                p.sethasFrenzyBoard(true);
            p.setCanReloadBeforeShooting(true);
            //TODO nMovesBeforeReloading depends on where the frenzy turn starts from
        }
    }

    /**
     * This method checks if any player is dead and counts their boards assigning the right amount of point to each player.
     * It is usually executed at the end of each turn
     */
    public void checkDeaths() {
        //TODO check deaths and assign the points to the players 
        Log.LOGGER.info("Checking deaths...");
        Log.LOGGER.info("Done checking deaths");
    }

    /**
     * Gives the chosen weapon to a player
     * @param weapon The weapon object to be given
     * @param player The nickname of the player
     */
    protected void giveWeaponToPlayer(String player, Weapon weapon){
        Player p = getPlayerByNickname(player);
        p.giveWeapon(weapon);
    }

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
     * Lets the player pick a new powerup card and choose the new spawn spot to respawn
     * @param player the nickname of the player
     */
    public void respawn(String player, int powerupIndexToDiscard){

        Player p = getPlayerByNickname(player);

        if(!p.isDead())
            throw new RuntimeException("The player you want to respawn is not dead!");

        Color discardedColor = p.discardPowerUpByIndex(powerupIndexToDiscard);

        p.revive();
        movePlayerToSpawnColor(player, discardedColor);
    }

    private void movePlayerToSpawnColor(String player, Color discardedColor) {
        gameMap.movePlayerToColorSpawn(player, discardedColor);
        int[] coord = gameMap.getPlayerSpotCoord(player);

        Player p = getPlayerByNickname(player);

        p.moveTo(coord[0], coord[1]);
    }

    /**
     * Refills all the ammo spots
     */
    public void refillAllAmmoSpots(){
        this.gameMap.refillAmmos(this.powerupDeck);
    }

    /**
     * Refills all the spawn spots with weapons
     */
    public void refillAllSpawnSpots(){
        this.gameMap.refillSpawns(this.weaponDeck);
    }

    /**
     * Moves the selected player to the new spot
     * @param player the nickname of the player
     * @param x the position of the spot on the x-axis
     * @param y the position of the spot on the y-axis
     */
    protected void movePlayer(String player, int x, int y){

        Player p = getPlayerByNickname(player);

        p.moveTo(x, y);

        gameMap.movePlayer(player, x, y);
    }

    /**
     * Moves the player to the selected spot and grabs the ammos on it
     * @param player the nickname of the player
     * @param x the position of the spot on the x-axis
     * @param y the position of the spot on the y-axis
     */
    public void moveAndGrab(Player player, int x, int y){}

    /**
     * Tells if player offender can shoot player defenders with the selected weapon
     * @param offendername The nickname of the first player
     * @param defendersNames The nickname of the second player
     * @param weapon The effect the weapon used
     * @return True only if offender can shoot defenders
     */
    public boolean p1CanShootPlayers(String offendername, ArrayList<String> defendersNames, Weapon weapon, int orderNumber, int indexInOrder) {


        int effect_number = weapon.getOrder().get(orderNumber)[indexInOrder];

        Player offender = getPlayerByNickname(offendername);

        ArrayList<Player> defenders = new ArrayList<>();

        for (String i : defendersNames)
            defenders.add(getPlayerByNickname(i));

        if (weapon.getEffects().get(effect_number).getVisibleByWho() == Visibility.NONE) { // for ( Player p : defenders )
            for (int k = 0; k < defenders.size() - 1; k++) {   //controllo i defenders, se qualcuno non rispetta visibility lo escludo
                if (this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(k).getxPosition(), defenders.get(k).getyPosition())) {
                    defenders.remove(k);
                    k -= 1;
                    return false;
                }
            }
        }
        if (weapon.getEffects().get(effect_number).getVisibleByWho() == Visibility.OFFENDER) {
            for (int k = 0; k < defenders.size() - 1; k++) {   //controllo i defenders, se qualcuno non rispetta visibility lo escludo
                if (!this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(k).getxPosition(), defenders.get(k).getyPosition())) {
                    defenders.remove(k);
                    k -= 1;
                    return false;
                }
            }
        }

        for (int k = 0; k < defenders.size() - 1; k++) {      //if a defender is not minDistance < |defender.position - offender.position| < MaxDistance remove him.

            int spots_on_x, spots_on_y;

            if (offender.getxPosition() <= defenders.get(k).getxPosition())
                spots_on_x = defenders.get(k).getxPosition() - offender.getxPosition();
            else
                spots_on_x = offender.getxPosition() - defenders.get(k).getxPosition();

            if (offender.getyPosition() <= defenders.get(k).getyPosition())
                spots_on_y = defenders.get(k).getyPosition() - offender.getyPosition();
            else
                spots_on_y = offender.getyPosition() - defenders.get(k).getyPosition();
            if (spots_on_x + spots_on_y > weapon.getEffects().get(effect_number).getMinDistance()) {
                defenders.remove(k);
                k -= 1;
                return false;

            }
            if (spots_on_x + spots_on_y < weapon.getEffects().get(effect_number).getMaxDistance()) {
                defenders.remove(k);
                k -= 1;
                return false;

            }
        }
        return true;
    }


    public int typeOfEffect( int ordernumber, int indexinOrder, Weapon weapon ){

        int effectNumber = weapon.getOrder().get(ordernumber)[indexinOrder];   //this is the effect we have to check

        int type = 0;

        if (weapon.getEffects().get(effectNumber).getnMoves() != 0) {      //this.player movement
            type = 0;
        }
        if (weapon.getEffects().get(effectNumber).getnMovesOtherPlayer() != 0) {      //other player movement
            type = 0;
        }
        if (weapon.getEffects().get(effectNumber).getnPlayerAttackable() != 0|| weapon.getEffects().get(effectNumber).getnPlayerMarkable() != 0) {      //damage effect
            type = 1;
        }
        return type;
    }

    public void makeMovementEffect(){

    }
    public void makeDamageEffect(){

    }

    /**
     * Shoots the defender with the weapon of the offender
     * @param offenderName Player who attacks
     * @param defendersNames Attacked player
     * @param weapon Shooting weapon
     */
    protected void shootPlayer(String offenderName, ArrayList<String> defendersNames, Weapon weapon){

        Player offender = getPlayerByNickname(offenderName);

        ArrayList<Player> defenders = new ArrayList<>();

        for(String i : defendersNames)
            defenders.add(getPlayerByNickname(i));

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
                    movePlayer(offender.getNickname(), xPos, yPos);


                continue;       //if nMoves != 0 it must be a movement only effects, it means that I don't need to check the other effects
            }

            if (effects.get(effect_number).getnMovesOtherPlayer() != 0) {
                System.out.println("where do you want to move the player you attack? maximum weapon.getEffects(effect_number).nMovesOtherPlayer moves");       //expect a new spot

                int xPos= 1, yPos = 1;     //spot where the user wants to move the other player

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
                    movePlayer(defenders.get(0).getNickname(), xPos, yPos);

                continue;   //if nMovesOtherPlayer != 0 it must be a movement only effects, it means that I don't need to check the other effects
            }

            if(effects.get(effect_number).getCost() != null){       //if there is a cost I pay it ( for example an optional shooting action )

                for ( Color cost : effects.get(effect_number).getCost() ){
                    if ( cost == Color.RED ){
                        if ( offender.getnRedAmmo() == 0 )
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else
                            offender.setnRedAmmo(offender.getnRedAmmo()-1);
                    }
                    if ( cost == Color.YELLOW ){
                        if ( offender.getnYellowAmmo() == 0 )
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else
                            offender.setnYellowAmmo(offender.getnYellowAmmo()-1);
                    }
                    if ( cost == Color.BLUE ){
                        if ( offender.getnBlueAmmo() == 0 )
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else
                            offender.setnBlueAmmo(offender.getnBlueAmmo()-1);
                    }
                    if ( cost == Color.ANY ){
                        if ( offender.getnRedAmmo() == 0 && offender.getnYellowAmmo() == 0 && offender.getnBlueAmmo() == 0 )
                            System.out.println( "You don't have enough ammo to do this optional attack");
                        else{
                            System.out.println( "Choose the color you want to pay with" );
                            /*TODO scale one of the color the user chose*/
                        }
                    }
                }
            }

            if ( effects.get(effect_number).getVisibleByWho() == Visibility.NONE ){ // for ( Player p : defenders )
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

            if( effects.get(effect_number).getnPlayerAttackable() != 0 ){    // defenders.size() must also be  < genNPlayerMarkable  !!
                /*TODO for the targeting scope we have a choice to make: the player ask to use the scope right before giving damage or we have to check if the player has a scope, then ask if he want to use it and to which player*/
                for (int k = 0; k < (defenders.size() - 1) && k < (effects.get(effect_number).getnPlayerAttackable() - 1); k++) {
                    defenders.get(k).giveDamage(offender.getNickname(), effects.get(effect_number).getnDamages());
                    playersHit.add(defenders.get(k));
                }
            }
            if( effects.get(effect_number).getnPlayerMarkable() != 0 ){ //defenders.size() must also be  < getnPlayerMarkable  !!

                for (int k = 0; k < (defenders.size() - 1) && k < (effects.get(effect_number).getnPlayerMarkable() - 1); k++) {
                    defenders.get(k).giveMarks(offender.getNickname(), effects.get(effect_number).getnMarks());
                    playersHit.add(defenders.get(k));
                }
            }
        }
        weapon.unload();
    }
    /**
     * Clears the player frenzy board and gives points to all players involved
     */
    public void giveFrenzyBoardPoints ( Player player) {

        int[] pointValues = {2, 1, 1, 1};

        ArrayList<String> ranking = player.getOffendersRanking();

        for (int i = 0; i < ranking.size(); i++)
            getPlayerByNickname(ranking.get(i)).givePoints(pointValues[i]);
    }
    /**
     * Clears the player board and gives points to all players involved
     */
    public void giveBoardPoints ( Player player  ) {

        int[] pointValues = {8, 6, 4, 2, 1, 1};

        getPlayerByNickname( player.getDamages().get(0) ).givePoints(1);            //first blood point

        ArrayList<String> ranking = player.getOffendersRanking();

        for (int i = 0; i < ranking.size(); i++)
            getPlayerByNickname(ranking.get(i)).givePoints(pointValues[i]);
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


    public boolean noMoreSkullsOnKST() {
        return this.kst.noMoreSkulls();
    }

    public void usePowerUp(String offenderName, String defenderName, PowerUp powerup) {

        Player offender = getPlayerByNickname(offenderName);
        Player defender = getPlayerByNickname(defenderName);

        Effect effect = powerup.getEffect();

        int spots_moved_on_x;
        int spots_moved_on_y;

        if (effect.getnMoves() != 0) {

            System.out.println("where do you want to move? maximum effect.nmoves moves");       //expect a new spot

            int xPos = 1, yPos = 1;     //spot where the user wants to move in

            if (offender.getxPosition() <= xPos)
                spots_moved_on_x = xPos - offender.getxPosition();
            else
                spots_moved_on_x = offender.getxPosition() - xPos;

            if (offender.getyPosition() <= yPos)
                spots_moved_on_y = xPos - offender.getyPosition();
            else
                spots_moved_on_y = offender.getyPosition() - xPos;

            if (spots_moved_on_x + spots_moved_on_y > effect.getnMoves())
                System.out.println("can't move here");
            else
                movePlayer(offender.getNickname(), xPos, yPos);
        }

        if (effect.getnMovesOtherPlayer() != 0) {
            System.out.println("where do you want to move the player you attack? maximum effect.nMovesOtherPlayer moves");       //expect a new spot

            int xPos= 1, yPos = 2;     //spot where the user wants to move the other player

            if (defender.getxPosition() <= xPos)
                spots_moved_on_x = xPos - defender.getxPosition();
            else
                spots_moved_on_x = defender.getxPosition() - xPos;

            if (offender.getyPosition() <= yPos)
                spots_moved_on_y = xPos - defender.getyPosition();
            else
                spots_moved_on_y = defender.getyPosition() - xPos;

            if (spots_moved_on_x + spots_moved_on_y > effect.getnMovesOtherPlayer())
                System.out.println("can't move this player here");
            else
                movePlayer(defender.getNickname(), xPos, yPos);
        }

        if( effect.getCost() != null ){       //if there is a cost I pay it ( for example the targeting scope )

            for ( Color cost : effect.getCost() ){
                if ( cost == Color.RED ){
                    if ( offender.getnRedAmmo() == 0 )
                        System.out.println( "You don't have enough ammo to do this optional attack");
                    else
                        offender.setnRedAmmo(offender.getnRedAmmo()-1);
                }
                if ( cost == Color.YELLOW ){
                    if ( offender.getnYellowAmmo() == 0 )
                        System.out.println( "You don't have enough ammo to do this optional attack");
                    else
                        offender.setnYellowAmmo(offender.getnYellowAmmo()-1);
                }
                if ( cost == Color.BLUE ){
                    if ( offender.getnBlueAmmo() == 0 )
                        System.out.println( "You don't have enough ammo to do this optional attack");
                    else
                        offender.setnBlueAmmo(offender.getnBlueAmmo()-1);
                }
                if ( cost == Color.ANY ){
                    if ( offender.getnRedAmmo() == 0 && offender.getnYellowAmmo() == 0 && offender.getnBlueAmmo() == 0 )
                        System.out.println( "You don't have enough ammo to do this optional attack");
                    else{
                        System.out.println( "Choose the color you want to pay with" );
                        /*TODO scale one of the color the user chose*/
                    }
                }
            }
        }
        if (effect.getVisibleByWho() == Visibility.NONE) { // for ( Player p : defenders )
            if (this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defender.getxPosition(), defender.getyPosition())) {
                System.out.println("Can't use this powerup");
            }
        }
        if (effect.getVisibleByWho() == Visibility.NONE) { // for ( Player p : defenders )
            if (!this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defender.getxPosition(), defender.getyPosition())) {
                System.out.println("Can't use this powerup");
            }
        }

        if (effect.isLinear()) {
            System.out.println("In which direction do you want to shoot?");
            //for example NORTH /*TODO look for scanf.
            // In this case if i already have the list of player i want to attack, i only have to check if someone is not in line and remove him.*/
        }

        //if a defender is not minDistance < |defender.position - offender.position| < MaxDistance cant't shoot him

        int spots_on_x, spots_on_y;

        if (offender.getxPosition() <= defender.getxPosition())
            spots_on_x = defender.getxPosition() - offender.getxPosition();
        else
            spots_on_x = offender.getxPosition() - defender.getxPosition();

        if (offender.getyPosition() <= defender.getyPosition())
            spots_on_y = defender.getyPosition() - offender.getyPosition();
        else
            spots_on_y = offender.getyPosition() - defender.getyPosition();

        if (spots_on_x + spots_on_y > effect.getMinDistance()) {
            System.out.println("Can't use this powerup");
        }
        if (spots_on_x + spots_on_y < effect.getMaxDistance()) {
            System.out.println("Can't use this powerup");
        }
        else {
            if( effect.getnPlayerAttackable() != 0 ) {
                defender.giveDamage(offender.getNickname(), effect.getnDamages());
            }

            if( effect.getnPlayerMarkable() != 0 ) {
                defender.giveMarks(offender.getNickname(), effect.getnDamages());
            }
        }
    }

    public boolean playerIsDead(String player) {
        Player p = getPlayerByNickname(player);

        return p.isDead();
    }

    /**
     * @param player is the current player
     * @param index index of the powerup
     * @return the powerup corresponding to index
     */
    public PowerUp getPowerupByIndex(String player, int index){
        Player currentPlayer = getPlayerByNickname(player);
        return currentPlayer.getPowerUpList().get(index);
    }

    /**
     * check the weapons that player has unloaded
     * @param player the player to check
     * @return an arraylist of the weapons unloaded
     */

    public ArrayList<Weapon> checkRechargeableWeapons (String player){
        Player currentPlayer = getPlayerByNickname(player);
        ArrayList<Weapon> rechargeableWeapons = new ArrayList<>();

        for (int i = 0; i < currentPlayer.getWeaponList().size(); i++){
            //first i need the cost of the weapon unloaded
            if (!currentPlayer.getWeaponList().get(i).isLoaded()) {
                ArrayList<Color> weaponCost = currentPlayer.getWeaponList().get(i).getCost();

                //cost of the weapon divided
                int nRedAmmoWeapon = Collections.frequency(weaponCost, Color.RED);
                int nBlueAmmoWeapon = Collections.frequency(weaponCost, Color.BLUE);
                int nYellowAmmoWeapon = Collections.frequency(weaponCost, Color.YELLOW);

                //add in the list the weapons that the player can reload with his ammo
                if (nRedAmmoWeapon < currentPlayer.getnRedAmmo() && nYellowAmmoWeapon < currentPlayer.getnYellowAmmo() && nBlueAmmoWeapon < currentPlayer.getnBlueAmmo())
                    rechargeableWeapons.add(currentPlayer.getWeaponList().get(i));
            }
        }

        return rechargeableWeapons;
    }

    /**
     * Tells if the player has any power up that can be used at any time during the turn
     * @param player the player to chech
     * @return
     */
    public boolean playerHasTurnPowerUp(String player) {
        Player p = getPlayerByNickname(player);

        if(p.hasTurnPowerUp())
            return true;
        return false;
    }

    public ArrayList<String> getAttackablePlayersPowerUp(String player, PowerUp powerUpToUse) {
        //TODO
        return new ArrayList<>();
    }

    /**
     * reload the weapon of the player corresponding to index , i am sure that i can reload that weapon -> see ReloadWeapon in controller.java
     * @param player
     * @param index
     */
    public void reloadWeapon(String player, int index){

        Player currentPlayer = getPlayerByNickname(player);

        currentPlayer.reloadWeapon(index);
    }

    /**
     * this method receive a player and a weapon
     * @param player the player to examinate
     * @param weaponToReload the weapon that we search in the player hand
     * @return the index of the weapon in the hand of the player
     */
    public int getRealWeaponIndexOfTheUnloadedWeapon(String player, Weapon weaponToReload){
        int realIndex = -1;
        Player currentPlayer = getPlayerByNickname(player);

        for (int i = 0; i < currentPlayer.getWeaponList().size(); i++ )
            if (currentPlayer.getWeaponList().get(i).equals(weaponToReload))
                realIndex = i;

        return realIndex;
    }

    public void executeMove(int index){
        //todo 0->move 1->move&grab 2->shoot
        //might be better to create an enum for this
    }

    /**
     * Revives the player
     * @param playerName the nickname of the player
     */
    protected void revive(String playerName) {

        Player p = getPlayerByNickname(playerName);

        p.revive();

    }

    /**
     * @return a copy of the list of Players
     */
    protected ArrayList<Player> clonePlayers(){
        return new ArrayList<>(this.players);
    }

    /**
     * @return a copy of the kill shot track
     */
    protected KillShotTrack cloneKST(){
        return this.kst.clone();
    }

    /**
     * @return a copy of the game map
     */
    protected GameMap cloneGameMap(){
        return this.gameMap.clone();
    }
}

