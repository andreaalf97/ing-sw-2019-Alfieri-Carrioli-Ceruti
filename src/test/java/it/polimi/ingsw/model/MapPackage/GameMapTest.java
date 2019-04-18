package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.CardsPackage.PowerupDeck;
import it.polimi.ingsw.model.CardsPackage.WeaponDeck;
import org.junit.Assert;
import org.junit.Test;

public class GameMapTest {

    @Test
    public void verifyJsonMapReading(){
        MapBuilder.generateMap(MapName.FIRE, new WeaponDeck(), new PowerupDeck());

        Assert.assertTrue(true);
    }

    @Test
    public void movePlayer() {
        Assert.assertEquals(0, 0);
    }

    @Test
    public void see(){
        //TODO
    }
}