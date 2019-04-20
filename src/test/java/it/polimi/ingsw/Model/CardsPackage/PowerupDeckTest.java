package it.polimi.ingsw.Model.CardsPackage;

import org.junit.Assert;
import org.junit.Test;

public class PowerupDeckTest {

    @Test
    public void verifyjson(){
        PowerupDeck powerUpDeckTest = new PowerupDeck();

        Assert.assertTrue(powerUpDeckTest.getPowerupList().size() == 24);
    }

}