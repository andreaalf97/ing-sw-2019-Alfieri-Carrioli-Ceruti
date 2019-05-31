package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class Pong implements AnswerEvent, Serializable {
    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        throw new RuntimeException("This should not be called");
    }
}
