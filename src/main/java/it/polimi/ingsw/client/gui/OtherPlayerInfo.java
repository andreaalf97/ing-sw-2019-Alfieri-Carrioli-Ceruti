package it.polimi.ingsw.client.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class OtherPlayerInfo {

    public String nickname;

    public int nRedAmmo;

    public int nBlueAmmo;

    public int nYellowAmmo;

    public ArrayList<String> damages;

    public ArrayList<String> marks;

    public int nDeaths;

    public int xPosition;
    public int yPosition;

    public boolean isDead;

    public OtherPlayerInfo(String nickname, JsonObject jsonObject) {

        JsonArray allPlayersArray = jsonObject.get("players").getAsJsonArray();
        JsonObject thisPlayer = null;

        for (JsonElement playerElement : allPlayersArray) {

            JsonObject player = playerElement.getAsJsonObject();

            String playerNickname = player.get("nickname").getAsString();

            if (playerNickname.equals(nickname)) {
                thisPlayer = player;
                break;
            }

        }

        if (thisPlayer == null)
            throw new RuntimeException("Did not find this player in the JSON");

        this.nickname = thisPlayer.get("nickname").getAsString();
        this.nRedAmmo = thisPlayer.get("nRedAmmo").getAsInt();
        this.nBlueAmmo = thisPlayer.get("nBlueAmmo").getAsInt();
        this.nYellowAmmo = thisPlayer.get("nYellowAmmo").getAsInt();
        this.nDeaths = thisPlayer.get("nDeaths").getAsInt();
        this.xPosition = thisPlayer.get("xPosition").getAsInt();
        this.yPosition = thisPlayer.get("yPosition").getAsInt();
        this.isDead = thisPlayer.get("isDead").getAsBoolean();
        this.marks = jsonArrayToArrayListString(thisPlayer.get("marks").getAsJsonArray());
        this.damages = jsonArrayToArrayListString(thisPlayer.get("damages").getAsJsonArray());
    }



    private ArrayList<String> jsonArrayToArrayListString(JsonArray jsonArray) {

        ArrayList<String> tempArray = new ArrayList<>();

        for(JsonElement element : jsonArray){

            tempArray.add(
                    element.getAsString()
            );

        }

        return tempArray;

    }

    public int getnRedAmmo(){
        return this.nRedAmmo;
    }

    public int getnBlueAmmo(){
        return this.nBlueAmmo;
    }

    public int getnYellowAmmo(){
        return this.nYellowAmmo;
    }
}
