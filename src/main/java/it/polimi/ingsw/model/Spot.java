package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Spot {


    private String room;
    private int idSpot;    /*a spot in the map is identified by <room, idSpot>  */

    private int positionX;
    private int positionY;
    private ArrayList<Boolean> doors;

    public Spot(String room,int idSpot,int positionX,int positionY){
        this.room = room;
        this.idSpot = idSpot;
        this.positionX = positionX;
        this.positionY = positionY;
        this.doors = new ArrayList<>(4);
    }



    public boolean sees(Spot spot){

        return true;
    }

    public boolean hasNordDoor(){
        return doors.get(0);
    }

    public boolean hasEastDoor(){
        return doors.get(1);
    }

    public boolean hasSouthDoor(){
        return doors.get(2);
    }

    public boolean hasWestDoor(){
        return doors.get(3);
    }


    public String getRoom() {
        return room;
    }

    public int getIdSpot() {
        return idSpot;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public ArrayList<Boolean> getDoors() {
        return doors;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setIdSpot(int idSpot) {
        this.idSpot = idSpot;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setDoors(ArrayList<Boolean> doors) {
        this.doors = doors;
    }
}
