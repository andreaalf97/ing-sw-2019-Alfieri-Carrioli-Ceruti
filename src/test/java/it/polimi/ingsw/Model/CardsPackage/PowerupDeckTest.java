package it.polimi.ingsw.Model.CardsPackage;

import org.junit.Assert;
import org.junit.Test;

public class PowerupDeckTest {

    @Test
    public void verifyjson(){
        PowerupDeck powerUpDeckTest = new PowerupDeck();

        Assert.assertTrue(powerUpDeckTest.getPowerupList().size() == 24);
    }


    @Test
    public void drawCard(){
        PowerupDeck powerupDeckTest = new PowerupDeck();
        Powerup powerupTest = powerupDeckTest.drawCard();

        Assert.assertTrue(powerupDeckTest.getPowerupList().size() == 23);

        while (powerupDeckTest.getPowerupList().size() != 0){
            powerupTest = powerupDeckTest.drawCard();
        }

        Powerup powerupTestNull = powerupDeckTest.drawCard();
        Assert.assertTrue(powerupTestNull == null);
    }


}