package it.polimi.ingsw.model;

import java.util.ArrayList;

public class GamesHandler {
    /**
     * The list of active games
     */
    private ArrayList<Game> games;

    /**
     * The list of active waiting rooms
     */
    private ArrayList<WaitingRoom> waitingRooms;

    /**
     * Basic constructor
     */
    public GamesHandler(){
        this.games = new ArrayList<>();
        this.waitingRooms = new ArrayList<>();
    }

    /**
     * Starts the game by passing the associated waiting room
     * @param room the waiting room
     */
    public void startGame(WaitingRoom room){
        this.games.add(new Game(room.getPlayers(), room.getFirstPlayer()));
        this.games.get(this.games.size() - 1).run();
    }

    /**
     * Creates and opens a new waiting room
     */
    public void createWaitingRoom(){
        this.waitingRooms.add(new WaitingRoom());
    }
}
