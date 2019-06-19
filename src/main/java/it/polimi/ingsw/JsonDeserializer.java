package it.polimi.ingsw;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.client.GameInfo;
import it.polimi.ingsw.client.cli.MapGrid;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.KillShotTrack;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.PowerUpDeck;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.cards.WeaponDeck;
import it.polimi.ingsw.model.map.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

public class JsonDeserializer {

    /**
     * constant for json file path
     */
    public static final String jsonEffectsFileNamePath = "src/main/resources/effects.json";

    public static final String jsonMapsFileNamePath = "src/main/resources/maps.json";

    /**
     * constant for reading "Weapons" key in json files
     */
    public static final String jsonWeaponsKey = "Weapons";
    
    public static final String jsonPowerUpsKey = "Powerups";

    public static JsonParser myJsonParser = new JsonParser();

//***********************************************************************************************************************************************

    public static GameInfo deserializedSnapshot(JsonObject lastSnapshotReceived){

        ArrayList<String> playersNames = deserializePlayerNamesObject(lastSnapshotReceived.get("playerNames").getAsJsonArray());

        ArrayList<Player> playersInfo = deserializePlayerObject(lastSnapshotReceived.get("players").getAsJsonArray());

        KillShotTrack killShotTrack = new KillShotTrack(lastSnapshotReceived.get("kst").getAsJsonObject());

        GameMap gameMap = new GameMap(lastSnapshotReceived.get("gameMap").getAsJsonObject());

        return new GameInfo(playersNames, killShotTrack, gameMap, playersInfo);
    }

 //Game deserialization, it receives a model Snapshot for persistence and it creates a new Game
    public static Game deserializeModelSnapshot(String modelSnapshot){
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
            JsonObject jsonDecks = myJsonParser.parse(new FileReader(jsonEffectsFileNamePath)).getAsJsonObject();
            JsonObject jsonPowerUpsDeck = jsonDecks.get(jsonPowerUpsKey).getAsJsonObject();

            return new PowerUp(powerUpName, jsonPowerUpsDeck);
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while reading JSON");
            return null;
        }
    }

    public static Weapon getWeaponByName(String weaponName){
        try {
            JsonObject jsonDecks = myJsonParser.parse(new FileReader(jsonEffectsFileNamePath)).getAsJsonObject();
            JsonObject jsonWeaponsDeck = jsonDecks.get(jsonWeaponsKey).getAsJsonObject();

            return new Weapon(weaponName, jsonWeaponsDeck);
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while reading JSON");
            return null;
        }
    }

    /**
     * create the powerUp deck
     * @return the powerUpDeck of the game
     */
    public static PowerUpDeck deserializePowerUpDeck() {
        ArrayList<PowerUp> powerUpList = new ArrayList<>();

        try{
            JsonObject jsonDecks = myJsonParser.parse(new FileReader(jsonEffectsFileNamePath)).getAsJsonObject();
            JsonObject jsonPowerupsDeck = jsonDecks.get(jsonPowerUpsKey).getAsJsonObject();
            Set<String> keys = jsonPowerupsDeck.keySet();

            for(int i = 0; i < 6; i++) {
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String powerupName = iterator.next();
                    powerUpList.add(new PowerUp(powerupName, jsonPowerupsDeck));

                }
            }

        }
        catch(FileNotFoundException e){
            MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return new PowerUpDeck(powerUpList);
    }

    /**
     * create the weapon deck reading it from json
     * @return the weapon deck of the game
     */
    public static WeaponDeck deserializeWeaponDeck(){

        ArrayList<Weapon> weaponList = new ArrayList<>();

        try {
            JsonObject jsonDecks = myJsonParser.parse(new FileReader(jsonEffectsFileNamePath)).getAsJsonObject();
            JsonObject jsonWeaponsDeck = jsonDecks.get(jsonWeaponsKey).getAsJsonObject();
            Set<String> keys = jsonWeaponsDeck.keySet();

            Iterator<String> iterator = keys.iterator();
            while(iterator.hasNext()){
                String weaponName = iterator.next();
                Weapon weaponTemp = new Weapon(weaponName, jsonWeaponsDeck);

                weaponList.add(weaponTemp);
            }

        }
        catch(FileNotFoundException e){
            MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return new WeaponDeck(weaponList);
    }

    /**
     * This static method reads the necessary values from a JSON file and constructs the new map selected
     * @param mapName the name of the chosen map
     * @return the map
     */
    public static GameMap deserializeGameMap(MapName mapName, WeaponDeck weaponDeck, PowerUpDeck powerUpDeck){

        //The Spot matrix I will work on
        Spot[][] tempSpotMatrix = new Spot[3][4];
        Random rand = new Random();

        try {
            JsonElement jsonElement = myJsonParser.parse(new FileReader(jsonMapsFileNamePath));

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
                            //If this is an ammo spot I randomly add (or don't) a power up and some ammo
                            tempSpotMatrix[i][j] = new AmmoSpot(doors, room);

                            if(rand.nextBoolean()){
                                tempSpotMatrix[i][j].refill(powerUpDeck.drawCard()); //Refills with a powerup
                            }
                            else{
                                tempSpotMatrix[i][j].refill(null); //Refills only ammos
                            }
                        }

                    }
                    else
                        tempSpotMatrix[i][j] = null;
                }
            }
        }
        catch (FileNotFoundException e){
            //If the file does not exist || we have problems with the file
            MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
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
        try {
            JsonArray chosenMap = myJsonParser.parse(new FileReader("src/main/resources/cliMaps.json")).getAsJsonObject().get(votedMap.toString()).getAsJsonArray();

            for(int r = 0; r < MapGrid.maxVerticalMapLength; r++){

                JsonArray jsonCol = chosenMap.get(r).getAsJsonArray();

                for(int c = 0; c < MapGrid.maxHorizontalMapLength; c++){
                    cliMap[r][c] = jsonCol.get(c).getAsString();
                }

                for (int c = MapGrid.maxHorizontalMapLength; c < MapGrid.maxHorizLength; c++)
                    cliMap[r][c] = " ";
            }

        }
        catch(FileNotFoundException e){
            cliMap = null;
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

        try {
            JsonObject weaponsJSON = myJsonParser.parse(new FileReader(jsonEffectsFileNamePath)).getAsJsonObject().get(jsonWeaponsKey).getAsJsonObject();
            weaponTest = new Weapon(weaponName, weaponsJSON);
        } catch (FileNotFoundException e) {
            weaponTest = null;
        }

        return weaponTest;
    }

    /**
     * this method load powerUps from effects,json file
     * @param powerUpName the name of the powerUp to load
     * @return the powerUp
     */
    public static PowerUp createPowerUpFromJson(String powerUpName){
        PowerUp powerUpTest;

        try {
            JsonObject powerupsJSON = myJsonParser.parse(new FileReader(jsonEffectsFileNamePath)).getAsJsonObject().get(jsonPowerUpsKey).getAsJsonObject();
            powerUpTest = new PowerUp(powerUpName, powerupsJSON);
        } catch (FileNotFoundException e) {
           powerUpTest = null;
        }

        return powerUpTest;
    }

}
