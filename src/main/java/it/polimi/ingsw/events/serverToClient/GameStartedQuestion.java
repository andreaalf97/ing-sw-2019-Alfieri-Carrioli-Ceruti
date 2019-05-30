package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class GameStartedQuestion implements QuestionEvent, Serializable {

    public ArrayList<String> playerNames;

    public String firstPlayer;

    public GameStartedQuestion(ArrayList<String> playerNames, String firstPlayer) {
        this.playerNames = playerNames;
        this.firstPlayer = firstPlayer;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
