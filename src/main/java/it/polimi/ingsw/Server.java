package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.server.VirtualView;

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
     * The receiver of all connections
     */
    private static Proxy proxy;

    /**
     * The list of active waiting rooms
     */
    private ArrayList<WaitingRoom> waitingRooms;

    /**
     * The nicknames that haven't started a game yet
     */
    private static ArrayList<String> tempNicknames;



    /**
     * Basic constructor
     */
    public Server(int port){
        this.port = port;
        this.proxy = new Proxy();
        this.waitingRooms = new ArrayList<>();
    }

    /**
     * Adds the player to the correct waiting room, if the room is ready it starts a new game
     * @param nickname the nickname of the new player
     * @param mapToVote the map he voted for
     * @param nSkullsToVote the amount of skulls he voted for
     */
    public synchronized void addPlayerToWaitingRoom(Socket socket, String nickname, MapName mapToVote, int nSkullsToVote){

        if(proxy.hasNickname(nickname) || tempNicknames.contains(nickname))
            throw new RuntimeException("This username is not valid and this should have been checked before");

        //If there is no room yet or all the rooms are empty --> create a new room
        if(waitingRooms.isEmpty() || waitingRooms.get(waitingRooms.size() - 1).nPlayers() >= WaitingRoom.MAXPLAYERS){
            WaitingRoom newWaitingRoom = new WaitingRoom();
            newWaitingRoom.addPlayer(socket, nickname, mapToVote, nSkullsToVote);
            Log.LOGGER.log(Level.INFO, "Adding " + nickname + " to waiting Room #" + waitingRooms.size());
            waitingRooms.add(newWaitingRoom);
        }

        //If there is a spot for a new player in the current room
        else{
            Log.LOGGER.log(Level.INFO, "Adding " + nickname + " to waiting Room #" + (waitingRooms.size() - 1));
            waitingRooms.get(waitingRooms.size() - 1).addPlayer(socket, nickname, mapToVote, nSkullsToVote);
        }

        tempNicknames.add(nickname);

        if(waitingRooms.get(waitingRooms.size() - 1).isReady()){
            startGame(waitingRooms.get(waitingRooms.size() - 1));
        }

    }

    /**
     * Starts a new game from the given waiting room
     * @param waitingRoom a ready waiting room
     */
    public synchronized static void startGame(WaitingRoom waitingRoom) {
        if(!waitingRoom.isReady())
            throw new RuntimeException("This room is not ready to start a game");

        //Remove all the waiting room nicknames from the temp nicknames
        for(String nickname : waitingRoom.players)
            tempNicknames.remove(nickname);

        proxy.startGame(waitingRoom);


    }

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

                //Creates a new handler with the new socket
                ClientVotesHandler clientVotesHandler = new ClientVotesHandler(this, socket);

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
    public static boolean notAValidUsername(String nickname){
        return proxy.hasNickname(nickname) || tempNicknames.contains(nickname);
    }

    public static void main(String[] args){

        Server server = new Server(2345);
        server.startServer();

    }
}
