package it.polimi.ingsw.main;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.view.server.VirtualView;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class waits for user input and sends it to the right controller
 * It associate every nickname to a specific Receiver and Controller
 * Every Receiver has 1 nickname
 * Every Controller has multiple nicknames
 */
public class GamesHandler implements Questioner {

    /**
     * A map that connects every nickname to its Controller
     */
    private Map<String, Controller> nicknameControllers;

    private ArrayList<Receiver> receivers;

    private ArrayList<String> nicknames;

    public GamesHandler(){
        this.nicknameControllers = new HashMap<>();
        this.receivers = new ArrayList<>();
        this.nicknames = new ArrayList<>();
    }


    public boolean hasNickname(String username) {
        return nicknames.contains(username);
    }

    public void startGame(WaitingRoom waitingRoom) {
        Game game = new Game(waitingRoom.players, waitingRoom.getVotedMap(), waitingRoom.getVotedSkulls());

        VirtualView virtualView = new VirtualView(waitingRoom.players, waitingRoom.receivers);

        Controller controller = new Controller(game, virtualView);

        GameView gameView = new GameView();

        virtualView.addObserver(controller);
        gameView.addObserver(virtualView);

        for(String player : waitingRoom.players) {
            nicknames.add(player);
            nicknameControllers.put(player, controller);

            Receiver receiver = waitingRoom.receivers.get(waitingRoom.players.indexOf(player));

            receivers.add(receiver);

            Thread t = new Thread(receiver);
            t.start();
        }

        controller.virtualView.sendAll("GAME STARTED");

    }

    @Override
    public void answer(String nickname, String answer) {
        Controller controller = nicknameControllers.get(nickname);

        controller.virtualView.notify(nickname, answer);
    }


    @Override
    public void lostConnection(String nickname) {

        Controller controller = nicknameControllers.get(nickname);
        controller.virtualView.sendAll(nickname + " DISCONNECTED");

        System.out.println("LOST CONNECTION with " + nickname);
    }

    /**
     * Reinserts the player to the correct game
     * @param nickname
     */
    public void reinsert(Receiver receiver, String nickname) {

        int index = nickname.indexOf(nickname);

        receivers.set(index, receiver);

        nicknameControllers.get(nickname).reinsert(nickname, receiver);

    }
}
