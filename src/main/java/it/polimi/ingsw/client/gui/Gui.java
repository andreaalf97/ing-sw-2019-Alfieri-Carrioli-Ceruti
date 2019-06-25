package it.polimi.ingsw.client.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.client.GameInfo;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.PlayerInfo;
import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.client.gui.scenes.*;
import it.polimi.ingsw.client.gui.scenes.gameScene.GameScene;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.NewConnectionAnswer;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.client.RemoteView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Gui extends Application implements QuestionEventHandler {

    protected GameScene gameScene;

    protected Stage window;

    private WaitingRoomGui waitingRoomGui;

    public static final String loginBackgroundImagePath = "src/main/resources/graphics/images/Adrenalina_front_image.jpg";

    public static final String loginCssPath = "/style/style.css";

    private int rmiPort = 5432;

    private int socketPort = 2345;

    private String chosenIp;

    private Image mapImage;

    private String playerColor;

    private Image planciaGiocatoreImage = null;

    /**
     * The player's username
     */
    private String username;

    /**
     * Initially, this attribute contains the chosen map, then it conains the actual map the game is played on
     */
    private MapName currentMap;

    /**
     * Works the same as the currentMap attribute, but with kst skulls
     */
    private int currentSkulls;

    /**
     * A temporary ID given by the server during the first stage of connection
     */
    private Integer temporaryId;

    /**
     * The client proxy, used to receive and send messages
     */
    RemoteView remoteView;

    /**
     * The most updated snapshot of the game
     */
    private JsonObject lastSnapshotReceived;

    private GameInfo gameInfo;

    /**
     * All of this player's info
     */
    private PlayerInfo playerInfo;

    public static void main(String[] args) {

        //Loads the window and starts the gui
        launch(args);
    }

    @Override
    public void start(Stage givenWindow) {

        this.window = givenWindow;

        //First window
        window.setTitle("Adrenalina");

        /*
        //Questi sono per settare la grandezza della scena a screen size!
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        window.setX(primaryScreenBounds.getMinX());
        window.setY(primaryScreenBounds.getMinY());
        window.setWidth(primaryScreenBounds.getWidth());
        window.setHeight(primaryScreenBounds.getHeight());
         */



        GameStartedQuestion event = fakeGameStartedEvent();

        //JsonObject myJson = JsonDeserializer.myJsonParser.parse("{{\"players\":[{\"nickname\":\"gino\",\"nRedAmmo\":1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"points\":0,\"weaponList\":[],\"powerUpList\":[],\"damages\":[],\"marks\":[],\"nDeaths\":0,\"xPosition\":0,\"yPosition\":0,\"isDead\":true,\"nMoves\":3,\"nMovesBeforeGrabbing\":1,\"nMovesBeforeShooting\":0,\"canReloadBeforeShooting\":false,\"playerStatus\":{\"isFirstTurn\":true,\"isFrenzyTurn\":false,\"isActive\":false,\"nActionsDone\":0,\"nActions\":2}},{\"nickname\":\"andreaalf\",\"nRedAmmo\":1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"points\":0,\"weaponList\":[],\"powerUpList\":[],\"damages\":[],\"marks\":[],\"nDeaths\":0,\"xPosition\":1,\"yPosition\":0,\"isDead\":true,\"nMoves\":3,\"nMovesBeforeGrabbing\":1,\"nMovesBeforeShooting\":0,\"canReloadBeforeShooting\":false,\"playerStatus\":{\"isFirstTurn\":true,\"isFrenzyTurn\":false,\"isActive\":false,\"nActionsDone\":0,\"nActions\":2}},{\"nickname\":\"meme\",\"nRedAmmo\":1,\"nBlueAmmo\":1,\"nYellowAmmo\":1,\"points\":0,\"weaponList\":[\"Hellion\",\"Furnace\"],\"powerUpList\":[],\"damages\":[],\"marks\":[],\"nDeaths\":0,\"xPosition\":2,\"yPosition\":1,\"isDead\":true,\"nMoves\":3,\"nMovesBeforeGrabbing\":1,\"nMovesBeforeShooting\":0,\"canReloadBeforeShooting\":false,\"playerStatus\":{\"isFirstTurn\":true,\"isFrenzyTurn\":false,\"isActive\":false,\"nActionsDone\":0,\"nActions\":2}}],\"playerNames\":[\"gino\",\"andreaalf\",\"meme\"],\"powerUpDeck\":{\"powerUpList\":[{\"powerUpName\":\"Teleporter\",\"color\":\"YELLOW\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":100,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TargetingScope\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"ANY\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Newton\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TagbackGrenade\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Teleporter\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":100,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TargetingScope\",\"color\":\"RED\",\"effect\":{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"ANY\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Newton\",\"color\":\"YELLOW\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TagbackGrenade\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Teleporter\",\"color\":\"YELLOW\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":100,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TargetingScope\",\"color\":\"RED\",\"effect\":{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"ANY\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Newton\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TagbackGrenade\",\"color\":\"YELLOW\",\"effect\":{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Teleporter\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":100,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TargetingScope\",\"color\":\"YELLOW\",\"effect\":{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"ANY\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Newton\",\"color\":\"BLUE\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TagbackGrenade\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Teleporter\",\"color\":\"YELLOW\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":100,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TargetingScope\",\"color\":\"RED\",\"effect\":{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"ANY\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Newton\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TagbackGrenade\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Teleporter\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":100,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TargetingScope\",\"color\":\"RED\",\"effect\":{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"ANY\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},\"isTurnPowerup\":false},{\"powerUpName\":\"Newton\",\"color\":\"YELLOW\",\"effect\":{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},\"isTurnPowerup\":true},{\"powerUpName\":\"TagbackGrenade\",\"color\":\"RED\",\"effect\":{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},\"isTurnPowerup\":false}]},\"weaponDeck\":{\"weaponList\":[{\"weaponName\":\"Heatseeker\",\"cost\":[\"RED\",\"RED\",\"YELLOW\"],\"effects\":[{\"nDamages\":3,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"}],\"isLoaded\":true,\"order\":[[0]]},{\"weaponName\":\"Hellion\",\"cost\":[\"RED\",\"YELLOW\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":50,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":2,\"nPlayersMarkable\":50,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1],[0,2]]},{\"weaponName\":\"Flamethrower\",\"cost\":[\"RED\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":2,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":2,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":true,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"YELLOW\",\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":1,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":true,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":2,\"maxDistance\":2,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0],[1,2]]},{\"weaponName\":\"GrenadeLauncher\",\"cost\":[\"RED\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":4,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2],[1,0,2],[1,2,0]]},{\"weaponName\":\"RocketLauncher\",\"cost\":[\"RED\",\"RED\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":4,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":true,\"cost\":[\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":2,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2],[0,2,3,4],[0,2,4]]},{\"weaponName\":\"Railgun\",\"cost\":[\"YELLOW\",\"YELLOW\",\"BLUE\"],\"effects\":[{\"nDamages\":3,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":2,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0],[1]]},{\"weaponName\":\"Cyberblade\",\"cost\":[\"YELLOW\",\"RED\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":1,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1,2],[1,0,2]]},{\"weaponName\":\"ZX-2\",\"cost\":[\"YELLOW\",\"RED\"],\"effects\":[{\"nDamages\":1,\"nMarks\":2,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":3,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]},{\"weaponName\":\"Shotgun\",\"cost\":[\"YELLOW\",\"YELLOW\"],\"effects\":[{\"nDamages\":3,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":1,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1],[2]]},{\"weaponName\":\"PowerGlove\",\"cost\":[\"YELLOW\",\"BLUE\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":1,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},{\"nDamages\":1,\"nMarks\":2,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":1,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":1,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"}],\"isLoaded\":true,\"order\":[[0,1],[2,3,4,3],[2,4,3]]},{\"weaponName\":\"Shockwave\",\"cost\":[\"YELLOW\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":3,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":1,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":true,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":4,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":1,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]},{\"weaponName\":\"Sledgehammer\",\"cost\":[\"YELLOW\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":3,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":true,\"visibleByWho\":\"NONE\"}],\"isLoaded\":true,\"order\":[[0],[1,2]]}]},\"ammoCardDeck\":{\"ammoCardList\":[{\"ammoColorList\":[\"YELLOW\",\"BLUE\",\"BLUE\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_7.png\"},{\"ammoColorList\":[\"YELLOW\",\"RED\",\"RED\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_8.png\"},{\"ammoColorList\":[\"RED\",\"BLUE\",\"BLUE\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_9.png\"},{\"ammoColorList\":[\"RED\",\"YELLOW\",\"YELLOW\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_10.png\"},{\"ammoColorList\":[\"BLUE\",\"YELLOW\",\"YELLOW\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_11.png\"},{\"ammoColorList\":[\"BLUE\",\"RED\",\"RED\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_12.png\"},{\"ammoColorList\":[\"YELLOW\",\"BLUE\",\"BLUE\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_13.png\"},{\"ammoColorList\":[\"YELLOW\",\"RED\",\"RED\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_14.png\"},{\"ammoColorList\":[\"RED\",\"BLUE\",\"BLUE\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_15.png\"},{\"ammoColorList\":[\"RED\",\"YELLOW\",\"YELLOW\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_16.png\"},{\"ammoColorList\":[\"BLUE\",\"YELLOW\",\"YELLOW\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_17.png\"},{\"ammoColorList\":[\"BLUE\",\"RED\",\"RED\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_18.png\"},{\"ammoColorList\":[\"YELLOW\",\"YELLOW\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_19.png\"},{\"ammoColorList\":[\"RED\",\"RED\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_20.png\"},{\"ammoColorList\":[\"BLUE\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_21.png\"},{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_22.png\"},{\"ammoColorList\":[\"YELLOW\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_23.png\"},{\"ammoColorList\":[\"RED\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_24.png\"},{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_25.png\"},{\"ammoColorList\":[\"YELLOW\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_26.png\"},{\"ammoColorList\":[\"RED\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_27.png\"},{\"ammoColorList\":[\"YELLOW\",\"YELLOW\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_28.png\"},{\"ammoColorList\":[\"RED\",\"RED\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_29.png\"},{\"ammoColorList\":[\"BLUE\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_30.png\"},{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_31.png\"},{\"ammoColorList\":[\"YELLOW\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_32.png\"},{\"ammoColorList\":[\"RED\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_33.png\"},{\"ammoColorList\":[\"YELLOW\",\"RED\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_34.png\"},{\"ammoColorList\":[\"YELLOW\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_35.png\"},{\"ammoColorList\":[\"RED\",\"BLUE\"],\"hasPowerUp\":true,\"ammoCardImagePath\":\"/graphics/ammo/ammo_36.png\"}]},\"kst\":{\"skullList\":[\"SKULL\",\"SKULL\",\"SKULL\",\"SKULL\",\"SKULL\",\"SKULL\"],\"isOverkill\":[false,false,false,false,false,false]},\"gameMap\":{\"row0\":{\"col0\":{\"spot\":{\"ammoCard\":{\"ammoColorList\":[\"YELLOW\",\"BLUE\",\"BLUE\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_1.png\"},\"playersHere\":[\"gino\"],\"doors\":[false,false,true,false],\"room\":\"SAPPHIRE\"}},\"col1\":{\"spot\":{\"ammoCard\":{\"ammoColorList\":[\"YELLOW\",\"RED\",\"RED\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_2.png\"},\"playersHere\":[],\"doors\":[false,false,false,false],\"room\":\"SAPPHIRE\"}},\"col2\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"LockRifle\",\"cost\":[\"BLUE\",\"BLUE\"],\"effects\":[{\"nDamages\":2,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1]]},{\"weaponName\":\"MachineGun\",\"cost\":[\"BLUE\",\"RED\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":2,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1,2,3],[0,2,3,1],[0,2,1],[0,3,1],[0,1,4],[0,4,1]]},{\"weaponName\":\"Thor\",\"cost\":[\"BLUE\",\"RED\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"LASTONEATTACKED\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"LASTONEATTACKED\"}],\"isLoaded\":true,\"order\":[[0,1,2]]}],\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"SAPPHIRE\"}},\"col3\":{\"spot\":{}}},\"row1\":{\"col0\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"PlasmaGun\",\"cost\":[\"BLUE\",\"YELLOW\"],\"effects\":[{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":2,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"NONE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[\"BLUE\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2],[1,0,2],[0,2,1]]},{\"weaponName\":\"Whisper\",\"cost\":[\"BLUE\",\"BLUE\",\"YELLOW\"],\"effects\":[{\"nDamages\":3,\"nMarks\":1,\"nPlayersMarkable\":1,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":2,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0]]},{\"weaponName\":\"Electroscythe\",\"cost\":[\"BLUE\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"BLUE\",\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]}],\"playersHere\":[\"andreaalf\"],\"doors\":[true,false,false,false],\"room\":\"RUBY\"}},\"col1\":{\"spot\":{\"ammoCard\":{\"ammoColorList\":[\"RED\",\"BLUE\",\"BLUE\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_3.png\"},\"playersHere\":[],\"doors\":[false,false,true,false],\"room\":\"RUBY\"}},\"col2\":{\"spot\":{\"ammoCard\":{\"ammoColorList\":[\"RED\",\"YELLOW\",\"YELLOW\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_4.png\"},\"playersHere\":[],\"doors\":[true,true,false,false],\"room\":\"RUBY\"}},\"col3\":{\"spot\":{\"ammoCard\":{\"ammoColorList\":[\"BLUE\",\"YELLOW\",\"YELLOW\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_5.png\"},\"playersHere\":[],\"doors\":[false,false,false,true],\"room\":\"TOPAZ\"}}},\"row2\":{\"col0\":{\"spot\":{}},\"col1\":{\"spot\":{\"ammoCard\":{\"ammoColorList\":[\"BLUE\",\"RED\",\"RED\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_6.png\"},\"playersHere\":[\"meme\"],\"doors\":[true,false,false,false],\"room\":\"DIAMOND\"}},\"col2\":{\"spot\":{\"ammoCard\":{\"ammoColorList\":[\"YELLOW\",\"RED\",\"RED\"],\"hasPowerUp\":false,\"ammoCardImagePath\":\"/graphics/ammo/ammo_2.png\"},\"playersHere\":[],\"doors\":[false,true,false,false],\"room\":\"DIAMOND\"}},\"col3\":{\"spot\":{\"weaponList\":[{\"weaponName\":\"TractorBeam\",\"cost\":[\"BLUE\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[\"RED\",\"YELLOW\"],\"nMoves\":0,\"nMovesOtherPlayer\":2,\"minDistance\":0,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":3,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":true,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0,1],[2,3]]},{\"weaponName\":\"VortexCannon\",\"cost\":[\"RED\",\"BLUE\"],\"effects\":[{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":2,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[\"RED\"],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"},{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":1,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":0,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":0,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":1,\"minDistance\":0,\"maxDistance\":0,\"mustBeOtherRoom\":false,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"DONTCARE\"}],\"isLoaded\":true,\"order\":[[0,1,2,3,4,3]]},{\"weaponName\":\"Furnace\",\"cost\":[\"RED\",\"BLUE\"],\"effects\":[{\"nDamages\":1,\"nMarks\":0,\"nPlayersMarkable\":0,\"nPlayersAttackable\":4,\"mustShootOtherPlayers\":false,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":100,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":false,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"},{\"nDamages\":1,\"nMarks\":1,\"nPlayersMarkable\":4,\"nPlayersAttackable\":50,\"mustShootOtherPlayers\":true,\"mustShootSamePlayers\":false,\"cost\":[],\"nMoves\":0,\"nMovesOtherPlayer\":0,\"minDistance\":1,\"maxDistance\":1,\"mustBeOtherRoom\":true,\"mustBeDifferentSpots\":false,\"mustBeSameSpots\":true,\"isLinear\":false,\"visibleByWho\":\"OFFENDER\"}],\"isLoaded\":true,\"order\":[[0],[1]]}],\"playersHere\":[],\"doors\":[false,false,false,true],\"room\":\"TOPAZ\"}}}}}").getAsJsonObject();
        GameInfo fakeGameInfo = new GameInfo();
        //PlayerInfo fakePlayerInfo = new PlayerInfo("meme", myJson);

        //Sets the Start Scene and shows it
        MyScene next = new GameScene(window, "meme", event, fakeGameInfo, playerInfo);  //TODO qui ci va fakePlayerInfo, non playerinfo che è vuoto
        //MyScene next = new StartScene(this, window);
        Scene nextScene = next.getScene();

        window.setScene(
                nextScene
        );


        window.show();
    }

    private GameStartedQuestion fakeGameStartedEvent() {

        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add("meme");
        playerNames.add("andreaalf");
        playerNames.add("gino");
        playerNames.add("ingConti");
        playerNames.add("keni");



        String firstPlayer = "meme";

        ArrayList<PlayerColor> playerColors = PlayerColor.getRandomArray(5);

        MapName mapName = MapName.FIRE;

        int votedSkulls = 7;

        return new GameStartedQuestion(playerNames, playerColors, firstPlayer, mapName, votedSkulls);

    }


    public static Image loadImage(String fileName) {

        try {
            return new Image(new FileInputStream(fileName));
        }
        catch (FileNotFoundException e){
            System.err.println("File not found");
            e.printStackTrace();
            return null;
        }

    }

    public void setGuiParams(RemoteView remoteView, String username, MapName votedMap, int votedSkulls){
        this.remoteView = remoteView;
        this.username = username;
        this.currentMap = votedMap;
        this.currentSkulls = votedSkulls;
    }

    /*CSS examples
    *button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
     text2.setStyle("-fx-font: normal bold 20px 'serif' ");
     gridPane.setStyle("-fx-background-color: BEIGE;"); */

    @Override
    public void handleEvent(TemporaryIdQuestion event) {

        System.out.println("RECEIVED TemporaryIdQuestion EVENT");

        temporaryId = event.temporaryId;

        //I'm sending the current values of the votes because initially they are the voted values, then
        //they become the actual values as voted by all players
        remoteView.sendAnswerEvent(
                new NewConnectionAnswer(temporaryId, username, currentMap, currentSkulls)
        );
    }

    @Override
    public void handleEvent(InvalidUsernameQuestion event) {

        System.out.println("RECEIVED InvalidUsernameQuestion EVENT");


        Modal.display("Username already LOGGED IN");

        MyScene next = new LoginScene(this, this.window);
        Scene nextScene = next.getScene();
        window.setScene(nextScene);

    }

    @Override
    public void handleEvent(AddedToWaitingRoomQuestion event) {

        System.out.println("RECEIVED AddedToWaitingRoomQuestion EVENT");


        waitingRoomGui = new WaitingRoomGui(event.players);
        waitingRoomGui.display();

    }

    @Override
    public void handleEvent(NewPlayerConnectedQuestion event) {

        System.out.println("RECEIVED NewPlayerConnectedQuestion EVENT");

        waitingRoomGui.addPlayer(event.nickname);
    }

    @Override
    public void handleEvent(DisconnectedQuestion event) {

        System.out.println("RECEIVED DisconnectedQuestion EVENT");


        waitingRoomGui.close();

        window.close();

        Modal.display("YOU HAVE BEEN DISCONECTED");

    }

    @Override
    public void handleEvent(GameStartedQuestion event) {

        System.out.println("RECEIVED GameStartedQuestion EVENT");

        waitingRoomGui.close();

        MyScene next = new GameScene(window, username, event, gameInfo, playerInfo);
        Scene nextScene = next.getScene();
        window.setScene(nextScene);

        //TODO andreaalf
        //Settare game scene e iniziare la partita
    }

    @Override
    public void handleEvent(PlayerDisconnectedQuestion event) {

    }

    @Override
    public void handleEvent(ActionQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayForAttackingQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayToPickWeaponQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToShootQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToUseTurnPowerUpQuestion event) {

    }

    @Override
    public void handleEvent(ChooseIfToUseAsyncPowerUpQuestion event) {

    }

    @Override
    public void handleEvent(ChoosePowerUpToRespawnQuestion event) {

    }

    @Override
    public void handleEvent(ChoosePowerUpToUseQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToAttackQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToPickQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToReloadQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToSwitchQuestion event) {

    }

    @Override
    public void handleEvent(ModelUpdate event) {
        this.lastSnapshotReceived = JsonDeserializer.stringToJsonObject(event.json);
        this.playerInfo = new PlayerInfo(username, lastSnapshotReceived);
        this.gameInfo = JsonDeserializer.deserializedSnapshot(JsonDeserializer.stringToJsonObject(event.json));
        gameScene.setGameInfo(this.gameInfo);
        gameScene.setPlayerInfo(this.playerInfo);
    }

    @Override
    public void handleEvent(TextMessage event) {

    }

    @Override
    public void handleEvent(WhereToMoveAndGrabQuestion event) {

    }

    @Override
    public void handleEvent(WhereToMoveQuestion event) {

    }


    @Override
    public void receiveEvent(QuestionEvent questionEvent) {

        Platform.runLater( () -> questionEvent.acceptEventHandler(this));


    }
}

