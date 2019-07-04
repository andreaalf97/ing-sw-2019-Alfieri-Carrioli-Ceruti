package it.polimi.ingsw.client.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.model.Color;

import java.util.ArrayList;

public class PlayerGrid {

    private static int maxVerticalLength = 15;
    private static int maxHorizontalLength = 70;

    private static String[][] playerGrid = new String[maxVerticalLength][maxHorizontalLength];

    private static int verticalFirstRowStandard = 4;

    private static int verticalSecondRowStandard = 10;

    private static int horizontalLeftObjects = 14;

    private static int horizontalRightColumn = 32;

    private static int horizontalSeparator = 31;



    public static void clearGrid(){
        for(int i = 0; i < maxVerticalLength; i++)
            for(int j = 0; j < maxHorizontalLength; j++)
                playerGrid[i][j] = " ";
    }

    public static void fillPlayerGrid(JsonObject lastSnapshot, String username, ArrayList<PlayerColor> playerColors, ArrayList<String> playerNames){
        PlayerColor myColor = CompleteGrid.getColorOfPlayer(username, playerColors, playerNames);

        playerGrid[2][0] = myColor.escape() + CompleteGrid.playerSymbol + Color.RESET;
        playerGrid[2][1] = ":";
        playerGrid[2][2] = myColor.escape() + username + Color.RESET;

        JsonArray jsonPlayers = lastSnapshot.get("players").getAsJsonArray();
        JsonObject jsonPlayer = getThisPlayer(jsonPlayers, username);

        insertAmmo(jsonPlayer);
        insertDamages(horizontalRightColumn, verticalFirstRowStandard, jsonPlayer.get("damages").getAsJsonArray(), playerColors, playerNames);
        insertMarks(horizontalRightColumn, verticalFirstRowStandard + 2, jsonPlayer.get("marks").getAsJsonArray(), playerColors, playerNames);
        insertNdeaths(jsonPlayer.get("nDeaths").getAsInt());
        insertWeapons(jsonPlayer.get("weaponList").getAsJsonArray());
        separateWeaponsAndPowerUps();
        insertPowerUps(jsonPlayer.get("powerUpList").getAsJsonArray());




    }

    private static void insertPowerUps(JsonArray powerUpList) {
        playerGrid[verticalSecondRowStandard][horizontalRightColumn] = "-";
        playerGrid[verticalSecondRowStandard][horizontalRightColumn + 1] = "P";
        playerGrid[verticalSecondRowStandard][horizontalRightColumn + 2] = "O";
        playerGrid[verticalSecondRowStandard][horizontalRightColumn + 3] = "W";
        playerGrid[verticalSecondRowStandard][horizontalRightColumn + 4] = "E";
        playerGrid[verticalSecondRowStandard][horizontalRightColumn + 5] = "R";
        playerGrid[verticalSecondRowStandard][horizontalRightColumn + 6] = "U";
        playerGrid[verticalSecondRowStandard][horizontalRightColumn + 7] = "P";
        playerGrid[verticalSecondRowStandard][horizontalRightColumn + 8] = "S";
        playerGrid[verticalSecondRowStandard][horizontalRightColumn + 9] = ":";

        for(int i = 0; i < powerUpList.size(); i++){
            String powerUpName = powerUpList.get(i).getAsJsonObject().get("powerUpName").getAsString();
            Color powerUpColor = Color.valueOf(powerUpList.get(i).getAsJsonObject().get("color").getAsString());
            playerGrid[verticalSecondRowStandard + 2*i][horizontalRightColumn + 11] = "" + i;
            playerGrid[verticalSecondRowStandard + 2*i][horizontalRightColumn + 12] = ":";
            playerGrid[verticalSecondRowStandard + 2*i][horizontalRightColumn + 13] = powerUpColor.escape() + powerUpName + Color.RESET;
        }
    }

    private static void separateWeaponsAndPowerUps() {
        for (int r = verticalSecondRowStandard; r < maxVerticalLength; r++)
            playerGrid[r][horizontalSeparator] = "│";
    }

    private static void insertWeapons(JsonArray weaponList) {
        playerGrid[verticalSecondRowStandard][0] = "-";
        playerGrid[verticalSecondRowStandard][1] = "W";
        playerGrid[verticalSecondRowStandard][2] = "E";
        playerGrid[verticalSecondRowStandard][3] = "A";
        playerGrid[verticalSecondRowStandard][4] = "P";
        playerGrid[verticalSecondRowStandard][5] = "O";
        playerGrid[verticalSecondRowStandard][6] = "N";
        playerGrid[verticalSecondRowStandard][7] = "S";
        playerGrid[verticalSecondRowStandard][8] = ":";

        for(int i = 0; i < weaponList.size(); i++){
            String weaponName = weaponList.get(i).getAsJsonObject().get("weaponName").getAsString();
            playerGrid[verticalSecondRowStandard + 2*i][horizontalLeftObjects] = "" + i;
            playerGrid[verticalSecondRowStandard + 2*i][horizontalLeftObjects + 1] = ":";
            insertWeaponName(weaponName,verticalSecondRowStandard + 2*i, horizontalLeftObjects + 2);
        }

    }

