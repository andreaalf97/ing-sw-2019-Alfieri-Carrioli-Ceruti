package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.Observable;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.map.AmmoSpot;
import it.polimi.ingsw.model.map.GameMap;
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
        - Can sometimes notify the view (depends on framework)
 */

public class Game extends Observable {

    /**
     * This ArrayList contains all the player objects The players must in the same
     * order of the round, so the first player must be in position 0 !!
     */
    private ArrayList<Player> players;

    /**
     * The list of all player who disconnected during the game
     */
    private ArrayList<Player> disconnectedPlayers;

    /**
     * This ArrayList contains the nicknames of the players The players must in the
     * same order of the round & in the same order of this.players!
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
     * this object initially contains all the Ammo cards
     */
    private AmmoCardDeck ammoCardDeck;

    /**
     * The Kill shot track of this game
     */
    private KillShotTrack kst;

    // ##########################################################################################################

    /**
     * This constructor instantiates a new Game by knowing the list of nicknames
     *
     * @param playerNames ArrayList of all the players names
     *
     */
    public Game(ArrayList<String> playerNames, MapName chosenMap, int nSkulls) {
        this.players = new ArrayList<>();
        this.disconnectedPlayers = new ArrayList<>();
        this.playerNames = playerNames;
        this.powerupDeck = JsonDeserializer.deserializePowerUpDeck();
        this.weaponDeck = JsonDeserializer.deserializeWeaponDeck();
        this.ammoCardDeck = JsonDeserializer.deserializeAmmoCardDeck();

        for (String name : this.playerNames)
            this.players.add(new Player(name));

        this.gameMap = JsonDeserializer.deserializeGameMap(chosenMap, weaponDeck, powerupDeck, ammoCardDeck);
        this.kst = new KillShotTrack(nSkulls);

        notifyObservers(modelSnapshot());
    }

    /**
     * constructor used by JsonDeserializer for persistence
     * @param playerNames the names of the players
     * @param players     the player objects
     * @param weaponDeck  the weaponDeck
     * @param powerUpDeck the powerUpDeck
     * @param kst         the killshot track
     * @param gameMap     the map of the game
     */
    public Game(ArrayList<String> playerNames, ArrayList<Player> players, WeaponDeck weaponDeck,
            PowerUpDeck powerUpDeck, AmmoCardDeck ammoCardDeck, KillShotTrack kst, GameMap gameMap) {
        this.playerNames = playerNames;
        this.players = players;
        this.weaponDeck = weaponDeck;
        this.powerupDeck = powerUpDeck;
        this.ammoCardDeck = ammoCardDeck;
        this.kst = kst;
        this.gameMap = gameMap;

        notifyObservers(modelSnapshot());
    }

    /**
     * persistence game builder
     */
    public Game(){
        JsonDeserializer.deserializeModelSnapshot(modelSnapshot());
        notifyObservers(modelSnapshot());
    }

    // TESTED
    /**
     * getter for players nicknames in the game
     *
     * @return players in game
     */
    public ArrayList<String> getPlayerNames() {
        return new ArrayList<>(this.playerNames);
    }

    // TESTED
    /**
     * getter for player powerup
     *
     * @param player the player
     * @return player powerup list
     */
    public ArrayList<PowerUp> getPlayerPowerUps(String player) {

        Player p = this.getPlayerByNickname(player);

        return p.getPowerUpList();
    }

    // TESTED
    /**
     * this method return the object player corresponding to nickname
     *
     * @param nickname the nickname of the player
     * @return the object Player
     */
    public Player getPlayerByNickname(String nickname) {

        if (!playerNames.contains(nickname))
            throw new IllegalArgumentException("This nickname does not exist!");

        return players.get(playerNames.indexOf(nickname));
    }

    // TESTED
    /**
     * @param player is the current player
     * @param index  index of the powerup
     * @return the powerup corresponding to index
     */
    public PowerUp getPowerUpByIndex(String player, int index) {
        Player currentPlayer = getPlayerByNickname(player);
        return currentPlayer.getPowerUpList().get(index);
    }

    // TESTED
    /**
     * check if skulls on kst are finished so we can set up FrenzyTurm
     *
     * @return true if there are no skulls on kst
     */
    public boolean noMoreSkullsOnKST() {
        return this.kst.noMoreSkulls();
    }

    // ##########################################################################################################

    // TODO DO THIS
    /**
     * This method sets up all player for the final frenzy turn
     */
    public void setupForFrenzy() {

        ArrayList<String> fromKillerOn = new ArrayList<>();
        ArrayList<String> fromStartPlayerOn = new ArrayList<>();

        ArrayList<String> skullList = kst.getSkullList();

        String killer = skullList.get(skullList.size() - 1);

        fromKillerOn.add(killer);

        String nextPlayer = getNextPlayer(killer);
        while (!(players.get(0).equals(nextPlayer))) {
            fromKillerOn.add(nextPlayer);
            nextPlayer = getNextPlayer(nextPlayer);
        }

        while (!(fromStartPlayerOn.equals(killer))) {
            fromStartPlayerOn.add(nextPlayer);
            nextPlayer = getNextPlayer(nextPlayer);
        }

        for (String i : fromKillerOn) {

            Player p = getPlayerByNickname(i);
            p.setNMoves(4);
            p.setNMovesBeforeGrabbing(2);
            p.setCanReloadBeforeShooting(true);
            p.setNMovesBeforeShooting(1);
            p.getPlayerStatus().nActionsDone = 0;

        }

        for (String i : fromStartPlayerOn) {

            Player p = getPlayerByNickname(i);
            p.setNMoves(0);
            p.setNMovesBeforeGrabbing(3);
            p.setCanReloadBeforeShooting(true);
            p.setNMovesBeforeShooting(2);
            p.getPlayerStatus().nActionsDone = 1;
        }

    }

    // TESTED
    /**
     * Gives a randomly picked powerup to the player
     *
     * @param player the player who's receiving the powerUp
     */
    public void givePowerUp(String player) {

        Player p = getPlayerByNickname(player);
        if (!this.powerupDeck.getPowerUpList().isEmpty())
            p.givePowerUp(this.powerupDeck.drawCard());
        else {
            // if deck is empty i reload it from json instead shuffle the old one

            MyLogger.LOGGER.log(Level.INFO, "deck is empty, reloading it from json");
            this.powerupDeck = JsonDeserializer.deserializePowerUpDeck();
            p.givePowerUp(this.powerupDeck.drawCard());
        }

        notifyObservers(clientSnapshot());
    }

