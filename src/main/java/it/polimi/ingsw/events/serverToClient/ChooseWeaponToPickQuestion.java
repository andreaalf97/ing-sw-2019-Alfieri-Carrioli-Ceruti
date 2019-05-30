package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.List;

public class ChooseWeaponToPickQuestion implements QuestionEvent, Serializable {

    public List<String> weaponsToPick;

    public ChooseWeaponToPickQuestion(List<String> weaponsToPick) {
        this.weaponsToPick = weaponsToPick;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
