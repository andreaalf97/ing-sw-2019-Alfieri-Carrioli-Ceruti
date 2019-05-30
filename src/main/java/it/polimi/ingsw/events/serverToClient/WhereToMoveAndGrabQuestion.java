package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class WhereToMoveAndGrabQuestion implements QuestionEvent, Serializable {

    public boolean[][] possibleSpots;

    public WhereToMoveAndGrabQuestion(boolean[][] possibleSpots) {
        this.possibleSpots = possibleSpots;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
