package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class KillShotTrackTest {

    @Test
    public void addKillTrue() {
        KillShotTrack kstTest = new KillShotTrack(5);
        kstTest.addKill(ShootingTest.playerGino, true);
        kstTest.addKill(ShootingTest.playerGino, true);
        kstTest.addKill(ShootingTest.playerGino, true);
        kstTest.addKill(ShootingTest.playerGino, true);
        kstTest.addKill(ShootingTest.playerGino, true);

        Assert.assertFalse(kstTest.getSkullList().contains("SKULL"));
        Assert.assertFalse(kstTest.getIsOverkill().contains(false));

        kstTest.addKill("ingsw", false);
        Assert.assertFalse(kstTest.getSkullList().contains("ingsw"));
        Assert.assertEquals(5 , kstTest.getSkullList().size());
        Assert.assertEquals(5, kstTest.getIsOverkill().size());

    }

    @Test
    public void getRanking() {
        KillShotTrack kstTest = new KillShotTrack(6);
        kstTest.addKill(ShootingTest.playerGino, false);
        kstTest.addKill(ShootingTest.playerGino, true);
        kstTest.addKill(ShootingTest.playerGino, true);
        kstTest.addKill("nogi", true);
        kstTest.addKill("nogi", false);
        kstTest.addKill("nogi", false); //this cover all the first if block inside the first for block, the while block is always covered

        ArrayList<String> ranking = new ArrayList<>(kstTest.getRanking());

        Assert.assertEquals(ShootingTest.playerGino, ranking.get(0));
        Assert.assertEquals("nogi", ranking.get(1));
        Assert.assertEquals(2,ranking.size());

        KillShotTrack kstTest2 = new KillShotTrack(6);
        ArrayList<String> ranking2 = new ArrayList<>(kstTest2.getRanking());

        Assert.assertEquals(0, ranking2.size());


        KillShotTrack kstTest3 = new KillShotTrack(6);
        kstTest3.addKill(ShootingTest.playerMeme, true);
        kstTest3.addKill(ShootingTest.playerMeme, false);
        kstTest3.addKill(ShootingTest.playerAndreaalf, false);
        kstTest3.addKill(ShootingTest.playerAndreaalf, false);
        kstTest3.addKill(ShootingTest.playerGino, true);
        kstTest3.addKill(ShootingTest.playerGino, true);
        ArrayList<String> ranking3 = new ArrayList<>(kstTest3.getRanking());

        Assert.assertEquals(ShootingTest.playerGino, ranking3.get(0));
        Assert.assertEquals(ShootingTest.playerMeme, ranking3.get(1));
        Assert.assertEquals(ShootingTest.playerAndreaalf, ranking3.get(2));
    }

    @Test
    public void noMoreSkullsFalse(){

        ArrayList<String> skullListTest = new ArrayList<>();
        skullListTest.add("SKULL");
        skullListTest.add("SKULL");
        skullListTest.add("SKULL");
        skullListTest.add("SKULL");
        skullListTest.add("SKULL");

        KillShotTrack kstTest = new KillShotTrack(skullListTest);

        Assert.assertFalse(kstTest.noMoreSkulls());

    }

    @Test
    public void noMoreSkullsTrue(){

        ArrayList<String> skullListTest = new ArrayList<>();
        skullListTest.add(ShootingTest.playerAndreaalf);
        skullListTest.add("ginogino");
        skullListTest.add("mememe");
        skullListTest.add(ShootingTest.playerAndreaalf);
        skullListTest.add(ShootingTest.playerAndreaalf);

        KillShotTrack kstTest = new KillShotTrack(skullListTest);

        Assert.assertTrue(kstTest.noMoreSkulls());

    }

    @Test
    public void noMoreSkullsException(){

        ArrayList<String> skullListTest = new ArrayList<>();
        skullListTest.add(ShootingTest.playerAndreaalf);
        skullListTest.add("SKULL");
        skullListTest.add("mememe");
        skullListTest.add(ShootingTest.playerAndreaalf);
        skullListTest.add(ShootingTest.playerAndreaalf);

        KillShotTrack kstTest = new KillShotTrack(skullListTest);

        try {
            kstTest.noMoreSkulls();
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
            return;
        }

        Assert.fail();
    }

}