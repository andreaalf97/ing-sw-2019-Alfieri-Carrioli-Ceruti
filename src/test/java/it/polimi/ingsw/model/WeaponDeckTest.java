package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponDeckTest {

    @Test
    void pickCard() {

        WeaponDeck weaponDeckTest = new WeaponDeck();

        weaponDeckTest.getWeaponList().add(new Weapon("SledgeHammer"));
        weaponDeckTest.getWeaponList().add(new Weapon("Shockwave"));

        Weapon weaponToPick = weaponDeckTest.pickCard();

        Assert.assertEquals(1, weaponDeckTest.getWeaponList().size());

    }
}