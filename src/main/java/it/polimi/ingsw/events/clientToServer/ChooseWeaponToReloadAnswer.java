package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;

public class ChooseWeaponToReloadAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String weaponToReload;

    public ChooseWeaponToReloadAnswer(String nickname, String weaponToReload) {
        this.nickname = nickname;
        this.weaponToReload = weaponToReload;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
