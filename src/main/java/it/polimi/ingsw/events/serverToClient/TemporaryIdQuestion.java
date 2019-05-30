package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class TemporaryIdQuestion implements QuestionEvent, Serializable {

    public int temporaryId;

    public TemporaryIdQuestion(Integer temporaryId) {
        this.temporaryId = temporaryId;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
