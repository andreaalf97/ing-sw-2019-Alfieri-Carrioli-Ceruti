package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.PowerUp;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PlayerTest {

    @Test
    public void checkDamage() {
    }


    @Test
    public void getOffendersRankingOneOfEach() {
        Player playerTest = new Player("andreaalf");
        ArrayList<String> offenders = new ArrayList<>();

        offenders.add("player1");
        offenders.add("player2");
        offenders.add("player1");
        offenders.add("player2");
        offenders.add("player1");
        offenders.add("player1");

        playerTest.setDamages(offenders);

        ArrayList<String> results = new ArrayList<>();
        results.add("player1");
        results.add("player2");

        Assert.assertEquals(playerTest.getOffendersRanking(), results);

        offenders = new ArrayList<>();

        playerTest.setDamages(offenders);

        Assert.assertTrue(playerTest.getOffendersRanking().isEmpty());
    }

    @Test
    public void getOffendersRankingOnlyOnePlayer(){
        Player playerTest = new Player("andreaalf");
        ArrayList<String> offenders = new ArrayList<>();

        offenders.add("player1");
        offenders.add("player1");
        offenders.add("player1");

        playerTest.setDamages(offenders);

        ArrayList<String> results = new ArrayList<>();
        results.add("player1");

        Assert.assertEquals(playerTest.getOffendersRanking(), results);
    }

    @Test
    public void getOffendersRankingNoDamages(){
        Player playerTest = new Player("andreaalf");
        ArrayList<String> offenders = new ArrayList<>();

        playerTest.setDamages(offenders);

        Assert.assertTrue(playerTest.getOffendersRanking().isEmpty());
    }

    @Test
    public void giveDamageHalfFull() {
        Player testPlayer = new Player("andreaalf");

        testPlayer.giveDamage("player1", 2);
        testPlayer.giveDamage("player2", 2);
        testPlayer.giveDamage("player1", 1);

        ArrayList<String> results = new ArrayList<>();
        results.add("player1");
        results.add("player1");
        results.add("player2");
        results.add("player2");
        results.add("player1");

        Assert.assertEquals(testPlayer.getDamages(), results);
    }

    @Test
    public void giveDamageOneOffender(){
        Player testPlayer = new Player("andreaalf");

        testPlayer.giveDamage("player1", 2);

        ArrayList<String> results = new ArrayList<>();
        results.add("player1");
        results.add("player1");

        Assert.assertEquals(testPlayer.getDamages(), results);

    }

    @Test
    public void giveMarks() {
        Player testPlayer = new Player("andreaalf");

        testPlayer.giveMarks("player1", 1);
        testPlayer.giveMarks("player2", 1);
        testPlayer.giveMarks("player3", 1);
        testPlayer.giveMarks("player1", 2);

        ArrayList<String> results = new ArrayList<>();
        results.add("player1");
        results.add("player2");
        results.add("player3");
        results.add("player1");
        results.add("player1");

        Assert.assertEquals(testPlayer.getMarks(), results);
    }

    @Test
    public void givePointsRegular() {
        Player testPlayer = new Player("andreaalf");
        testPlayer.setPoints(5);

        testPlayer.givePoints(3);

        Assert.assertEquals(8, testPlayer.getPoints());
    }

    @Test
    public void givePointsFromZero() {
        Player testPlayer = new Player("andreaalf");
        testPlayer.setPoints(0);

        testPlayer.givePoints(3);

        Assert.assertEquals(3, testPlayer.getPoints());
    }

    @Test
    public void canGiveMarksNO() {
        Player testPlayer = new Player("andreaalf");

        ArrayList<String> markers = new ArrayList<>();
        markers.add("Player1");
        markers.add("Player1");
        markers.add("Player1");
        testPlayer.setMarks(markers);

        Assert.assertFalse(
                testPlayer.canGiveMarks("Player1", 2) //asserting that player1 CAN'T give marks to andreaalf
        );
    }

    @Test
    public void canGiveMarksYES() {
        Player testPlayer = new Player("andreaalf");

        ArrayList<String> markers = new ArrayList<>();
        markers.add("Player1");
        markers.add("Player1");
        testPlayer.setMarks(markers);

        Assert.assertTrue(
                testPlayer.canGiveMarks("Player1", 1) //asserting that player1 CAN'T give marks to andreaalf
        );
    }

    @Test
    public void canGiveMarksYESempty() {
        Player testPlayer = new Player("andreaalf");

        ArrayList<String> markers = new ArrayList<>();
        testPlayer.setMarks(markers);

        Assert.assertTrue(
                testPlayer.canGiveMarks("Player1", 1) //asserting that player1 CAN'T give marks to andreaalf
        );
    }

    @Test
    public void canGiveMarksNOempty() {
        Player testPlayer = new Player("andreaalf");

        ArrayList<String> markers = new ArrayList<>();
        testPlayer.setMarks(markers);

        Assert.assertFalse(
                testPlayer.canGiveMarks("Player1", 4) //asserting that player1 CAN'T give marks to andreaalf
        );
    }

    @Test
    public void isDead(){
        Player testPlayer = new Player("andreaalf");
        testPlayer.setIsDead(true);

        Assert.assertTrue(testPlayer.isDead());
    }

    @Test
    public void kill(){
        Player testPlayer = new Player("andreaalf");

        testPlayer.setIsDead(false);

        testPlayer.kill();

        Assert.assertTrue(testPlayer.isDead());
    }

    @Test
    public void killException(){
        Player testPlayer = new Player("andreaalf");

        try{
            testPlayer.kill();
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
            return;
        }

        Assert.fail();
    }

    @Test
    public void giveAmmosRegular(){
        Player testPlayer = new Player("andreaalf");

        testPlayer.setnBlueAmmo(0);
        testPlayer.setnRedAmmo(0);
        testPlayer.setnYellowAmmo(0);

        ArrayList<Color> ammos = new ArrayList<>();
        ammos.add(Color.BLUE);
        ammos.add(Color.RED);
        ammos.add(Color.YELLOW);

        testPlayer.giveAmmos(ammos);

        Assert.assertTrue(testPlayer.getnBlueAmmo() == 1 && testPlayer.getnRedAmmo() == 1 && testPlayer.getnYellowAmmo() == 1);
    }

    @Test
    public void giveAmmosUntilFull(){
        Player testPlayer = new Player("andreaalf");

        testPlayer.setnBlueAmmo(0);
        testPlayer.setnRedAmmo(0);
        testPlayer.setnYellowAmmo(0);

        ArrayList<Color> ammos = new ArrayList<>();
        ammos.add(Color.BLUE);
        ammos.add(Color.BLUE);
        ammos.add(Color.BLUE);

        testPlayer.giveAmmos(ammos);

        Assert.assertTrue(testPlayer.getnBlueAmmo() == 3 && testPlayer.getnRedAmmo() == 0 && testPlayer.getnYellowAmmo() == 0);
    }

    @Test
    public void giveAmmosOverflow(){
        Player testPlayer = new Player("andreaalf");

        testPlayer.setnBlueAmmo(2);
        testPlayer.setnRedAmmo(0);
        testPlayer.setnYellowAmmo(0);

        ArrayList<Color> ammos = new ArrayList<>();
        ammos.add(Color.BLUE);
        ammos.add(Color.BLUE);
        ammos.add(Color.BLUE);

        testPlayer.giveAmmos(ammos);

        Assert.assertTrue(testPlayer.getnBlueAmmo() == 3 && testPlayer.getnRedAmmo() == 0 && testPlayer.getnYellowAmmo() == 0);
    }

    @Test
    public void givePowerUpToEmptyWallet(){
        Player testPlayer = new Player("andreaalf");

        PowerUp testPowerUp = new PowerUp();

        testPlayer.givePowerUp(testPowerUp);
        testPlayer.givePowerUp(testPowerUp);

        ArrayList<PowerUp> assertList = new ArrayList<>();
        assertList.add(testPowerUp);
        assertList.add(testPowerUp);

        Assert.assertEquals(testPlayer.getPowerUpList(), assertList);

    }

    @Test
    public void givePowerUpToFullWallet(){
        Player testPlayer = new Player("andreaalf");

        PowerUp testPowerUp = new PowerUp();

        testPlayer.givePowerUp(testPowerUp);
        testPlayer.givePowerUp(testPowerUp);
        testPlayer.givePowerUp(testPowerUp);

        try {
            testPlayer.givePowerUp(testPowerUp);
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
            return;
        }

        Assert.fail();
    }

    @Test
    public void discardPowerUpByIndex(){
        Player testPlayer = new Player("andreaalf");

        PowerUp testPowerUpRed = new PowerUp(Color.RED);
        PowerUp testPowerUp = new PowerUp();


        testPlayer.givePowerUp(testPowerUp);
        testPlayer.givePowerUp(testPowerUpRed);
        testPlayer.givePowerUp(testPowerUp);

        Color returnColor = testPlayer.discardPowerUpByIndex(1);

        ArrayList<PowerUp> testList = new ArrayList<>();
        testList.add(testPowerUp);
        testList.add(testPowerUp);

        Assert.assertTrue(testPlayer.getPowerUpList().equals(testList) && returnColor == Color.RED);
    }

    @Test
    public void discardPowerUpByIndexException(){
        Player testPlayer = new Player("andreaalf");

        PowerUp testPowerUp = new PowerUp();

        testPlayer.givePowerUp(testPowerUp);
        testPlayer.givePowerUp(testPowerUp);

        try {
            testPlayer.discardPowerUpByIndex(2);
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
            return;
        }

        Assert.fail();
    }

    @Test
    public void reviveRegular(){
        Player testPlayer = new Player("andreaalf");

        testPlayer.setIsDead(true);

        testPlayer.revive();

        Assert.assertFalse(testPlayer.isDead());
    }

    @Test
    public void reviveException(){
        Player testPlayer = new Player("andreaalf");

        testPlayer.setIsDead(false);

        try {
            testPlayer.revive();
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
            return;
        }

        Assert.fail();
    }
}