package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Effect;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.server.GamesHandler;
import it.polimi.ingsw.view.server.ServerProxy;
import it.polimi.ingsw.view.server.VirtualView;

import java.util.ArrayList;
import java.util.List;

/*
    THE CONTROLLER:
        - Receives input the virtualView
        - Processes requests
        - Gets data from the model
        - Passes data to the virtualView
 */

public class Controller implements Observer, AnswerEventHandler {

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

    public String lastPlayer;

    public boolean isFrenzyTurn;

    /**
     * Constructor
     * @param gameModel
     * @param virtualView
     */
    public Controller(Game gameModel, VirtualView virtualView){
        this.gameModel = gameModel;
        this.virtualView = virtualView;
        this.isFrenzyTurn = false;
        this.lastPlayer = "";
    }

    /**
     * This method:
     *      - notify the current player status
     *      - checks all deaths
     *      - refills all spots
     */
    private void endTurn(){

        Player playerThatHasJustEnded = gameModel.getCurrentPlayer();

        //if we are in frenzy turn and last player has ended the turn
        if(isFrenzyTurn && playerThatHasJustEnded.getNickname().equals(lastPlayer)){

            int maxPoints = 0;
            String winner = "";

            //first i calculate the points
            for(String current: gameModel.getAllPlayers()){

                Player player = gameModel.getPlayerByNickname(current);

                gameModel.giveFrenzyBoardPoints(player);
            }

            gameModel.giveKSTPoints();

            //then i check who won
            for(String current: gameModel.getAllPlayers()){
                Player player = gameModel.getPlayerByNickname(current);

                if(player.getPoints() > maxPoints){
                    maxPoints = player.getPoints();
                    winner = player.getNickname();
                }
            }

            virtualView.sendAllQuestionEvent(new EndGameQuestion(winner, maxPoints));
            virtualView.sendAllQuestionEvent(new DisconnectedQuestion());

            virtualView.disconnectAllPlayers();
            GamesHandler.deleteController(this, gameModel.getGameId());
        }


        Player nextPlayer = gameModel.endTurnUpdateStatus();

        gameModel.checkDeaths();

        gameModel.refillAllAmmoSpots();

        gameModel.refillAllSpawnSpots();


        if(gameModel.kstIsFull() && !isFrenzyTurn){

            lastPlayer = playerThatHasJustEnded.getNickname();

            isFrenzyTurn = true;

            virtualView.sendAllQuestionEvent(
                    new TextMessage("***********************FRENZY TURN IS STARTED*************************")
            );
        }

        ArrayList<String> possibleActions = gameModel.generatePossibleActions(nextPlayer.getNickname());
        sendQuestionEvent(nextPlayer.getNickname(), new ActionQuestion(possibleActions));

    }

    /**
     * Reinserts the player in the game
     * @param nickname the nickname of the player I want to put back into the game
     */
    public void reinsert(String nickname, ServerProxy proxy) {

        virtualView.sendAllQuestionEvent(
                new TextMessage(nickname + " RECONNECTED")
        );

        gameModel.reconnectPlayer(nickname);

        virtualView.reconnectPlayer(nickname, proxy);

    }

    /**
     * Starts the game for all players
     */
    public void startGame(MapName votedMap, int votedSkulls) {

        ArrayList<String> playerNames = gameModel.getPlayerNames();

        Player firstPlayer = gameModel.getPlayerByNickname(playerNames.get(0));

        ArrayList<PlayerColor> playerColors = PlayerColor.getRandomArray(gameModel.getPlayerNames().size());

        virtualView.sendAllQuestionEvent(
                new GameStartedQuestion(playerNames, playerColors, firstPlayer.getNickname(), votedMap, votedSkulls)
        );

        firstPlayer.playerStatus.isActive = true;

        String newMessage = firstPlayer.getNickname() + ": It is your turn";

        sendMessage(firstPlayer.getNickname(), newMessage);

        ArrayList<String> possibleActions = gameModel.generatePossibleActions(firstPlayer.getNickname());
        sendQuestionEvent(firstPlayer.getNickname(), new ActionQuestion(possibleActions));

    }

