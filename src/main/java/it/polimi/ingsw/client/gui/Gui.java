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
import it.polimi.ingsw.events.clientToServer.Ping;
import it.polimi.ingsw.events.clientToServer.SendCanReloadBeforeShootingAnswer;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.client.RemoteView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Gui extends Application implements QuestionEventHandler {

    protected GameScene gameScene;

    protected Stage window;

    private WaitingRoomGui waitingRoomGui;

    public static final String loginBackgroundImagePath = "/graphics/images/Adrenalina_front_image.jpg";

    public static final String loginCssPath = "/style/style.css";

    private final double screenRatioMin = 0.5500; // Screen ration of 16:9 is 0.5625
    private final double screenRatioMax = 0.5700;

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

        //GameStartedQuestion event = fakeGameStartedEvent();

        //GameInfo fakeGameInfo = new GameInfo("meme"); //mi serve l'username per sapere chi ha le armi e i powerup nel Json


        //Sets the Start Scene and shows it
        //MyScene next = new GameScene(window, "meme", event, fakeGameInfo, fakeGameInfo.playerInfo);
        MyScene next = new StartScene(this, window);
        Scene nextScene = next.getScene();

        window.setScene(
                nextScene
        );


        window.show();
    }

    private GameStartedQuestion fakeGameStartedEvent() {

        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add("meme");
        playerNames.add("gino");
        playerNames.add("andreaalf");
        //playerNames.add("ingConti");
        //playerNames.add("keni");



        String firstPlayer = "meme";

        ArrayList<PlayerColor> playerColors = PlayerColor.getRandomArray(5);

        MapName mapName = MapName.FIRE;

        int votedSkulls = 7;

        return new GameStartedQuestion(playerNames, playerColors, firstPlayer, mapName, votedSkulls);

    }


    public static Image loadImage(String fileName) {

        return new Image(Gui.class.getResourceAsStream(fileName));

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


    //*********************** NETWORK EVENTS *************************************************
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


        if (waitingRoomGui != null)
            waitingRoomGui.close();

        window.close();

        Modal.displayAndExit("YOU HAVE BEEN DISCONECTED");

    }

    @Override
    public void handleEvent(GameStartedQuestion event) {

        System.err.println("RECEIVED GameStartedQuestion EVENT");

        waitingRoomGui.close();

        //Scheduling the timer to send a Ping message every 2 seconds
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //System.err.println("Sending PING");
                remoteView.sendAnswerEvent(new Ping(username));
            }
        }, 0, 2000);

        //This constructor creates the starting scene
        MyScene next = new GameScene(window, username, event, gameInfo);
        this.gameScene = (GameScene) next;

        Scene nextScene = next.getScene();

        window.close();

        window = new Stage();


        //------------ Reading screen size ------------------------
        Rectangle2D screenVisibleBounds = Screen.getPrimary().getBounds();
        double aspectRatio = screenVisibleBounds.getHeight() / screenVisibleBounds.getWidth();

        //Deciding to use full screen or not resizable window
        if( ! (aspectRatio < screenRatioMax && aspectRatio > screenRatioMin)){
            window.setFullScreen(true);

            window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        }
        else {
            window.setResizable(false);
        }


        window.setScene(nextScene);

        window.show();

    }

    @Override
    public void handleEvent(GameRestartedQuestion event) {

        System.err.println("RECEIVED GameRestartedQuestion EVENT");

        //waitingRoomGui.close();

        //Scheduling the timer to send a Ping message every 2 seconds
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //System.err.println("Sending PING");
                remoteView.sendAnswerEvent(new Ping(username));
            }
        }, 0, 2000);

        //This constructor creates the starting scene

        GameStartedQuestion fake = new GameStartedQuestion(event);

        MyScene next = new GameScene(window, username, fake, gameInfo);
        this.gameScene = (GameScene) next;

        Scene nextScene = next.getScene();

        window.close();

        window = new Stage();


        //------------ Reading screen size ------------------------
        Rectangle2D screenVisibleBounds = Screen.getPrimary().getBounds();
        double aspectRatio = screenVisibleBounds.getHeight() / screenVisibleBounds.getWidth();

        //Deciding to use full screen or not resizable window
        if( ! (aspectRatio < screenRatioMax && aspectRatio > screenRatioMin)){
            window.setFullScreen(true);

            window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        }
        else {
            window.setResizable(false);
        }

        window.setScene(nextScene);

        window.show();
    }

    @Override
    public void handleEvent(PlayerDisconnectedQuestion event) {
        Modal.display(event.nickname + " DISCONNECTED FROM THE GAME");
    }

    //*****************************************************************************************

    @Override
    public void handleEvent(ActionQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.choosePossibleAction(event, username, remoteView));
    }

    @Override
    public void handleEvent(ChooseWeaponToAttackQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.chooseWeaponToAttack(event, username, remoteView));
    }

    @Override
    public void handleEvent(AskOrderAndDefenderQuestion event){
        Platform.runLater( () -> gameScene.playersInteractingSpace.askOrderAndDefender(event, username, remoteView, gameInfo.playersNames));
    }

    @Override
    public void handleEvent(UseGrenadeQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askUseGrenadeQuestion(event, username, remoteView));
    }

    @Override
    public void handleEvent(ChooseHowToShootQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseHowToShootQuestion(event, username, remoteView, gameInfo.playersNames));
    }

    @Override
    public void handleEvent(ChooseHowToPayForAttackingQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseHowToPayForAttackingQuestion(event, remoteView, playerInfo));
    }

    @Override
    public void handleEvent(ChooseHowToPayToSwitchWeaponsQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseHowToPayToSwitchWeaponsQuestion(event, remoteView, playerInfo, username));
    }

    @Override
    public void handleEvent(ChooseHowToPayToPickWeaponQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseHowToPayToPickWeaponQuestion(event, remoteView, playerInfo, username));
    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseHowToPayToReloadQuestion(event, remoteView, playerInfo, username));
    }

    @Override
    public void handleEvent(ChooseHowToUseTurnPowerUpQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseHowToUseTurnPowerUpQuestion(event, remoteView, playerInfo, username, gameInfo.playersNames));
    }

    @Override
    public void handleEvent(ChooseIfToUseAsyncPowerUpQuestion event) {
    }

    @Override
    public void handleEvent(ChoosePowerUpToRespawnQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChoosePowerUpToRespawnQuestion(event, username, remoteView));
    }

    @Override
    public void handleEvent(ChoosePowerUpToUseQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChoosePowerUpToUseQuestion(event, username, remoteView));
    }

    @Override
    public void handleEvent(ChooseWeaponToPickQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseWeaponToPickQuestion(event, username, remoteView));
    }

    @Override
    public void handleEvent(ChooseWeaponToReloadQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseWeaponToReloadQuestion(event, username, remoteView));
    }

    @Override
    public void handleEvent(ChooseWeaponToSwitchQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseWeaponToSwitchQuestion(event, username, remoteView));
    }

    @Override
    public void handleEvent(ModelUpdate event) {

        Platform.runLater( () -> {

            System.err.println("Model Update Received");

            this.lastSnapshotReceived = JsonDeserializer.stringToJsonObject(event.json);
            this.playerInfo = new PlayerInfo(username, lastSnapshotReceived);
            this.gameInfo = JsonDeserializer.deserializedSnapshot(this.lastSnapshotReceived, username);

            if (gameScene != null) {
                gameScene.setGameInfo(this.gameInfo);
                gameScene.update();
            }
        });
    }

    @Override
    public void handleEvent(TextMessage event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.textMessage(event));
    }

    @Override
    public void handleEvent(WhereToMoveAndGrabQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askWhereToMoveAndGrabQuestion(event, username, remoteView));
    }

    @Override
    public void handleEvent(WhereToMoveQuestion event) {
        Platform.runLater( () -> gameScene.playersInteractingSpace.askWhereToMoveQuestion(event, username, remoteView));
    }


    @Override
    public void handleEvent(ActionAfterReloadingQuestion event){
        Platform.runLater( () -> gameScene.playersInteractingSpace.actionAfterReloadingQuestion(event, username, remoteView));
    }

    @Override
    public void receiveEvent(QuestionEvent questionEvent) {

        Platform.runLater( () -> questionEvent.acceptEventHandler(this));
    }


    @Override
    public void handleEvent(ChooseIfUseATargetingScopeQuestion event){
        Platform.runLater( () -> gameScene.playersInteractingSpace.askChooseIfUseATargetingScopeQuestion(event, remoteView, gameInfo.playersNames));

    }

    @Override
    public void handleEvent(EndGameQuestion event){

    }

    @Override
    public void handleEvent(SendCanReloadBeforeShootingQuestion event){

    }

    @Override
    public void handleEvent(SendCanMoveBeforeShootingQuestion event){

    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadBeforeAttackQuestion event){

    }
}

