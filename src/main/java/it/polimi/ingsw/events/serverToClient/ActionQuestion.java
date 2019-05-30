package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.List;

public class ActionQuestion implements QuestionEvent, Serializable {

    public List<String> possibleAction;

    public ActionQuestion(List<String> possibleActions) {
        this.possibleAction = possibleActions;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
