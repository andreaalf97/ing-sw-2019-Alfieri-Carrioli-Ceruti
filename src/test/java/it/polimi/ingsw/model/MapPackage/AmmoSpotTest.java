package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.MapPackage.AmmoSpot;
import it.polimi.ingsw.model.MapPackage.Room;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AmmoSpotTest {


    @Test
    public void addAmmoWhenSpotIsUnloaded() {
        AmmoSpot ammoSpotTest = new AmmoSpot();
        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        ammoSpotTest.setAmmoColorList(ammoColorListTest);

        ammoSpotTest.addAmmos();
        Assert.assertFalse(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void addAmmoWhenSpotIsAlreadyLoadedWithPowerup(){
        AmmoSpot ammoSpotTest = new AmmoSpot();
        ArrayList<Color> ammoColorListTest = new ArrayList<>();

        ammoColorListTest.add(Color.RED);
        ammoColorListTest.add(Color.RED);
        ammoSpotTest.setAmmoColorList(ammoColorListTest);

        ammoSpotTest.addAmmos();
        Assert.assertEquals(ammoColorListTest,ammoSpotTest.getAmmoColorList());
    }

    @Test
    public void addAmmoWhenSpotIsAlreadyLoaded(){
        AmmoSpot ammoSpotTest = new AmmoSpot();
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
        AmmoSpot ammoSpotTest = new AmmoSpot();
        ArrayList<Color> ammoColorListTest = new ArrayList<>();

        ammoColorListTest.add(Color.RED);
        ammoColorListTest.add(Color.BLUE);

        ammoSpotTest.setAmmoColorList(ammoColorListTest);
        ammoSpotTest.removeAmmos();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void removeAmmoFalse(){
        AmmoSpot ammoSpotTest = new AmmoSpot();
        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        ammoSpotTest.setAmmoColorList(ammoColorListTest);

        ammoSpotTest.removeAmmos();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }
}