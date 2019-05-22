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

        ArrayList<Color> costChosen = new ArrayList<>();
        costChosen.add(Color.BLUE);
        try {
            gameTest.useDamagePowerUp("gino", "andreaalf", gameTest.getPlayerByNickname("gino").getPowerUpList().get(0).getEffect(), costChosen );
        }
        catch(InvalidChoiceException e){
            Assert.fail();
        }

        //TODO
        //Assert.assertEquals("gino", gameTest.getPlayerByNickname("amdreaalf").getDamages().get(0));
    }

    @Test
    public void shoot(){}

}
