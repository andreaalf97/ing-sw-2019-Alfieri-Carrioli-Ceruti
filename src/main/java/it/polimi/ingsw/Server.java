package it.polimi.ingsw;

import it.polimi.ingsw.controller.WaitingRoom;
import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.map.MapName;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

public class Server {

    /**
     * The port where the server is running
     */
    private final int port;

    /**
     * The list of open socket connections
     */
    private ArrayList<Socket> openConnections;

    /**
     * The list of active user names, used to validate new user names
     */
    private static ArrayList<String> usernames;

    /**
     * The list of active waiting rooms
     */
    private ArrayList<WaitingRoom> waitingRooms;

    /**
     * The max amount of players in each game
     */
    private static final int MAXPLAYERS = 8;



    /**
     * Basic constructor
     */
    public Server(int port){
        this.openConnections = new ArrayList<>();
        this.port = port;
        this.waitingRooms = new ArrayList<>();
        this.usernames = new ArrayList<>();
    }

    /**
     * Starts the game by passing the associated waiting room
     * @param room the waiting room
     */
    public void startGame(WaitingRoom room){
        //TODO need to close all connections once the game is over --> how to do it object oriented?
    }

    /**
     * Adds the player to the correct waiting room
     * @param nickname the nickname of the player
     * @param mapToVote the map he voted for
     * @param nSkullsToVote the amount of skulls he voted for
     */
    public synchronized void addPlayer(Socket socket, String nickname, MapName mapToVote, int nSkullsToVote){

        //If there is no room yet or all the rooms are empty --> create a new room
        if(waitingRooms.isEmpty() || waitingRooms.get(waitingRooms.size() - 1).nPlayers() >= MAXPLAYERS){
            WaitingRoom newWaitingRoom = new WaitingRoom();
            newWaitingRoom.addPlayer(socket, nickname, mapToVote, nSkullsToVote);
            waitingRooms.add(newWaitingRoom);
        }

        //If there is a spot for a new player in the current room
        else{
            this.waitingRooms.get(this.waitingRooms.size() - 1).addPlayer(socket, nickname, mapToVote, nSkullsToVote);
        }

    }

    public void removeRoom(WaitingRoom waitingRoom){
        if(waitingRooms.contains(waitingRoom))
            this.waitingRooms.remove(waitingRoom);
    }


    private void startServer(){

        ServerSocket serverSocket = null;

        try {
            //Opens a new server on the specified port
            serverSocket = new ServerSocket(port);
            System.out.println("Opened new server on port 2345");
        }
        catch (IOException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

        Log.LOGGER.log(Level.INFO, "Server ready on port " + port);
        //I now have a server open and ready to accept connections

        try {
            while (true) {
                //Accept a new connection
                Socket socket = serverSocket.accept();
                System.out.println("New connection accepted:");
                //Adds the connection to the list of open connections
                openConnections.add(socket);
                System.out.println("Added new socket to openConnection list");
                //Creates a new handler with the new socket
                ClientHandler clientHandler = new ClientHandler(this, socket);
                System.out.println("Created new clientHandler object");
                //Runs the new Handler on a new Thread
                Thread t = new Thread(clientHandler);
                t.start();
            }
        }
        catch (IOException | NullPointerException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }


        try {
            serverSocket.close();
        }
        catch (IOException | NullPointerException h) {
            Log.LOGGER.log(Level.SEVERE, h.getMessage());
            h.printStackTrace();
        }


    }

    /**
     * A new username is valid only if it's not taken
     * @param username the username to check
     * @return true if the username is not already an active username
     */
    public static boolean notAValidUsername(String username){
        return usernames.contains(username);
    }

    public static void main(String args[]){

        Server server = new Server(2345);
        System.out.println("Created new Server object with port 2345");
        server.startServer();

    }
}
