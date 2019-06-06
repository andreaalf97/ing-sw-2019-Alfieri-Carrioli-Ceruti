package it.polimi.ingsw.model;

import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
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

        Assert.assertFalse(testPlayer.isDead());
        Assert.assertEquals(testPlayer.getDamages(), results);
    }

    @Test
    public void giveDamageOneOffender(){
        Player testPlayer = new Player("andreaalf");

        testPlayer.giveDamage("player1", 2);

        ArrayList<String> results = new ArrayList<>();
        results.add("player1");
        results.add("player1");

        Assert.assertFalse(testPlayer.isDead());
        Assert.assertEquals(testPlayer.getDamages(), results);

    }

    @Test
    public void giveDamageFull(){
        Player playerTest = new Player("gino");
        playerTest.giveDamage("tammazzo", 11);
        Assert.assertTrue(playerTest.isDead());


        Player playerTest2 = new Player("andrealf");
        playerTest2.giveDamage("nontammazzo", 2);
        Assert.assertFalse(playerTest2.isDead());

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

        try{
            testPlayer.giveMarks("andreaalf", 13);
        }
        catch(RuntimeException e){
            Assert.assertTrue(true);
        }
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

        try{
            testPlayer.givePoints(-2);
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
        }
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

        try{
            testPlayer.kill();
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
        }
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
    public void giveWeapon() {
        Player playerTest = new Player("gino", 0,0);
        playerTest.giveWeapon(new Weapon("a"));

        Assert.assertEquals(1,playerTest.getWeaponList().size());

        playerTest.giveWeapon(new Weapon("b"));
        playerTest.giveWeapon(new Weapon("c"));

        try{
            playerTest.giveWeapon(new Weapon("d"));
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);

        }
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
    public void giveAmmosException(){
        Player testPlayer = new Player("gino");

        testPlayer.setnBlueAmmo(2);
        testPlayer.setnRedAmmo(0);
        testPlayer.setnYellowAmmo(0);

        ArrayList<Color> ammos = new ArrayList<>();
        ammos.add(Color.BLUE);
        ammos.add(Color.BLUE);
        try {
            ammos.add(Color.ANY);
            testPlayer.giveAmmos(ammos);
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
        }

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

    @Test
    public void reloadWeapon(){
        Player playerTest = new Player("gino");

        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("LockRifle");

        weaponTest.unload();

        playerTest.giveWeapon(weaponTest);

        playerTest.reloadWeapon(0);

        Assert.assertTrue(playerTest.getWeaponList().get(0).isLoaded());
    }

    @Test
    public void moveTo(){
        Player playerTest = new Player("gino", 1,2);

        playerTest.moveTo(2,2);
        Assert.assertEquals(2, playerTest.getxPosition());
        Assert.assertEquals(2, playerTest.getyPosition());

        try {
            playerTest.moveTo(4,1);
        }
        catch (IllegalArgumentException e){
            Assert.assertTrue(true);
        }

        try {
            playerTest.moveTo(1,4);
        }
        catch (IllegalArgumentException e){
            Assert.assertTrue(true);
        }

    }

    @Test
    public void removeAmmo(){
        Player playerTest = new Player("gino", 0,0);

        ArrayList<Color> ammosTest = new ArrayList<>();
        ammosTest.add(Color.RED);
        ammosTest.add(Color.RED);
        playerTest.giveAmmos(ammosTest);

        playerTest.removeAmmo(Color.RED);
        Assert.assertEquals(2, playerTest.getnRedAmmo());

        playerTest.removeAmmo(Color.BLUE);
        playerTest.removeAmmo(Color.YELLOW);
        Assert.assertEquals(0, playerTest.getnYellowAmmo());
        Assert.assertEquals(0, playerTest.getnBlueAmmo());


        try{
            playerTest.removeAmmo(Color.ANY);
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void removeWeaponByIndex(){
        Player playerTest = new Player("gino", 0,0);

        Weapon weaponTest = new Weapon("ginogino");

        playerTest.giveWeapon(weaponTest);
        playerTest.giveWeapon(new Weapon("a"));
        playerTest.giveWeapon(new Weapon("b"));

        Weapon weaponToRemove = playerTest.removeWeaponByIndex(0);

        Assert.assertEquals(weaponTest, weaponToRemove);

    }
}