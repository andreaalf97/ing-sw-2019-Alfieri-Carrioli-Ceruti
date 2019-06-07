package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.ShootingTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SpotTest {

    //these are useless tests
    @Test
    public void hasNorthDoorTrue() {
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(true);
        Room room = Room.AMETHYST;

        Spot spotTest = new Spot(doorsTest, room);
        Assert.assertTrue(spotTest.getDoors().get(0));
    }

    @Test
    public void hasNorthDoorFalse() {
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(false);
        Room room = Room.SAPPHIRE;

        Spot spotTest = new Spot(doorsTest, room);
        Assert.assertFalse(spotTest.getDoors().get(0));
    }

    @Test
    public void hasSouthDoorTrue() {
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(true);
        Room room = Room.SAPPHIRE;

        Spot spotTest = new Spot(doorsTest, room);
        Assert.assertTrue(spotTest.getDoors().get(2));
    }

    @Test
    public void hasSouthDoorFalse() {
        ArrayList<Boolean> doorstest = new ArrayList<>();
        doorstest.add(false);
        doorstest.add(false);
        doorstest.add(false);
        Room room = Room.SAPPHIRE;

        Spot spotTest = new Spot(doorstest, room);
        Assert.assertFalse(spotTest.getDoors().get(2));
    }

    @Test
    public void hasEastDoorTrue() {
        ArrayList<Boolean> doorstest = new ArrayList<>();
        doorstest.add(false);
        doorstest.add(true);
        Room room = Room.SAPPHIRE;

        Spot spotTest = new Spot(doorstest, room);
        Assert.assertTrue(spotTest.getDoors().get(1));
    }

    @Test
    public void hasEastDoorFalse() {
        ArrayList<Boolean> doorstest = new ArrayList<>();
        doorstest.add(false);
        doorstest.add(false);
        Room room = Room.SAPPHIRE;

        Spot spotTest = new Spot(doorstest, room);
        Assert.assertFalse(spotTest.getDoors().get(1));
    }

    @Test
    public void hasWestDoorTrue() {
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(true);
        Room room = Room.SAPPHIRE;

        Spot spotTest = new Spot(doorsTest, room);
        Assert.assertTrue(spotTest.getDoors().get(3));
    }

    @Test
    public void hasWestDoorFalse() {
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(false);
        doorsTest.add(false);
        Room room = Room.SAPPHIRE;

        Spot spotTest = new Spot(doorsTest, room);
        Assert.assertFalse(spotTest.getDoors().get(3));
    }

    @Test
    public void removePlayer(){
        Spot spotTest = new Spot();
        spotTest.addPlayer(ShootingTest.playerGino);

        spotTest.removePlayer(ShootingTest.playerGino);
        Assert.assertEquals(0, spotTest.getPlayersHere().size());

    }
}