package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class DisconnectedAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public boolean comingFromPingChecker;

    public DisconnectedAnswer(String nickname, boolean comingFromPingChecker) {
        this.nickname = nickname;
        this.comingFromPingChecker = comingFromPingChecker;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