    // USED IN TESTS
    /**
     * Gives the chosen weapon to a player
     *
     * @param weapon The weapon object to be given
     * @param player The nickname of the player
     */
    public void giveWeaponToPlayer(String player, Weapon weapon) {
        Player p = getPlayerByNickname(player);
        p.giveWeapon(weapon);

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * This method checks if any player is dead and counts their boards assigning
     * the right amount of point to each player. It is usually executed at the end
     * of each turn
     */
    public void checkDeaths() {
        MyLogger.LOGGER.info("Checking deaths...");

        for (Player i : players) {
            if (!i.playerStatus.isFirstTurn && i.isDead()) {
                giveBoardPointsAndModifyKST(i);
            }
        }
    }

    // TESTED
    /**
     * Clears the player board and gives points to all players involved, this method
     * also modify kst and assign mark
     */
    protected void giveBoardPointsAndModifyKST(Player player) throws RuntimeException {
        if (!player.isDead())
            System.err.println("This player is not dead");

        if (player.getDamages().isEmpty())
            return;

        // at this point i am sure that player is dead, so i can modify kst and give one
        // mark to the last player
        if (player.getDamages().size() == 12) {
            this.kst.addKill(player.getDamages().get(11), true);
            getPlayerByNickname(player.getDamages().get(11)).giveMarks(player.getNickname(), 1);
        } else if (player.getDamages().size() == 11)
            this.kst.addKill(player.getDamages().get(10), false);

        ArrayList<Integer> pointValues = new ArrayList<>();
        pointValues.add(8);
        pointValues.add(6);
        pointValues.add(4);
        pointValues.add(2);
        pointValues.add(1);
        pointValues.add(1);

        getPlayerByNickname(player.getDamages().get(0)).givePoints(1); // first blood point

        for (int k = 0; k < player.getnDeaths(); k++)
            pointValues.remove(0);

        ArrayList<String> ranking = player.getOffendersRanking();

        for (int i = 0; i < ranking.size() && i < pointValues.size(); i++)
            getPlayerByNickname(ranking.get(i)).givePoints(pointValues.get(i));

        player.resetDamages();
        player.addKill();

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * Clears the player frenzy board and gives points to all players involved
     */
    public void giveFrenzyBoardPoints(Player player) {

        int[] pointValues = { 2, 1, 1, 1 };

        ArrayList<String> ranking = player.getOffendersRanking();

        for (int i = 0; i < ranking.size(); i++)
            getPlayerByNickname(ranking.get(i)).givePoints(pointValues[i]);
    }

    // TESTED
    /**
     * Clears the Kill Shot Track and gives points to all players involved
     */
    public void giveKSTPoints() {
        int[] pointValues = { 8, 6, 4, 2, 1, 1 };

        ArrayList<String> ranking = kst.getRanking();

        for (int i = 0; i < ranking.size(); i++)
            getPlayerByNickname(ranking.get(i)).givePoints(pointValues[i]);

    }

    // TESTED
    /**
     * Show all the different spots where a player is allowed to move
     *
     * @param player The nickname of the player
     * @param nMoves The amount of required moves
     * @return A boolean matrix where the <i, j> element is true only if the player
     *         is allowed to move there
     */
    public boolean[][] wherePlayerCanMove(String player, int nMoves) {
        return this.gameMap.wherePlayerCanMove(player, nMoves);
    }

    // TESTED
    /**
     * Show all the different spots where a player is allowed to move and grab
     *
     * @param player The nickname of the player
     * @param nMoves The amount of required moves
     * @return A boolean matrix where the <i, j> element is true only if the player
     *         is allowed to move there and there is ammo to grab
     */
    public boolean[][] wherePlayerCanMoveAndGrab(String player, int nMoves) {
        return this.gameMap.wherePlayerCanMoveAndGrab(player, nMoves);
    }

    // TESTED
    /**
     * Lets the player pick a new powerup card and choose the new spawn spot to
     * respawn
     *
     * @param player the nickname of the player
     */
    public void respawn(String player, int powerupIndexToDiscard) {

        Player p = getPlayerByNickname(player);

        if (!p.isDead())
            throw new RuntimeException("The player you want to respawn is not dead!");

        Color discardedColor = p.discardPowerUpByIndex(powerupIndexToDiscard);

        // set player alive and move player to spawnspot
        p.revive();
        movePlayerToSpawnColor(player, discardedColor);

        notifyObservers(clientSnapshot());
    }

    // TESTED - PRIVATE METHOD
    /**
     * this method moves the player to the spawn spot associated to that color
     *
     * @param player         the player to respawn
     * @param discardedColor the color of the powerup discarded
     */
    private void movePlayerToSpawnColor(String player, Color discardedColor) {
        gameMap.movePlayerToColorSpawn(player, discardedColor);
        int[] coord = gameMap.getPlayerSpotCoord(player);

        Player p = getPlayerByNickname(player);
        // last Action is modifing the player coordinates
        p.moveTo(coord[0], coord[1]);

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * Refills all the ammo spots
     */
    public void refillAllAmmoSpots() {
        this.gameMap.refillAllAmmo(this.powerupDeck, this.ammoCardDeck);
        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * Refills all the spawn spots with weapons
     */
    public void refillAllSpawnSpots() {
        this.gameMap.refillAllSpawns(this.weaponDeck);
        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * Moves the selected player to the new spot
     *
     * @param player the nickname of the player
     * @param x      the position of the spot on the x-axis
     * @param y      the position of the spot on the y-axis
     */
    public void movePlayer(String player, int x, int y) {

        Player p = getPlayerByNickname(player);

        if (gameMap.validSpot(x, y)) {
            p.moveTo(x, y);
            gameMap.movePlayer(player, x, y);
        }

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * Moves the player to the selected spot and grabs the ammos on it
     *
     * @param player the nickname of the player
     * @param x      the position of the spot on the x-axis
     * @param y      the position of the spot on the y-axis
     */
    public void moveAndGrab(String player, int x, int y, int index) {

        Player p = getPlayerByNickname(player);

        if (gameMap.validSpot(x, y)) {
            p.moveTo(x, y);

            if(getSpotByIndex(x,y).isAmmoSpot()){
                AmmoSpot ammoSpot = (AmmoSpot) getSpotByIndex(x,y);
                if(ammoSpot.hasPowerUp()){
                    getPlayerByNickname(player).givePowerUp(powerupDeck.drawCard());
                }
            }
            gameMap.movePlayer(player, x, y);
            gameMap.grabSomething(x, y, p, index);


        }

        notifyObservers(clientSnapshot());
    }

    /**
     * Tells if player offender can shoot player defenders with the selected weapon
     *
     * @param offendername The nickname of the player who shoots
     * @param defenders    The nickname of the players the offender wants to shoot
     * @param effect       The effect of the weapon
     * @return The players that get shot during this effect
     */
    public ArrayList<Player> whoP1CanShootInThisEffect(String offendername, ArrayList<Player> defenders, Effect effect, ArrayList<Player> playersHit) throws InvalidChoiceException {
        ArrayList<Player> defenders_temp = new ArrayList<>();
        Player offender = getPlayerByNickname(offendername);
        if (effect.isLinear()) check_isLinear(defenders, offender, effect);
        // in questo caso devo colpire tutti i giocatori dentro un determinato spot (lo
        // spot del primo defender in defenders). Per fare questo ho bisogno di contare
        // quanti giocatori ci sono effettivamente dentro questo spot, così so fin dove
        // scorrere la lista dei defenders per applicare l'effetto
        if (effect.mustBeSameSpots() && (effect.getnPlayersAttackable() == 50 || effect.getnPlayersMarkable() == 50))
            return hit_players_same_spot(defenders, offender, effect, defenders_temp,  playersHit);

        if (effect.getVisibleByWho() == Visibility.NONE) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++) {
                // controllo i defenders, se qualcuno non rispetta vuol dire che l'utente ha dato input sbagliati, si ferma il suo attacco
                if (this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition()))
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -NONE");
            }
        }
        if (effect.getVisibleByWho() == Visibility.OFFENDER) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++) {
                // controllo i defenders, se qualcuno non rispetta visibility lo escludo
                if (!this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition()))
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -OFFENDER");
            }
        }
        if (effect.mustBeDifferentSpots()) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++) {
                for (int j = 0; i < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()) && i != j; i++) {
                    if ((defenders.get(i).getxPosition() == defenders.get(j).getxPosition() && defenders.get(i).getyPosition() == defenders.get(j).getyPosition())) {
                        // se due giocatori si trovano nello stesso spot significa che l'utente ha dato informazioni sbagliate, lancio eccezione
                        throw new InvalidChoiceException("almeno due giocatori non rispettano mustBeDifferentSpots");
                    }
                }
            }
        }
        if (effect.mustBeSameSpots()) {
            for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++) {
                for (int j = 0; i < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()) && i != j; i++) {
                    if ((defenders.get(i).getxPosition() != defenders.get(j).getxPosition() || defenders.get(i).getyPosition() != defenders.get(j).getyPosition())) {
                        // se due  giocatori si trovano in spot diversi significa che l'utente ha dato informazioni sbagliate, lancio ccezione
                        throw new InvalidChoiceException("almeno due giocatori non rispettano mustBeSameSpots");
                    }
                }
            }
        }
        for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++) {
            // if a defender is not minDistance < |distance offender-defender| < MaxDistance emove him.
            int distance = gameMap.distance(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition());
            if (distance < effect.getMinDistance()) throw new InvalidChoiceException("qualche defender non rispetta la distanza per questo effetto MinDistance");
            if (distance > effect.getMaxDistance()) throw new InvalidChoiceException("qualche defender non rispetta la distanza per questo effetto MaxDistance");
            if (effect.getVisibleByWho() == Visibility.LASTONEATTACKED) {
                if (playersHit.isEmpty()) throw new InvalidChoiceException("playersHit is empty and visibility is LASTONEATTACKED");
                if (!this.gameMap.see(playersHit.get(playersHit.size() - 1).getxPosition(), playersHit.get(playersHit.size() - 1).getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition()))
                    throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -LASTONEATTACKED");
            }
            // caso in cui devo sparare a giocatori tutti diversi in questo effetto
            if (effect.mustShootOtherPlayers() && effect.mustBeOtherRoom()) {
                for (int k = 0; i < defenders.size() && (k < effect.getnPlayersAttackable() || k < effect.getnPlayersMarkable()); k++) {
                    for (int j = 0; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()) && k != j; j++)
                        if (defenders.get(k) == defenders.get(j)) throw new InvalidChoiceException("can't shoot the same player twice in this effect!");
                }
            } else if (effect.mustBeOtherRoom()) {
                if (this.gameMap.getPlayerRoom(offendername) == this.gameMap.getPlayerRoom(defenders.get(i).getNickname()))
                    throw new InvalidChoiceException("almeno un defender è nella stessa stranza dell'offender -MustBeOtherRoom ");
            }
            // siamo nel cso in cui bisogna sparare a tutti quelli a cui non ho sparato più
            // l'ultimo colpito
            else if (effect.mustShootOtherPlayers() && effect.mustShootSamePlayers()) {
                if (playersHit.isEmpty()) throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1 and mustShootSamePlayers = 1");
                defenders_temp.add(playersHit.get(playersHit.size() - 1));
                for (int j = 0; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++) defenders_temp.add(defenders.get(j));
                return defenders_temp;
            } else if (effect.mustShootOtherPlayers()) { // per ogni player in defenders scorro i players in playersHit, se ne trovo due uguali lancio eccezione
                if (playersHit.isEmpty()) throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1");
                for (int k = 0; k < playersHit.size(); k++)
                    if (defenders.get(i) == playersHit.get(k)) throw new InvalidChoiceException("cercando di sparare ad un giocatore già colpito, non permesso in questo attacco  -MUSTSHOOTOTHERPLAYERS");
            } else if (effect.mustShootSamePlayers()) { // caso in cui devo sparare ai players a cui ho già sparato
                if (playersHit.isEmpty()) throw new InvalidChoiceException("playersHit is empty and mustShootSamePlayers = 1");
                if (effect.getnPlayersAttackable() != 0) {
                    for (int j = 0; j < effect.getnPlayersAttackable(); j++)
                        if (!playersHit.contains(defenders.get(j))) throw new InvalidChoiceException("cercando di sparare ad un giocatore che non ho mai colpito, non permesso in questo attacco -MUSTSHOOTSAMEPLAYER");
                }
                if (effect.getnPlayersMarkable() != 0) {
                    for (int j = 0; j < effect.getnPlayersMarkable(); j++)
                        if (!playersHit.contains(defenders.get(j))) throw new InvalidChoiceException("cercando di sparare ad un giocatore che non ho mai colpito, non permesso in questo attacco -MUSTSHOOTSAMEPLAYER");
                }
            }
        }
        for (int i = 0; i < defenders.size() && (i < effect.getnPlayersAttackable() || i < effect.getnPlayersMarkable()); i++)
            defenders_temp.add(defenders.get(i)); // questi sono i giocatori a cui effettivamente faccio danno
        return defenders_temp;
    }

    // submethod called by whoP1CanShootInThisEffect
    private void check_isLinear(ArrayList<Player> defenders, Player offender, Effect effect) throws InvalidChoiceException {
        if (effect.isLinear()) {
            for (int i = 0; i < defenders.size(); i++) {
                // se sia x che y sono diverse il defender è sicuramente non allineato
                if (defenders.get(i).getxPosition() != offender.getxPosition() && defenders.get(i).getyPosition() != offender.getyPosition()) throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_1");
                // controllo se offender e defender.get(i) non siano nello stesso spot, se così fosse andrebbe bene ma non sarebbe ancora decisa la direzione che devono rispettare i defenders successivi
                if (defenders.get(i).getxPosition() != offender.getxPosition() || defenders.get(i).getyPosition() != offender.getyPosition()) {
                    if (defenders.get(i).getxPosition() == offender.getxPosition()) { // offender e defender sono sulla stessa riga
                        if (defenders.get(i).getyPosition() > offender.getyPosition()) {
                            // il defender è a EAST rispetto all'offender, controllo se anche gli altri defenders sono a EAST rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for (int j = i; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++) {
                                if (defenders.get(j).getxPosition() != offender.getxPosition()) throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_2");
                                if (defenders.get(j).getyPosition() < offender.getyPosition()) throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_3");
                            }
                        } else if (defenders.get(i).getyPosition() < offender.getyPosition()) {
                            // il primo defender è a WEST rispetto all'offender, controllo se anche gli altri defenders sono a WEAST rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for (int j = i; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++) {
                                if (defenders.get(j).getxPosition() != offender.getxPosition()) throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_4");
                                if (defenders.get(j).getyPosition() > offender.getyPosition()) throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_5");
                            }
                        }
                    }
                    if (defenders.get(i).getyPosition() == offender.getyPosition()) { // offender e il defender sono sulla stessa colonna
                        if (defenders.get(i).getxPosition() > offender.getxPosition()) {
                            // il defender è a SOUTH rispetto all'offender, controllo se anche gli altri defenders sono a SOUTH rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for (int j = i; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++) {
                                if (defenders.get(j).getyPosition() != offender.getyPosition()) throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_6");
                                if (defenders.get(j).getxPosition() < offender.getxPosition()) throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_7");
                            }
                        } else if (defenders.get(0).getxPosition() < offender.getxPosition()) {
                            // il defender è a NORTH rispetto all'offender, controllo se anche gli altri defenders sono a NORTH rispetto all'offender, se almeno uno non rispetta, lancio l'eccezione
                            for (int j = i; j < defenders.size() && (j < effect.getnPlayersAttackable() || j < effect.getnPlayersMarkable()); j++) {
                                if (defenders.get(j).getyPosition() != offender.getyPosition()) throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_8");
                                if (defenders.get(j).getxPosition() > offender.getxPosition()) throw new InvalidChoiceException("defenders are not in the right spots -ISLINEAR_9");
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Player> hit_players_same_spot(ArrayList<Player> defenders, Player offender, Effect effect, ArrayList<Player> defenders_temp, ArrayList<Player> playersHit) throws InvalidChoiceException {
            //questi sono i giocatori nello stesso post tra quelli passati dall'utente, escluso l'offender
            int nPlayersInTheSameSpot = 1;
            //questi sono tutti i giocatori che sono effettivamente in quello spot, escluso l'offender, presi dalla mappa. Se questo numero di giocatori è diverso da nPlayersInTheSameSpot vuol dire che l'utente non mi ha passato tutti i giocatori correttamente, il suo attacco si ferma
            int nPlayersSameSpotFromMap = 1;
            if ( effect.getnPlayersAttackable() == 50 ) {
                for (Player p : players) {
                    if (p != offender && p != defenders.get(0))
                        if (p.getxPosition() == defenders.get(0).getxPosition() && p.getyPosition() == defenders.get(0).getyPosition()) nPlayersSameSpotFromMap++;
                }
                for ( int i = 1; i < defenders.size(); i++){
                    if ( defenders.get(i) != offender)
                        if (defenders.get(0).getxPosition() == defenders.get(i).getxPosition() && defenders.get(0).getyPosition() == defenders.get(i).getyPosition()) nPlayersInTheSameSpot++;
                }
            }else{ //entra qua se nPlayersMarkable == 50, cioè devo marchiare tutti i player nello stesso spot del player che ho colpito precedentemente, lui incluso
                defenders_temp.add(playersHit.get(playersHit.size()-1));
                for (Player p : players) {
                    if (p != offender && p != playersHit.get(playersHit.size()-1))
                        if (p.getxPosition() == playersHit.get(playersHit.size()-1).getxPosition() && p.getyPosition() == playersHit.get(playersHit.size()-1).getyPosition()) nPlayersSameSpotFromMap++;
                }
                for ( int i = 0; i < defenders.size(); i++)
                    if ( playersHit.get(playersHit.size()-1).getxPosition() == defenders.get(i).getxPosition() && playersHit.get(playersHit.size()-1).getyPosition() == defenders.get(i).getyPosition()) nPlayersInTheSameSpot++;
            }
            if (nPlayersInTheSameSpot != nPlayersSameSpotFromMap) throw new InvalidChoiceException("i giocatori passati dall'utente non corrispondono a tutti i giocatori in un determinato spot");
            if (effect.getVisibleByWho() == Visibility.NONE) {
                for (int i = 0; i < nPlayersInTheSameSpot && i < defenders.size(); i++) {   //controllo i defenders, se qualcuno non rispetta vuol dire che l'utente ha dato input sbagliati, si ferma il suo attacco
                    if (this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition()))
                        throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -NONE");
                }
            }
            if (effect.getVisibleByWho() == Visibility.OFFENDER) {
                for (int i = 0; i < nPlayersInTheSameSpot && i < defenders.size(); i++) //controllo i defenders, se qualcuno non rispetta visibility lo escludo
                    if (!this.gameMap.see(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition())) throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -OFFENDER");
            }
            for (int i = 0; i < nPlayersInTheSameSpot && i < defenders.size(); i++) {      //if a defender is not minDistance < |distance offender-defender| < MaxDistance remove him.
                int distance = gameMap.distance(offender.getxPosition(), offender.getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition());
                if (distance < effect.getMinDistance()) throw new InvalidChoiceException("qualche defender non rispetta la distanza per questo effetto MinDistance");
                if (distance > effect.getMaxDistance()) throw new InvalidChoiceException("qualche defender non rispetta la distanza per questo effetto MaxDistance");
                if (effect.getVisibleByWho() == Visibility.LASTONEATTACKED) {
                    if (playersHit.isEmpty()) throw new InvalidChoiceException("playersHit is empty and visibility is LASTONEATTACKED");
                    if (!this.gameMap.see(playersHit.get(playersHit.size() - 1).getxPosition(), playersHit.get(playersHit.size() - 1).getyPosition(), defenders.get(i).getxPosition(), defenders.get(i).getyPosition()))
                        throw new InvalidChoiceException("qualche defender non rispetta la visibilità dell'effetto -LASTONEATTACKED");
                }
                if (effect.mustShootOtherPlayers() && !effect.mustBeOtherRoom()) { //per ogni player in defenders scorro i players in playersHit, se ne trovo due uguali lancio eccezione
                    if (playersHit.isEmpty()) throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1");
                    for (int k = 0; k < playersHit.size() - 1; k++)
                        if (defenders.get(i) == playersHit.get(k)) throw new InvalidChoiceException("cercando di sparare ad un giocatore già colpito, non permesso in questo attacco");
                }else { //caso in cui devo sparare a giocatori tutti diversi in questo effetto
                    if ( effect.mustShootOtherPlayers() && effect.mustBeOtherRoom()){
                        for (int j = 0; j < nPlayersInTheSameSpot && j < defenders.size() && j != i; j++)
                            if (defenders.get(i) == defenders.get(j)) throw new InvalidChoiceException("trying to shoot the same defender twice, not permitted");
                    }
                }
                //vuol dire che devo sparare ai giocatori a cui avevo già sparato
                if (effect.mustShootSamePlayers()) { //per ogni player in defenders scorro i players in playersHit, se ne trovo due uguali lancio eccezione
                    if (playersHit.isEmpty()) throw new InvalidChoiceException("playersHit is empty and mustShootOtherPlayers = 1");
                    for (int k = 0; k < playersHit.size() - 1; k++)
                        if (defenders.get(i) == playersHit.get(k)) throw new InvalidChoiceException("cercando di sparare ad un giocatore già colpito, non permesso in questo attacco");
                }
            }
            //modifico defenders_temp, ovvero la lista dei giocatori a cui devo effettivamente sparare che passo a ShootPlayer, in modo da avere solo e tutti i giocatori che sono nello stesso spot.
            for (int i = 0; i < nPlayersInTheSameSpot && i < defenders.size(); i++)
                defenders_temp.add(defenders.get(i));       //questi sono i giocatori a cui effettivamente faccio danno
            return defenders_temp;
    }

    /**
     * this method checks what type of effect effect is
     *
     * @param effect the effect to check
     * @return the type of this effect. 0 = movemente effect, 1 = damage effect
     */
    public int typeOfEffect(Effect effect) {

        if (effect.getnMoves() != 0 || effect.getnMovesOtherPlayer() != 0)
            return 0; // movement effect
        else
            return 1; // damage effect

    }

    /**
     * this method check if the player can pay the cost in effect, then pays it
     *
     * @param effect the effect we have to see to pay his cost
     * @param player the player who has to pay
     * @return true if player pays, false if the player can't pay
     */
    public boolean payCostEffect(Effect effect, String player) {

        if (effect.getCost() != null) { // if there is a cost I pay it ( for example an optional shooting Action )
            try {
                pay(player, effect.getCost());
                return true;
            } catch (InvalidChoiceException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is called by ShootWithMovement() and makes the actual effect of
     * movement
     *
     * @param playersWhoMoveNames is the player being moved
     * @param effect              is the movement effect we have to look at
     * @param xPos                is the x position of the player after he moved
     * @param yPos                is the y position of the player after he moved
     * @throws InvalidChoiceException
     */
    public void makeMovementEffect(ArrayList<String> playersWhoMoveNames, Effect effect, ArrayList<Integer> xPos,
            ArrayList<Integer> yPos, ArrayList<Player> playersHit, String offenderName) throws InvalidChoiceException {

        ArrayList<Player> playersWhoMove = new ArrayList<>();

        Player offender = getPlayerByNickname(offenderName);

        int i = 0;

        for (String p : playersWhoMoveNames) {
            playersWhoMove.add(getPlayerByNickname(p));
        }

        if (effect.mustShootOtherPlayers()) {

            for (int k = 0; k < playersHit.size(); k++) {
                if (playersWhoMove.get(0) == playersHit.get(k)) {
                    throw new InvalidChoiceException(
                            "cercando di spostare ad un giocatore già spostato, non permesso in questa mossa  -MUSTSHOOTOTHERPLAYERS");
                }
            }
        }

        if (!playersWhoMoveNames.isEmpty()) {
            if (effect.getnMoves() != 0) {

                if (playersWhoMove.get(i) != offender) {
                    throw new InvalidChoiceException("Trying to move a defender instead of the offender");
                }

                if (this.gameMap.canMoveFromTo(playersWhoMove.get(i).getxPosition(),
                        playersWhoMove.get(i).getyPosition(), xPos.get(i), yPos.get(i), effect.getnMoves())) {
                    movePlayer(playersWhoMove.get(i).getNickname(), xPos.get(i), yPos.get(i));
                    xPos.remove(i);
                    yPos.remove(i);
                    if (playersWhoMove.get(i) != offender) {
                        playersHit.add(playersWhoMove.get(i));
                    }
                    playersWhoMoveNames.remove(i);
                } else
                    throw new InvalidChoiceException("giocatore spostato di number of spots != nMoves");
            }

            if (effect.getnMovesOtherPlayer() != 0) {

                if (playersWhoMove.get(i) == offender) {
                    throw new InvalidChoiceException("Trying to move the offender instead of a defender");
                }

                if (this.gameMap.canMoveFromTo(playersWhoMove.get(i).getxPosition(),
                        playersWhoMove.get(i).getyPosition(), xPos.get(i), yPos.get(i),
                        effect.getnMovesOtherPlayer())) {
                    movePlayer(playersWhoMove.get(i).getNickname(), xPos.get(i), yPos.get(i));
                    xPos.remove(i);
                    yPos.remove(i);
                    if (playersWhoMove.get(i) != offender) {
                        playersHit.add(playersWhoMove.get(i));
                    }
                    playersWhoMoveNames.remove(i);
                } else
                    throw new InvalidChoiceException("giocatore spostato di number of spots != nMovesOtherPlayer");
            }
        }
    }

    /**
     * This method is called by ShootWithMovemente() and ShootWithMovemente() and
     * makes the actual effect of damage
     *
     * @param offendername   is the player who makes damage
     * @param defenders_temp are the players who receive damage
     * @param effect         is the effect we have to look at
     */
    public void makeDamageEffect(String offendername, ArrayList<Player> defenders_temp, Effect effect) {

        Player offender = getPlayerByNickname(offendername);

        if (effect.getnPlayersAttackable() != 0) { // per ogni giocatore a cui bisogna dare danni presente nella lista
                                                   // dei defenders, assegno nDamages dell'effetto
            for (int i = 0; i < defenders_temp.size() && i < effect.getnPlayersAttackable(); i++) {
                defenders_temp.get(i).giveDamage(offendername, effect.getnDamages());
            }
        }

        if (effect.getnPlayersMarkable() != 0) { // per ogni giocatore a cui bisogna dare marchi presente nella lista
                                                 // dei defenders, assegno nMarks dell'effetto
            for (int i = 0; i < defenders_temp.size() && i < effect.getnPlayersMarkable(); i++) {
                defenders_temp.get(i).giveMarks(offendername, effect.getnMarks());
            }
        }
    }

    /**
     * This method, called from the controller, makes the actual shooting of the
     * weapon with movement effects
     *
     * @param offenderName        is the player who uses the weapon
     * @param defendersNames      are the players who receive the effects
     * @param weapon              is the weapon being used to shoot
     * @param chosenOrder         is the order of the effect chose by the user
     * @param xPosition           is the new x position of the player being moved
     * @param yPosition           is the new y position of the player being moved
     * @param playersWhoMoveNames is the player who moves
     * @return return true if all the checks on the effects are executed the right
     *         way
     * @throws InvalidChoiceException
     */
    public boolean shootWithMovement(String offenderName, ArrayList<String> defendersNames, Weapon weapon,
            Integer[] chosenOrder, ArrayList<Integer> xPosition, ArrayList<Integer> yPosition,
            ArrayList<String> playersWhoMoveNames) {

        ArrayList<Player> defenders = new ArrayList<>();

        ArrayList<Player> defenders_temp = new ArrayList<>();

        ArrayList<Player> playersHit = new ArrayList<>();

        GameMap backUpMap = new GameMap(this.gameMap);

        ArrayList<Player> backUpPlayers = new ArrayList<>();

        for (Player p : this.players) {
            backUpPlayers.add(new Player(p));
        }

        for (String i : defendersNames)
            defenders.add(getPlayerByNickname(i));

        try {
            // se l'arma è scarica non si può sparare, il giocatore perde un'azione
            if (!weapon.isLoaded())
                throw new InvalidChoiceException("This weapon is not loaded");

            for (int indexOfEffect = 0; indexOfEffect < chosenOrder.length; indexOfEffect++) { // scorro gli effetti di quest'arma nell'ordine scelto
                                                               // dall'utente

                Effect effetto = weapon.getEffects().get(chosenOrder[indexOfEffect]);

                if (typeOfEffect(effetto) == 0) { // Movement effect

                    if (playersWhoMoveNames.isEmpty())
                        return true;

                    // se l'effetto è linear devo fare un controllo sulla direzione cardinale in cui
                    // voglio sparare
                    if (effetto.isLinear) {

                        Direction cardinalDirection = Direction.NONE;

                        // di seguito calcolo la direzione cardinale confrontando il primo player che
                        // voglio spostare in playersWhoMoveNames e dove voglio spostarlo
                        // successivamente guardando xPos e yPos
                        if (xPosition.get(0) == getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition()
                                && yPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition()) {

                            if (yPosition.get(0) > getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition())
                                cardinalDirection = Direction.EAST;
                            else if (yPosition.get(0) < getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition())
                                cardinalDirection = Direction.WEST;

                        } else if (xPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition()
                                && yPosition.get(0) == getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition()) {

                            if (xPosition.get(0) > getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition())
                                cardinalDirection = Direction.SOUTH;
                            else if (xPosition.get(0) < getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition())
                                cardinalDirection = Direction.NORTH;

                        } else if (xPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(0)).getxPosition()
                                && yPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(0)).getyPosition())
                            throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------NON LINEAR");

                        // una volta calcolata la direzione cardinale controllo che tutti i movers
                        // rispettino questa direzione
                        for (int cont = 0; cont < playersWhoMoveNames.size(); cont++) {
                            if (cardinalDirection == Direction.NORTH) {
                                if (yPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(cont))
                                        .getyPosition()
                                        || xPosition.get(0) < getPlayerByNickname(playersWhoMoveNames.get(cont))
                                                .getxPosition())
                                    throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------1");
                            }
                            if (cardinalDirection == Direction.SOUTH) {
                                if (yPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(cont))
                                        .getyPosition()
                                        || xPosition.get(0) > getPlayerByNickname(playersWhoMoveNames.get(cont))
                                                .getxPosition())
                                    throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------2");
                            }
                            if (cardinalDirection == Direction.EAST) {
                                if (xPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(cont))
                                        .getxPosition()
                                        || yPosition.get(0) < getPlayerByNickname(playersWhoMoveNames.get(cont))
                                                .getyPosition())
                                    throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------3");
                            }
                            if (cardinalDirection == Direction.WEST) {
                                if (xPosition.get(0) != getPlayerByNickname(playersWhoMoveNames.get(cont))
                                        .getxPosition()
                                        || yPosition.get(0) > getPlayerByNickname(playersWhoMoveNames.get(cont))
                                                .getyPosition())
                                    throw new InvalidChoiceException("HAI SBAGLIATO DIREZIONE-------4");
                            }
                        }
                    }

                    makeMovementEffect(playersWhoMoveNames, effetto, xPosition, yPosition, playersHit, offenderName);

                    if (!payCostEffect(effetto, offenderName)) { // if the effect has a cost, the player pays it
                        throw new InvalidChoiceException("Cannot pay");
                    }
                }

                if (typeOfEffect(effetto) == 1) { // Damage effect

                    /*
                     * a whoP1ShootsInThisEffect gli passo al primo giro la lista dei nomi dei
                     * players ricevuta dall'utente, ai giri successivi i players a cui non ho
                     * ancora sparato, cioè quelli a cui devo sparare in questo effetto. Questo
                     * metodo mi ritorna una lista di player che effettivamente vengono
                     * colpiti/marchiati durante quest'effetto. Quindi li aggiungo a PlayersHit
                     */

                    defenders_temp = whoP1CanShootInThisEffect(offenderName, defenders, effetto, playersHit);

                    if (defenders_temp.isEmpty())
                        return true;

                    if (!payCostEffect(effetto, offenderName)) { // if the effect has a cost, the player pays it
                        throw new InvalidChoiceException("Cannot pay");
                    }

                    /*
                     * rimuovo da defendesNames i giocatori che ho colpito nell'effetto appena
                     * eseguito, così nel prossimo giro nel ciclo, ovvero nel prossimo effetto,
                     * escludo i giocatori colpiti nell'effetto precedente (esempio se il giovatore
                     * vuole colpire andreaalf poi gino poi andreaalf ( ma nel primo effetto si ha
                     * nPlayersAttackable = 2 ), WhoP1ShootsInThisEffect ritorna andreaalf, gino, li
                     * colpisco e li aggiungo a playersHit, nel prossimo effetto passo a
                     * whoP1CanShootInThisEffect defenders - defenders_temp, ovvero andreaalf (poi
                     * se c'è un must_shoot_other player in questo effetto lancio un'eccezione
                     */
                    for (Player p : defenders_temp) {
                        playersHit.add(p);
                        defenders.remove(p);
                    }
                    makeDamageEffect(offenderName, defenders_temp, effetto);
                }
            }
            if (defenders.size() != 0) {
                throw new InvalidChoiceException("Too many players for this weapon");
            }
            weapon.setLoaded(false);

            notifyObservers(clientSnapshot());

            return true;
        } catch (InvalidChoiceException e) {
            // resetto mappa
            this.gameMap = new GameMap(backUpMap);
            // resetto tutti i players in this game
            this.players = new ArrayList<>(backUpPlayers);
            MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    /**
     * This method, called from the controller, makes the actual shooting of the
     * weapon without movement effects
     *
     * @param offenderName   is the player who uses the weapon
     * @param defendersNames are the players who receive the effects
     * @param weapon         is the weapon being used to shoot
     * @param orderNumber    is the order of the effect chose by the user
     * @return return true if all the checks on the effects are executed the right
     *         way
     */
    public boolean shootWithoutMovement(String offenderName, ArrayList<String> defendersNames, Weapon weapon,
            Integer[] orderNumber) {

        ArrayList<Player> defenders = new ArrayList<>();

        ArrayList<Player> defenders_temp = new ArrayList<>();

        ArrayList<Player> playersHit = new ArrayList<>();

        ArrayList<Player> backUpPlayers = new ArrayList<>();

        GameMap backUpMap = new GameMap(this.gameMap);

        for (Player p : this.players) {
            backUpPlayers.add(new Player(p));
        }

        // defenders è la lista di tutti i giocatori che arriva dall'utente
        for (String i : defendersNames)
            defenders.add(getPlayerByNickname(i));

        try {
            // se l'arma è scarica non si può sparare, il giocatore perde un'azione
            if (!weapon.isLoaded())
                throw new InvalidChoiceException("This weapon is not loaded");

            for (int indexOfEffect = 0; indexOfEffect < orderNumber.length; indexOfEffect++) { // scorro gli effetti di quest'arma nell'ordine scelto
                                                               // dall'utente

                Effect effetto = weapon.getEffects().get(orderNumber[indexOfEffect]);

                if (typeOfEffect(effetto) == 1) { // Damage effect

                    /*
                     * a whoP1ShootsInThisEffect gli passo al primo giro la lista dei nomi dei
                     * players ricevuta dall'utente, ai giri successivi i players a cui non ho
                     * ancora sparato, cioè quelli a cui devo sparare in questo effetto. Questo
                     * metodo mi ritorna una lista di player che effettivamente vengono
                     * colpiti/marchiati durante quest'effetto. Quindi li aggiungo a PlayersHit
                     */

                    defenders_temp = whoP1CanShootInThisEffect(offenderName, defenders, effetto, playersHit);
                    if (defenders_temp.isEmpty()) {
                        return true;
                    }

                    if (!payCostEffect(effetto, offenderName)) { // if the effect has a cost, the player pays it
                        throw new InvalidChoiceException("Cannot pay");
                    }

                    /*
                     * rimuovo da defendesNames i giocatori che ho colpito nell'effetto appena
                     * eseguito, così nel prossimo giro nel ciclo, ovvero nel prossimo effetto,
                     * escludo i giocatori colpiti nell'effetto precedente (esempio se il giovatore
                     * vuole colpire andreaalf poi gino poi andreaalf ( ma nel primo effetto si ha
                     * nPlayersAttackable = 2 ), WhoP1ShootsInThisEffect ritorna andreaalf, gino, li
                     * colpisco e li aggiungo a playersHit, nel prossimo effetto passo a
                     * whoP1CanShootInThisEffect defenders - defenders_temp, ovvero andreaalf (poi
                     * se c'è un must_shoot_other player in questo effetto lancio un'eccezione
                     */
                    for (Player p : defenders_temp) {
                        playersHit.add(p);
                        defenders.remove(p);
                        // defendersNames.remove(p.getNickname());
                    }
                    makeDamageEffect(offenderName, defenders_temp, effetto);
                }
            }
            if (defenders.size() != 0) {
                throw new InvalidChoiceException("Too many players for this weapon");
            }
            weapon.setLoaded(false);

            notifyObservers(clientSnapshot());

            return true;
        } catch (InvalidChoiceException e) {
            // resetto mappa
            this.gameMap = new GameMap(backUpMap);
            // resetto tutti i players in this game
            this.players = new ArrayList<>(backUpPlayers);
            MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    /**
     * This method uses a power with damage effect
     *
     * @param currentPlayerName          is the player who uses the powerup
     * @param playerWhoReceiveEffectName is the player who receives the effect of
     *                                   the power up
     * @param effect                     is the effect of the power up
     * @throws InvalidChoiceException
     */
    public void useDamagePowerUp(String currentPlayerName, String playerWhoReceiveEffectName, Effect effect)
            throws InvalidChoiceException {

        if (currentPlayerName.equals(playerWhoReceiveEffectName))
            throw new InvalidChoiceException("you can't shoot to yourself");

        Player defender = getPlayerByNickname(playerWhoReceiveEffectName);

        Player currentPlayer = getPlayerByNickname(currentPlayerName);

        if (effect.getVisibleByWho() == Visibility.OFFENDER && !gameMap.see(currentPlayer.getxPosition(),
                currentPlayer.getyPosition(), defender.getxPosition(), defender.getyPosition()))
            throw new InvalidChoiceException("you can't see the defender");

        // creo un ArrayList<Player> in cui ci sarà solo un player per passarlo a
        // makeDamageEffect
        ArrayList<Player> defenders = new ArrayList<>();
        defenders.add(defender);

        makeDamageEffect(currentPlayerName, defenders, effect);

        notifyObservers(clientSnapshot());
    }

    /**
     * This method uses a power with movement effect
     *
     * @param currentPlayerName          is the player who uses the powerup
     * @param playerWhoReceiveEffectName is the player who receives the effect of
     *                                   the power up
     * @param effect                     is the effect of the power up
     * @param xPos                       is the new x position of the player being
     *                                   moved
     * @param yPos                       is the new y position of the player being
     *                                   moved
     * @throws InvalidChoiceException
     */
    public void useMovementPowerUp(String currentPlayerName, String playerWhoReceiveEffectName, Effect effect, int xPos,
            int yPos) throws InvalidChoiceException {

        Player playerWhoReceiveEffect = getPlayerByNickname(playerWhoReceiveEffectName);

        if (effect.getnMoves() != 0) {
            if (currentPlayerName != playerWhoReceiveEffectName)
                throw new InvalidChoiceException("you can't move another player");
            else
                movePlayer(playerWhoReceiveEffectName, xPos, yPos);

        }

        if (effect.getnMovesOtherPlayer() != 0) {
            if (currentPlayerName == playerWhoReceiveEffectName)
                throw new InvalidChoiceException("you can't move yourself");
            else {
                if (this.gameMap.canMoveFromTo(playerWhoReceiveEffect.getxPosition(),
                        playerWhoReceiveEffect.getyPosition(), xPos, yPos, effect.getnMovesOtherPlayer())) {
                    movePlayer(playerWhoReceiveEffectName, xPos, yPos);
                } else
                    throw new InvalidChoiceException("giocatore spostato di number of spots != nMovesOtherPlayer");
            }
        }

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * check the weapons that player has unloaded
     *
     * @param player the player to check
     * @return an arraylist of the weapons unloaded
     */
    public ArrayList<Weapon> checkRechargeableWeapons(String player) {
        Player currentPlayer = getPlayerByNickname(player);
        ArrayList<Weapon> rechargeableWeapons = new ArrayList<>();

        int nRedPowerUp = Collections.frequency(currentPlayer.getPowerUpColors(), Color.RED);
        int nBluePowerUp = Collections.frequency(currentPlayer.getPowerUpColors(), Color.BLUE);
        int nYellowPowerUp = Collections.frequency(currentPlayer.getPowerUpColors(), Color.YELLOW);

        for (int i = 0; i < currentPlayer.getWeaponList().size(); i++) {
            // first i need the cost of the weapon unloaded
            if (!currentPlayer.getWeaponList().get(i).isLoaded()) {
                ArrayList<Color> weaponCost = currentPlayer.getWeaponList().get(i).getCost();

                // cost of the weapon divided
                int nRedAmmoWeapon = Collections.frequency(weaponCost, Color.RED);
                int nBlueAmmoWeapon = Collections.frequency(weaponCost, Color.BLUE);
                int nYellowAmmoWeapon = Collections.frequency(weaponCost, Color.YELLOW);

                // add in the list the weapons that the player can reload with his ammo
                if ((nRedAmmoWeapon < currentPlayer.getnRedAmmo() + nRedPowerUp)
                        && (nYellowAmmoWeapon < currentPlayer.getnYellowAmmo() + nYellowPowerUp)
                        && (nBlueAmmoWeapon < currentPlayer.getnBlueAmmo() + nBluePowerUp))
                    rechargeableWeapons.add(currentPlayer.getWeaponList().get(i));
            }
        }

        return rechargeableWeapons;
    }

    // TESTED
    /**
     * reload the weapon of the player corresponding to index , i am sure that i can
     * reload that weapon -> see ReloadWeapon in controller.java
     *
     * @param player
     * @param index
     */
    public void reloadWeapon(String player, int index) {

        Player currentPlayer = getPlayerByNickname(player);

        currentPlayer.reloadWeapon(index);

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * this method receive a player and a weapon
     *
     * @param player         the player to examinate
     * @param weaponToReload the weapon that we search in the player hand
     * @return the index of the weapon in the hand of the player
     */
    public int getRealWeaponIndexOfTheUnloadedWeapon(String player, Weapon weaponToReload) {
        int realIndex = -1;
        Player currentPlayer = getPlayerByNickname(player);

        for (int i = 0; i < currentPlayer.getWeaponList().size(); i++)
            if (currentPlayer.getWeaponList().get(i).equals(weaponToReload))
                realIndex = i;

        return realIndex;
    }

    // ONLY USED IN TESTS
    /**
     * Revives the player
     *
     * @param playerName the nickname of the player
     */
    protected void revive(String playerName) {

        Player p = getPlayerByNickname(playerName);

        p.revive();

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * @return a copy of the list of Players
     */
    protected ArrayList<Player> clonePlayers() {
        return new ArrayList<>(this.players);
    }

    // TESTED
    /**
     * @return a copy of the kill shot track
     */
    protected KillShotTrack cloneKST() {
        return this.kst.clone();
    }

    // TESTED
    /**
     * @return a copy of the game map
     */
    protected GameMap cloneGameMap() {
        return this.gameMap.clone();
    }

    // TESTED
    /**
     * Makes the player pay the correct amount of ammo
     *
     * @param nickname the nickname
     * @param cost     the cost to pay
     */
    public void pay(String nickname, ArrayList<Color> cost) throws InvalidChoiceException {

        Player p = getPlayerByNickname(nickname);

        for (Color i : cost) {

            switch (i) {
            case RED:
                if (p.getnRedAmmo() == 0)
                    throw new InvalidChoiceException("Not enough red ammo");
                break;
            case BLUE:
                if (p.getnBlueAmmo() == 0)
                    throw new InvalidChoiceException("Not enough blue ammo");
                break;
            case YELLOW:
                if (p.getnYellowAmmo() == 0)
                    throw new InvalidChoiceException("Not enough yellow ammo");
                break;
            case ANY:
                throw new RuntimeException("Color.ANY can't appear here");
            }

            p.removeAmmo(i);
        }

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * Gives the ammo card on the spot to the player
     *
     * @param player the player who grab ammo
     * @throws InvalidChoiceException if this isn't an ammo spot
     */
    public void giveAmmoCard(String player) throws RuntimeException {

        Player p = getPlayerByNickname(player);

        int x = p.getxPosition();
        int y = p.getyPosition();

        if (!gameMap.isAmmoSpot(x, y))
            throw new RuntimeException("This is not an ammo spot");

        gameMap.grabSomething(x, y, p, -1);

        notifyObservers(clientSnapshot());

    }

    // TESTED
    /**
     * Returns a copy of the weapons on the spawn spot
     *
     * @param roomColor the color of the spawn
     * @return a copy of the weapons
     */
    public ArrayList<Weapon> showSpawnSpotWeapons(Color roomColor) {
        return gameMap.showSpawnSpotWeapons(roomColor);
    }

    // TESTED
    /**
     * pick a weapon from the spawnspot where the player is
     *
     * @param nickname   the player who wants to pick the weapon
     * @param weaponName the name of the weapon to pick in the spawnspot weapon list
     * @throws RuntimeException if this is not a spawn spot
     */
    public void pickWeaponFromSpawn(String nickname, String weaponName) throws RuntimeException {

        Player p = getPlayerByNickname(nickname);

        int x = p.getxPosition();
        int y = p.getyPosition();

        if (!gameMap.isSpawnSpot(x, y))
            throw new RuntimeException("this is not a spawn spot");

        int index = gameMap.indexOfWeapon(x, y, weaponName);

        gameMap.grabSomething(x, y, p, index);

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * pick a weapon from the spawnspot where the player is
     *
     * @param nickname the player who wants to pick the weapon
     * @param index    the index of the weapon to pick in the spawnspot weapon list
     * @throws RuntimeException if this is not a spawn spot
     */
    public void pickWeapon(String nickname, int index) throws RuntimeException {

        Player p = getPlayerByNickname(nickname);

        int x = p.getxPosition();
        int y = p.getyPosition();

        if (!gameMap.isSpawnSpot(x, y))
            throw new RuntimeException("this is not a spawn spot");

        gameMap.grabSomething(x, y, p, index);

        notifyObservers(clientSnapshot());

    }

    /**
     * switch a weapon of the player with a weapon on the spawnspot
     *
     * @param player          the player who switch the weapon
     * @param weaponToPick    index of the weapon to pick from the spawn spot weapon
     *                        list
     * @param weaponToDiscard index of the weapon to discard from player hand
     */
    public void switchWeapons(String player, String weaponToPick, String weaponToDiscard) {

        Player p = getPlayerByNickname(player);

        int x = p.getxPosition();
        int y = p.getyPosition();

        if (!gameMap.isSpawnSpot(x, y))
            throw new RuntimeException("This is not a spawn spot");

        Weapon toDiscard = p.removeWeaponByName(weaponToDiscard);

        toDiscard.reload();

        gameMap.grabWeapon(x, y, p, weaponToPick);

        gameMap.refill(x, y, toDiscard);

        notifyObservers(clientSnapshot());
    }

    // TESTED
    /**
     * notify status of current and next player
     *
     * @return the next player
     */
    public Player endTurnUpdateStatus() {

        Player current = getCurrentPlayer();

        String next = getNextPlayer(current.getNickname());

        current.endTurnCurrent();

        getPlayerByNickname(next).startTurn();

        return getPlayerByNickname(next);
    }

    // TESTED
    /**
     * @param current current player
     * @return the nickname of the next player
     */
    public String getNextPlayer(String current) {

        int currentIndex = playerNames.indexOf(current);
        Player nextPlayer;

        if (currentIndex == players.size() - 1)
            nextPlayer = players.get(0);
        else
            nextPlayer = players.get(currentIndex + 1);

        return nextPlayer.getNickname();

    }

    // TESTED
    /**
     *
     * @return the current player
     */
    private Player getCurrentPlayer() {
        for (Player i : players) {
            if (i.isCurrentPlayer())
                return i;
        }
        throw new RuntimeException("didn't find the current player!");
    }

    // TESTED
    /**
     * tells if the spot is valid, this method is cretaed in gameMap.java, but we
     * should know if spot is valid even in Game.java
     *
     * @param x spot x
     * @param y spot y
     * @return true if spot is valid
     */
    public boolean validSpot(int x, int y) {
        return gameMap.validSpot(x, y);
    }

    // ONLY USED IN TESTS
    /**
     * this method returns a spot by using index
     *
     * @param x the x of the spot
     * @param y the y of the spot
     * @return the spot corresponding to that index
     */
    public Spot getSpotByIndex(int x, int y) {
        return gameMap.getSpotByIndex(x, y);
    }

    /**
     * based on the playerStatus, generate the possible actions of the player
     *
     * @param nickname the nickname of the player
     * @return possible player actions
     */
    public ArrayList<String> generatePossibleActions(String nickname) {

        Player player = getPlayerByNickname(nickname);
        ArrayList<String> actions = new ArrayList<>();

        if (player.isDead()) {
            actions.add("Respawn");
            return actions;
        }

        if (isOnSpawnSpot(player))
            actions.add("PickWeapon");

        if (player.playerStatus.nActionsDone < player.playerStatus.nActions) {
            actions.add("Move");
            actions.add("MoveAndGrab");
            actions.add("Attack");
        }

        if (player.hasTurnPowerUp()) {
            actions.add("UseTurnPowerUp");
        }

        for (Weapon w : player.getWeaponList()) {
            if (!w.isLoaded()) {
                actions.add("Reload");
                break;
            }
        }

        actions.add("EndTurn");

        return actions;
    }

    // TESTED
    /**
     * tells if player is on a spawnspot
     *
     * @param player the player to check
     * @return true if player is on a spawnspot
     */
    private boolean isOnSpawnSpot(Player player) {

        int x = player.getxPosition();
        int y = player.getyPosition();

        return gameMap.isSpawnSpot(x, y);

    }

    // TESTED
    /**
     * this method creates a snapshot for the client, the first string is what all
     * the players see: kst, gameMap,playernames. The second string represent the
     * players object the return type of the method is an array, if you want the
     * json you need to stick the two String
     *
     * @return the client snapshot
     */
    public String clientSnapshot() {
        Gson gson = new Gson();

        // in clientSnaphot i will save all the infos: clientSnap[0] contains info
        // equals for all players and clientSnap[1] contains info that are "protected"
        String clientSnapshot;

        // clientSnapshot[0]
        String jsonPlayerNames = gson.toJson(playerNames.toArray());
        String jsonKST = gson.toJson(kst);
        String jsonGameMap = gameMapSnapshot(gson);

        clientSnapshot = "{ \"playerNames\":" + jsonPlayerNames + "," + "\"kst\":" + jsonKST + "," + "\"gameMap\":"
                + jsonGameMap + "," + "PORCODUE";

        clientSnapshot += "\"players\":" + gson.toJson(players.toArray()) + "}";

        return clientSnapshot;
    }

    // TESTED
    /**
     * this method creates a snapshot of the game in a string representing a json
     * file
     *
     * @return the string containing json game
     */
    public String modelSnapshot() {
        Gson gson = new Gson();

        // saving player nicknames
        String jsonPlayerNames = gson.toJson(playerNames.toArray());

        // saving players
        String jsonPlayers = gson.toJson(players.toArray());

        // saving kst
        String jsonKST = gson.toJson(kst);

        // saving powerUpDeck
        String jsonPowerUpDeck = gson.toJson(powerupDeck);

        // saving weaponDeck
        String jsonWeaponDeck = gson.toJson(weaponDeck);

        //saving ammoCardDeck
        String jsonAmmoCardDeck = gson.toJson(ammoCardDeck);

        // saving GameMap
        String jsonGameMap = gameMapSnapshot(gson);

        // create a json that stores all the information of the game in a string with
        // json format
        String modelSnapshot = "{ \"players\":" + jsonPlayers + "," + "\"playerNames\":" + jsonPlayerNames + ","
                + "\"powerUpDeck\":" + jsonPowerUpDeck + "," + "\"weaponDeck\":" + jsonWeaponDeck + "," + "\"ammoCardDeck\":" + jsonAmmoCardDeck +  "," + "\"kst\":"
                + jsonKST + "," + "\"gameMap\":" + jsonGameMap + "}";

        return modelSnapshot;
    }

    // TESTED
    /**
     * this method create the snapshot of the gameMap and returns it to above method
     *
     * @param gson the gson of the above method
     * @return the gameMap snapshot
     */
    private String gameMapSnapshot(Gson gson) {
        String jsonGameMap = "{";
        String jsonSpot;

        for (int i = 0; i < 3; i++) {

            jsonGameMap += "\"row" + i + "\": {";

            for (int j = 0; j < 4; j++) {

                jsonGameMap += "\"col" + j + "\": {";

                if (gameMap.validSpot(i, j))
                    jsonSpot = "\"spot\" :" + gson.toJson(gameMap.getSpotByIndex(i, j));
                else
                    jsonSpot = "\"spot\" :" + "{}";

                jsonGameMap += jsonSpot;

                if (j < 3)
                    jsonGameMap += "},";
                else
                    jsonGameMap += "}";
            }
            if (i < 2)
                jsonGameMap += "},";
            else
                jsonGameMap += "}";
        }

        jsonGameMap += "}";

        return jsonGameMap;

    }

    /**
     *
     * @param nickname the nickname of the player
     * @return all the names of the weapons loaded the player has
     */
    public ArrayList<String> getLoadedWeapons(String nickname) {

        Player player = getPlayerByNickname(nickname);

        // These are the weapons the player has
        ArrayList<Weapon> weapons = player.getWeaponList();

        // These are the weapons loaded, these are the weapons the player can choose to
        // shoot
        ArrayList<String> weaponsLoaded = new ArrayList<>();

        for (Weapon w : weapons) {

            if (w.isLoaded())
                weaponsLoaded.add(w.getWeaponName());
        }

        return weaponsLoaded;
    }

    /**
     * generate all the possible weapons that player can pick in a spawnSpot
     *
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
     *
     * @param x      the x of the spawnspot
     * @param y      the y of the spawnspot
     * @param weapon the weapon to add
     */
    public void addWeaponToSpawnSpot(int x, int y, Weapon weapon) {

        gameMap.addWeaponToSpawn(x, y, weapon);

    }

    /**
     * Creates a new weapon based on the name
     *
     * @param weaponName the name of the weapon
     * @return the weaponName object
     */
    public Weapon getWeaponByName(String weaponName) {
        return Weapon.getWeapon(weaponName);
    }

    /**
     * Creates a new powerUp based on the name
     *
     * @param powerUpName the name of the weapon
     * @return the powerUpName object
     */
    public PowerUp getPowerUpByName(String powerUpName) {
        return PowerUp.getPowerUp(powerUpName);
    }

    /**
     * tells if p1 on spot(x1,y1) can see p2 on (x2,y2)
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return true if p1 can see p2
     */
    public boolean p1SeeP2(int x1, int y1, int x2, int y2) {
        return gameMap.see(x1, y1, x2, y2);
    }

    public void disconnectPlayer(String nickname) {

        Player p = getPlayerByNickname(nickname);

        players.remove(p);
        disconnectedPlayers.add(p);

        gameMap.removePlayer(nickname);

    }

    public void reconnectPlayer(String nickname) {

        Player reconnectedPlayer = null;

        for (Player p : disconnectedPlayers)
            if (p.getNickname().equals(nickname)) {
                reconnectedPlayer = p;
                disconnectedPlayers.remove(p);
                break;
            }

        if (reconnectedPlayer == null)
            throw new RuntimeException("This player was not found in the disconnectedPlayer array");

        players.add(reconnectedPlayer);
        reconnectedPlayer.setIsDead(true);

    }
}
