package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.map.MapName;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class GameTest {

    @Test
    public void shootPlayerThor(){

        //Testing if attacking with Thor works as expected

        //Creating the list of players for the test
        ArrayList<String> playerNames = new ArrayList<>();
        //Offender
        playerNames.add("andreaalf");
        //Defender
        playerNames.add("ginogino");

        //creating a new game
        Game game = new Game(playerNames, MapName.FIRE, 5);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Thor", weaponsJSON);
        }
        catch (FileNotFoundException e){
            Assert.fail();
            return;
        }

        game.revive("andreaalf");
        game.revive("ginogino");

        game.giveWeaponToPlayer("andreaalf", weaponTest);
        game.giveWeapon("ginogino");

        game.movePlayer("andreaalf", 0, 0);
        game.movePlayer("ginogino", 0, 1);

        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("ginogino");

        //game.shootPlayer("andreaalf", defenders, weaponTest);

        //TODO don't know how to specify order to follow

        Assert.assertTrue(true);
    }

}