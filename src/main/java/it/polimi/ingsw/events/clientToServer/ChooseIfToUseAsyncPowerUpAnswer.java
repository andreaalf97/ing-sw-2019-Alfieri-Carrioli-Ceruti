package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.serverToClient.ChooseIfToUseAsyncPowerUpQuestion;

import java.io.Serializable;

public class ChooseIfToUseAsyncPowerUpAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String powerUpName;

    public boolean answer;

    public ChooseIfToUseAsyncPowerUpAnswer(String nickname, boolean answer, ChooseIfToUseAsyncPowerUpQuestion event) {
        this.nickname = nickname;
        this.powerUpName = event.powerUpName;
        this.answer = answer;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
