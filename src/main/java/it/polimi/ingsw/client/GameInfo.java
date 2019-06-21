package it.polimi.ingsw.client;

import it.polimi.ingsw.model.KillShotTrack;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.GameMap;

import java.util.ArrayList;

public class GameInfo {

    public ArrayList<String> playersNames;

    public KillShotTrack killShotTrack;

    public GameMap gameMap;

    public ArrayList<Player> playersInfo;

    public GameInfo( ArrayList<String> playersNames, KillShotTrack killShotTrack,GameMap gameMap, ArrayList<Player> playersInfo){

        this.playersNames = playersNames;
        this.killShotTrack = killShotTrack;
        this.gameMap = gameMap;
        this.playersInfo = playersInfo;
    }

}
