package it.polimi.ingsw.server;

import it.polimi.ingsw.events.serverToClient.NewPlayerConnectedQuestion;
import it.polimi.ingsw.events.serverToClient.TextMessage;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.server.ServerProxy;

import java.util.*;

class WaitingRoom {

    /**
     * The list of connected player
     */
    ArrayList<String> players;

    /**
     * Votes for each map:
     */
    private Map mapVotes;

    /**
     * Votes for amount of skulls to use
     */
    private Map skullVotes;

    /**
     * The list of all open connections
     */
    ArrayList<ServerProxy> serverProxies;

    /**
     * The length of the timer
     */
    private final static long TIMERMINUTES = (long) 1;

    /**
     * Maximum amount of players for a single game
     */
    private final static int MAXPLAYERS = 5;

    /**
     * Minimum amount of players for a single game
     */
    private final static int MINPLAYERS = 3;



    /**
     * Basic constructor
     */
    WaitingRoom(){
        this.players = new ArrayList<>();
        this.serverProxies = new ArrayList<>();

        //Puts 0 votes to each map
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

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                closeThisRoom();
            }
        }, TIMERMINUTES * 30 * 1000); //This should be TIMERMINUTES * 60 * 1000

    }

    /**
     * Adds a new player to this waiting room
     * @param proxy the proxy of the new player
     * @param nickname the nicknamen of the new player
     * @param mapToVote the map vote
     * @param nSkullsToVote the skulls vote
     */
    synchronized void addPlayer(ServerProxy proxy, String nickname, MapName mapToVote, int nSkullsToVote) {

        for(ServerProxy p : serverProxies)
            p.sendQuestionEvent(new NewPlayerConnectedQuestion(nickname));

        //Adds the player to the player list and all of his votes
        addVoteAndNickname(nickname, mapToVote, nSkullsToVote);

        this.serverProxies.add(proxy);

        if(players.size() >= MAXPLAYERS)
            closeThisRoom();

    }


    /**
     * Adds the votes of the new player to all the maps
     * @param nickname the nickname of the player
     * @param mapToVote the voted map
     * @param nSkullsToVote the voted skulls
     */
    private synchronized void addVoteAndNickname(String nickname, MapName mapToVote, int nSkullsToVote){

        if(players.contains(nickname))
            throw new RuntimeException("This waitingRoom already contains this player");

        //Adds the player to the player list
        players.add(nickname);

        //Adds a vote to the correct map
        int currentVotes = (int) (mapVotes.get(mapToVote));
        mapVotes.put(mapToVote, currentVotes + 1);


        if (nSkullsToVote < 5 || nSkullsToVote > 8)
            throw new RuntimeException("nSkullsToVote must be between 5 and 8");

        //Adds a vote to the correct amount of skulls
        currentVotes = (int) skullVotes.get(nSkullsToVote);
        skullVotes.put(nSkullsToVote, currentVotes + 1);

    }

    /**
     * @return the most voted game map
     */
    protected MapName getVotedMap(){
        MapName tempMaxMapName = MapName.values()[0];
        int tempMaxVotes = (int)mapVotes.get(MapName.values()[0]);

        for(int i = 1; i < MapName.values().length; i++){
            if((int)mapVotes.get(MapName.values()[i]) > tempMaxVotes){
                tempMaxMapName = MapName.values()[i];
                tempMaxVotes = (int)this.mapVotes.get(MapName.values()[i]);
            }
        }

        return tempMaxMapName;
    }

    /**
     * @return the most voted amount of skulls
     */
    protected int getVotedSkulls(){

        int maxVotedSkulls = 5;
        int maxVotes = (int)skullVotes.get(5);

        for(int i = 6; i <= 8; i++){
            if((int)skullVotes.get(i) > maxVotes){
                maxVotes = (int)skullVotes.get(i);
                maxVotedSkulls = i;
            }
        }

        return maxVotedSkulls;

    }

    /**
     * It closes the room once the timer has expired or the room reached its maximum capacity
     */
    private void closeThisRoom() {

        if(players.size() < MINPLAYERS){
            GamesHandler.roomNotFilledInTime(this);
            return;
        }

        GamesHandler.startGame(this);

    }

}
