package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;

public class ChoosePowerUpToRespawnAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String powerUpToRespawn;

    public Color color;

    public ChoosePowerUpToRespawnAnswer(String nickname, String powerUpToRespawn, Color color) {
        this.nickname = nickname;
        this.powerUpToRespawn = powerUpToRespawn;
        this.color = color;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
