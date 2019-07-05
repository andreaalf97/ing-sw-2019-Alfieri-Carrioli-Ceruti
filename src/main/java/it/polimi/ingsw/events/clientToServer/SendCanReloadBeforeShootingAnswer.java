package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class SendCanReloadBeforeShootingAnswer implements AnswerEvent, Serializable {


    public String nickname;

    public String weaponToReload;

    public ArrayList<String> weaponsLoaded;

    public SendCanReloadBeforeShootingAnswer(String nickname, String weaponToReload, ArrayList<String> weaponsLoaded){
        this.nickname = nickname;
        this.weaponToReload = weaponToReload;
        this.weaponsLoaded = weaponsLoaded;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
