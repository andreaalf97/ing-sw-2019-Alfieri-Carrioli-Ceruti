package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

public class SpotTest {


    @Test
    public void hasNorthDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(true);

        Assert.assertTrue(spotTest.getDoors().get(0));
    }

    @Test
    public void hasNorthDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);

        Assert.assertFalse(spotTest.getDoors().get(0));
    }

    @Test
    public void hasSouthDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().set(2,true);
        Assert.assertTrue(spotTest.getDoors().get(2));
    }

    @Test
    public void hasSouthDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);

        Assert.assertFalse(spotTest.getDoors().get(2));
    }

    @Test
    public void hasEastDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(true);
        Assert.assertTrue(spotTest.getDoors().get(1));
    }

    @Test
    public void hasEastDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        Assert.assertFalse(spotTest.getDoors().get(1));
    }

    @Test
    public void hasWestDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().set(3,true);
        Assert.assertTrue(spotTest.getDoors().get(3));
    }

    @Test
    public void hasWestDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        Assert.assertFalse(spotTest.getDoors().get(3));
    }
}