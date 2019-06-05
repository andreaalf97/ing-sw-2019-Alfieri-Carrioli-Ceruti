package it.polimi.ingsw.model.map;

public enum MapName {
    FIRE("src/main/resources/Grafica/Mappe/FIRE.png"),
    EARTH("/Grafica/Mappe/EARTH.png"),
    WIND("src/main/resources/Grafica/Mappe/WIND.png"),
    WATER("src/main/resources/Grafica/Mappe/WATER.png");

    private final String path;

    MapName(String path){
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }
}
