package it.polimi.ingsw.model;

public class Board {
    private KillShotTrack kst;
    private WeaponDeck weaponDeck;
    private PowerupDeck powerUpDeck;

    private GetMapFactory mapFactory = new GetMapFactory();

    //private Map map = mapFactory.getMap(N);


    public void chooseMap(int nMappa){
    }

    public void chooseNSkulls(int n){}

    public void setUpGuns(){}

    public void setUpAmmos(){}
}
