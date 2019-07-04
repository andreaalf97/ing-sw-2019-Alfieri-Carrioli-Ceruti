package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.MapName;

import java.io.Serializable;
import java.util.ArrayList;

public class GameStartedQuestion implements QuestionEvent, Serializable {

    public ArrayList<String> playerNames;

    public ArrayList<PlayerColor> playerColors;

    public String firstPlayer;

    public MapName mapName;

    public int votedSkulls;

    public GameStartedQuestion(ArrayList<String> playerNames, ArrayList<PlayerColor> playerColors, String firstPlayer, MapName mapName, int votedSkulls) {
        this.playerNames = playerNames;
        this.playerColors = playerColors;
        this.firstPlayer = firstPlayer;
        this.mapName = mapName;
        this.votedSkulls = votedSkulls;
    }

    public GameStartedQuestion(GameRestartedQuestion event) {

        this.playerNames = event.playerNames;
        this.playerColors = event.colors;
        this.firstPlayer = event.playerNames.get(0);
        this.mapName = event.mapName;
        this.votedSkulls = event.kstSkulls;

    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
