package it.polimi.ingsw.view.client;

import it.polimi.ingsw.events.AnswerEvent;

public interface RemoteView {

    void sendAnswerEvent(AnswerEvent event);

}
