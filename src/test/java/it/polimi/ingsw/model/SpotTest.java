package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SpotTest {

    @Test
    void seesInTheSameRoomTrue() {
        Spot spotTest1 = new Spot(Room.SAPPHIRE,0,0,0);
        Spot spotTest2 = new Spot(Room.SAPPHIRE,0,1,1);

        Assert.assertTrue(spotTest1.sees(spotTest2));
    }

    @Test
    void seesInTheSameRoomFalse() {
        Spot spotTest1 = new Spot(Room.SAPPHIRE,0,0,0);
        Spot spotTest2 = new Spot(Room.TOPAZ,0,2,2);

        Assert.assertFalse(spotTest1.sees(spotTest2));
    }

    @Test
    void seesInAnotherRoomNorthTrue() {
        Spot spotTest1 = new Spot(Room.SAPPHIRE,0,  0,  0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(true);
        spotTest1.setDoors(doorsTest);

        Spot spotTest2 = new Spot(Room.TOPAZ,0,0,1);

        Assert.assertTrue(spotTest1.sees(spotTest2));
    }

    @Test
    void seesInAnotherRoomNorthFalse() {
        Spot spotTest1 = new Spot(Room.SAPPHIRE,0,  0,  0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(false);
        spotTest1.setDoors(doorsTest);

        Spot spotTest2 = new Spot(Room.TOPAZ,0,0,1);

        Assert.assertFalse(spotTest1.sees(spotTest2));
    }

    @Test
    void hasNorthDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);
        doorsTest.set(0,true);
        Assert.assertTrue(doorsTest.get(0));
    }

    @Test
    void hasNorthDoorFalse() {
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