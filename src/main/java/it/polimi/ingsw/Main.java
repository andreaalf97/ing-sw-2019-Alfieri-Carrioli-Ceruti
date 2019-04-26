package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.WaitingRoom;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class Main {

    //TODO Refactor this after moving it to controller package

    /**
     * The list of active games
     */
    private ArrayList<Controller> games;

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
    public Main(){
        this.games = new ArrayList<>();
        this.waitingRooms = new ArrayList<>();
    }

    /**
     * Starts the game by passing the associated waiting room
     * @param room the waiting room
     */
    public void startGame(WaitingRoom room){

        //Creating and adding a new game to the list
        games.add(new Controller(new Game(room.getPlayers(), room.getVotedMap(), room.getVotedSkulls()), new View(room.getPlayers())));

        games.get(games.size() - 1).runGame();

        //TODO need to close all connections once the game is over --> how to do it object oriented?
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

        Main main = new Main();

        Log.LOGGER.info("Opening up for new players...");

        //Now the idea is to open the server for new connections
        //Each connection is a new player that is added to his waiting room.
        //Once the room is full (or a timer stops) a new Game (model) and a new view are created
        //The view keeps the connection open with all the players while the Game class is just the representation of the current game
        //TODO Might have to move the run() method from Game to view

        Log.LOGGER.warning("Adding a new random player just for testing --> USING WARNINGS");

        //20 chars max
        Log.LOGGER.warning("What is your nickname?");

        //Can only be FIRE, WATER, EARTH, WIND
        Log.LOGGER.warning("Choose map...");

        //Must be 5 <= nSkulls <= 8
        Log.LOGGER.warning("Choose nSkulls...");

        main.addPlayer("andreaalf", MapName.FIRE, 6);
        main.addPlayer("ginocerutino", MapName.FIRE, 6);
        main.addPlayer("mememe", MapName.WATER, 6);

        while(true){
            //Keep accepting new players

            for(WaitingRoom i : main.waitingRooms){
                if(i.isReady()){
                    main.startGame(i);
                    main.removeRoom(i);
                }
            }

        }

    }
}
