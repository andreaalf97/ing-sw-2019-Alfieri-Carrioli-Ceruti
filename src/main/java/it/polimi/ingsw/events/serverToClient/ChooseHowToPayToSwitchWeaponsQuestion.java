package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseHowToPayToSwitchWeaponsQuestion implements QuestionEvent, Serializable {

    public String weaponToPick;

    public List<Color> weaponCost;

    public String weaponToDiscard;

    public ChooseHowToPayToSwitchWeaponsQuestion(String weaponToPick, ArrayList<Color> weaponCost, String weaponToDiscard) {
        this.weaponToPick = weaponToPick;
        this.weaponCost = weaponCost;
        this.weaponToDiscard = weaponToDiscard;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
