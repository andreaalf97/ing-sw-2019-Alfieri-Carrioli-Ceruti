package it.polimi.ingsw.model.MapPackage;

import com.google.gson.*;
import java.io.FileReader;

public class MapBuilder {

    //TODO is this necessary??
    private MapBuilder(){}

    /**
     * This static method reads the necessary values from a JSON file and constructs a new map
     * @param mapName the name of the chosen map
     * @return the map
     */
    public static Map generateMap(String mapName)
    {
        Map tempMap = new Map(new Spot [3][4]);

        JsonParser jsonParser = new JsonParser();


        try{
            Object obj = jsonParser.parse(new FileReader("resources/maps.json"));
            JsonObject jsonObject = (JsonObject) obj;
            //jsonObject.get(mapName);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return tempMap;
    }
}
