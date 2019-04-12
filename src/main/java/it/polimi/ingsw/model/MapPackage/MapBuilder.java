package it.polimi.ingsw.model.MapPackage;

import com.google.gson.*;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

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
        Map tempMap = new Map();
        //temporary variables for the dynamic type of the matrix single spot
        Boolean isSpawnSpot;

        //temporary variables for the room of the matrix single spot
        Room room;

        //temporary variables for the doors of the matrix single spot
        ArrayList<Boolean> doors;
        JsonArray jsonDoors;

        try {
            JsonElement jsonElement = new JsonParser().parse(new FileReader("resources/maps.json"));
            JsonObject root = jsonElement.getAsJsonObject();

            JsonElement jsonMyMap = root.get(mapName);
            JsonObject jsonObjectMyMap = jsonMyMap.getAsJsonObject();


            for(int i = 0; i <= 2; i++) {
                JsonObject jsonRow = jsonObjectMyMap.get("row"+ i).getAsJsonObject();
                for (int j = 0; j <= 3; j++){
                    JsonObject jsonCol = jsonRow.get("col" + j).getAsJsonObject();

                    if (!jsonCol.toString().equals("{}")) {
                        isSpawnSpot = jsonCol.get("isSpawnSpot").getAsBoolean();
                        room = Room.valueOf(jsonCol.get("room").getAsString());

                        doors = new ArrayList<>();
                        jsonDoors = jsonCol.get("doors").getAsJsonArray();
                        for (int k = 0; k <= 3; k++) {
                            doors.add(jsonDoors.get(k).getAsBoolean());
                        }

                        if (isSpawnSpot)
                            tempMap.setSpot(i, j, new SpawnSpot(doors, room));
                        else
                            tempMap.setSpot(i, j, new AmmoSpot(doors, room));
                    }
                }
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return tempMap;
    }
}
