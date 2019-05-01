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

    //todo manca la frenesia?

    /**
     * The type of answer the controller is waiting for
     */
    protected Question waitingForAnswerToThisQuestion;

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
