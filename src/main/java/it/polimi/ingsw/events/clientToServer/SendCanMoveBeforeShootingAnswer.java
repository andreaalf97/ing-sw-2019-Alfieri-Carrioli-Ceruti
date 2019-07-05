package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class SendCanMoveBeforeShootingAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public int x;

    public int y;

    public ArrayList<String> weaponsLoaded;

    public SendCanMoveBeforeShootingAnswer(String nickname, int x, int y, ArrayList<String> weaponsLoaded){
        this.nickname = nickname;
        this.x = x;
        this.y = y;
        this.weaponsLoaded = weaponsLoaded;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