    public void restartGame(MapName mapName) {

        ArrayList<String> playerNames = gameModel.getPlayerNames();

        ArrayList<PlayerColor> playerColors = PlayerColor.getRandomArray(playerNames.size());

        int skulls = gameModel.getKSTsize();

        Player firstPlayer = gameModel.getPlayerByNickname(playerNames.get(0));

        if( ! firstPlayer.playerStatus.isActive )
            throw new RuntimeException( firstPlayer.getNickname() + " is not active");


        for(String nickname : playerNames)
            sendQuestionEvent(nickname, new GameRestartedQuestion(playerNames, playerColors, skulls, mapName));

        ArrayList<String> possibleActions = gameModel.generatePossibleActions(firstPlayer.getNickname());
        sendQuestionEvent(firstPlayer.getNickname(), new ActionQuestion(possibleActions));


    }

    private void sendMessage(String nickname, String message){

        virtualView.sendQuestionEvent(
                nickname,
                new TextMessage(message)
        );

    }

    @Override
    public void notifyObserver(Object arg){

        if( ! (arg instanceof AnswerEvent))
            throw new RuntimeException("This must be an AnswerEvent object");

        AnswerEvent answerEvent = (AnswerEvent) arg;

        answerEvent.acceptEventHandler(this);

    }

    private void sendQuestionEvent(String nickname, QuestionEvent event){

        virtualView.sendQuestionEvent(nickname, event);

    }

    //NETWORK EVENTS ************************************************************************************

    @Override
    public void receiveEvent(AnswerEvent answerEvent) {
        new Thread( () -> answerEvent.acceptEventHandler(this)).start();
    }

    @Override
    public void handleEvent(NewConnectionAnswer event) {
        throw new RuntimeException("Connection messages should not arrive to the controller");
    }

    @Override
    public synchronized void handleEvent(DisconnectedAnswer event) {

        System.out.println("Disconnecting " +  event.nickname + " from CONTROLLER");

        if(gameModel.getPlayerNames().size() > 3){

            Player p = gameModel.getPlayerByNickname(event.nickname);

            p.setIsDead(true);

            if(p.playerStatus.isActive)
                endTurn();

            gameModel.disconnectPlayer(event.nickname);

            virtualView.sendAllQuestionEvent(
                    new PlayerDisconnectedQuestion(event.nickname)
            );

        }
        else{

            System.out.println("Closing game because " + event.nickname + " disconnected");

            int gameId = gameModel.getGameId();

            gameModel.pause();

            GamesHandler.pauseGame(gameModel.getAllPlayers(), gameId);

            close();

        }


    }

    private void close() {

        virtualView.sendAllQuestionEvent(new DisconnectedQuestion());

        virtualView.disconnectAllPlayers();

    }
    //*************************************************************************************************************

    @Override
    public void handleEvent(ActionAttackAnswer event) {

        ArrayList<String> weaponsLoaded = gameModel.getLoadedWeapons(event.nickname);

        if(weaponsLoaded.isEmpty()){
            sendMessage(event.nickname, "You have no loaded weapon");

            //Regenerates all the possible actions this player can make
            ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
            sendQuestionEvent(event.nickname,
                    new ActionQuestion(possibleActions)
            );

            return;
        }

        Player currentPlayer = gameModel.getPlayerByNickname(event.nickname);

        if(currentPlayer.getnMovesBeforeShooting() > 0){
            boolean[][] allowedSpots = gameModel.wherePlayerCanMove(event.nickname, currentPlayer.getnMoves());

            sendQuestionEvent(event.nickname, new SendCanMoveBeforeShootingQuestion(allowedSpots, weaponsLoaded));
            return;
        }

        sendQuestionEvent(event.nickname, new ChooseWeaponToAttackQuestion(weaponsLoaded));

    }

