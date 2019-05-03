package it.polimi.ingsw.main;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.view.server.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class waits for user input and sends it to the right controller
 */
public class MessageParser implements Questioner {

    Map<String, Controller> nicknameControllers;

    ArrayList<Receiver> receivers;

    ArrayList<String> nicknames;

    private int nControllers;

    public MessageParser(){
        this.nicknameControllers = new HashMap<>();
        this.receivers = new ArrayList<>();
        this.nicknames = new ArrayList<>();
        this.nControllers = 0;
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

        nControllers++;

    }

    @Override
    public void answer(String nickname, String answer) {
        Controller controller = nicknameControllers.get(nickname);

        controller.virtualView.notify(nickname, answer);
    }

    @Override
    public void lostConnection(String nickname) {
        System.out.println("LOST CONNECTION");
    }
}
