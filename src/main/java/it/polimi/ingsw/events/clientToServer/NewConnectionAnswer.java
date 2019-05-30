package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.model.map.MapName;

import java.io.Serializable;

public class NewConnectionAnswer implements AnswerEvent, Serializable {

    public Integer temporaryId;

    public String nickname;

    public MapName votedMap;

    public int votedSkulls;

    public NewConnectionAnswer(Integer temporaryId, String nickname, MapName votedMap, int votedSkulls) {
        this.nickname = nickname;
        this.votedMap = votedMap;
        this.votedSkulls = votedSkulls;
        this.temporaryId = temporaryId;
    }

    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
