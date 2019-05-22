package it.polimi.ingsw.server;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.QuestionType;
import it.polimi.ingsw.view.ServerQuestion;
import it.polimi.ingsw.view.server.VirtualView;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class waits for user input and sends it to the right controller
 * It associate every nickname to a specific Receiver and Controller
 * Every Receiver has 1 nickname
 * Every Controller has multiple nicknames
 */
public class GamesHandler implements Observer {

    /**
     * A map that connects every nickname to its Controller
     */
    private Map<String, Controller> nicknameControllers;

    ServerProxy serverProxy;

    static final String SPLITTER = "$";

    protected GamesHandler() {
        this.nicknameControllers = new HashMap<>();
        this.serverProxy = new ServerProxy();
    }


    public void startGame(WaitingRoom waitingRoom) {
        Game game = new Game(waitingRoom.players, waitingRoom.getVotedMap(), waitingRoom.getVotedSkulls());

        VirtualView virtualView = new VirtualView(waitingRoom.players, serverProxy);

        Controller controller = new Controller(game, virtualView);

        virtualView.addObserver(controller);
        game.addObserver(virtualView);

        for(String player : waitingRoom.players)
            nicknameControllers.put(player, controller);


        controller.startGame();

    }

    @Override
    public void notifyObserver(Object obj){

        if(! (obj instanceof String))
            throw new RuntimeException("This should be a String object");

        String nicknameAndAnswer = (String) obj;

        if(nicknameAndAnswer.split(SPLITTER).length == 1){
            String nickname = nicknameAndAnswer.split(SPLITTER)[0];
            lostConnection(nickname);
        }

        String nickname = nicknameAndAnswer.split(SPLITTER)[0];
        String answer = nicknameAndAnswer.split(SPLITTER)[1];

        Controller controller = nicknameControllers.get(nickname);

        controller.virtualView.notify(nickname, answer);
    }


    private void lostConnection(String nickname) {

        Controller controller = nicknameControllers.get(nickname);

        ArrayList<String> messages = new ArrayList<>();
        messages.add(nickname + " DISCONNECTED");

        controller.virtualView.sendAll(new ServerQuestion(QuestionType.TextMessage, messages));

        System.out.println("LOST CONNECTION with " + nickname);
    }

    /**
     * Reinserts the player to the correct game
     * @param nickname
     */
    public void reinsert(Receiver receiver, String nickname) {

        int index = nickname.indexOf(nickname);

        //receivers.set(index, receiver);

        nicknameControllers.get(nickname).reinsert(nickname, receiver);

    }

    public void addSocket(String nickname, Socket socket) {

        //TODO
        //serverProxy.addSocket(nickname, socket);

    }

    public void removeWaitingRoomPlayer(String player) {

        //TODO
        //This should send a goodbye to the player and close the connection
        //serverProxy.removeWaitingRoomPlayer(player);

    }
}
