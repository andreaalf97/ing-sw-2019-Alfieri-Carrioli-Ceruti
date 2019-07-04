package it.polimi.ingsw;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.client.GameInfo;
import it.polimi.ingsw.client.PlayerInfo;
import it.polimi.ingsw.client.cli.MapGrid;
import it.polimi.ingsw.client.gui.OtherPlayerInfo;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.KillShotTrack;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.map.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

public class JsonDeserializer {

    /**
     * constant for json file path
     */
    public static final String jsonEffectsFileNamePath = "/effects.json";

    public static final String jsonMapsFileNamePath = "/maps.json";

    /**
     * constant for reading "Weapons" key in json files
     */
    public static final String jsonWeaponsKey = "Weapons";
    
    public static final String jsonPowerUpsKey = "Powerups";

    public static JsonParser myJsonParser = new JsonParser();

//***********************************************************************************************************************************************

    //Used to create a fake GameInfo class for Gui
    public static ArrayList<String> deserializeplayersNames(String snapshot){

        JsonObject jsonRoot = myJsonParser.parse(snapshot).getAsJsonObject();

        ArrayList<String> playersNames = deserializePlayerNamesObject(jsonRoot.get("playerNames").getAsJsonArray());

       return playersNames;
    }

    public static GameMap deserializeGameMap(String snapshot){

        JsonObject jsonRoot = myJsonParser.parse(snapshot).getAsJsonObject();

        GameMap gameMap = new GameMap(jsonRoot.get("gameMap").getAsJsonObject());

        return gameMap;
    }

    public static KillShotTrack deserializekst(String snapshot){

        JsonObject jsonRoot = myJsonParser.parse(snapshot).getAsJsonObject();

        KillShotTrack killShotTrack = new KillShotTrack(jsonRoot.get("kst").getAsJsonObject());

        return killShotTrack;
    }


    public static GameInfo deserializedSnapshot(JsonObject lastSnapshotReceived, String username){

        ArrayList<String> playersNames = deserializePlayerNamesObject(lastSnapshotReceived.get("playerNames").getAsJsonArray());

        KillShotTrack killShotTrack = new KillShotTrack(lastSnapshotReceived.get("kst").getAsJsonObject());

        GameMap gameMap = new GameMap(lastSnapshotReceived.get("gameMap").getAsJsonObject());

        PlayerInfo playerInfo = new PlayerInfo(username, lastSnapshotReceived);

        ArrayList<OtherPlayerInfo> otherPlayerInfos = deserializeOtherPlayerInfo(lastSnapshotReceived, username);

        return new GameInfo(playersNames, killShotTrack, gameMap, playerInfo, otherPlayerInfos);
    }

    private static ArrayList<OtherPlayerInfo> deserializeOtherPlayerInfo(JsonObject jsonRoot, String username) {
        ArrayList<OtherPlayerInfo> otherPlayerInfos = new ArrayList<>();

        JsonArray players = jsonRoot.get("players").getAsJsonArray();

        for(int i = 0; i < players.size(); i++){
            JsonObject otherPlayer = players.get(i).getAsJsonObject();
            String otherPlayerNickname = otherPlayer.get("nickname").getAsString();
            if(!otherPlayerNickname.equals(username)){
                OtherPlayerInfo otherPlayerObject= new OtherPlayerInfo(otherPlayerNickname, jsonRoot);
                otherPlayerInfos.add(otherPlayerObject);
            }
        }

        return otherPlayerInfos;
    }

    //Game deserialization, it receives a model Snapshot for persistence and it creates a new Game. USELESSSSSSSSSSSS
    public static Game deserializeModelSnapshot(String modelSnapshot){
        JsonObject jsonRoot = myJsonParser.parse(modelSnapshot).getAsJsonObject();

        ArrayList<Player> players = deserializePlayerObject(jsonRoot.get("players").getAsJsonArray());

        ArrayList<String> playerNames = deserializePlayerNamesObject(jsonRoot.get("playerNames").getAsJsonArray());

        WeaponDeck weaponDeck = new WeaponDeck(jsonRoot.get("weaponDeck").getAsJsonObject());

        PowerUpDeck powerUpDeck = new PowerUpDeck(jsonRoot.get("powerUpDeck").getAsJsonObject());

        AmmoCardDeck ammoCardDeck = new AmmoCardDeck(jsonRoot.get("ammoCardDeck").getAsJsonObject()); //FIXME

        KillShotTrack kst = new KillShotTrack(jsonRoot.get("kst").getAsJsonObject());

        GameMap gameMap = new GameMap(jsonRoot.get("gameMap").getAsJsonObject());

        int id = jsonRoot.get("gameId").getAsInt();

        ArrayList<Player> disconnectedPlayers = deserializePlayerObject(jsonRoot.get("disconnectedPlayers").getAsJsonArray());

        return new Game(playerNames, players, weaponDeck, powerUpDeck,ammoCardDeck, kst, gameMap, id, disconnectedPlayers);
    }


