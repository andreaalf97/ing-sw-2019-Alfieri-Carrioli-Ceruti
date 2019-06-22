package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.ShootingTest;
import it.polimi.ingsw.model.cards.AmmoCard;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AmmoSpotTest {


    @Test
    public void refillAmmoWhenSpotIsUnloaded() {

        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        PowerUp powerup = new PowerUp();

        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);

        boolean hasPowerUp = false;
        String imagePath = "";
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.BLUE);

        AmmoCard ammoCardDrawn = new AmmoCard(imagePath, colors, hasPowerUp);
        ammoSpotTest.refill(ammoCardDrawn);
        Assert.assertFalse(ammoSpotTest.getAmmoColorList().isEmpty());
    }

    @Test
    public void refillAmmoWhenSpotIsAlreadyLoadedWithPowerup(){
        ArrayList<Color> ammoColorListTest = new ArrayList<>();
        PowerUp powerup = new PowerUp();

        ammoColorListTest.add(Color.RED);
        ammoColorListTest.add(Color.RED);

        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColorListTest, powerup);

        ammoSpotTest.setPowerUp(powerup);
        Assert.assertEquals(ammoColorListTest,ammoSpotTest.getAmmoColorList());
    }

    @Test
    public void refillAmmoWhenSpotIsAlreadyLoaded(){
        ArrayList<Color> ammoColoListTest = new ArrayList<>();
        PowerUp powerup = null;

        ammoColoListTest.add(Color.RED);
        ammoColoListTest.add(Color.RED);
        ammoColoListTest.add(Color.RED);
        AmmoSpot ammoSpotTest = new AmmoSpot(ammoColoListTest, powerup);

        boolean hasPowerUp = false;
        String imagePath = "";
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);

        AmmoCard ammoCardDrawn = new AmmoCard(imagePath, colors, hasPowerUp);
        ammoSpotTest.refill(ammoCardDrawn);


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

        try {
            ammoSpotTest.removeAmmo();
            Assert.fail();
        }
        catch (RuntimeException e) {
            Assert.assertTrue(ammoSpotTest.getAmmoColorList().isEmpty());
        }

    }

    @Test
    public void refillGeneric(){
        AmmoSpot ammoSpotTest = new AmmoSpot();

        boolean hasPowerUp = false;
        String imagePath = "";
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);

        AmmoCard ammoCardDrawn = new AmmoCard(imagePath, colors, hasPowerUp);
        ammoSpotTest.refill(ammoCardDrawn);

        Assert.assertEquals(3,ammoSpotTest.getAmmoColorList().size());
        Assert.assertFalse(ammoSpotTest.getAmmoColorList().contains(Color.ANY));

        AmmoSpot ammoSpotTestWithPowerup = new AmmoSpot();

        PowerUp p1 = new PowerUp();
        colors.remove(0);
        ammoCardDrawn = new AmmoCard(imagePath, colors, hasPowerUp);
        ammoSpotTestWithPowerup.setPowerUp(p1);
        ammoSpotTestWithPowerup.refill(ammoCardDrawn);

        Assert.assertEquals(2,ammoSpotTestWithPowerup.getAmmoColorList().size());
        Assert.assertFalse(ammoSpotTest.getAmmoColorList().contains(Color.ANY));
    }

    @Test
    public void grabSomethingWithPowerup(){
        AmmoSpot ammoSpotTest = new AmmoSpot();
        Player playerTest = new Player(ShootingTest.playerGino);
        PowerUp p1 = new PowerUp();
        ammoSpotTest.setPowerUp(p1);

        boolean hasPowerUp = false;
        String imagePath = "";
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);

        AmmoCard ammoCardDrawn = new AmmoCard(imagePath, colors, hasPowerUp);
        ammoSpotTest.refill(ammoCardDrawn);
        ammoSpotTest.grabSomething(playerTest, -1);

        Assert.assertEquals(1, playerTest.getPowerUpList().size());
        Assert.assertTrue((playerTest.getnBlueAmmo() + playerTest.getnRedAmmo() + playerTest.getnYellowAmmo()) > 0);
    }
}