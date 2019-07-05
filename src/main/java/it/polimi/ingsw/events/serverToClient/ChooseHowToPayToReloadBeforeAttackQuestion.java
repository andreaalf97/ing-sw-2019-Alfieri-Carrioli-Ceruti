package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseHowToPayToReloadBeforeAttackQuestion implements QuestionEvent, Serializable {

    public String weaponToReload;

    public ArrayList<Color> cost;

    public ArrayList<String> weaponsLoaded;

    public ChooseHowToPayToReloadBeforeAttackQuestion(String weaponToReload, ArrayList<Color> cost, ArrayList<String> weaponsLoaded){
        this.weaponToReload = weaponToReload;

        this.cost = cost;

        this.weaponsLoaded = weaponsLoaded;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
