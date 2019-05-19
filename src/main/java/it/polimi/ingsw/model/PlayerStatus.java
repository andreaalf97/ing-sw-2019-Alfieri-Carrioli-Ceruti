package it.polimi.ingsw.model;

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

    /**
     * The question asked before the current one
     */
    public QuestionType lastQuestion;

    /**
     * The answer given before the current one
     */
    public String lastAnswer;


    public PlayerStatus(PlayerStatus playerStatus){
        this.isFirstTurn = playerStatus.isFirstTurn;
        this.isActive = playerStatus.isActive;
        this.waitingForAnswerToThisQuestion = playerStatus.waitingForAnswerToThisQuestion;
        this.nActionsDone = playerStatus.nActionsDone;
        this.isFrenzyTurn = playerStatus.isFrenzyTurn;
        this.nActions = playerStatus.nActions;
        this.lastQuestion = playerStatus.lastQuestion;
        this.lastAnswer = playerStatus.lastAnswer;
    }

    public PlayerStatus(){

        this.isFirstTurn = true;
        this.isActive = false;
        this.nActionsDone = 0;
        this.waitingForAnswerToThisQuestion = null;
        this.isFrenzyTurn = false;
        this.nActions = 2;
        this.lastQuestion = null;
        this.lastAnswer = null;

    }
}
