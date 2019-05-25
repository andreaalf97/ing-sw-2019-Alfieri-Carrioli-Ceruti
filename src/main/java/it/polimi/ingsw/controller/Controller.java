package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Effect;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.ClientAnswer;
import it.polimi.ingsw.view.QuestionType;
import it.polimi.ingsw.view.ServerQuestion;
import it.polimi.ingsw.view.server.VirtualView;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;

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
    public final static String SPLITTER = ":";

    /**
     * The char used to divide x and y of in coordinates
     */
    public final static String COMMA = ",";

    /**
     * The char used to divide multiple leveled information into a single message
     */
    public final static String DOUBLESPLITTER = "::";

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
     *      - notify the current player status
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
    public void reinsert(String nickname) {

        //TODO
        //virtualView.updateReceiver(nickname, receiver);

        ArrayList<String> messages = new ArrayList<>();
        messages.add(nickname + " reconnected");


    }

    /**
     * Starts the game for all players
     */
    public void startGame() {

        String message = "GAME STARTED";
        virtualView.sendAllMessage(message);

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

        String newMessage = firstPlayer.getNickname() + ": It is your turn";

        virtualView.sendMessage(firstPlayer.getNickname(), newMessage);

        ArrayList<String> messages = gameModel.generatePossibleActions(firstPlayer.getNickname());
        virtualView.sendQuestion(firstPlayer.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        firstPlayer.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

    }

    /**
     * Every message coming from a client arrives here
     * @param arg the ClientAnswer
     */
    @Override
    public void notifyObserver(Object arg) {
        //This should never happen
        if(arg != null && !(arg instanceof ClientAnswer))
            throw new RuntimeException("The arg should be a ClientAnswer class");

        //The arg is always a ClientAnswer!
        ClientAnswer clientAnswer = (ClientAnswer) arg;

        Player player = gameModel.getPlayerByNickname(clientAnswer.sender);

        //The index must be correct for the possible answer array
        if(clientAnswer.index > clientAnswer.possibleAnswers.size() - 1 || clientAnswer.index < 0){
            String message = "Index out of bound";
            virtualView.sendMessage(clientAnswer.sender,  message);
            return;
        }

        //Reading info from the client answer;
        QuestionType questionType = clientAnswer.questionType;
        String answer = clientAnswer.possibleAnswers.get(clientAnswer.index);

        System.out.println("ClientAnswer received from " + player.getNickname());

        //If the controller wasn't waiting for this answer
        if(player.playerStatus.waitingForAnswerToThisQuestion == null || questionType != player.playerStatus.waitingForAnswerToThisQuestion){
            String message = "This is not the answer I was waiting for";
            virtualView.sendMessage(player.getNickname(),  message);
            return;
        }

        //TODO might need to remove this for asynchronous power ups
        //If it wasn't the player's turn
        if ( isNotThisPlayersTurn(player) )
            return;

        //If the player responded with an Action to do
        if(questionType == QuestionType.Action){

            handleAction(clientAnswer.sender, answer);

            return;
        }

        //If the player responded with a power up to respawn
        if(questionType == QuestionType.ChoosePowerUpToRespawn){

            handleChoosePowerUpToRespawn(clientAnswer.sender, answer);

            return;
        }

        //If the player responded with the coords to move
        if(questionType == QuestionType.WhereToMove){

            handleWhereToMove(clientAnswer.sender, answer);
            //Reads what spot the player decided to move to
            return;
        }

        //If the player responded with the coords to move and grab
        if(questionType == QuestionType.WhereToMoveAndGrab){

            handleWhereToMoveAndGrab(clientAnswer.sender, answer);

            return;
        }

        if(questionType == QuestionType.PayWith){

            handlePayWith(clientAnswer.sender, answer);

            return;
        }

        //If the player responded with a weapon to switch with the spawn spot
        if(questionType == QuestionType.ChooseWeaponToSwitch){

            handleChooseWeaponToSwitch(clientAnswer.sender, answer);

            return;
        }

        if(questionType == QuestionType.ChooseWeaponToReload){

            handleChooseWeaponToReload(clientAnswer.sender, answer);

            return;
        }

        if(questionType == QuestionType.ChoosePowerUpToUse){

            handleChoosePowerUpToUse(clientAnswer.sender, answer);

            return;
        }

        if(questionType == QuestionType.UseTurnPowerUp){

            handlePowerUp(clientAnswer.sender, answer);

            return ;
        }

        if(questionType == QuestionType.ChooseWeaponToAttack) {

            handleChooseWeaponToAttack(clientAnswer.sender, answer);

            return;
        }

        if (questionType == QuestionType.Shoot) {

            handleShoot(clientAnswer.sender, answer);

            return;
        }

        //This is printed if I'm missing a return statement in the previous questions
        String message = "The controller received your answer (MISSING RETURN SOMEWHERE)";

        virtualView.sendMessage(player.getNickname(),  message);
    }

    private void handlePowerUp(String nickname, String answer) {
        Player player = gameModel.getPlayerByNickname(nickname);

        PowerUp powerUpToUse = gameModel.getPowerUpByName(player.playerStatus.lastAnswer); //this is the powerup to use

        String [] info = answer.split(DOUBLESPLITTER);

        String offenderName = info[0];

        int x = Integer.parseInt(info[1]);

        int y = Integer.parseInt(info[2]);

        try {
            gameModel.useMovementPowerUp(nickname, offenderName, powerUpToUse.getEffect(), x, y);
            player.removePowerUpByName(powerUpToUse.getPowerUpName(), powerUpToUse.getColor());
        }
        catch(InvalidChoiceException e){
            virtualView.sendMessage(player.getNickname(),"you can't use this powerUp like this bro");
        }

        ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
        virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

    }

    private void handleChoosePowerUpToUse(String nickname, String answer){
        Player player = gameModel.getPlayerByNickname(nickname);

        player.playerStatus.lastQuestion = QuestionType.ChoosePowerUpToUse;
        player.playerStatus.lastAnswer = answer;

        ArrayList<String> messages = new ArrayList<>();

        messages.add("Choose the player you want to move, the x and the y where do you want to move him");

        virtualView.sendQuestion(nickname, new ServerQuestion(QuestionType.UseTurnPowerUp, messages));

        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.UseTurnPowerUp;
    }


    private boolean isNotThisPlayersTurn(Player player){

        if(!player.playerStatus.isActive){
            String message = "This is not your turn";
            virtualView.sendMessage(player.getNickname(),  message);
            return true;
        }
        return false;
    }

    private void handleChooseWeaponToAttack(String nickname, String answer){

        Player player = gameModel.getPlayerByNickname(nickname);

        player.playerStatus.lastQuestion = QuestionType.ChooseWeaponToAttack;
        player.playerStatus.lastAnswer = answer;

        ArrayList<String> messages = new ArrayList<>();

        messages.add("Choose the index of the order, the players you want to shoot and eventually who and where you want to move");

        virtualView.sendQuestion(nickname, new ServerQuestion(QuestionType.Shoot, messages));

        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Shoot;

    }

    private void handleShoot(String nickname, String answer) {

        Player player = gameModel.getPlayerByNickname(nickname);

        //eventuale costo che ci sarà da pagare
        ArrayList<Color> cost = new ArrayList<>();

        Weapon weapon = gameModel.getWeaponByName(player.playerStatus.lastAnswer);

        //in answer ci sono le informazioni per sparare in questo modo  OrderIndex :: Defender0:Defender1:Defender2 :: Mover0:Mover2 :: x0,y0 : x1,y1
        String[] info = answer.split(DOUBLESPLITTER);

        String defender = info[1];

        //Questo è l'ordine scelto dall'utente
        int orderNumber = Integer.parseInt(info[0]);

        //Questo è un array di stringhe contenente i defenders
        String[] defenders = defender.split(SPLITTER);

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

            virtualView.sendQuestion(nickname, new ServerQuestion(QuestionType.PayWith, messages));

            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.PayWith;

            //aggiorno la risposta in modo da salvare anche il nome dell'arma, al primo posto, che servirà al prossimo giro, per sparare, dopo aver pagato
            answer = player.playerStatus.lastAnswer + DOUBLESPLITTER + answer;
            player.playerStatus.lastQuestion = QuestionType.Shoot;
            player.playerStatus.lastAnswer = answer;

            return;
        }else{
            //Caso in cui non devo pagare più nulla, quindi sparo!

            //Creo un arrayList<String> defenders da passare a ShootWithMovement
            ArrayList<String> arrayListdefenders = new ArrayList<>();
            for(String d : defenders )
                arrayListdefenders.add(d);

            if (shootWithMovement) {

                //Creo un arrayList<String> dmovers da passare a ShootWithMovement
                ArrayList<String> arrayListMovers = new ArrayList<>();
                //Stringa in cui ho tutti i movers:       mover0 : mover1 : mover2
                String mov = info[2];
                //Ho diviso la stringa mov in piccole stringhe ognuna contenente un mover
                String[] movers = mov.split(SPLITTER);

                for( String string : movers)
                    arrayListMovers.add(string);


                //Questa è la stringa contenente tutte le coordinate:        x0,y0 : x1,y1
                String coord = info[3];

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


            //Ho finito di sparare, genero le possibili azioni successive tra cui può scegliere l'utente
            ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

        }

    }

    private void checkAsynchronousPowerUp(String nickname, ArrayList<String> arrayListdefenders) {
        Player player = gameModel.getPlayerByNickname(nickname);

        for(PowerUp p : player.getPowerUpList())
            if(p.getPowerUpName().equals("TargetingScope")){
                ArrayList<String> message = new ArrayList<>();
                message.add("You have a targeting Scope, write the nickname of the player you want to add a damage or write NONE");
                virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.UseAsyncPowerUp, message));
                player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.UseAsyncPowerUp;
            }

        for( String s : arrayListdefenders)
            for(PowerUp p : gameModel.getPlayerByNickname(s).getPowerUpList())
                if(p.getPowerUpName().equals("TagbackGrenade") && gameModel.p1SeeP2(gameModel.getPlayerByNickname(s).getxPosition(), gameModel.getPlayerByNickname(s).getyPosition(), player.getxPosition(), player.getyPosition())){
                    ArrayList<String> message = new ArrayList<>();
                    message.add("You have a tagback grenade, write the nickname of the player you want to add a damage or write NONE");
                    virtualView.sendQuestion(s, new ServerQuestion(QuestionType.UseAsyncPowerUp, message));
                    gameModel.getPlayerByNickname(s).playerStatus.waitingForAnswerToThisQuestion = QuestionType.UseAsyncPowerUp;
                }
    }

    private void handleChooseWeaponToReload(String nickname, String answer) {

        Player player = gameModel.getPlayerByNickname(nickname);

        ArrayList<Color> cost = gameModel.getWeaponByName(answer).getCost();

        player.playerStatus.lastQuestion = QuestionType.ChooseWeaponToReload;
        player.playerStatus.lastAnswer = answer;

        ArrayList<String> messages = gameModel.generatePaymentChoice(player, cost);

        virtualView.sendQuestion(nickname, new ServerQuestion(QuestionType.PayWith, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.PayWith;

    }

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
            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;

        }

        ArrayList<String> messages = gameModel.generatePaymentChoice(player, weaponCost);
        virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.PayWith, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.PayWith;

        player.playerStatus.lastQuestion = QuestionType.ChooseWeaponToSwitch;
        player.playerStatus.lastAnswer = answer;

        return;

    }

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
            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
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
                //Ho diviso la stringa mov in piccole stringhe ognuna contenente un mover
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
            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
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
            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

            return;
        }


    }

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
        virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;


        return;

    }

    private void handleWhereToMove(String nickname, String answer) {
        Player player = gameModel.getPlayerByNickname(nickname);

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

        gameModel.movePlayer(player.getNickname(), xCoord, yCoord);

        player.playerStatus.nActionsDone += 1;

        ArrayList<String> messages = gameModel.generatePossibleActions(player.getNickname());
        virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.Action, messages));
        player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.Action;

        return;
    }

    private void handleChoosePowerUpToRespawn(String nickname, String answer) {
        Player player = gameModel.getPlayerByNickname(nickname);

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

        if(action == Actions.Attack){

            ArrayList<String> weaponsLoaded = gameModel.getLoadedWeapons(nickname);

            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.ChooseWeaponToAttack, weaponsLoaded));

            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.ChooseWeaponToAttack;

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

        if(action == Actions.UseTurnPowerUp){

            ArrayList<String> messages = new ArrayList<>();

            for (PowerUp p : player.getPowerUpList())
                if(p.isTurnPowerup())
                    messages.add(p.getPowerUpName());

            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.ChoosePowerUpToUse;

            virtualView.sendQuestion(nickname, new ServerQuestion(QuestionType.ChoosePowerUpToUse, messages));

            return;
        }

        if(action == Actions.UseAsyncPowerUp){
            //TODO
        }

        if(action == Actions.Reload){

            ArrayList<String> weapons = new ArrayList<>();

            ArrayList<Weapon> rechargeableWeapons =  gameModel.checkRechargeableWeapons(player.getNickname());

            for(Weapon w : rechargeableWeapons)
                weapons.add(w.getWeaponName());

            virtualView.sendQuestion(player.getNickname(), new ServerQuestion(QuestionType.ChooseWeaponToReload, weapons));

            player.playerStatus.waitingForAnswerToThisQuestion = QuestionType.ChooseWeaponToReload;

            return;
        }

        if(action == Actions.EndTurn){

            //Ends the turn and sends a question to the next player
            endTurn();

            player.playerStatus.waitingForAnswerToThisQuestion = null;

            String message = "Your turn is over";
            virtualView.sendMessage(player.getNickname(),  message);



            return;
        }

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
