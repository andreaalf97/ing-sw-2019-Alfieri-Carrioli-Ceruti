package it.polimi.ingsw.model;


import com.google.gson.*;
import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.Observable;
import it.polimi.ingsw.controller.Actions;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.map.GameMap;
import it.polimi.ingsw.model.map.MapBuilder;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.model.cards.Visibility;
import it.polimi.ingsw.model.map.Spot;

import java.util.ArrayList;
import java.util.Collections;

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

    /**
     * creates a new game by deserializing it from parametere modelSnapshot
     * @param modelSnapshot the json in which we have all the model
     */
    public Game(String modelSnapshot){
        JsonObject jsonRoot = new JsonParser().parse(modelSnapshot).getAsJsonObject();

        this.players = deserializePlayerObject(jsonRoot.get("player").getAsJsonArray());

        this.playerNames = deserializePlayerNamesObject(jsonRoot.get("playerNames").getAsJsonArray());

        this.weaponDeck = new WeaponDeck(jsonRoot.get("weaponDeck").getAsJsonObject());

        this.powerupDeck = new PowerUpDeck(jsonRoot.get("powerUpDeck").getAsJsonObject());

        this.kst = new KillShotTrack(jsonRoot.get("kst").getAsJsonObject());

        this.gameMap = new GameMap(jsonRoot.get("gameMap").getAsJsonObject());

    }

    /**
     * this method deserialize Player Names objects
     * @param jsonPlayerNames the json of the players
     * @return the Players ArrayList
     */
    private ArrayList<String> deserializePlayerNamesObject(JsonArray jsonPlayerNames) {

        ArrayList<String> playerNames = new ArrayList<>();

        for(int i = 0; i < jsonPlayerNames.size(); i++)
            playerNames.add(jsonPlayerNames.get(i).getAsString());

        return playerNames;
    }

    /**
     * this method deserialize Players
     * @param jsonPlayers the json that represents the players
     * @return the corrresponding ArrayList
     */
    private ArrayList<Player> deserializePlayerObject(JsonArray jsonPlayers) {
        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < jsonPlayers.size(); i++) {
            Player p = new Player(jsonPlayers.get(i).getAsJsonObject());
            players.add(p);
        }

        return players;
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
            p.getPlayerStatus().nActionsDone = 0;

        }

        for(String i : fromStartPlayerOn){

            Player p = getPlayerByNickname(i);
            p.setNMoves(0);
            p.setNMovesBeforeGrabbing(3);
            p.setCanReloadBeforeShooting(true);
            p.setNMovesBeforeShooting(2);
            p.getPlayerStatus().nActionsDone = 1;
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

            MyLogger.LOGGER.log(Level.INFO, "deck is empty, reloading it from json");
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
        //MyLogger.LOGGER.info("Checking deaths...");

        for(Player i : players){
            if(!i.playerStatus.isFirstTurn && i.isDead()){
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
        //last Action is modifing the player coordinates
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
    public void movePlayer(String player, int x, int y){

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

        if (effect.isLinear()) {

            for ( int i = 0; i < defenders.size(); i++){
                //se sia x che y sono diverse il defender è sicuramente non allineato
                if (defenders.get(i).getxPosition() != offender.getxPosition() && defenders.get(i).getyPosition() != offender.getyPosition()) {
                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_1");
                }

                //controllo se offender e defender.get(i) non siano nello stesso spot, se così fosse andrebbe bene ma non sarebbe ancora decisa la direzione che devono rispettare i defenders successivi
                if (defenders.get(i).getxPosition() != offender.getxPosition() || defenders.get(i).getyPosition() != offender.getyPosition()) {

                    if (defenders.get(i).getxPosition() == offender.getxPosition()) {      //offender e defender sono sulla stessa riga

                        if (defenders.get(i).getyPosition() > offender.getyPosition()) { //iL defender è a EAST rispetto all'offender

                            //controllo se anche gli altri defenders sono a EAST rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++){
                                if (defenders.get(j).getxPosition() != offender.getxPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_2");
                                }
                                if (defenders.get(j).getyPosition() < offender.getyPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_3");
                                }
                            }
                        }else
                        if (defenders.get(i).getyPosition() < offender.getyPosition()) { //il primo defender è a WEST rispetto all'offender
                            //controllo se anche gli altri defenders sono a WEAST rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders.size() &&  (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++){
                                if (defenders.get(j).getxPosition() != offender.getxPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_4");
                                }
                                if (defenders.get(j).getyPosition() > offender.getyPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_5");
                                }
                            }
                        }
                    }
                    if (defenders.get(i).getyPosition() == offender.getyPosition()) {      //offender e il defender sono sulla stessa colonna

                        if (defenders.get(i).getxPosition() > offender.getxPosition()) {   //il defender è a SOUTH rispetto all'offender
                            //controllo se anche gli altri defenders sono a SOUTH rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++){
                                if (defenders.get(j).getyPosition() != offender.getyPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_6");
                                }
                                if (defenders.get(j).getxPosition() < offender.getxPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_7");
                                }
                            }
                        }else
                        if (defenders.get(0).getxPosition() < offender.getxPosition()) {   //il defender è a NORTH rispetto all'offender
                            //controllo se anche gli altri defenders sono a NORTH rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++){
                                if (defenders.get(j).getyPosition() != offender.getyPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_8");
                                }
                                if (defenders.get(j).getxPosition() > offender.getxPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_9");
                                }
                            }
                        }
                    }
                }
            }
        }

        //in questo caso devo colpire tutti i giocatori dentro un determinato spot (lo spot del primo defender in defenders). Per fare questo ho bisogno di contare quanti giocatori ci sono effettivamente dentro questo spot, così so fin dove scorrere la lista dei defenders per applicare l'effetto
        if ( effect.mustBeSameSpots() && (effect.getnPlayersAttackable() == 50 || effect.getnPlayersMarkable() == 50)){

            //questi sono i giocatori nello stesso post tra quelli passati dall'utente, escluso l'offender
            int nPlayersInTheSameSpot = 1;

            //questi sono tutti i giocatori che sono effettivamente in quello spot, escluso l'offender, presi dalla mappa. Se questo numero di giocatori è diverso da nPlayersInTheSameSpot vuol dire che l'utente non mi ha passato tutti i giocatori correttamente, il suo attacco si ferma
            int nPlayersSameSpotFromMap = 1;

            if ( effect.getnPlayersAttackable() == 50 ) {

                for (Player p : players) {
                    if (p != offender && p != defenders.get(0)) {
                        if (p.getxPosition() == defenders.get(0).getxPosition() && p.getyPosition() == defenders.get(0).getyPosition())
                            nPlayersSameSpotFromMap++;
                    }
                }

                for ( int i = 1; i < defenders.size(); i++){
                    if ( defenders.get(i) != offender) {
                        if (defenders.get(0).getxPosition() == defenders.get(i).getxPosition() && defenders.get(0).getyPosition() == defenders.get(i).getyPosition()) {
                            nPlayersInTheSameSpot++;
                        }
                    }
                }
            }else{          //entra qua se nPlayersMarkable == 50, cioè devo marchiare tutti i player nello stesso spot del player che ho colpito precedentemente, lui incluso

                defenders_temp.add(playersHit.get(playersHit.size()-1));

                for (Player p : players) {
                    if (p != offender && p != playersHit.get(playersHit.size()-1)) {
                        if (p.getxPosition() == playersHit.get(playersHit.size()-1).getxPosition() && p.getyPosition() == playersHit.get(playersHit.size()-1).getyPosition())
                            nPlayersSameSpotFromMap++;
                    }
                }

                for ( int i = 0; i < defenders.size(); i++){
                    if ( playersHit.get(playersHit.size()-1).getxPosition() == defenders.get(i).getxPosition() && playersHit.get(playersHit.size()-1).getyPosition() == defenders.get(i).getyPosition()){
                        nPlayersInTheSameSpot++;
                    }
                }
            }

            if (nPlayersInTheSameSpot != nPlayersSameSpotFromMap)
                throw new InvalidChoiceException("i giocatori passati dall'utente non corrispondono a tutti i giocatori in un determinato spot");

            if (effect.getVisibleByWho() == Visibility.NONE) {
                for (int i = 0; i < nPlayersInTheSameSpot && i < defenders.size(); i++) {   //controllo i defenders, se qualcuno non rispetta vuol dire che l'utente ha dato input sbagliati, si ferma il suo attacco
                    if (this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition())) {
                        throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -NONE");
                    }
                }
            }
            if (effect.getVisibleByWho() == Visibility.OFFENDER) {
                for (int i = 0; i < nPlayersInTheSameSpot && i < defenders.size(); i++) {   //controllo i defenders, se qualcuno non rispetta visibility lo escludo
                    if (!this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition())) {
                        throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -OFFENDER");
                    }
                }
            }

            for (int i = 0; i < nPlayersInTheSameSpot && i < defenders.size(); i++) {      //if a defender is not minDistance < |distance offender-defender| < MaxDistance remove him.

                int distance = gameMap.distance(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition());

                if (distance < effect.getMinDistance()) {
                    throw new InvalidChoiceException("qualche defender non rispetta la distanza per questo effetto MinDistance");
                }
                if (distance > effect.getMaxDistance()) {
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
                if (effect.mustShootOtherPlayers() && !effect.mustBeOtherRoom()) {       //per ogni player in defenders scorro i players in playersHit, se ne trovo due uguali lancio eccezione

                    if (playersHit.isEmpty()) {
                        throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1");
                    }
                    for (int k = 0; k < playersHit.size() - 1; k++) {
                        if (defenders.get(i) == playersHit.get(k)) {
                            throw new InvalidChoiceException("cercando di sparare ad un giocatore già colpito, non permesso in questo attacco");
                        }
                    }
                }else {     //caso in cui devo sparare a giocatori tutti diversi in questo effetto
                    if ( effect.mustShootOtherPlayers() && effect.mustBeOtherRoom()){
                        for (int j = 0; j < nPlayersInTheSameSpot && j < defenders.size() && j != i; j++) {
                            if (defenders.get(i) == defenders.get(j))
                                throw new InvalidChoiceException("trying to shoot the same defender twice, not permitted");
                        }
                    }
                }
                //vuol dire che devo sparare ai giocatori a cui avevo già sparato
                if (effect.mustShootSamePlayers()) {       //per ogni player in defenders scorro i players in playersHit, se ne trovo due uguali lancio eccezione

                    if (playersHit.isEmpty()) {
                        throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1");
                    }
                    for (int k = 0; k < playersHit.size() - 1; k++) {
                        if (defenders.get(i) == playersHit.get(k)) {
                            throw new InvalidChoiceException("cercando di sparare ad un giocatore già colpito, non permesso in questo attacco");
                        }
                    }
                }
            }

            //modifico defenders_temp, ovvero la lista dei giocatori a cui devo effettivamente sparare che passo a ShootPlayer, in modo da avere solo e tutti i giocatori che sono nello stesso spot.
            for (int i = 0; i < nPlayersInTheSameSpot && i < defenders.size(); i++) {
                defenders_temp.add(defenders.get(i));       //questi sono i giocatori a cui effettivamente faccio danno
            }
            return defenders_temp;
        }

        if (effect.getVisibleByWho() == Visibility.NONE) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++) {   //controllo i defenders, se qualcuno non rispetta vuol dire che l'utente ha dato input sbagliati, si ferma il suo attacco
                if (this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition())) {
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -NONE");
                }
            }
        }
        if (effect.getVisibleByWho() == Visibility.OFFENDER) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++) {   //controllo i defenders, se qualcuno non rispetta visibility lo escludo
                if (!this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition())) {
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -OFFENDER");
                }
            }
        }

        if (effect.mustBeDifferentSpots()) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++){
                for (int j = 0; i < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()) && i != j; i++) {
                    if ((defenders.get(i).getxPosition() == defenders.get(j).getxPosition() && defenders.get(i).getyPosition() == defenders.get(j).getyPosition())) {     //se due giocatori si trovano nello stesso spot significa che l'utente ha dato informazioni sbagliate, lancio eccezione
                        throw new InvalidChoiceException("almeno due giocatori non rispettano mustBeDifferentSpots");
                    }
                }
            }
        }

        if (effect.mustBeSameSpots()) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++){
                for (int j = 0; i < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()) && i != j; i++) {
                    if ((defenders.get(i).getxPosition() != defenders.get(j).getxPosition() || defenders.get(i).getyPosition() != defenders.get(j).getyPosition())) {     //se due giocatori si trovano in spot diversi significa che l'utente ha dato informazioni sbagliate, lancio eccezione
                        throw new InvalidChoiceException("almeno due giocatori non rispettano mustBeSameSpots");
                    }
                }
            }
        }

        for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++) {      //if a defender is not minDistance < |distance offender-defender| < MaxDistance remove him.

            int distance = gameMap.distance(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition());

            if (distance < effect.getMinDistance()) {
                throw new InvalidChoiceException("qualche defender non rispetta la distanza per questo effetto MinDistance");
            }
            if (distance > effect.getMaxDistance()) {
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

            //caso in cui devo sparare a giocatori tutti diversi in questo effetto
            if ( effect.mustShootOtherPlayers() && effect.mustBeOtherRoom()){
                for (int k = 0; i < defenders.size() && (k < effect.getnPlayersAttackable() || k < effect.getnPlayersMarkable()); k++){
                    for (int j = 0; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()) && k != j; j++){
                        if ( defenders.get(k) == defenders.get(j))
                            throw new InvalidChoiceException("can't shoot the same player twice in this effect!");
                    }
                }
            }else if( effect.mustBeOtherRoom()) {
                if ( this.gameMap.getPlayerRoom(offendername) == this.gameMap.getPlayerRoom(defenders.get(i).getNickname())) {
                    throw new InvalidChoiceException("almeno un defender è nella stessa stranza dell'offender -MustBeOtherRoom ");
                }
            }
            //siamo nel cso in cui bisogna sparare a tutti quelli a cui non ho sparato più l'ultimo colpito
            else if( effect.mustShootOtherPlayers() && effect.mustShootSamePlayers() ){

                if (playersHit.isEmpty()) {
                    throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1 and mustShootSamePlayers = 1");
                }
                defenders_temp.add(playersHit.get(playersHit.size()-1));

                for (int j = 0; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++){
                    defenders_temp.add(defenders.get(j));
                }

                return defenders_temp;

            }else if(effect.mustShootOtherPlayers()) {       //per ogni player in defenders scorro i players in playersHit, se ne trovo due uguali lancio eccezione

                if (playersHit.isEmpty()) {
                    throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1");
                }
                for (int k = 0; k < playersHit.size(); k++) {
                    if (defenders.get(i) == playersHit.get(k)) {
                        throw new InvalidChoiceException("cercando di sparare ad un giocatore già colpito, non permesso in questo attacco  -MUSTSHOOTOTHERPLAYERS");
                    }
                }
            }else if(effect.mustShootSamePlayers()){  //caso in cui devo sparare ai players a cui ho già sparato

                if (playersHit.isEmpty()) {
                    throw new InvalidChoiceException("playersHit is empty and mustShootSamePlayers = 1");
                }
                if ( effect.getnPlayersAttackable() != 0 ){

                    for ( int j = 0; j < effect.getnPlayersAttackable(); j++) {
                        if (!playersHit.contains(defenders.get(j))) {
                            throw new InvalidChoiceException("cercando di sparare ad un giocatore che non ho mai colpito, non permesso in questo attacco -MUSTSHOOTSAMEPLAYER");
                        }
                    }
                }
                if ( effect.getnPlayersMarkable() != 0 ){

                    for ( int j = 0; j < effect.getnPlayersMarkable(); j++) {
                        if (!playersHit.contains(defenders.get(j))) {
                            throw new InvalidChoiceException("cercando di sparare ad un giocatore che non ho mai colpito, non permesso in questo attacco -MUSTSHOOTSAMEPLAYER");
                        }
                    }
                }
            }
        }

        for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++) {
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

        if(effect.getnMoves() != 0 || effect.getnMovesOtherPlayer() != 0)
            return 0;
        else
            return 1;

    }

    /**
     * this method check if the player can pay the cost in effect, then pays it
     * @param effect the effect we have to see to pay his cost
     * @param player the player who has to pay
     * @return true if player pays, false if the player can't pay
     */
    public boolean payCostEffect( Effect effect, String player ) {

        if (effect.getCost() != null) {       //if there is a cost I pay it ( for example an optional shooting Action )
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
     * @param playersWhoMoveNames is the player being moved
     * @param effect is the movement effect we have to look at
     * @param xPos is the  x position of the player after he moved
     * @param yPos is the  y position of the player after he moved
     * @throws InvalidChoiceException
     */
    public void makeMovementEffect(ArrayList<String> playersWhoMoveNames, Effect effect, ArrayList<Integer> xPos, ArrayList<Integer> yPos, ArrayList<Player> playersHit, String offenderName) throws InvalidChoiceException{

        ArrayList<Player> playersWhoMove = new ArrayList<>();

        Player offender = getPlayerByNickname(offenderName);

        int i = 0;

        for ( String p : playersWhoMoveNames){
            playersWhoMove.add(getPlayerByNickname(p));
        }

        if ( effect.mustShootOtherPlayers()){

            for (int k = 0; k < playersHit.size(); k++) {
                if (playersWhoMove.get(0) == playersHit.get(k)) {
                    throw new InvalidChoiceException("cercando di spostare ad un giocatore già spostato, non permesso in questa mossa  -MUSTSHOOTOTHERPLAYERS");
                }
            }
        }

        if (!playersWhoMoveNames.isEmpty()) {
            if (effect.getnMoves() != 0) {

                if ( playersWhoMove.get(i) != offender){
                    throw new InvalidChoiceException("Trying to move a defender instead of the offender");
                }

                if (this.gameMap.canMoveFromTo(playersWhoMove.get(i).getxPosition(), playersWhoMove.get(i).getyPosition(), xPos.get(i), yPos.get(i), effect.getnMoves())) {
                    movePlayer(playersWhoMove.get(i).getNickname(), xPos.get(i), yPos.get(i));
                    xPos.remove(i);
                    yPos.remove(i);
                    if (playersWhoMove.get(i) != offender){
                        playersHit.add(playersWhoMove.get(i));
                    }
                    playersWhoMoveNames.remove(i);
                } else
                    throw new InvalidChoiceException("giocatore spostato di number of spots != nMoves");
            }

            if (effect.getnMovesOtherPlayer() != 0) {

                if ( playersWhoMove.get(i) == offender){
                    throw new InvalidChoiceException("Trying to move the offender instead of a defender");
                }

                if (this.gameMap.canMoveFromTo(playersWhoMove.get(i).getxPosition(), playersWhoMove.get(i).getyPosition(), xPos.get(i), yPos.get(i), effect.getnMovesOtherPlayer())) {
                    movePlayer(playersWhoMove.get(i).getNickname(), xPos.get(i), yPos.get(i));
                    xPos.remove(i);
                    yPos.remove(i);
                    if (playersWhoMove.get(i) != offender){
                        playersHit.add(playersWhoMove.get(i));
                    }
                    playersWhoMoveNames.remove(i);
                } else
                    throw new InvalidChoiceException("giocatore spostato di number of spots != nMovesOtherPlayer");
            }
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

        if (effect.getnPlayersAttackable() != 0) {       //per ogni giocatore a cui bisogna dare danni presente nella lista dei defenders, assegno nDamages dell'effetto
            for (int i = 0; i < defenders_temp.size() && i < effect.getnPlayersAttackable(); i++) {
                defenders_temp.get(i).giveDamage(offendername, effect.getnDamages());
            }
        }

        if (effect.getnPlayersMarkable() != 0) {     //per ogni giocatore a cui bisogna dare marchi presente nella lista dei defenders, assegno nMarks dell'effetto
            for (int i = 0; i < defenders_temp.size() && i < effect.getnPlayersMarkable(); i++) {
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
     * @param playersWhoMoveNames is the player who moves
     * @return return true if all the checks on the effects are executed the right way
     * @throws InvalidChoiceException
     */
    public boolean shootWithMovement(String offenderName, ArrayList<String> defendersNames, Weapon weapon, int orderNumber, ArrayList<Integer> xPosition, ArrayList<Integer> yPosition, ArrayList<String> playersWhoMoveNames){

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

                    if(playersWhoMoveNames.isEmpty())
                        return true;

                    //se l'effetto è linear devo fare un controllo sulla direzione cardinale in cui voglio sparare
                    if(effetto.isLinear){

                        Direction cardinalDirection = Direction.NONE;

                        //di seguito calcolo la direzione cardinale confrontando il primo player che voglio spostare in playersWhoMoveNames e dove voglio spostarlo successivamente guardando xPos e yPos
                        if(xPosition.get(0) == getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition() && yPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition()) {

                            if (yPosition.get(0) > getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition())
                                cardinalDirection = Direction.EAST;
                            else if(yPosition.get(0) < getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition())
                                cardinalDirection = Direction.WEST;

                        }
                        else if(xPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition() && yPosition.get(0) == getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition()) {

                            if (xPosition.get(0) > getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition())
                                cardinalDirection = Direction.SOUTH;
                            else if(xPosition.get(0) < getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition())
                                cardinalDirection = Direction.NORTH;

                        }
                        else if (xPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition() && yPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition())
                            throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------NON LINEAR");


                        //una volta calcolata la direzione cardinale controllo che tutti i movers rispettino questa direzione
                        for (int cont = 0; cont < playersWhoMoveNames.size(); cont++){
                            if(cardinalDirection == Direction.NORTH){
                                if(yPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(cont)).getyPosition() || xPosition.get(0) < getPlayerByNickname(playersWhoMoveNames.get(cont)).getxPosition())
                                    throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------1");
                            }
                            if(cardinalDirection == Direction.SOUTH){
                                if(yPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(cont)).getyPosition() || xPosition.get(0) > getPlayerByNickname(playersWhoMoveNames.get(cont)).getxPosition())
                                    throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------2");
                            }
                            if(cardinalDirection == Direction.EAST){
                                if(xPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(cont)).getxPosition() || yPosition.get(0) < getPlayerByNickname(playersWhoMoveNames.get(cont)).getyPosition())
                                    throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------3");
                            }
                            if(cardinalDirection == Direction.WEST){
                                if(xPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(cont)).getxPosition() || yPosition.get(0) > getPlayerByNickname(playersWhoMoveNames.get(cont)).getyPosition())
                                    throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------4");
                            }
                        }
                    }

                    makeMovementEffect(playersWhoMoveNames, effetto, xPosition, yPosition, playersHit, offenderName);


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
            MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
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
            MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
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
                    throw new InvalidChoiceException("giocatore spostato di number of spots != nMoves");
            }
            if ( effect.getnMovesOtherPlayer() != 0){
                if( this.gameMap.canMoveFromTo(playerWhoReceiveEffect.getxPosition(), playerWhoReceiveEffect.getyPosition(), xPos, yPos, effect.getnMovesOtherPlayer()) ) {
                    movePlayer(playerWhoReceiveEffect.getNickname(), xPos, yPos);
                }else
                    throw new InvalidChoiceException("giocatore spostato di number of spots != nMovesOtherPlayer");
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

        int nRedPowerUp = Collections.frequency(currentPlayer.getPowerUpColors() , Color.RED);
        int nBluePowerUp = Collections.frequency(currentPlayer.getPowerUpColors(), Color.BLUE);
        int nYellowPowerUp = Collections.frequency(currentPlayer.getPowerUpColors() , Color.YELLOW);


        for (int i = 0; i < currentPlayer.getWeaponList().size(); i++){
            //first i need the cost of the weapon unloaded
            if (!currentPlayer.getWeaponList().get(i).isLoaded()) {
                ArrayList<Color> weaponCost = currentPlayer.getWeaponList().get(i).getCost();

                //cost of the weapon divided
                int nRedAmmoWeapon = Collections.frequency(weaponCost, Color.RED);
                int nBlueAmmoWeapon = Collections.frequency(weaponCost, Color.BLUE);
                int nYellowAmmoWeapon = Collections.frequency(weaponCost, Color.YELLOW);


                //add in the list the weapons that the player can reload with his ammo
                if ( (nRedAmmoWeapon < currentPlayer.getnRedAmmo() + nRedPowerUp) && (nYellowAmmoWeapon < currentPlayer.getnYellowAmmo() + nYellowPowerUp) && (nBlueAmmoWeapon < currentPlayer.getnBlueAmmo() + nBluePowerUp))
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
    public void pay(String nickname, ArrayList<Color> cost) throws InvalidChoiceException{

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
     * @param weaponName the name of the weapon to pick in the spawnspot weapon list
     * @throws RuntimeException if this is not a spawn spot
     */
    public void pickWeaponFromSpawn(String nickname, String weaponName) throws RuntimeException{

        Player p = getPlayerByNickname(nickname);

        int x = p.getxPosition();
        int y = p.getyPosition();

        if (!gameMap.isSpawnSpot(x, y))
            throw new RuntimeException("this is not a spawn spot");

        int index = gameMap.indexOfWeapon(x, y, weaponName);

        gameMap.grabSomething(x, y, p, index);

    }

    //TESTED
    /**
     * pick a weapon from the spawnspot where the player is
     * @param nickname the player who wants to pick the weapon
     * @param index the index of the weapon to pick in the spawnspot weapon list
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
     * @param weaponToPick index of the weapon to pick from the spawn spot weapon list
     * @param weaponToDiscard index of the weapon to discard from player hand
     */
    public void switchWeapons (String player, String weaponToPick, String weaponToDiscard){

        Player p = getPlayerByNickname(player);

        int x = p.getxPosition();
        int y = p.getyPosition();

        if(!gameMap.isSpawnSpot(x, y))
            throw new RuntimeException("This is not a spawn spot");

        Weapon toDiscard = p.removeWeaponByName(weaponToDiscard);

        toDiscard.reload();

        gameMap.grabWeapon(x, y, p, weaponToPick);

        gameMap.refill(x, y, toDiscard);


    }

    //TESTED
    /**
     * update status of current and next player
     * @return the next player
     */
    public Player endTurnUpdateStatus() {

        Player current = getCurrentPlayer();

        String next = getNextPlayer(current.getNickname());

        current.endTurnCurrent();

        getPlayerByNickname(next).startTurn();

        return getPlayerByNickname(next);
    }

    //TESTED
    /**
     * @param current current player
     * @return the nickname of the next player
     */
    protected String getNextPlayer(String current) {

        int currentIndex = playerNames.indexOf(current);

        if(currentIndex == players.size() - 1)
            return playerNames.get(0);
        else
            return playerNames.get(currentIndex + 1);

    }

    //TESTED
    /**
     *
     * @return the current player
     */
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

    //ONLY USED IN TESTS
    /**
     * this method returns a spot by using index
     * @param x the x of the spot
     * @param y the y of the spot
     * @return the spot corresponding to that index
     */
    public Spot getSpotByIndex(int x, int y){
        return gameMap.getSpotByIndex(x, y);
    }

    /**
     * based on the playerStatus, generate the possible actions of the player
     * @param nickname the nickname of the player
     * @return possible player actions
     */
    public ArrayList<String> generatePossibleActions(String nickname) {

        Player player = getPlayerByNickname(nickname);
        ArrayList<String> actions = new ArrayList<>();

        if(player.isDead()){
            actions.add(Actions.Respawn.toString());
            return actions;
        }

        if(isOnSpawnSpot(player))
            actions.add(Actions.PickWeapon.toString());

        if(player.playerStatus.nActionsDone < player.playerStatus.nActions){
            actions.add(Actions.Move.toString());
            actions.add(Actions.MoveAndGrab.toString());
            actions.add(Actions.Attack.toString());
        }

        if(player.hasTurnPowerUp()){
            actions.add(Actions.UsePowerUp.toString());
        }

        for(Weapon w : player.getWeaponList()){
            if(!w.isLoaded()) {
                actions.add(Actions.Reload.toString());
                break;
            }
        }

        actions.add(Actions.EndTurn.toString());

        return actions;
    }

    //TESTED
    /**
     * tells if player is on a spawnspot
     * @param player the player to check
     * @return true if player is on a spawnspot
     */
    private boolean isOnSpawnSpot(Player player) {

        int x = player.getxPosition();
        int y = player.getyPosition();

        return gameMap.isSpawnSpot(x, y);

    }

    //TESTED
    /**
     * this method creates a snapshot of the game in a string representing a json file
     * @return the string containing json game
     */
    public String modelSnapshot(){
        Gson gson = new Gson();

        //saving player nicknames
        String jsonPlayerNames = gson.toJson(playerNames.toArray());

        //saving players
        String jsonPlayers = gson.toJson(players.toArray());

        //saving kst
        String jsonKST = gson.toJson(kst);

        //saving powerUpDeck
        String jsonPowerUpDeck = gson.toJson(powerupDeck);

        //saving weaponDeck
        String jsonWeaponDeck = gson.toJson(weaponDeck);

        //saving GameMap
        String jsonGameMap = gameMapSnapshot(gson);

        //create a json that stores all the information of the game in a string with json format
        String modelSnapshot = "{ \"player\":" + jsonPlayers + "," + "\"playerNames\":" + jsonPlayerNames + "," + "\"powerUpDeck\":" + jsonPowerUpDeck + "," + "\"weaponDeck\":" + jsonWeaponDeck + "," + "\"kst\":" + jsonKST + "," + "\"gameMap\":" + jsonGameMap + "}" ;

        return modelSnapshot;
    }

    //TESTED
    /**
     * this method create the snapshot of the gameMap and returns it to above method
     * @param gson the gson of the above method
     * @return the gameMap snapshot
     */
    private String gameMapSnapshot(Gson gson) {
        String jsonGameMap = "{";
        String jsonSpot;

        for(int i = 0; i < 3; i++){

            jsonGameMap += "\"row" + i + "\": {";

            for (int j = 0; j < 4; j++){

                jsonGameMap += "\"col" + j + "\": {";

                if(gameMap.validSpot(i, j))
                    jsonSpot = "\"spot\" :" + gson.toJson(gameMap.getSpotByIndex(i, j));
                else
                    jsonSpot = "\"spot\" :" + "{}";

                jsonGameMap += jsonSpot;

                if (j < 3)
                    jsonGameMap += "},";
                else
                    jsonGameMap += "}";
            }
            if( i < 2)
                jsonGameMap += "},";
            else
                jsonGameMap += "}";
        }

        jsonGameMap += "}";

        return jsonGameMap;

    }

    /**
     * generate all the possible weapons that player can pick in a spawnSpot
     * @param nickname the nickname of the player
     * @return all the names of the weapons
     */
    public ArrayList<String> weaponsToPick(String nickname) {

        Player p = getPlayerByNickname(nickname);

        Spot playerSpot = getSpotByIndex(p.getxPosition(), p.getyPosition());

        return playerSpot.getSpawnWeaponNames();

    }

    /**
     * add a weapon to a spawnspot
     * @param x the x of the spawnspot
     * @param y the y of the spawnspot
     * @param weapon the weapon to add
     */
    public void addWeaponToSpawnSpot(int x, int y, Weapon weapon) {

        gameMap.addWeaponToSpawn(x, y, weapon);

    }

    /**
     * Creates a new weapon based on the name
     * @param weaponName the name of the weapon
     * @return the weaponName object
     */
    public Weapon getWeaponByName(String weaponName) {
        return Weapon.getWeapon(weaponName);
    }

    //TODO TEST
    /**
     * generate all the possible combinations for a player to pay something
     * @param player the player to check
     * @param cost the cost that player has to pay
     * @return all the possible combinations of payment
     */
    public ArrayList<String> generatePaymentChoice(Player player, ArrayList<Color> cost) {

        if(!player.canPay(cost))
            throw new RuntimeException("This cost can't be payed from this player");

        //Counting each cost occurrence in the cost array
        int redCost = 0;
        int blueCost = 0;
        int yellowCost = 0;
        for(Color c : cost){
            if(c == Color.RED)
                redCost++;
            if(c == Color.BLUE)
                blueCost++;
            if(c == Color.YELLOW)
                yellowCost++;
        }

        ArrayList<PowerUp> playerPowerUps = player.getPowerUpList(); //all the player power ups

        //Creating a list for each power up color
        ArrayList<PowerUp> redPowerUps = new ArrayList<>();
        ArrayList<PowerUp> bluePowerUps = new ArrayList<>();
        ArrayList<PowerUp> yellowPowerUps = new ArrayList<>();

        for(PowerUp p : playerPowerUps){

            if(p.getColor() == Color.RED)
                redPowerUps.add(p);

            if(p.getColor() == Color.BLUE)
                bluePowerUps.add(p);

            if(p.getColor() == Color.YELLOW)
                yellowPowerUps.add(p);

        }

        int nRedPowerUps = redPowerUps.size();
        int nBluePowerUps = bluePowerUps.size();
        int nYellowPowerUps = yellowPowerUps.size();


        //*************** RED *****************

        //Counting how many red ammo the player has
        int nRedAmmo = player.getnRedAmmo();
        //All the options I have to pay for the red cost
        ArrayList<String> redPaymentOptions = new ArrayList<>();

        //I try adding 0 red and all power up, then 1 red and x power ups ecc.....
        for( int tempRedAmmoInPayment = 0; tempRedAmmoInPayment <= nRedAmmo && tempRedAmmoInPayment <= redCost; tempRedAmmoInPayment++ ){

            //This means I have enough power ups to get to this color cost
            if(nRedPowerUps + tempRedAmmoInPayment >= redCost){

                String paymentOption = "";
                for(int i = 0; i < tempRedAmmoInPayment; i++)
                    paymentOption += "RED" + Controller.DOUBLESPLITTER;
                for(int i = 0; i < redCost - tempRedAmmoInPayment; i++)
                    paymentOption += redPowerUps.get(i).toString() + Controller.DOUBLESPLITTER;

                redPaymentOptions.add(paymentOption);
            }

        }


        //*************** BLUE *****************

        //Counting how many Blue ammo the player has
        int nBlueAmmo = player.getnBlueAmmo();
        //All the options I have to pay for the Blue cost
        ArrayList<String> bluePaymentOptions = new ArrayList<>();

        //I try adding 0 Blue and all power up, then 1 Blue and x power ups ecc.....
        for( int tempBlueAmmoInPayment = 0; tempBlueAmmoInPayment <= nBlueAmmo && tempBlueAmmoInPayment <= blueCost; tempBlueAmmoInPayment++ ){

            //This means I have enough power ups to get to this color cost
            if(nBluePowerUps + tempBlueAmmoInPayment >= blueCost){

                String paymentOption = "";
                for(int i = 0; i < tempBlueAmmoInPayment; i++)
                    paymentOption += "BLUE" + Controller.DOUBLESPLITTER;
                for(int i = 0; i < blueCost - tempBlueAmmoInPayment; i++)
                    paymentOption += bluePowerUps.get(i).toString() + Controller.DOUBLESPLITTER;

                bluePaymentOptions.add(paymentOption);
            }

        }

        //*************** YELLOW *****************

        //Counting how many yellow ammo the player has
        int nYellowAmmo = player.getnYellowAmmo();
        //All the options I have to pay for the yellow cost
        ArrayList<String> yellowPaymentOptions = new ArrayList<>();

        //I try adding 0 yellow and all power up, then 1 yellow and x power ups ecc.....
        for( int tempYellowAmmoInPayment = 0; tempYellowAmmoInPayment <= nYellowAmmo && tempYellowAmmoInPayment <= yellowCost; tempYellowAmmoInPayment++ ){

            //This means I have enough power ups to get to this color cost
            if(nYellowPowerUps + tempYellowAmmoInPayment >= yellowCost){

                String paymentOption = "";
                for(int i = 0; i < tempYellowAmmoInPayment; i++)
                    paymentOption += "YELLOW" + Controller.DOUBLESPLITTER;
                for(int i = 0; i < yellowCost - tempYellowAmmoInPayment; i++)
                    paymentOption += yellowPowerUps.get(i).toString() + Controller.DOUBLESPLITTER;

                yellowPaymentOptions.add(paymentOption);
            }

        }

        ArrayList<String> finalAnswer = new ArrayList<>();

        for(int i = 0; i < redPaymentOptions.size(); i++)
            for(int j = 0; j < bluePaymentOptions.size(); j++)
                for(int k = 0; k < yellowPaymentOptions.size(); k++){

                    finalAnswer.add(
                            redPaymentOptions.get(i) +
                                    bluePaymentOptions.get(j) +
                                    yellowPaymentOptions.get(k)
                    );

                }

        return finalAnswer;

    }

}

