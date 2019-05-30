package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class ChooseHowToUseTurnPowerUpQuestion implements QuestionEvent, Serializable {

    public String powerUpToUse;

    public ChooseHowToUseTurnPowerUpQuestion(String powerUpToUse) {
        this.powerUpToUse = powerUpToUse;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
