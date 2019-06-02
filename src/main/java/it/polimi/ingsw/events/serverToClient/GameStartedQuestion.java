package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.MapName;

import java.io.Serializable;
import java.util.ArrayList;

public class GameStartedQuestion implements QuestionEvent, Serializable {

    public ArrayList<String> playerNames;

    public String firstPlayer;

    public MapName mapName;

    public int votedSkulls;

    public GameStartedQuestion(ArrayList<String> playerNames, String firstPlayer, MapName mapName, int votedSkulls) {
        this.playerNames = playerNames;
        this.firstPlayer = firstPlayer;
        this.mapName = mapName;
        this.votedSkulls = votedSkulls;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
