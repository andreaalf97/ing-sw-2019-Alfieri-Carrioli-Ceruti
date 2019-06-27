package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class AskOrderAndDefenderQuestion implements QuestionEvent, Serializable {

    public String chosenWeapon;

    public ArrayList<Integer[]> possibleOrders;

    public AskOrderAndDefenderQuestion(String chosenWeapon, ArrayList<Integer[]> possibleOrders) {
        this.chosenWeapon = chosenWeapon;
        this.possibleOrders = possibleOrders;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
