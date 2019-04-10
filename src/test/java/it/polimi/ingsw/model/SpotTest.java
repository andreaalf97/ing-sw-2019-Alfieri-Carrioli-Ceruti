package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SpotTest {


    @Test
    public void hasNorthDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(true);
        spotTest.setDoors(doorsTest);
        Assert.assertTrue(spotTest.getDoors().get(0));
    }

    @Test
    public void hasNorthDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(false);
        spotTest.setDoors(doorsTest);
        Assert.assertFalse(spotTest.getDoors().get(0));
    }

    @Test
    public void hasSouthDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(true);
        spotTest.setDoors(doorsTest);
        Assert.assertTrue(spotTest.getDoors().get(2));
    }

    @Test
    public void hasSouthDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorstest = new ArrayList<>();
        doorstest.add(false);
        doorstest.add(false);
        doorstest.add(false);
        spotTest.setDoors(doorstest);
        Assert.assertFalse(spotTest.getDoors().get(2));
    }

    @Test
    public void hasEastDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorstest = new ArrayList<>();
        doorstest.add(false);
        doorstest.add(true);
        spotTest.setDoors(doorstest);
        Assert.assertTrue(spotTest.getDoors().get(1));
    }

    @Test
    public void hasEastDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorstest = new ArrayList<>();
        doorstest.add(false);
        doorstest.add(false);
        spotTest.setDoors(doorstest);
        Assert.assertFalse(spotTest.getDoors().get(1));
    }

    @Test
    public void hasWestDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(true);
        spotTest.setDoors(doorsTest);
        Assert.assertTrue(spotTest.getDoors().get(3));
    }

    @Test
    public void hasWestDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(false);
        spotTest.setDoors(doorsTest);
        Assert.assertFalse(spotTest.getDoors().get(3));
    }
}