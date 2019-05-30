package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.List;

public class ChooseWeaponToReloadQuestion implements QuestionEvent, Serializable {

    public List<String> weaponsToReload;

    public ChooseWeaponToReloadQuestion(List<String> weaponsToReload) {
        this.weaponsToReload = weaponsToReload;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
