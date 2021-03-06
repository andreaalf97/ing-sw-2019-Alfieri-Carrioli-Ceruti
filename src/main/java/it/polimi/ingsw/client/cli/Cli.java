package it.polimi.ingsw.client.cli;

import com.google.gson.JsonObject;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.PlayerInfo;
import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.view.client.*;

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.regex.Pattern;

public class Cli implements QuestionEventHandler {

    /**
     * Regex used to check on player's nickname
     */
    private final String validUsername = "^[a-zA-Z0-9][a-zA-Z0-9]*$";

    /**
     * Regex used to validate the ip address
     */
    private final String validIpAddress = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     * Regex used to check if the user inserted a number -- to avoid Number Format exceptions
     */
    private final String validEventInput = "^[0-9][0-9]*$";

    /**
     * The port for the socket connections
     */
    private final int socketPort = 2345;

    /**
     * The port for RMI connections
     */
    private final int rmiPort = 5432;

    /**
     * The player's username
     */
    private String username;

    /**
     * Initially, this attribute contains the chosen map, then it contains the actual map the game is played on
     */
    private MapName currentMap;

    /**
     * Works the same as the currentMap attribute, but with kst skulls
     */
    private int currentSkulls;

    /**
     * A temporary ID given by the server during the first stage of connection
     */
    private Integer temporaryId;

    /**
     * The client proxy, used to receive and send messages
     */
    RemoteView remoteView;

    /**
     * The system input stream
     */
    private Scanner sysin;

    /**
     * The most updated snapshot of the game
     */
    private JsonObject lastSnapshotReceived;

    /**
     * All of this player's info
     */
    private PlayerInfo playerInfo;

    /**
     * The list of all players in the game
     */
    private ArrayList<String> allPlayers;

    /**
     * the players board colors
     */
    private ArrayList<PlayerColor> playerColors;

    /**
     * Lock
     */
    private Object lock;


    /**
     * Constructor
     */
    private Cli(){

        this.username = null;
        this.currentMap = null;
        this.currentSkulls = 0;
        this.remoteView = null;
        this.sysin = new Scanner(System.in);
        this.lastSnapshotReceived = new JsonObject();
        this.playerInfo = null;
        this.allPlayers = new ArrayList<>();
        this.playerColors = null;
        this.lock = new Object();
    }

    /**
     * Asks the player to vote and then starts the connection
     */
    private void start(){


        System.out.println("*************************");
        System.out.println("*       WELCOME TO      *");
        System.out.println("*       ADRENALINA      *");
        System.out.println("*************************");
        System.out.println();

        System.out.println("Insert username:");

        //reads the username from the user
        String username = sysin.nextLine();

        while (!Pattern.matches(validUsername, username)){
            System.out.println("The username can only contain letters and numbers");
            System.out.println("Insert username:");
            username = sysin.nextLine();
        }

        System.out.println("Choose Map:");
        System.out.println("0 -- FIRE");
        System.out.println("1 -- EARTH");
        System.out.println("2 -- WIND");
        System.out.println("3 -- WATER");

        //reads the chose map
        String nextLine = sysin.nextLine();

        while ( ! validateNumericInput(0, 3, nextLine)){
            System.out.println("Select a number between 0 and 3");
            System.out.println("Choose Map:");
            System.out.println("0 -- FIRE");
            System.out.println("1 -- EARTH");
            System.out.println("2 -- WIND");
            System.out.println("3 -- WATER");

            nextLine = sysin.nextLine();
        }

        int votedMapNumber = Integer.parseInt(nextLine);

        //Retrieves the enum from the index value
        MapName votedMap = MapName.values()[votedMapNumber];

        System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

        //Reads the vote for the skulls
        nextLine = sysin.nextLine();

        while ( ! validateNumericInput(5, 8, nextLine)){
            System.out.println("Select a number between 5 and 8");
            System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

            nextLine = sysin.nextLine();
        }

        int nSkulls = Integer.parseInt(nextLine);

        System.out.println("Choose network type:");
        System.out.println("0 -- Socket");
        System.out.println("1 -- RMI");

        //reads if the player wants to use rmi or socket
        nextLine = sysin.nextLine();

        while ( ! validateNumericInput(0, 1, nextLine)){
            System.out.println("Choose network type:");
            System.out.println("0 -- Socket");
            System.out.println("1 -- RMI");

            nextLine = sysin.nextLine();
        }

        int chosenIndex = Integer.parseInt(nextLine);

        System.out.println("Insert remote IP address");

        String ipAddress = sysin.nextLine();

        while( ! validIp(ipAddress) ){

            System.out.println("Not a valid IP address");
            System.out.println("Insert remote IP address");
            ipAddress = sysin.nextLine();

        }

        this.username = username;
        this.currentMap = votedMap;

        this.currentSkulls = nSkulls;

        clearScreen();

        //SOCKET
        if(chosenIndex == 0)
            startSocketConnection(ipAddress, socketPort);
        else if(chosenIndex == 1)   //RMI
            startRmiConnection(ipAddress, rmiPort);

    }

