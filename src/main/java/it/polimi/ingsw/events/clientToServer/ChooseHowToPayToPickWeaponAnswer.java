package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;
import java.util.List;

public class ChooseHowToPayToPickWeaponAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String weaponToPick;

    public List<String> chosenPayment;

    public ChooseHowToPayToPickWeaponAnswer(String nickname, String weaponToPick, List<String> chosenPayment) {
        this.nickname = nickname;
        this.weaponToPick = weaponToPick;
        this.chosenPayment = chosenPayment;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
