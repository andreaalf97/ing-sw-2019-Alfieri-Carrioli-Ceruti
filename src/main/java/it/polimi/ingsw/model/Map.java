package it.polimi.ingsw.model;

public class Map {

    private Spot[][] map;

    public Map(Spot[][] map){
        this.map = map;
    }

    public boolean see(int spot1X, int spot1Y, int spot2X, int spot2Y){
        Spot spotX = getSpotByIndex(spot1X, spot1Y);
        Spot spotY = getSpotByIndex(spot2X, spot2Y);

        Spot tempSpot;

        if(spotX.getRoom() == spotY.getRoom()){
            return true;
        }
        else{
            if(spotX.hasNorthDoor()){
                tempSpot = getSpotByIndex(spot1X, spot1Y - 1);
                if(tempSpot.getRoom() == spotY.getRoom())
                    return true;
            }
            if(spotX.hasEastDoor()){
                tempSpot = getSpotByIndex(spot1X + 1, spot1Y);
                if(tempSpot.getRoom() == spotY.getRoom())
                    return true;
            }
            if(spotX.hasSouthDoor()){
                tempSpot = getSpotByIndex(spot1X, spot1Y + 1);
                if(tempSpot.getRoom() == spotY.getRoom())
                    return true;
            }
            if(spotX.hasWestDoor()){
                tempSpot = getSpotByIndex(spot1X - 1, spot1Y);
                if(tempSpot.getRoom() == spotY.getRoom())
                    return true;
            }
        }
        return false;
    }

    public Spot getSpotByIndex(int x, int y){ return map[x][y]; }

    public void movePlayer(String player, int newX, int newY){ return; }

    public void refill(int x,int y, Object objToAdd){
        map[x][y].refill(objToAdd);
    }

    public void grabSomething(int x, int y, Player p) {
        map[x][y].grabSomething(p);
    }

    public boolean isAmmoSpot(int x, int y){
        return map[x][y].isAmmoSpot();
    }

    public boolean isSpawnSpot(int x, int y){
        return map[x][y].isSpawnSpot();
    }


}
