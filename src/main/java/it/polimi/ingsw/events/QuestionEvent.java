package it.polimi.ingsw.events;

import it.polimi.ingsw.client.QuestionEventHandler;

public interface QuestionEvent {

    void acceptEventHandler(QuestionEventHandler handler);

}
