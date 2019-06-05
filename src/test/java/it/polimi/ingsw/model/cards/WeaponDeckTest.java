package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.MyJsonParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class WeaponDeckTest {

    @Test
    public void drawCard() {

        ArrayList<Weapon> weaponListTest = new ArrayList<Weapon>();
        Weapon w1 = new Weapon("SledgeHammer");
        Weapon w2 = new Weapon ("Shockwave");
        weaponListTest.add(w1);
        weaponListTest.add(w2);

        WeaponDeck weaponDeckTest = new WeaponDeck(weaponListTest);

        Weapon weaponPicked = weaponDeckTest.drawCard();

        Assert.assertTrue(1 == weaponDeckTest.getWeaponList().size() && weaponPicked.equals(w1));

    }

    @Test
    public void drawCardEmpty() {

        ArrayList<Weapon> weaponListTest = new ArrayList<>();

        WeaponDeck weaponDeckTest = new WeaponDeck(weaponListTest);

        Weapon weaponPicked = weaponDeckTest.drawCard();

        Assert.assertNull(weaponPicked);

    }

    @Test
    public void verifyJson(){
        WeaponDeck weaponDeckTest = MyJsonParser.createWeaponDeckFromJson();

        Assert.assertTrue(weaponDeckTest.getWeaponList().size() == 21);
    }
}