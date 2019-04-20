package it.polimi.ingsw.Model.MapPackage;

import com.google.gson.*;
import it.polimi.ingsw.Model.CardsPackage.PowerupDeck;
import it.polimi.ingsw.Model.CardsPackage.WeaponDeck;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class MapBuilder {

    /**
     * This static method reads the necessary values from a JSON file and constructs the new map selected
     * @param mapName the name of the chosen map
     * @return the map
     */
    public static GameMap generateMap(MapName mapName, WeaponDeck weaponDeck, PowerupDeck powerupDeck) {

        //The Spot matrix I will work on
        Spot[][] tempSpotMatrix = new Spot[3][4];

        //Random number generator
        Random rand = new Random();

        try {
            JsonElement jsonElement = new JsonParser().parse(new FileReader("resources/maps.json"));
            JsonObject root = jsonElement.getAsJsonObject(); //the entire json file as a JsonObject

            //TODO do we need a JsonElement AND a JsonObject?
            JsonElement jsonMyMap = root.get(mapName.toString());
            JsonObject jsonObjectMyMap = jsonMyMap.getAsJsonObject(); //the map selected is saved in jsonObjectMymap

            //these two nested for cycles load the information of the single spot in the matrix map
            for(int i = 0; i < tempSpotMatrix.length; i++) {

                JsonObject jsonRow = jsonObjectMyMap.get("row"+ i).getAsJsonObject();
                for (int j = 0; j < tempSpotMatrix[i].length; j++){

                    //FOR EACH SPOT IN THE JSON FILE

                    JsonObject jsonCol = jsonRow.get("col" + j).getAsJsonObject(); // <== jsonSpot

                    if (!jsonCol.toString().equals("{}")) {

                        //Reading isSpawnSpot & isAmmoSpot
                        boolean isSpawnSpot = jsonCol.get("isSpawnSpot").getAsBoolean();
                        boolean isAmmoSpot = jsonCol.get("isAmmoSpot").getAsBoolean();

                        //Reading all rooms
                        Room room = Room.valueOf(jsonCol.get("room").getAsString());

                        //Creating a Java ArrayList from a JsonArray object
                        ArrayList<Boolean>doors = new ArrayList<>();
                        JsonArray jsonDoors = jsonCol.get("doors").getAsJsonArray();
                        for (int k = 0; k <= 3 ; k++) {
                            doors.add(jsonDoors.get(k).getAsBoolean());
                        }

                        //Constructing a new SpawnSpot
                        if(isSpawnSpot) { //If this is a Spawn spot I add all the weapons
                            tempSpotMatrix[i][j] = new SpawnSpot(doors, room);

                            tempSpotMatrix[i][j].refill(weaponDeck.pickCard());
                            tempSpotMatrix[i][j].refill(weaponDeck.pickCard());
                            tempSpotMatrix[i][j].refill(weaponDeck.pickCard());
                        }

                        //Constructing a new AmmoSpot
                        if(isAmmoSpot) {
                            //If this is an ammo spot I randomly add (or don't) a power up and some ammo
                            tempSpotMatrix[i][j] = new AmmoSpot(doors, room);

                            if(rand.nextBoolean()){
                                tempSpotMatrix[i][j].refill(powerupDeck.drawCard()); //Refills with a powerup
                            }
                            else{
                                tempSpotMatrix[i][j].refill(null); //Refills only ammos
                            }
                        }

                    }
                    else
                        tempSpotMatrix[i][j] = null;
                }
            }
        }
        catch (FileNotFoundException e){
            //If the file does not exist || we have problems with the file
            e.printStackTrace();
        }

        //The map is created with its PROTECTED constructor
        return new GameMap(tempSpotMatrix);
    }

}
