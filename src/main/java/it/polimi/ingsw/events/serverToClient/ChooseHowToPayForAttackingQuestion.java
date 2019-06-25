package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.ChooseHowToShootAnswer;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseHowToPayForAttackingQuestion implements QuestionEvent, Serializable {

    public ChooseHowToShootAnswer chooseHowToShootAnswer;

    public ArrayList<Color> cost;

    public ChooseHowToPayForAttackingQuestion(ChooseHowToShootAnswer chooseHowToShootAnswer, ArrayList<Color> cost) {
        this.chooseHowToShootAnswer = chooseHowToShootAnswer;
        this.cost = cost;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
