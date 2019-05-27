package it.polimi.ingsw.client;

import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.view.client.RemoteViewRmiImpl;
import it.polimi.ingsw.view.client.RemoteViewSocket;

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Cli implements UserInterface {

    private final String validUsername = "^[a-zA-Z0-9]*$";

    private final String serverAddress = "127.0.0.1";

    private final int rmiPort = 5432;

    private final int socketPort = 2345;

    private void start(){

        //creates a scanner to read from the stdin
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to ADRENALINA");
        System.out.println("Insert username:");

        //reads the username from the user
        String username = scanner.nextLine();

        while (!Pattern.matches(validUsername, username)){
            System.out.println("The username can only contain letters and numbers");
            System.out.println("Insert username:");
            username = scanner.nextLine();
        }

        System.out.println("Choose Map:");
        System.out.println("0 -- FIRE");
        System.out.println("1 -- EARTH");
        System.out.println("2 -- WIND");
        System.out.println("3 -- WATER");

        //reads the chose map
        int votedMapNumber = scanner.nextInt();

        while (votedMapNumber < 0 || votedMapNumber > 3){
            System.out.println("Select a number between 0 and 3");
            System.out.println("Choose Map:");
            System.out.println("0 -- FIRE");
            System.out.println("1 -- EARTH");
            System.out.println("2 -- WIND");
            System.out.println("3 -- WATER");

            votedMapNumber = scanner.nextInt();
        }

        //Retrieves the enum from the index value
        MapName votedMap = MapName.values()[votedMapNumber];

        System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

        //Reads the vote for the skulls
        int nSkulls = scanner.nextInt();

        while (nSkulls < 5 || nSkulls > 8){
            System.out.println("Select a number between 5 and 8");
            System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

            nSkulls = scanner.nextInt();
        }

        System.out.println("Choose network type:");
        System.out.println("0 -- Socket");
        System.out.println("1 -- RMI");

        //reads if the player wants to use rmi or socket
        int chosenIndex = scanner.nextInt();

        while (chosenIndex != 0 && chosenIndex != 1){
            System.out.println("Choose network type:");
            System.out.println("0 -- Socket");
            System.out.println("1 -- RMI");

            chosenIndex = scanner.nextInt();
        }

        //SOCKET
        if(chosenIndex == 0)
            startSocketConnection(username, votedMap, nSkulls);
        else if(chosenIndex == 1)   //RMI
            startRmiConnection(username, votedMap, nSkulls);


        return;
    }

    /**
     * Starts an rmi connection with the server
     * @param username the username of the player
     * @param votedMap the voted map
     * @param nSkulls the voted skulls
     */
    private void startRmiConnection(String username, MapName votedMap, int nSkulls) {

        try {

            //Creates the remote view object to send to the server for callbacks
            RemoteViewRmiImpl remoteView = new RemoteViewRmiImpl(this);

            //Searches for the registry
            Registry registry = LocateRegistry.getRegistry(serverAddress, rmiPort);

            //Looks for the 'server' object (only has one method called connect())
            ServerInterface server = (ServerInterface)registry.lookup("server");

            //Sends the connection message (USERNAME:VOTEDMAP:VOTEDSKULLS)
            String connectionMessage = username + ":" + votedMap + ":" + nSkulls;

            //Call the remote method CONNECT
            server.connect(remoteView, connectionMessage);

            return;


        }
        catch (Exception e){
            System.err.println("Error while connecting to server");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return;

    }

    /**
     * Starts a new socket connection with the server
     * @param username the username of the player
     * @param votedMap the voted map
     * @param nSkulls the voted skulls
     */
    protected void startSocketConnection(String username, MapName votedMap, int nSkulls) {

        Socket serverSocket = null;
        try {
            //Connects with the server through socket
            serverSocket = new Socket(serverAddress, socketPort);
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

        //Creates a new RemoteViewSocket object which is used to keep the connection open and read all new messages
        RemoteViewSocket remoteViewSocket = new RemoteViewSocket(serverSocket, this);

        //sends the connection message to the server
        remoteViewSocket.sendMessage(username + ":" + votedMap + ":" + nSkulls);

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

    @Override
    public void notify(String json) {
        System.out.println("[*] Server notify -> " + json);
    }

    @Override
    public int askQuestionAction(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("What action do you want to take?");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionWhereToMove(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Where do you want to move?");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionWhereToMoveAndGrab(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Where do you want to move and grab?");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionChoosePowerUpToRespawn(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Choose a power up to respawn:");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionActionChoosePowerUpToAttack(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Choose a power up to attack:");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionChooseWeaponToAttack(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Choose a weapon to attack:");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionChooseWeaponToSwitch(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Choose the weapons to switch:");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionChooseWeaponToReload(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Choose the weapon to reload:");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionPayWith(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Choose how to pay");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionShoot(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Shoot:");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionChoosePowerUpToUse(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Choose the power up to use:");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionUseTurnPowerUp(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Use a turn power up:");
        return chooseAnswer(possibleAnswers);

    }

    @Override
    public int askQuestionUseAsyncPowerUp(String[] possibleAnswers) {

        System.out.println("*******************************************");
        System.out.println("Use an async power up:");
        return chooseAnswer(possibleAnswers);

    }


    private int chooseAnswer(String[] possibleAnswers){

        for(int i = 0; i < possibleAnswers.length; i++)
            System.out.println("[" + i + "] " + possibleAnswers[i]);

        Scanner scanner = new Scanner(System.in);

        int answer = scanner.nextInt();

        return answer;
    }
}
