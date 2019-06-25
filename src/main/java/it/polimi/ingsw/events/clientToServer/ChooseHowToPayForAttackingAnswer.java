package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class ChooseHowToPayForAttackingAnswer implements AnswerEvent, Serializable {

    public ChooseHowToShootAnswer chooseHowToShootAnswer;

    public ArrayList<String> paymentChosen;

    public ChooseHowToPayForAttackingAnswer(ChooseHowToShootAnswer chooseHowToShootAnswer, ArrayList<String> paymentChosen){
        this.chooseHowToShootAnswer = chooseHowToShootAnswer;
        this.paymentChosen = paymentChosen;
    }
    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
