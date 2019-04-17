package it.polimi.ingsw.model;

import it.polimi.ingsw.model.MapPackage.MapName;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
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
     */
    private Map mapVotes;

    private Map skullVotes;

    /**
     * Basic constructor
     */
    public WaitingRoom(){
        this.players = new ArrayList<>();
        this.firstPlayer = null;

        this.mapVotes = new EnumMap<MapName, Integer>(MapName.class);
        this.mapVotes.put(MapName.FIRE, 0);
        this.mapVotes.put(MapName.EARTH, 0);
        this.mapVotes.put(MapName.WIND, 0);
        this.mapVotes.put(MapName.WATER, 0);

        //The keys can be 5, 6, 7, 8, ecc, depending on the amount of skulls players want on the board
        //The values are the amount of votes each setup received
        this.skullVotes = new HashMap<Integer, Integer>();
        this.skullVotes.put(5, 0);
        this.skullVotes.put(6, 0);
        this.skullVotes.put(7, 0);
        this.skullVotes.put(8, 0);
    }

    //Can't return this.player because it could be modified
    public ArrayList<String> getPlayers() { return new ArrayList<>(this.players); }
    //Can return this.firstPlayer because String is immutable
    public String getFirstPlayer() { return this.firstPlayer; }

    /**
     * Adds a player to the players list and registers their votes
     * @param nickname The nickname of the new player
     * @param mapToVote the map chosen by the new player
     * @param nSkullsToVote the desired amount of skulls
     * @throws IllegalArgumentException if the method receives bad arguments
     */
    public void addPlayer(String nickname, MapName mapToVote, int nSkullsToVote) throws IllegalArgumentException{
        if(this.players.contains(nickname))
            throw new IllegalArgumentException("This waitingRoom already contains this player");
        this.players.add(nickname);

        int tempVotes = (int)(this.mapVotes.get(mapToVote));
        this.mapVotes.put(mapToVote, tempVotes+1);

        if(nSkullsToVote < 5 || nSkullsToVote > 8)
            throw new IllegalArgumentException("nSkullsToVote must be between 5 and 8");

        tempVotes = (int)this.skullVotes.get(nSkullsToVote);
        this.skullVotes.put(nSkullsToVote, tempVotes+1);
    }
}
