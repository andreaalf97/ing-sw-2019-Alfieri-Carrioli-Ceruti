package it.polimi.ingsw.model;

import it.polimi.ingsw.model.CardsPackage.Weapon;
import it.polimi.ingsw.model.CardsPackage.WeaponDeck;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class WeaponDeckTest {

    @Test
    void pickCard() {

        ArrayList<Weapon> weaponListTest = new ArrayList<Weapon>();
        Weapon w1 = new Weapon("SledgeHammer");
        Weapon w2 = new Weapon ("Shockwave");
        weaponListTest.add(w1);
        weaponListTest.add(w2);

        WeaponDeck weaponDeckTest = new WeaponDeck();
        weaponDeckTest.setWeaponDeck(weaponListTest);

        Weapon weaponPicked = weaponDeckTest.pickCard();

        Assert.assertEquals(1, weaponDeckTest.getWeaponList().size());

    }
}