    /**
     * Checks if the ip is valid
     * @param ipAddress the IP under test
     * @return true if it's a valid IP
     */
    private boolean validIp(String ipAddress) {
        return Pattern.matches(validIpAddress, ipAddress);
    }

    /**
     * Starts a new rmi connection with the server by sending a connection event
     * @param ipAddress the address of the server
     * @param port the port running an rmi registry
     */
    private void startRmiConnection(String ipAddress, int port) {

        try {

            //Creates the remote view object to send to the server for callbacks
            RemoteViewInterface remoteView = new RemoteViewRmiImpl(this);

            this.remoteView = (RemoteViewRmiImpl)remoteView;

            //Searches for the registry
            Registry registry = LocateRegistry.getRegistry(ipAddress, port);

            //Looks for the 'server' object (only has one method called connect())
            ServerInterface rmiRemoteServer = (ServerInterface)registry.lookup("server");

            //Sends the remote view to the server and then waits for a TemporaryIdQuestion
            rmiRemoteServer.connect(remoteView);

        }
        catch (Exception e){
            System.err.println("Error while connecting to server");
        }

    }

    /**
     * Starts a new socket connection with the server by sending a connection event
     * @param ipAddress the address of the server
     * @param port the port running an rmi registry
     */
    private void startSocketConnection(String ipAddress, int port) {

        Socket serverSocket = null;
        try {
            //Connects with the server through socket
            serverSocket = new Socket(ipAddress, port);
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

        //Creates a new RemoteViewSocket object which is used to keep the connection open and read all new messages
        RemoteViewSocket remoteViewSocket = new RemoteViewSocket(serverSocket, this);

        this.remoteView = remoteViewSocket;

        //runs the remoteViewSocket on a new thread to keep it open
        new Thread(remoteViewSocket).run();

    }

    /**
     * Starts the CLI
     * @param args not used
     */
    public static void main(String[] args){

        System.setProperty("java.rmi.server.hostname", args[0]);
        System.out.println("RMI ip is set to " + args[0]);

        Cli cli = new Cli();

        cli.start();

    }

    /**
     * print all the possible answers that player can do
     * @param possibleAnswers the possible answers
     * @return the index of the answer chosen by the client
     */
    private int chooseAnswer(List<String> possibleAnswers){

        if(!possibleAnswers.contains("Reset")) {
            possibleAnswers.add("Reset");
        }

        for(String possibleAnswer : possibleAnswers)
            System.out.println("[" + possibleAnswers.indexOf(possibleAnswer) + "] " + possibleAnswer);

        String nextLine = sysin.nextLine();

        while ( ! validateNumericInput(0, possibleAnswers.size() - 1, nextLine)){
            System.out.println("Wrong format");
            nextLine = sysin.nextLine();
        }

        try {
             int answer = Integer.parseInt(nextLine);

             while (answer < 0 || answer >= possibleAnswers.size()){

                 for(String possibleAnswer : possibleAnswers)
                     System.out.println("[" + possibleAnswers.indexOf(possibleAnswer) + "] " + possibleAnswer);

                 nextLine = sysin.nextLine();
                 while ( ! validateNumericInput(0, possibleAnswers.size() - 1, nextLine)){
                     System.out.println("Wrong format");
                     nextLine = sysin.nextLine();
                 }

                 answer = Integer.parseInt(nextLine);

             }

             if(answer == possibleAnswers.indexOf("Reset"))
                 return -1;

             return answer;
        }
        catch (NumberFormatException e){
            System.out.println("WRONG FORMAT, PLEASE ENTER AGAIN");
            return chooseAnswer(possibleAnswers);
        }

    }

    /**
     * Clears the console
     */
    private void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * builds the correct representation of the cli
     * @param votedMap the map to build
     */
    public void buildCliMapRepresentation(MapName votedMap){
        MapGrid.buildCliMap(votedMap);
    }

    /**
     * fill the map with the last snapshot received so player can see it
     */
    private void fillMapWithSnapshot(){
        MapGrid.fillMapWithAmmoAndCoord(lastSnapshotReceived, playerColors, allPlayers);
    }

//********************************** PRINTING ********************************************

    /**
     * show game map to player
     */
    public void showGameMap(){

        synchronized (lock) {

            MapGrid.clearMap();
            CompleteGrid.clearGrid();
            CompleteGrid.separateBoards();

            MapGrid.buildCliMap(currentMap);
            fillMapWithSnapshot();
            CompleteGrid.copyMapGrid(); //now MapGrid is contained in Grid

            CompleteGrid.fillMapWithKst(lastSnapshotReceived, playerColors, allPlayers);
            CompleteGrid.fillMapWithPlayers(lastSnapshotReceived, playerColors, allPlayers, username);

            CompleteGrid.printMap();
            System.out.println();
            System.out.println();

        }

    }


    public void showMyBoard(){

        synchronized (lock) {
            PlayerGrid.clearGrid();
            PlayerGrid.fillPlayerGrid(lastSnapshotReceived, username, playerColors, allPlayers);
            PlayerGrid.printMap();

            System.out.println();
            System.out.println();
        }

    }

//*********************** NETWORK EVENTS *************************************************

    @Override
    public void handleEvent(TemporaryIdQuestion event) {

        temporaryId = event.temporaryId;

        //I'm sending the current values of the votes because initially the are the voted values, then
        //they become the actual values as voted by all players
        remoteView.sendAnswerEvent(
                new NewConnectionAnswer(temporaryId, username, currentMap, currentSkulls)
        );

    }

    @Override
    public void handleEvent(InvalidUsernameQuestion event) {

        System.out.println("Invalid username, please enter a new one");

        Scanner scanner = new Scanner(System.in);

        String newUsername = scanner.nextLine();

        while (!Pattern.matches(validUsername, newUsername)){
            System.out.println("The username can only contain letters and numbers");
            System.out.println("Enter username:");
            username = scanner.nextLine();
        }

        this.username = newUsername;

        clearScreen();

        remoteView.sendAnswerEvent(
                new NewConnectionAnswer(temporaryId, username, currentMap, currentSkulls)
        );

    }

    @Override
    public void handleEvent(AddedToWaitingRoomQuestion event) {
        clearScreen();
        System.out.println("You have been added to a waiting room");
        System.out.println("Connected players:");
        for(String player : event.players)
            System.out.println(player);
    }

    @Override
    public void handleEvent(NewPlayerConnectedQuestion event) {

        System.out.println(event.nickname);

    }

    @Override
    public void handleEvent(DisconnectedQuestion event) {

        System.err.println("DISCONNECTED FROM SERVER");
        remoteView = null;
        System.exit(0);

    }

    @Override
    public void handleEvent(GameStartedQuestion event) {


        //Scheduling the timer to send a Ping message every 2 seconds
       new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //System.err.println("Sending PING");
                remoteView.sendAnswerEvent(new Ping(username));
            }
        }, 0, 2000);





        System.out.println("GAME STARTED");

        System.out.println("The players are:");

        //adds all the other players to the list
        this.allPlayers = event.playerNames;

        for(String player : this.allPlayers)
            System.out.println(player);

        System.out.println("****************************************");

        System.out.println(event.firstPlayer + " will be the first player");

        System.out.println("****************************************");

        System.out.println("The chosen map is " +  event.mapName);
        System.out.println("We are playing with " + event.votedSkulls + " skulls");

        System.out.println("****************************************");

        this.playerColors = event.playerColors;
        this.currentMap = event.mapName;
        this.currentSkulls = event.votedSkulls;


        buildCliMapRepresentation(currentMap);
    }

    @Override
    public void handleEvent(GameRestartedQuestion event) {

        //Scheduling the timer to send a Ping message every 2 seconds
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //System.err.println("Sending PING");
                remoteView.sendAnswerEvent(new Ping(username));
            }
        }, 0, 2000);


        System.out.println("THE GAME HAS RESTARTED");

        this.allPlayers = event.playerNames;
        this.playerColors = event.colors;
        this.currentMap = event.mapName;
        this.currentSkulls = event.kstSkulls;


        buildCliMapRepresentation(currentMap);
    }

    @Override
    public void handleEvent(PlayerDisconnectedQuestion event) {

        System.out.println(event.nickname + " DISCONNECTED FROM THE GAME");

    }

