package it.polimi.ingsw.model;

public class FrenzyTurn extends Turn {

    public FrenzyTurn(String owner, int nTurn, int nMovesDone, Game myGame){
        super(owner, nTurn, nMovesDone, myGame);
    }
    public FrenzyTurn(String owner, int nTurn, Game myGame){ super(owner, nTurn, 0, myGame); }

    @Override
    public boolean[][] whereCanShoot(){
        boolean[][] temp = new boolean[1][1];
        temp[0][0] = true;
        return temp;
    }

    @Override
    public boolean[][] whereCanMove(){
        boolean[][] temp = new boolean[1][1];
        temp[0][0] = true;
        return temp;
    }

    @Override
    public boolean[][] whereCanGrab(){
        boolean[][] temp = new boolean[1][1];
        temp[0][0] = true;
        return temp;
    }
}
