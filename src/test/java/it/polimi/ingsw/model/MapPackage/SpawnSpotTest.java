package it.polimi.ingsw.model.MapPackage;

import it.polimi.ingsw.model.CardsPackage.Weapon;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SpawnSpotTest {

    @Test
    public  void addWeaponCheckFalse() {
        SpawnSpot spawnSpotTest = new SpawnSpot();

        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");
        Weapon w4 = new Weapon("d");
        ArrayList<Weapon> weaponListTest = new ArrayList<>();
        weaponListTest.add(w1);
        weaponListTest.add(w2);
        weaponListTest.add(w3);

        spawnSpotTest.setWeaponList(weaponListTest);

        Assert.assertFalse(spawnSpotTest.addWeapon(w4));
    }
    @Test
    public void addWeaponCheckTrue() {
        SpawnSpot spawnSpotTest = new SpawnSpot();

        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");
        ArrayList<Weapon> weaponListTest = new ArrayList<>();

        weaponListTest.add(w1);
        weaponListTest.add(w2);
        spawnSpotTest.setWeaponList(weaponListTest);

        Assert.assertTrue(spawnSpotTest.addWeapon(w3));
    }

    @Test
    public void removeWeaponCheckEmptyWeaponListCondition() {
        SpawnSpot spawnSpotTest = new SpawnSpot();
        Weapon w = new Weapon("a");

        ArrayList<Weapon> weaponListTest = new ArrayList<Weapon>();
        spawnSpotTest.setWeaponList(weaponListTest);

        Assert.assertFalse(spawnSpotTest.removeWeapon(w));

    }

    @Test
    public void removeWeaponCheckNotInWeaponListCondition(){
        SpawnSpot spawnSpotTest = new SpawnSpot();
        Weapon w = new Weapon("a");
        Weapon wToRemove = new Weapon("b");

        ArrayList<Weapon> weaponListTest = new ArrayList<>();
        weaponListTest.add(w);

        spawnSpotTest.setWeaponList(weaponListTest);
        Assert.assertFalse(spawnSpotTest.removeWeapon(wToRemove));
    }
    @Test
    public void removeWeaponCheckTrueCondition(){
        SpawnSpot spawnSpotTest = new SpawnSpot();
        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");

        ArrayList<Weapon> weaponListTest = new ArrayList<Weapon>();
        weaponListTest.add(w1);
        weaponListTest.add(w2);
        weaponListTest.add(w3);

        spawnSpotTest.setWeaponList(weaponListTest);
        Assert.assertTrue(spawnSpotTest.removeWeapon(w1));


    }
}