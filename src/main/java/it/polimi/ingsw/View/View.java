package it.polimi.ingsw.View;

import java.util.Observable;
import java.util.Observer;

/*
    THE VIEW:
        - What the user sees (UI)
        - In Web Development consists of HTML & CSS, for us it's the CLI & the GUI
        - Communicates with the controller
        - Can be passed dynamic values from the controller
        - Template Engines
 */

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