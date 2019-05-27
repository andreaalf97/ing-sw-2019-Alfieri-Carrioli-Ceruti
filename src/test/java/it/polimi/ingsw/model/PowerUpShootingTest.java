package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("TargetingScope", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("TargetingScope", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("TagbackGrenade", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("TagbackGrenade", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("Teleporter", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("Teleporter", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("Teleporter", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("Newton", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("Newton", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
        PowerUp powerUpTest;
        try {
            JsonObject powerupsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Powerups").getAsJsonObject();
            powerUpTest = new PowerUp("Newton", powerupsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

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
