package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.map.MapName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class PowerUpShootingTest {

    private Game gameTest;

    @Before
    public void setup() {
        ArrayList<String> playersNamesTest = new ArrayList<>();
        playersNamesTest.add("gino");
        playersNamesTest.add("andreaalf");
        playersNamesTest.add("meme");

        gameTest = new Game(playersNamesTest, MapName.FIRE, 6);
    }

    @Test
    public void shootWithTargetingScope(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("TargetingScope");


        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("andreaalf", 1, 0);

        try {
            gameTest.useDamagePowerUp("gino", "andreaalf", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect());
        }
        catch(InvalidChoiceException e){
            Assert.fail();
        }
        Assert.assertEquals("gino", gameTest.getPlayerByNickname("andreaalf").getDamages().get(0));

    }

    @Test
    public void shootWithTargetingScopeToMyself(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("TargetingScope");


        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("andreaalf", 1, 0);

        try {
            gameTest.useDamagePowerUp("gino", "gino", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect());
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertTrue(gameTest.getPlayerByNickname("gino").getDamages().size() == 0);
    }

    @Test
    public void shootWithTagbackGrenade(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("TagbackGrenade");


        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("andreaalf", 1, 0);

        try {
            gameTest.useDamagePowerUp("gino", "andreaalf", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect());
        }
        catch(InvalidChoiceException e){
            Assert.fail();
        }

        Assert.assertEquals("gino", gameTest.getPlayerByNickname("andreaalf").getMarks().get(0));

    }

    @Test
    public void shootWithTagbackGrenadeCantSeeOffender(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("TagbackGrenade");


        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("andreaalf", 2, 2);

        try {
            gameTest.useDamagePowerUp("gino", "andreaalf", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect());
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getMarks().size());

    }

    @Test
    public void moveWithTeleporter(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Teleporter");

        //Players are dead by default so I need to revive them
        gameTest.revive("gino");

        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("gino", 0, 0);

        try {
            gameTest.useMovementPowerUp("gino", "gino", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect(), 2 , 2);
        }
        catch(InvalidChoiceException e){
            Assert.fail();
        }

        Assert.assertEquals(2 , gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2 , gameTest.getPlayerByNickname("gino").getyPosition());

    }

    @Test
    public void moveAnotherPlayerWithTeleporter(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Teleporter");


        //Players are dead by default so I need to revive them
        gameTest.revive("gino");
        gameTest.revive("andreaalf");


        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 0);


        try {
            gameTest.useMovementPowerUp("gino", "andreaalf", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect(), 2 , 2);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0 , gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(0 , gameTest.getPlayerByNickname("andreaalf").getyPosition());

    }

    @Test
    public void moveWithTeleporterInInvalidSpot(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Teleporter");


        //Players are dead by default so I need to revive them
        gameTest.revive("gino");

        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("gino", 0, 0);

        try {
            gameTest.useMovementPowerUp("gino", "gino", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect(), 2 , 0);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0 , gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0 , gameTest.getPlayerByNickname("gino").getyPosition());

    }

    @Test
    public void moveMyselfWithNewton(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Newton");

        //Players are dead by default so I need to revive them
        gameTest.revive("gino");

        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("gino", 0, 0);

        try {
            gameTest.useMovementPowerUp("gino", "gino", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect(), 2 , 2);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0 , gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0 , gameTest.getPlayerByNickname("gino").getyPosition());

    }

    @Test
    public void moveWithNewton(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Newton");


        //Players are dead by default so I need to revive them
        gameTest.revive("gino");
        gameTest.revive("andreaalf");

        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("andreaalf", 1, 1);

        try {
            gameTest.useMovementPowerUp("gino", "andreaalf", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect(), 0 , 0);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0 , gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(0 , gameTest.getPlayerByNickname("andreaalf").getyPosition());

    }

    @Test
    public void moveWithNewtonMoreThan2nMoves(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Newton");


        //Players are dead by default so I need to revive them
        gameTest.revive("gino");
        gameTest.revive("andreaalf");

        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("andreaalf", 0, 0);

        try {
            gameTest.useMovementPowerUp("gino", "andreaalf", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect(), 2 , 2);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0 , gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(0 , gameTest.getPlayerByNickname("andreaalf").getyPosition());

    }

}
