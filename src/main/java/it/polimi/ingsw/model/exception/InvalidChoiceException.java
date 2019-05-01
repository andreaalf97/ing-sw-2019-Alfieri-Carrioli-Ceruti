package it.polimi.ingsw.model.exception;

/**
 * Tells the Controller if the player choice is not valid
 */
public class InvalidChoiceException extends Exception {

    public InvalidChoiceException(String message){
        super(message);
    }
}