    /**
     * this method deserialize Players
     * @param jsonPlayers the json that represents the players
     * @return the corrresponding ArrayList
     */
    public static ArrayList<Player> deserializePlayerObject(JsonArray jsonPlayers) {
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
    public static ArrayList<String> deserializePlayerNamesObject(JsonArray jsonPlayerNames) {

        ArrayList<String> playerNames = new ArrayList<>();

        for(int i = 0; i < jsonPlayerNames.size(); i++)
            playerNames.add(jsonPlayerNames.get(i).getAsString());

        return playerNames;
    }
//********************************************************************************************************************************************


    public static PowerUp getPowerUpByName(String powerUpName){
            JsonObject jsonDecks = myJsonParser.parse(new BufferedReader(new InputStreamReader(JsonDeserializer.class.getResourceAsStream(jsonEffectsFileNamePath)))).getAsJsonObject();
            JsonObject jsonPowerUpsDeck = jsonDecks.get(jsonPowerUpsKey).getAsJsonObject();

            return new PowerUp(powerUpName, jsonPowerUpsDeck);
    }

    public static Weapon getWeaponByName(String weaponName){
        JsonObject jsonDecks = myJsonParser.parse(new BufferedReader(new InputStreamReader(JsonDeserializer.class.getResourceAsStream(jsonEffectsFileNamePath)))).getAsJsonObject();
        JsonObject jsonWeaponsDeck = jsonDecks.get(jsonWeaponsKey).getAsJsonObject();

        return new Weapon(weaponName, jsonWeaponsDeck);
    }

    /**
     * create the powerUp deck
     * @return the powerUpDeck of the game
     */
    public static PowerUpDeck deserializePowerUpDeck() {
        ArrayList<PowerUp> powerUpList = new ArrayList<>();

        JsonObject jsonDecks = myJsonParser.parse(new BufferedReader(new InputStreamReader(JsonDeserializer.class.getResourceAsStream(jsonEffectsFileNamePath)))).getAsJsonObject();
        JsonObject jsonPowerupsDeck = jsonDecks.get(jsonPowerUpsKey).getAsJsonObject();
        Set<String> keys = jsonPowerupsDeck.keySet();

        for(int i = 0; i < 6; i++) {
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String powerupName = iterator.next();
                powerUpList.add(new PowerUp(powerupName, jsonPowerupsDeck));

            }
        }

        return new PowerUpDeck(powerUpList);
    }

    /**
     * create the weapon deck reading it from json
     * @return the weapon deck of the game
     */
    public static WeaponDeck deserializeWeaponDeck(){

        ArrayList<Weapon> weaponList = new ArrayList<>();

        JsonObject jsonDecks = myJsonParser.parse(new BufferedReader(new InputStreamReader(JsonDeserializer.class.getResourceAsStream(jsonEffectsFileNamePath)))).getAsJsonObject();
        JsonObject jsonWeaponsDeck = jsonDecks.get(jsonWeaponsKey).getAsJsonObject();
        Set<String> keys = jsonWeaponsDeck.keySet();

        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()){
            String weaponName = iterator.next();
            Weapon weaponTemp = new Weapon(weaponName, jsonWeaponsDeck);

            weaponList.add(weaponTemp);
        }

