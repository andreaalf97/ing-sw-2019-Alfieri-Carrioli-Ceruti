package it.polimi.ingsw.model.cards;


import org.junit.Assert;
import org.junit.Test;

public class WeaponTest {


    @Test
    public void unload() {

        Weapon weaponTest = new Weapon("weaponTest");

        try {
            weaponTest.unload();
        }
        catch (RuntimeException e){
            Assert.fail();
        }

        try {
            weaponTest.unload();
        }
        catch (RuntimeException e){
            Assert.assertTrue(true);
        }
    }

}