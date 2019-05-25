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

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to ADRENALINA");
        System.out.println("Insert username:");

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

        MapName votedMap = MapName.values()[votedMapNumber];

        System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

        int nSkulls = scanner.nextInt();

        while (nSkulls < 5 || nSkulls > 8){
            System.out.println("Select a number between 5 and 8");
            System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

            nSkulls = scanner.nextInt();
        }

        System.out.println("Choose network type:");
        System.out.println("0 -- Socket");
        System.out.println("1 -- RMI");

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

    private void startRmiConnection(String username, MapName votedMap, int nSkulls) {

        try {

            RemoteViewRmiImpl remoteView = new RemoteViewRmiImpl(this);

            Registry registry = LocateRegistry.getRegistry(serverAddress, rmiPort);

            ServerInterface server = (ServerInterface)registry.lookup("server");

            String connectionMessage = username + ":" + votedMap + ":" + nSkulls;

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

    private void startSocketConnection(String username, MapName votedMap, int nSkulls) {

        Socket serverSocket = null;
        try {
            serverSocket = new Socket(serverAddress, socketPort);
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

        RemoteViewSocket remoteViewSocket = new RemoteViewSocket(serverSocket);
        remoteViewSocket.sendMessage(username + ":" + votedMap + ":" + nSkulls);

        new Thread(remoteViewSocket).run();

        return;

    }

    public static void main(String[] args){

        Cli cli = new Cli();

        cli.start();

    }

    public String askQuestion(String questionMessage, String[] possibleAnswer) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("QUESTION --> " + questionMessage);

        for (int i = 0; i < possibleAnswer.length; i++)
            System.out.println("[" + i + "] " + possibleAnswer[i]);

        int answer = scanner.nextInt();

        while(answer < 0 || answer >= possibleAnswer.length ) {

            System.out.println("Invalid choice, choose again:");

            answer = scanner.nextInt();
        }

        return possibleAnswer[answer];

    }

    @Override
    public void notify(String json) {
        System.out.println("[*] Server notify -> " + json);
    }

    @Override
    public String askQuestionAction(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionWhereToMove(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionWhereToMoveAndGrab(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChoosePowerUpToRespawn(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChoosePowerUpToDiscard(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionActionChoosePowerUpToAttack(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChooseWeaponToAttack(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChooseWeaponToSwitch(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChooseWeaponToReload(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionPayWith(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionShoot(String[] possibleAnswers) {
        return null;
    }
}
