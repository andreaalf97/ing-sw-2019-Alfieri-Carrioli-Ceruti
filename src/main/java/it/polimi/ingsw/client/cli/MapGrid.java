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

    private static void addPlayersInMap(int row, int column, JsonArray jsonPlayersHere, ArrayList<PlayerColor> playerColors, ArrayList<String> allPlayersNames){

        if(jsonPlayersHere.size() != 0) {

            for (int i = 0; i < jsonPlayersHere.size(); i++) {
                for (int playerColorIndex = 0; playerColorIndex < allPlayersNames.size(); playerColorIndex++) {
                    if (jsonPlayersHere.get(i).getAsString().equals(allPlayersNames.get(playerColorIndex))){
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

}
