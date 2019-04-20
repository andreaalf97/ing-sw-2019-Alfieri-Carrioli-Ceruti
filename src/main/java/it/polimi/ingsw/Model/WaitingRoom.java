package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.MapPackage.MapName;
import java.util.*;

public class WaitingRoom {

    /**
     * The list of connected player
     */
    private ArrayList<String> players;

    /**
     * Votes for each map:
     */
    private Map mapVotes;

    /**
     * Votes for amount of skulls to use
     */
    private Map skullVotes;

    /**
     * Timer
     */
    private Timer timer;

    /**
     * Used to define when the room is ready to start
     */
    private boolean isReady;

    private final static long TOTALTIME = 2 * 60 * 1000;

    /**
     * Basic constructor
     */
    public WaitingRoom(){
        this.players = new ArrayList<>();

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

        this.isReady = false;

        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(players.size() > 4)
                    isReady = true;
                else
                    Log.LOGGER.severe("Room has not been filled in time!");
            }
        }, TOTALTIME);

    }

    /**
     * Returns the amount of players registered into this room
     * @return this.players.size()
     */
    public int nPlayers(){
        return this.players.size();
    }

    //Can't return this.player because it could be modified
    public ArrayList<String> getPlayers() { return new ArrayList<>(this.players); }

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

        if(!this.isReady) {
            this.players.add(nickname);

            int tempVotes = (int) (this.mapVotes.get(mapToVote));
            this.mapVotes.put(mapToVote, tempVotes + 1);

            if (nSkullsToVote < 5 || nSkullsToVote > 8)
                throw new IllegalArgumentException("nSkullsToVote must be between 5 and 8");

            tempVotes = (int) this.skullVotes.get(nSkullsToVote);
            this.skullVotes.put(nSkullsToVote, tempVotes + 1);
        }
    }

    public MapName getVotedMap(){
        MapName tempMaxMapName = MapName.values()[0];
        int tempMaxVotes = (int)this.mapVotes.get(MapName.values()[0]);

        for(int i = 1; i < MapName.values().length; i++){
            if((int)this.mapVotes.get(MapName.values()[i]) > tempMaxVotes){
                tempMaxMapName = MapName.values()[i];
                tempMaxVotes = (int)this.mapVotes.get(MapName.values()[i]);
            }
        }

        return tempMaxMapName;
    }

    public int getVotedSkulls(){
        int maxSkulls = (int)this.skullVotes.get(5);

        for(int i = 6; i <= 8; i++){
            if((int)this.skullVotes.get(i) > maxSkulls){
                maxSkulls = (int)this.skullVotes.get(i);
            }
        }

        return maxSkulls;
    }

    public boolean isReady(){
        return this.isReady;
    }
}