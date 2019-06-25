package it.polimi.ingsw.client.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.PlayerInfo;
import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.view.client.*;

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class Cli implements QuestionEventHandler {

    /**
     * Regex used to check on player's nickname
     */
    private final String validUsername = "^[a-zA-Z0-9]*$";

    /**
     * Regex used to validate the ip address
     */
    private final String validIpAddress = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

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
        int votedMapNumber = Integer.parseInt(nextLine);

        while (votedMapNumber < 0 || votedMapNumber > 3){
            System.out.println("Select a number between 0 and 3");
            System.out.println("Choose Map:");
            System.out.println("0 -- FIRE");
            System.out.println("1 -- EARTH");
            System.out.println("2 -- WIND");
            System.out.println("3 -- WATER");

            nextLine = sysin.nextLine();
            votedMapNumber = Integer.parseInt(nextLine);
        }

        //Retrieves the enum from the index value
        MapName votedMap = MapName.values()[votedMapNumber];

        System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

        //Reads the vote for the skulls
        nextLine = sysin.nextLine();
        int nSkulls = Integer.parseInt(nextLine);

        while (nSkulls < 5 || nSkulls > 8){
            System.out.println("Select a number between 5 and 8");
            System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

            nextLine = sysin.nextLine();
            nSkulls = Integer.parseInt(nextLine);
        }

        System.out.println("Choose network type:");
        System.out.println("0 -- Socket");
        System.out.println("1 -- RMI");

        //reads if the player wants to use rmi or socket
        nextLine = sysin.nextLine();
        int chosenIndex = Integer.parseInt(nextLine);

        while (chosenIndex != 0 && chosenIndex != 1){
            System.out.println("Choose network type:");
            System.out.println("0 -- Socket");
            System.out.println("1 -- RMI");

            nextLine = sysin.nextLine();
            chosenIndex = Integer.parseInt(nextLine);
        }

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

        try {
            sleep(20);
        }
        catch (InterruptedException e){
            System.err.println("Error while sleeping");
        }


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

        Cli cli = new Cli();

        cli.start();

    }

    private int chooseAnswer(List<String> possibleAnswers){

        for(String possibleAnswer : possibleAnswers)
            System.out.println("[" + possibleAnswers.indexOf(possibleAnswer) + "] " + possibleAnswer);

        String nextLine = sysin.nextLine();

        try {
             return Integer.parseInt(nextLine);
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
        System.out.println(lastSnapshotReceived.toString());
        MapGrid.fillMapWithAmmoAndCoord(lastSnapshotReceived, playerColors, allPlayers);
    }

    /**
     * show game map to player
     */
    public void showGameMap(){
        fillMapWithSnapshot();
        MapGrid.printMap();
    }

//********************************** PRINTING ********************************************

    public void showAll(){
        showKst();
        showGameMap();
        showPlayerNames();
        showPlayers();
    }

    public void showKst(){
        JsonArray jsonKstKills = lastSnapshotReceived.get("kst").getAsJsonObject().get("skullList").getAsJsonArray();
        JsonArray jsonKstIsOverKill = lastSnapshotReceived.get("kst").getAsJsonObject().get("skullList").getAsJsonArray();

        System.out.println("THIS IS THE KILLSHOT TRACK: \n");
        for(int i = 0; i < jsonKstKills.size(); i++){
            System.out.println("[" + i + "] : " + jsonKstKills.get(i).getAsString());

            if(jsonKstIsOverKill.get(i).getAsBoolean()){
                System.out.println("\t with Overkill");
            }

            System.out.println("\n");
        }
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");

    }



    public void showPlayers(){}

    public void showPlayerNames(){
        JsonArray jsonPlayerNames = lastSnapshotReceived.get("playerNames").getAsJsonArray();

        System.out.println("PLAYERS NAMES: \n");

        for(int i = 0; i < jsonPlayerNames.size(); i++){
            System.out.println("[" + i + "] : " + jsonPlayerNames.get(i).getAsString() + "\n");
        }

        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");

    }

    public void showPlayer(String nickname){}

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

    }

    @Override
    public void handleEvent(GameStartedQuestion event) {

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
        this.playerColors = event.playerColors;


        buildCliMapRepresentation(currentMap);
    }

    @Override
    public void handleEvent(PlayerDisconnectedQuestion event) {

        System.out.println(event.nickname + " DISCONNECTED FROM THE GAME");

    }

