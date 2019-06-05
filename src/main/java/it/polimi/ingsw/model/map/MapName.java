package it.polimi.ingsw.model.map;

public enum MapName {
    FIRE("/Grafica/Mappe/FIRE.png"),
    EARTH("/Grafica/Mappe/EARTH.png"),
    WIND("/Grafica/Mappe/WIND.png"),
    WATER("/Grafica/Mappe/WATER.png");

    private final String path;

    MapName(String path){
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }
}
