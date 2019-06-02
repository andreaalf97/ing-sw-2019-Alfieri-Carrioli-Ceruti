package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseHowToShootQuestion implements QuestionEvent, Serializable {

    public String chosenWeapon;

    public ArrayList<int[]> possibleOrders;

    public boolean needsMovement;

    public ChooseHowToShootQuestion(String chosenWeapon, ArrayList<int[]> possibleOrders, boolean needsMovement) {
        this.chosenWeapon = chosenWeapon;
        this.possibleOrders = possibleOrders;
        this.needsMovement = needsMovement;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
