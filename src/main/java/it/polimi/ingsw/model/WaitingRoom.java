package it.polimi.ingsw.model;

import java.util.ArrayList;

public class WaitingRoom {

    /**
     * The list of connected player
     */
    private ArrayList<String> players;

    /**
     * The nickname of the first player
     */
    private String firstPlayer;

    /**
     * Basic constructor
     */
    public WaitingRoom(){ this.players = new ArrayList<>(); }

    public ArrayList<String> getPlayers() { return this.players; }
    public String getFirstPlayer() { return this.firstPlayer; }

    /**
     * Adds a player to the players list
     * @param nickname The nickname of the new player
     * @return True if the players was correctly added
     */
    public boolean addPlayer(String nickname) {
        if(this.players.contains(nickname))
            return false;
        this.players.add(nickname);
        return true;
    }

    /**
     * TODO
     */
    public void voteMap(){}
}
