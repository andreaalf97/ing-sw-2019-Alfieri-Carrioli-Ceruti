package it.polimi.ingsw.client.cli;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class MapPrinting {

    public static final int horizontalSpotStandard = 13;
    public static final int verticalSpotStandard = 7;
    public static final int horizontalAmmoStandard = 3;
    public static final int verticalAmmoStandard = 1;
    private static final int maxHorizontalLength = 53; //53 - 5 separazioni = 48 che diviso 4 fa 12 caselle a spot
    private  static final int maxVerticalLength = 21; //7 caselle di altezza
    public static String[][] mapFire = new String[maxVerticalLength][maxHorizontalLength];

    //don't touch this, need this for testing if filling correctly cli map
    public static String jsonTry = "  {\"players\":[{\"nickname\":\"gino\",\"nRedAmmo\":1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"nDeaths\":0,\"xPosition\":-1,\"yPosition\":-1,\"isDead\":true,\"damages\":[],\"marks\":[],\"nMoves\":3,\"nMovesBeforeGrabbing\":1,\"nMovesBeforeShooting\":0,\"canReloadBeforeShooting\":false,\"playerStatus\":{\"isFirstTurn\":true,\"isFrenzyTurn\":false,\"isActive\":false,\"nActionsDone\":0,\"nActions\":2,\"isConnected\":false},\"weaponList\":[],\"powerUpList\":[]},{\"nickname\":\"andreaalf\",\"nRedAmmo\":1,\"yPosition\":-1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"nDeaths\":0,\"xPosition\":-1,\"isDead\":true,\"damages\":[],\"marks\":[]},{\"nickname\":\"meme\",\"nRedAmmo\":1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"nDeaths\":0,\"xPosition\":-1,\"yPosition\":-1,\"isDead\":true,\"damages\":[],\"marks\":[]}],\"playerNames\":[\"gino\",\"andreaalf\",\"meme\"],\"kst\":{\"skullList\":[\"SKULL\",\"SKULL\",\"SKULL\",\"SKULL\",\"SKULL\"],\"isOverkill\":[false,false,false,false,false]},\"gameMap\":{\"row0\":{\"col0\":{\"spot\":{\"ammoColorList\":[\"BLUE\",\"YELLOW\",\"RED\"],\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"SAPPHIRE\"}},\"col1\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"powerup\":{\"powerUpName\":\"Teleporter\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":100,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},\"playersHere\":[],\"doors\":[false,false,false,false],\"room\":\"SAPPHIRE\"}},\"col2\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"LockRifle\",\"cost\":[\"BLUE\",\"BLUE\"],\"effects\":[{\"nDamages\":2,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1]]},{\"weaponName\":\"MachineGun\",\"cost\":[\"BLUE\",\"RED\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":2,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1,2,3],[0,2,3,1],[0,2,1],[0,3,1],[0,1,4],[0,4,1]]},{\"weaponName\":\"Thor\",\"cost\":[\"BLUE\",\"RED\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"LASTONEATTACKED\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"LASTONEATTACKED\"}],\"isLoaded\":true,\"order\":[[0,1,2]]}],\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"SAPPHIRE\"}},\"col3\":{\"spot\":{}}},\"row1\":{\"col0\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"PlasmaGun\",\"cost\":[\"BLUE\",\"YELLOW\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":2,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2],[1,0,2],[0,2,1]]},{\"weaponName\":\"Whisper\",\"cost\":[\"BLUE\",\"BLUE\",\"YELLOW\"],\"effects\":[{\"nDamages\":3,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":2,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0]]},{\"weaponName\":\"Electroscythe\",\"cost\":[\"BLUE\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\",\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]}],\"playersHere\":[],\"doors\":[true,false,false,false],\"room\":\"RUBY\"}},\"col1\":{\"spot\":{\"ammoColorList\":[\"BLUE\",\"BLUE\"],\"powerup\":{\"powerUpName\":\"TargetingScope\",\"color\":\"RED\",\"effect\":{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"ANY\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},\"isTurnPowerup\":false},\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"RUBY\"}},\"col2\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"powerup\":{\"powerUpName\":\"Newton\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},\"playersHere\":[],\"doors\":[true,true,false,false],\"room\":\"RUBY\"}},\"col3\":{\"spot\":{\"ammoColorList\":[\"RED\",\"RED\"],\"powerup\":{\"powerUpName\":\"TagbackGrenade\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},\"isTurnPowerup\":false},\"playersHere\":[],\"doors\":[false,false,false,true],\"room\":\"TOPAZ\"}}},\"row2\":{\"col0\":{\"spot\":{}},\"col1\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"BLUE\",\"YELLOW\"],\"playersHere\":[],\"doors\":[true,false,false,false],\"room\":\"DIAMOND\"}},\"col2\":{\"spot\":{\"ammoColorList\":[\"YELLOW\",\"BLUE\",\"RED\"],\"playersHere\":[],\"doors\":[false,true,false,false],\"room\":\"DIAMOND\"}},\"col3\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"TractorBeam\",\"cost\":[\"BLUE\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"RED\",\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":3,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1],[2,3]]},{\"weaponName\":\"VortexCannon\",\"cost\":[\"RED\",\"BLUE\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2,3,4,3]]},{\"weaponName\":\"Furnace\",\"cost\":[\"RED\",\"BLUE\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":4,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":1,\"nPlayersMarkable\":4,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":1,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]}],\"playersHere\":[],\"doors\":[false,false,false,true],\"room\":\"TOPAZ\"}}}}}";

    private static final String ansiBlue = "\u001b[34m";
    private static final String ansiRed = "\u001b[31m";
    private static final String ansiYellow = "\u001b[33m";
    private static final String ansiReset = "\u001b[0m";
    private static final String ansiWhite = "\u001b[37m";


    public static void fillMapWithAmmo(){

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonRoot = jsonParser.parse(jsonTry).getAsJsonObject();
        JsonObject jsonGameMap = jsonRoot.get("gameMap").getAsJsonObject();



        for(int r = 0; r < 3; r++){

            JsonObject jsonRow = jsonGameMap.get("row" + r).getAsJsonObject();

            for(int c = 0; c < 4; c++){

                JsonObject jsonSpot = jsonRow.get("col" + c).getAsJsonObject().get("spot").getAsJsonObject(); // <== jsonSpot

                if(!jsonSpot.toString().equals("{}")) { //appena sono sicuro di avere uno spot valido nel json posso istanziare uno spawnspot o un ammo spot

                    fillCliSpot(r, c, jsonSpot);
                }
            }
        }
    }



    private static void fillCliSpot(int r, int c, JsonObject jsonSpot) {

        if (jsonSpot.get("ammoColorList") != null){

            mapFire[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard] = "A";
            mapFire[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 1] = "M";
            mapFire[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 2] = "M";
            mapFire[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 3] = "O";
            mapFire[r*verticalSpotStandard + verticalAmmoStandard][c*horizontalSpotStandard + horizontalAmmoStandard + 4] = ":";




        }
        else{

        }
    }

    public static void fillMapFire(){

        mapFire[0][0] = ansiBlue + "╔" + ansiReset;

        for(int c = 1; c < maxHorizontalLength - 14; c++){
            mapFire[0][c] = ansiBlue + "═" + ansiReset;
        }

        mapFire[0][maxHorizontalLength - 14] = ansiBlue + "╗" + ansiReset;

        for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++){
            mapFire[0][c] = "";
        }  //first line filled

        for(int r = 1 ; r < 6; r++){

            mapFire[r][0] = ansiBlue + "║" + ansiReset;

            for(int c = 1 ; c < maxHorizontalLength - 14; c++){

                if(c == 13 || c == 26){
                    mapFire[r][c] = ansiBlue + "│" + ansiReset;
                }
                else {
                    mapFire[r][c] = " ";
                }

            }

            mapFire[r][maxHorizontalLength - 14] = ansiBlue + "║" + ansiReset;

            for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++)
                mapFire[r][c] = "";
        }
        //first row with all columns

        mapFire[6][0] =ansiBlue + "╚" + ansiReset;

        for(int c = 1; c < maxHorizontalLength - 14; c++) {
            if(c == 6 || c == 33){
                mapFire[6][c] = ansiBlue + "╕" + ansiReset;
            }
            else if (c == 7 || c == 34){
                mapFire[6][c] = " ";
            }
            else if(c == 8 || c == 35){
                mapFire[6][c] = ansiBlue + "╒" + ansiReset;
            }
            else{
                mapFire[6][c] = ansiBlue + "═" + ansiReset;
            }
        }

        mapFire[6][maxHorizontalLength - 14] = ansiBlue + "╝" + ansiReset;

        for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++)
            mapFire[6][c] = "";

        //finish blue room


        mapFire[7][0] = ansiRed + "╔" + ansiReset;

        for(int c = 1; c < maxHorizontalLength - 14; c++){
            if(c == 6 || c == 33){
                mapFire[7][c] = ansiRed + "╛" + ansiReset;
            }
            else if( c == 7 || c == 34){
                mapFire[7][c] = " ";
            }
            else if (c == 8 || c == 35){
                mapFire[7][c] = ansiRed + "╘" + ansiReset;
            }
            else{
                mapFire[7][c] = ansiRed + "═" + ansiReset;
            }
        }

        mapFire[7][maxHorizontalLength - 14] = ansiRed + "╗" + ansiReset;
        mapFire[7][maxHorizontalLength - 13] = ansiYellow + "╔" + ansiReset;

        for ( int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++){
            mapFire[7][c] = ansiYellow + "═" + ansiReset;
        }
        mapFire[7][maxHorizontalLength - 1] = ansiYellow + "╗" + ansiReset;

        for(int r = 8 ; r < 13 ; r++ ){

            mapFire[r][0] = ansiRed + "║" + ansiReset;

            for(int c = 1; c < maxHorizontalLength - 14; c++){
                if (c == 13 || c == 26){
                    mapFire[r][c] = ansiRed + "│" + ansiReset;
                }
                else{
                    mapFire[r][c] = " ";
                }
            }

            if( r == 9) {
                mapFire[r][maxHorizontalLength - 14] = ansiRed + "╙" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "╜" + ansiReset;
            }
            else if(r == 10){
                mapFire[r][maxHorizontalLength - 14] = ansiRed + "╓" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "╖" + ansiReset;
            }
            else{
                mapFire[r][maxHorizontalLength - 14] = ansiRed + "║" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "║" + ansiReset;
            }

            for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
                mapFire[r][c] = " ";

            mapFire[r][maxHorizontalLength - 1] = ansiYellow + "║" + ansiReset;

        }

        mapFire[13][0] = ansiRed + "╚" + ansiReset;
        for(int c = 1 ; c < maxHorizontalLength - 14; c++){
            if( c == 17){
                mapFire[13][c] = ansiRed + "╕" + ansiReset;
            }
            else if( c == 18){
                mapFire[13][c] = " ";
            }
            else if (c == 19){
                mapFire[13][c] = ansiRed + "╒" + ansiReset;

            }
            else {
                mapFire[13][c] = ansiRed + "═" + ansiReset;
            }
        }

        mapFire[13][maxHorizontalLength - 14] = ansiRed + "╝" + ansiReset;
        mapFire[13][maxHorizontalLength - 13] = ansiYellow + "║" + ansiReset;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++ ){
            mapFire[13][c] =  ansiYellow + "_" + ansiReset;
        }

        mapFire[13][maxHorizontalLength - 1] = ansiYellow + "║" + ansiReset;

        //14 row
        for (int c = 0; c < 13; c++){
            mapFire[14][c] = " ";
        }

        mapFire[14][13] = ansiWhite + "╔" + ansiReset;
        for(int c = 14; c < maxHorizontalLength - 14; c++){
            if (c == 17){
                mapFire[14][c] = ansiWhite + "╛" + ansiReset;
            }
            else if( c == 18){
                mapFire[14][c] = ansiWhite + " " + ansiReset;
            }
            else if( c == 19){
                mapFire[14][c] = ansiWhite + "╘" + ansiReset;
            }
            else
                mapFire[14][c] = ansiWhite + "═" + ansiReset;
        }

        mapFire[14][maxHorizontalLength - 14] = ansiWhite + "╗" + ansiReset;
        mapFire[14][maxHorizontalLength - 13] = ansiYellow + "║" + ansiReset;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++){
            mapFire[14][c] = " ";
        }
        mapFire[14][maxHorizontalLength - 1] = ansiYellow + "║" + ansiReset;

        for(int r = 15; r < 20; r++){
            for(int c = 0; c < 13; c++){
                mapFire[r][c] = " ";
            }

            mapFire[r][13] = ansiWhite + "║" + ansiReset;

            for(int c = 14; c < maxHorizontalLength - 14; c++){
                if (c == 26){
                    mapFire[r][c] = ansiWhite + "│" + ansiReset;
                }
                else{
                    mapFire[r][c] = " ";
                }
            }

            if( r == 16) {
                mapFire[r][maxHorizontalLength - 14] = ansiWhite + "╙" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "╜" + ansiReset;
            }
            else if(r == 17){
                mapFire[r][maxHorizontalLength - 14] = ansiWhite + "╓" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "╖" + ansiReset;
            }
            else{
                mapFire[r][maxHorizontalLength - 14] = ansiWhite + "║" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "║" + ansiReset;
            }

            for (int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
                mapFire[r][c] = " ";

            mapFire[r][maxHorizontalLength - 1] = ansiYellow + "║" + ansiReset;
        }


        for (int c = 0; c < 13; c++){
            mapFire[20][c] = " ";
        }

        mapFire[20][13] = ansiWhite + "╚" + ansiReset;

        for(int c = 14; c < maxHorizontalLength - 14; c++)
            mapFire[20][c] = ansiWhite + "═" + ansiReset;

        mapFire[20][maxHorizontalLength - 14] = ansiWhite + "╝" + ansiReset;

        mapFire[20][maxHorizontalLength - 13] = ansiYellow + "╚" + ansiReset;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
            mapFire[20][c] = ansiYellow + "═" + ansiReset;

        mapFire[20][maxHorizontalLength - 1] = ansiYellow + "╝" + ansiReset;
    }

    public static void printMyShit(){
        for (int r = 0; r < maxVerticalLength; r++) {

            System.out.println();

            for (int c = 0; c < maxHorizontalLength; c++) {

                System.out.print(mapFire[r][c]);

            }

        }
    }




    public static void printMapFire(){
        fillMapFire();
        fillMapWithAmmo();
        printMyShit();
    }

    public static void main(String[] args){
        printMapFire();
    }

}
