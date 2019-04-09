package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

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
        ammoSpotTest.getAmmoColorList().add(Color.RED);
        ammoSpotTest.getAmmoColorList().add(Color.RED);

        ammoSpotTest.addAmmos();
        Assert.assertEquals(2,ammoSpotTest.getAmmoColorList().size());
    }

    @Test
    public void addAmmoWhenSpotIsAlreadyLoaded(){
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.SAPPHIRE,0,0,0);
        ammoSpotTest.getAmmoColorList().add(Color.RED);
        ammoSpotTest.getAmmoColorList().add(Color.RED);
        ammoSpotTest.getAmmoColorList().add(Color.RED);

        ammoSpotTest.addAmmos();
        Assert.assertEquals(3,ammoSpotTest.getAmmoColorList().size());
    }


    @Test
    public  void removeAmmoTrue() {
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.SAPPHIRE,0,0,0);

        ammoSpotTest.getAmmoColorList().add(Color.RED);
        ammoSpotTest.getAmmoColorList().add(Color.BLUE);

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