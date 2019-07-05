package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseWeaponToAttackQuestion implements QuestionEvent, Serializable {

    public ArrayList<String> weaponsLoaded;

    public ChooseWeaponToAttackQuestion(ArrayList<String> weaponsLoaded) {

        this.weaponsLoaded = weaponsLoaded;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
