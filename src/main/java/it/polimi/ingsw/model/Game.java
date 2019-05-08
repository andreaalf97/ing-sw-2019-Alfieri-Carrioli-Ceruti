package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.map.GameMap;
import it.polimi.ingsw.model.map.MapBuilder;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.model.cards.Visibility;
import it.polimi.ingsw.model.map.Spot;

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

    //TESTED
    /**
     * getter for players nicknames in the game
     * @return players in game
     */
    public ArrayList<String> getPlayerNames() {
        return new ArrayList<>(this.playerNames);
    }

    //TESTED
    /**
     * getter for player powerup
     * @param player the player
     * @return player powerup list
     */
    public ArrayList<PowerUp> getPlayerPowerUps(String player) {

        Player p = this.getPlayerByNickname(player);

        return p.getPowerUpList();
    }

    //TESTED
    /**
     * this method return the object player corresponding to nickname
     * @param nickname the nickname of the player
     * @return the object Player
     */
    public Player getPlayerByNickname(String nickname){

        if(!playerNames.contains(nickname))
            throw new IllegalArgumentException("This nickname does not exist!");

        return players.get(playerNames.indexOf(nickname));
    }

    //TESTED
    /**
     * @param player is the current player
     * @param index index of the powerup
     * @return the powerup corresponding to index
     */
    public PowerUp getPowerUpByIndex(String player, int index){
        Player currentPlayer = getPlayerByNickname(player);
        return currentPlayer.getPowerUpList().get(index);
    }


    //TESTED
    /**
     * check if skulls on kst are finished so we can set up FrenzyTurm
     * @return true if there are no skulls on kst
     */
    public boolean noMoreSkullsOnKST() {
        return this.kst.noMoreSkulls();
    }

    //##########################################################################################################


    //TODO DO THIS
    /**
     * This method sets up all player for the final frenzy turn
     */
    public void setupForFrenzy () {

        ArrayList<String> fromKillerOn = new ArrayList<>();
        ArrayList<String> fromStartPlayerOn = new ArrayList<>();

        ArrayList<String> skullList = kst.getSkullList();

        String killer = skullList.get(skullList.size() - 1);

        fromKillerOn.add(killer);

        String nextPlayer = getNextPlayer(killer);
        while (!(players.get(0).equals(nextPlayer))){
            fromKillerOn.add(nextPlayer);
            nextPlayer = getNextPlayer(nextPlayer);
        }

        while (!(fromStartPlayerOn.equals(killer))){
            fromStartPlayerOn.add(nextPlayer);
            nextPlayer = getNextPlayer(nextPlayer);
        }

        for(String i : fromKillerOn){

            Player p = getPlayerByNickname(i);
            p.setNMoves(4);
            p.setNMovesBeforeGrabbing(2);
            p.setCanReloadBeforeShooting(true);
            p.setNMovesBeforeShooting(1);
            p.getPlayerStatus().nMovesDone = 0;

        }

        for(String i : fromStartPlayerOn){

            Player p = getPlayerByNickname(i);
            p.setNMoves(0);
            p.setNMovesBeforeGrabbing(3);
            p.setCanReloadBeforeShooting(true);
            p.setNMovesBeforeShooting(2);
            p.getPlayerStatus().nMovesDone = 1;
        }



    }

    //TESTED
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

    //USED IN TESTS
    /**
     * Gives the chosen weapon to a player
     * @param weapon The weapon object to be given
     * @param player The nickname of the player
     */
    public void giveWeaponToPlayer(String player, Weapon weapon){
        Player p = getPlayerByNickname(player);
        p.giveWeapon(weapon);
    }

    //TESTED
    /**
     * This method checks if any player is dead and counts their boards assigning the right amount of point to each player.
     * It is usually executed at the end of each turn
     */
    public void checkDeaths() {
        //Log.LOGGER.info("Checking deaths...");

        for(Player i : players){
            if(i.isDead()){
                giveBoardPointsAndModifyKST(i);
            }
        }
    }

    //TESTED
    /**
     * Clears the player board and gives points to all players involved, this method also modify kst and assign mark
     */
    protected void giveBoardPointsAndModifyKST ( Player player  ) throws RuntimeException {
        if(!player.isDead())
            throw new RuntimeException("This player is not dead");

        //at this point i am sure that player is dead, so i can modify kst and give one mark to the last player
        if (player.getDamages().size() == 12) {
            this.kst.addKill(player.getDamages().get(11), true);
            getPlayerByNickname(player.getDamages().get(11)).giveMarks(player.getNickname(), 1);
        }
        else
            this.kst.addKill(player.getDamages().get(10), false);

        ArrayList<Integer> pointValues = new ArrayList<>();
        pointValues.add(8);
        pointValues.add(6);
        pointValues.add(4);
        pointValues.add(2);
        pointValues.add(1);
        pointValues.add(1);

        getPlayerByNickname( player.getDamages().get(0) ).givePoints(1);            //first blood point

        for(int k = 0; k < player.getnDeaths(); k++)
            pointValues.remove(0);

        ArrayList<String> ranking = player.getOffendersRanking();

        for (int i = 0; i < ranking.size() && i < pointValues.size(); i++)
            getPlayerByNickname(ranking.get(i)).givePoints(pointValues.get(i));

        player.resetDamages();
        player.addKill();
    }

    //TESTED
    /**
     * Clears the player frenzy board and gives points to all players involved
     */
    public void giveFrenzyBoardPoints ( Player player) {

        int[] pointValues = {2, 1, 1, 1};

        ArrayList<String> ranking = player.getOffendersRanking();

        for (int i = 0; i < ranking.size(); i++)
            getPlayerByNickname(ranking.get(i)).givePoints(pointValues[i]);
    }

    //TESTED
    /**
     * Clears the Kill Shot Track and gives points to all players involved
     */
    public void giveKSTPoints(){
        int[] pointValues = {8, 6, 4, 2, 1, 1};

        ArrayList<String> ranking = kst.getRanking();

        for (int i = 0; i < ranking.size(); i++)
            getPlayerByNickname(ranking.get(i)).givePoints(pointValues[i]);

    }

    //TESTED
    /**
     * Show all the different spots where a player is allowed to move
     * @param player The nickname of the player
     * @param nMoves The amount of required moves
     * @return A boolean matrix where the <i, j> element is true only if the player is allowed to move there
     */
    public boolean[][] wherePlayerCanMove(String player, int nMoves){
        return this.gameMap.wherePlayerCanMove(player, nMoves);
    }

    //TESTED
    /**
     * Show all the different spots where a player is allowed to move and grab
     * @param player The nickname of the player
     * @param nMoves The amount of required moves
     * @return A boolean matrix where the <i, j> element is true only if the player is allowed to move there and there is ammo to grab
     */
    public boolean[][] wherePlayerCanMoveAndGrab(String player, int nMoves){
        return this.gameMap.wherePlayerCanMoveAndGrab(player, nMoves);
    }

    //TESTED
    /**
     * Lets the player pick a new powerup card and choose the new spawn spot to respawn
     * @param player the nickname of the player
     */
    public void respawn(String player, int powerupIndexToDiscard){

        Player p = getPlayerByNickname(player);

        if(!p.isDead())
            throw new RuntimeException("The player you want to respawn is not dead!");

        Color discardedColor = p.discardPowerUpByIndex(powerupIndexToDiscard);

        //set player alive and move player to spawnspot
        p.revive();
        movePlayerToSpawnColor(player, discardedColor);
    }

    //TESTED - PRIVATE METHOD
    /**
     * this method moves the player to the spawn spot associated to that color
     * @param player the player to respawn
     * @param discardedColor the color of the powerup discarded
     */
    private void movePlayerToSpawnColor(String player, Color discardedColor) {
        gameMap.movePlayerToColorSpawn(player, discardedColor);
        int[] coord = gameMap.getPlayerSpotCoord(player);

        Player p = getPlayerByNickname(player);
        //last action is modifing the player coordinates
        p.moveTo(coord[0], coord[1]);
    }

    //TESTED
    /**
     * Refills all the ammo spots
     */
    public void refillAllAmmoSpots(){
        this.gameMap.refillAllAmmo(this.powerupDeck);
    }

    //TESTED
    /**
     * Refills all the spawn spots with weapons
     */
    public void refillAllSpawnSpots(){
        this.gameMap.refillAllSpawns(this.weaponDeck);
    }

    //TESTED
    /**
     * Moves the selected player to the new spot
     * @param player the nickname of the player
     * @param x the position of the spot on the x-axis
     * @param y the position of the spot on the y-axis
     */
    protected void movePlayer(String player, int x, int y){

        Player p = getPlayerByNickname(player);

        if (gameMap.validSpot(x ,y)) {
            p.moveTo(x, y);
            gameMap.movePlayer(player, x, y);
        }
    }

    //TESTED
    /**
     * Moves the player to the selected spot and grabs the ammos on it
     * @param player the nickname of the player
     * @param x the position of the spot on the x-axis
     * @param y the position of the spot on the y-axis
     */
    public void moveAndGrab(String player, int x, int y, int index){

        Player p = getPlayerByNickname(player);

        if (gameMap.validSpot(x ,y)) {
            p.moveTo(x, y);
            gameMap.movePlayer(player, x, y);
            gameMap.grabSomething(x, y, p, index);
        }
    }

    //TODO TEST
    /**
     * Tells if player offender can shoot player defenders with the selected weapon
     * @param offendername The nickname of the player who shoots
     * @param defenders The nickname of the players the offender wants to shoot
     * @param effect The effect of the weapon
     * @return The players that get shot during this effect
     */
    public ArrayList<Player> whoP1CanShootInThisEffect(String offendername, ArrayList<Player> defenders, Effect effect, ArrayList<Player> playersHit) throws InvalidChoiceException{

        ArrayList<Player> defenders_temp = new ArrayList<>();

        Player offender = getPlayerByNickname(offendername);

        if (effect.getVisibleByWho() == Visibility.NONE) { // for ( Player p : defenders )
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable()); i++) {   //controllo i defenders, se qualcuno non rispetta vuol dire che l'utente ha dato input sbagliati, si ferma il suo attacco
                if (this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition())) {
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -NONE");
                }
            }
        }
        if (effect.getVisibleByWho() == Visibility.OFFENDER) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable()); i++) {   //controllo i defenders, se qualcuno non rispetta visibility lo escludo
                if (!this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition())) {
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -OFFENDER");
                }
            }
        }

        //TODO cambiare il modo per calcolare la distanza tra due spots
        for (int i = 0; i < defenders.size() && (i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable()); i++) {      //if a defender is not minDistance < |defender.position - offender.position| < MaxDistance remove him.

            int spots_on_x, spots_on_y;

            if (offender.getxPosition() <= defenders.get(i).getxPosition())
                spots_on_x = defenders.get(i).getxPosition() - offender.getxPosition();
            else
                spots_on_x = offender.getxPosition() - defenders.get(i).getxPosition();

            if (offender.getyPosition() <= defenders.get(i).getyPosition())
                spots_on_y = defenders.get(i).getyPosition() - offender.getyPosition();
            else
                spots_on_y = offender.getyPosition() - defenders.get(i).getyPosition();

            if (spots_on_x + spots_on_y < effect.getMinDistance()) {
                throw new InvalidChoiceException("qualche defender non rispetta la distanza per questo effetto MinDistance");
            }
            if (spots_on_x + spots_on_y > effect.getMaxDistance()) {
                throw new InvalidChoiceException("qualche defender non rispetta la distanza per questo effetto MaxDistance");
            }


            if (effect.getVisibleByWho() == Visibility.LASTONEATTACKED) {
                if (playersHit.isEmpty()) {
                    throw new InvalidChoiceException("playersHit is empty and visibility is LASTONEATTACKED");
                }
                if (!this.gameMap.see(playersHit.get(playersHit.size() - 1).getxPosition(), playersHit.get(playersHit.size() - 1).getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition())) {
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -LASTONEATTACKED");
                }
            }
            if (effect.mustShootOtherPlayers()) {       //per ogni player in defenders scorro i players in playersHit, se ne trovo due uguali lancio eccezione

                if (playersHit.isEmpty()) {
                    throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1");
                }
                for (int k = 0; k < playersHit.size() - 1; k++) {
                    if (defenders.get(i) == playersHit.get(k)) {
                        throw new InvalidChoiceException("cercando di sparare ad un giocatore già colpito, non permesso in questo attacco");
                    }
                }
            }
            //vuol dire che devo sparare ai giocatori a cui avevo già sparato
            /**if (effect.mustShootSamePlayers()) {       //per ogni player in defenders scorro i players in playersHit, se ne trovo due uguali lancio eccezione

                if (playersHit.isEmpty()) {
                    throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1");
                }
                for (int k = 0; k < playersHit.size() - 1; k++) {
                    if (defenders.get(i) == playersHit.get(k)) {
                        throw new InvalidChoiceException("cercando di sparare ad un giocatore già colpito, non permesso in questo attacco");
                    }
                }
            }*/

            if( effect.mustBeOtherRoom()) {
                if ( this.gameMap.getPlayerRoom(offendername) == this.gameMap.getPlayerRoom(defenders.get(i).getNickname())) {
                    throw new InvalidChoiceException("almeno un defender è nella stessa stranza dell'offender -MustBeOtherRoom ");
                }
            }
        }

        if (effect.mustBeDifferentSpots()) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable()); i++){
                for (int j = 0; i < defenders.size() && (j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable()) && i != j; i++) {
                    if ((defenders.get(i).getxPosition() == defenders.get(j).getxPosition() && defenders.get(i).getyPosition() == defenders.get(j).getyPosition())) {     //se due giocatori si trovano nello stesso spot significa che l'utente ha dato informazioni sbagliate, lancio eccezione
                        throw new InvalidChoiceException("almeno due giocatori non rispettano mustBeDifferentSpots");
                    }
                }
            }
        }
        /*if (effect.mustBeSameSpots()) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable()); i++){
                for (int j = 0; i < defenders.size() && (j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable()) && i != j; i++) {
                    if ((defenders.get(i).getxPosition() != defenders.get(j).getxPosition() || defenders.get(i).getyPosition() != defenders.get(j).getyPosition())) {     //se due giocatori si trovano in spot diversi significa che l'utente ha dato informazioni sbagliate, lancio eccezione
                        throw new InvalidChoiceException("almeno due giocatori non rispettano mustBeSameSpots");
                    }
                }
            }
        }*/

        if (effect.isLinear()) {

            for (int i = 0; i < defenders.size() && (i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable()); i++){

                //se sia x che y sono diverse il defender è sicuramente non allineato
                if (defenders.get(i).getxPosition() != offender.getxPosition() || defenders.get(i).getyPosition() != offender.getyPosition()) {
                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_1");
                }

                //controllo se offender e defender.get(i) non siano nello stesso spot, se così fosse andrebbe bene ma non sarebbe ancora decisa la direzione che devono rispettare i defenders successivi
                if (defenders.get(i).getxPosition() != offender.getxPosition() || defenders.get(i).getyPosition() != offender.getyPosition()) {

                    if (defenders.get(i).getxPosition() == offender.getxPosition()) {      //offender e defender sono sulla stessa riga

                        if (defenders.get(i).getyPosition() > offender.getyPosition()) { //iL defender è a EAST rispetto all'offender

                            //controllo se anche gli altri defenders sono a EAST rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders.size() && (j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable()); j++){
                                if (defenders.get(j).getyPosition() < offender.getyPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_2");
                                }
                            }
                        }else
                        if (defenders.get(i).getyPosition() < offender.getyPosition()) { //il primo defender è a WEST rispetto all'offender
                            //controllo se anche gli altri defenders sono a WEAST rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders.size() &&  (j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable()); j++){
                                if (defenders.get(j).getyPosition() > offender.getyPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_3");
                                }
                            }
                        }
                    }
                    if (defenders.get(i).getyPosition() == offender.getyPosition()) {      //offender e il defender sono sulla stessa colonna

                        if (defenders.get(i).getxPosition() > offender.getxPosition()) {   //il defender è a SOUTH rispetto all'offender
                            //controllo se anche gli altri defenders sono a SOUTH rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders.size() && (j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable()); j++){
                                if (defenders.get(j).getxPosition() < offender.getxPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_4");
                                }
                            }
                        }else
                        if (defenders.get(0).getxPosition() < offender.getxPosition()) {   //il defender è a NORTH rispetto all'offender
                            //controllo se anche gli altri defenders sono a NORTH rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders.size() && (j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable()); j++){
                                if (defenders.get(j).getxPosition() > offender.getxPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_5");
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < defenders.size() && (i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable()); i++) {
            defenders_temp.add(defenders.get(i));       //questi sono i giocatori a cui effettivamente faccio danno
        }
        return defenders_temp;
    }

    /**
     * this method checks what type of effect effect is
     * @param effect the effect to check
     * @return the type of this effect. 0 = movemente effect, 1 = damage effect
     */
    public int typeOfEffect( Effect effect ){

        int type = 0;

        if (effect.getnMoves() != 0) {      //this.player movement
            type = 0;
        }
        if (effect.getnMovesOtherPlayer() != 0) {      //other player movement
            type = 0;
        }
        if (effect.getnPlayerAttackable() != 0|| effect.getnPlayerMarkable() != 0) {      //damage effect
            type = 1;
        }
        return type;
    }

    /**
     * this method check if the player can pay the cost in effect, then pays it
     * @param effect the effect we have to see to pay his cost
     * @param player the player who has to pay
     * @return true if player pays, false if the player can't pay
     */
    public boolean payCostEffect( Effect effect, String player ) {

        if (effect.getCost() != null) {       //if there is a cost I pay it ( for example an optional shooting action )
            try{
                pay( player, effect.getCost() );
                return true;
            }
            catch(InvalidChoiceException e){
                return false;
            }
        }
        return true;
    }

    /**
     * This method is called by ShootWithMovemente() and makes the actual effect of movement
     * @param string is the player being moved
     * @param effect is the movement effect we have to look at
     * @param xPos is the  x position of the player after he moved
     * @param yPos is the  y position of the player after he moved
     * @throws InvalidChoiceException
     */
    public void makeMovementEffect(String string, Effect effect, ArrayList<Integer> xPos, ArrayList<Integer> yPos) throws InvalidChoiceException{

        Player player = getPlayerByNickname(string);

        int i = 0;

        if ( effect.getnMoves() != 0){
            if( this.gameMap.canMoveFromTo(player.getxPosition(), player.getyPosition(), xPos.get(i), yPos.get(i), effect.getnMoves()) ) {
                movePlayer(player.getNickname(),xPos.get(i), yPos.get(i));
                xPos.remove(i);
                yPos.remove(i);

            }else
                throw new InvalidChoiceException("giocatore spostato di number of spots != nMoves");
        }
        if ( effect.getnMovesOtherPlayer() != 0) {
            if (this.gameMap.canMoveFromTo(player.getxPosition(), player.getyPosition(), xPos.get(i), yPos.get(i), effect.getnMovesOtherPlayer())) {
                movePlayer(player.getNickname(), xPos.get(i), yPos.get(i));
                xPos.remove(i);
                yPos.remove(i);
            } else
                throw new InvalidChoiceException("giocatore spostato di number of spots != nMovesOtherPlayer");
        }
    }

    /**
     * This method is called by ShootWithMovemente() and ShootWithMovemente() and makes the actual effect of damage
     * @param offendername is the player who makes damage
     * @param defenders_temp are the players who receive damage
     * @param effect is the effect we have to look at
     */
    public void makeDamageEffect(String offendername, ArrayList<Player> defenders_temp, Effect effect){

        Player offender = getPlayerByNickname(offendername);

        if (effect.getnPlayerAttackable() != 0) {       //per ogni giocatore a cui bisogna dare danni presente nella lista dei defenders, assegno nDamages dell'effetto
            for (int i = 0; i < defenders_temp.size() && i < effect.getnPlayerAttackable(); i++) {
                defenders_temp.get(i).giveDamage(offendername, effect.getnDamages());
            }
        }

        if (effect.getnPlayerMarkable() != 0) {     //per ogni giocatore a cui bisogna dare marchi presente nella lista dei defenders, assegno nMarks dell'effetto
            for (int i = 0; i < defenders_temp.size() && i < effect.getnPlayerMarkable(); i++) {
                defenders_temp.get(i).giveMarks(offendername, effect.getnMarks());
            }
        }
    }

    /**
     * This method, called from the controller, makes the actual shooting of the weapon with movement effects
     * @param offenderName is the player who uses the weapon
     * @param defendersNames are the players who receive the effects
     * @param weapon is the weapon being used to shoot
     * @param orderNumber is the order of the effect chose by the user
     * @param xPosition is the new x position of the player being moved
     * @param yPosition is the new y position of the player being moved
     * @param playerWhoMoves is the player who moves
     * @return return true if all the checks on the effects are executed the right way
     * @throws InvalidChoiceException
     */
    public boolean shootWithMovement(String offenderName, ArrayList<String> defendersNames, Weapon weapon, int orderNumber, ArrayList<Integer> xPosition, ArrayList<Integer> yPosition, String playerWhoMoves){

        ArrayList<Player> defenders = new ArrayList<>();

        ArrayList<Player> defenders_temp = new ArrayList<>();

        ArrayList<Player> playersHit = new ArrayList<>();

        GameMap backUpMap = new GameMap(this.gameMap);

        ArrayList<Player> backUpPlayers = new ArrayList<>();

        for(Player p : this.players) {
            backUpPlayers.add(new Player(p));
        }

        for (String i : defendersNames)
            defenders.add(getPlayerByNickname(i));

        try {
            //se l'arma è scarica non si può sparare, il giocatore perde un'azione
            if (!weapon.isLoaded())
                throw new InvalidChoiceException("This weapon is not loaded");

            for (int i : weapon.getOrder().get(orderNumber)) {      //scorro gli effetti di quest'arma nell'ordine scelto dall'utente

                Effect effetto = weapon.getEffects().get(i);

                if (typeOfEffect(effetto) == 0) { //Movement effect

                    makeMovementEffect(playerWhoMoves, effetto, xPosition, yPosition);

                    if (!payCostEffect(effetto, offenderName)) {   //if the effect has a cost, the player pays it
                        throw new InvalidChoiceException("Cannot pay");
                    }
                }

                if (typeOfEffect(effetto) == 1) {  //Damage effect

                    /* a whoP1ShootsInThisEffect gli passo al primo giro la lista dei nomi dei players ricevuta dall'utente, ai giri successivi i players a cui non ho ancora sparato, cioè quelli a cui devo sparare in questo effetto.
                    Questo metodo mi ritorna una lista di player che effettivamente vengono colpiti/marchiati durante quest'effetto. Quindi li aggiungo a PlayersHit    */

                    defenders_temp = whoP1CanShootInThisEffect(offenderName, defenders, effetto, playersHit);

                    if(defenders_temp.isEmpty())
                        return true;

                    if (!payCostEffect(effetto, offenderName)) {   //if the effect has a cost, the player pays it
                        throw new InvalidChoiceException("Cannot pay");
                    }

                     /*rimuovo da defendesNames i giocatori che ho colpito nell'effetto appena eseguito, così nel prossimo giro nel ciclo,
                     ovvero nel prossimo effetto, escludo i giocatori colpiti nell'effetto precedente (esempio se il giovatore vuole colpire andreaalf
                     poi gino poi andreaalf ( ma nel primo effetto si ha nPlayersAttackable = 2 ), WhoP1ShootsInThisEffect ritorna andreaalf, gino, li colpisco e li aggiungo a playersHit, nel prossimo effetto passo a
                     whoP1CanShootInThisEffect defenders - defenders_temp, ovvero andreaalf (poi se c'è un must_shoot_other player in questo effetto lancio un'eccezione*/
                    for (Player p : defenders_temp) {
                        playersHit.add(p);
                        defenders.remove(p);
                    }
                    makeDamageEffect(offenderName, defenders_temp, effetto);
                }
            }
            if (defenders.size()!= 0){
                throw new InvalidChoiceException("Too many players for this weapon");
            }
            weapon.setLoaded(false);
            return true;
        } catch (InvalidChoiceException e) {
            //resetto mappa
            this.gameMap = new GameMap(backUpMap);
            //resetto tutti i players in this game
            this.players = new ArrayList<>(backUpPlayers);
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    /**
     * This method, called from the controller, makes the actual shooting of the weapon without movement effects
     * @param offenderName is the player who uses the weapon
     * @param defendersNames are the players who receive the effects
     * @param weapon is the weapon being used to shoot
     * @param orderNumber is the order of the effect chose by the user
     * @return return true if all the checks on the effects are executed the right way
     */
    public boolean shootWithoutMovement(String offenderName, ArrayList<String> defendersNames, Weapon weapon, int orderNumber) {

        ArrayList<Player> defenders = new ArrayList<>();

        ArrayList<Player> defenders_temp = new ArrayList<>();

        ArrayList<Player> playersHit = new ArrayList<>();

        ArrayList<Player> backUpPlayers = new ArrayList<>();

        GameMap backUpMap = new GameMap(this.gameMap);

        for(Player p : this.players) {
            backUpPlayers.add(new Player(p));
        }

        //defenders è la lista di tutti i giocatori che arriva dall'utente
        for (String i : defendersNames)
            defenders.add(getPlayerByNickname(i));

        try {
            //se l'arma è scarica non si può sparare, il giocatore perde un'azione
            if (!weapon.isLoaded())
                throw new InvalidChoiceException("This weapon is not loaded");

            for (int i : weapon.getOrder().get(orderNumber)) {      //scorro gli effetti di quest'arma nell'ordine scelto dall'utente

                Effect effetto = weapon.getEffects().get(i);

                if (typeOfEffect(effetto) == 1) {  //Damage effect

                    /* a whoP1ShootsInThisEffect gli passo al primo giro la lista dei nomi dei players ricevuta dall'utente, ai giri successivi i players a cui non ho ancora sparato, cioè quelli a cui devo sparare in questo effetto.
                    Questo metodo mi ritorna una lista di player che effettivamente vengono colpiti/marchiati durante quest'effetto. Quindi li aggiungo a PlayersHit    */

                    defenders_temp = whoP1CanShootInThisEffect(offenderName, defenders, effetto, playersHit);
                    if ( defenders_temp.isEmpty() ){
                        return true;
                    }

                    if (!payCostEffect(effetto, offenderName)) {   //if the effect has a cost, the player pays it
                        throw new InvalidChoiceException("Cannot pay");
                    }

                     /*rimuovo da defendesNames i giocatori che ho colpito nell'effetto appena eseguito, così nel prossimo giro nel ciclo,
                     ovvero nel prossimo effetto, escludo i giocatori colpiti nell'effetto precedente (esempio se il giovatore vuole colpire andreaalf
                     poi gino poi andreaalf ( ma nel primo effetto si ha nPlayersAttackable = 2 ), WhoP1ShootsInThisEffect ritorna andreaalf, gino, li colpisco e li aggiungo a playersHit, nel prossimo effetto passo a
                     whoP1CanShootInThisEffect defenders - defenders_temp, ovvero andreaalf (poi se c'è un must_shoot_other player in questo effetto lancio un'eccezione*/
                    for (Player p : defenders_temp) {
                        playersHit.add(p);
                        defenders.remove(p);
                        //defendersNames.remove(p.getNickname());
                    }
                    makeDamageEffect(offenderName, defenders_temp, effetto);
                }
            }
            if (defenders.size()!= 0) {
                throw new InvalidChoiceException("Too many players for this weapon");
            }
            weapon.setLoaded(false);
            return true;
        } catch (InvalidChoiceException e) {
            //resetto mappa
            this.gameMap = new GameMap(backUpMap);
            //resetto tutti i players in this game
            this.players = new ArrayList<>(backUpPlayers);
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    /**
     * This method uses a power with damage effect
     * @param currentPlayerName is the player who uses the powerup
     * @param playerWhoReceiveEffectName is the player who receives the effect of the power up
     * @param effect is the effect of the power up
     * @throws InvalidChoiceException
     */
    public void useDamagePowerUp( String currentPlayerName, String playerWhoReceiveEffectName, Effect effect ) throws InvalidChoiceException{

        Player  playerWhoReceiveEffect = getPlayerByNickname(playerWhoReceiveEffectName);

        //creo un ArrayList<Player> in cui ci sarà solo un player per passarlo a makeDamageEffect
        ArrayList<Player> singlePlayerArray = new ArrayList<>();
        singlePlayerArray.add(playerWhoReceiveEffect);

        if (!payCostEffect(effect, currentPlayerName)) {   //if the effect has a cost, the player pays it
            throw new InvalidChoiceException("Cannot pay");
        }
        makeDamageEffect( currentPlayerName, singlePlayerArray, effect );
    }

    /**
     * This method uses a power with movement effect
     * @param currentPlayerName  is the player who uses the powerup
     * @param playerWhoReceiveEffectName is the player who receives the effect of the power up
     * @param effect is the effect of the power up
     * @param xPos is the new x position of the player being moved
     * @param yPos is the new y position of the player being moved
     * @throws InvalidChoiceException
     */
    public void useMovementPowerUp ( String currentPlayerName, String playerWhoReceiveEffectName, Effect effect, int xPos, int yPos )throws InvalidChoiceException{

        Player  playerWhoReceiveEffect = getPlayerByNickname(playerWhoReceiveEffectName);

        if (!payCostEffect(effect, currentPlayerName)) {   //if the effect has a cost, the player pays it
            throw new InvalidChoiceException("Cannot pay");
        }


        if (effect.getnMoves() != 0 || effect.getnMovesOtherPlayer() != 0) {
            if ( effect.getnMoves() != 0){
                if( this.gameMap.canMoveFromTo(playerWhoReceiveEffect.getxPosition(), playerWhoReceiveEffect.getyPosition(), xPos, yPos, effect.getnMoves()) ) {
                    movePlayer(playerWhoReceiveEffect.getNickname(), xPos, yPos);
                }else
                    throw new InvalidChoiceException("giocatore spostato di number of spots != nMoves o nMovesOtherPlayer");
            }

        }
    }

    public ArrayList<String> getAttackablePlayersPowerUp(String player, PowerUp powerUpToUse) {
        //TODO
        return new ArrayList<>();
    }

    //TESTED
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

    //todo i don't think that a return boolean method like this need to be tested
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

    //TESTED
    /**
     * reload the weapon of the player corresponding to index , i am sure that i can reload that weapon -> see ReloadWeapon in controller.java
     * @param player
     * @param index
     */
    public void reloadWeapon(String player, int index){

        Player currentPlayer = getPlayerByNickname(player);

        currentPlayer.reloadWeapon(index);
    }

    //TESTED
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

    //TODO MAKE THE CONTROLLER SEND THE INDEX CORRESPONDING TO MOVE
    /**
     * method that execute the move corresponding to index
     * @param index the move representation
     */
    public void executeMove(int index){
        //todo 0->move 1->move&grab 2->shoot
        //might be better to create an enum for this
    }

    //ONLY USED IN TESTS
    /**
     * Revives the player
     * @param playerName the nickname of the player
     */
    protected void revive(String playerName) {

        Player p = getPlayerByNickname(playerName);

        p.revive();

    }

    //TESTED
    /**
     * @return a copy of the list of Players
     */
    protected ArrayList<Player> clonePlayers(){
        return new ArrayList<>(this.players);
    }

    //TESTED
    /**
     * @return a copy of the kill shot track
     */
    protected KillShotTrack cloneKST(){
        return this.kst.clone();
    }

    //TESTED
    /**
     * @return a copy of the game map
     */
    protected GameMap cloneGameMap(){
        return this.gameMap.clone();
    }

    //TESTED
    /**
     * Makes the player pay the correct amount of ammo
     * @param nickname the nickname
     * @param cost the cost to pay
     */
    public void pay(String nickname, ArrayList<Color> cost) throws InvalidChoiceException, RuntimeException{

        Player p = getPlayerByNickname(nickname);

        for(Color i : cost){

            switch (i){
                case RED:
                    if(p.getnRedAmmo() == 0)
                        throw new InvalidChoiceException("Not enough red ammo");
                    break;
                case BLUE:
                    if(p.getnBlueAmmo() == 0)
                        throw new InvalidChoiceException("Not enough blue ammo");
                    break;
                case YELLOW:
                    if(p.getnYellowAmmo() == 0)
                        throw new InvalidChoiceException("Not enough yellow ammo");
                    break;
                case ANY:
                    throw new RuntimeException("Color.ANY can't appear here");
            }

            p.removeAmmo(i);
        }
    }

    //TESTED
    /**
     * Gives the ammo card on the spot to the player
     * @param player the player who grab ammo
     * @throws InvalidChoiceException if this isn't an ammo spot
     */
    public void giveAmmoCard(String player) throws RuntimeException{

        Player p = getPlayerByNickname(player);

        int x = p.getxPosition();
        int y = p.getyPosition();

        if(! gameMap.isAmmoSpot(x, y))
            throw new RuntimeException("This is not an ammo spot");

        gameMap.grabSomething(x, y, p, -1);
    }

    //TESTED
    /**
     * Returns a copy of the weapons on the spawn spot
     * @param roomColor the color of the spawn
     * @return a copy of the weapons
     */
    public ArrayList<Weapon> showSpawnSpotWeapons(Color roomColor){
        return gameMap.showSpawnSpotWeapons(roomColor);
    }

    //TESTED
    /**
     * pick a weapon from the spawnspot where the player is
     * @param nickname the player who wants to pick the weapon
     * @param index index of the weapon to pick in the spawnspot weapon list
     * @throws RuntimeException if this is not a spawn spot
     */
    public void pickWeapon(String nickname, int index) throws RuntimeException{

        Player p = getPlayerByNickname(nickname);

        int x = p.getxPosition();
        int y = p.getyPosition();

        if (!gameMap.isSpawnSpot(x, y))
            throw new RuntimeException("this is not a spawn spot");

        gameMap.grabSomething(x, y, p, index);

    }

    /**
     * switch a weapon of the player with a weapon on the spawnspot
     * @param player the player who switch the weapon
     * @param indexToDiscard index of the weapon to discard from player hand
     * @param indexToPick index of the weapon to pick from the spawn spot weapon list
     * @throws RuntimeException if the spot isn't a spawn spot
     */
    public void switchWeapons (String player, int indexToDiscard, int indexToPick) throws RuntimeException{

        Player p = getPlayerByNickname(player);

        int x = p.getxPosition();
        int y = p.getyPosition();

        if(!gameMap.isSpawnSpot(x, y))
            throw new RuntimeException("This is not a spawn spot");

        Weapon toDiscard = p.removeWeaponByIndex(indexToDiscard);

        toDiscard.reload();

        gameMap.grabSomething(x, y, p, indexToPick);


        gameMap.refill(x, y, toDiscard);


    }

    public void endTurnUpdateStatus() {

        Player current = getCurrentPlayer();

        String next = getNextPlayer(current.getNickname());

        current.endTurnCurrent();

        getPlayerByNickname(next).startTurn();
    }

    private String getNextPlayer(String current) {

        int currentIndex = players.indexOf(current);

        if(currentIndex == players.size() - 1)
            return players.get(0).getNickname();
        else
            return players.get(currentIndex + 1).getNickname();

    }

    private Player getCurrentPlayer() {
        for(Player i : players){
            if(i.isCurrentPlayer())
                return i;
        }
        throw new RuntimeException("didn't find the current player!");
    }

    //TESTED
    /**
     * tells if the spot is valid, this method is cretaed in gameMap.java, but we should know if spot is valid even in Game.java
     * @param x spot x
     * @param y spot y
     * @return true if spot is valid
     */
    public boolean validSpot(int x , int y){
        return gameMap.validSpot(x , y);
    }

    //TESTED  todo i have create this for testing in refillAllAmmoSpot and refillallSpawnSpot
    /**
     * this method returns a spot by using index
     * @param x the x of the spot
     * @param y the y of the spot
     * @return the spot corresponding to that index
     */
    public Spot getSpotByIndex(int x, int y){
        return gameMap.getSpotByIndex(x, y);
    }





}

