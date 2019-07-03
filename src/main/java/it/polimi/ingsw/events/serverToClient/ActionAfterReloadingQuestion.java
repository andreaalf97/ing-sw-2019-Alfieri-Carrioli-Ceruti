package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.List;

public class ActionAfterReloadingQuestion implements QuestionEvent, Serializable {

    public List<String> possibleAction;

    public ActionAfterReloadingQuestion(List<String> possibleActions) {
        this.possibleAction = possibleActions;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
