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

    protected int nMovesDone;

    //todo manca la frenesia?

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
        this.nMovesDone = playerStatus.nMovesDone;
        this.waitingForShootingOrder = playerStatus.waitingForShootingOrder;
    }

    public PlayerStatus(){

        this.isFirstTurn = true;

    }
}
