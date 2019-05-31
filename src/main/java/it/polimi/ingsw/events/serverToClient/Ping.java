package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class Ping implements QuestionEvent, Serializable {


    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        throw new RuntimeException("This should not be called");
    }
}
