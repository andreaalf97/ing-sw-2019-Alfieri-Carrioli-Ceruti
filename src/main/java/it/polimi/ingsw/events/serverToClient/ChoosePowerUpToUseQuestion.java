package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.List;

public class ChoosePowerUpToUseQuestion implements QuestionEvent, Serializable {

    public List<String> powerUpNames;

    public List<Color> colors;

    public ChoosePowerUpToUseQuestion(List<String> powerUpNames, List<Color> colors) {
        this.powerUpNames = powerUpNames;
        this.colors = colors;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
