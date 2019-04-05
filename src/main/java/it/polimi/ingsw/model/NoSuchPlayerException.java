package it.polimi.ingsw.model;

public class NoSuchPlayerException extends Exception {
    public NoSuchPlayerException(){
        super();
    }
    public NoSuchPlayerException(String s){
        super(s);
    }
}
