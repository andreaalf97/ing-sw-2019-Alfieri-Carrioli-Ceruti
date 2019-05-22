package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.Receiver;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.ClientAnswer;
import it.polimi.ingsw.view.QuestionType;
import it.polimi.ingsw.view.ServerQuestion;
import it.polimi.ingsw.view.server.VirtualView;

import java.util.ArrayList;

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

    /**
     * The char used to divide information into a single message
     */
    private final String SPLITTER = ":";

    /**
     * The char used to divide multiple leveled information into a single message
     */
    private final String DOUBLESPLITTER = "::";

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

        Player nextPlayer = gameModel.endTurnUpdateStatus();

        gameModel.checkDeaths();

        gameModel.refillAllAmmoSpots();

        gameModel.refillAllSpawnSpots();

        ArrayList<String> messages = gameModel.generatePossibleActions(nextPlayer.getNickname());
        virtualView.sendQuestion(nextPlayer.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        nextPlayer.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

    }

    /**
     * Returns the coordinates of the spot from the string message
     */
    private int[] parseSpot(String answer) {

        int[] coords = new int[2];

        coords[0] = Integer.parseInt(answer.split(SPLITTER)[0]);
        coords[1] = Integer.parseInt(answer.split(SPLITTER)[1]);

        return coords;
    }

    private String[] parseWeaponToSwitch(String answer){

        String[] weapons = new String[2];

        weapons[0] = answer.split(SPLITTER)[0];
        weapons[1] = answer.split(SPLITTER)[1];

        return weapons;

    }

    /**
     * Reinserts the player in the game
     * @param nickname the nickname of the player I want to put back into the game
     */
    public void reinsert(String nickname, Receiver receiver) {

        virtualView.updateReceiver(nickname, receiver);

        ArrayList<String> messages = new ArrayList<>();
        messages.add(nickname + " reconnected");

        ServerQuestion serverQuestion = new ServerQuestion(QuestionType.TextMessage, messages);

        virtualView.sendAll(serverQuestion);

    }

    /**
     * Starts the game for all players
     */
    public void startGame() {

        ArrayList<String> message = new ArrayList<>();
        message.add("GAME STARTED");
        virtualView.sendAll(new ServerQuestion(QuestionType.TextMessage, message));

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

        firstPlayer.playerStatus.isActive = true;
        firstPlayer.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

        ArrayList<String> messages = new ArrayList<>();
        messages.add(firstPlayer.getNickname() + ": It is your turn");

        virtualView.sendQuestion(firstPlayer.getNickname(), new ServerQuestion(QuestionType.TextMessage, messages));

        messages = gameModel.generatePossibleActions(firstPlayer.getNickname());
        virtualView.sendQuestion(firstPlayer.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        firstPlayer.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

    }

    /**
     * Every message coming from a client arrives here
     * @param arg the ClientAnswer
     */
    @Override
    public void update(Object arg) {

        //This should never happen
        if(arg != null && !(arg instanceof ClientAnswer))
            throw new RuntimeException("The arg should be a ClientAnswer class");

        //The arg is always a ClientAnswer!
        ClientAnswer clientAnswer = (ClientAnswer) arg;

        //The index must be correct for the possible answer array
        if(clientAnswer.index > clientAnswer.possibleAnswers.size() - 1 || clientAnswer.index < 0){
            ArrayList<String> message = new ArrayList<>();
            message.add("Index out of bound");
            virtualView.sendQuestion(clientAnswer.sender,  new ServerQuestion(QuestionType.TextMessage, message));
            return;
        }

        //Reading info from the client answer
        Player player = gameModel.getPlayerByNickname(clientAnswer.sender);
        QuestionType questionType = clientAnswer.questionType;
        String answer = clientAnswer.possibleAnswers.get(clientAnswer.index);

        System.out.println("ClientAnswer received from " + player.getNickname());

        //If the controller wasn't waiting for this answer
        if(player.playerStatus.waitingForAnswerToThisQuestion == null || questionType != player.playerStatus.waitingForAnswerToThisQuestion){
            ArrayList<String> message = new ArrayList<>();
            message.add("This is not the answer I was waiting for");
            virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
            return;
        }

        //TODO might need to remove this for asynchronous power ups
        //If it wasn't the player's turn
        if ( isNotThisPlayersTurn(player) )
            return;

        //If the player responded with an Action to do
        if(questionType == QuestionType.Action){
            handleAction(player, answer);
        }

        //If the player responded with a power up to respawn
        if(questionType == QuestionType.ChoosePowerUpToRespawn){

            handleChoosePowerUpToRespawn(player, answer);
        }

        //If the player responded with the coords to move
        if(questionType == QuestionType.WhereToMove){

            handleWhereToMove(player, answer);
            //Reads what spot the player decided to move to
        }

        //If the player responded with the coords to move and grab
        if(questionType == QuestionType.WhereToMoveAndGrab){

            handleWhereToMoveAndGrab(player, answer);
        }

        if(questionType == QuestionType.PayWith){

            handlePayWith(player, answer);
        }

        //If the player responded with a weapon to switch with the spawn spot
        if(questionType == QuestionType.ChooseWeaponToSwitch){

            handleChooseWeaponToSwitch(player, answer);
        }

        //This is printed if I'm missing a return statement in the previous questions
        ArrayList<String> message = new ArrayList<>();
        message.add("The controller received your answer (MISSING RETURN SOMEWHERE");

        virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
    }

    private boolean isNotThisPlayersTurn(Player player){

        if(!player.playerStatus.isActive){
            ArrayList<String> message = new ArrayList<>();
            message.add("This is not your turn");
            virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
            return true;
        }
        return false;
    }

    private void handleChooseWeaponToSwitch(Player player, String answer) {

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
            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;

        }

        ArrayList<String> messages = generatePaymentChoice(player, weaponCost);
        virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.PayWith, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.PayWith;

        player.playerStatus.lastQuestion = QuestionType.ChooseWeaponToSwitch;
        player.playerStatus.lastAnswer = answer;

        return;

    }

    private void handlePayWith(Player player, String answer) {

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
            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;

        }

    }

    private void handleWhereToMoveAndGrab(Player player, String answer) {

        //Reads what spot the player decided to move to
        int xCoord;
        int yCoord;

        try {
            int[] coords = parseSpot(answer);
            xCoord = coords[0];
            yCoord = coords[1];
        }
        catch (IllegalArgumentException e){
            ArrayList<String> message = new ArrayList<>();
            message.add("Invalid spot response");
            virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
            return;
        }

        gameModel.moveAndGrab(player.getNickname(), xCoord, yCoord, -1);

        player.playerStatus.nActionsDone += 1;


        ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
        virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;


        return;

    }

    private void handleWhereToMove(Player player, String answer) {
        int xCoord;
        int yCoord;

        try {
            int[] coords = parseSpot(answer);
            xCoord = coords[0];
            yCoord = coords[1];
        }
        catch (IllegalArgumentException e){
            ArrayList<String> message = new ArrayList<>();
            message.add("Invalid spot response");
            virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
            return;
        }

        gameModel.movePlayer(player.getNickname(), xCoord, yCoord);

        player.playerStatus.nActionsDone += 1;

        ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
        virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

        return;
    }

    private void handleChoosePowerUpToRespawn(Player player, String answer) {

        String powerUpName = answer.split(SPLITTER)[0];
        Color color = Color.valueOf(answer.split(SPLITTER)[1].toUpperCase());

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

        ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
        virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

        return;

    }

    private void handleAction(Player player, String answer) {

        //Reads what Action the player decided to do
        Actions action = null;
        try {
            action = Actions.valueOf(answer);
        }
        catch (IllegalArgumentException e){
            ArrayList<String> message = new ArrayList<>();
            message.add("Invalid Action response");
            virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
            return;
        }

        //RESPAWN
        if(action == Actions.Respawn){

            //Draws a weapon and gives it to the player
            gameModel.givePowerUp(player.getNickname());

            //If it's this player's first turn, I give him another powerup
            if(player.playerStatus.isFirstTurn){
                gameModel.givePowerUp(player.getNickname());
            }

            //Creates the list of powerups to discard
            ArrayList<String> powerUpsToRespawn = new ArrayList<>();

            for(PowerUp p : player.getPowerUpList())
                powerUpsToRespawn.add(p.toString());

            virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.ChoosePowerUpToRespawn, powerUpsToRespawn));

            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.ChoosePowerUpToRespawn;

            return;
        }

        if(action == Actions.PickWeapon){

            //I read all the weapons on the spawn spot where the player is
            ArrayList<String> weaponsOnTheSpawnSpot = gameModel.weaponsToPick(player.getNickname());
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

            //The list for the messages to send the player
            ArrayList<String> possibleAnswers = new ArrayList<>();

            //If the player has to choose which weapon to discard
            if(playerWeapons.size() > 2){

                for(String i : weaponsToPick)
                    for(Weapon j : playerWeapons)
                        possibleAnswers.add(i + SPLITTER + j.getWeaponName());

            }
            else {
                //If the player only has to choose which weapon to pick
                possibleAnswers = weaponsToPick;
            }

            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.ChooseWeaponToSwitch, possibleAnswers));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.ChooseWeaponToSwitch;
            return;
        }

        if(action == Actions.Move){

            ArrayList<String> spots = new ArrayList<>();
            boolean[][] allowedSpots = gameModel.wherePlayerCanMove(player.getNickname(), player.getnMoves());

            for(int i = 0; i < allowedSpots.length; i++)
                for (int j = 0; j < allowedSpots[i].length; j++)
                    if(allowedSpots[i][j])
                        spots.add(i + SPLITTER + j);

            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.WhereToMove, spots));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.WhereToMove;

            return;
        }

        if(action == Actions.MoveAndGrab){

            ArrayList<String> spots = new ArrayList<>();
            boolean[][] allowedSpots = gameModel.wherePlayerCanMoveAndGrab(player.getNickname(), player.getnMovesBeforeGrabbing());

            for(int i = 0; i < allowedSpots.length; i++)
                for (int j = 0; j < allowedSpots[i].length; j++)
                    if(allowedSpots[i][j])
                        spots.add(i + SPLITTER + j);

            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.WhereToMoveAndGrab, spots));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.WhereToMoveAndGrab;

            return;
        }

        if(action == Actions.Attack){
            return;
        }

        if(action == Actions.UsePowerUp){
            return;
        }

        if(action == Actions.ReloadAndEndTurn){
            return;
        }

        if(action == Actions.EndTurn){

            //Ends the turn and sends a question to the next player
            endTurn();

            player.playerStatus.waitingForAnswerToThisQuestion = null;

            ArrayList<String> message = new ArrayList<>();
            message.add("Your turn is over");
            virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));



            return;
        }

    }

    private ArrayList<String> generatePaymentChoice(Player player, ArrayList<Color> cost) {

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
                    paymentOption += "RED" + DOUBLESPLITTER;
                for(int i = 0; i < redCost - tempRedAmmoInPayment; i++)
                    paymentOption += redPowerUps.get(i).toString() + DOUBLESPLITTER;

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
                    paymentOption += "BLUE" + DOUBLESPLITTER;
                for(int i = 0; i < blueCost - tempBlueAmmoInPayment; i++)
                    paymentOption += bluePowerUps.get(i).toString() + DOUBLESPLITTER;

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
                    paymentOption += "YELLOW" + DOUBLESPLITTER;
                for(int i = 0; i < yellowCost - tempYellowAmmoInPayment; i++)
                    paymentOption += yellowPowerUps.get(i).toString() + DOUBLESPLITTER;

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

    /*
    private void payToPick(String nickname, String weaponName) throws InvalidChoiceException{

        Weapon toPick = gameModel.getWeaponByName(weaponName);

        ArrayList<Color> price = toPick.getCost();
        price.remove(0);

        //If the weapon has more than one cost I try to make the player pay
        if(!price.isEmpty()) {
                gameModel.pay(nickname, price);
        }

    }
    */

}
