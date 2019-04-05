package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void checkDamage() {
    }


    @Test
    void getOffendersRanking() {
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

        assertTrue(playerTest.getOffendersRanking().equals(results));

        offenders = new ArrayList<String>();
        results = new ArrayList<String>();

        //I'm now testing when only one player gives damages
        offenders.add("player1");
        offenders.add("player1");
        offenders.add("player1");

        playerTest.setDamages(offenders);

        results.add("player1");

        assertTrue(playerTest.getOffendersRanking().equals(results));

        offenders = new ArrayList<String>();

        playerTest.setDamages(offenders);

        assertTrue(playerTest.getOffendersRanking().isEmpty());
    }

    @Test
    void canAttack() {
    }

    @Test
    void giveDamage() {
        Player testPlayer = new Player("andreaalf");

        testPlayer.giveDamage("player1", 2);
        testPlayer.giveDamage("player2", 2);
        testPlayer.giveDamage("player1", 1);

        ArrayList<String> results = new ArrayList<String>();
        results.add("player1");
        results.add("player1");
        results.add("player2");
        results.add("player2");
        results.add("player1");

        assertTrue(testPlayer.getDamages().equals(results));

        testPlayer.setDamages(new ArrayList<String>());
        testPlayer.giveDamage("player1", 2);

        results = new ArrayList<String>();
        results.add("player1");
        results.add("player1");

        assertTrue(testPlayer.getDamages().equals(results));

    }

    @Test
    void giveMarks() {
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

        assertTrue(testPlayer.getMarks().equals(results));
    }

    @Test
    void givePoints() {
    }
}