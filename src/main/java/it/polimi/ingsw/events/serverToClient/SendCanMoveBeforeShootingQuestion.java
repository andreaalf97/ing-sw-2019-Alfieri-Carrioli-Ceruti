package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class SendCanMoveBeforeShootingQuestion implements QuestionEvent, Serializable {
    public boolean[][] allowedSpots;

    public ArrayList<String> weaponsLoaded;

    public SendCanMoveBeforeShootingQuestion(boolean[][] allowedSpots, ArrayList<String> weaponsLoaded){
        this.allowedSpots = allowedSpots;
        this.weaponsLoaded = weaponsLoaded;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
