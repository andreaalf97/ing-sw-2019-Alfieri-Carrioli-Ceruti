package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class ModelUpdate implements QuestionEvent, Serializable {
    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {

    }
}
