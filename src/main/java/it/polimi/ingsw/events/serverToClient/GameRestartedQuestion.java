package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.map.MapName;

import java.io.Serializable;
import java.util.ArrayList;

public class GameRestartedQuestion implements QuestionEvent, Serializable {

    public ArrayList<String> playerNames;

    public ArrayList<PlayerColor> colors;

    public int kstSkulls;

    public MapName mapName;

    public GameRestartedQuestion(ArrayList<String> playerNames, ArrayList<PlayerColor> colors, int kstSkulls, MapName mapName) {
        this.playerNames = playerNames;
        this.colors = colors;
        this.kstSkulls = kstSkulls;
        this.mapName = mapName;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
