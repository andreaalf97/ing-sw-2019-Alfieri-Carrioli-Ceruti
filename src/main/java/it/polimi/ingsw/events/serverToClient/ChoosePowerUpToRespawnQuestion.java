package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.List;

public class ChoosePowerUpToRespawnQuestion implements QuestionEvent, Serializable {

    public List<String> powerUpToRespawn;

    public List<Color> colors;

    public ChoosePowerUpToRespawnQuestion(List<String> powerUpToRespawn) {
        this.powerUpToRespawn = powerUpToRespawn;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
