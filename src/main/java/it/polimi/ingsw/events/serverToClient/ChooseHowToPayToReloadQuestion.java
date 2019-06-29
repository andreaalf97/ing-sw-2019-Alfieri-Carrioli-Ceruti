package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseHowToPayToReloadQuestion implements QuestionEvent, Serializable {


    public String weaponToReload;

    public List<Color> cost;

    public ChooseHowToPayToReloadQuestion(String weaponToReload, List<Color> cost) {
        this.weaponToReload = weaponToReload;
        this.cost = cost;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
