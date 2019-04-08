package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AmmoSpotTest {


    @Test
    void addAmmoWhenSpotIsUnloaded() {
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.TOPAZ,0,0,0);

        ammoSpotTest.addAmmo();
        Assert.assertTrue(!ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    void addAmmoWhenSpotIsAlreadyLoadedWithPowerup(){
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.SAPPHIRE,0,0,0);
        ammoSpotTest.getAmmoColorList().add(Color.RED);
        ammoSpotTest.getAmmoColorList().add(Color.RED);

        ammoSpotTest.addAmmo();
        Assert.assertEquals(2,ammoSpotTest.getAmmoColorList().size());
    }

    @Test
    void addAmmoWhenSpotIsAlreadyLoaded(){
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.SAPPHIRE,0,0,0);
        ammoSpotTest.getAmmoColorList().add(Color.RED);
        ammoSpotTest.getAmmoColorList().add(Color.RED);
        ammoSpotTest.getAmmoColorList().add(Color.RED);

        ammoSpotTest.addAmmo();
        Assert.assertEquals(3,ammoSpotTest.getAmmoColorList().size());
    }


    @Test
    void removeAmmoTrue() {
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.SAPPHIRE,0,0,0);

        ammoSpotTest.getAmmoColorList().add(Color.RED);
        ammoSpotTest.getAmmoColorList().add(Color.BLUE);

        ammoSpotTest.removeAmmo();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    void removeAmmoFalse(){
        AmmoSpot ammoSpotTest = new AmmoSpot(Room.TOPAZ,0,0,0);

        ammoSpotTest.removeAmmo();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }
}