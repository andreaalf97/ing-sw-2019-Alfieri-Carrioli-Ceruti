package it.polimi.ingsw.model;

import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.map.MapName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class GameTest {

    Game gameTest;

    @Before
    public void setup() {
        ArrayList<String> playersNamesTest = new ArrayList<>();
        playersNamesTest.add(ShootingTest.playerGino);
        playersNamesTest.add(ShootingTest.playerAndreaalf);
        playersNamesTest.add(ShootingTest.playerMeme);

        gameTest = new Game(playersNamesTest, MapName.FIRE, 6);
    }

    @Test
    public void givePowerUp() {
        gameTest.givePowerUp(ShootingTest.playerGino);
        Assert.assertEquals(1, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().size());

        //todo test when weapondeck is empty
    }

    @Test
    public void checkDeaths() {
        gameTest.getPlayerByNickname(ShootingTest.playerMeme).revive();
        gameTest.getPlayerByNickname(ShootingTest.playerGino).revive();
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).revive();

        gameTest.getPlayerByNickname(ShootingTest.playerMeme).playerStatus.isFirstTurn = false;
        gameTest.getPlayerByNickname(ShootingTest.playerGino).playerStatus.isFirstTurn = false;
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).playerStatus.isFirstTurn = false;

        gameTest.getPlayerByNickname(ShootingTest.playerGino).giveDamage(ShootingTest.playerAndreaalf, 6);
        gameTest.getPlayerByNickname(ShootingTest.playerGino).giveDamage(ShootingTest.playerMeme, 5);

        gameTest.checkDeaths();

        Assert.assertTrue(gameTest.getPlayerByNickname(ShootingTest.playerGino).isDead());
        Assert.assertFalse(gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).isDead());
        Assert.assertFalse(gameTest.getPlayerByNickname(ShootingTest.playerMeme).isDead());
        Assert.assertEquals(9, gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getPoints());
        Assert.assertEquals(6, gameTest.getPlayerByNickname(ShootingTest.playerMeme).getPoints());
    }

    @Test
    public void giveBoardPoints() {
        //i have to do this because in the game at the start we consider the players dead, waiting for spawn
        gameTest.getPlayerByNickname(ShootingTest.playerMeme).revive();
        gameTest.getPlayerByNickname(ShootingTest.playerGino).revive();
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).revive();


        //check normal kill
        gameTest.getPlayerByNickname(ShootingTest.playerGino).giveDamage(ShootingTest.playerMeme, 11);
        gameTest.giveBoardPointsAndModifyKST(gameTest.getPlayerByNickname(ShootingTest.playerGino));
        Assert.assertTrue(gameTest.getPlayerByNickname(ShootingTest.playerGino).isDead());

        Assert.assertEquals(9, gameTest.getPlayerByNickname(ShootingTest.playerMeme).getPoints());

        //check overKill
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).giveDamage(ShootingTest.playerGino, 12);
        gameTest.giveBoardPointsAndModifyKST(gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf));
        Assert.assertTrue(gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).isDead());

        Assert.assertEquals(9, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPoints());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(ShootingTest.playerGino).getMarks().size());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getPoints());
        Assert.assertFalse(gameTest.getPlayerByNickname(ShootingTest.playerMeme).isDead());

        //check that if i have already kill gino he should give only 6 points
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).giveDamage(ShootingTest.playerGino, 12);
        gameTest.giveBoardPointsAndModifyKST(gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf));
        Assert.assertEquals(16, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPoints());

    }

    @Test
    public void giveFrenzyBoardPoints() {
        gameTest.getPlayerByNickname(ShootingTest.playerGino).giveDamage(ShootingTest.playerAndreaalf, 1);
        gameTest.getPlayerByNickname(ShootingTest.playerGino).giveDamage(ShootingTest.playerMeme, 1);

        gameTest.giveFrenzyBoardPoints(gameTest.getPlayerByNickname(ShootingTest.playerGino));
        Assert.assertEquals(2, gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getPoints());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(ShootingTest.playerMeme).getPoints());

    }

    @Test
    public void giveKSTPoints() {
        //todo dopo la kill multipla questo test  va aggiornato
        //all'inizio sono tutti morti
        gameTest.getPlayerByNickname(ShootingTest.playerGino).revive();
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).revive();
        gameTest.getPlayerByNickname(ShootingTest.playerMeme).revive();

        gameTest.getPlayerByNickname(ShootingTest.playerMeme).playerStatus.isFirstTurn = false;
        gameTest.getPlayerByNickname(ShootingTest.playerGino).playerStatus.isFirstTurn = false;
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).playerStatus.isFirstTurn = false;

        // 3 segnalini per meme
        gameTest.getPlayerByNickname(ShootingTest.playerGino).giveDamage(ShootingTest.playerMeme, 12);
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).giveDamage(ShootingTest.playerMeme, 11);
        gameTest.checkDeaths();

        gameTest.getPlayerByNickname(ShootingTest.playerGino).revive();
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).revive();

        Assert.assertEquals(18, gameTest.getPlayerByNickname(ShootingTest.playerMeme).getPoints());

        // 2 segnalini per andrealf
        gameTest.getPlayerByNickname(ShootingTest.playerGino).giveDamage(ShootingTest.playerAndreaalf, 11);
        gameTest.getPlayerByNickname(ShootingTest.playerMeme).giveDamage(ShootingTest.playerAndreaalf, 11);
        gameTest.checkDeaths();

        gameTest.getPlayerByNickname(ShootingTest.playerGino).revive();
        gameTest.getPlayerByNickname(ShootingTest.playerMeme).revive();

        Assert.assertEquals(16, gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getPoints());

        //4 segnalini per gino
        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).giveDamage(ShootingTest.playerGino, 12);
        gameTest.getPlayerByNickname(ShootingTest.playerMeme).giveDamage(ShootingTest.playerGino, 12);
        gameTest.checkDeaths();

        gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).revive();
        gameTest.getPlayerByNickname(ShootingTest.playerMeme).revive();

        Assert.assertEquals(14, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPoints());

        //chiamo metodo e controllo punti: gino 22, meme 24, andreaalf 20
        //chiamo metodo e controllo punti: gino 22, meme 24, andreaalf 20
        gameTest.giveKSTPoints();
        Assert.assertEquals(24, gameTest.getPlayerByNickname(ShootingTest.playerMeme).getPoints());
        Assert.assertEquals(20, gameTest.getPlayerByNickname(ShootingTest.playerAndreaalf).getPoints());
        Assert.assertEquals(22, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPoints());

        Assert.assertTrue(gameTest.noMoreSkullsOnKST());

    }

    @Test
    public void wherePlayerCanMove() {

        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);
        boolean[][] temp = gameTest.wherePlayerCanMove(ShootingTest.playerGino, 1);

        Assert.assertTrue(temp[0][0]);
        Assert.assertTrue(temp[0][1]);
        Assert.assertTrue(temp[1][0]);


        gameTest.movePlayer(ShootingTest.playerAndreaalf, 0, 0);
        boolean[][] temp2 = gameTest.wherePlayerCanMove(ShootingTest.playerAndreaalf, 10);


        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                if (gameTest.validSpot(i, j))
                    Assert.assertTrue(temp2[i][j]);

    }

    @Test
    public void wherePlayerCanMoveAndGrab() {

        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);
        boolean[][] temp = gameTest.wherePlayerCanMoveAndGrab(ShootingTest.playerGino, 1);

        Assert.assertTrue(temp[0][0]);
        Assert.assertTrue(temp[0][1]);


    }

    @Test
    public void respawn() {
        //this is the first spawn for a player, like the first turn
        PowerUp powerUpTestRed = new PowerUp(Color.RED);
        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTestRed);

        gameTest.respawn(ShootingTest.playerGino, 0);
        Assert.assertFalse(gameTest.getPlayerByNickname(ShootingTest.playerGino).isDead());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(ShootingTest.playerGino).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(ShootingTest.playerGino).getyPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().size());


        //another test we can do is when a player get killed and then has to respawn
        PowerUp powerUpTestBlue = new PowerUp(Color.BLUE);
        gameTest.getPlayerByNickname(ShootingTest.playerGino).givePowerUp(powerUpTestBlue);

        gameTest.getPlayerByNickname(ShootingTest.playerGino).giveDamage(ShootingTest.playerMeme, 11);
        Assert.assertTrue(gameTest.getPlayerByNickname(ShootingTest.playerGino).isDead());

        gameTest.respawn(ShootingTest.playerGino, 0);
        Assert.assertFalse(gameTest.getPlayerByNickname(ShootingTest.playerGino).isDead());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(ShootingTest.playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(ShootingTest.playerGino).getyPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(ShootingTest.playerGino).getPowerUpList().size());

    }

    @Test
    public void refillAllAmmoSpots() {
        //pick some ammos and then control if all spots are refilled
        gameTest.movePlayer(ShootingTest.playerGino, 1, 0);
        try {
            gameTest.giveAmmoCard(ShootingTest.playerGino);
        } catch (RuntimeException e) {
            Assert.assertTrue(true);
        }

        gameTest.movePlayer(ShootingTest.playerGino, 1, 1);
        try {
            gameTest.giveAmmoCard(ShootingTest.playerGino);
        } catch (RuntimeException e) {
            Assert.fail();
        }

        gameTest.movePlayer(ShootingTest.playerGino, 2, 1);
        try {
            gameTest.giveAmmoCard(ShootingTest.playerGino);
        } catch (RuntimeException e) {
            Assert.fail();
        }

        gameTest.refillAllAmmoSpots();

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                if (gameTest.validSpot(i, j) && gameTest.getSpotByIndex(i, j).isAmmoSpot())
                    Assert.assertTrue(gameTest.getSpotByIndex(i, j).isFull());


    }

    @Test
    public void refillAllSpawnSpots() {
        //pick three weapons and refill the spawn spots
        gameTest.movePlayer(ShootingTest.playerGino, 1, 0);
        gameTest.pickWeapon(ShootingTest.playerGino, 0);

        gameTest.movePlayer(ShootingTest.playerGino, 0, 2);
        gameTest.pickWeapon(ShootingTest.playerGino, 0);

        gameTest.movePlayer(ShootingTest.playerGino, 2, 3);
        gameTest.pickWeapon(ShootingTest.playerGino, 0);

        Assert.assertEquals(3, gameTest.getPlayerByNickname(ShootingTest.playerGino).getWeaponList().size());
        gameTest.refillAllSpawnSpots();

        Assert.assertTrue(gameTest.getSpotByIndex(1, 0).isFull());
        Assert.assertTrue(gameTest.getSpotByIndex(0, 2).isFull());
        Assert.assertTrue(gameTest.getSpotByIndex(2, 3).isFull());

    }

    @Test
    public void movePlayer() {
        gameTest.movePlayer(ShootingTest.playerGino, 2, 3);
        Assert.assertEquals(2, gameTest.getPlayerByNickname(ShootingTest.playerGino).getxPosition());
        Assert.assertEquals(3, gameTest.getPlayerByNickname(ShootingTest.playerGino).getyPosition());
        Assert.assertTrue(gameTest.getSpotByIndex(2, 3).getPlayersHere().contains(ShootingTest.playerGino));

    }

    @Test
    public void movePlayerAndGrab() {
        gameTest.moveAndGrab(ShootingTest.playerGino, 1, 0, 0);
        Assert.assertEquals(1, gameTest.getPlayerByNickname(ShootingTest.playerGino).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(ShootingTest.playerGino).getyPosition());
        Assert.assertTrue(gameTest.getSpotByIndex(1, 0).getPlayersHere().contains(ShootingTest.playerGino));
        Assert.assertEquals(1, gameTest.getPlayerByNickname(ShootingTest.playerGino).getWeaponList().size());

    }

    @Test
    public void checkRechargeableWeapons() {
        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");

        gameTest.giveWeaponToPlayer(ShootingTest.playerGino, w1);
        gameTest.giveWeaponToPlayer(ShootingTest.playerGino, w2);
        gameTest.giveWeaponToPlayer(ShootingTest.playerGino, w3);

        Assert.assertEquals(3, gameTest.getPlayerByNickname(ShootingTest.playerGino).getWeaponList().size());

        w1.unload();
        w2.unload();

        ArrayList<Weapon> weaponstest = gameTest.checkRechargeableWeapons(ShootingTest.playerGino);
        Assert.assertEquals(2, weaponstest.size());
        Assert.assertTrue(weaponstest.contains(w1));
        Assert.assertTrue(weaponstest.contains(w2));


    }

    @Test
    public void reloadWeapon() {
        Weapon w1 = new Weapon("a");

        gameTest.giveWeaponToPlayer(ShootingTest.playerGino, w1);

        w1.unload();

        gameTest.reloadWeapon(ShootingTest.playerGino, 0);

        Assert.assertTrue(gameTest.getPlayerByNickname(ShootingTest.playerGino).getWeaponList().get(0).isLoaded());


    }

    @Test
    public void getRealIndexOfWeapon() {
        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");


        gameTest.giveWeaponToPlayer(ShootingTest.playerGino, w1);
        gameTest.giveWeaponToPlayer(ShootingTest.playerGino, w2);
        gameTest.giveWeaponToPlayer(ShootingTest.playerGino, w3);

        Assert.assertEquals(1, gameTest.getRealWeaponIndexOfTheUnloadedWeapon(ShootingTest.playerGino, w2));


    }

    @Test
    public void pay() {
        ArrayList<Color> cost = new ArrayList<>();
        cost.add(Color.RED);
        cost.add(Color.BLUE);
        cost.add(Color.YELLOW);
        try {
            gameTest.pay(ShootingTest.playerGino, cost);
            Assert.assertEquals(0, gameTest.getPlayerByNickname(ShootingTest.playerGino).getnYellowAmmo() + gameTest.getPlayerByNickname(ShootingTest.playerGino).getnBlueAmmo() + gameTest.getPlayerByNickname(ShootingTest.playerGino).getnRedAmmo());
        } catch (InvalidChoiceException e) {
            Assert.fail();
        } catch (RuntimeException re) {
            Assert.fail();
        }

        ArrayList<Color> costException = new ArrayList<>();
        costException.add(Color.ANY);

        try {
            gameTest.pay(ShootingTest.playerGino, costException);
        } catch (InvalidChoiceException e) {
            Assert.fail();
        } catch (RuntimeException re) {
            Assert.assertTrue(true);
        }


    }

    @Test
    public void giveAmmoCard() {
        gameTest.movePlayer(ShootingTest.playerGino, 0, 0);

        try {
            gameTest.giveAmmoCard(ShootingTest.playerGino);
            Assert.assertTrue(gameTest.getPlayerByNickname(ShootingTest.playerGino).getnRedAmmo() + gameTest.getPlayerByNickname(ShootingTest.playerGino).getnBlueAmmo() + gameTest.getPlayerByNickname(ShootingTest.playerGino).getnYellowAmmo() > 3);
        } catch (RuntimeException e) {
            Assert.fail();
        }

        gameTest.movePlayer(ShootingTest.playerGino, 1, 0);

        try {
            gameTest.giveAmmoCard(ShootingTest.playerGino);
        } catch (RuntimeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void pickWeapon() {
        Assert.assertEquals(0, gameTest.getPlayerByNickname(ShootingTest.playerGino).getWeaponList().size());
        gameTest.movePlayer(ShootingTest.playerGino, 1 , 0);
        gameTest.pickWeapon(ShootingTest.playerGino, 0);
        Assert.assertEquals(1, gameTest.getPlayerByNickname(ShootingTest.playerGino).getWeaponList().size());
    }

    @Test
    public void getNextPlayer(){
        Assert.assertEquals(ShootingTest.playerAndreaalf, gameTest.getNextPlayer(ShootingTest.playerGino));
        Assert.assertEquals(ShootingTest.playerMeme, gameTest.getNextPlayer(ShootingTest.playerAndreaalf));
        Assert.assertEquals(ShootingTest.playerGino, gameTest.getNextPlayer(ShootingTest.playerMeme));
    }

    @Test
    public void verifyDeserialization(){
        String jsonModelSnapshot = gameTest.modelSnapshot();

        Game newGame = JsonDeserializer.deserializeModelSnapshot(jsonModelSnapshot);

        Assert.assertTrue(true);
    }

    @Test
    public void verifySerializationClientSnapshot(){
        String clientSnapshot = gameTest.clientSnapshot();

        Assert.assertTrue(true);
    }


}