package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Game {
    private String winner;
    private String firstPlayer;
    private String currentPlayer;
    private Turn currentTurn;
    private Board board;
    private ArrayList<Player> players;

    public boolean addPlayer(){return true;}

    public void pickFirstPlayer(){return;}

    public void calcFinalScores(){return;}

    public void givePointsToPlayer(String player, int nPoints){return;}
}
