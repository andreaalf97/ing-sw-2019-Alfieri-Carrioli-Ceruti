package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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