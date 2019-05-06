package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.map.GameMap;
import it.polimi.ingsw.model.map.MapBuilder;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.model.cards.Visibility;
import it.polimi.ingsw.model.map.Spot;

import java.lang.reflect.Array;
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

        ArrayList<String> playersWithFrenzyBoard = new ArrayList<String>();

        for(Player p : players) {
            if (p.getDamages().isEmpty())
                p.sethasFrenzyBoard(true);
            p.setCanReloadBeforeShooting(true);
            //TODO nMovesBeforeReloading depends on where the frenzy turn starts from
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
        Log.LOGGER.info("Checking deaths...");

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
     * @param defendersNames The nickname of the players the offender wants to shoot
     * @param effect The effect of the weapon
     * @return The players that get shot during this effect
     */
    public ArrayList<Player> whoP1CanShootInThisEffect(String offendername, ArrayList<String> defendersNames, Effect effect, ArrayList<Player> playersHit) throws InvalidChoiceException{

        Player offender = getPlayerByNickname(offendername);

        ArrayList<Player> defenders_temp = new ArrayList<>();

        ArrayList<Player> defenders = new ArrayList<>();


        for (String i : defendersNames)
            defenders_temp.add(getPlayerByNickname(i));


        if (effect.getVisibleByWho() == Visibility.NONE) { // for ( Player p : defenders )
            for (int i = 0; i < defenders_temp.size() - 1 || i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable(); i++) {   //controllo i defenders, se qualcuno non rispetta vuol dire che l'utente ha dato input sbagliati, si ferma il suo attacco
                if (this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders_temp.get(i).getxPosition(), defenders_temp.get(i).getyPosition())) {
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -NONE");
                }
            }
        }
        if (effect.getVisibleByWho() == Visibility.OFFENDER) {
            for (int i = 0; i < defenders_temp.size() - 1 || i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable(); i++) {   //controllo i defenders, se qualcuno non rispetta visibility lo escludo
                if (!this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders_temp.get(i).getxPosition(), defenders_temp.get(i).getyPosition())) {
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -OFFENDER");
                }
            }
        }

        for (int i = 0; i < defenders_temp.size() - 1 || i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable(); i++) {      //if a defender is not minDistance < |defender.position - offender.position| < MaxDistance remove him.

            int spots_on_x, spots_on_y;

            if (offender.getxPosition() <= defenders_temp.get(i).getxPosition())
                spots_on_x = defenders_temp.get(i).getxPosition() - offender.getxPosition();
            else
                spots_on_x = offender.getxPosition() - defenders_temp.get(i).getxPosition();

            if (offender.getyPosition() <= defenders_temp.get(i).getyPosition())
                spots_on_y = defenders_temp.get(i).getyPosition() - offender.getyPosition();
            else
                spots_on_y = offender.getyPosition() - defenders_temp.get(i).getyPosition();

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
                if (!this.gameMap.see(playersHit.get(playersHit.size() - 1).getxPosition(), playersHit.get(playersHit.size() - 1).getyPosition(), defenders_temp.get(i).getxPosition(), defenders_temp.get(i).getyPosition())) {
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -LASTONEATTACKED");
                }
            }
            if (effect.mustShootOtherPlayers()) {       //per ogni player in defenders_temp scorro i players in playersHit, se ne trovo due uguali lancio eccezione

                if (playersHit.isEmpty()) {
                    throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1");
                }
                for (int k = 0; k < playersHit.size() - 1; k++) {
                    if (defenders_temp.get(i) == playersHit.get(k)) {
                        throw new InvalidChoiceException("cercando di sparare ad un giocatore già colpito, non permesso in questo attacco");
                    }
                }
            }

            if( effect.mustBeOtherRoom()) {
                if ( this.gameMap.getPlayerRoom(offendername) == this.gameMap.getPlayerRoom(defenders_temp.get(i).getNickname())) {
                    throw new InvalidChoiceException("almeno un defender è nella stessa stranza dell'offender -MustBeOtherRoom ");
                }
            }
        }

        if (effect.mustBeDifferentSpots()) {
            for ( int i = 0; i < defenders_temp.size() - 1 || i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable(); i++ ){
                for ( int j = 0; ( j < defenders_temp.size() - 1 || j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable() ) && i != j; j++ ) {
                    if ((defenders_temp.get(i).getxPosition() == defenders_temp.get(j).getxPosition() && defenders_temp.get(i).getyPosition() == defenders_temp.get(j).getyPosition())) {     //se due giocatori si trovano nello stesso spot significa che l'utente ha dato informazioni sbagliate, lancio eccezione
                        throw new InvalidChoiceException("almeno due giocatori non rispettano mustBeDifferentSpots");
                    }
                }
            }
        }

        if (effect.isLinear()) {

            for ( int i = 0; i < defenders_temp.size() - 1 || i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable(); i++ ){

                //se sia x che y sono diverse il defender è sicuramente non allineato
                if (defenders_temp.get(i).getxPosition() != offender.getxPosition() && defenders_temp.get(i).getyPosition() != offender.getyPosition()) {
                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_1");
                }

                //controllo se offender e defender.get(i) non siano nello stesso spot, se così fosse andrebbe bene ma non sarebbe ancora decisa la direzione che devono rispettare i defenders successivi
                if (defenders_temp.get(i).getxPosition() != offender.getxPosition() || defenders_temp.get(i).getyPosition() != offender.getyPosition()) {

                    if (defenders_temp.get(i).getxPosition() == offender.getxPosition()) {      //offender e defender sono sulla stessa riga

                        if (defenders_temp.get(i).getyPosition() > offender.getyPosition()) { //iL defender è a EAST rispetto all'offender

                            //controllo se anche gli altri defenders sono a EAST rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders_temp.size() - 1 || j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable(); j++){
                                if (defenders_temp.get(j).getyPosition() < offender.getyPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_2");
                                }
                            }
                        }else
                        if (defenders_temp.get(i).getyPosition() < offender.getyPosition()) { //il primo defender è a WEST rispetto all'offender
                            //controllo se anche gli altri defenders sono a WEAST rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders_temp.size() - 1 || j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable(); j++){
                                if (defenders_temp.get(j).getyPosition() > offender.getyPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_3");
                                }
                            }
                        }
                    }
                    if (defenders_temp.get(i).getyPosition() == offender.getyPosition()) {      //offender e il defender sono sulla stessa colonna

                        if (defenders_temp.get(i).getxPosition() > offender.getxPosition()) {   //il defender è a SOUTH rispetto all'offender
                            //controllo se anche gli altri defenders sono a SOUTH rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders_temp.size() - 1 || j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable(); j++){
                                if (defenders_temp.get(j).getxPosition() < offender.getxPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_4");
                                }
                            }
                        }else
                        if (defenders_temp.get(0).getxPosition() < offender.getxPosition()) {   //il defender è a NORTH rispetto all'offender
                            //controllo se anche gli altri defenders sono a NORTH rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for ( int j = i; j < defenders_temp.size() - 1 || j < effect.getnPlayerAttackable() || j < effect.getnPlayerMarkable(); j++){
                                if (defenders_temp.get(j).getxPosition() > offender.getxPosition()){
                                    throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_5");
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < defenders_temp.size() - 1 || i < effect.getnPlayerAttackable() || i < effect.getnPlayerMarkable(); i++) {
            defenders.add(defenders_temp.get(i));       //questi sono i giocatori a cui effettivamente faccio danno
        }

        return defenders;
    }

    //todo comment these methods and test them

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    public void payCostEffect( Effect effect, String player ) {

        ArrayList<Color> cost = new ArrayList<>();

        for (Color color: effect.getCost())
            cost.add(color);

        if (effect.getCost() != null) {       //if there is a cost I pay it ( for example an optional shooting action )
            try{
                pay( player, cost );
            }
            catch(InvalidChoiceException e){

            }
        }
    }


    public void makeMovementEffect(String string, Effect effect, int xPos, int yPos) throws InvalidChoiceException{

        Player player = getPlayerByNickname(string);

        if ( effect.getnMoves() != 0){
            if( this.gameMap.canMoveFromTo(player.getxPosition(), player.getyPosition(), xPos, yPos, effect.getnMoves()) ) {
                movePlayer(player.getNickname(), xPos, yPos);
            }else
                throw new InvalidChoiceException("giocatore spostato di number of spots != nMoves");
        }
        if ( effect.getnMovesOtherPlayer() != 0) {
            if (this.gameMap.canMoveFromTo(player.getxPosition(), player.getyPosition(), xPos, yPos, effect.getnMovesOtherPlayer())) {
                movePlayer(player.getNickname(), xPos, yPos);
            } else
                throw new InvalidChoiceException("giocatore spostato di number of spots != nMovesOtherPlayer");
        }
    }

    public void makeDamageEffect(String offendername, ArrayList<Player> defenders_temp, Effect effect){

        Player offender = getPlayerByNickname(offendername);

        for ( Player p : defenders_temp){           //per ogni giocatore presente nella lista dei defenders, assegno quanti danni e marchi dell'effetto

            if ( effect.getnDamages() != 0 )
                p.giveDamage(offender.getNickname(), effect.getnDamages());

            if ( effect.getnMarks() != 0 )
                p.giveMarks(offender.getNickname(), effect.getnMarks());

        }
    }


    public boolean shootWithMovement(String offenderName, ArrayList<String> defendersNames, Weapon weapon, int orderNumber, int xPosition, int yPosition, String playerWhoMoves)throws InvalidChoiceException {

        Player offender = getPlayerByNickname(offenderName);

        ArrayList<Player> defenders = new ArrayList<>();

        ArrayList<Player> defenders_temp = new ArrayList<>();

        ArrayList<Player> playersHit = new ArrayList<>();

        GameMap backUpMap = new GameMap(this.gameMap);

        ArrayList<Player> backUpPlayers = new ArrayList<>(this.players);

        for (String i : defendersNames)
            defenders.add(getPlayerByNickname(i));


        try {
            //se l'arma è scarica non si può sparare, il giocatore perde un'azione
            if (!weapon.isLoaded())
                throw new InvalidChoiceException("This weapon is not loaded");

            for (int i : weapon.getOrder().get(orderNumber)) {      //scorro gli effetti di quest'arma nell'ordine scelto dall'utente

                Effect effetto = weapon.getEffects().get(i);

                payCostEffect(effetto, offenderName);      //if the effect has a cost, the player pays it

                if (typeOfEffect(effetto) == 0) //Movement effect
                    makeMovementEffect(playerWhoMoves, effetto, xPosition, yPosition);

                if (typeOfEffect(effetto) == 1) {  //Damage effect

                    /* a whoP1ShootsInThisEffect gli passo al primo giro la lista dei nomi dei players ricevuta dall'utente, ai giri successivi i players a cui non ho ancora sparato, cioè quelli a cui devo sparare in questo effetto.
                    Questo metodo mi ritorna una lista di player che effettivamente vengono colpiti/marchiati durante quest'effetto. Quindi li aggiungo a PlayersHit    */

                    defenders_temp = whoP1CanShootInThisEffect(offenderName, defendersNames, effetto, playersHit);

                     /*rimuovo da defendesNames i giocatori che ho colpito nell'effetto appena eseguito, così nel prossimo giro nel ciclo,
                     ovvero nel prossimo effetto, escludo i giocatori colpiti nell'effetto precedente (esempio se il giovatore vuole colpire andreaalf
                     poi gino poi andreaalf ( ma nel primo effetto si ha nPlayersAttackable = 2 ), WhoP1ShootsInThisEffect ritorna andreaalf, gino, li colpisco e li aggiungo a playersHit, nel prossimo effetto passo a
                     whoP1CanShootInThisEffect defenders - defenders_temp, ovvero andreaalf (poi se c'è un must_shoot_other player in questo effetto lancio un'eccezione*/
                    for (Player p : defenders_temp) {
                        playersHit.add(p);
                        defendersNames.remove(p.getNickname());
                    }
                    makeDamageEffect(offenderName, defenders_temp, effetto);
                }
            }
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

    public boolean shootWithoutMovement(String offenderName, ArrayList<String> defendersNames, Weapon weapon, int orderNumber) {

        Player offender = getPlayerByNickname(offenderName);

        ArrayList<Player> defenders = new ArrayList<>();

        ArrayList<Player> defenders_temp = new ArrayList<>();

        ArrayList<Player> playersHit = new ArrayList<>();

        ArrayList<Player> backUpPlayers = new ArrayList<>(this.players);

        GameMap backUpMap = new GameMap(this.gameMap);

        for (String i : defendersNames)
            defenders.add(getPlayerByNickname(i));

        try {
            //se l'arma è scarica non si può sparare, il giocatore perde un'azione
            if (!weapon.isLoaded())
                throw new InvalidChoiceException("This weapon is not loaded");

            for (int i : weapon.getOrder().get(orderNumber)) {      //scorro gli effetti di quest'arma nell'ordine scelto dall'utente

                Effect effetto = weapon.getEffects().get(i);

                payCostEffect(effetto, offenderName);      //if the effect has a cost, the player pays it

                if (typeOfEffect(effetto) == 1) {  //Damage effect

                    /* a whoP1ShootsInThisEffect gli passo al primo giro la lista dei nomi dei players ricevuta dall'utente, ai giri successivi i players a cui non ho ancora sparato, cioè quelli a cui devo sparare in questo effetto.
                    Questo metodo mi ritorna una lista di player che effettivamente vengono colpiti/marchiati durante quest'effetto. Quindi li aggiungo a PlayersHit    */

                    defenders_temp = whoP1CanShootInThisEffect(offenderName, defendersNames, effetto, playersHit);
                    if ( defenders_temp == null ){
                        throw new InvalidChoiceException("defenders_temp == null");
                    }

                     /*rimuovo da defendesNames i giocatori che ho colpito nell'effetto appena eseguito, così nel prossimo giro nel ciclo,
                     ovvero nel prossimo effetto, escludo i giocatori colpiti nell'effetto precedente (esempio se il giovatore vuole colpire andreaalf
                     poi gino poi andreaalf ( ma nel primo effetto si ha nPlayersAttackable = 2 ), WhoP1ShootsInThisEffect ritorna andreaalf, gino, li colpisco e li aggiungo a playersHit, nel prossimo effetto passo a
                     whoP1CanShootInThisEffect defenders - defenders_temp, ovvero andreaalf (poi se c'è un must_shoot_other player in questo effetto lancio un'eccezione*/
                    for (Player p : defenders_temp) {
                        playersHit.add(p);
                        defendersNames.remove(p.getNickname());
                    }
                    makeDamageEffect(offenderName, defenders_temp, effetto);
                }
            }
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

    /*
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

                    if ( effects.get(effect_number).isCanShootAnyPlayer() ){}
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

                /*if( effects.get(effect_number).mustBeOtherRoom()) {
                    for ( int k = 0; k < defenders.size() - 1; k++ ){
                        if ( this.gameMap.getSpotByIndex(offender.getxPosition(), offender.getyPosition()).getRoom() == this.gameMap.getSpotByIndex(defenders.get(k).getxPosition(), defenders.get(k).getyPosition()).getRoom()) {
                            defenders.remove(k);
                            k -= 1;
                        }
                    }
                }
            if ( effects.get(effect_number).isLinear()) {
                System.out.println("In which direction do you want to shoot?");
                //for example NORTH /*TODO look for scanf.
                // In this case if i already have the list of player i want to attack, i only have to check if someone is not in line and remove him.
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
    }*/


    /*public void usePowerUp(String offenderName, String defenderName, PowerUp powerup) {

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
                        /*TODO scale one of the color the user chose
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
            // In this case if i already have the list of player i want to attack, i only have to check if someone is not in line and remove him.
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
    }*/


    public void useDamagePowerUp( String currentPlayerName, String playerWhoReceiveEffectName, Effect effect ){

        Player currentPlayer = getPlayerByNickname(currentPlayerName);
        Player  playerWhoReceiveEffect = getPlayerByNickname(playerWhoReceiveEffectName);


        ArrayList<Player> temp = new ArrayList<>();
        temp.add(playerWhoReceiveEffect);

        payCostEffect( effect, currentPlayerName );      //if the effect has a cost, the player pays it

        makeDamageEffect( currentPlayerName, temp, effect );

    }

    public void useMovementPowerUp ( String currentPlayerName, String playerWhoReceiveEffectName, Effect effect, int xPos, int yPos )throws InvalidChoiceException{

        Player  playerWhoReceiveEffect = getPlayerByNickname(playerWhoReceiveEffectName);

        payCostEffect( effect, currentPlayerName );      //if the effect has a cost, the player pays it


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

 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

        Player next = getNextPlayer(current);

        current.endTurnCurrent();

        next.startTurn();
    }

    private Player getNextPlayer(Player current) {

        int currentIndex = players.indexOf(current);

        if(currentIndex == players.size() - 1)
            return players.get(0);
        else
            return players.get(currentIndex + 1);

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

