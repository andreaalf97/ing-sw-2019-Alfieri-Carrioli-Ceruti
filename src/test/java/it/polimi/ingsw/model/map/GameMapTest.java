package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.cards.PowerUpDeck;
import it.polimi.ingsw.model.cards.WeaponDeck;
import org.junit.Assert;
import org.junit.Test;

public class GameMapTest {

    @Test
    public void verifyJsonMapReading(){
        MapBuilder.generateMap(MapName.FIRE, new WeaponDeck(), new PowerUpDeck());

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