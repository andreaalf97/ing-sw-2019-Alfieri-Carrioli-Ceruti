package it.polimi.ingsw.model.map;

import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.ShootingTest;
import it.polimi.ingsw.model.cards.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class GameMapTest {

    GameMap gameMapTestFire;

    @Before
    public void setUp() throws Exception {
        gameMapTestFire = JsonDeserializer.deserializeGameMap(MapName.FIRE, JsonDeserializer.deserializeWeaponDeck(), JsonDeserializer.deserializePowerUpDeck(), JsonDeserializer.deserializeAmmoCardDeck());
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
        Player playerTest = new Player(ShootingTest.playerGino, 0, 0);

        gameMapTestFire.map[0][0].addPlayer(ShootingTest.playerGino);

        int[] coordinates = new int[2];
        coordinates = gameMapTestFire.getPlayerSpotCoord(ShootingTest.playerGino);
        Assert.assertEquals(0, coordinates[0]);
        Assert.assertEquals(0, coordinates[1]);

        Assert.assertNull(gameMapTestFire.getPlayerSpotCoord("nogi"));
    }

    @Test
    public void movePlayer(){
        Player playerTest = new Player(ShootingTest.playerGino, 0, 0);

        gameMapTestFire.map[0][0].addPlayer(ShootingTest.playerGino);

        gameMapTestFire.movePlayer(ShootingTest.playerGino,2,2);

        int[] coordinates = new int[2];
        coordinates = gameMapTestFire.getPlayerSpotCoord(ShootingTest.playerGino);
        Assert.assertEquals(2, coordinates[0]);
        Assert.assertEquals(2, coordinates[1]);

        try
        {
            gameMapTestFire.movePlayer(ShootingTest.playerGino, -1 , 3);
        }
        catch (IllegalArgumentException e){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void grabSomething(){
        Player player = new Player("gino, 0, 0");

        gameMapTestFire.map[0][0].addPlayer(ShootingTest.playerGino);
        boolean hasPowerUp = false;
        String imagePath = "";
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);

        AmmoCard ammoCardDrawn = new AmmoCard(imagePath, colors, hasPowerUp);
        gameMapTestFire.refill(0,0, ammoCardDrawn);
        gameMapTestFire.grabSomething(0, 0, player, -1);

        //check player gino grab only ammo
        Assert.assertTrue(player.getnRedAmmo() + player.getnYellowAmmo() + player.getnBlueAmmo() > 3);

        gameMapTestFire.map[0][0].setPowerUp(new PowerUp());
        gameMapTestFire.grabSomething(0,0, player,-1);

        //check player has grab the powerup
        Assert.assertTrue(player.getPowerUpList().size() != 0);

        gameMapTestFire.map[0][0].setPowerUp(new PowerUp());

        //check runtime exception index != -1 for ammospot
        try {
            gameMapTestFire.grabSomething(0, 0, player, 0);
        }
        catch(RuntimeException e){
            Assert.assertTrue(true);
        }

        gameMapTestFire.movePlayer(ShootingTest.playerGino, 1,0);
        gameMapTestFire.grabSomething(1, 0, player, 0);

        //check player has taken the weapon
        Assert.assertTrue(player.getWeaponList().size() != 0);



    }

    @Test
    public void wherePlayerCanMove(){
        Player playerTest = new Player(ShootingTest.playerGino, 0, 0);

        gameMapTestFire.map[0][0].addPlayer(ShootingTest.playerGino);

        boolean[][] tempMap = gameMapTestFire.wherePlayerCanMove(ShootingTest.playerGino, 2);

        Assert.assertTrue(tempMap[0][0]);
        Assert.assertTrue(tempMap[0][1]);
        Assert.assertTrue(tempMap[1][0]);
        Assert.assertTrue(tempMap[1][1]);
        Assert.assertTrue(tempMap[0][2]);

        boolean[][] tempMap2 = gameMapTestFire.wherePlayerCanMove(ShootingTest.playerGino, 10);

        //check all true
        for (int i = 0; i < tempMap2.length ; i++)
            for (int j = 0; j < tempMap2[i].length; j++)
                if (gameMapTestFire.map[i][j] != null) {
                    Assert.assertTrue(tempMap2[i][j]);
                }

        try{
            boolean[][] tempMapException = gameMapTestFire.wherePlayerCanMove("nogi", 2);
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
        }

    }

    @Test
    public void canMoveFromTo(){
        //north 1 move
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(1,0,0,0, 1));
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(2,3,1,3, 1));

        //east 1 move
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(1,2,1,3, 1));
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(1,1,1,2, 1));

        //south 1 move
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(0,2,1,2, 1));
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(1,3,2,3, 1));

        //west 1 move
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(2,3,2,2, 1));
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(1,1,1,0, 1));

        //spot don't recheable with one move but with 3 moves cause there is a wall
        Assert.assertFalse(gameMapTestFire.canMoveFromTo(1,1,0,1, 1));
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(1,1,0,1, 3));

        //another casual
        Assert.assertFalse(gameMapTestFire.canMoveFromTo(0,0,2,3, 2));
        Assert.assertTrue(gameMapTestFire.canMoveFromTo(0,0,2,3, 5));

        try{
            gameMapTestFire.canMoveFromTo(2,0, 2,1, 1);
        }
        catch(RuntimeException e){
            Assert.assertTrue(true);
        }

        try{
            gameMapTestFire.canMoveFromTo(-1,0, 2,1, 1);
        }
        catch(RuntimeException e){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void wherePlayerCanMoveAndGrab(){

        gameMapTestFire.map[0][0].addPlayer(ShootingTest.playerGino);
        boolean[][] tempMap = gameMapTestFire.wherePlayerCanMoveAndGrab(ShootingTest.playerGino, 2);

        //normal check
        Assert.assertTrue(tempMap[0][0]);
        Assert.assertTrue(tempMap[0][1]);
        Assert.assertTrue(tempMap[1][1]);

        gameMapTestFire.map[2][3].addPlayer("nogi");
        boolean[][] tempMap2 = gameMapTestFire.wherePlayerCanMoveAndGrab("nogi", 10);

        //check all true
        for (int i = 0; i < tempMap2.length ; i++)
            for (int j = 0; j < tempMap2[i].length; j++)
                if (gameMapTestFire.map[i][j] != null && gameMapTestFire.map[i][j].isAmmoSpot()) {
                    Assert.assertTrue(tempMap2[i][j]);
                }

        //check exception throw
        try{
            gameMapTestFire.wherePlayerCanMoveAndGrab("ginoginogino", 3);
        }
        catch(RuntimeException e){
            Assert.assertTrue(true);
        }

    }

    @Test
    public void respawnPlayer(){
        //ROOM RUBY
        gameMapTestFire.movePlayerToColorSpawn(ShootingTest.playerGino, Color.RED);
        Assert.assertTrue(gameMapTestFire.map[1][0].getPlayersHere().contains(ShootingTest.playerGino));

        //ROOM SAPPHIRE
        gameMapTestFire.movePlayerToColorSpawn(ShootingTest.playerGino, Color.BLUE);
        Assert.assertTrue(gameMapTestFire.map[0][2].getPlayersHere().contains(ShootingTest.playerGino));
    }

    @Test
    public void refill(){
        Player playerTest = new Player(ShootingTest.playerGino, 0 , 0);
        gameMapTestFire.grabSomething(0,0, playerTest, -1);

        boolean hasPowerUp = false;
        String imagePath = "";
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);

        AmmoCard ammoCardDrawn = new AmmoCard(imagePath, colors, hasPowerUp);
        gameMapTestFire.refill(0,0, ammoCardDrawn);
        Assert.assertTrue(gameMapTestFire.map[0][0].isFull());

        gameMapTestFire.grabSomething(1, 0, playerTest, 0);
        Assert.assertEquals(2, gameMapTestFire.map[1][0].getSpawnWeapons().size());

        Weapon w = new Weapon("a");
        gameMapTestFire.refill(1, 0, w);
        Assert.assertEquals(3, gameMapTestFire.map[1][0].getSpawnWeapons().size());

    }

    @Test
    public void refillAllAmmo(){
        Player playerTest = new Player (ShootingTest.playerGino, 0 ,0);

        gameMapTestFire.grabSomething(0,0, playerTest, -1);
        gameMapTestFire.grabSomething(0,1, playerTest, -1);
        gameMapTestFire.grabSomething(2,1, playerTest, -1);

        PowerUpDeck powerUpDeckTest = JsonDeserializer.deserializePowerUpDeck();
        AmmoCardDeck ammoCardDeckTest = JsonDeserializer.deserializeAmmoCardDeck();
        gameMapTestFire.refillAllAmmo(powerUpDeckTest,ammoCardDeckTest);
        for (int i = 0; i < gameMapTestFire.map.length ; i++)
            for (int j = 0; j < gameMapTestFire.map[i].length; j++)
                if (gameMapTestFire.map[i][j]  != null && gameMapTestFire.map[i][j].isAmmoSpot())
                    Assert.assertTrue(gameMapTestFire.map[i][j].isFull());

    }

    @Test
    public void refillAllSpawns(){
        Player playerTest = new Player(ShootingTest.playerGino, 1, 0);

        gameMapTestFire.grabSomething(1, 0 , playerTest, 0);
        gameMapTestFire.grabSomething(0, 2 , playerTest, 0);
        gameMapTestFire.grabSomething(2, 3 , playerTest, 0);

        Assert.assertEquals(3, playerTest.getWeaponList().size());

        WeaponDeck weaponDeckTest = JsonDeserializer.deserializeWeaponDeck();
        gameMapTestFire.refillAllSpawns(weaponDeckTest);
        for (int i = 0; i < gameMapTestFire.map.length ; i++)
            for (int j = 0; j < gameMapTestFire.map[i].length; j++)
                if (gameMapTestFire.map[i][j]  != null && gameMapTestFire.map[i][j].isSpawnSpot())
                    Assert.assertTrue(gameMapTestFire.map[i][j].isFull());

    }

    @Test
    public void showSpawnWeapons(){
        ArrayList<Weapon> weaponListTest = gameMapTestFire.showSpawnSpotWeapons(Color.RED);

        Assert.assertEquals(3, weaponListTest.size());
    }

    @Test
    public void getPlayerRoom(){
        Player playerTest = new Player(ShootingTest.playerGino, 0 , 0);

        gameMapTestFire.map[0][0].addPlayer(ShootingTest.playerGino);
        Assert.assertEquals(Room.SAPPHIRE, gameMapTestFire.getPlayerRoom(ShootingTest.playerGino));

        Assert.assertEquals(null, gameMapTestFire.getPlayerRoom("ingg"));
    }

    @Test
    public void distance(){
        Assert.assertEquals(0, gameMapTestFire.distance(0, 0, 0, 0));
        Assert.assertEquals(0, gameMapTestFire.distance(2, 1, 2, 1));

        Assert.assertEquals(2, gameMapTestFire.distance(0, 0, 1, 1));
        Assert.assertEquals(2, gameMapTestFire.distance(1, 1, 0, 0));

        Assert.assertEquals(3, gameMapTestFire.distance(0, 0, 1, 2));
        Assert.assertEquals(3, gameMapTestFire.distance(1, 2, 0, 0));

        Assert.assertEquals(3, gameMapTestFire.distance(0, 0, 2, 1));
        Assert.assertEquals(3, gameMapTestFire.distance(2, 1, 0, 0));

        Assert.assertEquals(2, gameMapTestFire.distance(0, 1, 1, 2));
        Assert.assertEquals(2, gameMapTestFire.distance(1, 2, 0, 1));

        Assert.assertEquals(5, gameMapTestFire.distance(0, 0, 2, 3));
        Assert.assertEquals(5, gameMapTestFire.distance(2, 3, 0, 0));

        Assert.assertEquals(4, gameMapTestFire.distance(2, 1, 0, 1));
        Assert.assertEquals(4, gameMapTestFire.distance(0, 1, 2, 1));

    }
}