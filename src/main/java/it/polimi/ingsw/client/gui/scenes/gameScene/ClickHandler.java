package it.polimi.ingsw.client.gui.scenes.gameScene;

import java.util.List;

public abstract class ClickHandler {

    List<String> waitingList;

    ClickHandler(List<String> waitingList){
        this.waitingList = waitingList;
    }

    abstract void newClick(String id);

}
