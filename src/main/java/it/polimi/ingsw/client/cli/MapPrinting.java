package it.polimi.ingsw.client.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.model.map.Room;


public class MapPrinting {

    //attributes valid for all spots length
    public static final int horizontalSpotStandard = 13;
    public static final int verticalSpotStandard = 7;

    //attributes for ammo label in each spot
    public static final int horizontalAmmoStandard = 2;
    public static final int verticalAmmoStandard = 1;

    //attributes for coord label
    public static final int horizontalCoordStandard = 5;
    public static final int verticalCoordStandard = 5;

    //attributes fro map building
    private static final int maxHorizontalLength = 53; //53 - 5 separazioni = 48 che diviso 4 fa 12 caselle a spot
    private  static final int maxVerticalLength = 21; //7 caselle di altezza
    public static String[][] map = new String[maxVerticalLength][maxHorizontalLength];

    //don't touch this, need this for testing if filling correctly cli map
    public static String jsonTry = "  {\"players\":[{\"nickname\":\"gino\",\"nRedAmmo\":1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"nDeaths\":0,\"xPosition\":-1,\"yPosition\":-1,\"isDead\":true,\"damages\":[],\"marks\":[],\"nMoves\":3,\"nMovesBeforeGrabbing\":1,\"nMovesBeforeShooting\":0,\"canReloadBeforeShooting\":false,\"playerStatus\":{\"isFirstTurn\":true,\"isFrenzyTurn\":false,\"isActive\":false,\"nActionsDone\":0,\"nActions\":2,\"isConnected\":false},\"weaponList\":[],\"powerUpList\":[]},{\"nickname\":\"andreaalf\",\"nRedAmmo\":1,\"yPosition\":-1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"nDeaths\":0,\"xPosition\":-1,\"isDead\":true,\"damages\":[],\"marks\":[]},{\"nickname\":\"meme\",\"nRedAmmo\":1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"nDeaths\":0,\"xPosition\":-1,\"yPosition\":-1,\"isDead\":true,\"damages\":[],\"marks\":[]}],\"playerNames\":[\"gino\",\"andreaalf\",\"meme\"],\"kst\":{\"skullList\":[\"SKULL\",\"SKULL\",\"SKULL\",\"SKULL\",\"SKULL\"],\"isOverkill\":[false,false,false,false,false]},\"gameMap\":{\"row0\":{\"col0\":{\"spot\":{\"ammoColorList\":[\"BLUE\",\"YELLOW\",\"RED\"],\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"SAPPHIRE\"}},\"col1\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"powerup\":{\"powerUpName\":\"Teleporter\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":100,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},\"playersHere\":[],\"doors\":[false,false,false,false],\"room\":\"SAPPHIRE\"}},\"col2\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"LockRifle\",\"cost\":[\"BLUE\",\"BLUE\"],\"effects\":[{\"nDamages\":2,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1]]},{\"weaponName\":\"MachineGun\",\"cost\":[\"BLUE\",\"RED\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":2,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1,2,3],[0,2,3,1],[0,2,1],[0,3,1],[0,1,4],[0,4,1]]},{\"weaponName\":\"Thor\",\"cost\":[\"BLUE\",\"RED\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"LASTONEATTACKED\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"LASTONEATTACKED\"}],\"isLoaded\":true,\"order\":[[0,1,2]]}],\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"SAPPHIRE\"}},\"col3\":{\"spot\":{}}},\"row1\":{\"col0\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"PlasmaGun\",\"cost\":[\"BLUE\",\"YELLOW\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":2,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2],[1,0,2],[0,2,1]]},{\"weaponName\":\"Whisper\",\"cost\":[\"BLUE\",\"BLUE\",\"YELLOW\"],\"effects\":[{\"nDamages\":3,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":2,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0]]},{\"weaponName\":\"Electroscythe\",\"cost\":[\"BLUE\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\",\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]}],\"playersHere\":[],\"doors\":[true,false,false,false],\"room\":\"RUBY\"}},\"col1\":{\"spot\":{\"ammoColorList\":[\"BLUE\",\"BLUE\"],\"powerup\":{\"powerUpName\":\"TargetingScope\",\"color\":\"RED\",\"effect\":{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"ANY\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},\"isTurnPowerup\":false},\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"RUBY\"}},\"col2\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"powerup\":{\"powerUpName\":\"Newton\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},\"playersHere\":[],\"doors\":[true,true,false,false],\"room\":\"RUBY\"}},\"col3\":{\"spot\":{\"ammoColorList\":[\"RED\",\"RED\"],\"powerup\":{\"powerUpName\":\"TagbackGrenade\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},\"isTurnPowerup\":false},\"playersHere\":[],\"doors\":[false,false,false,true],\"room\":\"TOPAZ\"}}},\"row2\":{\"col0\":{\"spot\":{}},\"col1\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"BLUE\",\"YELLOW\"],\"playersHere\":[],\"doors\":[true,false,false,false],\"room\":\"DIAMOND\"}},\"col2\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"BLUE\",\"RED\"],\"playersHere\":[],\"doors\":[false,true,false,false],\"room\":\"DIAMOND\"}},\"col3\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"TractorBeam\",\"cost\":[\"BLUE\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"RED\",\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":3,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1],[2,3]]},{\"weaponName\":\"VortexCannon\",\"cost\":[\"RED\",\"BLUE\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2,3,4,3]]},{\"weaponName\":\"Furnace\",\"cost\":[\"RED\",\"BLUE\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":4,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":1,\"nPlayersMarkable\":4,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":1,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]}],\"playersHere\":[],\"doors\":[false,false,false,true],\"room\":\"TOPAZ\"}}}}}";
    

    public static void fillMapWithAmmoAndCoord(){

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonRoot = jsonParser.parse(jsonTry).getAsJsonObject();
        JsonObject jsonGameMap = jsonRoot.get("gameMap").getAsJsonObject();

        for(int r = 0; r < 3; r++){

            JsonObject jsonRow = jsonGameMap.get("row" + r).getAsJsonObject();

            for(int c = 0; c < 4; c++){

                JsonObject jsonSpot = jsonRow.get("col" + c).getAsJsonObject().get("spot").getAsJsonObject(); // <== jsonSpot

                if(!jsonSpot.toString().equals("{}")) { //appena sono sicuro di avere uno spot valido nel json posso istanziare uno spawnspot o un ammo spot

                    fillCliSpot(r, c, jsonSpot);
                    fillCoord(r, c);
                }
            }
        }
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

        if (jsonSpot.get("ammoColorList") != null){

            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard] = "A";
            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 1] = "M";
            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 2] = "M";
            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 3] = "O";
            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 4] = ":";

            JsonArray jsonAmmoColorList = jsonSpot.get("ammoColorList").getAsJsonArray();
            for(int i = 0; i < jsonAmmoColorList.size(); i++){
                Color ammoColor = Color.valueOf(jsonAmmoColorList.get(i).getAsString().toUpperCase());
                fillAmmoColorList(r*verticalSpotStandard + verticalAmmoStandard, c*horizontalSpotStandard + horizontalAmmoStandard + 6 + 2*i, ammoColor);
            }

            if(jsonSpot.get("powerup") != null)
                fillAmmoWithPowerUp(r*verticalSpotStandard + verticalAmmoStandard,c*horizontalSpotStandard + horizontalAmmoStandard + 10);
        }
        else{
            Room room = Room.valueOf(jsonSpot.get("room").getAsString().toUpperCase());

            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard] = room.escape() + "S" + Color.RESET;
            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 1] = room.escape() + "P" + Color.RESET;
            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 2] = room.escape() + "A" + Color.RESET;
            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 3] = room.escape() + "W" + Color.RESET;
            map[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 4] = room.escape() + "N" + Color.RESET;

            map[r*verticalSpotStandard + verticalAmmoStandard + 1][c*horizontalSpotStandard + horizontalAmmoStandard] = room.escape() + "S" + Color.RESET;
            map[r*verticalSpotStandard + verticalAmmoStandard + 1][c*horizontalSpotStandard + horizontalAmmoStandard + 1] = room.escape() + "P" + Color.RESET;
            map[r*verticalSpotStandard + verticalAmmoStandard + 1][c*horizontalSpotStandard + horizontalAmmoStandard + 2] = room.escape() + "O" + Color.RESET;
            map[r*verticalSpotStandard + verticalAmmoStandard + 1][c*horizontalSpotStandard + horizontalAmmoStandard + 3] = room.escape() + "T" + Color.RESET;

        }
    }

    private static void fillAmmoWithPowerUp(int r, int c) {
        map[r][c] = "P";
    }

    private static void fillAmmoColorList(int r, int c, Color ammoColor) {
        map[r][c] = ammoColor.escape() + "A" + Color.RESET;
    }

    public static void fillMapFire(){

        map[0][0] = Color.BLUE.escape() + "╔" + Color.RESET;

        for(int c = 1; c < maxHorizontalLength - 14; c++){
            map[0][c] = Color.BLUE.escape() + "═" + Color.RESET;
        }

        map[0][maxHorizontalLength - 14] = Color.BLUE.escape() + "╗" + Color.RESET;

        for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++){
            map[0][c] = "";
        }  //first line filled

        for(int r = 1 ; r < 6; r++){

            map[r][0] = Color.BLUE.escape() + "║" + Color.RESET;

            for(int c = 1 ; c < maxHorizontalLength - 14; c++){

                if(c == 13 || c == 26){
                    map[r][c] = Color.BLUE.escape() + "│" + Color.RESET;
                }
                else {
                    map[r][c] = " ";
                }

            }

            map[r][maxHorizontalLength - 14] = Color.BLUE.escape() + "║" + Color.RESET;

            for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++)
                map[r][c] = "";
        }
        //first row with all columns

        map[6][0] =Color.BLUE.escape() + "╚" + Color.RESET;

        for(int c = 1; c < maxHorizontalLength - 14; c++) {
            if(c == 6 || c == 33){
                map[6][c] = Color.BLUE.escape() + "╕" + Color.RESET;
            }
            else if (c == 7 || c == 34){
                map[6][c] = " ";
            }
            else if(c == 8 || c == 35){
                map[6][c] = Color.BLUE.escape() + "╒" + Color.RESET;
            }
            else{
                map[6][c] = Color.BLUE.escape() + "═" + Color.RESET;
            }
        }

        map[6][maxHorizontalLength - 14] = Color.BLUE.escape() + "╝" + Color.RESET;

        for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++)
            map[6][c] = "";

        //finish blue room


        map[7][0] = Color.RED.escape() + "╔" + Color.RESET;

        for(int c = 1; c < maxHorizontalLength - 14; c++){
            if(c == 6 || c == 33){
                map[7][c] = Color.RED.escape() + "╛" + Color.RESET;
            }
            else if( c == 7 || c == 34){
                map[7][c] = " ";
            }
            else if (c == 8 || c == 35){
                map[7][c] = Color.RED.escape() + "╘" + Color.RESET;
            }
            else{
                map[7][c] = Color.RED.escape() + "═" + Color.RESET;
            }
        }

        map[7][maxHorizontalLength - 14] = Color.RED.escape() + "╗" + Color.RESET;
        map[7][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╔" + Color.RESET;

        for ( int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++){
            map[7][c] = Color.YELLOW.escape() + "═" + Color.RESET;
        }
        map[7][maxHorizontalLength - 1] = Color.YELLOW.escape() + "╗" + Color.RESET;

        for(int r = 8 ; r < 13 ; r++ ){

            map[r][0] = Color.RED.escape() + "║" + Color.RESET;

            for(int c = 1; c < maxHorizontalLength - 14; c++){
                if (c == 13 || c == 26){
                    map[r][c] = Color.RED.escape() + "│" + Color.RESET;
                }
                else{
                    map[r][c] = " ";
                }
            }

            if( r == 9) {
                map[r][maxHorizontalLength - 14] = Color.RED.escape() + "╙" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╜" + Color.RESET;
            }
            else if(r == 10){
                map[r][maxHorizontalLength - 14] = Color.RED.escape() + "╓" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╖" + Color.RESET;
            }
            else{
                map[r][maxHorizontalLength - 14] = Color.RED.escape() + "║" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "║" + Color.RESET;
            }

            for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
                map[r][c] = " ";

            map[r][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;

        }

        map[13][0] = Color.RED.escape() + "╚" + Color.RESET;
        for(int c = 1 ; c < maxHorizontalLength - 14; c++){
            if( c == 17){
                map[13][c] = Color.RED.escape() + "╕" + Color.RESET;
            }
            else if( c == 18){
                map[13][c] = " ";
            }
            else if (c == 19){
                map[13][c] = Color.RED.escape() + "╒" + Color.RESET;

            }
            else {
                map[13][c] = Color.RED.escape() + "═" + Color.RESET;
            }
        }

        map[13][maxHorizontalLength - 14] = Color.RED.escape() + "╝" + Color.RESET;
        map[13][maxHorizontalLength - 13] = Color.YELLOW.escape() + "║" + Color.RESET;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++ ){
            map[13][c] =  Color.YELLOW.escape() + "_" + Color.RESET;
        }

        map[13][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;

        //14 row
        for (int c = 0; c < 13; c++){
            map[14][c] = " ";
        }

        map[14][13] = Color.WHITE.escape() + "╔" + Color.RESET;
        for(int c = 14; c < maxHorizontalLength - 14; c++){
            if (c == 17){
                map[14][c] = Color.WHITE.escape() + "╛" + Color.RESET;
            }
            else if( c == 18){
                map[14][c] = Color.WHITE.escape() + " " + Color.RESET;
            }
            else if( c == 19){
                map[14][c] = Color.WHITE.escape() + "╘" + Color.RESET;
            }
            else
                map[14][c] = Color.WHITE.escape() + "═" + Color.RESET;
        }

        map[14][maxHorizontalLength - 14] = Color.WHITE.escape() + "╗" + Color.RESET;
        map[14][maxHorizontalLength - 13] = Color.YELLOW.escape() + "║" + Color.RESET;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++){
            map[14][c] = " ";
        }
        map[14][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;

        for(int r = 15; r < 20; r++){
            for(int c = 0; c < 13; c++){
                map[r][c] = " ";
            }

            map[r][13] = Color.WHITE.escape() + "║" + Color.RESET;

            for(int c = 14; c < maxHorizontalLength - 14; c++){
                if (c == 26){
                    map[r][c] = Color.WHITE.escape() + "│" + Color.RESET;
                }
                else{
                    map[r][c] = " ";
                }
            }

            if( r == 16) {
                map[r][maxHorizontalLength - 14] = Color.WHITE.escape() + "╙" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╜" + Color.RESET;
            }
            else if(r == 17){
                map[r][maxHorizontalLength - 14] = Color.WHITE.escape() + "╓" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╖" + Color.RESET;
            }
            else{
                map[r][maxHorizontalLength - 14] = Color.WHITE.escape() + "║" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "║" + Color.RESET;
            }

            for (int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
                map[r][c] = " ";

            map[r][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;
        }


        for (int c = 0; c < 13; c++){
            map[20][c] = " ";
        }

        map[20][13] = Color.WHITE.escape() + "╚" + Color.RESET;

        for(int c = 14; c < maxHorizontalLength - 14; c++)
            map[20][c] = Color.WHITE.escape() + "═" + Color.RESET;

        map[20][maxHorizontalLength - 14] = Color.WHITE.escape() + "╝" + Color.RESET;

        map[20][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╚" + Color.RESET;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
            map[20][c] = Color.YELLOW.escape() + "═" + Color.RESET;

        map[20][maxHorizontalLength - 1] = Color.YELLOW.escape() + "╝" + Color.RESET;
    }

    private static void fillMapWater(){

        map[0][0] = Color.RED.escape() + "╔" + Color.RESET;

        for(int c = 1; c < 13; c++){
            map[0][c] = Color.RED.escape() + "═" + Color.RESET;
        }

        map[0][13] = Color.RED.escape() + "╗" + Color.RESET;
        map[0][14] = Color.BLUE.escape() + "╔" + Color.RESET;

        for(int c = 15; c < maxHorizontalLength - 14; c++){
            map[0][c] = Color.BLUE.escape() + "═" + Color.RESET;
        }  //first line filled

        for(int r = 1 ; r < 6; r++){

            map[r][0] = Color.RED.escape() + "║" + Color.RESET;

            for(int c = 1 ; c < maxHorizontalLength - 14; c++){
                if(c == 13){
                    map[r][c] = Color.RED.escape() + "║" + Color.RESET;
                }
                else if(c == 14){
                    map[r][c] = Color.BLUE.escape() + "║" + Color.RESET;
                }
                else if(c == 26){
                    map[r][c] = Color.BLUE.escape() + "│" + Color.RESET;
                }
                else {
                    map[r][c] = " ";
                }

                //ADDING DOORS TO FIRST SPOTS ROW
                if(r == 2){
                    if(c == 13)
                        map[r][c] = Color.RED.escape() + "╙" + Color.RESET;
                    else if(c == 14)
                        map[r][c] = Color.BLUE.escape() + "╜" + Color.RESET;
                }
                else if (r == 3){
                    if (c == 13)
                        map[r][c] = Color.RED.escape() + "╓" + Color.RESET;
                    else if(c == 14)
                        map[r][c] = Color.BLUE.escape() + "╖" + Color.RESET;
                }

            }

            map[r][maxHorizontalLength - 14] = Color.BLUE.escape() + "║" + Color.RESET;

            for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++)
                map[r][c] = "";
        }
        //first row with all columns

        map[6][0] = Color.RED.escape() + "║" + Color.RESET;

        for(int c = 1; c < 13; c++) {
            map[6][c] = Color.RED.escape() + "_" + Color.RESET;
        }

        map[6][13] =  Color.RED.escape() + "║" + Color.RESET;
        map[6][14] =  Color.BLUE.escape() + "╚" + Color.RESET;

        for(int c = 15; c < maxHorizontalLength - 14; c++ ) {
            if (c == 19 || c == 32)
                map[6][c] = Color.BLUE.escape() + "╕" + Color.RESET;
            else if (c == 20 || c == 33)
                map[6][c] = " ";
            else if (c == 21 || c == 34)
                map[6][c] = Color.BLUE.escape() + "╒" + Color.RESET;
            else
                 map[6][c] = Color.BLUE.escape() + "═" + Color.RESET;
        }

        map[6][maxHorizontalLength - 14] = Color.BLUE.escape() + "╝" + Color.RESET;

        for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++)
            map[6][c] = "";

        map[7][0] = Color.RED.escape() + "║" + Color.RESET;

        for(int c = 1; c < 13; c++)
            map[7][c] = " ";

        map[7][13] = Color.RED.escape() + "║" + Color.RESET;
        map[7][14] = Color.PURPLE.escape() + "╔" + Color.RESET;

        for(int c = 15; c < maxHorizontalLength - 14; c++){
            if(c == 19 || c == 32)
                map[7][c] = Color.PURPLE.escape() + "╛" + Color.RESET;
            else if (c == 20 || c == 33)
                map[7][c] = " ";
            else if( c == 21 || c == 34)
                map[7][c] = Color.PURPLE.escape() + "╘" + Color.RESET;
            else
                map[7][c] = Color.PURPLE.escape() + "═" + Color.RESET;
        }

        map[7][maxHorizontalLength - 14] = Color.PURPLE.escape() + "╗" + Color.RESET;
        map[7][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╔" + Color.RESET;

        for ( int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++){
            map[7][c] = Color.YELLOW.escape() + "═" + Color.RESET;
        }
        map[7][maxHorizontalLength - 1] = Color.YELLOW.escape() + "╗" + Color.RESET;



        for(int r = 8 ; r < 13 ; r++ ){

            map[r][0] = Color.RED.escape() + "║" + Color.RESET;

            for(int c = 1; c < 13; c++)
                    map[r][c] = " ";

            if( r == 9) {
                map[r][13] = Color.RED.escape() + "╙" + Color.RESET;
                map[r][14] = Color.PURPLE.escape() + "╜" + Color.RESET;
            }
            else if(r == 10){
                map[r][13] = Color.RED.escape() + "╓" + Color.RESET;
                map[r][14] = Color.PURPLE.escape() + "╖" + Color.RESET;
            }
            else{
                map[r][13] = Color.RED.escape() + "║" + Color.RESET;
                map[r][14] = Color.PURPLE.escape() + "║" + Color.RESET;
            }

            for(int c = 15; c < maxHorizontalLength - 14; c++) {
                if (c == 26)
                    map[r][c] = Color.PURPLE.escape() + "│" + Color.RESET;
                else
                    map[r][c] = " ";
            }

            if( r == 9) {
                map[r][maxHorizontalLength - 14] = Color.PURPLE.escape() + "╙" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╜" + Color.RESET;
            }
            else if ( r == 10){
                map[r][maxHorizontalLength - 14] = Color.PURPLE.escape() + "╓" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╖" + Color.RESET;
            }
            else {
                map[r][maxHorizontalLength - 14] = Color.PURPLE.escape() + "║" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "║" + Color.RESET;
            }

            for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
                map[r][c] = " ";

            map[r][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;
        }

        map[13][0] = Color.RED.escape() + "╚" + Color.RESET;

        for(int c = 1 ; c < 13; c++){
            if(c == 5)
                map[13][c] = Color.RED.escape() + "╕" + Color.RESET;
            else if ( c == 6)
                map[13][c] = " ";
            else if ( c == 7)
                map[13][c] = Color.RED.escape() + "╒" + Color.RESET;
            else
                map[13][c] = Color.RED.escape() + "═" + Color.RESET;
        }

        map[13][13] = Color.RED.escape() + "╝" + Color.RESET;
        map[13][14] = Color.RED.escape() + "╚" + Color.RESET;

        for(int c = 15; c < maxHorizontalLength - 14; c ++) {
            if ( c == 19)
                map[13][c] = Color.PURPLE.escape() + "╕" + Color.RESET;
            else if (c == 20)
                map[13][c] = " ";
            else if( c == 21)
                map[13][c] = Color.PURPLE.escape() + "╒" + Color.RESET;
            else
                map[13][c] = Color.PURPLE.escape() + "═" + Color.RESET;

        }

        map[13][maxHorizontalLength - 14] = Color.PURPLE.escape() + "╝" + Color.RESET;
        map[13][maxHorizontalLength - 13] = Color.YELLOW.escape() + "║" + Color.RESET;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++ ){
            map[13][c] =  Color.YELLOW.escape() + "_" + Color.RESET;
        }

        map[13][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;

        //14 row

        map[14][0] = Color.WHITE.escape() + "╔" + Color.RESET;
        for (int c = 1; c < 14; c++){
            if(c == 5)
                map[14][c] = Color.WHITE.escape() + "╛" + Color.RESET;
            else if (c == 6)
                map[14][c] = " ";
            else if (c == 7)
                map[14][c] = Color.WHITE.escape() + "╘" + Color.RESET;
            else
            map[14][c] = "═";
        }


        for(int c = 15; c < maxHorizontalLength - 14; c++){
            if (c == 19){
                map[14][c] = Color.WHITE.escape() + "╛" + Color.RESET;
            }
            else if( c == 20){
                map[14][c] = Color.WHITE.escape() + " " + Color.RESET;
            }
            else if( c == 21){
                map[14][c] = Color.WHITE.escape() + "╘" + Color.RESET;
            }
            else
                map[14][c] = Color.WHITE.escape() + "═" + Color.RESET;
        }

        map[14][maxHorizontalLength - 14] = Color.WHITE.escape() + "╗" + Color.RESET;
        map[14][maxHorizontalLength - 13] = Color.YELLOW.escape() + "║" + Color.RESET;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++){
            map[14][c] = " ";
        }
        map[14][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;

        for(int r = 15; r < 20; r++){

            map[r][0] = Color.WHITE.escape() + "║" + Color.RESET;

            for(int c = 1; c < 13; c++){

                map[r][c] = " ";
            }

            map[r][13] = Color.WHITE.escape() + "│" + Color.RESET;

            for(int c = 14; c < maxHorizontalLength - 14; c++){
                if (c == 26){
                    map[r][c] = Color.WHITE.escape() + "│" + Color.RESET;
                }
                else{
                    map[r][c] = " ";
                }
            }

            if( r == 16) {
                map[r][maxHorizontalLength - 14] = Color.WHITE.escape() + "╙" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╜" + Color.RESET;
            }
            else if(r == 17){
                map[r][maxHorizontalLength - 14] = Color.WHITE.escape() + "╓" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╖" + Color.RESET;
            }
            else{
                map[r][maxHorizontalLength - 14] = Color.WHITE.escape() + "║" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.YELLOW.escape() + "║" + Color.RESET;
            }

            for (int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
                map[r][c] = " ";

            map[r][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;
        }


        map[20][0] = Color.WHITE.escape() + "╚" + Color.RESET;

        for(int c = 1; c < maxHorizontalLength - 14; c++)
            map[20][c] = Color.WHITE.escape() + "═" + Color.RESET;

        map[20][maxHorizontalLength - 14] = Color.WHITE.escape() + "╝" + Color.RESET;

        map[20][maxHorizontalLength - 13] = Color.YELLOW.escape() + "╚" + Color.RESET;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
            map[20][c] = Color.YELLOW.escape() + "═" + Color.RESET;

        map[20][maxHorizontalLength - 1] = Color.YELLOW.escape() + "╝" + Color.RESET;
    }

    private static void fillMapEarth() {

    }

    private static void fillMapWind(){

        map[0][0] = Color.BLUE.escape() + "╔" + Color.RESET;

        for(int c = 1; c < maxHorizontalLength - 14; c++){
            map[0][c] = Color.BLUE.escape() + "═" + Color.RESET;
        }

        map[0][maxHorizontalLength - 14] = Color.BLUE.escape() + "╗" + Color.RESET;
        map[0][maxHorizontalLength - 13] = Color.GREEN.escape() + "╔" + Color.RESET;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++){
            map[0][c] = Color.GREEN.escape() + "═" + Color.RESET;
        }  //first line filled

        map[0][maxHorizontalLength - 1] = Color.GREEN.escape() + "╗" + Color.RESET;

        for(int r = 1 ; r < 6; r++){

            map[r][0] = Color.BLUE.escape() + "║" + Color.RESET;

            for(int c = 1 ; c < maxHorizontalLength - 14; c++){

                if(c == 13 || c == 26){
                    map[r][c] = Color.BLUE.escape() + "│" + Color.RESET;
                }
                else {
                    map[r][c] = " ";
                }

            }

            if( r == 2) {
                map[r][maxHorizontalLength - 14] = Color.BLUE.escape() + "╙" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.GREEN.escape() + "╜" + Color.RESET;
            }
            else if(r == 3){
                map[r][maxHorizontalLength - 14] = Color.BLUE.escape() + "╓" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.GREEN.escape() + "╖" + Color.RESET;
            }
            else{
                map[r][maxHorizontalLength - 14] = Color.BLUE.escape() + "║" + Color.RESET;
                map[r][maxHorizontalLength - 13] = Color.GREEN.escape() + "║" + Color.RESET;
            }

            for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
                map[r][c] = " ";

            map[r][maxHorizontalLength - 1] = Color.GREEN.escape() + "║" + Color.RESET;



        }
        //first row with all columns

        map[6][0] =Color.BLUE.escape() + "╚" + Color.RESET;

        for(int c = 1; c < maxHorizontalLength - 14; c++) {
            if(c == 6 || c == 33){
                map[6][c] = Color.BLUE.escape() + "╕" + Color.RESET;
            }
            else if (c == 7 || c == 34){
                map[6][c] = " ";
            }
            else if(c == 8 || c == 35){
                map[6][c] = Color.BLUE.escape() + "╒" + Color.RESET;
            }
            else{
                map[6][c] = Color.BLUE.escape() + "═" + Color.RESET;
            }
        }

        map[6][maxHorizontalLength - 14] = Color.BLUE.escape() + "╝" + Color.RESET;
        map[6][maxHorizontalLength - 13] = Color.GREEN.escape() + "╚" + Color.RESET;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++) {
            if ( c == 46){
                map[6][c] = Color.GREEN.escape() + "╕" + Color.RESET;
            }
            else if ( c == 47){
                map[6][c] = " ";
            }
            else if ( c == 48){
                map[6][c] = Color.GREEN.escape() + "╒" + Color.RESET;
            }
            else {
                map[6][c] = Color.GREEN.escape() + "═" + Color.RESET;
            }
        }

        map[6][maxHorizontalLength - 1] = Color.GREEN.escape() + "╝" + Color.RESET;
        //finish blue room


        map[7][0] = Color.RED.escape() + "╔" + Color.RESET;

        for(int c = 1; c < 26; c++){
            if(c == 6 ){
                map[7][c] = Color.RED.escape() + "╛" + Color.RESET;
            }
            else if( c == 7){
                map[7][c] = " ";
            }
            else if (c == 8){
                map[7][c] = Color.RED.escape() + "╘" + Color.RESET;
            }
            else{
                map[7][c] = Color.RED.escape() + "═" + Color.RESET;
            }
        }

        map[7][26] = Color.RED.escape() + "╗" + Color.RESET;
        map[7][27] = Color.YELLOW.escape() + "╔" + Color.RESET;

        for ( int c = 28; c < maxHorizontalLength - 1; c++){
            if (c == 33 || c == 46){
                map[7][c] = Color.YELLOW.escape() + "╛" + Color.RESET;
            }
            else if (c == 34 || c == 47){
                map[7][c] = " ";

            }
            else if (c == 35 || c == 48){
                map[7][c] =  Color.YELLOW.escape() + "╘" + Color.RESET;
            }
            else {
                map[7][c] = Color.YELLOW.escape() + "═" + Color.RESET;
            }
        }

        map[7][maxHorizontalLength - 1] = Color.YELLOW.escape() + "╗" + Color.RESET;

        for(int r = 8 ; r < 13 ; r++ ){

            map[r][0] = Color.RED.escape() + "║" + Color.RESET;

            for(int c = 1; c < 26; c++){
                if (c == 13 ){
                    map[r][c] = Color.RED.escape() + "│" + Color.RESET;
                }
                else{
                    map[r][c] = " ";
                }
            }

            map[r][26] = Color.RED.escape() + "║" + Color.RESET;
            map[r][27] = Color.YELLOW.escape() + "║" + Color.RESET;

            for(int c = 28; c < maxHorizontalLength - 1; c++) {
                if ( c == 39){
                    map[r][c] = Color.YELLOW.escape() + "│" + Color.RESET;
                }
                else {
                    map[r][c] = " ";
                }
            }

            map[r][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;

        }


        map[13][0] = Color.RED.escape() + "╚" + Color.RESET;
        for(int c = 1 ; c < 26; c++){
            if( c == 17){
                map[13][c] = Color.RED.escape() + "╕" + Color.RESET;
            }
            else if( c == 18){
                map[13][c] = " ";
            }
            else if (c == 19){
                map[13][c] = Color.RED.escape() + "╒" + Color.RESET;

            }
            else {
                map[13][c] = Color.RED.escape() + "═" + Color.RESET;
            }
        }

        map[13][26] = Color.RED.escape() + "╝" + Color.RESET;
        map[13][27] = Color.YELLOW.escape() + "║" + Color.RESET;

        for(int c = 28; c < maxHorizontalLength - 1; c++ ){
            if ( c == 39){
                map[13][c] = Color.YELLOW.escape() + "│" + Color.RESET;
            }
            else {
                map[13][c] = Color.YELLOW.escape() + "_" + Color.RESET;
            }
        }

        map[13][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;

        //14 row
        for (int c = 0; c < 13; c++){
            map[14][c] = " ";
        }

        map[14][13] = Color.WHITE.escape() + "╔" + Color.RESET;

        for(int c = 14; c < 26; c++){
            if (c == 17){
                map[14][c] = Color.WHITE.escape() + "╛" + Color.RESET;
            }
            else if( c == 18){
                map[14][c] = Color.WHITE.escape() + " " + Color.RESET;
            }
            else if( c == 19){
                map[14][c] = Color.WHITE.escape() + "╘" + Color.RESET;
            }
            else
                map[14][c] = Color.WHITE.escape() + "═" + Color.RESET;
        }

        map[14][26] = Color.WHITE.escape() + "╗" + Color.RESET;
        map[14][27] = Color.YELLOW.escape() + "║" + Color.RESET;

        for(int c = 28; c < maxHorizontalLength - 1; c++){
            if ( c == 39){
                map[14][c] = Color.YELLOW.escape() + "│" + Color.RESET;
            }
            else {
                map[14][c] = " ";
            }
        }
        map[14][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;

        for(int r = 15; r < 20; r++){
            for(int c = 0; c < 13; c++){
                map[r][c] = " ";
            }

            map[r][13] = Color.WHITE.escape() + "║" + Color.RESET;

            for(int c = 14; c < 26; c++){
                    map[r][c] = " ";
            }

            if( r == 16) {
                map[r][26] = Color.WHITE.escape() + "╙" + Color.RESET;
                map[r][27] = Color.YELLOW.escape() + "╜" + Color.RESET;
            }
            else if(r == 17){
                map[r][26] = Color.WHITE.escape() + "╓" + Color.RESET;
                map[r][27] = Color.YELLOW.escape() + "╖" + Color.RESET;
            }
            else{
                map[r][26] = Color.WHITE.escape() + "║" + Color.RESET;
                map[r][27] = Color.YELLOW.escape() + "║" + Color.RESET;
            }

            for (int c = 28; c < maxHorizontalLength - 1; c++) {
                if(c == 39){
                    map[r][c] = Color.YELLOW.escape() + "│" + Color.RESET;
                }
                else {
                    map[r][c] = " ";
                }
            }

            map[r][maxHorizontalLength - 1] = Color.YELLOW.escape() + "║" + Color.RESET;
        }


        for (int c = 0; c < 13; c++){
            map[20][c] = " ";
        }

        map[20][13] = Color.WHITE.escape() + "╚" + Color.RESET;

        for(int c = 14; c < 26; c++)
            map[20][c] = Color.WHITE.escape() + "═" + Color.RESET;

        map[20][26] = Color.WHITE.escape() + "╝" + Color.RESET;

        map[20][27] = Color.YELLOW.escape() + "╚" + Color.RESET;

        for(int c = 28; c < maxHorizontalLength - 1; c++) {
            map[20][c] = Color.YELLOW.escape() + "═" + Color.RESET;
        }

        map[20][maxHorizontalLength - 1] = Color.YELLOW.escape() + "╝" + Color.RESET;
    }



    public static void fillMap(MapName mapName){
        if (mapName.equals(MapName.FIRE))
            fillMapFire();
        else if(mapName.equals(MapName.WATER))
            fillMapWater();
        else if(mapName.equals(MapName.EARTH))
            fillMapEarth();
        else
            fillMapWind();
    }

    public static void printMyShit(){
        for (int r = 0; r < maxVerticalLength; r++) {

            System.out.println();

            for (int c = 0; c < maxHorizontalLength; c++) {

                System.out.print(map[r][c]);

            }

        }
    }

    public static void printMapFire(){
        fillMapFire();
        fillMapWithAmmoAndCoord();
        printMyShit();
    }

    public static void printMapWater(){
        fillMapWater();
        printMyShit();
    }

    public static void printMapWind(){
        fillMapWind();
        printMyShit();
    }

    public static void main(String[] args){
        printMapFire();
        printMapWater();
        printMapWind();
    }

}
