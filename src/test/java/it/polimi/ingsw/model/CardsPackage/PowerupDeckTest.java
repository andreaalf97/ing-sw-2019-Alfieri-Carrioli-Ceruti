package it.polimi.ingsw.model.CardsPackage;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PowerupDeckTest {

    @Test
    public void verifyjson(){
        PowerupDeck powerUpDeckTest = new PowerupDeck();

        Assert.assertTrue(powerUpDeckTest.getPowerupList().size() == 24);
    }

}