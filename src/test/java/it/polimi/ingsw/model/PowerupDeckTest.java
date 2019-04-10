package it.polimi.ingsw.model;

import it.polimi.ingsw.model.CardsPackage.Powerup;
import it.polimi.ingsw.model.CardsPackage.PowerupDeck;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class PowerupDeckTest {

    @Test
    void pickCard() {
        PowerupDeck powerupDeckTest = new PowerupDeck();

        powerupDeckTest.getPowerupList().add(new Powerup("TargetingScope"));
        powerupDeckTest.getPowerupList().add(new Powerup("Teleporter"));
        powerupDeckTest.getPowerupList().add(new Powerup("Newton"));


        Powerup powerupToPick = powerupDeckTest.pickCard();

        Assert.assertEquals(2, powerupDeckTest.getPowerupList().size());
    }
}