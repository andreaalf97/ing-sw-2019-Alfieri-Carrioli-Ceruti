package it.polimi.ingsw.Model.MapPackage;

import it.polimi.ingsw.Model.KillShotTrack;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class KillShotTrackTest {

    @Test
    public void addKillTrue() {
        KillShotTrack kstTest = new KillShotTrack(5);
        kstTest.addKill("gino", true);
        kstTest.addKill("gino", true);
        kstTest.addKill("gino", true);
        kstTest.addKill("gino", true);
        kstTest.addKill("gino", true);

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
        kstTest.addKill("gino", false);
        kstTest.addKill("gino", true);
        kstTest.addKill("gino", true);
        kstTest.addKill("nogi", true);
        kstTest.addKill("nogi", false);
        kstTest.addKill("nogi", false); //this cover all the first if block inside the first for block, the while block is always covered

        ArrayList<String> ranking = new ArrayList<>(kstTest.getRanking());

        Assert.assertEquals("gino", ranking.get(0));
        Assert.assertEquals("nogi", ranking.get(1));
        Assert.assertEquals(2,ranking.size());

        KillShotTrack kstTest2 = new KillShotTrack(6);
        ArrayList<String> ranking2 = new ArrayList<>(kstTest2.getRanking());

        Assert.assertEquals(0, ranking2.size());
    }

}