package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AmmoSpotTest {


    @Test
    public void addAmmoWhenSpotIsUnloaded() {
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.TOPAZ,0,0,0);

        ammoSpotTest.addAmmos();
        Assert.assertFalse(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void addAmmoWhenSpotIsAlreadyLoadedWithPowerup(){
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.SAPPHIRE,0,0,0);
        ArrayList<Color> ammoColorListTest = new ArrayList<>();

        ammoColorListTest.add(Color.RED);
        ammoColorListTest.add(Color.RED);
        ammoSpotTest.setAmmoColorList(ammoColorListTest);

        ammoSpotTest.addAmmos();
        Assert.assertEquals(ammoColorListTest,ammoSpotTest.getAmmoColorList());
    }

    @Test
    public void addAmmoWhenSpotIsAlreadyLoaded(){
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.SAPPHIRE,0,0,0);
        ArrayList<Color> ammoColoListTest = new ArrayList<>();

        ammoColoListTest.add(Color.RED);
        ammoColoListTest.add(Color.RED);
        ammoColoListTest.add(Color.RED);
        ammoSpotTest.setAmmoColorList(ammoColoListTest);

        ammoSpotTest.addAmmos();
        Assert.assertEquals(ammoColoListTest,ammoSpotTest.getAmmoColorList());
    }


    @Test
    public  void removeAmmoTrue() {
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.SAPPHIRE,0,0,0);
        ArrayList<Color> ammoColorListTest = new ArrayList<>();

        ammoColorListTest.add(Color.RED);
        ammoColorListTest.add(Color.BLUE);

        ammoSpotTest.removeAmmos();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void removeAmmoFalse(){
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.TOPAZ,0,0,0);

        ammoSpotTest.removeAmmos();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }
}