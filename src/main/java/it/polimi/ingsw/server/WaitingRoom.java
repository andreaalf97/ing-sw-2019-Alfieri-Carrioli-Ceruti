package it.polimi.ingsw.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.client.RemoteViewInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;

public class WaitingRoom {

    /**
     * The list of connected player
     */
    protected ArrayList<String> players;

    /**
     * Votes for each map:
     */
    private Map mapVotes;

    /**
     * Votes for amount of skulls to use
     */
    private Map skullVotes;

    /**
     * All the open sockets
     */
    protected ArrayList<Socket> sockets;

    /**
     * All the open rmi connections
     */
    protected ArrayList<RemoteViewInterface> remoteViews;

    /**
     * The length of the timer
     */
    protected final static long TIMERMINUTES = (long) 1;

    /**
     * Maximum amount of players for a single game
     */
    protected final static int MAXPLAYERS = 5;

    /**
     * Minimum amount of players for a single game
     */
    protected final static int MINPLAYERS = 3;



    /**
     * Basic constructor
     */
    public WaitingRoom(){
        this.players = new ArrayList<>();
        this.sockets = new ArrayList<>();
        this.remoteViews = new ArrayList<>();

        this.mapVotes = new EnumMap<MapName, Integer>(MapName.class);
        this.mapVotes.put(MapName.FIRE, 0);
        this.mapVotes.put(MapName.EARTH, 0);
        this.mapVotes.put(MapName.WIND, 0);
        this.mapVotes.put(MapName.WATER, 0);

        //The keys can be 5, 6, 7, 8, ecc, depending on the amount of skulls players want on the board
        //The values are the amount of votes each setup received
        this.skullVotes = new HashMap<Integer, Integer>();
        this.skullVotes.put(5, 0);
        this.skullVotes.put(6, 0);
        this.skullVotes.put(7, 0);
        this.skullVotes.put(8, 0);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                closeThisRoom();
            }
        }, 10 * 1000); //This should be TIMERMINUTES * 60 * 1000

    }

    /**
     * Adds a player to the players list and registers their votes
     * @param nickname The nickname of the new player
     * @param mapToVote the map chosen by the new player
     * @param nSkullsToVote the desired amount of skulls
     */
    protected synchronized void addPlayer(Socket socket, String nickname, MapName mapToVote, int nSkullsToVote) {

        addVote(nickname, mapToVote, nSkullsToVote);

        remoteViews.add(null);
        sockets.add(socket);

        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println("You have been added to a waiting room, timer is set to " + TIMERMINUTES +  " minutes");
            printWriter.flush();
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while sending message through socket");
        }


        if(players.size() == MAXPLAYERS)
            closeThisRoom();

    }

    protected synchronized void addPlayer(RemoteViewInterface remoteView, String nickname, MapName mapToVote, int nSkullsToVote){

        addVote(nickname, mapToVote, nSkullsToVote);

        remoteViews.add(remoteView);
        sockets.add(null);

        try {
            remoteView.sendMessage("You have been added to a waiting room, timer is set to " + TIMERMINUTES +  " minutes");
        }
        catch (RemoteException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while sending message from waiting room through rmi");
        }


        if(players.size() == MAXPLAYERS)
            closeThisRoom();

    }

    private synchronized void addVote(String nickname, MapName mapToVote, int nSkullsToVote){

        if(players.contains(nickname))
            throw new RuntimeException("This waitingRoom already contains this player");

        players.add(nickname);

        int tempVotes = (int) (mapVotes.get(mapToVote));
        mapVotes.put(mapToVote, tempVotes + 1);

        if (nSkullsToVote < 5 || nSkullsToVote > 8)
            throw new RuntimeException("nSkullsToVote must be between 5 and 8");


        tempVotes = (int) skullVotes.get(nSkullsToVote);
        skullVotes.put(nSkullsToVote, tempVotes + 1);

    }

    private void closeThisRoom() {

        if(players.size() < MINPLAYERS){
            GamesHandler.roomNotFilledInTime(this);
            return;
        }

        GamesHandler.startGame(this);

    }

    /**
     * @return the most voted game map
     */
    protected MapName getVotedMap(){
        MapName tempMaxMapName = MapName.values()[0];
        int tempMaxVotes = (int)mapVotes.get(MapName.values()[0]);

        for(int i = 1; i < MapName.values().length; i++){
            if((int)mapVotes.get(MapName.values()[i]) > tempMaxVotes){
                tempMaxMapName = MapName.values()[i];
                tempMaxVotes = (int)this.mapVotes.get(MapName.values()[i]);
            }
        }

        return tempMaxMapName;
    }

    /**
     * @return the most voted amount of skulls
     */
    protected int getVotedSkulls(){
        int maxSkulls = (int)skullVotes.get(5);

        for(int i = 6; i <= 8; i++){
            if((int)skullVotes.get(i) > maxSkulls){
                maxSkulls = (int)skullVotes.get(i);
            }
        }

        return maxSkulls;
    }

}
