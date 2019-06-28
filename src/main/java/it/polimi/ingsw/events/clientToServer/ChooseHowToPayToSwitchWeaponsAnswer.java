package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseHowToPayToSwitchWeaponsAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String weaponToPick;

    public ArrayList<String> paymentChoice;

    public String weaponToDiscard;

    public ChooseHowToPayToSwitchWeaponsAnswer(String nickname, String weaponToPick, ArrayList<String> paymentChoice, String weaponToDiscard) {
        this.nickname = nickname;
        this.weaponToPick = weaponToPick;
        this.paymentChoice = paymentChoice;
        this.weaponToDiscard = weaponToDiscard;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
