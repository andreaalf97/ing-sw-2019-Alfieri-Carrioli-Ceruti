package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import it.polimi.ingsw.view.QuestionType;

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
     * The type of answer the controller is waiting for
     */
    public QuestionType waitingForAnswerToThisQuestion;


    public PlayerStatus(JsonObject jsonPlayerStatus){
        this.isActive = jsonPlayerStatus.get("isActive").getAsBoolean();
        this.isFirstTurn = jsonPlayerStatus.get("isFirstTurn").getAsBoolean();
        this.isFrenzyTurn = jsonPlayerStatus.get("isFrenzyTurn").getAsBoolean();
        this.nActions = jsonPlayerStatus.get("nActions").getAsInt();
        this.nActionsDone = jsonPlayerStatus.get("nActionsDone").getAsInt();
        if (jsonPlayerStatus.get("waitingForAnswerToThisQuestion") != null)
             this.waitingForAnswerToThisQuestion = QuestionType.valueOf(jsonPlayerStatus.get("waitingForAnswerToThisQuestion").getAsString());

    }


    public PlayerStatus(PlayerStatus playerStatus){
        this.isFirstTurn = playerStatus.isFirstTurn;
        this.isActive = playerStatus.isActive;
        this.waitingForAnswerToThisQuestion = playerStatus.waitingForAnswerToThisQuestion;
        this.nActionsDone = playerStatus.nActionsDone;
        this.isFrenzyTurn = playerStatus.isFrenzyTurn;
        this.nActions = playerStatus.nActions;
    }

    public PlayerStatus(){

        this.isFirstTurn = true;
        this.isActive = false;
        this.nActionsDone = 0;
        this.waitingForAnswerToThisQuestion = null;
        this.isFrenzyTurn = false;
        this.nActions = 2;

    }
}
