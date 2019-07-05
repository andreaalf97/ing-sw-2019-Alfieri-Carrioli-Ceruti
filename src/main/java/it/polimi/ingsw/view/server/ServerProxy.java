package it.polimi.ingsw.view.server;

import it.polimi.ingsw.events.QuestionEvent;

public interface ServerProxy {

    void sendQuestionEvent(QuestionEvent questionEvent);

    void setNickname(String nickname);

    void setReceiver(AnswerEventReceiver receiver);

    void close();

}
