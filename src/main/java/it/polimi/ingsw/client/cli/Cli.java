package it.polimi.ingsw.client.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.client.UserInterface;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.view.client.RemoteView;
import it.polimi.ingsw.view.client.RemoteViewInterface;
import it.polimi.ingsw.view.client.RemoteViewRmiImpl;
import it.polimi.ingsw.view.client.RemoteViewSocket;

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class Cli implements QuestionEventHandler {
//TODO FARE I METODI SHOW
public class Cli implements UserInterface {

    private final String validUsername = "^[a-zA-Z0-9]*$";

    private final String serverAddress = "127.0.0.1";

    private JsonObject lastSnapshotReceived;

    private final int rmiPort = 5432;

    private final int socketPort = 2345;

    private final int rmiPort = 5432;

    private String username;

    private MapName currentMap;

    private int currentSkulls;

    private Integer temporaryId;

    RemoteView remoteView;

    private Scanner sysin;

    private Cli(){

        this.username = null;
        this.currentMap = null;
        this.currentSkulls = 0;
        this.remoteView = null;
        this.sysin = new Scanner(System.in);
    }

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

    private boolean validPort(int port) {

        return (port > 1000 && port < 10000);

    }

    private boolean validIp(String ipAddress) {
        //TODO andreaalf
        return true;
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
            System.err.println(e.getMessage());
            e.printStackTrace();
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

    private int chooseAnswer(String[] possibleAnswers){

        for(int i = 0; i < possibleAnswers.length; i++)
            System.out.println("[" + i + "] " + possibleAnswers[i]);

        Scanner scanner = new Scanner(System.in);

        int answer = scanner.nextInt();

        return answer;
    }

    private void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

//*********************** NETWORK EVENTS *************************************************

    @Override
    public void notify(String json) {
        JsonParser jsonParser = new JsonParser();
        this.lastSnapshotReceived = (JsonObject) jsonParser.parse(json);

        System.out.println("[*] Server notify -> " + json);
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
    public void handleEvent(DisconnectQuestion event) {
        System.err.println("DISCONNECTED FROM SERVER");
        remoteView = null;
    }

    @Override
    public void handleEvent(GameStartedQuestion event) {

        clearScreen();

        System.out.println("GAME STARTED");

        System.out.println("The payers are:");

        for(String player : event.playerNames)
            System.out.println(player);

        System.out.println(event.firstPlayer + " will be the first player");


    }

//*****************************************************************************************

    @Override
    public void handleEvent(ActionQuestion event) {

        System.out.println("****************************************");
        System.out.println("Choose action:");
        for(String action : event.possibleAction)
            System.out.println("[" + event.possibleAction.indexOf(action) + "] " + action);

        String nextLine = sysin.nextLine();
        int answer = Integer.parseInt(nextLine);

        String stringAnswer = event.possibleAction.get(answer);

        switch (stringAnswer){

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

            default:
                throw new RuntimeException("No such action --> " + stringAnswer);

        }


    }

    @Override
    public void handleEvent(ChooseHowToPayForAttackingQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToShootQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToUseTurnPowerUpQuestion event) {

    }

    @Override
    public void handleEvent(ChooseIfToUseAsyncPowerUpQuestion event) {

        System.out.println("****************************************");
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

        System.out.println("****************************************");
        System.out.println("Choose power up to respawn:");

        for(String powerUp : event.powerUpToRespawn)
            System.out.println("[" + event.powerUpToRespawn.indexOf(powerUp) + "] " + powerUp + " " + event.colors.get(event.powerUpToRespawn.indexOf(powerUp)));

        String line = sysin.nextLine();
        int answer = Integer.parseInt(line);

        remoteView.sendAnswerEvent(
                new ChoosePowerUpToRespawnAnswer(username, event.powerUpToRespawn.get(answer), event.colors.get(answer))
        );

    }

    @Override
    public void handleEvent(ChoosePowerUpToUseQuestion event) {

        System.out.println("****************************************");
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

        System.out.println("****************************************");
        System.out.println("Choose weapon to use:");

        for(String weaponName : event.weaponsLoaded)
            System.out.println("[" + event.weaponsLoaded.indexOf(weaponName) + "] " + weaponName);

        String line = sysin.nextLine();
        int answer = Integer.parseInt(line);

        remoteView.sendAnswerEvent(
                new ChooseWeaponToAttackAnswer(username, event.weaponsLoaded.get(answer))
        );



    }

    @Override
    public void handleEvent(ChooseWeaponToPickQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToReloadQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToSwitchQuestion event) {

    }

    @Override
    public void handleEvent(ModelUpdate event) {

    }

    @Override
    public void handleEvent(TextMessage event) {

    }

    @Override
    public void handleEvent(WhereToMoveAndGrabQuestion event) {

    }

    @Override
    public void handleEvent(WhereToMoveQuestion event) {

    }






    @Override
    public void receiveEvent(QuestionEvent questionEvent) {
        questionEvent.acceptEventHandler(this);
    }

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

    public void showGameMap(){}

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
}
