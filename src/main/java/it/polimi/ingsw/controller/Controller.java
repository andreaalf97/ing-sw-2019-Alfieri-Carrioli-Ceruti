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
import it.polimi.ingsw.view.server.ServerProxy;
import it.polimi.ingsw.view.server.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * Constructor
     * @param gameModel
     * @param virtualView
     */
    public Controller(Game gameModel, VirtualView virtualView){
        this.gameModel = gameModel;
        this.virtualView = virtualView;
    }

    /**
     * This method:
     *      - notify the current player status
     *      - checks all deaths
     *      - refills all spots
     */
    private void endTurn(){

        Player nextPlayer = gameModel.endTurnUpdateStatus();

        gameModel.checkDeaths();

        gameModel.refillAllAmmoSpots();

        gameModel.refillAllSpawnSpots();

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

        /*
        for(String i : playerNames){

            Player tempPlayer = gameModel.getPlayerByNickname(i);

            tempPlayer.playerStatus.isActive = false;
            tempPlayer.playerStatus.waitingForAnswerToThisQuestion = null;
            tempPlayer.playerStatus.isFirstTurn = true;
            tempPlayer.playerStatus.nActionsDone = 0;
            tempPlayer.playerStatus.nActions = 2;
            tempPlayer.playerStatus.isFrenzyTurn = false;
        }
        */

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

    /*

    private void checkAsynchronousPowerUp(String nickname, ArrayList<String> arrayListdefenders) {
        Player player = gameModel.getPlayerByNickname(nickname);

        for(PowerUp p : player.getPowerUpList())
            if(p.getPowerUpName().equals("TargetingScope")){
                ArrayList<String> message = new ArrayList<>();
                message.add("You have a targeting Scope, write the nickname of the player you want to add a damage or write NONE");
                sendQuestionEvent(player.getNickname(), new ServerQuestion(QuestionType.UseAsyncPowerUp, message));
                player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.UseAsyncPowerUp;
            }

        for( String s : arrayListdefenders)
            for(PowerUp p : gameModel.getPlayerByNickname(s).getPowerUpList())
                if(p.getPowerUpName().equals("TagbackGrenade") && gameModel.p1SeeP2(gameModel.getPlayerByNickname(s).getxPosition(), gameModel.getPlayerByNickname(s).getyPosition(), player.getxPosition(), player.getyPosition())){
                    ArrayList<String> message = new ArrayList<>();
                    message.add("You have a tagback grenade, write the nickname of the player you want to add a damage or write NONE");
                    sendQuestionEvent(s, new ServerQuestion(QuestionType.UseAsyncPowerUp, message));
                    gameModel.getPlayerByNickname(s).playerStatus.waitingForAnswerToThisQuestion = QuestionType.UseAsyncPowerUp;
                }
    }
     */

    /*
    private void handleChooseWeaponToSwitch(String nickname, String answer) {

        Player player = gameModel.getPlayerByNickname(nickname);

        Weapon weaponToPick;

        if(answer.contains(SPLITTER))
            weaponToPick = gameModel.getWeaponByName(answer.split(SPLITTER)[0]);
        else
            weaponToPick = gameModel.getWeaponByName(answer);

        ArrayList<Color> weaponCost = weaponToPick.getCost();
        weaponCost.remove(0);

        if(weaponCost.isEmpty()){

            //This means the player is also telling which weapon he wants to discard
            if(answer.contains(SPLITTER)){

                String[] weapons = parseWeaponToSwitch(answer);
                gameModel.switchWeapons(player.getNickname(), weapons[0], weapons[1]);

            }
            else {

                gameModel.pickWeaponFromSpawn(player.getNickname(), answer);

            }

            ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
            sendQuestionEvent(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;

        }

        ArrayList<String> messages = gameModel.generatePaymentChoice(player, weaponCost);
        sendQuestionEvent(player.getNickname(), new ServerQuestion(QuestionType.PayWith, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.PayWith;

        player.playerStatus.lastQuestion = QuestionType.ChooseWeaponToSwitch;
        player.playerStatus.lastAnswer = answer;

        return;

    }
     */

    /*
    private void handlePayWith(String nickname, String answer) {

        Player player = gameModel.getPlayerByNickname(nickname);

        String[] paymentChosen = answer.split(DOUBLESPLITTER);

        for(String s : paymentChosen){

            //This means I'm paying with a power up
            if(s.contains(SPLITTER)){
                String chosenPowerUpToPay = s.split(SPLITTER)[0];
                player.removePowerUpByName(chosenPowerUpToPay, Color.valueOf(s.split(SPLITTER)[1]));
            }
            else {
                Color chosenColorToPay = Color.valueOf(s);
                player.removeAmmo(chosenColorToPay);
            }

        }

        //If I'm waiting on how to pay for the weapon he wants to grab from the ground
        if(player.playerStatus.lastQuestion == QuestionType.ChooseWeaponToSwitch){


            //This means the player is also telling which weapon he wants to discard
            if(player.playerStatus.lastAnswer.contains(SPLITTER)){

                String[] weapons = parseWeaponToSwitch(player.playerStatus.lastAnswer);
                gameModel.switchWeapons(player.getNickname(), weapons[0], weapons[1]);

            }
            else {

                gameModel.pickWeaponFromSpawn(player.getNickname(), player.playerStatus.lastAnswer);

            }

            player.playerStatus.lastAnswer = null;
            player.playerStatus.lastQuestion = null;

            ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
            sendQuestionEvent(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;
        }

        if ( player.playerStatus.lastQuestion == QuestionType.Shoot ){

            //Ora last answer è così:        weaponName :: OrderNumber :: Defenders :: Movers :: coordinates
            String[] info = player.playerStatus.lastAnswer.split(DOUBLESPLITTER);

            Weapon weapon = gameModel.getWeaponByName(info[0]);

            String defender = info[2];

            //Questo è l'ordine scelto dall'utente
            int orderNumber = Integer.parseInt(info[1]);

            //Questo è un array di stringhe contenente i defenders
            String[] defenders = defender.split(SPLITTER);

            //Questa è la variabile ausiliaria che mi permette di sapere fin dove scorrere gli effetti in base a quanti giocatori mi ha passato l'utente e quanti giocatori permettono di colpire gli effetti
            int nPlayersInThisAttack = 0;

            boolean shootWithMovement = false;

            for (int i : weapon.getOrder().get(orderNumber)) {  //scorro gli effetti nell'ordine scelto per capire se chiamare ShootWith or ShootWithout movement

                Effect effect = weapon.getEffects().get(i);

                if(gameModel.typeOfEffect(effect) == 0)     //movement effect
                    shootWithMovement = true;

                nPlayersInThisAttack += effect.getnPlayersAttackable();
                nPlayersInThisAttack += effect.getnPlayersMarkable();

                //se ci sono meno defender che persone da attaccare all'effetto a cui siamo arrivati, devo fermarmi qui, esco
                if (defenders.length <= nPlayersInThisAttack)
                    break;
            }

            //Creo un arrayList<String> defenders da passare a ShootWith or Without Movement
            ArrayList<String> arrayListdefenders = new ArrayList<>();
            for(String d : defenders )
                arrayListdefenders.add(d);

            if (shootWithMovement) {

                //Creo un arrayList<String> di movers da passare a ShootWithMovement
                ArrayList<String> arrayListMovers = new ArrayList<>();
                //Stringa in cui ho tutti i movers:       mover0 : mover1 : mover2
                String mov = info[3];
                //Ho diviso la stringa mov in piccole stringhe ognuna contenente un movers
                String[] movers = mov.split(SPLITTER);

                for( String string : movers)
                    arrayListMovers.add(string);

                //Questa è la stringa contenente tutte le coordinate:        x0,y0 : x1,y1
                String coord = info[4];

                //Ho diviso la stringa coord in piccole stringhe ognuna contenente una posizione es: coordinates[0] = x0,y0  coordinates[1] = x1,y1
                String[] coordinates = coord.split(SPLITTER);

                //Questi sono gli arrayList di interi che devo passare a ShootWithMovement
                ArrayList<Integer> xPositions = new ArrayList<>();
                ArrayList<Integer> yPositions = new ArrayList<>();

                String[] position;

                int xPos, yPos;

                for (String s : coordinates ){
                    //divido la posizione (inizialmente divisa da una virgola) in due stringhe, la prima contenente la x e la seconda la y
                    position = s.split(COMMA);
                    //trasformo la x e y in interi
                    xPos = Integer.parseInt(position[0]);
                    yPos = Integer.parseInt(position[1]);
                    //aggiungo la x e y agli arrayList da passare a ShootWithMovement
                    xPositions.add(xPos);
                    yPositions.add(yPos);
                }

                gameModel.shootWithMovement(nickname, arrayListdefenders, weapon, orderNumber, xPositions, yPositions, arrayListMovers);
            }else {
                gameModel.shootWithoutMovement(nickname, arrayListdefenders, weapon, orderNumber);
            }

            checkAsynchronousPowerUp(nickname, arrayListdefenders);

            ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
            sendQuestionEvent(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;
        }

        if(player.playerStatus.lastQuestion == QuestionType.ChooseWeaponToReload){

           for(int i = 0; i < player.getWeaponList().size(); i++)
               if(player.playerStatus.lastAnswer.equals(player.getWeaponList().get(i).getWeaponName()))
                   gameModel.reloadWeapon(nickname, i);

            player.playerStatus.lastAnswer = null;
            player.playerStatus.lastQuestion = null;

            ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
            sendQuestionEvent(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;
        }


    }
    */

    /*
    private void handleWhereToMoveAndGrab(String nickname, String answer) {


        Player player = gameModel.getPlayerByNickname(nickname);

        //Reads what spot the player decided to move to
        int xCoord;
        int yCoord;

        try {
            int[] coords = parseSpot(answer);
            xCoord = coords[0];
            yCoord = coords[1];
        }
        catch (IllegalArgumentException e){
            String message = "Invalid spot response";
            virtualView.sendMessage(player.getNickname(),  message);
            return;
        }

        gameModel.moveAndGrab(player.getNickname(), xCoord, yCoord, -1);

        player.playerStatus.nActionsDone += 1;


        ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
        sendQuestionEvent(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;


        return;

    }
    */

    /*
    private void handleAction(String nickname, String answer) {

        Player player = gameModel.getPlayerByNickname(nickname);

        //Reads what Action the player decided to do
        Actions action = null;
        try {
            action = Actions.valueOf(answer);
        }
        catch (IllegalArgumentException e){
            String message = "Invalid Action response";
            virtualView.sendMessage(player.getNickname(),  message);
            return;
        }


        if(action == Actions.UseAsyncPowerUp){
            //TODO
        }


    }
    */

    @Override
    public void receiveEvent(AnswerEvent answerEvent) {
        answerEvent.acceptEventHandler(this);
    }

    @Override
    public void handleEvent(NewConnectionAnswer event) {
        throw new RuntimeException("Connection messages should not arrive to the controller");
    }

    @Override
    public synchronized void handleEvent(DisconnectedAnswer event) {

        Player p = gameModel.getPlayerByNickname(event.nickname);

        if(p.playerStatus.isActive)
            endTurn();

        gameModel.disconnectPlayer(event.nickname);

        virtualView.disconnectPlayer(event.nickname);

        virtualView.sendAllQuestionEvent(
                new PlayerDisconnectedQuestion(event.nickname)
        );
    }

    @Override
    public void handleEvent(ActionAttackAnswer event) {

        List<String> weaponsLoaded = gameModel.getLoadedWeapons(event.nickname);

        if(weaponsLoaded.isEmpty()){
            sendMessage(event.nickname, "You have no loaded weapon");

            //Regenerates all the possible actions this player can make
            ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
            sendQuestionEvent(event.nickname,
                    new ActionQuestion(possibleActions)
            );

            return;
        }

        sendQuestionEvent(event.nickname, new ChooseWeaponToAttackQuestion(weaponsLoaded));

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
    public void handleEvent(ChooseHowToPayToPickWeaponAnswer event) {

        List<String> chosenPayment = event.chosenPayment;
        String SPLITTER = ":";
        Player playerObject = gameModel.getPlayerByNickname(event.nickname);

        //First, I make the player pay
        for(String s : chosenPayment){

            //This means I'm paying with a power up
            if(s.contains(SPLITTER)){
                String chosenPowerUpToPay = s.split(SPLITTER)[0];
                playerObject.removePowerUpByName(chosenPowerUpToPay, Color.valueOf(s.split(SPLITTER)[1]));
            }
            else {
                Color chosenColorToPay = Color.valueOf(s);
                playerObject.removeAmmo(chosenColorToPay);
            }

        }

        //I pick the weapon and give it to the player
        gameModel.pickWeaponFromSpawn(playerObject.getNickname(), event.weaponToPick);

        ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
        sendQuestionEvent(event.nickname,
            new ActionQuestion(possibleActions)
        );

    }


    @Override
    public void handleEvent(ChooseHowToShootAnswer event) {
        /*

        Player player = gameModel.getPlayerByNickname(event.nickname);

        //eventuale costo che ci sarà da pagare
        ArrayList<Color> cost = new ArrayList<>();

        Weapon weapon = gameModel.getWeaponByName(event.weapon);

        //Questo è l'ordine scelto dall'utente
        int orderNumber = event.orderNumber;

        //Questo è un array di stringhe contenente i defenders
        String[] defenders = (String[]) event.defenders.toArray();

        //Questa è la variabile ausiliaria che mi permette di sapere fin dove scorrere gli effetti in base a quanti giocatori mi ha passato l'utente e quanti giocatori permettono di colpire gli effetti
        int nPlayersInThisAttack = 0;

        boolean shootWithMovement = false;

        for (int i : weapon.getOrder().get(orderNumber)) {  //scorro gli effetti nell'ordine scelto per vedere se c'è un costo aggiuntivc da pagare e per capire se chiamare ShootWith or ShootWithout movement

            Effect effect = weapon.getEffects().get(i);

            if(gameModel.typeOfEffect(effect) == 0)     //movement effect
                shootWithMovement = true;

            cost.addAll(effect.getCost());

            nPlayersInThisAttack += effect.getnPlayersAttackable();
            nPlayersInThisAttack += effect.getnPlayersMarkable();

            //se ci sono meno defender che persone da attaccare all'effetto a cui siamo arrivati, devo fermarmi qui, esco
            if (defenders.length <= nPlayersInThisAttack)
                break;
        }

        //Se c'è un costo da pagare, devo chiedere all'utente come vuole pagarlo
        if( !cost.isEmpty() ){

            ArrayList<String> messages = gameModel.generatePaymentChoice(player, cost);

            sendQuestionEvent(event.nickname, new ChooseHowToPayForAttackingQuestion(event, cost));

            return;
        }else{
            //Caso in cui non devo pagare più nulla, quindi sparo!

            //Creo un arrayList<String> defenders da passare a ShootWithMovement
            ArrayList<String> arrayListdefenders = (ArrayList<String>)event.defenders;

            if (shootWithMovement) {

                String[] movers = (String[]) event.movers.toArray();

                //Questi sono gli arrayList di interi che devo passare a ShootWithMovement
                ArrayList<Integer> xPositions = (ArrayList<Integer>)event.xCoords;
                ArrayList<Integer> yPositions = (ArrayList<Integer>)event.yCoords;


                gameModel.shootWithMovement(event.nickname, arrayListdefenders, weapon, orderNumber, xPositions, yPositions, (ArrayList<String>)Arrays.asList(movers));


            }else {
                gameModel.shootWithoutMovement(event.nickname, arrayListdefenders, weapon, orderNumber);
            }

            checkAsynchronousPowerUp(event.nickname, arrayListdefenders);

            ArrayList<String> possibleActions = gameModel.generatePossibleActions(player.getNickname());

            sendQuestionEvent(event.nickname, new ActionQuestion(possibleActions));

        }


         */
    }

    @Override
    public void handleEvent(ChooseHowToUseTurnPowerUpAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        PowerUp powerUpToUse = gameModel.getPowerUpByName(event.powerUpToUse); //this is the powerup to use

        String offenderName = event.nickname;

        int x = event.x;

        int y = event.y;

        try {
            gameModel.useMovementPowerUp(event.nickname, offenderName, powerUpToUse.getEffect(), x, y);
            player.removePowerUpByName(powerUpToUse.getPowerUpName(), powerUpToUse.getColor());
        }
        catch(InvalidChoiceException e){
            sendMessage(event.nickname,"you can't use this powerUp like this bro");
        }

        ArrayList<String> possibleActions = gameModel.generatePossibleActions(player.getNickname());
        sendQuestionEvent(player.getNickname(), new ActionQuestion(possibleActions));

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

        Player player = gameModel.getPlayerByNickname(event.nickname);

        sendQuestionEvent(event.nickname, new ChooseHowToUseTurnPowerUpQuestion(event.powerUpToUse));

    }

    @Override
    public void handleEvent(ChooseWeaponToAttackAnswer event) {

        //FIXME
        //This needs the possible orders and a boolean to tell if it needs movement

        ArrayList<int[]> possibleOrders = new ArrayList<>();

        sendQuestionEvent(event.nickname, new ChooseHowToShootQuestion(event.chosenWeapon, possibleOrders, true));

    }

    @Override
    public void handleEvent(ChooseWeaponToPickAnswer event) {

        Weapon weaponToPick = gameModel.getWeaponByName(event.weaponToPick);

        ArrayList<Color> weaponCost = weaponToPick.getCost();
        weaponCost.remove(0);

        if(weaponCost.isEmpty()){

            gameModel.pickWeaponFromSpawn(event.nickname, event.nickname);

            ArrayList<String> possibleActions = gameModel.generatePossibleActions(event.nickname);
            sendQuestionEvent(event.nickname,
                    new ActionQuestion(possibleActions)
            );

            return;
        }

        Player p = gameModel.getPlayerByNickname(event.nickname);

        sendQuestionEvent(event.nickname,
                new ChooseHowToPayToPickWeaponQuestion(event.weaponToPick, weaponCost)
        );

    }

    @Override
    public void handleEvent(ChooseWeaponToReloadAnswer event) {

        Player player = gameModel.getPlayerByNickname(event.nickname);

        ArrayList<Color> cost = gameModel.getWeaponByName(event.weaponToReload).getCost();

        sendQuestionEvent(event.nickname, new ChooseHowToPayToReloadQuestion(event.weaponToReload));

    }

    @Override
    public void handleEvent(ChooseWeaponToSwitchAnswer event) {

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

}
