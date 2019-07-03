package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.server.AnswerEventReceiver;
import it.polimi.ingsw.view.server.ServerProxy;
import it.polimi.ingsw.view.server.VirtualView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * This hashmap contains all the usernames that belong to a paused game
     */
    private static Map<String, String> pausedUsernames;

    /**
     * The waiting room for players who are trying to restart a game
     */
    private static PGWaitingRoom pgWaitingRoom;

    /**
     * The id we are giving to every game
     */
    private static int nextId;

    private static final String JSONfolder = "src/main/resources/JSONsnapshots";


    GamesHandler() {

        this.waitingRooms = new ArrayList<>();
        this.nicknamesControllers = new HashMap<>();
        this.temporaryProxies = new HashMap<>();
        this.pausedUsernames = new HashMap<>();
        this.pgWaitingRoom = null;
        this.nextId = 0;

        File dir = new File(JSONfolder);

        for(File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();



    }

    public static void reloadGame(String jsonPath, ArrayList<String> reconnectingPlayers, ArrayList<ServerProxy> proxies) {

        Game game = new Game(jsonPath);

        VirtualView virtualView = new VirtualView(reconnectingPlayers, proxies);

        Controller controller = new Controller(game, virtualView);

        virtualView.addObserver(controller);
        game.addObserver(virtualView);

        game.forceNotify();

        //Adding each player to the PLAYER - CONTROLLER map
        for (String player : reconnectingPlayers)
            nicknamesControllers.put(player, controller);

        for (ServerProxy proxy : proxies)
            proxy.setReceiver(controller.virtualView);

        pgWaitingRoom = null;

        controller.restartGame();

    }

    public static void pauseGame(ArrayList<String> nicknames, String jsonPath) {

        for(String i : nicknames) {
            nicknamesControllers.remove(i);
            pausedUsernames.put(i, jsonPath);
        }

    }

    synchronized boolean isAValidTemporaryId(Integer id) {

        return !temporaryProxies.containsKey(id);

    }

    void addTemporaryId(Integer id, ServerProxy proxy) {

        temporaryProxies.put(id, proxy);

    }

    static void startGame(WaitingRoom waitingRoom) {

        MapName votedMap = waitingRoom.getVotedMap();
        int votedSkulls = waitingRoom.getVotedSkulls();

        //Creates a new Game
        Game game = new Game(waitingRoom.players, votedMap, votedSkulls, nextId);
        nextId++;

        //Creates a new virtual view
        VirtualView virtualView = new VirtualView(waitingRoom.players, waitingRoom.serverProxies);

        //Creates a new controller
        Controller controller = new Controller(game, virtualView);

        //Setting up the OBSERVER - OBSERVABLE pattern
        virtualView.addObserver(controller);
        game.addObserver(virtualView);

        game.forceNotify();

        //Adding each player to the PLAYER - CONTROLLER map
        for (String player : waitingRoom.players)
            nicknamesControllers.put(player, controller);

        for (ServerProxy proxy : waitingRoom.serverProxies)
            proxy.setReceiver(controller.virtualView);

        //Deleting the waiting room
        waitingRooms.remove(waitingRoom);

        //Starting the game
        controller.startGame(votedMap, votedSkulls);

    }

    /**
     * This method is called if the room has not been filled in time
     *
     * @param waitingRoom the room to delete
     */
    static void roomNotFilledInTime(WaitingRoom waitingRoom) {

        System.out.println("Deleting " + waitingRoom + " due to lack of players");

        for (ServerProxy p : waitingRoom.serverProxies) {

            p.sendQuestionEvent(
                    new TextMessage("You are being disconnected because the room didn't fill in time")
            );

            p.sendQuestionEvent(
                    new DisconnectedQuestion()
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

        //Reading all info from the event
        Integer temporaryId = event.temporaryId;

        String nickname = event.nickname;

        MapName votedMap = event.votedMap;

        int votedSkulls = event.votedSkulls;

        ServerProxy proxy = temporaryProxies.get(temporaryId);



        //This means the player was already playing in a game
        //Reinserting player in an active game
        if (nicknamesControllers.containsKey(nickname)) {
            Controller controller = nicknamesControllers.get(nickname);

            proxy.setNickname(nickname);
            proxy.setReceiver(controller.virtualView);

            proxy.sendQuestionEvent(new TextMessage("You have been inserted back into your game"));

            controller.reinsert(nickname, proxy);
            return;
        }

        //This means the player has chosen an username which was already connected into a waiting room, so he has to choose a new username
        for (WaitingRoom w : waitingRooms) {
            if (w.players.contains(nickname)) {
                proxy.sendQuestionEvent(
                        new InvalidUsernameQuestion()
                );
                return;
            }
        }

        temporaryProxies.remove(proxy);

        proxy.setNickname(nickname);

        if(pausedUsernames.containsKey(nickname)){

            newPausedGameConnection(nickname, pausedUsernames.get(nickname), proxy);
            return;

        }

        if (waitingRooms.isEmpty())
            waitingRooms.add(new WaitingRoom());

        WaitingRoom waitingRoom = waitingRooms.get(waitingRooms.size() - 1);

        waitingRoom.addPlayer(proxy, nickname, votedMap, votedSkulls);

        proxy.sendQuestionEvent(new AddedToWaitingRoomQuestion(waitingRoom.players));


    }

    private void newPausedGameConnection(String nickname, String jsonPath, ServerProxy proxy) {

        System.err.println("" + nickname + " is trying to restart a game --> " + jsonPath);

        if(this.pgWaitingRoom == null){
            this.pgWaitingRoom = new PGWaitingRoom(jsonPath);
        }

        boolean added = pgWaitingRoom.addPlayer(nickname, jsonPath, proxy);

        if(!added){
            proxy.sendQuestionEvent(new TextMessage("Other players are trying to reload their game, try again later"));
            return;
        }

    }

    @Override
    public void handleEvent(DisconnectedAnswer event) {

        temporaryProxies.remove(Integer.parseInt(event.nickname));
        System.out.println("Removing temporary ID " + event.nickname + " from temporaryProxies hash map");

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
    public void handleEvent(ChooseHowToPayToPickWeaponAnswer event) {
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

    @Override
    public void handleEvent(RefreshPossibleActionsAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");
    }

    @Override
    public void handleEvent(AskOrderAndDefenderAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");
    }

    @Override
    public void handleEvent(ChooseHowToPayForAttackingAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");
    }

    @Override
    public void handleEvent(ChooseHowToPayToSwitchWeaponsAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");
    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");
    }

    @Override
    public void handleEvent(Ping event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");
    }

    @Override
    public void handleEvent(UseGrenadeAnswer event) {
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");
    }

    @Override
    public void handleEvent(RefreshPossibleActionsAfterReloadingAnswer event){
        throw new RuntimeException("The GamesHandler received an unexpected event during connection");
    }

}
