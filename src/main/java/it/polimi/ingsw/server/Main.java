package it.polimi.ingsw.server;

import it.polimi.ingsw.Log;
import it.polimi.ingsw.model.map.MapName;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class has a few functions:
 *      It starts the server on the given port
 *      It keeps accepting new connections from new clients
 */
public class Main {

    /**
     * The port where the server is running
     */
    private final int port;

    /**
     * The entry point for new messages directed to a running game
     */
    private static GamesHandler gamesHandler = new GamesHandler();

    /**
     * The list of active waiting rooms
     */
    private static ArrayList<WaitingRoom> waitingRooms = new ArrayList<>();

    /**
     * The nicknames of all the players currently connected
     */
    private static ArrayList<String> allNicknames = new ArrayList<>();



    /**
     * Basic constructor
     */
    private Main(int port){
        this.port = port;
    }

    /**
     * Adds the player to the correct waiting room, if the room is ready it starts a new game
     * @param nickname the nickname of the new player
     * @param mapToVote the map he voted for
     * @param nSkullsToVote the amount of skulls he voted for
     */
    /* TODO
    Attualmente se una waiting room è piena (5 players) e non si connettono più nuovi giocatori al Main,
    la partita non parte:
    questo perchè il controllo viene fatto quando aggiungo un giocatore alla waiting room
    (Se l'ultima waiting room è piena, inizio la partita e creo una nuova room per il giocatore nuovo)
     */
    synchronized static void addPlayerToWaitingRoom(Receiver receiver, String nickname, MapName mapToVote, int nSkullsToVote){

        if(gamesHandler.hasNickname(nickname) || allNicknames.contains(nickname))
            throw new RuntimeException("This username is not valid and this should have been checked before");

        //If there is no room yet or all the rooms are empty --> create a new room
        if(waitingRooms.isEmpty() || waitingRooms.get(waitingRooms.size() - 1).nPlayers() >= WaitingRoom.MAXPLAYERS){

            Log.LOGGER.log(Level.INFO, "Creating waiting room #" + waitingRooms.size());
            WaitingRoom newWaitingRoom = new WaitingRoom();
            newWaitingRoom.addPlayer(receiver, nickname, mapToVote, nSkullsToVote);
            Log.LOGGER.log(Level.INFO, "Adding " + nickname + " to waiting Room #" + waitingRooms.size());
            waitingRooms.add(newWaitingRoom);
        }

        //If there is a spot for a new player in the current room
        else{
            Log.LOGGER.log(Level.INFO, "Adding " + nickname + " to waiting Room #" + (waitingRooms.size() - 1));
            waitingRooms.get(waitingRooms.size() - 1).addPlayer(receiver, nickname, mapToVote, nSkullsToVote);
        }

        allNicknames.add(nickname);

        if(waitingRooms.get(waitingRooms.size() - 1).isReady()){
            startGame(waitingRooms.get(waitingRooms.size() - 1));
        }

    }

    /**
     * Starts a new game from the given waiting room
     * @param waitingRoom a ready waiting room
     */
    synchronized static void startGame(WaitingRoom waitingRoom) {
        if(!waitingRoom.isReady())
            throw new RuntimeException("This room is not ready to start a game");

        waitingRooms.remove(waitingRoom);

        //Remove all the waiting room nicknames from the temp nicknames
        //Also setting the correct game room number to all player receivers
        for(String nickname : waitingRoom.players) {
            allNicknames.remove(nickname);

            int index = waitingRoom.players.indexOf(nickname);
            waitingRoom.receivers.get(index).setNickname(nickname);
        }




        gamesHandler.startGame(waitingRoom);


    }

    /**
     * Inserts a player back to its original game
     * @param nickname
     */
    public static void reinsert(Receiver receiver, String nickname) {

        gamesHandler.reinsert(receiver, nickname);
    }

    /**
     * It keeps accepting new connections forever
     */
    private void startServer(){

        //The socket where the server is running
        ServerSocket serverSocket = null;

        try {
            //Opens a new server on the specified port
            serverSocket = new ServerSocket(port);
            Log.LOGGER.log(Level.INFO,"Opened new server on port 2345");
        }
        catch (IOException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

        //I now have a server open and ready to accept connections

        try {
            while (true) {
                //Accept a new connection
                Socket socket = serverSocket.accept();
                Log.LOGGER.log(Level.INFO, "New connection accepted from " + socket.getRemoteSocketAddress());

                //Creates a new receiver (the handler of the streams) but does not run it
                Receiver receiver = new Receiver(
                        "",
                        gamesHandler, //this is the receiver of all messages once the game has started
                        new BufferedReader(new InputStreamReader(socket.getInputStream())), //the stram from the client
                        new PrintWriter(socket.getOutputStream()) //the stream to the client
                );

                //Creates a new handler with the new socket
                ClientVotesHandler clientVotesHandler = new ClientVotesHandler(receiver);

                //Runs the new Handler on a new Thread
                Thread t = new Thread(clientVotesHandler);
                t.start();
            }
        }
        catch (IOException | NullPointerException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                serverSocket.close();
            }
            catch (IOException | NullPointerException h) {
                Log.LOGGER.log(Level.SEVERE, h.getMessage());
                h.printStackTrace();
            }
        }





    }

    /**
     * A new nickname is valid only if it's not taken
     * @param nickname the nickname to check
     * @return true if the nickname is not already an active nickname
     */
    static boolean activeUsername(String nickname){
        return gamesHandler.hasNickname(nickname) ||
                allNicknames.contains(nickname);
    }

    /**
     * Deletes the nickname from the list of active nicknames
     * @param nickname
     */
    static void disconnected(String nickname){
        if(nickname == null)
            return;
        allNicknames.remove(nickname);
    }



    /**
     * The server funtion just chooses the port and starts a server there
     * @param args not used
     */
    public static void main(String[] args){

        Main server = new Main(2345);
        server.startServer();

    }


}
