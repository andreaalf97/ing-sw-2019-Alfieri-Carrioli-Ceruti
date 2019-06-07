package it.polimi.ingsw.model.map;

public enum MapName {
    FIRE("/graphics/boards/FIRE.png"),
    EARTH("/graphics/boards/EARTH.png"),
    WIND("/graphics/boards/WIND.png"),
    WATER("/graphics/boards/WATER.png");

    private final String path;

    MapName(String path){
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }
}
