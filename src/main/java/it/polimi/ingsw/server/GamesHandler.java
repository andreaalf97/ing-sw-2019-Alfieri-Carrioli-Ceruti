package it.polimi.ingsw.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.AddedToWaitingRoomQuestion;
import it.polimi.ingsw.events.serverToClient.DisconnectQuestion;
import it.polimi.ingsw.events.serverToClient.InvalidUsernameQuestion;
import it.polimi.ingsw.events.serverToClient.TextMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.client.RemoteViewInterface;
import it.polimi.ingsw.view.server.AnswerEventReceiver;
import it.polimi.ingsw.view.server.ServerProxy;
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
public class GamesHandler implements AnswerEventHandler, AnswerEventReceiver {

    /**
     * A map that connects every nickname to its Controller
     */
    private static Map<String, Controller> nicknamesControllers;

    /**
     * The list of all active waiting rooms
     */
    private static ArrayList<WaitingRoom> waitingRooms;

    /**
     * This holds the unnamed proxies until the connection message
     */
    private static Map<Integer, ServerProxy> temporaryProxies;

    GamesHandler() {

        this.waitingRooms = new ArrayList<>();
        this.nicknamesControllers = new HashMap<>();
        this.temporaryProxies = new HashMap<>();
    }

    synchronized boolean isAValidTemporaryId(Integer id){

        return ! temporaryProxies.containsKey(id);

    }

    void addTemporaryId(Integer id, ServerProxy proxy){

        temporaryProxies.put(id, proxy);

    }

    static void startGame(WaitingRoom waitingRoom) {

        //Creates a new Game
        Game game = new Game(waitingRoom.players, waitingRoom.getVotedMap(), waitingRoom.getVotedSkulls());

        //Creates a new virtual view
        VirtualView virtualView = new VirtualView(waitingRoom.players, waitingRoom.serverProxies);

        //Creates a new controller
        Controller controller = new Controller(game, virtualView);

        //Setting up the OBSERVER - OBSERVABLE pattern
        virtualView.addObserver(controller);
        game.addObserver(virtualView);

        //Adding each player to the PLAYER - CONTROLLER map
        for (String player : waitingRoom.players)
            nicknamesControllers.put(player, controller);

        //Deleting the waiting room
        waitingRooms.remove(waitingRoom);

        //Starting the game
        controller.startGame();

    }

    /**
     * This method is called if the room has not been filled in time
     * @param waitingRoom the room to delete
     */
    static void roomNotFilledInTime(WaitingRoom waitingRoom) {

        System.out.println("Deleting " + waitingRoom + " due to lack of players");

        for(ServerProxy p : waitingRoom.serverProxies){

            p.sendQuestionEvent(
                    new TextMessage("You are being disconnected befause the room didn't fill in time")
            );

            p.sendQuestionEvent(
                    new DisconnectQuestion()
            );

        }

        waitingRooms.remove(waitingRoom);

    }

    @Override
    public void receiveAnswer(AnswerEvent answerEvent) {
        this.receiveEvent(answerEvent);
    }

    @Override
    public void receiveEvent(AnswerEvent answerEvent) {
        answerEvent.acceptEventHandler(this);
    }

    @Override
    public void handleEvent(NewConnectionAnswer event) {

        Integer temporaryId = event.temporaryId;

        String nickname = event.nickname;

        MapName votedMap = event.votedMap;

        int votedSkulls = event.votedSkulls;

        //If this is not a valid username, I ask the player to choose another one
        if( notAValidUsername(nickname)){

            temporaryProxies.get(temporaryId).sendQuestionEvent(
                    new InvalidUsernameQuestion()
            );

            return;
        }

        ServerProxy proxy = temporaryProxies.remove(temporaryId);

        if (waitingRooms.isEmpty())
            waitingRooms.add(new WaitingRoom());

        WaitingRoom waitingRoom = waitingRooms.get(waitingRooms.size() - 1);

        waitingRoom.addPlayer(proxy, nickname, votedMap, votedSkulls);

        proxy.sendQuestionEvent(new AddedToWaitingRoomQuestion(waitingRoom.players));


    }

    /**
     * Checks if the hashmap already contains the given username to validate it
     * @param nickname the username
     * @return
     */
    private boolean notAValidUsername(String nickname) {

        return  nicknamesControllers.containsKey(nickname);

    }


    @Override
    public void handleEvent(ActionAttackAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");
    }

    @Override
    public void handleEvent(ActionEndTurnAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ActionMoveAndGrabAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ActionMoveAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ActionPickWeaponAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ActionReloadAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ActionRespawnAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ActionUseTurnPowerUpAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChooseHowToPayAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChooseHowToShootAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChooseHowToUseTurnPowerUpAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChooseIfToUseAsyncPowerUpAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChoosePowerUpToRespawnAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChoosePowerUpToUseAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChooseWeaponToAttackAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChooseWeaponToPickAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChooseWeaponToReloadAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(ChooseWeaponToSwitchAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(WhereToMoveAndGrabAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }

    @Override
    public void handleEvent(WhereToMoveAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");

    }
}
