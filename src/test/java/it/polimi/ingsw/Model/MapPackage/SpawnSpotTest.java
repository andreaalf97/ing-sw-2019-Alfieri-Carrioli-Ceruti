package it.polimi.ingsw.Model.MapPackage;

import it.polimi.ingsw.Model.CardsPackage.Weapon;
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
}