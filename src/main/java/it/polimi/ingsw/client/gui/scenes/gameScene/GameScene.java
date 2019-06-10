package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.PlayerColor;
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

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;

public class GameScene implements MyScene {

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
    private GameMapGrid gameMapGrid;

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



    private final String cssPath = "/style/gameStyle.css";

    private final double screenRatioMin = 0.5500; // Screen ration of 16:9 is 0.5625
    private final double screenRatioMax = 0.5700;

    private final int externalRows = 27;
    private final int externalCols = 48;

    private final int otherPlanciasCol = 30;
    private final int otherPlanciasRow = 5;
    private final int otherPlanciasOffset = 5;

    /**
     * This constructor builds the scene by using a gridpane and positioning all elements
     * int the correct spot
     * @param window the window
     * @param username the username of this player
     * @param event the game started event
     */
    public GameScene(Stage window, String username, GameStartedQuestion event) {

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

        //------------ Reading screen size ------------------------
        Rectangle2D screenVisibleBounds = Screen.getPrimary().getBounds();
        double aspectRatio = screenVisibleBounds.getHeight() / screenVisibleBounds.getWidth();

        //Setting up the external grid
        GridPane externalGridPane = setUpExternalGridPane(externalCols, externalRows);



        //Setting up the map grid
        this.gameMapGrid = new GameMapGrid(this.mapName.getPath());

        //Adding the map grid to the main pane
        //Starting from column 0, row 5, colspan 30, rowspan 24
        externalGridPane.add(gameMapGrid.getGridPane(),0,5,30,24);



        //Setting up the player's plancia
        PlayerColor myColor = playerColors.get(playerNames.indexOf(username));

        this.myPlancia = new MyPlancia(myColor, "rightShadow");

        //Adding the plancia to the main pane
        externalGridPane.add(myPlancia.getplanciaGridPane(), 0, 0, 22, 5);



        //Setting up all other plancias

        ArrayList<PlayerColor> tempColors = new ArrayList<>(playerColors);

        int indexOfThisPlayer = playerNames.indexOf(username);

        tempColors.remove(indexOfThisPlayer);

        ArrayList<String> otherPlayers = new ArrayList<>();

        for(String p : playerNames){
            if( !p.equals(username) )
                otherPlayers.add(p);
        }

        this.otherPlayersPlancias = new OtherPlayersPlancias(otherPlayers, tempColors);

        int i = 0;
        for (GridPane g : otherPlayersPlancias.getGridPanes()){
            externalGridPane.add(g, otherPlanciasCol, otherPlanciasRow + (i * otherPlanciasOffset), 19, 4);
            i++;
        }




        //The message viewer
        this.messageBox = new Label("This is Adrenalina bitches");
        messageBox.getStyleClass().add("messages");
        messageBox.setTextAlignment(TextAlignment.CENTER);

        System.err.println("Font --> " + messageBox.getFont());

        externalGridPane.add(messageBox, 30, 24, 14, 3);







        setUpPlayerPowerUps(externalGridPane, 23, 1, 3, 3);
        setUpPlayerWeapons(externalGridPane, 33, 1, 3, 3);


        setUpLogo(externalGridPane, 44, 0, 4, 4);

        setUpExit(externalGridPane, 44, 24, 4, 3);


        //Loading the pane into the scene
        this.scene = new Scene(externalGridPane, 1120, 630);

        //Deciding to use full screen or not resizable window
        if(aspectRatio < screenRatioMax && aspectRatio > screenRatioMin){
            window.setFullScreen(true);

            window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        }
        else {
            window.setResizable(false);
        }


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
     * Sets up the player weapons
     * @param externalGridPane the external pane
     * @param col the col
     * @param row the row
     * @param colspan the colspan
     * @param rowspan the rowspan
     */
    private void setUpPlayerWeapons(GridPane externalGridPane, int col, int row, int colspan, int rowspan) {

        ArrayList<String> weaponPaths = new ArrayList<>();
        weaponPaths.add("/graphics/cards/AD_weapons_IT_0217.png");
        weaponPaths.add("/graphics/cards/AD_weapons_IT_0215.png");
        weaponPaths.add("/graphics/cards/AD_weapons_IT_0218.png");

        for(int i = 0; i < 3; i++){

            CardBox newCard = new CardBox(weaponPaths.get(i));


            externalGridPane.add(newCard.gethBox(), col + (i*colspan), row, colspan, rowspan);

        }

    }

    /**
     * Sets up the player power ups
     * @param externalGridPane the external pane
     * @param col the col
     * @param row the row
     * @param colspan the colspan
     * @param rowspan the rowspan
     */
    private void setUpPlayerPowerUps(GridPane externalGridPane, int col, int row, int colspan, int rowspan) {

        for(int i = 0; i < 3; i++){

            CardBox newCard = new CardBox("/graphics/cards/AD_powerups_IT_02.png");


            externalGridPane.add(newCard.gethBox(), col + (i*colspan), row, colspan, rowspan);

        }


    }

    /**
     * Sets up the external grid pane
     * @param nCols the amount of cols
     * @param nRows the amount of rows
     * @return the gridpane
     */
    private GridPane setUpExternalGridPane(int nCols, int nRows) {

        GridPane externalGridPane = new GridPane();

        setGrid(externalGridPane, nCols, nRows);

        //Setting up CSS for main pane
        externalGridPane.getStylesheets().add(cssPath);
        externalGridPane.getStyleClass().add("mainBackground");


        //externalGridPane.setBackground(new Background(new BackgroundFill(Color.ORANGERED, CornerRadii.EMPTY, Insets.EMPTY)));

        return  externalGridPane;
    }

    /**
     * This method sets up the GridPane and splits it into cols and rows
     * It's static because it is used by other grid panes too
     * @param pane the pane to split
     * @param externalCols the amount of cols
     * @param externalRows the amount of rows
     */
    private void setGrid(GridPane pane, int externalCols, int externalRows) {


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
}