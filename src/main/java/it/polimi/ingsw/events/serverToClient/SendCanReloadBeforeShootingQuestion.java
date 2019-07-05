package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class SendCanReloadBeforeShootingQuestion implements QuestionEvent, Serializable {

    public ArrayList<String> rechargeableWeaponNames;

    public ArrayList<String> weaponsLoaded;

    public SendCanReloadBeforeShootingQuestion(ArrayList<String> rechargeableWeaponNames, ArrayList<String> weaponsLoaded){
        this.rechargeableWeaponNames = rechargeableWeaponNames;
        this.weaponsLoaded = weaponsLoaded;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
