package it.polimi.ingsw.client.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.client.GameInfo;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.PlayerInfo;
import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.client.gui.scenes.*;
import it.polimi.ingsw.client.gui.scenes.gameScene.GameScene;
import it.polimi.ingsw.client.gui.scenes.gameScene.PlayersInteractingSpace;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.NewConnectionAnswer;
import it.polimi.ingsw.events.serverToClient.*;
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

        GameInfo fakeGameInfo = new GameInfo("meme"); //mi serve l'username per sapere chi ha le armi e i powerup nel Json


        //Sets the Start Scene and shows it
        MyScene next = new GameScene(window, "meme", event, fakeGameInfo, fakeGameInfo.playerInfo);
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
        Modal.display(event.nickname + " DISCONNECTED FROM THE GAME");
    }

    //*****************************************************************************************

    @Override
    public void handleEvent(ActionQuestion event) {
        gameScene.playersInteractingSpace.choosePossibleAction(event, username, remoteView);
    }

    @Override
    public void handleEvent(ChooseWeaponToAttackQuestion event) {
        gameScene.playersInteractingSpace.chooseWeaponToAttack(event, username, remoteView);
    }

    @Override
    public void handleEvent(AskOrderAndDefenderQuestion event){
        gameScene.playersInteractingSpace.askOrderAndDefender(event, username, remoteView, gameInfo.playersNames);
    }

    @Override
    public void handleEvent(ChooseHowToShootQuestion event) {
        gameScene.playersInteractingSpace.askChooseHowToShoot(event, username, remoteView, gameInfo.playersNames);
    }

    @Override
    public void handleEvent(ChooseHowToPayForAttackingQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayToSwitchWeaponsQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayToPickWeaponQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadQuestion event) {

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
        this.gameInfo = JsonDeserializer.deserializedSnapshot(JsonDeserializer.stringToJsonObject(event.json), username);
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

