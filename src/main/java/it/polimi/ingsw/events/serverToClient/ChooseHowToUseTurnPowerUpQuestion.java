package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;

public class ChooseHowToUseTurnPowerUpQuestion implements QuestionEvent, Serializable {

    public String powerUpToUseName;

    public Color powerUpToUseColor;

    public ChooseHowToUseTurnPowerUpQuestion(String powerUpToUse, Color color) {
        this.powerUpToUseName = powerUpToUse;
        this.powerUpToUseColor = color;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
