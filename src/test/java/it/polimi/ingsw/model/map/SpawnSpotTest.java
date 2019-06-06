package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.ShootingTest;
import it.polimi.ingsw.model.cards.Weapon;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SpawnSpotTest {

    @Test
    public  void addWeaponCheckFalse() {

        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");
        Weapon w4 = new Weapon("d");
        ArrayList<Weapon> weaponListTest = new ArrayList<>();
        weaponListTest.add(w1);
        weaponListTest.add(w2);
        weaponListTest.add(w3);

        SpawnSpot spawnSpotTest =new SpawnSpot(weaponListTest);

        Assert.assertFalse(spawnSpotTest.addWeapon(w4));
    }

    @Test
    public void addWeaponCheckTrue() {

        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");
        ArrayList<Weapon> weaponListTest = new ArrayList<>();
        weaponListTest.add(w1);
        weaponListTest.add(w2);

        SpawnSpot spawnSpotTest = new SpawnSpot(weaponListTest);

        Assert.assertTrue(spawnSpotTest.addWeapon(w3));
    }

    @Test
    public void removeWeaponCheckEmptyWeaponListCondition() {
        Weapon w = new Weapon("a");
        ArrayList<Weapon> weaponListTest = new ArrayList<Weapon>();

        SpawnSpot spawnSpotTest = new SpawnSpot(weaponListTest);

        Assert.assertFalse(spawnSpotTest.removeWeapon(w));

    }

    @Test
    public void removeWeaponCheckNotInWeaponListCondition(){
        Weapon w = new Weapon("a");
        Weapon wToRemove = new Weapon("b");

        ArrayList<Weapon> weaponListTest = new ArrayList<>();
        weaponListTest.add(w);

        SpawnSpot spawnSpotTest = new SpawnSpot(weaponListTest);

        Assert.assertFalse(spawnSpotTest.removeWeapon(wToRemove));
    }

    @Test
    public void removeWeaponCheckTrueCondition(){
        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");

        ArrayList<Weapon> weaponListTest = new ArrayList<Weapon>();
        weaponListTest.add(w1);
        weaponListTest.add(w2);
        weaponListTest.add(w3);

        SpawnSpot spawnSpotTest = new SpawnSpot(weaponListTest);
        Assert.assertTrue(spawnSpotTest.removeWeapon(w1));


    }

    @Test
    public void refillCheckWeaponIsAddedWithoutRuntimeExceptions(){
        Weapon w1 = new Weapon("a");
        ArrayList<Weapon> weaponsListTest = new ArrayList<>();
        SpawnSpot spawnSpotTest = new SpawnSpot(weaponsListTest);

        spawnSpotTest.refill(w1);

        Assert.assertEquals(1, spawnSpotTest.getWeaponList().size());
    }

    @Test
    public void grabSomething(){
        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");

        ArrayList<Weapon> weaponListTest = new ArrayList<>();
        weaponListTest.add(w1);
        weaponListTest.add(w2);
        weaponListTest.add(w3);

        SpawnSpot spawnSpotTest = new SpawnSpot(weaponListTest);

        Player playerTest = new Player(ShootingTest.playerGino, 1, 0);
        spawnSpotTest.grabSomething(playerTest, 0);

        Assert.assertTrue(playerTest.getWeaponList().contains(w1));

    }
}

