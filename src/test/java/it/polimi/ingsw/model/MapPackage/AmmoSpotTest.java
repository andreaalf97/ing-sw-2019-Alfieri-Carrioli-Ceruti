package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.CardsPackage.Powerup;
import it.polimi.ingsw.model.Color;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AmmoSpotTest {


    @Test
    public void addAmmoWhenSpotIsUnloaded() {

        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        Powerup powerup = new Powerup();

        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);

        ammoSpotTest.addAmmo();
        Assert.assertFalse(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void addAmmoWhenSpotIsAlreadyLoadedWithPowerup(){
        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        Powerup powerup = new Powerup();

        ammoColorListTest.add(Color.RED);
        ammoColorListTest.add(Color.RED);

        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);

        ammoSpotTest.addAmmo();
        Assert.assertEquals(ammoColorListTest,ammoSpotTest.getAmmoColorList());
    }

    @Test
    public void addAmmoWhenSpotIsAlreadyLoaded(){
        ArrayList<Color> ammoColoListTest = new ArrayList<>();
        Powerup powerup = new Powerup();

        ammoColoListTest.add(Color.RED);
        ammoColoListTest.add(Color.RED);
        ammoColoListTest.add(Color.RED);
        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColoListTest, powerup);

        ammoSpotTest.addAmmo();
        Assert.assertEquals(ammoColoListTest,ammoSpotTest.getAmmoColorList());
    }


    @Test
    public  void removeAmmoTrue() {
        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        Powerup powerup = new Powerup();

        ammoColorListTest.add(Color.RED);
        ammoColorListTest.add(Color.BLUE);

        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);
        ammoSpotTest.removeAmmo();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void removeAmmoFalse(){
        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        Powerup powerup = new Powerup();
        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);

        ammoSpotTest.removeAmmo();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }
}