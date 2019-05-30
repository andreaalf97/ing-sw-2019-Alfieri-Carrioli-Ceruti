package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class ChooseIfToUseAsyncPowerUpQuestion implements QuestionEvent, Serializable {

    public String powerUpName;

    public ChooseIfToUseAsyncPowerUpQuestion(String powerUpName) {
        this.powerUpName = powerUpName;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
