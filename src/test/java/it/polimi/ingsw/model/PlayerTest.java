package it.polimi.ingsw.model;

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
    public void canAttack() {
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
}