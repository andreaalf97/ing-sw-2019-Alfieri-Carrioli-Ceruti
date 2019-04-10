package it.polimi.ingsw.model.Map;

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
        JsonParser jsonParser = new JsonParser();
        try{
            Object obj = jsonParser.parse(new FileReader("maps.json"));
            JsonObject jsonObject = (JsonObject) obj;

            //jsonObject.get(mapName);





        }
        catch(Exception e){
            e.printStackTrace();
        }

        return new Map(new Spot[1][1]);
    }
}
