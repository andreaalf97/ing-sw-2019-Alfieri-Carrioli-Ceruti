package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

public class SpawnSpotTest {

    @Test
    public  void addWeaponCheckFalse() {
        SpawnSpot spawnSpotTest = new SpawnSpot(Room.TOPAZ,0,0,0);

        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");
        Weapon w4 = new Weapon("d");
        spawnSpotTest.getWeaponList().add(w1);
        spawnSpotTest.getWeaponList().add(w2);
        spawnSpotTest.getWeaponList().add(w3);

        Assert.assertFalse(spawnSpotTest.addWeapon(w4));
    }
    @Test
    public void addWeaponCheckTrue() {
        SpawnSpot spawnSpotTest = new SpawnSpot(Room.TOPAZ,0,0,0);

        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");

        spawnSpotTest.getWeaponList().add(w1);
        spawnSpotTest.getWeaponList().add(w2);

        Assert.assertTrue(spawnSpotTest.addWeapon(w3));
    }

    @Test
    public void removeWeaponCheckEmptyWeaponListCondition() {
        SpawnSpot spawnSpotTest = new SpawnSpot(Room.RUBY,0,0,0);
        Weapon w = new Weapon("a");

        Assert.assertFalse(spawnSpotTest.removeWeapon(w));

    }

    @Test
    public void removeWeaponCheckNotInWeaponListCondition(){
        SpawnSpot spawnSpotTest = new SpawnSpot(Room.RUBY,0,0,0);
        Weapon w = new Weapon("a");
        Weapon wToRemove = new Weapon("b");

        spawnSpotTest.addWeapon(w);
        Assert.assertFalse(spawnSpotTest.removeWeapon(wToRemove));
    }
    @Test
    public void removeWeaponCheckTrueCondition(){
        SpawnSpot spawnSpotTest = new SpawnSpot(Room.RUBY,0,0,0);
        Weapon w1 = new Weapon("a");
        Weapon w2 = new Weapon("b");
        Weapon w3 = new Weapon("c");

        spawnSpotTest.addWeapon(w1);
        spawnSpotTest.addWeapon(w2);
        spawnSpotTest.addWeapon(w3);

        Assert.assertTrue(spawnSpotTest.removeWeapon(w1));


    }
}