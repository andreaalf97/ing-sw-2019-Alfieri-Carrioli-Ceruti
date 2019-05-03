package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.map.GameMap;
import it.polimi.ingsw.model.map.MapBuilder;
import it.polimi.ingsw.model.map.MapName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class GameTest {

    Game gameTest;

    @Before
    public void setup(){
        ArrayList<String> playersNamesTest = new ArrayList<>();
        playersNamesTest.add("gino");
        playersNamesTest.add("andreaalf");
        playersNamesTest.add("meme");

        gameTest = new Game(playersNamesTest, MapName.FIRE, 5);
    }

    //todo incomplete
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

        //Players are dead by default so I need to revive them
        game.revive("andreaalf");
        game.revive("ginogino");

        //Giving the duplicate to the player
        game.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck
        game.giveWeapon("ginogino");

        //Moving these players to the testing spots
        game.movePlayer("andreaalf", 0, 0);
        game.movePlayer("ginogino", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("ginogino");

        //game.shootPlayer("andreaalf", defenders, weaponTest);

        //TODO don't know how to specify order to follow

        Assert.assertTrue(true);
    }

    @Test
    public void givePowerUp(){
        gameTest.givePowerUp("gino");
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getPowerUpList().size());

        //todo test when weapondeck is empty
    }

    @Test
    public void giveBoardPoints(){
        //i have to do this because in the game at the start we consider the players dead, waiting for spawn
        gameTest.getPlayerByNickname("meme").revive();
        gameTest.getPlayerByNickname("gino").revive();
        gameTest.getPlayerByNickname("andreaalf").revive();



        //check normal kill
        gameTest.getPlayerByNickname("gino").giveDamage("meme", 11);
        gameTest.giveBoardPointsAndModifyKST(gameTest.getPlayerByNickname("gino"));
        Assert.assertTrue(gameTest.getPlayerByNickname("gino").isDead());

        Assert.assertEquals(9, gameTest.getPlayerByNickname("meme").getPoints());

        //check overKill
        gameTest.getPlayerByNickname("andreaalf").giveDamage("gino", 12);
        gameTest.giveBoardPointsAndModifyKST(gameTest.getPlayerByNickname("andreaalf"));
        Assert.assertTrue(gameTest.getPlayerByNickname("andreaalf").isDead());

        Assert.assertEquals(9, gameTest.getPlayerByNickname("gino").getPoints());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getMarks().size());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getPoints());
        Assert.assertFalse(gameTest.getPlayerByNickname("meme").isDead());

    }

    @Test
    public void checkDeaths(){
        gameTest.getPlayerByNickname("meme").revive();
        gameTest.getPlayerByNickname("gino").revive();
        gameTest.getPlayerByNickname("andreaalf").revive();

        gameTest.getPlayerByNickname("gino").giveDamage("andreaalf", 6);
        gameTest.getPlayerByNickname("gino").giveDamage("meme", 5);

        gameTest.checkDeaths();

        Assert.assertTrue(gameTest.getPlayerByNickname("gino").isDead());
        Assert.assertFalse(gameTest.getPlayerByNickname("andreaalf").isDead());
        Assert.assertFalse(gameTest.getPlayerByNickname("meme").isDead());
        Assert.assertEquals(9, gameTest.getPlayerByNickname("andreaalf").getPoints());
        Assert.assertEquals(6, gameTest.getPlayerByNickname("meme").getPoints());
    }

}