//*****************************************************************************************

    @Override
    public void handleEvent(ActionQuestion event) {

        event.possibleAction.add("ShowMap");

        System.out.println("Choose action:");
        for(String action : event.possibleAction)
            System.out.println("[" + event.possibleAction.indexOf(action) + "] " + action);


        String nextLine = sysin.nextLine();


        int answer = Integer.parseInt(nextLine);

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
                remoteView.sendAnswerEvent(new RefreshPossibleActionsAnswer(username));
                showGameMap(); //TODO USE EXISTENT QUESTION
                break;

            default:
                throw new RuntimeException("No such action --> " + stringAnswer);


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

            System.out.println("You have to pay --> " + cost);

            paymentChosen = handlePayment(event.cost);
        }

        remoteView.sendAnswerEvent(
                new ChooseHowToPayForAttackingAnswer(event.chooseHowToShootAnswer, paymentChosen)
        );

        System.out.println();
    }

    @Override
    public void handleEvent(ChooseHowToPayToPickWeaponQuestion event) {

        System.out.println("Choose how to pay to pick " + event.weaponName);

        ArrayList<String> paymentChosen = handlePayment(event.cost);

        remoteView.sendAnswerEvent(
                new ChooseHowToPayToPickWeaponAnswer(username, event.weaponName, paymentChosen)
        );

        System.out.println();
    }

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
                        possibleChoice.add(colorToPay.toString());
                    if(playerInfo.nBlueAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    if(playerInfo.nYellowAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    break;
            }

            for(Color powerUpColor : playerInfo.powerUpColors){

                if(powerUpColor.equals(colorToPay)){
                    int index = playerInfo.powerUpColors.indexOf(powerUpColor);
                    possibleChoice.add(playerInfo.powerUpNames.get(index) + ":" + powerUpColor);
                }

            }

            int answer = chooseAnswer(possibleChoice);

            paymentChosen.add(possibleChoice.get(answer));

        }

        return paymentChosen;
    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadQuestion event) {

    }


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
        int answer = Integer.parseInt(line);

        remoteView.sendAnswerEvent(
                new ChoosePowerUpToUseAnswer(username, event.powerUpNames.get(answer), event.colors.get(answer))
        );
    }

    @Override
    public void handleEvent(ChooseWeaponToAttackQuestion event) {

        System.out.println("Choose weapon to use:");

        int answer = chooseAnswer(event.weaponsLoaded);

        remoteView.sendAnswerEvent(
                new ChooseWeaponToAttackAnswer(username, event.weaponsLoaded.get(answer))
        );

    }

    @Override
    public void handleEvent(AskOrderAttackQuestion event){

        System.out.println("Choose how to shoot:");

        System.out.println("Let's start with the order");

        ArrayList<String> stringOrders = toStringArray(event.possibleOrders);

        int answer = chooseAnswer(stringOrders);

        Integer[] chosenOrder = event.possibleOrders.get(answer);

        remoteView.sendAnswerEvent(
                new AskOrderAttackAnswer(username, event.chosenWeapon, chosenOrder)
        );
    }

    @Override
    public void handleEvent(ChooseHowToShootQuestion event) {
        int answer;

        System.out.println("Now the defenders");

        //The list of chosen defenders
        ArrayList<String> defenders = new ArrayList<>();

        //The possible choices is the list of other players + the STOP choice
        ArrayList<String> possibleChoices = new ArrayList<>(this.allPlayers);
        possibleChoices.add("STOP");

        //The index of the STOP choice
        int indexOfStopAnswer = possibleChoices.indexOf("STOP");

        System.out.println("Choose a defender");

        answer = chooseAnswer(possibleChoices);

        //Adding the first defender
        if(answer != indexOfStopAnswer)
            defenders.add(possibleChoices.get(answer));

        //Keeps adding defenders until the user chooses STOP
        while ( answer != indexOfStopAnswer ){

            System.out.println("Next:");
            answer = chooseAnswer(possibleChoices);

            if(answer != indexOfStopAnswer)
                defenders.add(possibleChoices.get(answer));
        }





        if( ! event.shootWithMovement){
            remoteView.sendAnswerEvent(
                    new ChooseHowToShootAnswer(username, event.order, event.chosenWeapon, defenders, null, null, null)
            );
        }

        possibleChoices.remove("STOP");
        possibleChoices.add(this.username);
        possibleChoices.add("STOP");

        ArrayList<String> movers = new ArrayList<>();
        ArrayList<Integer> xCoords = new ArrayList<>();
        ArrayList<Integer> yCoords = new ArrayList<>();

        indexOfStopAnswer = possibleChoices.indexOf("STOP");

        System.out.println("Now the movers:");

        answer = chooseAnswer(possibleChoices);

        while ( answer != indexOfStopAnswer ){

            System.out.println("Insert X for this player");
            String line = sysin.nextLine();
            xCoords.add(Integer.parseInt(line));

            System.out.println("Insert Y for this player");
            line = sysin.nextLine();
            yCoords.add(Integer.parseInt(line));

            System.out.println("Next mover:");
            answer = chooseAnswer(possibleChoices);

        }

        remoteView.sendAnswerEvent(
                new ChooseHowToShootAnswer(username, event.order, event.chosenWeapon, defenders, movers, xCoords, yCoords)
        );

    }

    @Override
    public void handleEvent(ChooseWeaponToPickQuestion event) {

        int answer = chooseAnswer(event.weaponsToPick);

        remoteView.sendAnswerEvent(
                new ChooseWeaponToPickAnswer(username, event.weaponsToPick.get(answer))
        );

    }

    @Override
    public void handleEvent(ChooseWeaponToReloadQuestion event) {

        int answer = chooseAnswer(event.weaponsToReload);

        remoteView.sendAnswerEvent(
                new ChooseWeaponToReloadAnswer(username, event.weaponsToReload.get(answer))
        );

    }

    @Override
    public void handleEvent(ChooseWeaponToSwitchQuestion event) {

    }

    @Override
    public void handleEvent(ModelUpdate event) {

        this.lastSnapshotReceived = JsonDeserializer.stringToJsonObject(event.json);

        System.out.println("[!] NOTIFY : New JSON received");

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

        MapGrid.removeAmmoLabel(coords[0],coords[1]);

        remoteView.sendAnswerEvent(
                new WhereToMoveAndGrabAnswer(username, coords[0], coords[1])
        );
    }

    @Override
    public void handleEvent(WhereToMoveQuestion event) {

        System.out.println("Choose where to move:");

        int[] coords = askForCoords(event.possibleSpots);

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

        return possibleCoords.get(answer);

    }


    @Override
    public void receiveEvent(QuestionEvent questionEvent) {

        clearScreen();
        System.out.println();
        System.out.println("____________________________________________");
        System.out.println();

        questionEvent.acceptEventHandler(this);

        System.out.println();

    }
}

