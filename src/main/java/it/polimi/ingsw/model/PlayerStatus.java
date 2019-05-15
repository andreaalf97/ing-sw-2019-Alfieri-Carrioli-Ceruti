package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Question;

public class PlayerStatus {

    /**
     * true if it's this player's first turn in the game
     */
    protected boolean isFirstTurn;

    /**
     * true during this player's turn
     */
    protected boolean isActive;

    /**
     * How many actions this player has done during his turn
     */
    protected int nActionsDone;

    /**
     * The type of answer the controller is waiting for
     */
    protected Question waitingForAnswerToThisQuestion;

    /**
     *
     */
    protected boolean waitingForShootingOrder;

    public PlayerStatus(PlayerStatus playerStatus){
        this.isFirstTurn = playerStatus.isFirstTurn;
        this.isActive = playerStatus.isActive;
        this.waitingForAnswerToThisQuestion = playerStatus.waitingForAnswerToThisQuestion;
        this.nActionsDone = playerStatus.nActionsDone;
        this.waitingForShootingOrder = playerStatus.waitingForShootingOrder;
    }

    public PlayerStatus(){

        this.isFirstTurn = true;
        this.isActive = false;
        this.nActionsDone = 0;
        this.waitingForAnswerToThisQuestion = null;

    }
}
