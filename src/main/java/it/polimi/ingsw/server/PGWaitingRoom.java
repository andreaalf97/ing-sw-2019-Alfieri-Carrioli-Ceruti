package it.polimi.ingsw.server;

import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.server.ServerProxy;

import java.util.ArrayList;

/**
 * Paused Game waiting room
 */
public class PGWaitingRoom {

    /**
     * The path to the JSON of this game
     */
    private final int gameId;

    /**
     * The currently reconnected players
     */
    private ArrayList<String> reconnectingPlayers;

    /**
     * The list of currently reconnected proxies
     */
    private ArrayList<ServerProxy> proxies;

    /**
     * The map to play on
     */
    private MapName votedMap;

    /**
     * Constructor
     */
    public PGWaitingRoom(int gameId, MapName votedMap){

        this.gameId = gameId;

        this.reconnectingPlayers = new ArrayList<>();

        this.proxies = new ArrayList<>();

        this.votedMap = votedMap;

    }

    public int getGameId(){
        return this.gameId;
    }

    /**
     * Adds a player to this waiting room
     * @param nickname the nickname
     * @param proxy the proxy of the player
     * @return if it wai able to add the player
     */
    public void addPlayer(String nickname, ServerProxy proxy){

        reconnectingPlayers.add(nickname);
        proxies.add(proxy);

        if(reconnectingPlayers.size() > 2){

            GamesHandler.reloadGame(gameId, this.reconnectingPlayers, this.proxies, this.votedMap);

        }
    }


}