    @Override
    public void handleEvent(ChooseWeaponToAttackAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        Weapon weaponToUse = player.getWeaponByName(event.chosenWeapon);

        ArrayList<Integer[]> possibleOrders = weaponToUse.getOrder();

        sendQuestionEvent(event.nickname, new AskOrderAndDefenderQuestion(event.chosenWeapon, possibleOrders));


    }

    /**
     * this intermediate method is needed to know if controller has to choose between shootWith/WithoutMovement
     * @param event
     */
    @Override
    public void handleEvent(AskOrderAndDefenderAnswer event){
        Player player = gameModel.getPlayerByNickname(event.nickname);

        Weapon weaponToUse = player.getWeaponByName(event.chosenWeapon);

        boolean shootWithMovement = false;

        shootWithMovement = checkFreeEffectOfMoving(weaponToUse);

        //uso una copia di defenders così potrò eliminare quello che voglio
        ArrayList<String> defendersTemp = new ArrayList<>(event.defenders);
        //ultimo indice dell'effetto che andrò ad usare per sparare
        int i = 0;

        //scorro gli effetti,per ogni persona che devo attaccare rimuovo un defender, così so dove devo fermarmi per far pagare l'utente
        if(!defendersTemp.isEmpty()) {

            for (; i < event.order.length; i++) {

                int effectIndex = event.order[i];

                Effect effect = weaponToUse.getEffects().get(effectIndex);

                //se ho un effetto di movimento devo chiamare shootWithMovement()
                if (gameModel.typeOfEffect(effect) == 0)
                    shootWithMovement = true;

                removeDefendersThatPlayerCanAttackWithThisEffect(effect, defendersTemp);

                if (defendersTemp.isEmpty())
                    break;

            }
        }

        boolean playerHasTargetingScope = checkIfPlayerHasTargetingScope(player);

        //se il giocatore ha un mirino devo chiedergli se vuole usarlo su qualche defender
        if(playerHasTargetingScope){
            sendQuestionEvent(event.nickname, new ChooseIfUseATargetingScopeQuestion(event.nickname, event.chosenWeapon, event.order, shootWithMovement, i + 1, event.defenders));
        }
        else {
            sendQuestionEvent(event.nickname, new ChooseHowToShootQuestion(event.nickname, event.chosenWeapon, event.order, shootWithMovement, i + 1, event.defenders, null));
        }
    }

    public void handleEvent(ChooseIfUseATargetingScopeAnswer event){
        sendQuestionEvent(event.nickname, new ChooseHowToShootQuestion(event.nickname, event.chosenWeapon, event.order, event.shootWithMovement, event.indexOfLastEffect, event.defenders, event.defenderToApplyTargetingScope));
    }

    /**
     * tells how many defender this effect need
     * @param effect the effect to consider
     * @return the number of player that player have to put in defenders
     */
    private void removeDefendersThatPlayerCanAttackWithThisEffect(Effect effect, ArrayList<String> defenders) {
        int nPlayersAttackable = effect.getnPlayersAttackable();
        int nPlayersMarkable = effect.getnPlayersMarkable();

        for(int i = 0; i < nPlayersAttackable; i++) {
            if (!defenders.isEmpty())
                defenders.remove(0);
        }

        if( !(nPlayersAttackable == 1 && nPlayersMarkable == 1))
        for(int i = 0; i < nPlayersMarkable; i++) {
            if (!defenders.isEmpty())
                defenders.remove(0);
        }
    }

