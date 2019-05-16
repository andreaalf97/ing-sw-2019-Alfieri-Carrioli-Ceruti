package it.polimi.ingsw.model;

import it.polimi.ingsw.view.QuestionType;

public class PlayerStatus {

    /**
     * true if it's this player's first turn in the game
     */
    public boolean isFirstTurn;

    /**
     * true during this player's turn
     */
    public boolean isActive;

    /**
     * How many actions this player has done during his turn
     */
    public int nActionsDone;

    /**
     * The type of answer the controller is waiting for
     */
    public QuestionType waitingForAnswerToThisQuestion;


    public PlayerStatus(PlayerStatus playerStatus){
        this.isFirstTurn = playerStatus.isFirstTurn;
        this.isActive = playerStatus.isActive;
        this.waitingForAnswerToThisQuestion = playerStatus.waitingForAnswerToThisQuestion;
        this.nActionsDone = playerStatus.nActionsDone;
    }

    public PlayerStatus(){

        this.isFirstTurn = true;
        this.isActive = false;
        this.nActionsDone = 0;
        this.waitingForAnswerToThisQuestion = null;

    }
}
