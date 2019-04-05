package it.polimi.ingsw.model;

public class FirstTurn extends Turn {

    public FirstTurn(String owner, int nTurn, int nMovesDone, Game myGame){
        super(owner, nTurn, nMovesDone, myGame);
    }
    public FirstTurn(String owner, int nTurn, Game myGame){
        super(owner, nTurn, 0, myGame);
    }

    @Override
    public void startTurn(){



    }

    @Override
    public void endTurn(){return;}
}
