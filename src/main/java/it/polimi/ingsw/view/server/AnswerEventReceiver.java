package it.polimi.ingsw.view.server;

import it.polimi.ingsw.events.AnswerEvent;

public interface AnswerEventReceiver {

    void receiveAnswer(AnswerEvent answerEvent);

}
