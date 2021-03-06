package it.polimi.ingsw.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.gui.OtherPlayerInfo;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerInfo extends OtherPlayerInfo{

    public ArrayList<String> weaponNames;
    public ArrayList<ArrayList<Color>> weaponCosts;

    public ArrayList<String> powerUpNames;
    public ArrayList<Color> powerUpColors;


    public PlayerInfo(String nickname, JsonObject jsonObject){

        super(nickname, jsonObject);

        JsonArray allPlayersArray = jsonObject.get("players").getAsJsonArray();
        JsonObject thisPlayer = null;

        for(JsonElement playerElement : allPlayersArray){

            JsonObject player = playerElement.getAsJsonObject();

            String playerNickname = player.get("nickname").getAsString();

            if(playerNickname.equals(nickname)){
                thisPlayer = player;
                break;
            }

        }

        if(thisPlayer == null)
            throw new RuntimeException("Did not find this player in the JSON");

        this.weaponNames = new ArrayList<>();
        this.weaponCosts = new ArrayList<>();
        this.powerUpNames = new ArrayList<>();
        this.powerUpColors = new ArrayList<>();

        ArrayList<Weapon> readWeapons = new ArrayList<>();
        ArrayList<PowerUp> readPowerUps = new ArrayList<>();

        if(thisPlayer.get("weaponList") != null)
            readWeapons = jsonArrayToArrayListWeapon(thisPlayer.get("weaponList").getAsJsonArray());
        if(thisPlayer.get("powerUpList") != null)
            readPowerUps = jsonArrayToArrayListPowerUp(thisPlayer.get("powerUpList").getAsJsonArray());

        for(Weapon w : readWeapons){
            this.weaponNames.add(w.getWeaponName());
            this.weaponCosts.add(w.getCost());
        }

        for(PowerUp p : readPowerUps){
            this.powerUpNames.add(p.getPowerUpName());
            this.powerUpColors.add(p.getColor());
        }
    }

    private ArrayList<PowerUp> jsonArrayToArrayListPowerUp(JsonArray powerUpJsonList) {

        ArrayList<PowerUp> powerUps = new ArrayList<>();

        for(JsonElement element : powerUpJsonList){

            PowerUp tempPowerUp = new PowerUp(element.getAsJsonObject());
            powerUps.add(tempPowerUp);

        }

        return powerUps;

    }

    private ArrayList<Weapon> jsonArrayToArrayListWeapon(JsonArray weaponsAsJsonArray) {

        ArrayList<Weapon> weapons = new ArrayList<>();

        for(JsonElement element : weaponsAsJsonArray){

            Weapon tempWeapon = new Weapon(element.getAsJsonObject());
            weapons.add(tempWeapon);

        }

        return weapons;

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

}
