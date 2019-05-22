package it.polimi.ingsw.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.model.map.MapName;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

public class Main implements Observer {

    final static int socketPort = 2345;

    static ArrayList<WaitingRoom> waitingRooms = new ArrayList<>();

    static ArrayList<String> allConnectedUsernames = new ArrayList<>();

    static GamesHandler gamesHandler = new GamesHandler();

    static void addSocketConnection(String nickname, Socket socket){

        gamesHandler.addSocket(nickname, socket);

    }

    @Override
    public synchronized void notifyObserver(Object obj) {

        if(! (obj instanceof WaitingRoom))
            throw new RuntimeException("This must be a waiting room object");

        WaitingRoom waitingRoom = (WaitingRoom)obj;

        //If the waiting room has not been filled in time
        if(waitingRoom.players.size() < 3) {
            for (String player : waitingRoom.players) {
                gamesHandler.removeWaitingRoomPlayer(player);
            }

            waitingRooms.remove(waitingRoom);

            return;
        }

        waitingRooms.add(new WaitingRoom());

        gamesHandler.startGame(waitingRoom);

    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {

        new Thread( () -> {

            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(Main.socketPort);
            }
            catch (IOException e){
                MyLogger.LOGGER.log(Level.SEVERE, "Error while opening socket server");
            }

            try {
                while (true){

                    Socket newSocket = serverSocket.accept();

                    String jsonStart = new Scanner(newSocket.getInputStream()).nextLine();

                    //This message should look like USERNAME:MAP:SKULLS

                    String username = jsonStart.split(":")[0];
                    String votedMap = jsonStart.split(":")[1];
                    String votedSkulls = jsonStart.split(":")[2];

                    PrintWriter printWriter = new PrintWriter(newSocket.getOutputStream());


                    if(allConnectedUsernames.contains(username))
                        printWriter.println("{\"Message\": \"ERROR\"}");
                    else {
                        printWriter.println("{\"Message\": \"OK\"}");
                        allConnectedUsernames.add(username);
                    }


                    addSocketConnection(username, newSocket);

                    addPlayerToWaitingRoom(username, votedMap, votedSkulls);
                }
            }
            catch (IOException e){
                MyLogger.LOGGER.log(Level.SEVERE, "Error while accepting new socket connections");
            }

        }).start();

        MyLogger.LOGGER.log(Level.INFO, "Creating rmiReceiverServer");
        RmiReceiverServerImpl rmiReceiverServer = new RmiReceiverServerImpl(gamesHandler.serverProxy);

        MyLogger.LOGGER.log(Level.INFO, "Creating new Registry");
        Registry registry = LocateRegistry.getRegistry();

        MyLogger.LOGGER.log(Level.INFO, "Binding String \"rmi_server\" to RMI Server object");
        registry.bind("rmi_server", rmiReceiverServer);

        MyLogger.LOGGER.log(Level.INFO, "Waiting for new invocations from the clients");

    }

    static void addPlayerToWaitingRoom(String username, String votedMap, String votedSkulls) {

        waitingRooms.get(waitingRooms.size() - 1).addPlayer(username, MapName.valueOf(votedMap), Integer.parseInt(votedSkulls));

    }
}
