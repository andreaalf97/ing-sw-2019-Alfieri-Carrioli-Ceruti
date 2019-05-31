package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.List;

public class ChooseHowToPayToPickWeaponQuestion implements QuestionEvent, Serializable {

    public String weaponName;

    public List<Color> cost;

    public ChooseHowToPayToPickWeaponQuestion(String weaponToPick, List<Color> cost) {
        this.weaponName = weaponToPick;
        this.cost = cost;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
