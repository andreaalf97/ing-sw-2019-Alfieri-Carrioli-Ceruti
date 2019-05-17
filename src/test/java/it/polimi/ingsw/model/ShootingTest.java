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

//TODO ricordarsi che in lanciarazzi (rocketlauncher) bisogna passare solo una volta il primo defender. In fucile al plasma invece (plasmagun) bisogna passare due volte il defender!

public class ShootingTest {
    private Game gameTest;

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

        //Testing if attacking with LockRifle works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
    public void shootPlayerLockRifleSameDefendersException() {

        //Testing if attacking with LockRifle works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        defenders.add("gino");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        //Testing if I didn't removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());


    }

    @Test
    public void shootPlayerLockRifleOneDefender() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
    public void shootPlayerMachineGunSameDefendersException() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        defenders.add("ingConti");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());


    }

    @Test
    public void shootPlayerMachineGunAllDefendersThirdPlayer(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
    public void shootPlayerMachineGunAllDefendersThirdPlayerException(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 4);

        Assert.assertFalse(result);


        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    //TODO come faccio a sparare a solo uno dei giocatori in playersHit???
    /*@Test
    public void shootPlayerMachineGunThreeDamageToAPlayerException() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        defenders.add("gino");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());


    }*/

    @Test
    public void shootPlayerThorTwoDefenders() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
    public void shootPlayerThorInvalidVisibility() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        Assert.assertEquals(3, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 2, 1);
        gameTest.movePlayer("ingConti", 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");


        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(3, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerThorSamePlayersException() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        Assert.assertEquals(3, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 2, 1);
        gameTest.movePlayer("ingConti", 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("gino");
        defenders.add("meme");


        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(3, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerThorFirstAndThirdPlayerEquals() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        Assert.assertEquals(3, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 2, 1);
        gameTest.movePlayer("ingConti", 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("gino");


        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(3, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerPlasmaGunRegular() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PlasmaGun", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("andreaalf");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getyPosition());

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());


    }

    @Test
    public void shootPlayerPlasmaGunNoOptional() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PlasmaGun", weaponsJSON);
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

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("andreaalf");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunInvalidMove() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PlasmaGun", weaponsJSON);
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

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("andreaalf");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getyPosition());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunTryMoveOffender() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PlasmaGun", weaponsJSON);
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
        defenders.add("gino");


        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 2, xArray, yArray, movingPlayers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getyPosition());

        //shouldn't move gino
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getyPosition());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunOneMove() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PlasmaGun", weaponsJSON);
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

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(0);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("andreaalf");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunZeroMove1() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PlasmaGun", weaponsJSON);
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

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(0);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("andreaalf");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunDifferentPlayersException() {

        //Testing if attacking with PlasmaGun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("PlasmaGun", weaponsJSON);
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

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("andreaalf");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movingPlayers);

        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

    }

    @Test
    public void shootPlayerWhisperSameRoom() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Whisper", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 1, 2);
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerWhisperTooClose() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Whisper", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 1, 2);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerWhisperTooCloseDifferentRoom() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Whisper", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 2, 1);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerWhisperVeryFar() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Whisper", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 1, 3);
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerWhisperBehindDoor() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Whisper", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 2, 1);
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerWhisperNotVisible() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Whisper", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 2, 1);
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerElectroscytheFirstEffect() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Electroscythe", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 2, 1);
        gameTest.movePlayer("gino", 2, 1);
        gameTest.movePlayer("meme", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerElectroscytheFirstEffectInvalidChoice() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Electroscythe", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 2, 1);
        gameTest.movePlayer("gino", 2, 1);
        gameTest.movePlayer("meme", 2, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerElectroscytheSecondEffect() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Electroscythe", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 2, 1);
        gameTest.movePlayer("gino", 2, 1);
        gameTest.movePlayer("meme", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerElectroscytheSecondEffectInvalidChoice() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Electroscythe", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 2, 1);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());


        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerElectroscytheSecondEffectOneDefender() {

        //Testing if attacking with Electroscythe works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Electroscythe", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 2, 1);
        gameTest.movePlayer("gino", 2, 1);
        gameTest.movePlayer("meme", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if meme has damage
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());

    }

    @Test
    public void shootPlayerTractorBeamFirstEffect() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("TractorBeam", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerTractorBeamFirstEffectMovingDifferentThanShooting() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("TractorBeam", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("meme");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getyPosition());


        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerTractorBeamFirstEffectShootingANonVisiblePlayer() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("TractorBeam", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerTractorBeamSecondEffect() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("TractorBeam", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerTractorBeamSecondEffectInvalidSpot() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("TractorBeam", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getyPosition());

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerTractorBeamSecondEffectMovingDifferentThanShooting() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("TractorBeam", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 1, 2);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("meme");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());


        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerVortexCannon() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("VortexCannon", weaponsJSON);
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
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 1, 1);
        gameTest.movePlayer("ingConti", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        xArray.add(1);
        yArray.add(1);

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");
        movers.add("meme");
        movers.add("ingConti");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("ingConti").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("ingConti").getyPosition());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    @Test
    public void shootPlayerVortexCannonMainEffectOnly() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("VortexCannon", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 1, 0);
        gameTest.movePlayer("gino", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(2);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());

    }

    @Test
    public void shootPlayerVortexCannonMainEffectOnlyDefenderNotVisible() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("VortexCannon", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 1, 0);
        gameTest.movePlayer("gino", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getyPosition());

    }

    @Test
    public void shootPlayerVortexCannonTwoPlayers() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("VortexCannon", weaponsJSON);
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
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 1, 1);
        gameTest.movePlayer("ingConti", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");
        movers.add("meme");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getyPosition());

        Assert.assertEquals(2, gameTest.getPlayerByNickname("ingConti").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("ingConti").getyPosition());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    @Test
    public void shootPlayerVortexCannonPlayerInitiallyNotVisible() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("VortexCannon", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 2, 2);
        gameTest.movePlayer("gino", 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(3);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(3, gameTest.getPlayerByNickname("gino").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    @Test
    public void shootPlayerVortexCannonSamePlayersException() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("VortexCannon", weaponsJSON);
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
        gameTest.movePlayer("gino", 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    @Test
    public void shootPlayerVortexCannonVortexNonVisibleException() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("VortexCannon", weaponsJSON);
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
        gameTest.movePlayer("meme", 1, 1);
        gameTest.movePlayer("ingConti", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(2);
        yArray.add(1);

        xArray.add(2);
        yArray.add(1);

        xArray.add(2);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");
        movers.add("meme");
        movers.add("ingConti");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getyPosition());

        Assert.assertEquals(2, gameTest.getPlayerByNickname("ingConti").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("ingConti").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    @Test
    public void shootPlayerVortexCannonVortexFarFromDefender() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("VortexCannon", weaponsJSON);
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
        gameTest.movePlayer("gino", 1, 2);
        gameTest.movePlayer("meme", 1, 2);
        gameTest.movePlayer("ingConti", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(0);

        xArray.add(1);
        yArray.add(0);

        xArray.add(1);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");
        movers.add("meme");
        movers.add("ingConti");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("meme").getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("ingConti").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("ingConti").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    @Test
    public void shootPlayerFurnaceDifferentRoom() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Furnace", weaponsJSON);
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
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 1, 1);
        gameTest.movePlayer("ingConti", 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    @Test
    public void shootPlayerFurnaceSameRoomAsOffender() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Furnace", weaponsJSON);
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
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 0, 1);
        gameTest.movePlayer("ingConti", 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

    }

    @Test
    public void shootPlayerFurnaceSecondEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Furnace", weaponsJSON);
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
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 1, 0);
        gameTest.movePlayer("ingConti", 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I'm assigning the correct amount of marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());

    }

    @Test
    public void shootPlayerFurnaceSecondEffectInvalidChoice() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Furnace", weaponsJSON);
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
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 1, 0);
        gameTest.movePlayer("ingConti", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I'm assigning the correct amount of marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());

    }

    //TODO NOT WORKS, GINO IS TWICE IN THE DEFENDERS LIST AND I HIT HIM TWICE
    @Test
    public void shootPlayerFurnaceSecondEffectSamePlayersInTheDefenderList() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Furnace", weaponsJSON);
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
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 1, 0);
        gameTest.movePlayer("ingConti", 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I'm assigning the correct amount of marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());

    }

    @Test
    public void shootPlayerFurnaceSecondEffectCheckIfICanShootToAllPlayersInTheSpot() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Furnace", weaponsJSON);
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
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 1, 0);
        gameTest.movePlayer("ingConti", 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I'm assigning the correct amount of marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());

    }

    @Test
    public void shootPlayerHeatseeker() {

        //Testing if attacking with Heatseeker works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
    public void shootPlayerHeatseekerTwoPlayersException() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Heatseeker", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 2, 1);
        gameTest.movePlayer("meme", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);
    }

    @Test
    public void shootPlayerHellionThreeDefendersSameSpot(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Hellion", weaponsJSON);
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

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 1, 1);
        gameTest.movePlayer("ingConti", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());

        Assert.assertFalse(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionThreeDefendersOnlyOneValid(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Hellion", weaponsJSON);
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

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 1, 2);
        gameTest.movePlayer("ingConti", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());

        Assert.assertFalse(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionSecondOrder(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Hellion", weaponsJSON);
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

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 1, 1);
        gameTest.movePlayer("ingConti", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add("andreaalf");
        testArray1.add("andreaalf");


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("ingConti").getMarks());

        Assert.assertFalse(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionThreeDefendersOnlyFirstValid(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Hellion", weaponsJSON);
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

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 1, 2);
        gameTest.movePlayer("ingConti", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());

        Assert.assertTrue(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionInMapThereAreMorePlayersThanTheUserPassesFirstOrder(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Hellion", weaponsJSON);
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

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 1, 1);
        gameTest.movePlayer("ingConti", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());

        Assert.assertTrue(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionInMapThereAreMorePlayersThanTheUserPassesSecondOrder(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Hellion", weaponsJSON);
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

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 1, 1);
        gameTest.movePlayer("ingConti", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getMarks());

        Assert.assertTrue(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerFirstEffectTwoDefenders(){

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Flamethrower", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 1, 1);
        gameTest.movePlayer("gino", 1, 2);
        gameTest.movePlayer("meme", 1, 3);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        Assert.assertFalse(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerFirstEffectTwoDefendersOnlyOneValid(){

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Flamethrower", weaponsJSON);
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
        gameTest.movePlayer("meme", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        Assert.assertTrue(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerFirstEffectThreeDefendersOneNotConsidered(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Flamethrower", weaponsJSON);
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

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 1, 1);
        gameTest.movePlayer("gino", 1, 2);
        gameTest.movePlayer("meme", 1, 3);
        gameTest.movePlayer("ingConti", 1, 3);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("ingConti").getDamages());

        Assert.assertTrue(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerPlayersNotLinear(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Flamethrower", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 1, 1);
        gameTest.movePlayer("gino", 1, 2);
        gameTest.movePlayer("meme", 2, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        Assert.assertTrue(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerPlayersInTheSameSpot(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Flamethrower", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 1, 1);
        gameTest.movePlayer("gino", 1, 2);
        gameTest.movePlayer("meme", 1, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        Assert.assertTrue(gameTest.getPlayerByNickname("andreaalf").getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerSecondEffectThreeDefenders(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");
        players.add("keny");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Flamethrower", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");
        gameTest.revive("keny");


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 0, 1);
        gameTest.movePlayer("ingConti", 0, 2);
        gameTest.movePlayer("keny", 0, 2);

        ArrayList<Color> colorsAmmo = new ArrayList<>();
        colorsAmmo.add(Color.YELLOW);
        gameTest.getPlayerByNickname("andreaalf").giveAmmos(colorsAmmo);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");
        defenders.add("keny");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add("andreaalf");
        testArray1.add("andreaalf");

        ArrayList<String> testArray2 = new ArrayList<>();
        testArray2.add("andreaalf");

        ArrayList<String> testArray3 = new ArrayList<>();
        testArray3.add("andreaalf");



        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct damages to ingConti
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct damages to keny
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname("keny").getDamages());

        //checking ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());

    }

    @Test
    public void shootPlayerFlamethrowerSecondEffectTheeDefendersExceptionWall(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");
        players.add("keny");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Flamethrower", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");
        gameTest.revive("keny");


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 2, 1);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 1, 1);
        gameTest.movePlayer("ingConti", 1, 1);
        gameTest.movePlayer("keny", 0, 1);

        ArrayList<Color> colorsAmmo = new ArrayList<>();
        colorsAmmo.add(Color.YELLOW);
        gameTest.getPlayerByNickname("andreaalf").giveAmmos(colorsAmmo);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");
        defenders.add("keny");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();


        ArrayList<String> testArray1 = new ArrayList<>();


        ArrayList<String> testArray2 = new ArrayList<>();


        ArrayList<String> testArray3 = new ArrayList<>();


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname("keny").getDamages());

    }

    @Test
    public void shootPlayerFlamethrowerSecondEffectTheeDefendersExceptionCorner(){

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");
        players.add("keny");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Flamethrower", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");
        gameTest.revive("keny");


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 2, 2);
        gameTest.movePlayer("gino", 2, 3);
        gameTest.movePlayer("meme", 2, 3);
        gameTest.movePlayer("ingConti", 2, 3);
        gameTest.movePlayer("keny", 1, 3);

        ArrayList<Color> colorsAmmo = new ArrayList<>();
        colorsAmmo.add(Color.YELLOW);
        gameTest.getPlayerByNickname("andreaalf").giveAmmos(colorsAmmo);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");
        defenders.add("keny");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();


        ArrayList<String> testArray1 = new ArrayList<>();


        ArrayList<String> testArray2 = new ArrayList<>();


        ArrayList<String> testArray3 = new ArrayList<>();


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname("ingConti").getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname("keny").getDamages());

    }

    @Test
    public void shootPlayerGrenadeLauncherFirstEffectFirstAttack(){

        //Testing if attacking with GrenadeLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("GrenadeLauncher", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 2, 3);
        gameTest.movePlayer("gino", 1, 3);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(2);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerGrenadeLauncherFirstEffectFirstMoveOtherPlayer(){

        //Testing if attacking with GrenadeLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("GrenadeLauncher", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 2, 3);
        gameTest.movePlayer("gino", 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(3);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(3, gameTest.getPlayerByNickname("gino").getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());

    }

    @Test
    public void shootPlayerGrenadeLauncherSecondEffectTwoDefenders(){

        //Testing if attacking with GrenadeLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("GrenadeLauncher", weaponsJSON);
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
        gameTest.movePlayer("meme", 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("gino");
        defenders.add("meme");


        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("meme");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());


        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getyPosition());


    }

    @Test
    public void shootPlayerGrenadeLauncherSecondEffectAllInTheTargetSpotfromTheBeginning(){

        //Testing if attacking with GrenadeLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("GrenadeLauncher", weaponsJSON);
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
        gameTest.movePlayer("meme", 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("meme");
        defenders.add("gino");
        defenders.add("meme");

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add("meme");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 2, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();
        testArray.add("andreaalf");

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());


        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("meme").getyPosition());


    }

    @Test
    public void shootPlayerRocketLauncherFirstEffect() {

        //Testing if attacking with RocketLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("RocketLauncher", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("gino");

        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

    }

    @Test
    public void shootPlayerRocketLauncherSecondEffect() {

        //Testing if attacking with RocketLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("RocketLauncher", weaponsJSON);
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
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("gino");
        movingPlayers.add("andreaalf");


        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getDamages());


        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getyPosition());


    }

    @Test
    public void shootPlayerRocketLauncherSecondEffectFourDefendersException() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");
        players.add("keny");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with RocketLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("RocketLauncher", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");
        gameTest.revive("keny");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 0, 1);
        gameTest.movePlayer("ingConti", 0, 1);
        gameTest.movePlayer("keny", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");
        defenders.add("keny");

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("gino");
        movingPlayers.add("andreaalf");


        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xArray, yArray, movingPlayers);
        Assert.assertFalse(b);

    }

    @Test
    public void shootPlayerRocketLauncherSecondEffectFourDefenders() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");
        players.add("keny");


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with RocketLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("RocketLauncher", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");
        gameTest.revive("keny");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 0, 1);
        gameTest.movePlayer("ingConti", 0, 1);
        gameTest.movePlayer("keny", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");
        defenders.add("keny");

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add("gino");
        movingPlayers.add("andreaalf");


        boolean b = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getDamages());

        ArrayList<String> testArray2 = new ArrayList<>();
        testArray2.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname("ingConti").getDamages());

        ArrayList<String> testArray3 = new ArrayList<>();
        testArray3.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname("keny").getDamages());


        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getyPosition());

    }

    @Test
    public void shootPlayerRailgunFirstEffect(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Railgun", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 1, 0);
        gameTest.movePlayer("gino", 1, 3);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
    }

    @Test
    public void shootPlayerRailgunFirstEffectShootThroughAWall(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Railgun", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 1, 1);
        gameTest.movePlayer("gino", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
    }

    @Test
    public void shootPlayerRailgunFirstEffectNotValidPositionOfDefender(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Railgun", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
    }

    @Test
    public void shootPlayerRailgunSecondEffectTwoDefendersThroughAWall(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Railgun", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
    }

    @Test
    public void shootPlayerRailgunSecondEffectTwoDefendersOneNotValid(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Railgun", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 1, 1);
        gameTest.movePlayer("meme", 0, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean b = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getMarks());
    }

    @Test
    public void shootPlayerCyberblade() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("andreaalf");

        gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xPos, yPos, playerWhoMove);
        //gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, 0, 1, "andreaalf");

        //Testing if I removed the right ammos
        //Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());

    }

    @Test
    public void shootPlayerCyberbladeSecondOrder() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        gameTest.movePlayer("gino", 0, 1);
        gameTest.movePlayer("meme", 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("andreaalf");

        gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, playerWhoMove);
        //gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, 0, 1, "andreaalf");

        //Testing if I removed the right ammos
        //Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());

    }

    @Test
    public void shootPlayerCyberbladeDistanceExceptionAndCheckingBackupMapAndPlayers() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("andreaalf");

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

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

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        gameTest.movePlayer("gino", 0, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("gino");

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xPos, yPos, playerWhoMove);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        Assert.assertEquals(0,  gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(1,  gameTest.getPlayerByNickname("gino").getyPosition());
    }

    @Test
    public void shootPlayerShotgunSecondEffect() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());
    }

    @Test
    public void shootPlayerPowerGloveFirstEffectInvalidDistance() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("andreaalf");

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

    }

    @Test
    public void shootPlayerPowerGloveFirstEffect() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("andreaalf");

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 0, xPos, yPos, playerWhoMove);

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

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("andreaalf");
        playerWhoMove.add("andreaalf");

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

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

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("andreaalf");
        playerWhoMove.add("andreaalf");


        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

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

    @Test
    public void shootPlayerPowerGloveSecondEffectNonLinear() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
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
        gameTest.movePlayer("gino", 1, 0);
        gameTest.movePlayer("meme", 1, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(1);
        xPos.add(1);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(0);
        yPos.add(1);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("andreaalf");
        playerWhoMove.add("andreaalf");


        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("meme").getDamages());
    }

    @Test
    public void shootPlayerShockwaveFirstEffectMustBeDifferentSpotsException() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");
        players.add("keny");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Shockwave", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");
        gameTest.revive("keny");


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
        gameTest.movePlayer("keny", 0, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");
        defenders.add("keny");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        /*ArrayList<String> testArray = new ArrayList<>();
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
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname("ingConti").getMarks());*/

    }

    @Test
    public void shootPlayerShockwaveFirstEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");
        players.add("keny");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Shockwave", weaponsJSON);
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
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("meme", 0, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getDamages());

    }

    @Test
    public void shootPlayerShockwaveSecondEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add("andreaalf");
        players.add("gino");
        players.add("meme");
        players.add("ingConti");
        players.add("keny");

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Shockwave", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");
        gameTest.revive("meme");
        gameTest.revive("ingConti");
        gameTest.revive("keny");


        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname("andreaalf").giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 1);
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("meme", 0, 0);
        gameTest.movePlayer("ingConti", 0, 0);
        gameTest.movePlayer("keny", 0, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");
        defenders.add("ingConti");
        defenders.add("keny");

        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add("andreaalf");

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname("meme").getDamages());

        ArrayList<String> testArray2 = new ArrayList<>();
        testArray2.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname("ingConti").getDamages());

        ArrayList<String> testArray3 = new ArrayList<>();
        testArray3.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname("keny").getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnYellowAmmo());

    }

    @Test
    public void shootPlayerSledgeHammerFirstEffectRegular() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Sledgehammer", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 0);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");


        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

    }

    @Test
    public void shootPlayerSledgeHammerFirstEffectMinDistanceException() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Sledgehammer", weaponsJSON);
        } catch (FileNotFoundException e) {
            Assert.fail();
            return;
        }

        //Players are dead by default so I need to revive them
        gameTest.revive("andreaalf");
        gameTest.revive("gino");


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer("andreaalf", weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer("andreaalf", 0, 0);
        gameTest.movePlayer("gino", 0, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");


        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

    }

    @Test
    public void shootPlayerSledgeHammerFirstEffectTooManyPlayersException() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Sledgehammer", weaponsJSON);
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
        gameTest.movePlayer("gino", 0, 0);
        gameTest.movePlayer("meme", 0, 0);



        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add("gino");
        defenders.add("meme");


        boolean result = gameTest.shootWithoutMovement("andreaalf", defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());

    }

    @Test
    public void shootPlayerSledgeHammerSecondEffectRegular() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Sledgehammer", weaponsJSON);
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
        yPos.add(2);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("gino");


        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());


        //Testing gino is in the right spot
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname("gino").getyPosition());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());
    }

    @Test
    public void shootPlayerSledgeHammerSecondEffectTooManyMoves() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Sledgehammer", weaponsJSON);
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
        xPos.add(1);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(2);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("gino");


        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());


        //Testing gino is in the right spot
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());
    }

    @Test
    public void shootPlayerSledgeHammerSecondEffectZeroMoves() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Sledgehammer", weaponsJSON);
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

        ArrayList<Integer> yPos = new ArrayList<>();

        ArrayList<String> playerWhoMove = new ArrayList<>();

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add("andreaalf");
        testArray.add("andreaalf");
        testArray.add("andreaalf");


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());


        //Testing gino is in the right spot
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getyPosition());

        Assert.assertEquals(0, gameTest.getPlayerByNickname("andreaalf").getnRedAmmo());
    }

    @Test
    public void shootPlayerSledgeHammerSecondEffectNonLinear() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest;
        try {
            JsonObject weaponsJSON = new JsonParser().parse(new FileReader("src/main/resources/effects.json")).getAsJsonObject().get("Weapons").getAsJsonObject();
            weaponTest = new Weapon("Sledgehammer", weaponsJSON);
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
        xPos.add(1);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add("gino");

        boolean result = gameTest.shootWithMovement("andreaalf", defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname("gino").getDamages());


        //Testing gino is in the right spot
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname("gino").getyPosition());

    }
}