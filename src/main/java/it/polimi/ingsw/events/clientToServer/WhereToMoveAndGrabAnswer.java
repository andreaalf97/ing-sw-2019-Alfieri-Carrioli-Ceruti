package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class WhereToMoveAndGrabAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public int xCoord;

    public int yCoord;

    public WhereToMoveAndGrabAnswer(String nickname, int xCoord, int yCoord) {
        this.nickname = nickname;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
