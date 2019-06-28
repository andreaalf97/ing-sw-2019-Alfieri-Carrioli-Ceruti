package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.List;

public class ChooseWeaponToSwitchQuestion implements QuestionEvent, Serializable {

    public List<String> weaponsToPick;

    public List<String> weaponsToRemove;

    public ChooseWeaponToSwitchQuestion(List<String> weaponsToPick, List<String> weaponsToRemove) {
        this.weaponsToPick = weaponsToPick;
        this.weaponsToRemove = weaponsToRemove;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