    @Override
    public void handleEvent(ChooseHowToShootAnswer event) {

        //eventuale costo che ci sarà da pagare
        ArrayList<Color> cost = new ArrayList<>();

        Weapon weapon = gameModel.getWeaponByName(event.weapon);


        for (int i = 0; i < event.indexOfLastEffectUsed; i++) {  //scorro gli effetti nell'ordine scelto per vedere se c'è un costo aggiuntivc da pagare e per capire se chiamare ShootWith or ShootWithout movement
            int numberOfEffect = event.chosenOrder[i];

            Effect effect = weapon.getEffects().get(numberOfEffect);

            cost.addAll(effect.getCost());

        }

        Player offenderPlayer = gameModel.getPlayerByNickname(event.nickname);

        //se l'utente ha deciso di usare il targeting scope deve pagare un Color.Any
        if(event.defenderToApplyTargeting != null){

            Color targetingScopeColor = null;

            for(PowerUp p : offenderPlayer.getPowerUpList()){
                if(p.getPowerUpName().equals("TargetingScope")){
                    targetingScopeColor = p.getColor();
                }
            }

            offenderPlayer.removePowerUpByNameAndColor("TargetingScope", targetingScopeColor);
            cost.add(Color.ANY);
        }

        //Se c'è un costo da pagare, devo chiedere all'utente come vuole pagarlo
        if(offenderPlayer.canPay(cost)){
            sendQuestionEvent(event.nickname, new ChooseHowToPayForAttackingQuestion(event, cost));
        }else{
            sendMessage(event.nickname, "You can't pay for all these effects, move and grab something before.\n" );
            sendQuestionEvent(event.nickname, new ActionQuestion(gameModel.generatePossibleActions(event.nickname)));
        }

    }

    @Override
    public void handleEvent(ChooseHowToPayForAttackingAnswer event){
        Player offender = gameModel.getPlayerByNickname(event.chooseHowToShootAnswer.nickname);

        List<String> chosenPayment = event.paymentChosen;

        if(offender.canPayWithString(event.paymentChosen)) {
            removeAmmoFromPlayer(offender, chosenPayment);

            boolean playerHasFinallyShoot = false;

            if (event.chooseHowToShootAnswer.movers != null) {
                playerHasFinallyShoot = gameModel.shootWithMovement(event.chooseHowToShootAnswer.nickname, event.chooseHowToShootAnswer.defenders, offender.getWeaponByName(event.chooseHowToShootAnswer.weapon), event.chooseHowToShootAnswer.chosenOrder, event.chooseHowToShootAnswer.xCoords, event.chooseHowToShootAnswer.yCoords, event.chooseHowToShootAnswer.movers, event.chooseHowToShootAnswer.defenderToApplyTargeting);
            } else {
                playerHasFinallyShoot = gameModel.shootWithoutMovement(event.chooseHowToShootAnswer.nickname, event.chooseHowToShootAnswer.defenders, offender.getWeaponByName(event.chooseHowToShootAnswer.weapon), event.chooseHowToShootAnswer.chosenOrder, event.chooseHowToShootAnswer.defenderToApplyTargeting);
            }


            if (playerHasFinallyShoot) {
                sendMessage(event.chooseHowToShootAnswer.nickname, "SHOT COMPLETED SUCCESSFULLY!");
                offender.playerStatus.nActionsDone++;

            } else {
                sendMessage(event.chooseHowToShootAnswer.nickname, "YOU DID NOT SHOOT!");
                offender.playerStatus.nActionsDone++;
            }


        }
        else{
            sendMessage(event.chooseHowToShootAnswer.nickname, "YOU CAN'T PAY FOR ALL THESE EFFECTS, PLEASE CHECK THE WEAPON RULES AND MAYBE INSERT LESS DEFENDER");
            sendQuestionEvent(event.chooseHowToShootAnswer.nickname, new ActionQuestion(gameModel.generatePossibleActions(event.chooseHowToShootAnswer.nickname)));
            return;
        }

        //alla fine posso rigenerare le azioni all'utente
        sendQuestionEvent(event.chooseHowToShootAnswer.nickname, new ActionQuestion(gameModel.generatePossibleActions(event.chooseHowToShootAnswer.nickname)));
    }

