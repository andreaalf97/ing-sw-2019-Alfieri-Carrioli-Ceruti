package it.polimi.ingsw.client.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.model.map.Room;

import java.util.ArrayList;


public class MapGrid {

    //attributes valid for all spots length
    public static final int horizontalSpotStandard = 15;
    public static final int verticalSpotStandard = 7;

    //attributes for ammo label in each spot
    public static final int horizontalAmmoStandard = 3;
    public static final int verticalAmmoStandard = 1;

    //attributes for player box in each spot
    public static final int horizontalPlayerStandard = 3;
    public static final int verticalPlayerStandard = 3;

    //attributes for coord label
    public static final int horizontalCoordStandard = 8;
    public static final int verticalCoordStandard = 5;

    //attributes for spawn store
    public static final int maxHorizLength = 81;
    public static int cont = 0;

    //attributes fro map building
    public static final int maxHorizontalMapLength = 61; //53 - 5 separazioni = 48 che diviso 4 fa 12 caselle a spot
    public static final int maxVerticalMapLength = 21; //7 caselle di altezza
    public static String[][] map = new String[maxVerticalMapLength][maxHorizLength];



    //don't touch this, need this for testing if filling correctly cli map
    public static String jsonTry = "{\"players\":[{\"nickname\":\"gino\",\"nRedAmmo\":1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"nDeaths\":0,\"xPosition\":-1,\"yPosition\":-1,\"isDead\":true,\"damages\":[],\"marks\":[],\"nMoves\":3,\"nMovesBeforeGrabbing\":1,\"nMovesBeforeShooting\":0,\"canReloadBeforeShooting\":false,\"playerStatus\":{\"isFirstTurn\":true,\"isFrenzyTurn\":false,\"isActive\":false,\"nActionsDone\":0,\"nActions\":2,\"isConnected\":false},\"weaponList\":[],\"powerUpList\":[]},{\"nickname\":\"andreaalf\",\"nRedAmmo\":1,\"yPosition\":-1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"nDeaths\":0,\"xPosition\":-1,\"isDead\":true,\"damages\":[],\"marks\":[]},{\"nickname\":\"meme\",\"nRedAmmo\":1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"nDeaths\":0,\"xPosition\":-1,\"yPosition\":-1,\"isDead\":true,\"damages\":[],\"marks\":[]}],\"playerNames\":[\"gino\",\"andreaalf\",\"meme\"],\"kst\":{\"skullList\":[\"SKULL\",\"SKULL\",\"SKULL\",\"SKULL\",\"SKULL\"],\"isOverkill\":[false,false,false,false,false]},\"gameMap\":{\"row0\":{\"col0\":{\"spot\":{\"ammoColorList\":[\"BLUE\",\"YELLOW\",\"RED\"],\"playersHere\":[\"gino\", \"meme\", \"andreaalf\"],\"doors\":[false,false,true,false],\"room\":\"SAPPHIRE\"}},\"col1\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"powerup\":{\"powerUpName\":\"Teleporter\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":100,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},\"playersHere\":[],\"doors\":[false,false,false,false],\"room\":\"SAPPHIRE\"}},\"col2\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"LockRifle\",\"cost\":[\"BLUE\",\"BLUE\"],\"effects\":[{\"nDamages\":2,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1]]},{\"weaponName\":\"MachineGun\",\"cost\":[\"BLUE\",\"RED\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":2,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1,2,3],[0,2,3,1],[0,2,1],[0,3,1],[0,1,4],[0,4,1]]},{\"weaponName\":\"Thor\",\"cost\":[\"BLUE\",\"RED\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"LASTONEATTACKED\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"LASTONEATTACKED\"}],\"isLoaded\":true,\"order\":[[0,1,2]]}],\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"SAPPHIRE\"}},\"col3\":{\"spot\":{}}},\"row1\":{\"col0\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"PlasmaGun\",\"cost\":[\"BLUE\",\"YELLOW\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":2,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2],[1,0,2],[0,2,1]]},{\"weaponName\":\"Whisper\",\"cost\":[\"BLUE\",\"BLUE\",\"YELLOW\"],\"effects\":[{\"nDamages\":3,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":2,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0]]},{\"weaponName\":\"Electroscythe\",\"cost\":[\"BLUE\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\",\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]}],\"playersHere\":[],\"doors\":[true,false,false,false],\"room\":\"RUBY\"}},\"col1\":{\"spot\":{\"ammoColorList\":[\"BLUE\",\"BLUE\"],\"powerup\":{\"powerUpName\":\"TargetingScope\",\"color\":\"RED\",\"effect\":{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"ANY\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},\"isTurnPowerup\":false},\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"RUBY\"}},\"col2\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"powerup\":{\"powerUpName\":\"Newton\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},\"playersHere\":[],\"doors\":[true,true,false,false],\"room\":\"RUBY\"}},\"col3\":{\"spot\":{\"ammoColorList\":[\"RED\",\"RED\"],\"powerup\":{\"powerUpName\":\"TagbackGrenade\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},\"isTurnPowerup\":false},\"playersHere\":[],\"doors\":[false,false,false,true],\"room\":\"TOPAZ\"}}},\"row2\":{\"col0\":{\"spot\":{}},\"col1\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"BLUE\",\"YELLOW\"],\"playersHere\":[],\"doors\":[true,false,false,false],\"room\":\"DIAMOND\"}},\"col2\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"BLUE\",\"RED\"],\"playersHere\":[],\"doors\":[false,true,false,false],\"room\":\"DIAMOND\"}},\"col3\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"TractorBeam\",\"cost\":[\"BLUE\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"RED\",\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":3,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1],[2,3]]},{\"weaponName\":\"VortexCannon\",\"cost\":[\"RED\",\"BLUE\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2,3,4,3]]},{\"weaponName\":\"Furnace\",\"cost\":[\"RED\",\"BLUE\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":4,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":1,\"nPlayersMarkable\":4,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":1,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]}],\"playersHere\":[],\"doors\":[false,false,false,true],\"room\":\"TOPAZ\"}}}}}";

    public static void buildCliMap(MapName votedMap){
        map = JsonDeserializer.deserializeCliMap(votedMap);
    }

    public static void fillMapWithAmmoAndCoord(JsonObject lastSnapshotReceived, ArrayList<PlayerColor> playerColors, ArrayList<String> allPlayersNames){

        if (lastSnapshotReceived.get("gameMap") != null){
            JsonObject jsonGameMap = lastSnapshotReceived.get("gameMap").getAsJsonObject();

            for(int r = 0; r < 3; r++) {

                JsonObject jsonRow = jsonGameMap.get("row" + r).getAsJsonObject();

                for (int c = 0; c < 4; c++) {

                    JsonObject jsonSpot = jsonRow.get("col" + c).getAsJsonObject().get("spot").getAsJsonObject(); // <== jsonSpot

                    if (!jsonSpot.toString().equals("{}")) { //appena sono sicuro di avere uno spot valido nel json posso istanziare uno spawnspot o un ammo spot

                        fillCliSpot(r, c, jsonSpot);
                        fillCoord(r, c);

                        JsonArray jsonPlayersHere = jsonSpot.get("playersHere").getAsJsonArray();
                        addPlayersInMap(r, c, jsonPlayersHere, playerColors, allPlayersNames);
                    }
                }
            }
        }
    }

    private static void addPlayersInMap(int row, int column, JsonArray jsonPlayersHere, ArrayList<PlayerColor> playerColors, ArrayList<String> allPlayersNames) {

        if(jsonPlayersHere.size() != 0) {

            for (int i = 0; i < jsonPlayersHere.size(); i++) {
                for (int playerColorIndex = 0; playerColorIndex < allPlayersNames.size(); playerColorIndex++) {
                    if (jsonPlayersHere.get(i).getAsString().equals(allPlayersNames.get(playerColorIndex))) {
                        addPlayerInMap(row, column, playerColors.get(playerColorIndex), i);
                    }
                }
            }
        }

    }

    private static void addPlayerInMap(int row, int column, PlayerColor playerColor, int indexOfPlayerInSpot) {
        map[row*verticalSpotStandard +  verticalPlayerStandard][column*horizontalSpotStandard + horizontalPlayerStandard + 3*indexOfPlayerInSpot] = playerColor.escape() + "╬" + Color.RESET;

    }

    private static void fillCoord(int r, int c) {
        String x = String.valueOf(r);
        String y = String.valueOf(c);
        map[r*verticalSpotStandard + verticalCoordStandard][c*horizontalSpotStandard + horizontalCoordStandard] = "(";
        map[r*verticalSpotStandard + verticalCoordStandard][c*horizontalSpotStandard + horizontalCoordStandard + 1] = x;
        map[r*verticalSpotStandard + verticalCoordStandard][c*horizontalSpotStandard + horizontalCoordStandard + 2] = ",";
        map[r*verticalSpotStandard + verticalCoordStandard][c*horizontalSpotStandard + horizontalCoordStandard + 3] = y;
        map[r*verticalSpotStandard + verticalCoordStandard][c*horizontalSpotStandard + horizontalCoordStandard + 4] = ")";

    }

    private static void fillCliSpot(int r, int c, JsonObject jsonSpot) {
        if(!jsonSpot.toString().equals("{}")) {
            if (jsonSpot.get("ammoCard") != null) {

                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard] = "A";
                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard + 1] = "M";
                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard + 2] = "M";
                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard + 3] = "O";
                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard + 4] = ":";

                JsonObject jsonAmmoCard = jsonSpot.get("ammoCard").getAsJsonObject();
                JsonArray jsonAmmoColorList = jsonAmmoCard.get("ammoColorList").getAsJsonArray();
                for (int i = 0; i < jsonAmmoColorList.size(); i++) {
                    Color ammoColor = Color.valueOf(jsonAmmoColorList.get(i).getAsString().toUpperCase());
                    fillAmmoColorList(r * verticalSpotStandard + verticalAmmoStandard, c * horizontalSpotStandard + horizontalAmmoStandard + 6 + 2 * i, ammoColor);
                }

                if (jsonAmmoCard.get("hasPowerUp").getAsBoolean() && jsonAmmoColorList.size() != 0)
                    fillAmmoWithPowerUp(r * verticalSpotStandard + verticalAmmoStandard, c * horizontalSpotStandard + horizontalAmmoStandard + 10);
            } else {

                addSpawnStore(jsonSpot);

                Room room = Room.valueOf(jsonSpot.get("room").getAsString().toUpperCase());

                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard] = room.escape() + "S" + Color.RESET;
                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard + 1] = room.escape() + "P" + Color.RESET;
                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard + 2] = room.escape() + "A" + Color.RESET;
                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard + 3] = room.escape() + "W" + Color.RESET;
                map[r * verticalSpotStandard + verticalAmmoStandard][c * horizontalSpotStandard + horizontalAmmoStandard + 4] = room.escape() + "N" + Color.RESET;

                map[r * verticalSpotStandard + verticalAmmoStandard + 1][c * horizontalSpotStandard + horizontalAmmoStandard] = room.escape() + "S" + Color.RESET;
                map[r * verticalSpotStandard + verticalAmmoStandard + 1][c * horizontalSpotStandard + horizontalAmmoStandard + 1] = room.escape() + "P" + Color.RESET;
                map[r * verticalSpotStandard + verticalAmmoStandard + 1][c * horizontalSpotStandard + horizontalAmmoStandard + 2] = room.escape() + "O" + Color.RESET;
                map[r * verticalSpotStandard + verticalAmmoStandard + 1][c * horizontalSpotStandard + horizontalAmmoStandard + 3] = room.escape() + "T" + Color.RESET;

            }
        }
    }

    public static void removeAmmoLabel(int x, int y) {
        for(int c = y * horizontalSpotStandard + horizontalAmmoStandard + 6; c < y * horizontalSpotStandard + horizontalAmmoStandard + 11; c++)
            map[x * verticalSpotStandard + verticalAmmoStandard][c] = " ";
    }

    private static void fillAmmoWithPowerUp(int r, int c) {
        map[r][c] = Color.WHITE.escape() + "P" + Color.RESET;
    }

    private static void fillAmmoColorList(int r, int c, Color ammoColor) {
        map[r][c] = ammoColor.escape() + "A" + Color.RESET;
    }

    public static void clearMap(){
        for(int r = 0; r < maxVerticalMapLength; r++){
            for(int c = 0; c < maxHorizLength; c++){
                map[r][c] = " ";
            }
        }
    }

    public static void printMap(){
        for (int r = 0; r < maxVerticalMapLength; r++) {

            System.out.println();

            for (int c = 0; c < maxHorizLength; c++) {

                System.out.print(map[r][c]);

            }

        }
    }

    public static void addSpawnStore(JsonObject jsonSpot){
        Room spawnRoom = Room.valueOf(jsonSpot.get("room").getAsString());

        map[verticalSpotStandard*cont][maxHorizontalMapLength + 3] = spawnRoom.escape() + "S" + Color.RESET;
        map[verticalSpotStandard*cont][maxHorizontalMapLength + 4] = spawnRoom.escape() + "P" + Color.RESET;
        map[verticalSpotStandard*cont][maxHorizontalMapLength + 5] = spawnRoom.escape() + "A" + Color.RESET;
        map[verticalSpotStandard*cont][maxHorizontalMapLength + 6] = spawnRoom.escape() + "W" + Color.RESET;
        map[verticalSpotStandard*cont][maxHorizontalMapLength + 7] = spawnRoom.escape() + "N" + Color.RESET;

        map[verticalSpotStandard*cont][maxHorizontalMapLength + 9] = spawnRoom.escape() + "S" + Color.RESET;
        map[verticalSpotStandard*cont][maxHorizontalMapLength + 10] = spawnRoom.escape() + "T" + Color.RESET;
        map[verticalSpotStandard*cont][maxHorizontalMapLength + 11] = spawnRoom.escape() + "O" + Color.RESET;
        map[verticalSpotStandard*cont][maxHorizontalMapLength + 12] = spawnRoom.escape() + "R" + Color.RESET;
        map[verticalSpotStandard*cont][maxHorizontalMapLength + 13] = spawnRoom.escape() + "E" + Color.RESET;
        map[verticalSpotStandard*cont][maxHorizontalMapLength + 14] = spawnRoom.escape() + ":" + Color.RESET;

        insertWeapon(jsonSpot.get("weaponList").getAsJsonArray(), spawnRoom.escape());

        cont++;
        if(cont == 3)
            cont = 0;
    }

    private static void insertWeapon(JsonArray weaponList, String spawnRoomEscape) {

        for(int i = 0; i < weaponList.size(); i++){
            map[verticalSpotStandard*cont + i + 1][maxHorizontalMapLength + 3] = spawnRoomEscape + i + Color.RESET;
            map[verticalSpotStandard*cont + i + 1][maxHorizontalMapLength + 4] = spawnRoomEscape + ":" + Color.RESET;
            String weaponName = weaponList.get(i).getAsJsonObject().get("weaponName").getAsString();

            for(int j = 0; j < weaponName.length(); j++){
                char character = weaponName.charAt(j);
                map[verticalSpotStandard*cont + i + 1][maxHorizontalMapLength + 5 + j] = spawnRoomEscape + character + Color.RESET;
            }

        }
    }


    public static void main(String[] argv){
        buildCliMap(MapName.FIRE);

        JsonObject jsonMapTry = JsonDeserializer.myJsonParser.parse(jsonTry).getAsJsonObject();

        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("gino");
        nicknames.add("meme");
        nicknames.add("andreaalf");

        ArrayList <PlayerColor> playerColors = PlayerColor.getRandomArray(3);

        fillMapWithAmmoAndCoord(jsonMapTry, playerColors, nicknames );


        printMap();
    }


}
