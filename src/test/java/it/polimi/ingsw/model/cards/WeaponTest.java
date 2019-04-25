package it.polimi.ingsw.model.cards;


import org.junit.Assert;
import org.junit.Test;

public class WeaponTest {



    @Test
    public void reloadException() {

        Weapon weaponTest = new Weapon("weaponTest");

        weaponTest.reload();

        try {
            weaponTest.reload();
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
            return;
        }

        Assert.assertTrue(false);

    }

    @Test
    public void unloadException() {

        Weapon weaponTest = new Weapon("weaponTest");


        try {
            weaponTest.unload();
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
            return;
        }

        Assert.assertTrue(false);

    }

}