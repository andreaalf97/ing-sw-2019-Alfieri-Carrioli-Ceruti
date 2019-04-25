package it.polimi.ingsw.Model.MapPackage;

import it.polimi.ingsw.Model.CardsPackage.PowerUp;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AmmoSpotTest {


    @Test
    public void addAmmoWhenSpotIsUnloaded() {

        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        PowerUp powerup = new PowerUp();

        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);

        ammoSpotTest.addAmmo();
        Assert.assertFalse(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void addAmmoWhenSpotIsAlreadyLoadedWithPowerup(){
        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        PowerUp powerup = new PowerUp();

        ammoColorListTest.add(Color.RED);
        ammoColorListTest.add(Color.RED);

        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);

        ammoSpotTest.addAmmo();
        Assert.assertEquals(ammoColorListTest,ammoSpotTest.getAmmoColorList());
    }

    @Test
    public void addAmmoWhenSpotIsAlreadyLoaded(){
        ArrayList<Color> ammoColoListTest = new ArrayList<>();
        PowerUp powerup = new PowerUp();

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
        PowerUp powerup = new PowerUp();

        ammoColorListTest.add(Color.RED);
        ammoColorListTest.add(Color.BLUE);

        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);
        ammoSpotTest.removeAmmo();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void removeAmmoFalse(){
        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        PowerUp powerup = new PowerUp();
        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);

        ammoSpotTest.removeAmmo();
        Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void refill(){
        AmmoSpot ammoSpotTest = new AmmoSpot();
        ammoSpotTest.refill(null);

        Assert.assertEquals(3,ammoSpotTest.getAmmoColorList().size());
        Assert.assertFalse(ammoSpotTest.getAmmoColorList().contains(Color.ANY));

        AmmoSpot ammoSpotTestWithPowerup = new AmmoSpot();
        PowerUp p1 = new PowerUp();
        ammoSpotTestWithPowerup.refill(p1);

        Assert.assertEquals(2,ammoSpotTestWithPowerup.getAmmoColorList().size());
        Assert.assertFalse(ammoSpotTest.getAmmoColorList().contains(Color.ANY));
    }


    @Test
    public void grabSomething(){
        AmmoSpot ammoSpotTest = new AmmoSpot();
        Player playerTest = new Player("gino");
        PowerUp p1 = new PowerUp();
        ammoSpotTest.refill(p1);

        ammoSpotTest.grabSomething(playerTest);

        Assert.assertEquals(1, playerTest.getPowerUpList().size());

    }
}