    @Override
    public void handleEvent(ChooseHowToPayToSwitchWeaponsAnswer event) {

        Player playerObject = gameModel.getPlayerByNickname(event.nickname);

        removeAmmoFromPlayer(playerObject, event.paymentChoice);

        //I pick the weapon and give it to the player
        gameModel.switchWeapons(event.nickname, event.weaponToPick, event.weaponToDiscard);

        ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
        sendQuestionEvent(event.nickname,
                new ActionQuestion(possibleActions)
        );

    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        removeAmmoFromPlayer(player, event.chosenPayment);

        player.reloadWeaponByName(event.weapon);

        ArrayList<String> possibleActions = gameModel.generateActionsAfterReloading(event.nickname);

        sendQuestionEvent(event.nickname,
                new ActionAfterReloadingQuestion(possibleActions)
        );

    }

    @Override
    public void handleEvent(Ping event) {

        //System.err.println("Received new ping from " + event.nickname);

        virtualView.ping(event.nickname);
    }

    @Override
    public void handleEvent(UseGrenadeAnswer event) {

        Player offender = gameModel.getPlayerByNickname(event.offender);

        if( ! offender.playerStatus.isActive){
            sendMessage(event.nickname, "You took too long to decide, it's no longer the offender's turn");
            return;
        }

        gameModel.giveMarks(event.nickname, event.offender, 1);

    }

    @Override
    public void handleEvent(ActionEndTurnAnswer event) {

        //Ends the turn and sends a question to the next player
        endTurn();

        String message = "Your turn is over";
        TextMessage textMessage = new TextMessage(message);

        sendQuestionEvent(event.nickname, textMessage);

    }

    @Override
    public void handleEvent(ActionMoveAndGrabAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        ArrayList<String> spots = new ArrayList<>();
        boolean[][] allowedSpots = gameModel.wherePlayerCanMoveAndGrab(event.nickname, player.getnMovesBeforeGrabbing());

        sendQuestionEvent(event.nickname, new WhereToMoveAndGrabQuestion(allowedSpots));

    }

    @Override
    public void handleEvent(ActionMoveAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        boolean[][] allowedSpots = gameModel.wherePlayerCanMove(event.nickname, player.getnMoves());

        sendQuestionEvent(event.nickname, new WhereToMoveQuestion(allowedSpots));

    }

    @Override
    public void handleEvent(ActionPickWeaponAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        //I read all the weapons on the spawn spot where the player is
        ArrayList<String> weaponsOnTheSpawnSpot = gameModel.weaponsToPick(event.nickname);

        //I create an array to fill with only the weapon he can pay for
        ArrayList<String> weaponsToPick = new ArrayList<>();

        //I read the weapon in the player's hand
        ArrayList<Weapon> playerWeapons = player.getWeaponList();

        //For each weapon on the spot, I check if the player is able to pay for it
        for(String weapon : weaponsOnTheSpawnSpot){

            Weapon tempWeapon = gameModel.getWeaponByName(weapon);  //From a string to a weapon object

            //I read the cost of the weapon and remove the first cost (don't have to pay for it to pick)
            ArrayList<Color> tempWeaponCost = tempWeapon.getCost();
            tempWeaponCost.remove(0);

            //If the player can pay for it, good
            if(player.canPay(tempWeaponCost))
                weaponsToPick.add(weapon);

        }


        if(weaponsToPick.isEmpty()){
            sendMessage(event.nickname, "You don't have enough ammo for any of these weapons");

            ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
            sendQuestionEvent(event.nickname,
                    new ActionQuestion(possibleActions)
            );

            return;
        }



        //If the player has to choose which weapon to discard
        if(playerWeapons.size() > 2){

            ArrayList<String> weaponNames = new ArrayList<>();

            for(Weapon w : playerWeapons)
                weaponNames.add(w.getWeaponName());

            sendQuestionEvent(event.nickname, new ChooseWeaponToSwitchQuestion(weaponsToPick, weaponNames));
        }
        else {
            sendQuestionEvent(event.nickname, new ChooseWeaponToPickQuestion(weaponsToPick));
        }

    }

