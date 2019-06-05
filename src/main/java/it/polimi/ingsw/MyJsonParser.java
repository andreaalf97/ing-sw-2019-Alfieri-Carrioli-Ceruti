package it.polimi.ingsw;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.KillShotTrack;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.PowerUpDeck;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.cards.WeaponDeck;
import it.polimi.ingsw.model.map.GameMap;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class MyJsonParser {

    public static final String jsonEffectsFileNamePath = "src/main/resources/effects.json";

    public static JsonParser myJsonParser = new JsonParser();

//***********************************************************************************************************************************************

 //Game deserialization, it receives a model Snapshot for persistence and it creates a new Game
    public static Game createNewGameDeserializingModelSnapshot(String modelSnapshot){
        JsonObject jsonRoot = myJsonParser.parse(modelSnapshot).getAsJsonObject();

        ArrayList<Player> players = deserializePlayerObject(jsonRoot.get("players").getAsJsonArray());

        ArrayList<String> playerNames = deserializePlayerNamesObject(jsonRoot.get("playerNames").getAsJsonArray());

        WeaponDeck weaponDeck = new WeaponDeck(jsonRoot.get("weaponDeck").getAsJsonObject());

        PowerUpDeck powerUpDeck = new PowerUpDeck(jsonRoot.get("powerUpDeck").getAsJsonObject());

        KillShotTrack kst = new KillShotTrack(jsonRoot.get("kst").getAsJsonObject());

        GameMap gameMap = new GameMap(jsonRoot.get("gameMap").getAsJsonObject());

        return new Game(playerNames, players, weaponDeck, powerUpDeck, kst, gameMap);
    }

    /**
     * this method deserialize Players
     * @param jsonPlayers the json that represents the players
     * @return the corrresponding ArrayList
     */
    private static ArrayList<Player> deserializePlayerObject(JsonArray jsonPlayers) {
        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < jsonPlayers.size(); i++) {
            Player p = new Player(jsonPlayers.get(i).getAsJsonObject());
            players.add(p);
        }

        return players;
    }

    /**
     * this method deserialize Player Names objects
     * @param jsonPlayerNames the json of the players
     * @return the Players ArrayList
     */
    private static ArrayList<String> deserializePlayerNamesObject(JsonArray jsonPlayerNames) {

        ArrayList<String> playerNames = new ArrayList<>();

        for(int i = 0; i < jsonPlayerNames.size(); i++)
            playerNames.add(jsonPlayerNames.get(i).getAsString());

        return playerNames;
    }
//********************************************************************************************************************************************


    public static PowerUp getPowerUpByName(String powerUpName){
        try {
            JsonObject jsonDecks = new JsonParser().parse(new FileReader(jsonEffectsFileNamePath)).getAsJsonObject();
            JsonObject jsonPowerUpsDeck = jsonDecks.get("Powerups").getAsJsonObject();

            return new PowerUp(powerUpName, jsonPowerUpsDeck);
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while reading JSON");
            return null;
        }
    }

    public static Weapon getWeaponByName(String weaponName){
        try {
            JsonObject jsonDecks = new JsonParser().parse(new FileReader(jsonEffectsFileNamePath)).getAsJsonObject();
            JsonObject jsonWeaponsDeck = jsonDecks.get("Weapons").getAsJsonObject();

            return new Weapon(weaponName, jsonWeaponsDeck);
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while reading JSON");
            return null;
        }
    }



}
