package it.polimi.ingsw.Model.CardsPackage;

import org.junit.Assert;
import org.junit.Test;

public class PowerUpDeckTest {

    @Test
    public void verifyjson(){
        PowerUpDeck powerUpDeckTest = new PowerUpDeck();

        Assert.assertTrue(powerUpDeckTest.getPowerUpList().size() == 24);
    }


    @Test
    public void drawCard(){
        PowerUpDeck powerUpDeckTest = new PowerUpDeck();
        PowerUp powerUpTest = powerUpDeckTest.drawCard();

        Assert.assertTrue(powerUpDeckTest.getPowerUpList().size() == 23);

        while (powerUpDeckTest.getPowerUpList().size() != 0){
            powerUpTest = powerUpDeckTest.drawCard();
        }

        PowerUp powerUpTestNull = powerUpDeckTest.drawCard();
        Assert.assertTrue(powerUpTestNull == null);
    }


}