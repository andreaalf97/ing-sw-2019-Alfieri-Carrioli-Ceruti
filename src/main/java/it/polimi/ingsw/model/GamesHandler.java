package it.polimi.ingsw.model;

import java.util.ArrayList;

public class GamesHandler {
    private ArrayList<Game> games;
    private ArrayList<WaitingRoom> waitingRooms;

    public GamesHandler(){
        this.games = new ArrayList<>();
        this.waitingRooms = new ArrayList<>();
    }

    public void startGame(WaitingRoom room){
        //TODO waitingRoom should tell which map was chosen
        this.games.add(new Game(room.getPlayers(), room.getFirstPlayer()));
        this.games.get(this.games.size() - 1).run();
    }

    public void createWaitingRoom(){
        this.waitingRooms.add(new WaitingRoom());
    }
}
