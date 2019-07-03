package it.polimi.ingsw.model.cards;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Color;

import java.util.ArrayList;

public class AmmoCard {

    private ArrayList<Color> ammoColorList;
    private boolean hasPowerUp;

    public String getAmmoCardImagePath() {
        return ammoCardImagePath;
    }

    private String ammoCardImagePath;

    public AmmoCard ( String ammoCardImagePath, ArrayList<Color> colors, boolean hasPowerUp){
        this.ammoCardImagePath = ammoCardImagePath;
        this.ammoColorList = colors;
        this.hasPowerUp = hasPowerUp;
    }

    public AmmoCard(){
        this.ammoColorList = new ArrayList<>();
        this.hasPowerUp = false;
        this.ammoCardImagePath = "";
    }

    public AmmoCard(JsonObject jsonAmmoCard){
        this.ammoColorList = new ArrayList<>();
        JsonArray jsonAmmoColorList = jsonAmmoCard.get("ammoColorList").getAsJsonArray();
        for(int i = 0; i < jsonAmmoColorList.size(); i++){
            Color color = Color.valueOf(jsonAmmoColorList.get(i).getAsString());
            this.ammoColorList.add(color);
        }


        this.hasPowerUp = jsonAmmoCard.get("hasPowerUp").getAsBoolean();
        this.ammoCardImagePath = jsonAmmoCard.get("ammoCardImagePath").getAsString();
    }

    public void setAmmoColorList(ArrayList<Color> ammoColorList) {
        this.ammoColorList = ammoColorList;
    }

    public ArrayList<Color> getAmmoColorList(){
        return new ArrayList<>(this.ammoColorList);
    }

    public void setPowerUp(boolean hasPowerup) {
        this.hasPowerUp = hasPowerup;
    }

    public boolean hasPowerUp(){
        return hasPowerUp;
    }
}
