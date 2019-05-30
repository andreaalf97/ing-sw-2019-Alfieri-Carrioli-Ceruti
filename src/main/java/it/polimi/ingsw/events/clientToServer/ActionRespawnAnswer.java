package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class ActionRespawnAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public ActionRespawnAnswer(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
