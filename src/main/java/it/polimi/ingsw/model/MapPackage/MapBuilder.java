package it.polimi.ingsw.model.MapPackage;

import com.google.gson.*;
import it.polimi.ingsw.model.CardsPackage.PowerupDeck;
import it.polimi.ingsw.model.CardsPackage.WeaponDeck;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class MapBuilder {

    //TODO is this necessary??
    private MapBuilder(){}



    /**
     * This static method reads the necessary values from a JSON file and constructs the new map selected
     * @param mapName the name of the chosen map
     * @return the map
     */
    public static GameMap generateMap(MapName mapName, WeaponDeck weaponDeck, PowerupDeck powerupDeck)
    {
        GameMap tempGameMap = new GameMap();
        //temporary variable for the dynamic type of the matrix single spot
        boolean isSpawnSpot;

        //temporary variable for the room of the matrix single spot
        Room room;

        //temporary variable for the doors of the matrix single spot
        ArrayList<Boolean> doors;
        JsonArray jsonDoors;

        try {
            JsonElement jsonElement = new JsonParser().parse(new FileReader("resources/maps.json"));
            JsonObject root = jsonElement.getAsJsonObject(); //all the file maps.json is now saved in root

            JsonElement jsonMyMap = root.get(mapName.toString());
            JsonObject jsonObjectMyMap = jsonMyMap.getAsJsonObject(); //the map selected is saved in jsonObjectMymap

            //these two nested for cycles load the information of the single spot in the matrix map
            for(int i = 0; i <= 2; i++) {
                JsonObject jsonRow = jsonObjectMyMap.get("row"+ i).getAsJsonObject();
                for (int j = 0; j <= 3; j++){
                    JsonObject jsonCol = jsonRow.get("col" + j).getAsJsonObject();
                    if (!jsonCol.toString().equals("{}")) {

                        isSpawnSpot = jsonCol.get("isSpawnSpot").getAsBoolean();
                        room = Room.valueOf(jsonCol.get("room").getAsString());

                        doors = new ArrayList<>();
                        jsonDoors = jsonCol.get("doors").getAsJsonArray();

                        for (int k = 0; k <= 3 ; k++) {
                            doors.add(jsonDoors.get(k).getAsBoolean());
                        }

                        if (isSpawnSpot)
                            tempGameMap.setSpot(i, j, new SpawnSpot(doors, room));
                        else
                            tempGameMap.setSpot(i, j, new AmmoSpot(doors, room));
                    }
                }
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        tempGameMap.refillAll(weaponDeck, powerupDeck);

        return tempGameMap;
    }
}
