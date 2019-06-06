package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.JsonDeserializer;
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
    
    public static final String playerGino = "gino";
    public static final String playerMeme = "meme";
    public static final String playerAndreaalf = "andreaalf";
    public static final String playerIngConti = "ingConti";
    public static final String playerKeny = "keny";
    private Game gameTest;

    @Before
    public void setup() {
        ArrayList<String> playersNamesTest = new ArrayList<>();
        playersNamesTest.add(playerGino);
        playersNamesTest.add(playerAndreaalf);
        playersNamesTest.add(playerMeme);

        gameTest = new Game(playersNamesTest, MapName.FIRE, 6);
    }

    @Test
    public void shootPlayerLockRifleTwoDefenders() {

        //Testing if attacking with LockRifle works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!

        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("LockRifle");


        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);


        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());


    }

    @Test
    public void shootPlayerLockRifleSameDefendersException() {

        //Testing if attacking with LockRifle works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("LockRifle");


        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerGino);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        //Testing if I didn't removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());


    }

    @Test
    public void shootPlayerLockRifleOneDefender() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("LockRifle");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

    }

    @Test
    public void shootPlayerLockRifleInvalidChoice1() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("LockRifle");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 2, 1);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);


        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        //Testing if meme has the correct marks
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());


    }

    @Test
    public void shootPlayerLockRifleInvalidChoice2() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("LockRifle");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 2, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);


        //in this case, we make andreaalf pay 1 RED even if the attack was wrong
        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        ArrayList<String> testMarks = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testMarks, gameTest.getPlayerByNickname(playerGino).getMarks());

        ArrayList<String> testMarks1 = new ArrayList<>();

        //Testing if I added the correct marks to meme
        Assert.assertEquals(testMarks1, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerMachineGun2Defenders() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("MachineGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerGino);


        gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);


        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());


    }

    @Test
    public void shootPlayerMachineGunAllDefenders() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("MachineGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);


        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());


    }

    @Test
    public void shootPlayerMachineGunSameDefendersException() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("MachineGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());


    }

    @Test
    public void shootPlayerMachineGunAllDefendersThirdPlayer(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("MachineGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerGino);
        defenders.add(playerIngConti);

        gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 4);


        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerMachineGunAllDefendersThirdPlayerException(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("MachineGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 4);

        Assert.assertFalse(result);


        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    //TODO come faccio a sparare a solo uno dei giocatori in playersHit???
    /*
    @Test
    public void shootPlayerMachineGunThreeDamageToAPlayerException() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("MachineGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerGino);
        defenders.add(playerGino);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());


    }*/

    @Test
    public void shootPlayerThorTwoDefenders() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Thor");


        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

    }

    @Test
    public void shootPlayerThorThreeDefenders() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Thor");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        ArrayList<Color> colors = new ArrayList<>();

        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);

        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 2, 1);
        gameTest.movePlayer(playerIngConti, 2, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);


        gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        //Testing if I removed the right ammos
        //Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    @Test
    public void shootPlayerThorInvalidVisibility() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Thor");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        ArrayList<Color> colors = new ArrayList<>();

        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);

        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        Assert.assertEquals(3, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 2, 1);
        gameTest.movePlayer(playerIngConti, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);


        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(3, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerThorSamePlayersException() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Thor");


        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        ArrayList<Color> colors = new ArrayList<>();

        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);

        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        Assert.assertEquals(3, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 2, 1);
        gameTest.movePlayer(playerIngConti, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerGino);
        defenders.add(playerMeme);


        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(3, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerThorFirstAndThirdPlayerEquals() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Thor");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        ArrayList<Color> colors = new ArrayList<>();

        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);

        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        Assert.assertEquals(3, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 2, 1);
        gameTest.movePlayer(playerIngConti, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerGino);


        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(3, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerPlasmaGunRegular() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PlasmaGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerAndreaalf);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());


    }

    @Test
    public void shootPlayerPlasmaGunNoOptional() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PlasmaGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerAndreaalf);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunInvalidMove() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PlasmaGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerAndreaalf);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunTryMoveOffender() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PlasmaGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerGino);


        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 2, xArray, yArray, movingPlayers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());

        //shouldn't move gino
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getyPosition());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunOneMove() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PlasmaGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(0);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerAndreaalf);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunZeroMove1() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PlasmaGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(0);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerAndreaalf);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

    }

    @Test
    public void shootPlayerPlasmaGunDifferentPlayersException() {

        //Testing if attacking with PlasmaGun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PlasmaGun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerAndreaalf);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movingPlayers);

        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

    }

    @Test
    public void shootPlayerWhisperSameRoom() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Whisper");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 2);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerWhisperTooClose() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Whisper");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 2);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerWhisperTooCloseDifferentRoom() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Whisper");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 1);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerWhisperVeryFar() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Whisper");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 3);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerWhisperBehindDoor() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Whisper");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 1);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerWhisperNotVisible() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Whisper");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 1);
        gameTest.movePlayer(playerGino, 0, 0);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct marks to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerElectroscytheFirstEffect() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Electroscythe");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 1);
        gameTest.movePlayer(playerGino, 2, 1);
        gameTest.movePlayer(playerMeme, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerElectroscytheFirstEffectInvalidChoice() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Electroscythe");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 1);
        gameTest.movePlayer(playerGino, 2, 1);
        gameTest.movePlayer(playerMeme, 2, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerElectroscytheSecondEffect() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Electroscythe");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 1);
        gameTest.movePlayer(playerGino, 2, 1);
        gameTest.movePlayer(playerMeme, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerElectroscytheSecondEffectInvalidChoice() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Electroscythe");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 1);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());


        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerElectroscytheSecondEffectOneDefender() {

        //Testing if attacking with Electroscythe works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Electroscythe");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 1);
        gameTest.movePlayer(playerGino, 2, 1);
        gameTest.movePlayer(playerMeme, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if meme has damage
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());

    }

    @Test
    public void shootPlayerTractorBeamFirstEffect() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("TractorBeam");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerTractorBeamFirstEffectMovingDifferentThanShooting() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("TractorBeam");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerMeme);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getyPosition());


        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerTractorBeamFirstEffectShootingANonVisiblePlayer() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("TractorBeam");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerTractorBeamSecondEffect() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("TractorBeam");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerTractorBeamSecondEffectInvalidSpot() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("TractorBeam");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getyPosition());

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerTractorBeamSecondEffectMovingDifferentThanShooting() {

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("TractorBeam");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 1, 2);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerMeme);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(0);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerGino).getyPosition());


        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerVortexCannon() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("VortexCannon");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 1, 1);
        gameTest.movePlayer(playerIngConti, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        xArray.add(1);
        yArray.add(1);

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);
        movers.add(playerMeme);
        movers.add(playerIngConti);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerIngConti).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerIngConti).getyPosition());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    @Test
    public void shootPlayerVortexCannonMainEffectOnly() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("VortexCannon");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 0);
        gameTest.movePlayer(playerGino, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(2);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerGino).getyPosition());

    }

    @Test
    public void shootPlayerVortexCannonMainEffectOnlyDefenderNotVisible() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("VortexCannon");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 0);
        gameTest.movePlayer(playerGino, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getyPosition());

    }

    @Test
    public void shootPlayerVortexCannonTwoPlayers() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("VortexCannon");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 1, 1);
        gameTest.movePlayer(playerIngConti, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);
        movers.add(playerMeme);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getyPosition());

        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerIngConti).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerIngConti).getyPosition());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    @Test
    public void shootPlayerVortexCannonPlayerInitiallyNotVisible() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("VortexCannon");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 2);
        gameTest.movePlayer(playerGino, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(3);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(3, gameTest.getPlayerByNickname(playerGino).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    @Test
    public void shootPlayerVortexCannonSamePlayersException() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("VortexCannon");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerGino).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    @Test
    public void shootPlayerVortexCannonVortexNonVisibleException() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("VortexCannon");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 1, 1);
        gameTest.movePlayer(playerIngConti, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(2);
        yArray.add(1);

        xArray.add(2);
        yArray.add(1);

        xArray.add(2);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);
        movers.add(playerMeme);
        movers.add(playerIngConti);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getyPosition());

        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerIngConti).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerIngConti).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    @Test
    public void shootPlayerVortexCannonVortexFarFromDefender() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("VortexCannon");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 2);
        gameTest.movePlayer(playerMeme, 1, 2);
        gameTest.movePlayer(playerIngConti, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(0);

        xArray.add(1);
        yArray.add(0);

        xArray.add(1);
        yArray.add(0);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);
        movers.add(playerMeme);
        movers.add(playerIngConti);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerGino).getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerMeme).getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerIngConti).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerIngConti).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    @Test
    public void shootPlayerFurnaceDifferentRoom() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Furnace");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 1, 1);
        gameTest.movePlayer(playerIngConti, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    @Test
    public void shootPlayerFurnaceSameRoomAsOffender() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Furnace");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 0, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

    }

    @Test
    public void shootPlayerFurnaceSecondEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Furnace");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 1, 0);
        gameTest.movePlayer(playerIngConti, 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I'm assigning the correct amount of marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());

    }

    @Test
    public void shootPlayerFurnaceSecondEffectInvalidChoice() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Furnace");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 1, 0);
        gameTest.movePlayer(playerIngConti, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I'm assigning the correct amount of marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());

    }

    @Test
    public void shootPlayerFurnaceSecondEffectSamePlayersInTheDefenderList() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Furnace");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 1, 0);
        gameTest.movePlayer(playerIngConti, 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I'm assigning the correct amount of marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());

    }

    @Test
    public void shootPlayerFurnaceSecondEffectCheckIfICanShootToAllPlayersInTheSpot() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Furnace");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 1, 0);
        gameTest.movePlayer(playerIngConti, 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I'm assigning the correct amount of marks to everyone
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());

    }

    @Test
    public void shootPlayerHeatseeker() {

        //Testing if attacking with Heatseeker works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Heatseeker");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        //Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I removed the right ammos
        //Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());

    }

    @Test
    public void shootPlayerHeatseekerCantShootSomeoneYouSee() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Heatseeker");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 0, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

    }

    @Test
    public void shootPlayerHeatseekerTwoPlayersException() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Heatseeker");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 2, 1);
        gameTest.movePlayer(playerMeme, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);
    }

    @Test
    public void shootPlayerHellionThreeDefendersSameSpot(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Hellion");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 1, 1);
        gameTest.movePlayer(playerIngConti, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());

        Assert.assertFalse(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionThreeDefendersOnlyOneValid(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Hellion");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 1, 2);
        gameTest.movePlayer(playerIngConti, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());

        Assert.assertFalse(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionSecondOrder(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Hellion");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 1, 1);
        gameTest.movePlayer(playerIngConti, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add(playerAndreaalf);
        testArray1.add(playerAndreaalf);


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertNotEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerIngConti).getMarks());

        Assert.assertFalse(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionThreeDefendersOnlyFirstValid(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Hellion");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 1, 2);
        gameTest.movePlayer(playerIngConti, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());

        Assert.assertTrue(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionInMapThereAreMorePlayersThanTheUserPassesFirstOrder(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Hellion");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 1, 1);
        gameTest.movePlayer(playerIngConti, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());

        Assert.assertTrue(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerHellionInMapThereAreMorePlayersThanTheUserPassesSecondOrder(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Hellion works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Hellion");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 1, 1);
        gameTest.movePlayer(playerIngConti, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct marks to gino, meme and ingConti
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getMarks());

        Assert.assertTrue(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerFirstEffectTwoDefenders(){

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Flamethrower");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 1);
        gameTest.movePlayer(playerGino, 1, 2);
        gameTest.movePlayer(playerMeme, 1, 3);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        Assert.assertFalse(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerFirstEffectTwoDefendersOnlyOneValid(){

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Flamethrower");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        Assert.assertTrue(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerFirstEffectThreeDefendersOneNotConsidered(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Flamethrower");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 1);
        gameTest.movePlayer(playerGino, 1, 2);
        gameTest.movePlayer(playerMeme, 1, 3);
        gameTest.movePlayer(playerIngConti, 1, 3);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        Assert.assertTrue(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerPlayersNotLinear(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Flamethrower");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 1);
        gameTest.movePlayer(playerGino, 1, 2);
        gameTest.movePlayer(playerMeme, 2, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        Assert.assertTrue(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerPlayersInTheSameSpot(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Flamethrower");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 1);
        gameTest.movePlayer(playerGino, 1, 2);
        gameTest.movePlayer(playerMeme, 1, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        Assert.assertTrue(gameTest.getPlayerByNickname(playerAndreaalf).getWeaponList().get(0).isLoaded());
    }

    @Test
    public void shootPlayerFlamethrowerSecondEffectThreeDefenders(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);
        players.add(playerKeny);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Flamethrower");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);
        gameTest.revive(playerKeny);


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 0, 2);
        gameTest.movePlayer(playerKeny, 0, 2);

        ArrayList<Color> colorsAmmo = new ArrayList<>();
        colorsAmmo.add(Color.YELLOW);
        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colorsAmmo);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);
        defenders.add(playerKeny);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add(playerAndreaalf);
        testArray1.add(playerAndreaalf);

        ArrayList<String> testArray2 = new ArrayList<>();
        testArray2.add(playerAndreaalf);

        ArrayList<String> testArray3 = new ArrayList<>();
        testArray3.add(playerAndreaalf);



        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct damages to ingConti
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct damages to keny
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname(playerKeny).getDamages());

        //checking ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());

    }

    @Test
    public void shootPlayerFlamethrowerSecondEffectTheeDefendersExceptionWall(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);
        players.add(playerKeny);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Flamethrower");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);
        gameTest.revive(playerKeny);


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 1);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 1, 1);
        gameTest.movePlayer(playerIngConti, 1, 1);
        gameTest.movePlayer(playerKeny, 0, 1);

        ArrayList<Color> colorsAmmo = new ArrayList<>();
        colorsAmmo.add(Color.YELLOW);
        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colorsAmmo);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);
        defenders.add(playerKeny);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();


        ArrayList<String> testArray1 = new ArrayList<>();


        ArrayList<String> testArray2 = new ArrayList<>();


        ArrayList<String> testArray3 = new ArrayList<>();


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname(playerKeny).getDamages());

    }

    @Test
    public void shootPlayerFlamethrowerSecondEffectTheeDefendersExceptionCorner(){

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);
        players.add(playerKeny);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Flamethrower works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Flamethrower");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);
        gameTest.revive(playerKeny);


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 2);
        gameTest.movePlayer(playerGino, 2, 3);
        gameTest.movePlayer(playerMeme, 2, 3);
        gameTest.movePlayer(playerIngConti, 2, 3);
        gameTest.movePlayer(playerKeny, 1, 3);

        ArrayList<Color> colorsAmmo = new ArrayList<>();
        colorsAmmo.add(Color.YELLOW);
        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colorsAmmo);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);
        defenders.add(playerKeny);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();


        ArrayList<String> testArray1 = new ArrayList<>();


        ArrayList<String> testArray2 = new ArrayList<>();


        ArrayList<String> testArray3 = new ArrayList<>();


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname(playerKeny).getDamages());

    }

    @Test
    public void shootPlayerGrenadeLauncherFirstEffectFirstAttack(){

        //Testing if attacking with GrenadeLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("GrenadeLauncher");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 3);
        gameTest.movePlayer(playerGino, 1, 3);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(2);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerGino).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerGrenadeLauncherFirstEffectFirstMoveOtherPlayer(){

        //Testing if attacking with GrenadeLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("GrenadeLauncher");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 2, 3);
        gameTest.movePlayer(playerGino, 1, 2);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(3);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(3, gameTest.getPlayerByNickname(playerGino).getyPosition());

        testArray = new ArrayList<>();

        //Testing if I added the correct marks to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerGrenadeLauncherSecondEffectTwoDefenders(){

        //Testing if attacking with GrenadeLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("GrenadeLauncher");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerGino);
        defenders.add(playerMeme);


        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerMeme);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());


        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getyPosition());


    }

    @Test
    public void shootPlayerGrenadeLauncherSecondEffectAllInTheTargetSpotfromTheBeginning(){

        //Testing if attacking with GrenadeLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("GrenadeLauncher");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 1, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerMeme);
        defenders.add(playerGino);
        defenders.add(playerMeme);

        ArrayList<Integer> xArray = new ArrayList<>();
        ArrayList<Integer> yArray = new ArrayList<>();

        xArray.add(1);
        yArray.add(1);

        ArrayList<String> movers = new ArrayList<>();
        movers.add(playerMeme);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 2, xArray, yArray, movers);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino and meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());


        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerMeme).getyPosition());


    }

    @Test
    public void shootPlayerRocketLauncherFirstEffect() {

        //Testing if attacking with RocketLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("RocketLauncher");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerGino);

        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerGino).getyPosition());

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

    }

    @Test
    public void shootPlayerRocketLauncherSecondEffect() {

        //Testing if attacking with RocketLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("RocketLauncher");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerGino);
        movingPlayers.add(playerAndreaalf);


        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add(playerAndreaalf);

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getDamages());


        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerGino).getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());


    }

    @Test
    public void shootPlayerRocketLauncherSecondEffectFourDefendersException() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);
        players.add(playerKeny);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with RocketLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("RocketLauncher");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);
        gameTest.revive(playerKeny);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 0, 1);
        gameTest.movePlayer(playerKeny, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);
        defenders.add(playerKeny);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerGino);
        movingPlayers.add(playerAndreaalf);


        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xArray, yArray, movingPlayers);
        Assert.assertFalse(b);

    }

    @Test
    public void shootPlayerRocketLauncherSecondEffectFourDefenders() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);
        players.add(playerKeny);


        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with RocketLauncher works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("RocketLauncher");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);
        gameTest.revive(playerKeny);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 0, 1);
        gameTest.movePlayer(playerKeny, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);
        defenders.add(playerKeny);

        ArrayList<Integer> xArray = new ArrayList<>();
        xArray.add(0);
        xArray.add(1);

        ArrayList<Integer> yArray = new ArrayList<>();
        yArray.add(2);
        yArray.add(1);

        ArrayList<String> movingPlayers = new ArrayList<>();
        movingPlayers.add(playerGino);
        movingPlayers.add(playerAndreaalf);


        boolean b = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xArray, yArray, movingPlayers);
        Assert.assertTrue(b);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add(playerAndreaalf);

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getDamages());

        ArrayList<String> testArray2 = new ArrayList<>();
        testArray2.add(playerAndreaalf);

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        ArrayList<String> testArray3 = new ArrayList<>();
        testArray3.add(playerAndreaalf);

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname(playerKeny).getDamages());


        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerGino).getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());

    }

    @Test
    public void shootPlayerRailgunFirstEffect(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Railgun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 0);
        gameTest.movePlayer(playerGino, 1, 3);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
    }

    @Test
    public void shootPlayerRailgunFirstEffectShootThroughAWall(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Railgun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 1, 1);
        gameTest.movePlayer(playerGino, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
    }

    @Test
    public void shootPlayerRailgunFirstEffectNotValidPositionOfDefender(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Railgun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
    }

    @Test
    public void shootPlayerRailgunSecondEffectTwoDefendersThroughAWall(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Railgun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 2, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);
        Assert.assertTrue(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        testArray = new ArrayList<>();

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
    }

    @Test
    public void shootPlayerRailgunSecondEffectTwoDefendersOneNotValid(){

        //Testing if attacking with Railgun works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Railgun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);
        //Testing if this player receives a drawn weapon from the deck

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 1, 1);
        gameTest.movePlayer(playerMeme, 0, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean b = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);
        Assert.assertFalse(b);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getMarks());
    }

    @Test
    public void shootPlayerCyberblade() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Cyberblade");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        ArrayList<Color> colors = new ArrayList<>();

        //colors.add(Color.BLUE);

        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 0);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerAndreaalf);

        gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xPos, yPos, playerWhoMove);
        //gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, 0, 1, playerAndreaalf);

        //Testing if I removed the right ammos
        //Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());

    }

    @Test
    public void shootPlayerCyberbladeSecondOrder() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Cyberblade");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        ArrayList<Color> colors = new ArrayList<>();

        //colors.add(Color.BLUE);

        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerAndreaalf);

        gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xPos, yPos, playerWhoMove);
        //gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, 0, 1, playerAndreaalf);

        //Testing if I removed the right ammos
        //Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I removed the right ammos
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());

    }

    @Test
    public void shootPlayerCyberbladeDistanceExceptionAndCheckingBackupMapAndPlayers() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Cyberblade");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        ArrayList<Color> colors = new ArrayList<>();

        //colors.add(Color.BLUE);

        gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 0, 0);
        gameTest.movePlayer(playerMeme, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerAndreaalf);

        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());

        //Testing if the position of the offenders is reset
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());
    }

    @Test
    public void shootPlayerZX_2_FirstEffect() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("ZX-2");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

    }

    @Test
    public void shootPlayerZX_2_SecondEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("ZX-2");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);


        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 0, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getMarks());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getMarks());

        ArrayList<String> testArray2 = new ArrayList<>();
        testArray2.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname(playerIngConti).getMarks());

    }

    @Test
    public void shootPlayerShotgunFirstEffect() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Shotgun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerGino);

        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xPos, yPos, playerWhoMove);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        Assert.assertEquals(0,  gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(1,  gameTest.getPlayerByNickname(playerGino).getyPosition());
    }

    @Test
    public void shootPlayerShotgunSecondEffect() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Shotgun");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());
    }

    @Test
    public void shootPlayerPowerGloveFirstEffectInvalidDistance() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PowerGlove");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 0);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerAndreaalf);

        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

    }

    @Test
    public void shootPlayerPowerGloveFirstEffect() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PowerGlove");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);

        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerAndreaalf);

        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 0, xPos, yPos, playerWhoMove);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add(playerAndreaalf);
        testArray1.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerGino).getMarks());
    }

    @Test
    public void shootPlayerPowerGloveSecondEffectInvalidChoiceDistance() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PowerGlove");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);
        xPos.add(1);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);
        yPos.add(2);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerAndreaalf);
        playerWhoMove.add(playerAndreaalf);

        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

        //Testing if I removed the right ammos
        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnBlueAmmo());

        //Testing if the damages to gino are reset to 0
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getDamages().size());

        //Testing if the position of the offenders is reset
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getyPosition());

    }

    @Test
    public void shootPlayerPowerGloveSecondEffect() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PowerGlove");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);
        yPos.add(2);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerAndreaalf);
        playerWhoMove.add(playerAndreaalf);


        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add(playerAndreaalf);
        testArray1.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getDamages());
    }

    @Test
    public void shootPlayerPowerGloveSecondEffectNonLinear() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("PowerGlove");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 1, 0);
        gameTest.movePlayer(playerMeme, 1, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(1);
        xPos.add(1);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(0);
        yPos.add(1);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerAndreaalf);
        playerWhoMove.add(playerAndreaalf);


        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerMeme).getDamages());
    }

    @Test
    public void shootPlayerShockwaveFirstEffectMustBeDifferentSpotsException() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);
        players.add(playerKeny);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Shockwave");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);
        gameTest.revive(playerKeny);


        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);
        gameTest.movePlayer(playerMeme, 0, 1);
        gameTest.movePlayer(playerIngConti, 0, 1);
        gameTest.movePlayer(playerKeny, 0, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);
        defenders.add(playerKeny);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);
    }

    @Test
    public void shootPlayerShockwaveFirstEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);
        players.add(playerKeny);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Shockwave");


        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);


        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 0, 0);
        gameTest.movePlayer(playerMeme, 0, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getDamages());

    }

    @Test
    public void shootPlayerShockwaveSecondEffect() {

        ArrayList<String > players = new ArrayList<>();
        players.add(playerAndreaalf);
        players.add(playerGino);
        players.add(playerMeme);
        players.add(playerIngConti);
        players.add(playerKeny);

        gameTest = new Game(players, MapName.FIRE, 6);

        //Testing if attacking with Thor works as expected

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Shockwave");


        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);
        gameTest.revive(playerIngConti);
        gameTest.revive(playerKeny);


        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 1);
        gameTest.movePlayer(playerGino, 0, 0);
        gameTest.movePlayer(playerMeme, 0, 0);
        gameTest.movePlayer(playerIngConti, 0, 0);
        gameTest.movePlayer(playerKeny, 0, 2);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);
        defenders.add(playerIngConti);
        defenders.add(playerKeny);

        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 1);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

        ArrayList<String> testArray1 = new ArrayList<>();
        testArray1.add(playerAndreaalf);

        //Testing if I added the correct damages to meme
        Assert.assertEquals(testArray1, gameTest.getPlayerByNickname(playerMeme).getDamages());

        ArrayList<String> testArray2 = new ArrayList<>();
        testArray2.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray2, gameTest.getPlayerByNickname(playerIngConti).getDamages());

        ArrayList<String> testArray3 = new ArrayList<>();
        testArray3.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray3, gameTest.getPlayerByNickname(playerKeny).getDamages());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnYellowAmmo());

    }

    @Test
    public void shootPlayerSledgeHammerFirstEffectRegular() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Sledgehammer");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 0);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);


        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

    }

    @Test
    public void shootPlayerSledgeHammerFirstEffectMinDistanceException() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Sledgehammer");


        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 1);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);


        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

    }

    @Test
    public void shootPlayerSledgeHammerFirstEffectTooManyPlayersException() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Sledgehammer");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);
        gameTest.revive(playerMeme);


        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 0);
        gameTest.movePlayer(playerMeme, 0, 0);



        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);
        defenders.add(playerMeme);


        boolean result = gameTest.shootWithoutMovement(playerAndreaalf, defenders, weaponTest, 0);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());

    }

    @Test
    public void shootPlayerSledgeHammerSecondEffectRegular() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Sledgehammer");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 0);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(0);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(2);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerGino);


        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());


        //Testing gino is in the right spot
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(2, gameTest.getPlayerByNickname(playerGino).getyPosition());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());
    }

    @Test
    public void shootPlayerSledgeHammerSecondEffectTooManyMoves() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Sledgehammer");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 0);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(1);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(2);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerGino);


        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();

        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());


        //Testing gino is in the right spot
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getyPosition());

        Assert.assertEquals(1, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());
    }

    @Test
    public void shootPlayerSledgeHammerSecondEffectZeroMoves() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Sledgehammer");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 0);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();

        ArrayList<Integer> yPos = new ArrayList<>();

        ArrayList<String> playerWhoMove = new ArrayList<>();

        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertTrue(result);

        ArrayList<String> testArray = new ArrayList<>();
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);
        testArray.add(playerAndreaalf);


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());


        //Testing gino is in the right spot
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getyPosition());

        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerAndreaalf).getnRedAmmo());
    }

    @Test
    public void shootPlayerSledgeHammerSecondEffectNonLinear() {

        //Creates a new weapon by reading from the JSON file
        //The weapon I'm giving to the player is a duplicate!
        Weapon weaponTest = JsonDeserializer.createWeaponForTesting("Sledgehammer");

        //Players are dead by default so I need to revive them
        gameTest.revive(playerAndreaalf);
        gameTest.revive(playerGino);

        //ArrayList<Color> colors = new ArrayList<>();
        //colors.add(Color.BLUE);
        //gameTest.getPlayerByNickname(playerAndreaalf).giveAmmos(colors);

        //Giving the duplicate to the player
        gameTest.giveWeaponToPlayer(playerAndreaalf, weaponTest);

        //Moving these players to the testing spots
        gameTest.movePlayer(playerAndreaalf, 0, 0);
        gameTest.movePlayer(playerGino, 0, 0);


        //Array to pass to the shootPlayer method
        ArrayList<String> defenders = new ArrayList<>();
        defenders.add(playerGino);

        //creating the positions player wants to move in
        ArrayList<Integer> xPos = new ArrayList<>();
        xPos.add(1);

        ArrayList<Integer> yPos = new ArrayList<>();
        yPos.add(1);

        ArrayList<String> playerWhoMove = new ArrayList<>();
        playerWhoMove.add(playerGino);

        boolean result = gameTest.shootWithMovement(playerAndreaalf, defenders, weaponTest, 1, xPos, yPos, playerWhoMove);

        Assert.assertFalse(result);

        ArrayList<String> testArray = new ArrayList<>();


        //Testing if I added the correct damages to gino
        Assert.assertEquals(testArray, gameTest.getPlayerByNickname(playerGino).getDamages());


        //Testing gino is in the right spot
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getxPosition());
        Assert.assertEquals(0, gameTest.getPlayerByNickname(playerGino).getyPosition());

    }
}