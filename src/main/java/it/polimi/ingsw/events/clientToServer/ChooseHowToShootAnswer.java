package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseHowToShootAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String weapon;

    public ArrayList<String> defenders;

    public ArrayList<String> movers;

    public ArrayList<Integer> xCoords;

    public ArrayList<Integer> yCoords;

    public Integer[] chosenOrder;

    public int indexOfLastEffectUsed;

    public String defenderToApplyTargeting;

    public ChooseHowToShootAnswer(String nickname, Integer[] chosenOrder, String weapon, ArrayList<String> defenders, ArrayList<String> mover, ArrayList<Integer> xCoords, ArrayList<Integer> yCoords, int indexOfLastEffectUsed, String defenderToApplyTargeting) {
        this.nickname = nickname;
        this.weapon = weapon;
        this.defenders = defenders;
        this.movers = mover;
        this.xCoords = xCoords;
        this.yCoords = yCoords;
        this.chosenOrder = chosenOrder;
        this.indexOfLastEffectUsed = indexOfLastEffectUsed;
        this.defenderToApplyTargeting = defenderToApplyTargeting;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
