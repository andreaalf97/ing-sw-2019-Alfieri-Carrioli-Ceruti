package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseHowToPayToReloadQuestion implements QuestionEvent, Serializable {


    public String weaponToReload;

    public List<String> paymentChoices;

    public ChooseHowToPayToReloadQuestion(String weaponToReload, ArrayList<String> paymentChoices) {

        this.weaponToReload = weaponToReload;
        this.paymentChoices = paymentChoices;

    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
