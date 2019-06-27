package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class AskOrderAndDefenderAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String chosenWeapon;

    public Integer[] order;

    public ArrayList<String> defenders;

    public AskOrderAndDefenderAnswer(String nickname, String chosenWeapon, Integer[] chosenOrder, ArrayList<String> defenders) {
        this.nickname = nickname;
        this.chosenWeapon = chosenWeapon;
        this.order = chosenOrder;
        this.defenders = defenders;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
