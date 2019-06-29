package it.polimi.ingsw.client.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.MapName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CompleteGrid {

    public static int maxHorizontalGridLength = 111;

    public static String[][] grid = new String[MapGrid.maxVerticalMapLength + 1][maxHorizontalGridLength];

    public static int kstVerticalStandard = 0;
    public static int kstHorizontalStandard = 83;

    public static int otherPlayersVerticalStart = 3;
    public static int otherPlayersVerticalStandard = 5;
    public static int otherPlayersHorizontalStandard = 83;

    public static String playerSymbol = "╬";

    public static String kstBackground = "\u001b[41;1m";


    public static void clearGrid(){
        for(int r = 0; r < MapGrid.maxVerticalMapLength + 1; r++){
            for(int c = 0; c < maxHorizontalGridLength; c++){
                grid[r][c] = " ";
            }
        }
    }

    public static void copyMapGrid(){
        for(int r = 0; r < MapGrid.maxVerticalMapLength; r++)
            for(int c = 0; c < MapGrid.maxHorizLength; c++)
                grid[r][c] = MapGrid.map[r][c];
    }

    public static void separateBoards(){
        for(int r = 0; r < MapGrid.maxVerticalMapLength + 1; r++)
            grid[r][MapGrid.maxHorizLength] =  Color.WHITE.escape() + "│" + Color.RESET;
    }

    public static void printMap(){
        for (int r = 0; r < MapGrid.maxVerticalMapLength + 1; r++) {

            System.out.println();

            for (int c = 0; c < maxHorizontalGridLength; c++) {

                System.out.print(grid[r][c]);

            }

        }
    }

    public static void fillMapWithKst(JsonObject jsonSnapshot, ArrayList<PlayerColor> playerColors, ArrayList<String> playerNames){
        grid[kstVerticalStandard][kstHorizontalStandard] = kstBackground  + "K" + Color.RESET;
        grid[kstVerticalStandard][kstHorizontalStandard + 1] = kstBackground + "S" + Color.RESET;
        grid[kstVerticalStandard][kstHorizontalStandard + 2] = kstBackground + "T" + Color.RESET;

        grid[kstVerticalStandard][kstHorizontalStandard + 3] = "[";

        JsonArray jsonKstKills = jsonSnapshot.get("kst").getAsJsonObject().get("skullList").getAsJsonArray();
        JsonArray jsonKstOverkills = jsonSnapshot.get("kst").getAsJsonObject().get("isOverkill").getAsJsonArray();

        int nSkulls = countHowManySkulls(jsonKstKills);

        for(int i = 0; i < jsonKstKills.size() - nSkulls; i++){

            String killerUsername = jsonKstKills.get(i).getAsString();

            if(!killerUsername.equals("SKULL")) {
                PlayerColor killerColor = getColorOfPlayer(killerUsername, playerColors, playerNames);
                grid[kstVerticalStandard][kstHorizontalStandard + 4 + i] = " ";
                grid[kstVerticalStandard][kstHorizontalStandard + 4 + i] += killerColor.escape() + playerSymbol + Color.RESET;

                if(jsonKstOverkills.get(i).getAsBoolean()){
                    grid[kstVerticalStandard][kstHorizontalStandard + 4 + i] += killerColor.escape() + playerSymbol + Color.RESET;
                }

                grid[kstVerticalStandard][kstHorizontalStandard + 4 + i] += " ";

                if( i != jsonKstKills.size() - nSkulls - 1){
                    grid[kstVerticalStandard][kstHorizontalStandard + 4 + i] += ",";
                }
            }
        }
        grid[kstVerticalStandard][kstHorizontalStandard + 4 + jsonKstKills.size() - nSkulls] = "]";

        printLeaderBoards(jsonKstKills, jsonKstOverkills, playerNames, playerColors);
    }

    private static void printLeaderBoards(JsonArray jsonKstKills, JsonArray jsonKstOverkills, ArrayList<String> playerNames, ArrayList<PlayerColor> playerColors) {

        Map leaderboards = new HashMap<String, Integer>();

        for(int i = 0; i < playerNames.size(); i++){
            String player = playerNames.get(i);
            leaderboards.put(player, 0);
        }

        for(int i = 0; i < jsonKstKills.size(); i++){
            String killer = jsonKstKills.get(i).getAsString();

            if(!killer.equals("SKULL")){

                int tempKills = (int)leaderboards.get(killer);

                if(jsonKstOverkills.get(i).getAsBoolean()){
                    leaderboards.put(killer, tempKills + 2);
                }
                else{
                    leaderboards.put(killer, tempKills + 1);
                }
            }
        }


        //ordering hashMap, ty andreaalf
        ArrayList<String> ranking = new ArrayList<>();
        ArrayList<Integer> playerKills = new ArrayList<>();

        while(!leaderboards.isEmpty()){ //I am removing the biggest element every time, so I continue until the HashMap is empty

            int maxValue = -1; //If a value is inside the recurrences array it can't be smaller than 1
            String maxPlayer = "ERROR"; //This should never be inserted into the ranking array

            //Looking for the current biggest element
            for(String player : (Set<String>)leaderboards.keySet()){
                if((int)leaderboards.get(player) > maxValue){
                    maxValue = (int)leaderboards.get(player);
                    maxPlayer = player;
                }
            }

            ranking.add(maxPlayer); //Adding the current biggest element to the ranking array
            playerKills.add((int)leaderboards.get(maxPlayer));
            leaderboards.remove(maxPlayer); //Removing the biggest element from the HashMap so I can find the next biggest element
        }

        grid[kstVerticalStandard + 1][kstHorizontalStandard] = kstBackground + "LEADERBOARDS" + Color.RESET;
        grid[kstVerticalStandard + 1][kstHorizontalStandard] += ":[";

        for(int i = 0; i < ranking.size(); i++) {
            String playerName = ranking.get(i);
            PlayerColor playerColor = getColorOfPlayer(playerName, playerColors, playerNames);
            grid[kstVerticalStandard + 1][kstHorizontalStandard] += " " + playerColor.escape() + playerSymbol + Color.RESET + " : " + playerKills.get(i);

            if(i != ranking.size() - 1)
                grid[kstVerticalStandard + 1][kstHorizontalStandard] += ",";
        }

        grid[kstVerticalStandard + 1][kstHorizontalStandard] += "]";

    }

    private static int countHowManySkulls(JsonArray jsonKstSkulls) {
        int n = 0;
        for(int i = 0; i < jsonKstSkulls.size(); i++)
            if(jsonKstSkulls.get(i).getAsString().equals("SKULL"))
                n++;

        return n;
    }

    public static void fillMapWithPlayers(JsonObject jsonSnapshot, ArrayList<PlayerColor> playerColors, ArrayList<String> playerNames, String username){
        JsonArray jsonPlayers = jsonSnapshot.get("players").getAsJsonArray();

        removePlayerFromArray(jsonPlayers, username);

        for(int i = 0; i < jsonPlayers.size(); i++){
                JsonObject jsonPlayer = jsonPlayers.get(i).getAsJsonObject();
                String playerName = jsonPlayer.get("nickname").getAsString();
                PlayerColor playerColor = getColorOfPlayer(playerName, playerColors, playerNames);

                grid[i * otherPlayersVerticalStandard + otherPlayersVerticalStart][otherPlayersHorizontalStandard] = playerColor.escape() + playerSymbol + Color.RESET;
                grid[i * otherPlayersVerticalStandard + otherPlayersVerticalStart][otherPlayersHorizontalStandard + 1] = ":";
                grid[i * otherPlayersVerticalStandard + otherPlayersVerticalStart][otherPlayersHorizontalStandard + 2] = playerColor.escape() + playerName + Color.RESET;

                JsonArray jsonDamages = jsonPlayer.get("damages").getAsJsonArray();
                insertDamages(i * otherPlayersVerticalStandard + 1 + otherPlayersVerticalStart, jsonDamages, playerColors, playerNames);

                JsonArray jsonMarks = jsonPlayer.get("marks").getAsJsonArray();
                insertMarks(i * otherPlayersVerticalStandard + 2 + otherPlayersVerticalStart, jsonMarks, playerColors, playerNames);

                int Ndeaths = jsonPlayer.get("nDeaths").getAsInt();
                insertNdeaths(i  * otherPlayersVerticalStandard + 3 + otherPlayersVerticalStart, Ndeaths);

        }
    }

    private static void insertNdeaths(int row, int ndeaths) {
        grid[row][otherPlayersHorizontalStandard] = "-nDeaths: " + ndeaths;
    }

    private static void insertMarks(int row, JsonArray jsonMarks, ArrayList<PlayerColor> playerColors, ArrayList<String> playerNames) {
        grid[row][otherPlayersHorizontalStandard] = "-Marks: [";

        for(int i = 0; i < jsonMarks.size(); i++){
            String damagerName = jsonMarks.get(i).getAsString();
            PlayerColor damagerColor = getColorOfPlayer(damagerName, playerColors, playerNames);

            grid[row][otherPlayersHorizontalStandard] += " ";
            grid[row][otherPlayersHorizontalStandard] += damagerColor.escape() + playerSymbol + Color.RESET;
            grid[row][otherPlayersHorizontalStandard] += " ";

            if(i != jsonMarks.size() - 1)
                grid[row][otherPlayersHorizontalStandard] += ",";
        }

        grid[row][otherPlayersHorizontalStandard] += "]";
    }

    private static void insertDamages(int row, JsonArray jsonDamages, ArrayList<PlayerColor> playerColors, ArrayList<String> playerNames){

        grid[row][otherPlayersHorizontalStandard] = "-Damages: [";

        for(int i = 0; i < jsonDamages.size(); i++){
            String damagerName = jsonDamages.get(i).getAsString();
            PlayerColor damagerColor = getColorOfPlayer(damagerName, playerColors, playerNames);

            grid[row][otherPlayersHorizontalStandard] += " ";
            grid[row][otherPlayersHorizontalStandard] += damagerColor.escape() + playerSymbol + Color.RESET;
            grid[row][otherPlayersHorizontalStandard] += " ";

            if(i != jsonDamages.size() - 1)
                grid[row][otherPlayersHorizontalStandard] += ",";
        }

        grid[row][otherPlayersHorizontalStandard] += "]";
    }

    private static void removePlayerFromArray(JsonArray jsonPlayers, String username) {
        for(int i = 0; i < jsonPlayers.size(); i++){
            if(jsonPlayers.get(i).getAsJsonObject().get("nickname").getAsString().equals(username))
                jsonPlayers.remove(i);
        }
    }

    private static PlayerColor getColorOfPlayer(String username, ArrayList<PlayerColor> playerColors, ArrayList<String> playerNames) {
        int i;

        for(i = 0; i < playerNames.size(); i++){
            if(playerNames.get(i).equals(username))
                break;
        }

        return playerColors.get(i);
    }
}
