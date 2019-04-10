package it.polimi.ingsw.model;

public class MapBuilder {

    //TODO is this necessary??
    private MapBuilder(){}

    /**
     * This static method reads the necessary values from a JSON file and constructs a new map
     * @param mapName the name of the chosen map
     * @return the map
     */
    public static Map generateMap(MapName mapName){
        return new Map();
    }
}
