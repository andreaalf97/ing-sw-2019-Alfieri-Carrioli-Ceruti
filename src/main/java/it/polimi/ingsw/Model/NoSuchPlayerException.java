package it.polimi.ingsw.Model;

public class NoSuchPlayerException extends Exception {
    public NoSuchPlayerException(){
        super();
    }
    public NoSuchPlayerException(String s){
        super(s);
    }
}
