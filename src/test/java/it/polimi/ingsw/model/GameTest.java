package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
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

    @Test
    public void wherePlayerCanMove(){

        gameTest.movePlayer("gino", 0 , 0);
        boolean [][] temp = gameTest.wherePlayerCanMove("gino", 1);

        Assert.assertTrue(temp[0][0]);
        Assert.assertTrue(temp[0][1]);
        Assert.assertTrue(temp[1][0]);


        gameTest.movePlayer("andreaalf", 0, 0);
        boolean[][] temp2 = gameTest.wherePlayerCanMove("andreaalf", 10);


        for(int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                if(gameTest.validSpot(i, j))
                    Assert.assertTrue(temp2[i][j]);

    }

    @Test
    public void wherePlayerCanMoveAndGrab(){

        gameTest.movePlayer("gino", 0 , 0);
        boolean [][] temp = gameTest.wherePlayerCanMoveAndGrab("gino", 1);

        Assert.assertTrue(temp[0][0]);
        Assert.assertTrue(temp[0][1]);
        Assert.assertTrue(temp[1][0]);


        gameTest.movePlayer("andreaalf", 0, 0);
        boolean[][] temp2 = gameTest.wherePlayerCanMoveAndGrab("andreaalf", 10);


        for(int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                if(gameTest.validSpot(i, j))
                    Assert.assertTrue(temp2[i][j]);

    }

    @Test
    public void respawn(){
        //this is the first spawn for a player, like the first turn
        PowerUp powerUpTestRed = new PowerUp(Color.RED);
        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTestRed);

        gameTest.respawn("gino", 0);
        Assert.assertFalse(gameTest.getPlayerByNickname("gino").isDead());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getyPosition());
        Assert.assertEquals(0,gameTest.getPlayerByNickname("gino").getPowerUpList().size());


        //another test we can do is when a player get killed and then has to respawn
        PowerUp powerUpTestBlue = new PowerUp(Color.BLUE);
        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTestBlue);

        gameTest.getPlayerByNickname("gino").giveDamage("meme", 11);
        Assert.assertTrue(gameTest.getPlayerByNickname("gino").isDead());

        gameTest.respawn("gino", 0 );
        Assert.assertFalse(gameTest.getPlayerByNickname("gino").isDead());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());
        Assert.assertEquals(0,gameTest.getPlayerByNickname("gino").getPowerUpList().size());

    }

    @Test
    public void refillAllAmmoSpots(){
        //pick some ammos and then control if all spots are refilled
        gameTest.movePlayer("gino", 1 , 0);
        try {
            gameTest.giveAmmoCard("gino");
        }
        catch (InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        gameTest.movePlayer("gino", 1 , 1);
        try {
            gameTest.giveAmmoCard("gino");
        }
        catch (InvalidChoiceException e){
            Assert.fail();
        }

        gameTest.movePlayer("gino", 2 , 1);
        try {
            gameTest.giveAmmoCard("gino");
        }
        catch (InvalidChoiceException e){
            Assert.fail();
        }

        gameTest.refillAllAmmoSpots();

        for(int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                if(gameTest.validSpot(i, j) && gameTest.getSpotByIndex(i,j).isAmmoSpot())
                    Assert.assertTrue(gameTest.getSpotByIndex(i, j).isFull());


    }

    @Test
    public void refillAllSpawnSpots(){
        //pick three weapons and refill the spawn spots
        gameTest.movePlayer("gino", 1, 0);
        gameTest.pickWeapon("gino", 0);

        gameTest.movePlayer("gino", 0, 2);
        gameTest.pickWeapon("gino", 0);

        gameTest.movePlayer("gino", 2, 3);
        gameTest.pickWeapon("gino", 0);

        Assert.assertEquals(3, gameTest.getPlayerByNickname("gino").getWeaponList().size());
        gameTest.refillAllSpawnSpots();

        Assert.assertTrue(gameTest.getSpotByIndex(1,0).isFull());
        Assert.assertTrue(gameTest.getSpotByIndex(0,2).isFull());
        Assert.assertTrue(gameTest.getSpotByIndex(2,3).isFull());

    }

    @Test
    public void movePlayer(){
        gameTest.movePlayer("gino",2, 3);
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(3, gameTest.getPlayerByNickname("gino").getyPosition());
        Assert.assertTrue(gameTest.getSpotByIndex(2, 3).getPlayersHere().contains("gino"));

    }

}