    private static void insertWeaponName(String weaponName, int row, int column) {

        for(int i = 0; i < weaponName.length(); i++) {
            char c = weaponName.charAt(i);
            playerGrid[row][column + i] = "" + c;
        }
    }

    private static void insertNdeaths(int nDeaths){
        playerGrid[verticalFirstRowStandard + 4][horizontalRightColumn] = "-nDeaths: " + nDeaths;

    }

    private static void insertMarks( int column, int row, JsonArray jsonMarks, ArrayList<PlayerColor> playerColors, ArrayList<String> playerNames) {
        playerGrid[row][column] = "-Marks: [";

        for(int i = 0; i < jsonMarks.size(); i++){
            String damagerName = jsonMarks.get(i).getAsString();
            PlayerColor damagerColor = CompleteGrid.getColorOfPlayer(damagerName, playerColors, playerNames);

            playerGrid[row][column] += " ";
            playerGrid[row][column] += damagerColor.escape() + CompleteGrid.playerSymbol + Color.RESET;
            playerGrid[row][column] += " ";

            if(i != jsonMarks.size() - 1)
                playerGrid[row][column] += ",";
        }

        playerGrid[row][column] += "]";
    }

    private static void insertDamages(int column, int row, JsonArray jsonDamages, ArrayList<PlayerColor> playerColors, ArrayList<String> playerNames){

        playerGrid[row][column] = "-Damages: [";

        for(int i = 0; i < jsonDamages.size(); i++){
            String damagerName = jsonDamages.get(i).getAsString();
            PlayerColor damagerColor = CompleteGrid.getColorOfPlayer(damagerName, playerColors, playerNames);

            playerGrid[row][column] += " ";
            playerGrid[row][column] += damagerColor.escape() + CompleteGrid.playerSymbol + Color.RESET;
            playerGrid[row][column] += " ";

            if(i != jsonDamages.size() - 1)
                playerGrid[row][column] += ",";
        }

        playerGrid[row][column] += "]";
    }


    private static void insertAmmo(JsonObject jsonPlayer) {
        playerGrid[verticalFirstRowStandard][0] = "-";
        playerGrid[verticalFirstRowStandard][1] = "A";
        playerGrid[verticalFirstRowStandard][2] = "M";
        playerGrid[verticalFirstRowStandard][3] = "M";
        playerGrid[verticalFirstRowStandard][4] = "O";
        playerGrid[verticalFirstRowStandard][5] = ":";

        playerGrid[verticalFirstRowStandard][7] =  Color.BLUE.escape() + "B" + Color.RESET;
        playerGrid[verticalFirstRowStandard][8] =  Color.BLUE.escape() + "L" + Color.RESET;
        playerGrid[verticalFirstRowStandard][9] =  Color.BLUE.escape() + "U" + Color.RESET;
        playerGrid[verticalFirstRowStandard][10] =  Color.BLUE.escape() + "E" + Color.RESET;

        int nBlue = jsonPlayer.get("nBlueAmmo").getAsInt();

        playerGrid[verticalFirstRowStandard][horizontalLeftObjects] =  "" + nBlue;

        playerGrid[verticalFirstRowStandard + 2][7] =  Color.YELLOW.escape() + "Y" + Color.RESET;
        playerGrid[verticalFirstRowStandard + 2][8] =  Color.YELLOW.escape() + "E" + Color.RESET;
        playerGrid[verticalFirstRowStandard + 2][9] =  Color.YELLOW.escape() + "L" + Color.RESET;
        playerGrid[verticalFirstRowStandard + 2][10] =  Color.YELLOW.escape() + "L" + Color.RESET;
        playerGrid[verticalFirstRowStandard + 2][11] =  Color.YELLOW.escape() + "O" + Color.RESET;
        playerGrid[verticalFirstRowStandard + 2][12] =  Color.YELLOW.escape() + "W" + Color.RESET;

        int nYellow = jsonPlayer.get("nYellowAmmo").getAsInt();

        playerGrid[verticalFirstRowStandard + 2][horizontalLeftObjects] =  "" + nYellow;

        playerGrid[verticalFirstRowStandard + 4][7] =  Color.RED.escape() + "R" + Color.RESET;
        playerGrid[verticalFirstRowStandard + 4][8] =  Color.RED.escape() + "E" + Color.RESET;
        playerGrid[verticalFirstRowStandard + 4][9] =  Color.RED.escape() + "D" + Color.RESET;

        int nRed = jsonPlayer.get("nRedAmmo").getAsInt();

        playerGrid[verticalFirstRowStandard + 4][horizontalLeftObjects] =  "" + nRed;

    }

    private static JsonObject getThisPlayer(JsonArray jsonPlayers, String username) {

        for(int i = 0; i < jsonPlayers.size(); i++){
            JsonObject jsonPlayer = jsonPlayers.get(i).getAsJsonObject();
            if(jsonPlayer.get("nickname").getAsString().equals(username))
                return jsonPlayer;
        }

        throw new RuntimeException("this player should be in the json, PlayerGrid!!!!!!!!!!1");

    }

    public static void printMap(){
        for (int r = 0; r < maxVerticalLength; r++) {

            System.out.println();

            for (int c = 0; c < maxHorizontalLength; c++) {

                System.out.print(playerGrid[r][c]);

            }

        }
    }
}
