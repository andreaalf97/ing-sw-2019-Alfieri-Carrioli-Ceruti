package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class WhereToMoveAnswer implements AnswerEvent, Serializable {

    /**
     * The nickname of the player
     */
    public String nickname;

    /**
     * The X coordinate of the spot
     */
    public int xCoord;

    /**
     * The Y coordinate of the spot
     */
    public int yCoord;

    public WhereToMoveAnswer(String nickname, int xCoord, int yCoord) {
        this.nickname = nickname;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
