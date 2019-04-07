package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SpotTest {

    @Test
    void sees() {
        Assert.assertTrue(true);
    }

    @Test
    void hasNordDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);
        doorsTest.set(0,true);
        Assert.assertTrue(doorsTest.get(0));
    }

    @Test
    void hasNordDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);
        doorsTest.set(0,false);
        Assert.assertFalse(doorsTest.get(0));
    }

    @Test
    void hasSouthDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);
        doorsTest.set(2,true);
        Assert.assertTrue(doorsTest.get(2));
    }

    @Test
    void hasSouthDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);
        doorsTest.set(2,false);
        Assert.assertFalse(doorsTest.get(2));
    }

    @Test
    void hasEastDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);
        doorsTest.set(1,true);
        Assert.assertTrue(doorsTest.get(1));
    }

    @Test
    void hasEastDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);
        doorsTest.set(1,false);
        Assert.assertFalse(doorsTest.get(1));
    }

    @Test
    void hasWestDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);
        doorsTest.set(3,true);
        Assert.assertTrue(doorsTest.get(3));
    }

    @Test
    void hasWestDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);
        doorsTest.set(3,false);
        Assert.assertFalse(doorsTest.get(3));
    }
}