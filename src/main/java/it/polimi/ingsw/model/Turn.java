package it.polimi.ingsw.model;

public class Turn {

    private String owner;
    private int nTurn;
    private int nMovesDone;
    Game myGame;

    public Turn(String owner, int nTurn, int nMovesDone, Game myGame){
        this.owner = owner;
        this.nTurn = nTurn;
        this.nMovesDone = nMovesDone;
        this.myGame = myGame;
    }

    public Turn(String owner, int nTurn, Game myGame){
        this(owner, nTurn, 0, myGame);
    }

    public void respawn(){return;}

    public void movePlayer(Spot spot){return;}

    public boolean[][] whereCanShoot(){
        boolean[][] temp = new boolean[1][1];
        temp[0][0] = true;
        return temp;
    }

    public boolean[][] whereCanMove(){
        boolean[][] temp = new boolean[1][1];
        temp[0][0] = true;
        return temp;
    }

    public boolean[][] whereCanGrab(){
        boolean[][] temp = new boolean[1][1];
        temp[0][0] = true;
        return temp;
    }

    public void attack(Weapon weapon, String otherPlayer){return;}

    public void reload(Weapon myWeapon){return;}

    public void grabWeapon(){return;}

    public void moveAndGrab(){return;}

    public void startTurn(){return;}

    public void endTurn(){return;}

    public void freePlayerBoard(){return;}

    public void updateJML(){return;}

    public void run(){return;}

    public void givePointsToPlayer(String player, int nPoints){return;}
}
