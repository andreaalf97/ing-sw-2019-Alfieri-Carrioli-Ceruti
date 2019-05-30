package it.polimi.ingsw.model;

import com.google.gson.JsonObject;

public class PlayerStatus {

    /**
     * true if it's this player's first turn in the game
     */
    public boolean isFirstTurn;

    /**
     * If the player is in frenzy
     */
    public boolean isFrenzyTurn;

    /**
     * true during this player's turn
     */
    public boolean isActive;

    /**
     * How many actions this player has done during his turn
     */
    public int nActionsDone;

    /**
     * How many actions this player is allowed to do during his turn
     */
    public int nActions;

    /**
     * creates the playerStatus reading it by json
     * @param jsonPlayerStatus the status of the player in jsom
     */
    public PlayerStatus(JsonObject jsonPlayerStatus){
        this.isActive = jsonPlayerStatus.get("isActive").getAsBoolean();
        this.isFirstTurn = jsonPlayerStatus.get("isFirstTurn").getAsBoolean();
        this.isFrenzyTurn = jsonPlayerStatus.get("isFrenzyTurn").getAsBoolean();
        this.nActions = jsonPlayerStatus.get("nActions").getAsInt();
        this.nActionsDone = jsonPlayerStatus.get("nActionsDone").getAsInt();
    }

    public PlayerStatus(PlayerStatus playerStatus){
        this.isFirstTurn = playerStatus.isFirstTurn;
        this.isActive = playerStatus.isActive;
        this.nActionsDone = playerStatus.nActionsDone;
        this.isFrenzyTurn = playerStatus.isFrenzyTurn;
        this.nActions = playerStatus.nActions;
    }

    public PlayerStatus(){

        this.isFirstTurn = true;
        this.isActive = false;
        this.nActionsDone = 0;
        this.isFrenzyTurn = false;
        this.nActions = 2;

    }
}
