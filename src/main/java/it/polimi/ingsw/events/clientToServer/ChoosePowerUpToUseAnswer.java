package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;

public class ChoosePowerUpToUseAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String powerUpToUse;

    public Color color;

    public ChoosePowerUpToUseAnswer(String nickname, String powerUpToUse, Color color) {
        this.nickname = nickname;
        this.powerUpToUse = powerUpToUse;
        this.color = color;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
