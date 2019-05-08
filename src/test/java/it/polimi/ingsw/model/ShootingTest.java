package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.map.MapName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class ShootingTest {
    Game gameTest;

    @Before
    public void setup() {
        ArrayList<String> playersNamesTest = new ArrayList<>();
        playersNamesTest.add("gino");
        playersNamesTest.add("andreaalf");
        playersNamesTest.add("meme");

        gameTest = new Game(playersNamesTest, MapName.FIRE, 6);
    }

    @Test
    public void shootPlayerLockRifleTwoDefenders() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("LockRifle", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);


        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());


    }

    @Test
    public void shootPlayerLockRifleOneDefender() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("LockRifle", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);


        //TODO don't know how to specify when to stop throwing effects

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

    }

    @Test
    public void shootPlayerLockRifleInvalidChoice1() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("LockRifle", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 2, 1);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);


        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        //Testing if meme has the correct marks
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());


    }

    @Test
    public void shootPlayerLockRifleInvalidChoice2() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("LockRifle", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 2, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);


        //in this case, we make andreaalf pay 1 RED even if the attack was wrong
        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        ArrayList<String> testMarks = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testMarks, gameTest.getPlayerByNickname("gino").getMarks());

        ArrayList<String> testMarks1 = new ArrayList<>();

        //Testing if I added the correct marks to meme
        Assert.assertEquals(testMarks1, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerMachineGun2Defenders() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("MachineGun", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 0, 1);
        gameTest.movePlayer("ingConti", 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("gino");


        gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);


        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());


    }

    @Test
    public void shootPlayerMachineGunAllDefenders() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("MachineGun", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 0, 1);
        gameTest.movePlayer("ingConti", 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);


        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());


    }

    @Test
    public void shootPlayerMachineGunAllDefendersNoLast(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("MachineGun", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 0, 1);
        gameTest.movePlayer("ingConti", 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("gino");
        defenders.add("ingConti");

        gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 4);


        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerThorTwoDefenders() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Thor", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

    }

    @Test
    public void shootPlayerThorThreeDefenders() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Thor", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");

        ArrayList<Color> colors = new ArrayList<>();

        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);

        gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 2, 1);
        gameTest.movePlayer("ingConti", 2, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");


        gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        //Testing if I removed the right ammos
        //Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    @Test
    public void shootPlayerHeatseeker() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Heatseeker", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        //Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I removed the right ammos
        //Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());

    }

    @Test
    public void shootPlayerHeatseekerCantShootSomeoneYouSee() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Heatseeker", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 0, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

    }

    @Test
    public void shootPlayerCyberblade() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Cyberblade", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        ArrayList<Color> colors = new ArrayList<>();

        //colors.add(Color.BLUE);

        gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 2, xPos, yPos, "andreaalf");
        //gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, 0, 1, "andreaalf");

        //Testing if I removed the right ammos
        //Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());

    }

    @Test
    public void shootPlayerCyberbladeDisanceExceptionAndCheckingBacupMapAndPlayers() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Cyberblade", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        ArrayList<Color> colors = new ArrayList<>();

        //colors.add(Color.BLUE);

        gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 2, xPos, yPos, "andreaalf");

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());

        //Testing if the position of the offenders is reset
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getyPosition());
    }

    @Test
    public void shootPlayerZX_2_FirstEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("ZX-2", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerZX_2_SecondEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("ZX-2", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");


        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 0, 1);
        gameTest.movePlayer("ingConti", 0, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getMarks());

        ArrayList<String> testArray2 = new ArrayList<>();
        testArray2.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname("ingConti").getMarks());

    }

    @Test
    public void shootPlayerShotgunFirstEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Shotgun", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(0);

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, "gino");

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
    }

    @Test
    public void shootPlayerShotgunSecondEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Shotgun", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 2);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
    }

    @Test
    public void shootPlayerPowerGloveFirstEffectInvalidDistance() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PowerGlove", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xPos, yPos, "andreaalf");

        Assert.assertFalse(result);

    }

    @Test
    public void shootPlayerPowerGloveFirstEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PowerGlove", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xPos, yPos, "andreaalf");

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add("andreaalf");
        testArray1.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("gino").getMarks());
    }

    @Test
    public void shootPlayerPowerGloveSecondEffectInvalidChoiceDistance() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PowerGlove", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 0, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);
        xPos.add(1);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);
        yPos.add(2);

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, "andreaalf");

        Assert.assertFalse(result);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        //Testing if the damages to gino are reset to 0
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getDamages().size());

        //Testing if the position of the offenders is reset
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getyPosition());

    }

    @Test
    public void shootPlayerPowerGloveSecondEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = null;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PowerGlove", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 0, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);
        yPos.add(2);

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, "andreaalf");

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add("andreaalf");
        testArray1.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getDamages());
    }

}
