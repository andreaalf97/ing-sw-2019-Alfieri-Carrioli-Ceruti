package it.polimi.ingsw.controller;

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
     *
     */
    protected boolean waitingForAnswerToThisQuestion;
    /**
     *
     */
    protected boolean nMovesDone;
    /**
     *
     */
    protected boolean waitingForShootingOrder;

    public PlayerStatus(){
        this.isFirstTurn = true;
    }
}