//***************************************************************************************** END NETWORK EVENTS

    @Override
    public void handleEvent(ActionAfterReloadingQuestion event){

        event.possibleAction.add("ShowMap");
        event.possibleAction.add("ShowMyBoard");

        System.out.println("Choose action:");
        for(String action : event.possibleAction)
            System.out.println("[" + event.possibleAction.indexOf(action) + "] " + action);

        String nextLine = sysin.nextLine();

        while ( ! validateNumericInput(0, event.possibleAction.size() - 1, nextLine)){
            System.out.println("Wrong format");
            nextLine = sysin.nextLine();
        }

        int answer = Integer.parseInt(nextLine);

        while (answer < 0 || answer >= event.possibleAction.size()) {
            System.out.println("OUT OF BOUND! Reinsert number");

            System.out.println("Choose action:");
            for(String action : event.possibleAction)
                System.out.println("[" + event.possibleAction.indexOf(action) + "] " + action);

            nextLine = sysin.nextLine();

            while (!Pattern.matches(validEventInput, nextLine)){
                System.out.println("Wrong format");
                nextLine = sysin.nextLine();
            }

            answer = Integer.parseInt(nextLine);

        }

        String stringAnswer = event.possibleAction.get(answer);

        switch (stringAnswer){
            case "Reload":
                remoteView.sendAnswerEvent(new ActionReloadAnswer(username));
                break;

            case "EndTurn":
                remoteView.sendAnswerEvent(new ActionEndTurnAnswer(username));
                break;

            case "ShowMap":
                showGameMap();
                remoteView.sendAnswerEvent(new RefreshPossibleActionsAfterReloadingAnswer(username));
                break;

            case "ShowMyBoard":
                showMyBoard();
                remoteView.sendAnswerEvent(new RefreshPossibleActionsAfterReloadingAnswer(username));
                break;

            default:
                throw new RuntimeException("No such action --> " + stringAnswer);
        }
    }

    @Override
    public void handleEvent(ActionQuestion event) {

        event.possibleAction.add("ShowMap");
        event.possibleAction.add("ShowMyBoard");

        System.out.println("Choose action:");
        for(String action : event.possibleAction)
            System.out.println("[" + event.possibleAction.indexOf(action) + "] " + action);

        String nextLine = sysin.nextLine();

        while (!Pattern.matches(validEventInput, nextLine)){
            System.out.println("Wrong format");
            nextLine = sysin.nextLine();
        }

        int answer = Integer.parseInt(nextLine);


        while (answer < 0 || answer >= event.possibleAction.size()) {
            System.out.println("OUT OF BOUND! reinsert the number");

            System.out.println("Choose action:");
            for(String action : event.possibleAction)
                System.out.println("[" + event.possibleAction.indexOf(action) + "] " + action);

            nextLine = sysin.nextLine();

            while ( ! validateNumericInput(0, event.possibleAction.size() - 1, nextLine)){
                System.out.println("Wrong format");
                nextLine = sysin.nextLine();
            }

            answer = Integer.parseInt(nextLine);

        }

        String stringAnswer = event.possibleAction.get(answer);

        switch (stringAnswer) {

            case "Attack":
                remoteView.sendAnswerEvent(new ActionAttackAnswer(username));
                break;

            case "EndTurn":
                remoteView.sendAnswerEvent(new ActionEndTurnAnswer(username));
                break;

            case "MoveAndGrab":
                remoteView.sendAnswerEvent(new ActionMoveAndGrabAnswer(username));
                break;

            case "Move":
                remoteView.sendAnswerEvent(new ActionMoveAnswer(username));
                break;

            case "PickWeapon":
                remoteView.sendAnswerEvent(new ActionPickWeaponAnswer(username));
                break;

            case "Reload":
                remoteView.sendAnswerEvent(new ActionReloadAnswer(username));
                break;

            case "Respawn":
                remoteView.sendAnswerEvent(new ActionRespawnAnswer(username));
                break;

            case "UseTurnPowerUp":
                remoteView.sendAnswerEvent(new ActionUseTurnPowerUpAnswer(username));
                break;

            case "ShowMap":
                showGameMap();
                remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
                break;

            case "ShowMyBoard":
                showMyBoard();
                remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
                break;

            default:
                throw new RuntimeException("No such action --> " + stringAnswer);
        }

    }

    /**
     * first client choose the weapon to attack
     * @param event
     */
    @Override
    public void handleEvent(ChooseWeaponToAttackQuestion event) {

        System.out.println("Choose weapon to use:");

        int answer = chooseAnswerWithNoResetOption(event.weaponsLoaded);

        if(answer == -1) {
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        remoteView.sendAnswerEvent(
                new ChooseWeaponToAttackAnswer(username, event.weaponsLoaded.get(answer))
        );

    }

    /**
     * then client has to choose the weapon attack order
     * @param event
     */
    @Override
    public void handleEvent(AskOrderAndDefenderQuestion event){

        System.out.println("Choose how to shoot:");

        System.out.println("Let's start with the order");

        ArrayList<String> stringOrders = toStringArray(event.possibleOrders);

        int answer = chooseAnswerWithNoResetOption(stringOrders);

        if(answer == -1){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        Integer[] chosenOrder = event.possibleOrders.get(answer);

        System.out.println("Now the defenders");
        //The list of chosen defenders
        ArrayList<String> defenders = new ArrayList<>();

        //The possible choices is the list of other players + the STOP choice
        ArrayList<String> possibleChoices = new ArrayList<>(this.allPlayers);
        possibleChoices.add("STOP");

        //The index of the STOP choice
        int indexOfStopAnswer = possibleChoices.indexOf("STOP");

        System.out.println("Choose a defender");

        answer = chooseAnswerWithNoResetOption(possibleChoices);

        if(answer == -1){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        //Adding the first defender
        if(answer != indexOfStopAnswer)
            defenders.add(possibleChoices.get(answer));

        //Keeps adding defenders until the user chooses STOP
        while ( answer != indexOfStopAnswer ){

            System.out.println("Next:");
            answer = chooseAnswerWithNoResetOption(possibleChoices);

            if(answer == -1){
                remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
                return;
            }

            if(answer != indexOfStopAnswer)
                defenders.add(possibleChoices.get(answer));
        }

        remoteView.sendAnswerEvent(
                new AskOrderAndDefenderAnswer(username, event.chosenWeapon, chosenOrder, defenders)
        );
    }

    @Override
    public void handleEvent(ChooseIfUseATargetingScopeQuestion event){

        String defenderChosen;

        System.out.println("Do you want to use your targeting scope against someone?:");

        ArrayList<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("YES");
        possibleAnswers.add("NO");

        int answer = chooseAnswerWithNoResetOption(possibleAnswers);

        if(answer == -1){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        if(answer == 0) {
            System.out.println("Choose the player you want to apply the targetingScope on:");
            possibleAnswers = new ArrayList<>();

            for (String playerdefender : event.defenders) {
                possibleAnswers.add(playerdefender);
            }

            answer = chooseAnswerWithNoResetOption((possibleAnswers));

            if (answer == -1) {
                remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
                return;
            }

            defenderChosen = possibleAnswers.get(answer);
        }
        else{
            defenderChosen = null;
        }

            remoteView.sendAnswerEvent(new ChooseIfUseATargetingScopeAnswer(event.nickname, event.chosenWeapon, event.order, event.shootWithMovement, event.indexOfLastEffect, event.defenders, defenderChosen));

    }

    @Override
    public void handleEvent(UseGrenadeQuestion event) {


        System.out.println("Do you want to use your grenade against " +  event.offender + " ?");

        ArrayList<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("YES");
        possibleAnswers.add("NO");

        int answer = chooseAnswer(possibleAnswers);

        if(answer == 0){
            remoteView.sendAnswerEvent(new UseGrenadeAnswer(username, event.offender));
        }
    }

    @Override
    public void handleEvent(ChooseHowToShootQuestion event) {
        int answer;

        //The possible choices is the list of other players + the STOP choice
        ArrayList<String> possibleChoices = new ArrayList<>(this.allPlayers);
        possibleChoices.add("STOP");

        //The index of the STOP choice
        int indexOfStopAnswer;

        //now i can call the gameModel methods with all the info
        if(event.shootWithMovement){

            ArrayList<String> movers = new ArrayList<>();
            ArrayList<Integer> xCoords = new ArrayList<>();
            ArrayList<Integer> yCoords = new ArrayList<>();

            indexOfStopAnswer = possibleChoices.indexOf("STOP");

            System.out.println("Now the movers:");

            answer = chooseAnswerWithNoResetOption(possibleChoices);

            if(answer == -1){
                remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
                return;
            }

            //adding the first mover
            if(answer != indexOfStopAnswer){
                movers.add(possibleChoices.get(answer));
            }

            while (answer != indexOfStopAnswer) {


                System.out.println("Insert X for this player");
                String line = sysin.nextLine();

                while (!Pattern.matches(validEventInput, line)){
                    System.out.println("Wrong format");
                    line = sysin.nextLine();
                }

                xCoords.add(Integer.parseInt(line));

                System.out.println("Insert Y for this player");
                line = sysin.nextLine();

                while (!Pattern.matches(validEventInput, line)){
                    System.out.println("Wrong format");
                    line = sysin.nextLine();
                }

                yCoords.add(Integer.parseInt(line));

                System.out.println("Next mover:");
                answer = chooseAnswerWithNoResetOption(possibleChoices);

                if(answer == -1){
                    remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
                    return;
                }

                if(answer != indexOfStopAnswer){
                    movers.add(possibleChoices.get(answer));
                }

            }

            if(movers.isEmpty()){
                remoteView.sendAnswerEvent(
                        new ChooseHowToShootAnswer(username, event.order, event.chosenWeapon, event.defenders, null, null, null, event.indexOfLastEffect, event.defenderChosen)
                );
            }else {
                remoteView.sendAnswerEvent(
                        new ChooseHowToShootAnswer(username, event.order, event.chosenWeapon, event.defenders, movers, xCoords, yCoords, event.indexOfLastEffect, event.defenderChosen)
                );
            }
        }
        else{
            remoteView.sendAnswerEvent(
                    new ChooseHowToShootAnswer(username, event.order, event.chosenWeapon, event.defenders, null, null, null, event.indexOfLastEffect, event.defenderChosen));
        }
    }

    @Override
    public void handleEvent(ChooseHowToPayForAttackingQuestion event) {
        System.out.println("Choose how to pay to shoot ");
        ArrayList<String> paymentChosen = new ArrayList<>();

        String cost = "";
        if(!event.cost.isEmpty()) {
            for (Color c : event.cost)
                cost += c.toString() + " ";

            paymentChosen = handlePayment(event.cost);
        }

        remoteView.sendAnswerEvent(
                new ChooseHowToPayForAttackingAnswer(event.chooseHowToShootAnswer, paymentChosen)
        );

        System.out.println();
    }

    @Override
    public void handleEvent(ChooseHowToPayToSwitchWeaponsQuestion event) {

        System.out.println("You chose to discard " + event.weaponToDiscard);

        ArrayList<String> paymentChoice = handlePayment(event.weaponCost);

        if(paymentChoice == null){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        remoteView.sendAnswerEvent(
                new ChooseHowToPayToSwitchWeaponsAnswer(username, event.weaponToPick, paymentChoice, event.weaponToDiscard)
        );


    }

    @Override
    public void handleEvent(ChooseHowToPayToPickWeaponQuestion event) {

        System.out.println("Choose how to pay to pick " + event.weaponName);

        ArrayList<String> paymentChosen = handlePayment(event.cost);

        if(paymentChosen == null){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        remoteView.sendAnswerEvent(
                new ChooseHowToPayToPickWeaponAnswer(username, event.weaponName, paymentChosen)
        );

        System.out.println();
    }

    /**
     * this method handles player payment in Cli
     * @param costToPay the ammo to pay
     * @return the payment option chosen by the client
     */
    private ArrayList<String> handlePayment(List<Color> costToPay){
        String cost = "";

        for(Color c : costToPay)
            cost += c.toString() + " ";

        System.out.println("You have to pay --> " + cost);

        ArrayList<String> paymentChosen = new ArrayList<>();

        for(Color colorToPay : costToPay){

            System.out.println("How would you like to pay for " + colorToPay);

            ArrayList<String> possibleChoice = new ArrayList<>();

            switch (colorToPay){

                case RED:
                    if(playerInfo.nRedAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    break;

                case BLUE:
                    if(playerInfo.nBlueAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    break;

                case YELLOW:
                    if(playerInfo.nYellowAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    break;

                case ANY:
                    if(playerInfo.nRedAmmo > 0)
                        possibleChoice.add("RED");
                    if(playerInfo.nBlueAmmo > 0)
                        possibleChoice.add("BLUE");
                    if(playerInfo.nYellowAmmo > 0)
                        possibleChoice.add("YELLOW");
                    break;
            }

            for(int i = 0; i < playerInfo.powerUpNames.size(); i++){

                if(colorToPay.equals(Color.ANY) && ( ! playerInfo.powerUpNames.get(i).equals("TargetingScope"))){
                    possibleChoice.add(playerInfo.powerUpNames.get(i) + ":" + playerInfo.powerUpColors.get(i));
                }
                else if( ! colorToPay.equals(Color.ANY)) {

                    if(playerInfo.powerUpColors.get(i).equals(colorToPay))
                        possibleChoice.add(playerInfo.powerUpNames.get(i) + ":" + playerInfo.powerUpColors.get(i));

                }

            }

            int answer = chooseAnswer(possibleChoice);

            if(answer == -1){
                return null;
            }

            paymentChosen.add(possibleChoice.get(answer));

        }

        return paymentChosen;
    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadQuestion event) {

        System.out.println("You chose to reload " + event.weaponToReload);

        ArrayList<String> chosenPayment = handlePayment(event.cost);

        if(chosenPayment == null){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        remoteView.sendAnswerEvent(new ChooseHowToPayToReloadAnswer(username, event.weaponToReload, chosenPayment));


    }


    /**
     * modify an arrayList<Integer[]> in an ArrayList<String> to print the choices
     * @param possibleOrders the order of the weapon
     * @return the arrayList<String> correspondent
     */
    private ArrayList<String> toStringArray(ArrayList<Integer[]> possibleOrders) {

        ArrayList<String> returnValue = new ArrayList<>();

        for(Integer[] order : possibleOrders){

            String stringOrder = "[";

            for(int i = 0; i < order.length - 1; i++)
                stringOrder += order[i] + ", ";

            stringOrder += order[order.length - 1] + "]";

            returnValue.add(stringOrder);

        }

        return returnValue;

    }

    @Override
    public void handleEvent(ChooseHowToUseTurnPowerUpQuestion event) {
        System.out.println("Choose the name of the player to move:");
        for (String playerName : allPlayers) {
            int index = allPlayers.indexOf(playerName);

            String printing = "[" + index + "]";
            printing += ":" + playerName;
            System.out.println(printing);
        }

        String nextLine = sysin.nextLine();

        while (!Pattern.matches(validEventInput, nextLine)){
            System.out.println("Wrong format");
            nextLine = sysin.nextLine();
        }

        int answer = Integer.parseInt(nextLine);


        String playerTarget = allPlayers.get(answer);

        System.out.println("insert the coordinates in this format: x,y");

        nextLine = sysin.nextLine();

        while (!nextLine.contains(",")) {
            nextLine = sysin.nextLine();
        }

        String[] coords = nextLine.split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        remoteView.sendAnswerEvent(
                 new ChooseHowToUseTurnPowerUpAnswer(username, event.powerUpToUseName,event.powerUpToUseColor, playerTarget, x, y)
         );


    }

    @Override
    public void handleEvent(ChooseIfToUseAsyncPowerUpQuestion event) {

        System.out.println(event.powerUpName + ": do you want to use this power up?");

        System.out.println("[0] YES");
        System.out.println("[1] NO");

        String nextLine = sysin.nextLine();

        while (!Pattern.matches(validEventInput, nextLine)){
            System.out.println("Wrong format");
            nextLine = sysin.nextLine();
        }

        int answer = Integer.parseInt(nextLine);

        if(answer == 0){
            remoteView.sendAnswerEvent(
                    new ChooseIfToUseAsyncPowerUpAnswer(username, true, event)
            );
        }
        else{
            remoteView.sendAnswerEvent(
                    new ChooseIfToUseAsyncPowerUpAnswer(username, false, event)
            );
        }
    }

    @Override
    public void handleEvent(ChoosePowerUpToRespawnQuestion event) {

        System.out.println("Choose power up to respawn:");

        for(String powerUp : event.powerUpToRespawn){

            int index = event.powerUpToRespawn.indexOf(powerUp);

            String printing = "";
            printing += "[" + index + "] ";
            printing += powerUp + " " + event.colors.get(index).toString();

            System.out.println(printing);
        }

        String line = sysin.nextLine();

        while (!Pattern.matches(validEventInput, line)){
            System.out.println("Wrong format");
            line = sysin.nextLine();
        }

        int answer = Integer.parseInt(line);

        remoteView.sendAnswerEvent(
                new ChoosePowerUpToRespawnAnswer(username, event.powerUpToRespawn.get(answer), event.colors.get(answer))
        );

    }

    @Override
    public void handleEvent(ChoosePowerUpToUseQuestion event) {

        System.out.println("Choose power up to use:");

        for(String powerUp : event.powerUpNames)
            System.out.println("[" + event.powerUpNames.indexOf(powerUp) + "] " + powerUp + " " + event.colors.get(event.powerUpNames.indexOf(powerUp)));

        String line = sysin.nextLine();

        while (!Pattern.matches(validEventInput, line)){
            System.out.println("Wrong format");
            line = sysin.nextLine();
        }

        int answer = Integer.parseInt(line);

        remoteView.sendAnswerEvent(
                new ChoosePowerUpToUseAnswer(username, event.powerUpNames.get(answer), event.colors.get(answer))
        );
    }

    @Override
    public void handleEvent(ChooseWeaponToPickQuestion event) {

        int answer = chooseAnswer(event.weaponsToPick);

        if(answer == -1){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        remoteView.sendAnswerEvent(
                new ChooseWeaponToPickAnswer(username, event.weaponsToPick.get(answer))
        );

    }

    @Override
    public void handleEvent(ChooseWeaponToReloadQuestion event) {
        System.out.println("Attention! You are ending your turn with this action!\n");
        System.out.println("Choose the weapon you want to reload:");

        int answer = chooseAnswer(event.weaponsToReload);

        if(answer == -1){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        remoteView.sendAnswerEvent(
                new ChooseWeaponToReloadAnswer(username, event.weaponsToReload.get(answer))
        );

    }

    @Override
    public void handleEvent(ChooseWeaponToSwitchQuestion event) {

        System.out.println("Choose the weapon to discard");

        int indexToDiscard = chooseAnswer(event.weaponsToRemove);

        if (indexToDiscard == -1) {
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        System.out.println("Choose the weapon to pick");

        int indexToPick = chooseAnswer(event.weaponsToPick);

        if (indexToPick == -1) {
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        remoteView.sendAnswerEvent(
                new ChooseWeaponToSwitchAnswer(username, event.weaponsToRemove.get(indexToDiscard), event.weaponsToPick.get(indexToPick))
        );


    }

    @Override
    public void handleEvent(ModelUpdate event) {

        this.lastSnapshotReceived = JsonDeserializer.stringToJsonObject(event.json);

        //System.out.println("[!] NOTIFY : New JSON received");

        this.playerInfo = new PlayerInfo(username, lastSnapshotReceived);

    }

    @Override
    public void handleEvent(TextMessage event) {

        System.out.println("[*] NEW MESSAGE: " + event.message);

    }

    @Override
    public void handleEvent(WhereToMoveAndGrabQuestion event) {

        System.out.println("Choose where to move and grab:");

        int[] coords = askForCoords(event.possibleSpots);

        if(coords == null){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }


        MapGrid.removeAmmoLabel(coords[0],coords[1]);

        remoteView.sendAnswerEvent(
                new WhereToMoveAndGrabAnswer(username, coords[0], coords[1])
        );
    }

    @Override
    public void handleEvent(WhereToMoveQuestion event) {

        System.out.println("Choose where to move:");

        int[] coords = askForCoords(event.possibleSpots);

        if (coords == null){
            remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
            return;
        }

        remoteView.sendAnswerEvent(
                new WhereToMoveAnswer(username, coords[0], coords[1])
        );

    }

    /**
     * This method receives a list of possible coords and returns the chosen ones
     * @param possibleSpots the list of spots
     * @return the chosen coordinates
     */
    private int[] askForCoords(boolean[][] possibleSpots){

        //The list with all the possible coords
        ArrayList<int[]> possibleCoords = new ArrayList<>();

        //Fills the list with all possible coords
        for(int i = 0; i < possibleSpots.length; i++) {
            for (int j = 0; j < possibleSpots[i].length; j++) {
                if (possibleSpots[i][j] == true) {
                    int[] newCoord = new int[2];
                    newCoord[0] = i;
                    newCoord[1] = j;
                    possibleCoords.add(newCoord);
                }
            }
        }

        //Transforms all the coord into strings to print to the user
        ArrayList<String> possibleCoordsString = new ArrayList<>();


        for(int[] coords : possibleCoords){
            String newString = "(" + coords[0] + ", " +  coords[1] + ")";
            possibleCoordsString.add(newString);
        }

        int answer = chooseAnswer(possibleCoordsString);

        if(answer == -1)
            return null;

        return possibleCoords.get(answer);

    }

    @Override
    public void receiveEvent(QuestionEvent questionEvent) {

        new Thread( () -> questionEvent.acceptEventHandler(this)).start();

    }

    //TODO STACCARE LA CONNESSIONE
    @Override
    public void handleEvent(EndGameQuestion event)
    {
       System.out.println(event.winner + " won with " + event.winnerPoints + "points.");

       if(username.equals(event.winner)){
           System.out.println("Congratulations, you won!");
       }
       else
       {
           System.out.println("Better luck next time :)");
       }
    }

    @Override
    public void handleEvent(SendCanMoveBeforeShootingQuestion event){
        System.out.println("Do you want to move before shooting?");
        ArrayList<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("YES");
        possibleAnswers.add("NO");

        int answer = chooseAnswerWithNoResetOption(possibleAnswers);

        if(answer == 0){
            int coord[] = askForCoords(event.allowedSpots);

            if(coord != null){
                remoteView.sendAnswerEvent(new SendCanMoveBeforeShootingAnswer(username, coord[0], coord[1], event.weaponsLoaded));
            }
            else{
                remoteView.sendAnswerEvent(new SendCanMoveBeforeShootingAnswer(username, -1, -1, event.weaponsLoaded));
            }

        }else{
            remoteView.sendAnswerEvent(new SendCanMoveBeforeShootingAnswer(username, -1, -1, event.weaponsLoaded));
        }
    }

    @Override
    public  void handleEvent(SendCanReloadBeforeShootingQuestion event){
        System.out.println("Do you want to reload before shooting?");
        ArrayList<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("YES");
        possibleAnswers.add("NO");

        int answer = chooseAnswerWithNoResetOption(possibleAnswers);

        if(answer == 0){

            System.out.println("Choose weapon you want to reload");

            answer = chooseAnswerWithNoResetOption(event.rechargeableWeaponNames);

            remoteView.sendAnswerEvent( new SendCanReloadBeforeShootingAnswer(username, event.rechargeableWeaponNames.get(answer), event.weaponsLoaded));
        }
        else{
            remoteView.sendAnswerEvent( new SendCanReloadBeforeShootingAnswer(username, null, event.weaponsLoaded));
        }

    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadBeforeAttackQuestion event){

        System.out.println("You chose to reload " + event.weaponToReload);

        ArrayList<String> chosenPayment = handlePayment(event.cost);

        remoteView.sendAnswerEvent(new ChooseHowToPayToReloadBeforeAttackAnswer(username, event.weaponToReload, chosenPayment, event.weaponsLoaded));
    }

    private int chooseAnswerWithNoResetOption(ArrayList<String> possibleAnswers) {

        for(String possibleAnswer : possibleAnswers)
            System.out.println("[" + possibleAnswers.indexOf(possibleAnswer) + "] " + possibleAnswer);

        String nextLine = sysin.nextLine();

        while (!Pattern.matches(validEventInput, nextLine)){
            System.out.println("Wrong format");
            nextLine = sysin.nextLine();
        }

        try {
            int answer = Integer.parseInt(nextLine);

            while (answer < 0 || answer >= possibleAnswers.size()){

                for(String possibleAnswer : possibleAnswers)
                    System.out.println("[" + possibleAnswers.indexOf(possibleAnswer) + "] " + possibleAnswer);

                nextLine = sysin.nextLine();
                while (!Pattern.matches(validEventInput, nextLine)){
                    System.out.println("Wrong format");
                    nextLine = sysin.nextLine();
                }

                answer = Integer.parseInt(nextLine);

            }

            return answer;
        }
        catch (NumberFormatException e){
            System.out.println("WRONG FORMAT, PLEASE ENTER AGAIN");
            return chooseAnswer(possibleAnswers);
        }
    }

    private boolean validateNumericInput(int minValue, int maxValue, String input){

        if (!Pattern.matches(validEventInput, input))
            return false;

        int answer = Integer.parseInt(input);

        if(answer < minValue || answer > maxValue)
            return false;

        return true;

    }


}

