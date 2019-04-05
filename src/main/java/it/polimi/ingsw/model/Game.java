package it.polimi.ingsw.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Game {
    private String winner;
    private String firstPlayer;
    private String currentPlayer;
    private Turn currentTurn;
    private Board board;
    private ArrayList<Player> players;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Turn currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    //#####################################################################

    public boolean addPlayer(String newPlayer){
        if(playerNames().contains(newPlayer)){
            return false;
        }

        players.add(new Player(newPlayer));
        return true;
    }

    public ArrayList<String> playerNames(){
        Iterator i = this.players.iterator();
        ArrayList<String> names = new ArrayList<>();

        while(i.hasNext()){
            Player temp = (Player)i.next();
            names.add(temp.getNickname());
        }

        return names;
    }

    public void pickFirstPlayer(){ firstPlayer = players.get(0).getNickname(); }

    public void calcFinalScores(){return;}

    public void givePointsToPlayer(String player, int nPoints){
        Iterator i = players.iterator();

        while(i.hasNext()){
            Player temp = (Player)i.next();

            if(temp.getNickname() == player)
                temp.givePoints(nPoints);
        }
    }

    public void runTurn(){}
}
