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

    public int[] chosenOrder;

    public ChooseHowToShootAnswer(String nickname, int[] chosenOrder, String weapon, List<String> defenders, List<String> mover, List<Integer> xCoords, List<Integer> yCoords) {
        this.nickname = nickname;
        this.weapon = weapon;
        this.defenders = defenders;
        this.movers = mover;
        this.xCoords = xCoords;
        this.yCoords = yCoords;
        this.chosenOrder = chosenOrder;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
