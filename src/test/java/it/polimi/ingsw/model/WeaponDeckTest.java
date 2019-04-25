package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.cards.WeaponDeck;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class WeaponDeckTest {

    @Test
    public void pickCard() {

        ArrayList<Weapon> weaponListTest = new ArrayList<Weapon>();
        Weapon w1 = new Weapon("SledgeHammer");
        Weapon w2 = new Weapon ("Shockwave");
        weaponListTest.add(w1);
        weaponListTest.add(w2);

        WeaponDeck weaponDeckTest = new WeaponDeck(weaponListTest);

        Weapon weaponPicked = weaponDeckTest.drawCard();

        Assert.assertEquals(1, weaponDeckTest.getWeaponList().size());

    }
    @Test
    public void verifyJson(){
        WeaponDeck weaponDeckTest = new WeaponDeck();

        Assert.assertTrue(weaponDeckTest.getWeaponList().size() == 21);
    }
}