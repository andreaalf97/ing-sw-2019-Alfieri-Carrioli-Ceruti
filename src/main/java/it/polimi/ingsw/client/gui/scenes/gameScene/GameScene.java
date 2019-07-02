package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.GameInfo;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.PlayerInfo;
import it.polimi.ingsw.client.gui.ClosingBox;
import it.polimi.ingsw.client.gui.scenes.MyScene;
import it.polimi.ingsw.events.serverToClient.GameStartedQuestion;
import it.polimi.ingsw.model.map.MapName;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameScene extends ClickHandler implements MyScene {

    /**
     * The stage where this scene is located
     */
    private Stage window;

    /**
     * This scene
     */
    private Scene scene;

    /**
     * The map grid pane
     */
    private BoardGrid boardGrid;

    /**
     * The part of scene for the player to comunicate/choose things
     */
    public PlayersInteractingSpace playersInteractingSpace;

    /**
     * The pane to put all weapons in the player's hand
     */
    private ArrayList<CardBox> weaponBoxes;

    /**
     * The pane to put all weapons in the player's hand
     */
    private ArrayList<CardBox> powerUpBoxes;

    /**
     * The pane where the player's plancia is
     */
    private MyPlancia myPlancia;
    /**
     * The pane where other players's plancia is
     */
    private OtherPlayersPlancias otherPlayersPlancias;

    private CardBox cardBox;

    /**
     * The label that prompts all the messages
     */
    private Label messageBox;

    private final String username;

    public ArrayList<String> playerNames;

    public ArrayList<PlayerColor> playerColors;

    public String firstPlayer;

    public MapName mapName;

    public int votedSkulls;

    public GameInfo gameInfo;

    private final String cssPath = "/style/gameStyle.css";

    private final int externalRows = 54;
    private final int externalCols = 96;

    private final int otherPlanciasCol = 60;
    private final int otherPlanciasRow = 12;
    private final int otherPlanciasOffset = 10;



    /**
     * This constructor builds the scene by using a gridpane and positioning all elements
     * int the correct spot
     * @param window the window
     * @param username the username of this player
     * @param event the game started event
     */
    public GameScene(Stage window, String username, GameStartedQuestion event, GameInfo gameInfo) {

        super(new ArrayList<>());

        this.gameInfo = gameInfo;

        this.window = window;
        this.username = username;
        this.playerNames = event.playerNames;
        this.playerColors = event.playerColors;
        this.firstPlayer = event.firstPlayer;
        this.mapName = event.mapName;
        this.votedSkulls = event.votedSkulls;


        this.weaponBoxes = new ArrayList<>();
        this.powerUpBoxes = new ArrayList<>();

        Font.loadFont(
                GameScene.class.getResource("/fonts/ZCOOLKuaiLe-Regular.ttf").toExternalForm(),
                120
        );

        //Setting up the external grid
        GridPane externalGridPane = setUpExternalGridPane(externalCols, externalRows);

        //************************************* + Board Grid + *********************************************

        //Setting up the map grid
        this.boardGrid = new BoardGrid(this.mapName.getPath(), this.votedSkulls, this.playerNames, this.playerColors, this.username);

        //Adding the map grid to the main pane
        //Starting from column 0, row 5, colspan 30, rowspan 24
        externalGridPane.add(boardGrid.getGridPane(),0,10,60,48);

        //************************************* + PLAYERS SPACE FOR DECISIONS/THINGHS TO CHOOSE + *********************************************

        this.playersInteractingSpace = new PlayersInteractingSpace();
        externalGridPane.add(playersInteractingSpace.getGridPane(), 0, 0, 44, 10);

        //************************************* + MY PLANCIA + *********************************************

        //Setting up the player's plancia
        PlayerColor myColor = playerColors.get(playerNames.indexOf(username));

        //this.myPlancia = new MyPlancia(myColor, "rightShadow", playerInfo,  this.playerNames, this.playerColors);
        this.myPlancia = new MyPlancia(myColor, playerColors, playerNames);

        //Adding the plancia to the main pane
        externalGridPane.add(myPlancia.getplanciaGridPane(), 56, 0, 42, 10);

        //************************************* + OTHER PLANCIAS + *********************************************

        //Copies the player color array
        ArrayList<PlayerColor> otherPlayersColors = new ArrayList<>(playerColors);
        ArrayList<PlayerColor> allPlayersColors = new ArrayList<>(playerColors);

        //Gets the index of this player
        int indexOfThisPlayer = playerNames.indexOf(username);

        //Removes this player's color from the array
        otherPlayersColors.remove(indexOfThisPlayer);

        //Creates the array of other player's usernames
        ArrayList<String> otherPlayers = new ArrayList<>(playerNames);
        otherPlayers.remove(username);

        //Creates the list of other plancias
        //this.otherPlayersPlancias = new OtherPlayersPlancias(otherPlayersColors, gameInfo, username, otherPlayers, allPlayersColors, playerNames);

        this.otherPlayersPlancias = new OtherPlayersPlancias(otherPlayersColors, otherPlayers, allPlayersColors, playerNames, username);


        int i = 0;
        for (GridPane g : otherPlayersPlancias.getGridPanes()){
            String usernameOtherPlayers = otherPlayers.get(i);
            Label label = new Label(usernameOtherPlayers);
            label.setStyle("-fx-font-size: 16px");
            externalGridPane.add(label, otherPlanciasCol, (otherPlanciasRow + (i * otherPlanciasOffset))-1, 15, 1);
            externalGridPane.add(g, otherPlanciasCol, otherPlanciasRow + (i * otherPlanciasOffset), 36, 8);
            i++;
        }
        //************************************* + MESSAGE VIEWER + *********************************************

        this.messageBox = new Label("ADRENALINA");
        messageBox.getStyleClass().add("messages");
        messageBox.setTextAlignment(TextAlignment.CENTER);

        //System.err.println("Font --> " + messageBox.getFont());

        externalGridPane.add(messageBox, 60, 48, 28, 6);

        //************************************* + POWER UPS AND WEAPONS + *********************************************

        this.cardBox = new CardBox();
        externalGridPane.add(cardBox.getGridPane(), 44, 0, 12, 10);

        //************************************* + LOGO + *********************************************
        //setUpLogo(externalGridPane, 88, 0, 8, 8);
        //************************************* + EXIT BUTTON + *********************************************

        setUpExit(externalGridPane, 88, 50, 7, 6);

        update();

        //Loading the pane into the scene
        this.scene = new Scene(externalGridPane, 1120, 630);


    }

    public void update() {

        //Updating BoardGrid
        boardGrid.update(this.gameInfo);

        //Updating my Plancia
        myPlancia.update(this.gameInfo.playerInfo);

        //Updating other players plancias
        otherPlayersPlancias.update(gameInfo, username, playerColors, playerNames);

        //Updating my cards and power ups
        cardBox.update(this.gameInfo.playerInfo);

    }

    /**
     * Sets up the exit button
     * @param externalGridPane the external pane
     * @param col the col for the button
     * @param row the row for the button
     * @param colspan the colspan
     * @param rowspan the rowspan
     */
    private void setUpExit(GridPane externalGridPane, int col, int row, int colspan, int rowspan) {

        Button exitButton = new Button("Exit");
        exitButton.setMinSize(100, 50);
        externalGridPane.add(exitButton, col, row, colspan, rowspan);

        exitButton.setOnAction(event -> ClosingBox.display(window));
    }

    /**
     * Sets up the top right logo
     * @param externalGridPane the external pane
     * @param col the col
     * @param row the row
     * @param colspan the colspan
     * @param rowspan the rowspan
     */
    private void setUpLogo(GridPane externalGridPane, int col, int row, int colspan, int rowspan) {

        HBox logo = new HBox();

        Image image = new Image(
                "graphics/customLOGO.png",
                0, 0,
                true, false
        );

        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1, 1, true, true, true, false)
        );

        logo.setBackground(new Background(backgroundImage));

        externalGridPane.add(logo, col, row, colspan, rowspan);

    }


    /**
     * Sets up the external grid pane
     * @param nCols the amount of cols
     * @param nRows the amount of rows
     * @return the gridpane
     */
    private GridPane setUpExternalGridPane(int nCols, int nRows) {

        GridPane externalGridPane = new GridPane();

        setMainGrid(externalGridPane, nCols, nRows);

        //Setting up CSS for main pane
        externalGridPane.getStylesheets().add(cssPath);
        externalGridPane.getStyleClass().add("mainBackground");
        externalGridPane.setGridLinesVisible(false);

        return  externalGridPane;
    }

    /**
     * This method sets up the GridPane and splits it into cols and rows
     * It's static because it is used by other grid panes too
     * @param pane the pane to split
     * @param externalCols the amount of cols
     * @param externalRows the amount of rows
     */
    private void setMainGrid(GridPane pane, int externalCols, int externalRows) {


        for(int i = 0; i < externalCols; i++){
            ColumnConstraints colConstrains = new ColumnConstraints();
            colConstrains.setPercentWidth(100.0 / externalCols);
            pane.getColumnConstraints().add(colConstrains);
        }
        for(int i = 0; i < externalRows; i++){
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / externalRows);
            pane.getRowConstraints().add(rowConstraints);
        }

    }

    @Override
    public Scene getScene() {
        return this.scene;
    }

    @Override
    void newClick(String id) {
        System.err.println("CLICKED ON " + id);
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }
}