    @Override
    public void handleEvent(ActionReloadAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        ArrayList<String> weapons = new ArrayList<>();

        ArrayList<Weapon> rechargeableWeapons =  gameModel.checkRechargeableWeapons(player.getNickname());

        for(Weapon w : rechargeableWeapons)
            weapons.add(w.getWeaponName());

        if(weapons.isEmpty()){
            sendMessage(event.nickname, "You don't have enough ammo to reload any weapon");

            ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
            sendQuestionEvent(event.nickname,
                    new ActionQuestion(possibleActions)
            );

            return;
        }

        sendQuestionEvent(player.getNickname(), new ChooseWeaponToReloadQuestion(weapons));

    }

    @Override
    public void handleEvent(ActionRespawnAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        //Draws a weapon and gives it to the player
        gameModel.givePowerUp(player.getNickname());

        //If it's this player's first turn, I give him another powerup
        if(player.playerStatus.isFirstTurn){
            gameModel.givePowerUp(player.getNickname());
        }

        //Creates the list of powerups to discard
        ArrayList<String> powerUpsToRespawn = new ArrayList<>();
        ArrayList<Color> colors = new ArrayList<>();

        for(PowerUp p : player.getPowerUpList()) {
            powerUpsToRespawn.add(p.getPowerUpName());
            colors.add(p.getColor());
        }

        sendQuestionEvent(event.nickname,  new ChoosePowerUpToRespawnQuestion(powerUpsToRespawn, colors));

    }

    @Override
    public void handleEvent(ActionUseTurnPowerUpAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        ArrayList<String> powerUpToUse = new ArrayList<>();
        ArrayList<Color> colors = new ArrayList<>();

        for (PowerUp p : player.getPowerUpList())
            if(p.isTurnPowerup()) {
                powerUpToUse.add(p.getPowerUpName());
                colors.add(p.getColor());
            }

        sendQuestionEvent(event.nickname, new ChoosePowerUpToUseQuestion(powerUpToUse, colors));

    }

    @Override
    public void handleEvent(ChooseHowToUseTurnPowerUpAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        String powerUpName = event.powerUpToUse;

        Color powerUpColor = event.powerUpColor;

        PowerUp playerPowerUp = player.getPlayerPowerUpByNameAndColor(powerUpName, powerUpColor);

        String offenderName = event.nickname;

        int x = event.x;

        int y = event.y;

        if(gameModel.validSpot(x,y)) {
            try {
                gameModel.removePowerUpByNameAndColor(player, playerPowerUp.getPowerUpName(), playerPowerUp.getColor());
                gameModel.useMovementPowerUp(offenderName, event.mover, playerPowerUp.getEffect(), x, y);
            } catch (InvalidChoiceException e) {
                sendMessage(event.nickname, "Can't use this powerUp like this, wasted it");
            }
        }else{
            gameModel.removePowerUpByNameAndColor(player, playerPowerUp.getPowerUpName(), playerPowerUp.getColor());
            sendMessage(event.nickname, "not valid spot, you have wasted your powerUp");
        }


        ArrayList<String> possibleActions = gameModel.generatePossibleActions(player.getNickname());
        sendQuestionEvent(player.getNickname(), new ActionQuestion(possibleActions));

    }


    @Override
    public void handleEvent(ChooseHowToPayToPickWeaponAnswer event){

        List<String> chosenPayment = event.chosenPayment;
        Player playerObject = gameModel.getPlayerByNickname(event.nickname);

       removeAmmoFromPlayer(playerObject, chosenPayment);

        //I pick the weapon and give it to the player
        gameModel.pickWeaponFromSpawn(playerObject.getNickname(), event.weaponToPick);

        ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
        sendQuestionEvent(event.nickname,
            new ActionQuestion(possibleActions)
        );

    }

    /**
     * this method make the player pay
     * @param playerObject
     * @param chosenPayment
     */
    private void removeAmmoFromPlayer(Player playerObject, List<String> chosenPayment) {
        //First, I make the player pay
        for(String s : chosenPayment){
            String SPLITTER = ":";

            //This means I'm paying with a power up
            if(s.contains(SPLITTER)){
                String chosenPowerUpToPay = s.split(SPLITTER)[0];
                gameModel.removePowerUpByNameAndColor(playerObject, chosenPowerUpToPay, Color.valueOf(s.split(SPLITTER)[1]));
            }
            else {
                Color chosenColorToPay = Color.valueOf(s);
                playerObject.removeAmmo(chosenColorToPay);
            }

        }
    }

