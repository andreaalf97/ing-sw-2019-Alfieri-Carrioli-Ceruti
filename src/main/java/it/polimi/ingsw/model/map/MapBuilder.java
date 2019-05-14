package it.polimi.ingsw.model.map;

import com.google.gson.*;
import it.polimi.ingsw.model.cards.PowerUpDeck;
import it.polimi.ingsw.model.cards.WeaponDeck;
import it.polimi.ingsw.model.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

public class MapBuilder {

    private static Random rand = new Random();


    /**
     * This static method reads the necessary values from a JSON file and constructs the new map selected
     * @param mapName the name of the chosen map
     * @return the map
     */
    public static GameMap generateMap(MapName mapName, WeaponDeck weaponDeck, PowerUpDeck powerupDeck) {

        //The Spot matrix I will work on
        Spot[][] tempSpotMatrix = new Spot[3][4];


        try {
            JsonElement jsonElement = new JsonParser().parse(new FileReader("src/main/resources/maps.json"));

            JsonObject jsonObjectMyMap = jsonElement.getAsJsonObject().get(mapName.toString()).getAsJsonObject(); //the map selected is saved in jsonObjectMymap

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

                            tempSpotMatrix[i][j].refill(weaponDeck.drawCard());
                            tempSpotMatrix[i][j].refill(weaponDeck.drawCard());
                            tempSpotMatrix[i][j].refill(weaponDeck.drawCard());
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
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

        //The map is created with its PROTECTED constructor
        return new GameMap(tempSpotMatrix);
    }

}
