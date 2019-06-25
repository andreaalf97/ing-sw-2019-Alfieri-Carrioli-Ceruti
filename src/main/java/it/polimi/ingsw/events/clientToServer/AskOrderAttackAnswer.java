package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class AskOrderAttackAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String chosenWeapon;

    public Integer[] order;

    public AskOrderAttackAnswer(String nickname, String chosenWeapon,Integer[] chosenOrder) {
        this.nickname = nickname;
        this.chosenWeapon = chosenWeapon;
        this.order = chosenOrder;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
