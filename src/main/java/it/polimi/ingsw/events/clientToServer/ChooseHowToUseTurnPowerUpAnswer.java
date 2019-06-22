package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;

public class ChooseHowToUseTurnPowerUpAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String powerUpToUse;

    public Color powerUpColor;

    public String movers;

    public int x;

    public int y;

    public ChooseHowToUseTurnPowerUpAnswer(String nickname, String powerUpToUse,Color color, String movers, int x, int y) {
        this.nickname = nickname;
        this.powerUpToUse = powerUpToUse;
        this.powerUpColor = color;
        this.movers = movers;
        this.x = x;
        this.y = y;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
