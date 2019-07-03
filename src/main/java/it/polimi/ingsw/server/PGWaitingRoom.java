package it.polimi.ingsw.server;

import com.google.gson.JsonObject;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.view.server.ServerProxy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Paused Game waiting room
 */
public class PGWaitingRoom {

    /**
     * The path to the JSON of this game
     */
    private final String jsonPath;

    /**
     * The list of all players who are supposed to reconnect
     */
    private final ArrayList<String> inGamePlayers;

    /**
     * The currently reconnected players
     */
    private ArrayList<String> reconnectingPlayers;

    /**
     * The list of currently reconnected proxies
     */
    private ArrayList<ServerProxy> proxies;

    /**
     * Constructor
     */
    public PGWaitingRoom(String jsonPath){

        this.jsonPath = jsonPath;

        this.inGamePlayers = getInGamePlayers(jsonPath);

        this.reconnectingPlayers = new ArrayList<>();

        this.proxies = new ArrayList<>();

    }

    /**
     * Returns the list of all players who are supposed to reconnect
     * @param jsonPath the path to the json of the game
     * @return a list of nicknames
     */
    private ArrayList<String> getInGamePlayers(String jsonPath) {

        System.out.println("Trying to open JSON at " + jsonPath);

        try {

            JsonObject root = JsonDeserializer.myJsonParser.parse(new FileReader(jsonPath)).getAsJsonObject();

            ArrayList<String> connectedPlayers = JsonDeserializer.deserializePlayerNamesObject(root.get("playerNames").getAsJsonArray());
            ArrayList<String> disconnectedPlayers = JsonDeserializer.deserializePlayerNamesObject(root.get("disconnectedPlayerNames").getAsJsonArray());

            for (String i : disconnectedPlayers)
                connectedPlayers.add(i);

            return connectedPlayers;
        }
        catch (FileNotFoundException e){
            System.out.println("FILE NOT FOUND");
        }

        return null;
    }

    /**
     * Adds a player to this waiting room
     * @param nickname the nickname
     * @param jsonPath the path to the json file
     * @param proxy the proxy of the player
     * @return if it wai able to add the player
     */
    public boolean addPlayer(String nickname, String jsonPath, ServerProxy proxy){

        if( ! (inGamePlayers.contains(nickname))){
            System.err.println("" +  nickname + " tried to connect to the wrong waiting room");
            System.err.println("Usernames in this PGWaitingRoom: " + inGamePlayers);
            return false;
        }

        if( ! (jsonPath.equals(this.jsonPath))){
            System.err.println("" +  nickname + " tried to connect to the wrong waiting room");
            System.err.println("Path for this PGWaitingRoom: " + this.jsonPath);
            return false;
        }

        reconnectingPlayers.add(nickname);
        proxies.add(proxy);

        if(reconnectingPlayers.size() > 2){

            GamesHandler.reloadGame(this.jsonPath, this.reconnectingPlayers, this.proxies);

        }

        return true;
    }


}
