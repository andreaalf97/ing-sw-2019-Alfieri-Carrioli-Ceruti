package it.polimi.ingsw.model;


import it.polimi.ingsw.model.CardsPackage.Weapon;
import org.junit.Assert;
import org.junit.Test;

public class WeaponTest {

    @Test
    public void verifyReadingOfEffectJson(){
        Weapon weaponTest = new Weapon("Lockrifle");
        weaponTest.loadWeaponFromJson("LockRifle");
        Assert.assertTrue(true);
    }


    @Test
    public void reload() {
    }

}