package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Map;

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
     * Votes for each map:
     * mapVotes[0] --> FIRE
     * mapVotes[1] --> EARTH
     * mapVotes[2] --> WIND
     * mapVotes[3] --> WATER
     */
    private Map mapVotes;

    private Map skullVotes;

    /**
     * Basic constructor
     */
    public WaitingRoom(){
        this.players = new ArrayList<>();
    }

    //Can't return this.player because it could be modified
    public ArrayList<String> getPlayers() { return new ArrayList<>(this.players); }
    //Can return this.firstPlayer because String is immutable
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
