package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUpDeck;
import it.polimi.ingsw.model.cards.WeaponDeck;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GameMapTest {

    GameMap gameMapTestFire;

    @Before
    public void setUp() throws Exception {
        gameMapTestFire = MapBuilder.generateMap(MapName.FIRE, new WeaponDeck(), new PowerUpDeck());
    }

    @Test
    public void verifyJsonMapReading(){
        Assert.assertTrue(true);
    }

    @Test
    public void see(){
        //same rooms
        Assert.assertTrue(gameMapTestFire.see(0,0,0,1));

        //north door
        Assert.assertTrue(gameMapTestFire.see(1,2,0,2));

        //east door
       Assert.assertTrue(gameMapTestFire.see(1,2,1,3));

        //south door
        Assert.assertTrue(gameMapTestFire.see(0,0,1,0));

       //west door
       Assert.assertTrue(gameMapTestFire.see(2,3,2,2));

       //null spot
        Assert.assertTrue(gameMapTestFire.map[2][0] == null);
        Assert.assertFalse(gameMapTestFire.see(2,0,2,1));


    }

    @Test
    public void getPlayerCoord(){
        Player playerTest = new Player("gino", 0, 0);

        gameMapTestFire.map[0][0].addPlayer("gino");

        int[] coordinates = new int[2];
        coordinates = gameMapTestFire.getPlayerSpotCoord("gino");
        Assert.assertEquals(0, coordinates[0]);
        Assert.assertEquals(0, coordinates[1]);

        Assert.assertEquals(null, gameMapTestFire.getPlayerSpotCoord("nogi"));

    }

    @Test
    public void movePlayer(){
        Player playerTest = new Player("gino", 0, 0);

        gameMapTestFire.map[0][0].addPlayer("gino");

        gameMapTestFire.movePlayer("gino",2,2);

        int[] coordinates = new int[2];
        coordinates = gameMapTestFire.getPlayerSpotCoord("gino");
        Assert.assertEquals(2, coordinates[0]);
        Assert.assertEquals(2, coordinates[1]);
    }



}