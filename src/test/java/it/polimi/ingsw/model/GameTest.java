package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.map.MapName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.zip.CheckedOutputStream;

public class GameTest {

    Game gameTest;

    @Before
    public void setup() {
        ArrayList<String> playersNamesTest = new ArrayList<>();
        playersNamesTest.add("gino");
        playersNamesTest.add("andreaalf");
        playersNamesTest.add("meme");

        gameTest = new Game(playersNamesTest, MapName.FIRE, 6);
    }

    @Test
    public void givePowerUp() {
        gameTest.givePowerUp("gino");
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getPowerUpList().size());

        //todo test when weapondeck is empty
    }

    @Test
    public void checkDeaths() {
        gameTest.getPlayerByNickname("meme").revive();
        gameTest.getPlayerByNickname("gino").revive();
        gameTest.getPlayerByNickname("andreaalf").revive();

        gameTest.getPlayerByNickname("meme").playerStatus.isFirstTurn = false;
        gameTest.getPlayerByNickname("gino").playerStatus.isFirstTurn = false;
        gameTest.getPlayerByNickname("andreaalf").playerStatus.isFirstTurn = false;

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
    public void giveBoardPoints() {
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

        //check that if i have already kill gino he should give only 6 points
        gameTest.getPlayerByNickname("andreaalf").giveDamage("gino", 12);
        gameTest.giveBoardPointsAndModifyKST(gameTest.getPlayerByNickname("andreaalf"));
        Assert.assertEquals(16, gameTest.getPlayerByNickname("gino").getPoints());

    }

    @Test
    public void giveFrenzyBoardPoints() {
        gameTest.getPlayerByNickname("gino").giveDamage("andreaalf", 1);
        gameTest.getPlayerByNickname("gino").giveDamage("meme", 1);

        gameTest.giveFrenzyBoardPoints(gameTest.getPlayerByNickname("gino"));
        Assert.assertEquals(2, gameTest.getPlayerByNickname("andreaalf").getPoints());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getPoints());

    }

    @Test
    public void giveKSTPoints() {
        //todo dopo la kill multipla questo test  va aggiornato
        //all'inizio sono tutti morti
        gameTest.getPlayerByNickname("gino").revive();
        gameTest.getPlayerByNickname("andreaalf").revive();
        gameTest.getPlayerByNickname("meme").revive();

        gameTest.getPlayerByNickname("meme").playerStatus.isFirstTurn = false;
        gameTest.getPlayerByNickname("gino").playerStatus.isFirstTurn = false;
        gameTest.getPlayerByNickname("andreaalf").playerStatus.isFirstTurn = false;

        // 3 segnalini per meme
        gameTest.getPlayerByNickname("gino").giveDamage("meme", 12);
        gameTest.getPlayerByNickname("andreaalf").giveDamage("meme", 11);
        gameTest.checkDeaths();

        gameTest.getPlayerByNickname("gino").revive();
        gameTest.getPlayerByNickname("andreaalf").revive();

        Assert.assertEquals(18, gameTest.getPlayerByNickname("meme").getPoints());

        // 2 segnalini per andrealf
        gameTest.getPlayerByNickname("gino").giveDamage("andreaalf", 11);
        gameTest.getPlayerByNickname("meme").giveDamage("andreaalf", 11);
        gameTest.checkDeaths();

        gameTest.getPlayerByNickname("gino").revive();
        gameTest.getPlayerByNickname("meme").revive();

        Assert.assertEquals(16, gameTest.getPlayerByNickname("andreaalf").getPoints());

        //4 segnalini per gino
        gameTest.getPlayerByNickname("andreaalf").giveDamage("gino", 12);
        gameTest.getPlayerByNickname("meme").giveDamage("gino", 12);
        gameTest.checkDeaths();

        gameTest.getPlayerByNickname("andreaalf").revive();
        gameTest.getPlayerByNickname("meme").revive();

        Assert.assertEquals(14, gameTest.getPlayerByNickname("gino").getPoints());

        //chiamo metodo e controllo punti: gino 22, meme 24, andreaalf 20
        //chiamo metodo e controllo punti: gino 22, meme 24, andreaalf 20
        gameTest.giveKSTPoints();
        Assert.assertEquals(24, gameTest.getPlayerByNickname("meme").getPoints());
        Assert.assertEquals(20, gameTest.getPlayerByNickname("andreaalf").getPoints());
        Assert.assertEquals(22, gameTest.getPlayerByNickname("gino").getPoints());

        Assert.assertTrue(gameTest.noMoreSkullsOnKST());

    }

    @Test
    public void wherePlayerCanMove() {

        gameTest.movePlayer("gino", 0, 0);
        boolean[][] temp = gameTest.wherePlayerCanMove("gino", 1);

        Assert.assertTrue(temp[0][0]);
        Assert.assertTrue(temp[0][1]);
        Assert.assertTrue(temp[1][0]);


        gameTest.movePlayer("andreaalf", 0, 0);
        boolean[][] temp2 = gameTest.wherePlayerCanMove("andreaalf", 10);


        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                if (gameTest.validSpot(i, j))
                    Assert.assertTrue(temp2[i][j]);

    }

    @Test
    public void wherePlayerCanMoveAndGrab() {

        gameTest.movePlayer("gino", 0, 0);
        boolean[][] temp = gameTest.wherePlayerCanMoveAndGrab("gino", 1);

        Assert.assertTrue(temp[0][0]);
        Assert.assertTrue(temp[0][1]);


    }

    @Test
    public void respawn() {
        //this is the first spawn for a player, like the first turn
        PowerUp powerUpTestRed = new PowerUp(Color.RED);
        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTestRed);

        gameTest.respawn("gino", 0);
        Assert.assertFalse(gameTest.getPlayerByNickname("gino").isDead());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getyPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getPowerUpList().size());


        //another test we can do is when a player get killed and then has to respawn
        PowerUp powerUpTestBlue = new PowerUp(Color.BLUE);
        gameTest.getPlayerByNickname("gino").givePowerUp(powerUpTestBlue);

        gameTest.getPlayerByNickname("gino").giveDamage("meme", 11);
        Assert.assertTrue(gameTest.getPlayerByNickname("gino").isDead());

        gameTest.respawn("gino", 0);
        Assert.assertFalse(gameTest.getPlayerByNickname("gino").isDead());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getPowerUpList().size());

    }

    @Test
    public void refillAllAmmoSpots() {
        //pick some ammos and then control if all spots are refilled
        gameTest.movePlayer("gino", 1, 0);
        try {
            gameTest.giveAmmoCard("gino");
        } catch (RuntimeException e) {
            Assert.assertTrue(true);
        }

        gameTest.movePlayer("gino", 1, 1);
        try {
            gameTest.giveAmmoCard("gino");
        } catch (RuntimeException e) {
            Assert.fail();
        }

        gameTest.movePlayer("gino", 2, 1);
        try {
            gameTest.giveAmmoCard("gino");
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
        gameTest.movePlayer("gino", 1, 0);
        gameTest.pickWeapon("gino", 0);

        gameTest.movePlayer("gino", 0, 2);
        gameTest.pickWeapon("gino", 0);

        gameTest.movePlayer("gino", 2, 3);
        gameTest.pickWeapon("gino", 0);

        Assert.assertEquals(3, gameTest.getPlayerByNickname("gino").getWeaponList().size());
        gameTest.refillAllSpawnSpots();

        Assert.assertTrue(gameTest.getSpotByIndex(1, 0).isFull());
        Assert.assertTrue(gameTest.getSpotByIndex(0, 2).isFull());
        Assert.assertTrue(gameTest.getSpotByIndex(2, 3).isFull());

    }

    @Test
    public void movePlayer() {
        gameTest.movePlayer("gino", 2, 3);
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(3, gameTest.getPlayerByNickname("gino").getyPosition());
        Assert.assertTrue(gameTest.getSpotByIndex(2, 3).getPlayersHere().contains("gino"));

    }

    @Test
    public void movePlayerAndGrab() {
        gameTest.moveAndGrab("gino", 1, 0, 0);
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getyPosition());
        Assert.assertTrue(gameTest.getSpotByIndex(1, 0).getPlayersHere().contains("gino"));
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getWeaponList().size());

    }

    @Test
    public void checkRechargeableWeapons() {
        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");

        gameTest.giveWeaponToPlayer("gino", w1);
        gameTest.giveWeaponToPlayer("gino", w2);
        gameTest.giveWeaponToPlayer("gino", w3);

        Assert.assertEquals(3, gameTest.getPlayerByNickname("gino").getWeaponList().size());

        w1.unload();
        w2.unload();

        ArrayList<Weapon> weaponstest = gameTest.checkRechargeableWeapons("gino");
        Assert.assertEquals(2, weaponstest.size());
        Assert.assertTrue(weaponstest.contains(w1));
        Assert.assertTrue(weaponstest.contains(w2));


    }

    @Test
    public void reloadWeapon() {
        Weapon w1 = new Weapon("a");

        gameTest.giveWeaponToPlayer("gino", w1);

        w1.unload();

        gameTest.reloadWeapon("gino", 0);

        Assert.assertTrue(gameTest.getPlayerByNickname("gino").getWeaponList().get(0).isLoaded());


    }

    @Test
    public void getRealIndexOfWeapon() {
        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");


        gameTest.giveWeaponToPlayer("gino", w1);
        gameTest.giveWeaponToPlayer("gino", w2);
        gameTest.giveWeaponToPlayer("gino", w3);

        Assert.assertEquals(1, gameTest.getRealWeaponIndexOfTheUnloadedWeapon("gino", w2));


    }

    @Test
    public void pay() {
        ArrayList<Color> cost = new ArrayList<>();
        cost.add(Color.RED);
        cost.add(Color.BLUE);
        cost.add(Color.YELLOW);
        try {
            gameTest.pay("gino", cost);
            Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getnYellowAmmo() + gameTest.getPlayerByNickname("gino").getnBlueAmmo() + gameTest.getPlayerByNickname("gino").getnRedAmmo());
        } catch (InvalidChoiceException e) {
            Assert.fail();
        } catch (RuntimeException re) {
            Assert.fail();
        }

        ArrayList<Color> costException = new ArrayList<>();
        costException.add(Color.ANY);

        try {
            gameTest.pay("gino", costException);
        } catch (InvalidChoiceException e) {
            Assert.fail();
        } catch (RuntimeException re) {
            Assert.assertTrue(true);
        }


    }

    @Test
    public void giveAmmoCard() {
        gameTest.movePlayer("gino", 0, 0);

        try {
            gameTest.giveAmmoCard("gino");
            Assert.assertTrue(gameTest.getPlayerByNickname("gino").getnRedAmmo() + gameTest.getPlayerByNickname("gino").getnBlueAmmo() + gameTest.getPlayerByNickname("gino").getnYellowAmmo() > 3);
        } catch (RuntimeException e) {
            Assert.fail();
        }

        gameTest.movePlayer("gino", 1, 0);

        try {
            gameTest.giveAmmoCard("gino");
        } catch (RuntimeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void pickWeapon() {
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getWeaponList().size());
        gameTest.movePlayer("gino", 1 , 0);
        gameTest.pickWeapon("gino", 0);
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getWeaponList().size());
    }

    @Test
    public void getNextPlayer(){
        Assert.assertEquals("andreaalf", gameTest.getNextPlayer("gino"));
        Assert.assertEquals("meme", gameTest.getNextPlayer("andreaalf"));
        Assert.assertEquals("gino", gameTest.getNextPlayer("meme"));
    }

    @Test
    public void verifyDeserialization(){
        String jsonModelSnapshot = gameTest.modelSnapshot();

        Game newGame = new Game(jsonModelSnapshot);

        Assert.assertTrue(true);
    }

    //FIXME after changing method
    /*
    @Test
    public void generatePossiblePaymentChoice(){
        ArrayList<Color> costTest = new ArrayList<>();
        costTest.add(Color.BLUE);
        costTest.add(Color.BLUE);
        costTest.add(Color.BLUE);

        PowerUp p1Test = new PowerUp("Teleporter", Color.BLUE);
        PowerUp p2Test = new PowerUp("TagbackGrenade", Color.BLUE);

        gameTest.getPlayerByNickname("gino").givePowerUp(p1Test);
        gameTest.getPlayerByNickname("gino").givePowerUp(p2Test);

        ArrayList<String> payment = gameTest.generatePaymentChoice(gameTest.getPlayerByNickname("gino"), costTest);


        //simple check with only one choice
        Assert.assertTrue(payment.contains("BLUE::Teleporter:BLUE::TagbackGrenade:BLUE::"));


        costTest = new ArrayList<>();
        costTest.add(Color.BLUE);
        costTest.add(Color.BLUE);

        payment = gameTest.generatePaymentChoice(gameTest.getPlayerByNickname("gino"), costTest);

        System.out.println("SHOULD PAY 2 BLUE");
        for (String s : payment){
            System.out.println(s);
        }
        //TODO DON'T WORK "BLUE::TagbackGrenade:BLUE::"

        costTest = new ArrayList<>();
        costTest.add(Color.BLUE);
        payment = gameTest.generatePaymentChoice(gameTest.getPlayerByNickname("gino"), costTest);

        System.out.println("SHOULD PAY 1 BLUE");
        for (String s : payment){
            System.out.println(s);
        }
        //TODO DON'T WORK "TagbackGrenade:BLUE::"

        costTest.add(Color.YELLOW);

        ArrayList<Color> ammoTest = new ArrayList<>();
        ammoTest.add(Color.YELLOW);

        gameTest.getPlayerByNickname("gino").giveAmmos(ammoTest);
        gameTest.getPlayerByNickname("gino").givePowerUp(new PowerUp("Teleporter", Color.YELLOW));

        payment = gameTest.generatePaymentChoice(gameTest.getPlayerByNickname("gino"), costTest);
        System.out.println("SHOULD PAY 1 BLUE AND 1 YELLOW");
        for (String s : payment){
            System.out.println(s);
        }
        //todo don't work choices with TagbackGrenade
    }
    */

    @Test
    public void verifySerializationClientSnapshot(){
        String clientSnapshot = gameTest.clientSnapshot();

        Assert.assertTrue(true);
    }


}