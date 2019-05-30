package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class WhereToMoveQuestion implements QuestionEvent, Serializable {

    public boolean[][] possibleSpots;

    public WhereToMoveQuestion(boolean[][] possibleSpots) {
        this.possibleSpots = possibleSpots;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
