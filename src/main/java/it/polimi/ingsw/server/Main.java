package it.polimi.ingsw.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.map.MapName;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
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
            rmiServer = new RmiServer();    //tries to create a new rmi server, to which the clients will call the connect() method
        }
        catch (RemoteException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while creating rmi server");
            return;
        }

        try {
            serverSocket = new ServerSocket(socketPort);    //Opens a new socket server
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while opening server socket");
            return;
        }

        MyLogger.LOGGER.log(Level.INFO, "Server is open on port " + socketPort);

        try {

            //Creates the registry on the rmi port
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            //binds the name "server" with the server object
            MyLogger.LOGGER.log(Level.INFO, "Binding the name 'server' with the rmi server object...");
            registry.rebind("server", rmiServer);
            MyLogger.LOGGER.log(Level.INFO, "Bound OK");


            while (true) {

                Socket socket = serverSocket.accept(); //Keeps waiting for new connections
                MyLogger.LOGGER.log(Level.INFO, "Accepted new socket connection from " + socket.getRemoteSocketAddress());

                //Used to send messages through the socket
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

                //Reads the first line from the socket
                String connectionMessage = new Scanner(socket.getInputStream()).nextLine();

                //connectionMessage should look like USERNAME:MAP:SKULLS

                //Extracts all parts of the connection message
                String username = connectionMessage.split(":")[0];
                MapName votedMap = MapName.valueOf(connectionMessage.split(":")[1]);
                int votedSkulls = Integer.parseInt(connectionMessage.split(":")[2]);

                //FIXME
                if(allConnectedUsernames.contains(username)){
                    printWriter.println("Username already connected");
                    printWriter.flush();
                    continue;
                }

                //handles the new socket connection
                gamesHandler.newConnection(socket, username, votedMap, votedSkulls);
                printWriter.println("MESSAGE$OK");
                printWriter.flush();
                printWriter.close();

            }
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while accepting new connections");
        }
        catch (NumberFormatException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while parsing voted skulls");
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
