package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.View.View;

import java.util.Observable;
import java.util.Observer;

/*
    THE CONTROLLER:
        - Receives input the view
        - Processes requests
        - Gets data from the model
        - Passes data to the view
 */

public class Controller implements Observer {

    /**
     * The MODEL
     */
    Game gameModel;

    /**
     * The VIEW
     */
    View view;

    @Override
    public void update(Observable o, Object arg) {

    }
}
