package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class ChooseWeaponToSwitchAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String weaponToDiscard;

    public String weaponToPick;

    public ChooseWeaponToSwitchAnswer(String nickname, String weaponToDiscard, String weaponToPick) {
        this.nickname = nickname;
        this.weaponToDiscard = weaponToDiscard;
        this.weaponToPick = weaponToPick;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