    @Override
    public void handleEvent(ChooseIfToUseAsyncPowerUpAnswer event) {

    }

    @Override
    public void handleEvent(ChoosePowerUpToRespawnAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        String powerUpName = event.powerUpToRespawn;
        Color color = event.color;

        int powerUpIndex = -1;

        ArrayList<PowerUp> powerUpList = player.getPowerUpList();
        for(int i = 0; i < powerUpList.size(); i++){

            PowerUp tempPowerUp = powerUpList.get(i);

            if(tempPowerUp.getColor().equals(color) && tempPowerUp.getPowerUpName().equals(powerUpName)) {
                powerUpIndex = i;
                break;
            }
            else {
                if(i == powerUpList.size() - 1)
                    throw new RuntimeException("The PowerUp was not in the powerUpList");
            }
        }

        gameModel.respawn(player.getNickname(), powerUpIndex);

        ArrayList<String> possibleActions = gameModel.generatePossibleActions(player.getNickname());
        sendQuestionEvent(player.getNickname(), new ActionQuestion(possibleActions));

    }

    @Override
    public void handleEvent(ChoosePowerUpToUseAnswer event) {

        sendQuestionEvent(event.nickname, new ChooseHowToUseTurnPowerUpQuestion(event.powerUpToUse, event.color));

    }

    @Override
    public void handleEvent(ChooseWeaponToPickAnswer event) {

        Weapon weaponToPick = gameModel.getWeaponByName(event.weaponToPick);

        ArrayList<Color> weaponCost = weaponToPick.getCost();
        weaponCost.remove(0);

        if(weaponCost.isEmpty()){

            gameModel.pickWeaponFromSpawn(event.nickname, event.weaponToPick);

            ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
            sendQuestionEvent(event.nickname,
                    new ActionQuestion(possibleActions)
            );

            return;
        }

        sendQuestionEvent(event.nickname,
                new ChooseHowToPayToPickWeaponQuestion(event.weaponToPick, weaponCost)
        );

    }

    @Override
    public void handleEvent(ChooseWeaponToReloadAnswer event) {

        ArrayList<Color> cost = gameModel.getWeaponByName(event.weaponToReload).getCost();

        sendQuestionEvent(event.nickname,
                new ChooseHowToPayToReloadQuestion(event.weaponToReload, cost)
        );

    }

    @Override
    public void handleEvent(ChooseWeaponToSwitchAnswer event) {


        Weapon weaponToPick = gameModel.getWeaponByName(event.weaponToPick);

        ArrayList<Color> weaponCost = weaponToPick.getCost();
        weaponCost.remove(0);

        if(weaponCost.isEmpty()){

            gameModel.switchWeapons(event.nickname, event.weaponToPick, event.weaponToDiscard);

            ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
            sendQuestionEvent(event.nickname,
                    new ActionQuestion(possibleActions)
            );

            return;
        }

        sendQuestionEvent(event.nickname,
                new ChooseHowToPayToSwitchWeaponsQuestion(event.weaponToPick, weaponCost, event.weaponToDiscard)
        );


        //FIXME need to pay first!
        //gameModel.switchWeapons(event.nickname, event.weaponToPick, event.weaponToDiscard);


    }

    @Override
    public void handleEvent(WhereToMoveAndGrabAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        //Reads what spot the player decided to move to
        int xCoord = event.xCoord;
        int yCoord = event.yCoord;


        gameModel.moveAndGrab(player.getNickname(), xCoord, yCoord, -1);

        player.playerStatus.nActionsDone += 1;


        List<String> possibleActions = gameModel.generatePossibleActions(player.getNickname());
        sendQuestionEvent(player.getNickname(), new ActionQuestion(possibleActions));

    }

