package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.view.server.VirtualView;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class waits for user input and sends it to the right controller
 */
public class Proxy  {

    Map<String, Controller> nicknameControllers;

    ArrayList<Socket> sockets;

    ArrayList<String> nicknames;

    public Proxy(){
        this.nicknameControllers = new HashMap<>();
        this.sockets = new ArrayList<>();
        this.nicknames = new ArrayList<>();
    }


    public boolean hasNickname(String username) {
        return nicknames.contains(username);
    }

    public void startGame(WaitingRoom waitingRoom) {
        Game game = new Game(waitingRoom.players, waitingRoom.getVotedMap(), waitingRoom.getVotedSkulls());

        VirtualView virtualView = new VirtualView(waitingRoom.players);

        Controller controller = new Controller(game, virtualView);

        GameView gameView = new GameView();

        virtualView.addObserver(controller);
        gameView.addObserver(virtualView);

        for(String player : waitingRoom.players) {
            nicknames.add(player);
            nicknameControllers.put(player, controller);
            sockets.add(waitingRoom.sockets.get(waitingRoom.players.indexOf(player)));
        }

    }
}
