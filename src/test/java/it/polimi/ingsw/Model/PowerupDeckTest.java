package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.CardsPackage.Powerup;
import it.polimi.ingsw.Model.CardsPackage.PowerupDeck;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PowerupDeckTest {

    @Test
    public void pickCard() {
        Powerup p1 = new Powerup();
        Powerup p2 = new Powerup();
        Powerup p3 = new Powerup();
        ArrayList<Powerup> powerupListTest = new ArrayList<>();
        powerupListTest.add(p1);
        powerupListTest.add(p2);
        powerupListTest.add(p3);


        PowerupDeck powerupDeckTest = new PowerupDeck(powerupListTest);
        Powerup powerupToPick = powerupDeckTest.drawCard();

        Assert.assertEquals(2, powerupDeckTest.getPowerupList().size());
    }
}