    @Override
    public void handleEvent(WhereToMoveAnswer event) {
        Player player = gameModel.getPlayerByNickname(event.nickname);

        int xCoord = event.xCoord;
        int yCoord = event.yCoord;

        gameModel.movePlayer(player.getNickname(), xCoord, yCoord);

        player.playerStatus.nActionsDone += 1;

        List<String> possibleActions = gameModel.generatePossibleActions(player.getNickname());

        sendQuestionEvent(player.getNickname(), new ActionQuestion(possibleActions));

    }

    @Override
    public void handleEvent(RefreshPossibleActionsAnswer event){
        List<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
        sendQuestionEvent(event.nickname , new ActionQuestion(possibleActions));
    }

    @Override
    public void handleEvent(RefreshPossibleActionsAfterReloadingAnswer event){
        Player player = gameModel.getPlayerByNickname(event.nickname);
        List<String> possibleActions;

        possibleActions = gameModel.generateActionsAfterReloading(event.nickname);

        sendQuestionEvent(event.nickname , new ActionQuestion(possibleActions));
    }

    private boolean checkIfPlayerHasTargetingScope(Player player){

        for(PowerUp p: player.getPowerUpList()){
            if(p.getPowerUpName().equals("TargetingScope")){
                return true;
            }
        }

        return false;
    }

    private boolean checkFreeEffectOfMoving(Weapon weaponToCheck){
        for(Effect effect: weaponToCheck.getEffects())
            if(effect.getnMoves() != 0)
                return true;

        return false;
    }

    public void handleEvent(SendCanMoveBeforeShootingAnswer event){
        Player player = gameModel.getPlayerByNickname(event.nickname);

        int xCoord = event.x;
        int yCoord = event.y;

        if(xCoord != -1 && yCoord != -1) {
            gameModel.movePlayer(player.getNickname(), xCoord, yCoord);
        }

        Player currentPlayer = gameModel.getCurrentPlayer();

        if(currentPlayer.canreloadBeforeShooting()){

            ArrayList<Weapon> rechargeableWeapons = gameModel.checkRechargeableWeapons(event.nickname);
            ArrayList<String> rechargeableWeaponsNames = new ArrayList<>();

            for(Weapon w : rechargeableWeapons){
                rechargeableWeaponsNames.add(w.getWeaponName());
            }

            if(!rechargeableWeapons.isEmpty()){
                sendQuestionEvent(event.nickname, new SendCanReloadBeforeShootingQuestion(rechargeableWeaponsNames, event.weaponsLoaded));
                return;
            }

        }


        sendQuestionEvent(player.getNickname(), new ChooseWeaponToAttackQuestion(event.weaponsLoaded));
    }

    public void handleEvent(SendCanReloadBeforeShootingAnswer event){
        if(event.weaponToReload != null) {
            ArrayList<Color> cost = gameModel.getWeaponByName(event.weaponToReload).getCost();

            sendQuestionEvent(event.nickname,
                    new ChooseHowToPayToReloadBeforeAttackQuestion(event.weaponToReload, cost, event.weaponsLoaded)
            );
        }else{
            sendQuestionEvent(event.nickname, new ChooseWeaponToAttackQuestion(event.weaponsLoaded));
        }
    }

    public void handleEvent(ChooseHowToPayToReloadBeforeAttackAnswer event){

        if(event.chosenPayment != null) {
            Player player = gameModel.getPlayerByNickname(event.nickname);

            removeAmmoFromPlayer(player, event.chosenPayment);

            player.reloadWeaponByName(event.weaponToReload);
        }

            sendQuestionEvent(event.nickname, new ChooseWeaponToAttackQuestion(event.weaponsLoaded));


    }

    public ArrayList<String> getPlayerNames() {

        return gameModel.getPlayerNames();

    }

    public ArrayList<PlayerColor> getPlayerColors() {

        return PlayerColor.getRandomArray(gameModel.getPlayerNames().size());

    }

    public int getSkulls() {
        return gameModel.getKSTsize();
    }


}
