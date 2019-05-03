package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.view.server.VirtualView;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

/*
    THE CONTROLLER:
        - Receives input the virtualView
        - Processes requests
        - Gets data from the model
        - Passes data to the virtualView
 */

public class Controller implements Observer {

    /**
     * The MODEL
     * This is here because the Controller is the only one allowed to modify the gameModel
     */
    Game gameModel;

    /**
     * The VIEW
     * This is here because the Controller might need to send messages to the clients through the Virtual View
     */
    public VirtualView virtualView;

    public Controller(Game gameModel, VirtualView virtualView){
        this.gameModel = gameModel;
        this.virtualView = virtualView;
    }

    /**
     * This method implements the void run() method from Runnable and is used to process the entire Game by pausing when waiting for data from the virtualView
     */
    public void runGame(){
        Log.LOGGER.info("Game starting");
        Log.LOGGER.warning("The map has been chosen by polling");
        Log.LOGGER.warning("The KST has been set up after polling");

        boolean endOfTurns = false;

        //Runs the first turn for all players
        for(String currentPlayer : gameModel.getPlayerNames()){
            Log.LOGGER.log(Level.INFO, "IT IS {0} TURN", currentPlayer);

            //Spawn player by choosing 2 powerups!
            //At this point the player should have nothing in his hands
            gameModel.givePowerUp(currentPlayer);
            gameModel.givePowerUp(currentPlayer);

            int chosenPowerupToDiscard = this.virtualView.askForIndexPowerupToDiscard(currentPlayer, this.gameModel.getPlayerPowerUps(currentPlayer));

            gameModel.respawn(currentPlayer, chosenPowerupToDiscard);

            /*con che arma vuoi sparare?
            aspetto la weapon
            scorro gli effetti, se ce n'è almeno uno di movimento,
            chiedo, oltre alla lista di player che l'utente vuole attaccare, chi vuole spostare (se lui o un avversario) e in che posizione
            shoot: shootWithMovement(String offenderName, ArrayList<String> defendersNames, Weapon weapon, int orderNumber, int xPosition, int yPosition, String playerWhoMoves)
            alrimenti se non ci sono effetti di movimento: sthootWithoutMovement(String offenderName, ArrayList<String> defendersNames, Weapon weapon, int orderNumber)*/
            runTurn(currentPlayer);
        }

        //Runs until the end of the regular game, will handle frenzy in the next loop
        while(!endOfTurns){

            for(String currentPlayer : gameModel.getPlayerNames()) {
                Log.LOGGER.log(Level.INFO, "IT IS {0} TURN", currentPlayer);

                runTurn(currentPlayer);

                checkDeaths();
                gameModel.refillAllAmmoSpots();
                gameModel.refillAllSpawnSpots();



                if (gameModel.noMoreSkullsOnKST())
                    endOfTurns = true;
            }
        }

        //Set up each player for frenzy round
        for(String currentPlayer : gameModel.getPlayerNames()){
            Log.LOGGER.log(Level.INFO, "SETTING UP {0} FOR FRENZY", currentPlayer);

            //gameModel.setupForFrenzy(currentPlayer);
        }

        //Run the frenzy round
        for(String currentPlayer : gameModel.getPlayerNames()){
            Log.LOGGER.log(Level.INFO, "IT IS {0} FRENZY TURN", currentPlayer);

            //TODO make sure the round continues from the last player
            runTurn(currentPlayer);
        }

        gameModel.giveKSTpoints();

        endGame();
    }

    /**
     * This runs the turn for the current player
     * @param currentPlayer
     */
    private void runTurn(String currentPlayer){

        //If player is dead, respawn
        if(gameModel.playerIsDead(currentPlayer)){
            gameModel.givePowerUp(currentPlayer);
            int chosenPowerUpToDiscard = virtualView.askForIndexPowerupToDiscard(currentPlayer, gameModel.getPlayerPowerUps(currentPlayer));

            gameModel.respawn(currentPlayer, chosenPowerUpToDiscard);
        }


        //Do you want to use a power up?
        useTurnPowerup(currentPlayer);

        //Move - Move&Grab - Attack
        doOneMove(currentPlayer);

        //Do you want to use a power up?
        useTurnPowerup(currentPlayer);

        //Move - Move&Grab - Attack
        doOneMove(currentPlayer);

        //Do you want to use a power up?
        useTurnPowerup(currentPlayer);

        //Do you want to reload?
        reloadWeapons(currentPlayer);

        //Checks if any player is dead
        checkDeaths();
        gameModel.refillAllAmmoSpots();
        gameModel.refillAllSpawnSpots();
    }

