package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SpotTest {

    @Test
    void sees() {
        assertTrue(true);
    }

    @Test
    void hasNordDoor() {
        Spot spotTest = new Spot("red",0,0,0);
        ArrayList<Boolean> doorsTest = new ArrayList<>();
        spotTest.setDoors(doorsTest);

        doorsTest.set(0,true);

        assertTrue(doorsTest.get(0) == true);

    }

    @Test
    void hasEastDoor() {
    }

    @Test
    void hasSouthDoor() {
    }

    @Test
    void hasWestDoor() {
    }
}