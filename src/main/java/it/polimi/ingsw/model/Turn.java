package it.polimi.ingsw.model;

public class Turn {

    private String owner;
    private int nTurn;
    private int nMovesDone;

    public void respawn(){}

    public void movePlayer(Spot spot){}

    public boolean[][] whereCanShoot(){}

    public boolean[][] whereCanMove(){}

    public boolean[][] whereCanGrab(){}

    public void attack(Weapon weapon, String otherPlayer){}

    public void reload(Weapon myWeapon){}

    public void grabWeapon(){}

    public void moveAndGrab(){}

    public void startTurn(){}

    public void endTurn(){}

    public void freePlayerBoard(){}

    public void updateJML(){}
}
