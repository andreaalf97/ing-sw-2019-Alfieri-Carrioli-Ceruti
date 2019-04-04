package it.polimi.ingsw.model;

public class FirstTurn extends Turn {

    public FirstTurn(String owner, int nTurn, int nMovesDone){
        super(owner, nTurn, nMovesDone);
    }
    public FirstTurn(String owner, int nTurn){
        super(owner, nTurn, 0);
    }

    @Override
    public void startTurn(){return;}

    @Override
    public void endTurn(){return;}
}
