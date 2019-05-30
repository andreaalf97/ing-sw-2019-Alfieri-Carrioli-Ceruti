package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class AddedToWaitingRoomQuestion implements QuestionEvent, Serializable {

    public ArrayList<String> players;

    public AddedToWaitingRoomQuestion(ArrayList<String> players) {
        this.players = players;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
