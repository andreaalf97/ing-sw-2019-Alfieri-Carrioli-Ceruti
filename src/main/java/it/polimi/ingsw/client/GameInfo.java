package it.polimi.ingsw.client;

import it.polimi.ingsw.client.gui.OtherPlayerInfo;
import it.polimi.ingsw.model.KillShotTrack;
import it.polimi.ingsw.model.map.GameMap;

import java.util.ArrayList;

public class GameInfo {

    public ArrayList<String> playersNames;

    public KillShotTrack killShotTrack;

    public GameMap gameMap;

    public PlayerInfo playerInfo;

    public ArrayList<OtherPlayerInfo> otherPlayerInfos;

    public GameInfo( ArrayList<String> playersNames, KillShotTrack killShotTrack, GameMap gameMap, PlayerInfo playerInfo, ArrayList<OtherPlayerInfo> otherPlayerInfos){

        this.playersNames = playersNames;
        this.killShotTrack = killShotTrack;
        this.gameMap = gameMap;
        this.playerInfo = playerInfo;
        this.otherPlayerInfos = otherPlayerInfos;
    }

}