    /**
     * Asks the player if he wants to reload any of his weapons
     * @param player the player that choose only one of his weapons
     */
    private void reloadWeapons(String player) {
        //check all the weapons unloaded
        ArrayList<Weapon> weaponsThatCanBeReloaded = gameModel.checkRechargeableWeapons(player);
        int chosenWeapon = -1; //this will be the index of the weapons to load

        //ask the index of the weapon that the user wants to reload, the index refers to weaponThatCanBeReloaded, not the weapons in the player hand!!
        chosenWeapon = virtualView.askForIndexWeaponToReload(weaponsThatCanBeReloaded);

        if (chosenWeapon != -1) {

            //if i have others weapon to reload i continue to ask the player if he wants to reload the others
            while (chosenWeapon != -1) {

                //calculate the real index of the weapon in the hand of the player
                int realWeaponIndex = gameModel.getRealWeaponIndexOfTheUnloadedWeapon(player, weaponsThatCanBeReloaded.get(chosenWeapon));

                //reload the weapon in the player hand
                gameModel.reloadWeapon(player, realWeaponIndex);

                chosenWeapon = -1; //todo this is unneccesary only if virtualView can return -1

                //keep asking the player if he wants to reload another weapon and recalculate the weapons that player can reload
                weaponsThatCanBeReloaded = gameModel.checkRechargeableWeapons(player);
                chosenWeapon = virtualView.askForIndexWeaponToReload(weaponsThatCanBeReloaded);

            }
        }

    }

    /**
     * Asks the player to do one move by choosing between Move / Move&Grab / Attack and executes it
     * @param player the choosing player
     */
    private void doOneMove(String player) {
        int chosenMove = virtualView.askForIndexMoveToDo(player);

        gameModel.executeMove(chosenMove);
    }

    /**
     * Asks the player if he wants to use a "turn" power up and executes it
     * A "turn" power up is one that can be used at any time during a turn (i.e. Teleport)
     * If the player does not have any Turn power up the method does't ask him anything
     * @param player the choosing player
     */
    private void useTurnPowerup(String player) {

        //If the player does not have any power up that can be used now I don't ask him anything
        int chosenPowerupIndex = -1;
        if(gameModel.playerHasTurnPowerUp(player))
            chosenPowerupIndex = virtualView.askForIndexPowerupToUse( player, gameModel.getPlayerPowerUps(player));
        else
            return;

        //Keep asking the player if he wants to use a powerup until he says NO or he's out of usable power ups
        while (chosenPowerupIndex != -1) {

            //Retrieve the power up from the given index
            PowerUp powerUpToUse = gameModel.getPowerupByIndex(player, chosenPowerupIndex);

            //Finds all players I can attack with this power up
            ArrayList<String> attackablePlayers = gameModel.getAttackablePlayersPowerUp(player, powerUpToUse);

            //Asks the player which player he wants to use the power up on
            String chosenPlayerName = virtualView.askForPlayerNameToAttackPowerup(attackablePlayers);

            //Uses the powerup on the player
            gameModel.usePowerUp(player, chosenPlayerName, powerUpToUse);

            chosenPowerupIndex = -1;
            if(gameModel.playerHasTurnPowerUp(player))
                chosenPowerupIndex = virtualView.askForIndexPowerupToUse( player, gameModel.getPlayerPowerUps(player));

            //N.B. chosenPowerupIndex could still be -1 after virtualView.askForIndexPowerupToUse(...) if the player selects NONE as choice
        }

    }

    /**
     * This method checks if any player is dead and counts their boards assigning the right amount of point to each player.
     * It is usually executed at the end of each turn
     */
    public void checkDeaths() {
        gameModel.checkDeaths();
        //NB the model will assign points, not the controller
    }

    /**
     * This finds the winner, and tells everyone the game is over
     */
    private void endGame() {
        //TODO
    }

    /**
     * This method:
     *      - update the current player status
     *      - checks all deaths
     *      - refills
     */
    private void endTurn(){

        gameModel.endTurnUpdateStatus();

        gameModel.checkDeaths();

        gameModel.refillAllAmmoSpots();

        gameModel.refillAllSpawnSpots();
    }

    @Override
    public void update(Observable o, Object arg) {

        if( !(o instanceof VirtualView) )
            throw new RuntimeException("The observable is not a virtual view");

        if(arg != null && !(arg instanceof ArrayList))
            throw new RuntimeException("The arg should be an array list");

        Log.LOGGER.log(Level.INFO, "Controller class received a new message");

        String nickname = ((ArrayList<String>) arg).get(0);
        String message = ((ArrayList<String>) arg).get(1);

        System.out.println(nickname + " sent: " + message);
        virtualView.sendMessage(nickname, message);
    }

}
