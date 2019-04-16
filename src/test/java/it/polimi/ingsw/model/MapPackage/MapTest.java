package it.polimi.ingsw.model.MapPackage;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    void verifyJsonMapReading(){
        MapBuilder.generateMap(MapName.FIRE);

        Assert.assertTrue(true);
    }

    @Test
    void movePlayer() {
        assertEquals(0, 0);
    }

    @Test
    void see(){
        //TODO
    }
}