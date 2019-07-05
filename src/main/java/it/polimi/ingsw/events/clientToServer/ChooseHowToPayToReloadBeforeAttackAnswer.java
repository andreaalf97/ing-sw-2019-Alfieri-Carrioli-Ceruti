package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseHowToPayToReloadBeforeAttackAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String weaponToReload;

    public ArrayList<String> chosenPayment;

    public ArrayList<String> weaponsLoaded;

    public ChooseHowToPayToReloadBeforeAttackAnswer(String nickname, String weaponToReload, ArrayList<String> chosenPayment, ArrayList<String> weaponsLoaded){
        this.nickname = nickname;
        this.weaponToReload = weaponToReload;
        this.chosenPayment = chosenPayment;
        this.weaponsLoaded = weaponsLoaded;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
