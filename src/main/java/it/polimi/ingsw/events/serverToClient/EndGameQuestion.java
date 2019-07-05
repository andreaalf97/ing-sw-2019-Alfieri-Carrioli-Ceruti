package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;

public class EndGameQuestion implements QuestionEvent, Serializable {

    public String winner;

    public int winnerPoints;

    public EndGameQuestion(String winner, int winnerPoints){
        this.winner = winner;
        this.winnerPoints = winnerPoints;
    }



    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
