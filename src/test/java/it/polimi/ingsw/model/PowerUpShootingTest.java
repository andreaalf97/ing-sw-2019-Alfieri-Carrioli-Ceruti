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
        playersNamesTest.add(ShootingTest.playerGino);
        playersNamesTest.add(ShootingTest.playerAndreaalf);
        playersNamesTest.add(ShootingTest.playerMeme);

        gameTest = new Game(playersNamesTest, MapName.FIRE, 6, 0);
    }

    @Test
    public void shootWithTargetingScope(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("TargetingScope");


        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerAndreaalf);
        gameTest.revive(ShootingTest.playerGino);
        gameTest.revive(ShootingTest.playerMeme);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);
        gameTest.movePlayer(ShootingTest.playerAndreaalf, 1, 0);

        try {
            gameTest.useDamagePowerUp(ShootingTest.playerGino, ShootingTest.playerAndreaalf, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect());
        }
        catch(InvalidChoiceException e){
            Assert.fail();
        }
        Assert.assertEquals(ShootingTest.playerGino, gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getDamages().get(0));

    }

    @Test
    public void shootWithTargetingScopeToMyself(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("TargetingScope");


        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerAndreaalf);
        gameTest.revive(ShootingTest.playerGino);
        gameTest.revive(ShootingTest.playerMeme);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);
        gameTest.movePlayer(ShootingTest.playerAndreaalf, 1, 0);

        try {
            gameTest.useDamagePowerUp(ShootingTest.playerGino, ShootingTest.playerGino, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect());
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertTrue(gameTest.getPlayerByNickname(ShootingTest.playerGino).getDamages().size() == 0);
    }

    @Test
    public void shootWithTagbackGrenade(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("TagbackGrenade");


        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerAndreaalf);
        gameTest.revive(ShootingTest.playerGino);
        gameTest.revive(ShootingTest.playerMeme);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);
        gameTest.movePlayer(ShootingTest.playerAndreaalf, 1, 0);

        try {
            gameTest.useDamagePowerUp(ShootingTest.playerGino, ShootingTest.playerAndreaalf, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect());
        }
        catch(InvalidChoiceException e){
            Assert.fail();
        }

        Assert.assertEquals(ShootingTest.playerGino, gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getMarks().get(0));

    }

    @Test
    public void shootWithTagbackGrenadeCantSeeOffender(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("TagbackGrenade");


        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerAndreaalf);
        gameTest.revive(ShootingTest.playerGino);
        gameTest.revive(ShootingTest.playerMeme);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);
        gameTest.movePlayer(ShootingTest.playerAndreaalf, 2, 2);

        try {
            gameTest.useDamagePowerUp(ShootingTest.playerGino, ShootingTest.playerAndreaalf, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect());
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0, gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getMarks().size());

    }

    @Test
    public void moveWithTeleporter(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Teleporter");

        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerGino);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);

        try {
            gameTest.useMovementPowerUp(ShootingTest.playerGino, ShootingTest.playerGino, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect(), 2 , 2);
        }
        catch(InvalidChoiceException e){
            Assert.fail();
        }

        Assert.assertEquals(2 , gameTest.getPlayerByNickname(ShootingTest.playerGino).getxPosition());
        Assert.assertEquals(2 , gameTest.getPlayerByNickname(ShootingTest.playerGino).getyPosition());

    }

    @Test
    public void moveAnotherPlayerWithTeleporter(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Teleporter");


        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerGino);
        gameTest.revive(ShootingTest.playerAndreaalf);


        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerAndreaalf, 0, 0);
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);


        try {
            gameTest.useMovementPowerUp(ShootingTest.playerGino, ShootingTest.playerAndreaalf, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect(), 2 , 2);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0 , gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getxPosition());
        Assert.assertEquals(0 , gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getyPosition());

    }

    @Test
    public void moveWithTeleporterInInvalidSpot(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Teleporter");


        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerGino);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);

        try {
            gameTest.useMovementPowerUp(ShootingTest.playerGino, ShootingTest.playerGino, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect(), 2 , 0);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0 , gameTest.getPlayerByNickname(ShootingTest.playerGino).getxPosition());
        Assert.assertEquals(0 , gameTest.getPlayerByNickname(ShootingTest.playerGino).getyPosition());

    }

    @Test
    public void moveMyselfWithNewton(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Newton");

        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerGino);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);

        try {
            gameTest.useMovementPowerUp(ShootingTest.playerGino, ShootingTest.playerGino, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect(), 2 , 2);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0 , gameTest.getPlayerByNickname(ShootingTest.playerGino).getxPosition());
        Assert.assertEquals(0 , gameTest.getPlayerByNickname(ShootingTest.playerGino).getyPosition());

    }

    @Test
    public void moveWithNewtonCheckNotLinear(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Newton");


        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerGino);
        gameTest.revive(ShootingTest.playerAndreaalf);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 1, 1);
        gameTest.movePlayer(ShootingTest.playerAndreaalf, 1, 1);

        try {
            gameTest.useMovementPowerUp(ShootingTest.playerGino, ShootingTest.playerAndreaalf, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect(), 0 , 0);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(1 , gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getxPosition());
        Assert.assertEquals(1 , gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getyPosition());

    }

    @Test
    public void moveWithNewtonMoreThan2nMoves(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Newton");


        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerGino);
        gameTest.revive(ShootingTest.playerAndreaalf);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);
        gameTest.movePlayer(ShootingTest.playerAndreaalf, 1, 3);

        try {
            gameTest.useMovementPowerUp(ShootingTest.playerGino, ShootingTest.playerAndreaalf, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect(), 1 , 0);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(1 , gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getxPosition());
        Assert.assertEquals(3 , gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getyPosition());

    }


    @Test
    public void moveWithNewtonRight(){
        PowerUp powerUpTest = JsonDeserializer.createPowerUpFromJson("Newton");


        //Players are dead by default so I need to revive them
        gameTest.revive(ShootingTest.playerGino);
        gameTest.revive(ShootingTest.playerAndreaalf);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);
        gameTest.movePlayer(ShootingTest.playerAndreaalf, 1, 0);

        try {
            gameTest.useMovementPowerUp(ShootingTest.playerGino, ShootingTest.playerAndreaalf, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().get(0).getEffect(), 1 , 1);
        }
        catch(InvalidChoiceException e){
            Assert.assertTrue(true);
        }

        Assert.assertEquals(1 , gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getxPosition());
        Assert.assertEquals(1 , gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getyPosition());

    }
}
