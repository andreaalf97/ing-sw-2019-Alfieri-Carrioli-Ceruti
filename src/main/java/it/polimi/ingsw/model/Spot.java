package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Spot {


    private Room room;
    private int idSpot;    /*a spot in the map is identified by <room, idSpot>  */
    private int positionX;
    private int positionY;

    private ArrayList<String> playersHere;
    private ArrayList<Boolean> doors; //North-> 0, East->1, South->2, West ->3

    public Spot(Room room,int idSpot,int positionX,int positionY){
        this.room = room;
        this.idSpot = idSpot;
        this.positionX = positionX;
        this.positionY = positionY;

        this.playersHere = new ArrayList<>();
        this.doors = new ArrayList<>(4);
    }



    public boolean sees(Spot spot){
        if(this.getRoom() == spot.getRoom())
            return true;
        else {
            if ((this.getPositionX() == spot.getPositionX() + 1) && this.getDoors().get(1))
                return true;

            if ((this.getPositionX() == spot.getPositionX() - 1) && this.getDoors().get(3))
                return true;

            if ((this.getPositionY() == spot.getPositionY() + 1) && this.getDoors().get(0))
                return true;

            if ((this.getPositionY() == spot.getPositionY() - 1) && this.getDoors().get(2))
                return true;
        }

        return false;
    }

    public boolean hasNordDoor(){ return doors.get(0);}

    public boolean hasEastDoor(){ return doors.get(1); }

    public boolean hasSouthDoor(){ return doors.get(2);}

    public boolean hasWestDoor(){ return doors.get(3);}


    public Room getRoom() { return room;}

    public int getIdSpot() {return idSpot;}

    public int getPositionX() {return positionX;}

    public int getPositionY() { return positionY;}

    public ArrayList<Boolean> getDoors() { return doors;}

    public void setRoom(Room room) { this.room = room;}

    public void setIdSpot(int idSpot) { this.idSpot = idSpot;}

    public void setPositionX(int positionX) { this.positionX = positionX;}

    public void setPositionY(int positionY) { this.positionY = positionY;}

    public void setDoors(ArrayList<Boolean> doors) { this.doors = doors;}

    public ArrayList<String> getPlayersHere() { return playersHere; }

    public void setPlayersHere(ArrayList<String> playersHere) { this.playersHere = playersHere; }
}
