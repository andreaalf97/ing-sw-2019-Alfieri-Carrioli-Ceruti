package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseHowToPayToReloadAnswer implements AnswerEvent, Serializable {

    public String nickname;
    public String weapon;
    public ArrayList<String> chosenPayment;

    public ChooseHowToPayToReloadAnswer(String nickname, String weapon, ArrayList<String> chosenPayment) {
        this.nickname = nickname;
        this.weapon = weapon;
        this.chosenPayment = chosenPayment;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }



}
