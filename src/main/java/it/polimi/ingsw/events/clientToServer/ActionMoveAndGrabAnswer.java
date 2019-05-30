package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class ActionMoveAndGrabAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public ActionMoveAndGrabAnswer(String username) {
        this.nickname = username;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