        return new WeaponDeck(weaponList);
    }

    public static AmmoCardDeck deserializeAmmoCardDeck(){
        ArrayList<AmmoCard> ammoCards = new ArrayList<>();

        JsonArray jsonAmmoCardDeck = myJsonParser.parse(new BufferedReader(new InputStreamReader(JsonDeserializer.class.getResourceAsStream(jsonEffectsFileNamePath)))).getAsJsonObject().get("AmmoCards").getAsJsonArray();
        for(int i = 0; i < jsonAmmoCardDeck.size(); i++){
            AmmoCard ammoLoaded = deserializeAmmoCard(jsonAmmoCardDeck.get(i).getAsJsonObject());
            ammoCards.add(ammoLoaded);
        }

        return new AmmoCardDeck(ammoCards);
    }

    private static AmmoCard deserializeAmmoCard(JsonObject jsonAmmoCard) {
        boolean hasPowerUp = jsonAmmoCard.get("hasPowerUp").getAsBoolean();
        String imagePath = jsonAmmoCard.get("ammoCardImagePath").getAsString();
        JsonArray jsonColors = jsonAmmoCard.get("colors").getAsJsonArray();
        ArrayList<Color> colors = new ArrayList<>();

        for(int i = 0; i < jsonColors.size(); i++){
            Color c = Color.valueOf(jsonColors.get(i).getAsString());
            colors.add(c);
        }

        return new AmmoCard(imagePath, colors, hasPowerUp);
    }

    /**
     * This static method reads the necessary values from a JSON file and constructs the new map selected
     * @param mapName the name of the chosen map
     * @return the map
     */
    public static GameMap deserializeGameMap(MapName mapName, WeaponDeck weaponDeck, PowerUpDeck powerUpDeck, AmmoCardDeck ammoCardDeck){

        //The Spot matrix I will work on
        Spot[][] tempSpotMatrix = new Spot[3][4];
        Random rand = new Random();

        JsonElement jsonElement = myJsonParser.parse(new BufferedReader(new InputStreamReader(JsonDeserializer.class.getResourceAsStream(jsonMapsFileNamePath))));

        JsonObject jsonObjectMyMap = jsonElement.getAsJsonObject().get(mapName.toString()).getAsJsonObject(); //the map selected is saved in jsonObjectMymap

        //these two nested for cycles load the information of the single spot in the matrix map
        for(int i = 0; i < tempSpotMatrix.length; i++) {

            JsonObject jsonRow = jsonObjectMyMap.get("row"+ i).getAsJsonObject();
            for (int j = 0; j < tempSpotMatrix[i].length; j++){

                //FOR EACH SPOT IN THE JSON FILE

                JsonObject jsonCol = jsonRow.get("col" + j).getAsJsonObject(); // <== jsonSpot

                if (!jsonCol.toString().equals("{}")) {

                    //Reading isSpawnSpot & isAmmoSpot
                    boolean isSpawnSpot = jsonCol.get("isSpawnSpot").getAsBoolean();
                    boolean isAmmoSpot = jsonCol.get("isAmmoSpot").getAsBoolean();

                    //Reading all rooms
                    Room room = Room.valueOf(jsonCol.get("room").getAsString());

                    //Creating a Java ArrayList from a JsonArray object
                    ArrayList<Boolean>doors = new ArrayList<>();
                    JsonArray jsonDoors = jsonCol.get("doors").getAsJsonArray();
                    for (int k = 0; k <= 3 ; k++) {
                        doors.add(jsonDoors.get(k).getAsBoolean());
                    }

                    //Constructing a new SpawnSpot
                    if(isSpawnSpot) { //If this is a Spawn spot I add all the weapons
                        tempSpotMatrix[i][j] = new SpawnSpot(doors, room);

                        tempSpotMatrix[i][j].refill(weaponDeck.drawCard());
                        tempSpotMatrix[i][j].refill(weaponDeck.drawCard());
                        tempSpotMatrix[i][j].refill(weaponDeck.drawCard());
                    }

                    //Constructing a new AmmoSpot
                    if(isAmmoSpot) {

                        tempSpotMatrix[i][j] = new AmmoSpot(doors, room);
                        AmmoCard ammoCard = ammoCardDeck.drawCard();
                        tempSpotMatrix[i][j].refill(ammoCard);

                        if(ammoCard.hasPowerUp())
                            tempSpotMatrix[i][j].setPowerUp(powerUpDeck.drawCard());
                    }

                }
                else
                    tempSpotMatrix[i][j] = null;
            }
        }

        //The map is created with its PROTECTED constructor
        return new GameMap(tempSpotMatrix);
    }

    /**
     * serialize the string received in json
     * @param stringToParse the string that i have to incapsuled in a JsonObject
     * @return the jsonObject corresponding to event
     */
    public static JsonObject stringToJsonObject(String stringToParse){
        return myJsonParser.parse(stringToParse).getAsJsonObject();
    }

    /**
     * this method reads the cli map from the Json
     * @param votedMap the map chosen
     * @return the cliMap
     */
    public static String[][] deserializeCliMap(MapName votedMap){
        String [][] cliMap = new String[MapGrid.maxVerticalMapLength][MapGrid.maxHorizLength];

        //TODO TEST DESERIALIZATION

        JsonArray chosenMap = myJsonParser.parse(new BufferedReader(new InputStreamReader(JsonDeserializer.class.getResourceAsStream("/cliMaps.json")))).getAsJsonObject().get(votedMap.toString()).getAsJsonArray();

        for(int r = 0; r < MapGrid.maxVerticalMapLength; r++){

            JsonArray jsonCol = chosenMap.get(r).getAsJsonArray();

            for(int c = 0; c < MapGrid.maxHorizontalMapLength; c++){
                cliMap[r][c] = jsonCol.get(c).getAsString();
            }

            for (int c = MapGrid.maxHorizontalMapLength; c < MapGrid.maxHorizLength; c++)
                cliMap[r][c] = " ";
        }

        return cliMap;

    }



    /**
     * this method loads weapon from effects.json
     * @param weaponName the name of the weapon to load
     * @return the weapon
     */
    public static Weapon createWeaponForTesting(String weaponName){
        Weapon weaponTest;


        JsonObject weaponsJSON = myJsonParser.parse(new BufferedReader(new InputStreamReader(JsonDeserializer.class.getResourceAsStream(jsonEffectsFileNamePath)))).getAsJsonObject().get(jsonWeaponsKey).getAsJsonObject();
        weaponTest = new Weapon(weaponName, weaponsJSON);


        return weaponTest;
    }

    /**
     * this method load powerUps from effects,json file
     * @param powerUpName the name of the powerUp to load
     * @return the powerUp
     */
    public static PowerUp createPowerUpFromJson(String powerUpName){
        PowerUp powerUpTest;

        JsonObject powerupsJSON = myJsonParser.parse(new BufferedReader(new InputStreamReader(JsonDeserializer.class.getResourceAsStream(jsonEffectsFileNamePath)))).getAsJsonObject().get(jsonPowerUpsKey).getAsJsonObject();
        powerUpTest = new PowerUp(powerUpName, powerupsJSON);

        return powerUpTest;
    }

}
