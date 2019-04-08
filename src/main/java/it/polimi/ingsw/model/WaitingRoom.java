package it.polimi.ingsw.model;

import java.util.ArrayList;

public class WaitingRoom {
    private ArrayList<String> players;
    private String firstPlayer;

    public WaitingRoom(){ this.players = new ArrayList<>(); }

    public ArrayList<String> getPlayers() { return this.players; }
    public String getFirstPlayer() { return this.firstPlayer; }

    public boolean addPlayer(String nickname) {
        if(this.players.contains(nickname))
            return false;
        this.players.add(nickname);
        return true;
    }
    public void voteMap(){}
}
