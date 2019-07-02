package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class UseGrenadeQuestion implements QuestionEvent, Serializable {

    public String offender;

    public UseGrenadeQuestion(String offender) {
        this.offender = offender;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }


}
