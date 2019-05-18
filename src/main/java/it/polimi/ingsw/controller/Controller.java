package it.polimi.ingsw.controller;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.server.Receiver;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.ClientAnswer;
import it.polimi.ingsw.view.QuestionType;
import it.polimi.ingsw.view.ServerQuestion;
import it.polimi.ingsw.view.server.VirtualView;

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

    /**
     * The char used to divide information into a single message
     */
    private final String SPLITTER = ":";

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
     * @param o the VirtualView
     * @param arg the ClientAnswer
     */
    @Override
    public void update(Observable o, Object arg) {

        if( !(o instanceof VirtualView) )
            throw new RuntimeException("The observable is not a virtual view");

        if(arg != null && !(arg instanceof ClientAnswer))
            throw new RuntimeException("The arg should be a ClientAnswer class");

        MyLogger.LOGGER.log(Level.INFO, "Controller class received a new clientAnswer");

        ClientAnswer clientAnswer = (ClientAnswer) arg;

        if(clientAnswer.index > clientAnswer.possibleAnswers.size() - 1 || clientAnswer.index < 0){
            ArrayList<String> message = new ArrayList<>();
            message.add("Index out of bound");
            virtualView.sendQuestion(clientAnswer.sender,  new ServerQuestion(QuestionType.TextMessage, message));
            return;
        }

        Player player = gameModel.getPlayerByNickname(clientAnswer.sender);
        QuestionType questionType = clientAnswer.questionType;
        String answer = clientAnswer.possibleAnswers.get(clientAnswer.index);

        System.out.println("ClientAnswer received from " + player.getNickname());

        if(player.playerStatus.waitingForAnswerToThisQuestion == null || questionType != player.playerStatus.waitingForAnswerToThisQuestion){
            ArrayList<String> message = new ArrayList<>();
            message.add("This is not the answer I was waiting for");
            virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
            return;
        }

        if(!player.playerStatus.isActive){
            ArrayList<String> message = new ArrayList<>();
            message.add("This is not your turn");
            virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
            return;
        }

        //If the player responded with an Action to do
        if(questionType == QuestionType.Action){

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

                ArrayList<String> weaponsToPick = gameModel.weaponsToPick(player.getNickname());

                ArrayList<Weapon> playerWeapons = player.getWeaponList();

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

        if(questionType == QuestionType.ChoosePowerUpToRespawn){

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

        if(questionType == QuestionType.WhereToMove){

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

            gameModel.movePlayer(player.getNickname(), xCoord, yCoord);

            player.playerStatus.nActionsDone += 1;

            ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;
        }

        if(questionType == QuestionType.WhereToMoveAndGrab){

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

        if(questionType == QuestionType.ChooseWeaponToSwitch){

            //This means the player is also telling which weapon he wants to discard
            if(answer.contains(SPLITTER)){
                String[] weapons = parseWeaponToSwitch(answer);

                try {
                    payToPick(player.getNickname(), weapons[0]);
                }
                catch (InvalidChoiceException e){
                    ArrayList<String> message = new ArrayList<>();
                    message.add("Not enough ammo!");

                    virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
                    return;
                }

                gameModel.switchWeapons(player.getNickname(), weapons[0], weapons[1]);


            }
            else {

                try {
                    payToPick(player.getNickname(), answer);
                }
                catch (InvalidChoiceException e){
                    ArrayList<String> message = new ArrayList<>();
                    message.add("Not enough ammo!");

                    virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
                    return;
                }

                gameModel.pickWeaponFromSpawn(player.getNickname(), answer);

            }

            ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;
        }

        ArrayList<String> message = new ArrayList<>();
        message.add("The controller received your answer");

        virtualView.sendQuestion(player.getNickname(),  new ServerQuestion(QuestionType.TextMessage, message));
    }

    private void payToPick(String nickname, String weaponName) throws InvalidChoiceException{

        Weapon toPick = gameModel.getWeaponByName(weaponName);

        ArrayList<Color> price = toPick.getCost();
        price.remove(0);

        //If the weapon has more than one cost I try to make the player pay
        if(!price.isEmpty()) {
                gameModel.pay(nickname, price);
        }

    }

}
