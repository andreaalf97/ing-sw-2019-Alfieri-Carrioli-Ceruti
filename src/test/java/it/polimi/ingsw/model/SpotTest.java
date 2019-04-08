package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SpotTest {


    @Test
    void hasNorthDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(true);

        Assert.assertTrue(spotTest.getDoors().get(0));
    }

    @Test
    void hasNorthDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);

        Assert.assertFalse(spotTest.getDoors().get(0));
    }

    @Test
    void hasSouthDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().set(2,true);
        Assert.assertTrue(spotTest.getDoors().get(2));
    }

    @Test
    void hasSouthDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);

        Assert.assertFalse(spotTest.getDoors().get(2));
    }

    @Test
    void hasEastDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(true);
        Assert.assertTrue(spotTest.getDoors().get(1));
    }

    @Test
    void hasEastDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        Assert.assertFalse(spotTest.getDoors().get(1));
    }

    @Test
    void hasWestDoorTrue() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().set(3,true);
        Assert.assertTrue(spotTest.getDoors().get(3));
    }

    @Test
    void hasWestDoorFalse() {
        Spot spotTest = new Spot(Room.RUBY,0,0,0);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        spotTest.getDoors().add(false);
        Assert.assertFalse(spotTest.getDoors().get(3));
    }
}