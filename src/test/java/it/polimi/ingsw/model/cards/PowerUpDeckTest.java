package it.polimi.ingsw.model.cards;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PowerUpDeckTest {

    @Test
    public void verifyjson(){
        PowerUpDeck powerUpDeckTest = new PowerUpDeck();

        Assert.assertTrue(powerUpDeckTest.getPowerUpList().size() == 24);
    }

    @Test
    public void drawCardRegular() {
        PowerUp p1 = new PowerUp();
        PowerUp p2 = new PowerUp();
        PowerUp p3 = new PowerUp();
        ArrayList<PowerUp> powerUpListTest = new ArrayList<>();
        powerUpListTest.add(p1);
        powerUpListTest.add(p2);
        powerUpListTest.add(p3);


        PowerUpDeck powerUpDeckTest = new PowerUpDeck(powerUpListTest);
        PowerUp powerUpToPick = powerUpDeckTest.drawCard();

        Assert.assertTrue(2 == powerUpDeckTest.getPowerUpList().size() && powerUpToPick.equals(p1));
    }

}