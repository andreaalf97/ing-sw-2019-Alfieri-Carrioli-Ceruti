package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class UseGrenadeAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String offender;

    public UseGrenadeAnswer(String nickname, String offender) {
        this.offender = offender;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
