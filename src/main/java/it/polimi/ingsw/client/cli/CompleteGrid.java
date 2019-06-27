package it.polimi.ingsw.client.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Color;

import java.util.ArrayList;

public class CompleteGrid {

    public static int maxHorizontalGridLength = 111;

    public static String[][] grid = new String[MapGrid.maxVerticalMapLength][maxHorizontalGridLength];

    public static int kstVerticalStandard = 1;
    public static int kstHorizontalStandard = 82;

    public static String playerSymbol = "╬";

    public static String kstBackground = "\u001b[47m";

    public void clearGrid(){
        for(int r = 0; r < MapGrid.maxVerticalMapLength; r++){
            for(int c = 0; c < maxHorizontalGridLength; c++){
                grid[r][c] = " ";
            }
        }
    }

    public static void fillMapWithKst(JsonObject jsonSnapshot, ArrayList<Color> playerColors, ArrayList<String> playerNames){
        grid[kstVerticalStandard][kstHorizontalStandard] = kstBackground + Color.BLACK + "K" + Color.RESET;
        grid[kstVerticalStandard][kstHorizontalStandard + 1] = kstBackground + Color.BLACK + "S" + Color.RESET;
        grid[kstVerticalStandard][kstHorizontalStandard + 2] = kstBackground + Color.BLACK + "T" + Color.RESET;

        grid[kstVerticalStandard][kstHorizontalStandard + 3] = "[";

        JsonArray jsonKstKills = jsonSnapshot.get("kst").getAsJsonObject().get("skullList").getAsJsonArray();
        JsonArray jsonKstOverkills = jsonSnapshot.get("kst").getAsJsonObject().get("isOverkill").getAsJsonArray();

        for(int i = 0; i < jsonKstKills.size(); i++){
            String killerUsername = jsonKstKills.get(i).getAsString();

            if(!killerUsername.equals("SKULL")) {
                Color killerColor = getColorOfPlayer(killerUsername, playerColors, playerNames);
                grid[kstVerticalStandard][kstHorizontalStandard + 4] = killerColor.escape() + playerSymbol + Color.RESET;

                if(jsonKstOverkills.get(i).getAsBoolean()){
                    grid[kstVerticalStandard][kstHorizontalStandard + 5] = killerColor.escape() + playerSymbol + Color.RESET;
                }

            }


        }


    }

    private static Color getColorOfPlayer(String killerUsername, ArrayList<Color> playerColors, ArrayList<String> playerNames) {
        int i;

        for(i = 0; i < playerNames.size(); i++){
            if(playerNames.get(i).equals(killerUsername))
                break;
        }

        return playerColors.get(i);
    }
}
