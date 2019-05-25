package it.polimi.ingsw.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.QuestionType;
import it.polimi.ingsw.view.ServerQuestion;
import it.polimi.ingsw.view.client.RemoteViewInterface;
import it.polimi.ingsw.view.server.VirtualView;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * This class waits for user input and sends it to the right controller
 * It associate every nickname to a specific Receiver and Controller
 * Every Receiver has 1 nickname
 * Every Controller has multiple nicknames
 */
public class GamesHandler {

    /**
     * A map that connects every nickname to its Controller
     */
    private static Map<String, Controller> nicknameControllers;

    private static ArrayList<WaitingRoom> waitingRooms;

    protected GamesHandler() {
        this.nicknameControllers = new HashMap<>();
        this.waitingRooms = new ArrayList<>();
    }

    static void startGame(WaitingRoom waitingRoom) {
        Game game = new Game(waitingRoom.players, waitingRoom.getVotedMap(), waitingRoom.getVotedSkulls());

        VirtualView virtualView = new VirtualView(waitingRoom.players, waitingRoom.sockets, waitingRoom.remoteViews);

        Controller controller = new Controller(game, virtualView);

        virtualView.addObserver(controller);
        game.addObserver(virtualView);

        for (String player : waitingRoom.players)
            nicknameControllers.put(player, controller);

        waitingRooms.remove(waitingRoom);


        controller.startGame();

    }

    static void roomNotFilledInTime(WaitingRoom waitingRoom) {

        MyLogger.LOGGER.log(Level.INFO, "Deleting " + waitingRoom + " due to lack of players");

        for (int i = 0; i < waitingRoom.players.size(); i++) {

            try {
                if(waitingRoom.sockets.get(i) != null) {
                    System.out.println("Notifying socket connection '" + waitingRoom.players.get(i) + "' that he's being disconnected");

                    PrintWriter printWriter = new PrintWriter(waitingRoom.sockets.get(i).getOutputStream());
                    printWriter.println("You are being disconnected from the server due to lack of players");
                    printWriter.flush();
                    Main.allConnectedUsernames.remove(waitingRoom.players.get(i));

                    waitingRoom.sockets.get(i).close();
                }
                else {
                    System.out.println("Notifying rmi connection '" + waitingRoom.players.get(i) + "' that he's being disconnected");

                    RemoteViewInterface remoteView = waitingRoom.remoteViews.get(i);
                    remoteView.sendMessage("You are being disconnected from the server due to lack of players");
                }
            } catch (Exception e) {
                MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            }

        }

        waitingRooms.remove(waitingRoom);


    }

    private void lostConnection(String nickname) {

        Controller controller = nicknameControllers.get(nickname);

        ArrayList<String> messages = new ArrayList<>();
        messages.add(nickname + " DISCONNECTED");

        System.out.println("LOST CONNECTION with " + nickname);
    }

    public void newConnection(Socket socket, String username, MapName votedMap, int votedSkulls) {

        if (waitingRooms.isEmpty())
            waitingRooms.add(new WaitingRoom());

        if (nicknameControllers.containsKey(username)) {
            //TODO
            return;
        }

        waitingRooms.get(waitingRooms.size() - 1).addPlayer(socket, username, votedMap, votedSkulls);

    }

    public void newConnection(RemoteViewInterface remoteView, String username, MapName votedMap, int votedSkulls) {

        if(waitingRooms.isEmpty())
            waitingRooms.add(new WaitingRoom());

        if(nicknameControllers.containsKey(username))   //TODO
            return;

        waitingRooms.get(waitingRooms.size() - 1).addPlayer(remoteView, username, votedMap, votedSkulls);

    }

}
