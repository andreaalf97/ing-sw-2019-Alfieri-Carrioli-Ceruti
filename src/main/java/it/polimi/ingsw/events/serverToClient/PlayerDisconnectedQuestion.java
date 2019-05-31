package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class PlayerDisconnectedQuestion implements QuestionEvent, Serializable {

    public String nickname;

    public PlayerDisconnectedQuestion(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
