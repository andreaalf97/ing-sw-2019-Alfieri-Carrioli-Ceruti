package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;
import java.util.List;

public class ChooseHowToShootAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String weapon;

    public List<String> defenders;

    public List<String> movers;

    public List<Integer> xCoords;

    public List<Integer> yCoords;

    public int orderNumber;

    public ChooseHowToShootAnswer(String nickname, String weapon, List<String> defenders, List<String> mover, List<Integer> xCoords, List<Integer> yCoords, int orderNumber) {
        this.nickname = nickname;
        this.weapon = weapon;
        this.defenders = defenders;
        this.movers = mover;
        this.xCoords = xCoords;
        this.yCoords = yCoords;
        this.orderNumber = orderNumber;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
