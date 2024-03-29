package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class ChooseWeaponToPickAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String weaponToPick;

    public ChooseWeaponToPickAnswer(String nickname, String weaponToPick) {
        this.nickname = nickname;
        this.weaponToPick = weaponToPick;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
