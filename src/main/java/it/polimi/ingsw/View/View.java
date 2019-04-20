package it.polimi.ingsw.View;

import java.util.Observable;
import java.util.Observer;

public class View extends Observable implements Runnable, Observer {

    @Override
    public void run() {

    }

    /**
     * Here we handle what happens when the Observable objects calls the notifyObservers() method
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {

    }
}