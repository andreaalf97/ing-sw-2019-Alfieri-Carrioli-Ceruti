package it.polimi.ingsw.model;

import it.polimi.ingsw.model.MapPackage.MapName;

import java.util.ArrayList;

public class GamesHandler {
    /**
     * The list of active games
     */
    private ArrayList<Game> games;

    /**
     * The list of active waiting rooms
     */
    private ArrayList<WaitingRoom> waitingRooms;

    /**
     * The max amount of players in each game
     */
    private static final int MAXPLAYERS = 8;

    /**
     * Basic constructor
     */
    public GamesHandler(){
        this.games = new ArrayList<>();
        this.waitingRooms = new ArrayList<>();
    }

    /**
     * Starts the game by passing the associated waiting room
     * @param room the waiting room
     */
    public void startGame(WaitingRoom room){

        //Creating and adding a new game to the list
        this.games.add(new Game(room.getPlayers(), room.getVotedMap(), room.getVotedSkulls()));

        //Opening a new thread for the new game
        //TODO might not be the right way to handle it if we are using MVC
        Thread thread = new Thread(this.games.get(this.games.size() - 1));
        thread.start();
    }


    /**
     * Adds the player to the correct waiting room
     * @param nickname the nickname of the player
     * @param mapToVote the map he voted for
     * @param nSkullsToVote the amount of skulls he voted for
     */
    public void addPlayer(String nickname, MapName mapToVote, int nSkullsToVote){

        //If there is no room yet or all the rooms are empty --> create a new room
        if(this.waitingRooms.isEmpty() || this.waitingRooms.get(this.waitingRooms.size() - 1).nPlayers() >= MAXPLAYERS){
            WaitingRoom newWaitingRoom = new WaitingRoom();
            newWaitingRoom.addPlayer(nickname, mapToVote, nSkullsToVote);
            this.waitingRooms.add(newWaitingRoom);
        }

        //If there is a spot for a new player in the current room
        else{
            this.waitingRooms.get(this.waitingRooms.size() - 1).addPlayer(nickname, mapToVote, nSkullsToVote);
        }

    }

    public void removeRoom(WaitingRoom waitingRoom){
        if(this.waitingRooms.contains(waitingRoom))
            this.waitingRooms.remove(waitingRoom);
    }

    public static void main(String args[]){
        Log.LOGGER.info("Server starting...");

        GamesHandler gamesHandler = new GamesHandler();

        Log.LOGGER.info("Opening up for new players...");

        //Now the idea is to open the server for new connections
        //Each connection is a new player that is added to his waiting room.
        //Once the room is full (or a timer stops) a new Game (model) and a new View are created
        //The view keeps the connection open with all the players while the Game class is just the representation of the current game
        //TODO Might have to move the run() method from Game to View

        Log.LOGGER.warning("Adding a new random player just for testing --> USING WARNINGS");

        //20 chars max
        Log.LOGGER.warning("What is your nickname?");

        //Can only be FIRE, WATER, EARTH, WIND
        Log.LOGGER.warning("Choose map...");

        //Must be 5 <= nSkulls <= 8
        Log.LOGGER.warning("Choose nSkulls...");

        gamesHandler.addPlayer("andreaalf", MapName.FIRE, 6);
        gamesHandler.addPlayer("ginocerutino", MapName.FIRE, 6);
        gamesHandler.addPlayer("mememe", MapName.WATER, 6);

        while(true){
            //Keep accepting new players

            for(WaitingRoom i : gamesHandler.waitingRooms){
                if(i.isReady()){
                    gamesHandler.startGame(i);
                    gamesHandler.removeRoom(i);
                }
            }

        }

    }
}
