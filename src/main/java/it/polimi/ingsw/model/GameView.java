package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.GameMap;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**
 * This class collects data from the model and create a custom message for the view
 * The players are not allowed to see the decks from the model
 * TODO the players should not be allowed to see the power up in the ammo spot!
 */
@Deprecated
public class GameView extends Observable implements Observer {

    public ArrayList<Player> players;

    public KillShotTrack kst;

    public GameMap gameMap;

    /**
     * This method updates this object and notifies the View
     * @param o The Game object
     * @param obj Unused
     */
    @Override
    public void update(Observable o, Object obj){
        if(!(o instanceof Game))
            throw new IllegalArgumentException("Illegal update argument for the GameView");

        Game game = (Game)o;

        this.players = game.clonePlayers();

        this.kst = game.cloneKST();

        this.gameMap = game.cloneGameMap();

        setChanged();
        notifyObservers();
    }
}
