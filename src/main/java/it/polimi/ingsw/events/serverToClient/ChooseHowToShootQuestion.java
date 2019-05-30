package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class ChooseHowToShootQuestion implements QuestionEvent, Serializable {

    public String chosenWeapon;

    public ChooseHowToShootQuestion(String chosenWeapon) {
        this.chosenWeapon = chosenWeapon;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
