package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Spot {

    private ArrayList<String> playersHere;
    private ArrayList<Boolean> doors; //North-> 0, East->1, South->2, West ->3
    private Room room;

    //TODO might not need these 3 attributes below --> Spot is already referenced by the Map Matrix
    private int idSpot;    /*a spot in the map is identified by <room, idSpot>  */
    private int positionX;
    private int positionY;

    public Spot(Room room, int idSpot, int positionX, int positionY){
        this.playersHere = new ArrayList<>();
        this.doors = new ArrayList<>(4);
        this.room = room;
        this.idSpot = idSpot;
        this.positionX = positionX;
        this.positionY = positionY;
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
