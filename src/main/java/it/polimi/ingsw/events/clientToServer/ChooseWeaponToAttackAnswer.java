package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class ChooseWeaponToAttackAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String chosenWeapon;

    public ChooseWeaponToAttackAnswer(String nickname, String chosenWeapon) {
        this.nickname = nickname;
        this.chosenWeapon = chosenWeapon;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
