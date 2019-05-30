package it.polimi.ingsw.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.events.serverToClient.TemporaryIdQuestion;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.server.AnswerEventReceiver;
import it.polimi.ingsw.view.server.ServerProxySocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

/**
 * This class starts the server and listens for new socket connections from the clients
 */
public class Main {

    /**
     * The port that is listening for new socket connections
     */
    final static int socketPort = 2345;

    /**
     * The port where the server launches the rmi registry
     */
    final static int rmiPort = 5432;

    /**
     * A random number generator used to create a random id
     */
    final static Random rand = new Random();

    /**
     * The list of all connected users
     * TODO might need to move this to the games handler, or even completely remove it
     */
    static ArrayList<String> allConnectedUsernames;

    /**
     * The handler of all new connections and waiting rooms
     */
    static GamesHandler gamesHandler;

    private Main(){
        this.allConnectedUsernames = new ArrayList<>();
        this.gamesHandler = new GamesHandler();
    }

    /**
     * Starts the server by creating a new registry and a new server socket
     */
    private void start(){

        ServerSocket serverSocket = null; //The socket for new socket connections
        RmiServer rmiServer = null; //The server for new rmi connections

        try {
            rmiServer = new RmiServer(gamesHandler);    //tries to create a new rmi server, to which the clients will call the connect() method
        }
        catch (RemoteException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while creating rmi server");
            e.printStackTrace();
            return;
        }

        try {
            serverSocket = new ServerSocket(socketPort);    //Opens a new socket server
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while opening server socket");
            e.printStackTrace();
            return;
        }

        System.out.println("Server is open on port " + socketPort);

        try {

            //Creates the registry on the rmi port
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            //binds the name "server" with the server object
            System.out.println("Binding the name 'server' with the rmi server object...");
            registry.rebind("server", rmiServer);
            System.out.println("Bound OK");


            while (true) {

                Socket socket = serverSocket.accept(); //Keeps waiting for new connections
                System.out.println("Accepted new socket connection from " + socket.getRemoteSocketAddress());

                //Creates a new server proxy for this client
                ServerProxySocket serverProxySocket = new ServerProxySocket("", gamesHandler, socket);

                //Creates a new temporary ID and keeps checking until it's a valid id
                Integer temporaryId = rand.nextInt(100000);

                while ( ! gamesHandler.isAValidTemporaryId(temporaryId))
                    temporaryId = rand.nextInt(100000);

                gamesHandler.addTemporaryId(temporaryId, serverProxySocket);

                System.out.println("This socket was assigned ID: " + temporaryId);

                //starts the new server proxy to receive the connection message
                new Thread(serverProxySocket).start();

                //Sends the temporary id to the player
                //The player will send it back with all the necessary information (nickname, votes ecc.)
                serverProxySocket.sendQuestionEvent(
                        new TemporaryIdQuestion(temporaryId)
                );

            }
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while accepting new connections");
        }

    }

    /**
     * Starts the server
     */
    public static void main(String[] args) {

        Main main = new Main();

        main.start();

    }
}
