package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.List;

public class ChooseWeaponToAttackQuestion implements QuestionEvent, Serializable {

    public List<String> weaponsLoaded;

    public ChooseWeaponToAttackQuestion(List<String> weaponsLoaded) {

        this.weaponsLoaded = weaponsLoaded;

    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
