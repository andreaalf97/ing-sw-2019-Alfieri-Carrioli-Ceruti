package it.polimi.ingsw.client.gui.scenes;

import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.events.serverToClient.GameStartedQuestion;
import it.polimi.ingsw.model.map.MapName;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameScene implements MyScene {

    private Stage window;

    private Scene scene;

    private final String username;

    public ArrayList<String> playerNames;

    public ArrayList<PlayerColor> playerColors;

    public String firstPlayer;

    public MapName mapName;

    public int votedSkulls;



    private final String mapPath;
    private final String cssPath = "/style/gameStyle.css";

    private final double screenRatioMin = 0.5500; // Screen ration of 16:9 is 0.5625
    private final double screenRatioMax = 0.5700;

    private final int externalRows = 27;
    private final int externalCols = 48;

    private final int otherPlanciasCol = 30;
    private final int otherPlanciasRow = 5;
    private final int otherPlanciasOffset = 5;


    public GameScene(Stage window, String username, GameStartedQuestion event) {

        this.window = window;
        this.username = username;
        this.playerNames = event.playerNames;
        this.playerColors = event.playerColors;
        this.firstPlayer = event.firstPlayer;
        this.mapName = event.mapName;
        this.votedSkulls = event.votedSkulls;

        //this.mapPath = this.mapName.getPath();
        this.mapPath = mapName.getPath();

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
        GridPane mapGridPane = setUpMapGridPane();
        mapGridPane.getStyleClass().add("rightShadow");


        //Adding the map grid to the main pane
        externalGridPane.add(mapGridPane,0,5,30,24);

        //Setting up the player's plancia
        PlayerColor myColor = playerColors.get(playerNames.indexOf(username));
        HBox myPlancia = setUpPlanciaByPlayerColor(myColor);
        myPlancia.getStyleClass().add("rightShadow");


        //Setting up all other players' plancias
        setUpOtherPlancias(externalGridPane, otherPlanciasCol, otherPlanciasRow, otherPlanciasOffset);

        //Adding the plancia to the main pane
        externalGridPane.add(myPlancia, 0, 0, 22, 5);

        //The message viewer
        Label messages = new Label("This is Adrenalina bitches");
        messages.getStyleClass().add("messages");
        messages.setTextAlignment(TextAlignment.CENTER);

        System.err.println("Font --> " + messages.getFont());

        externalGridPane.add(messages, 30, 24, 18, 3);


        setUpPlayerPowerUps(externalGridPane, 23, 1, 3, 3);
        setUpPlayerWeapons(externalGridPane, 33, 1, 3, 3);


        setUpLogo(externalGridPane, 44, 0, 4, 4);




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

    private void setUpPlayerWeapons(GridPane externalGridPane, int col, int row, int colspan, int rowspan) {

        ArrayList<String> tempPaths = new ArrayList<>();
        tempPaths.add("/graphics/cards/AD_weapons_IT_0220.png");
        tempPaths.add("/graphics/cards/AD_weapons_IT_0217.png");
        tempPaths.add("/graphics/cards/AD_weapons_IT_028.png");

        for(int i = 0; i < tempPaths.size(); i++){

            HBox newCard = new HBox();

            Image image = new Image(
                    tempPaths.get(i),
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

            newCard.setBackground(new Background(backgroundImage));

            newCard.getStyleClass().add("shadow");

            externalGridPane.add(newCard, col + (i*colspan), row, colspan, rowspan);

        }

    }

    private void setUpPlayerPowerUps(GridPane externalGridPane, int col, int row, int colspan, int rowspan) {

        for(int i = 0; i < 3; i++){

            HBox newCard = new HBox();

            Image image = new Image(
                    "/graphics/cards/AD_powerups_IT_02.png",
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

            newCard.setBackground(new Background(backgroundImage));

            newCard.getStyleClass().add("shadow");

            externalGridPane.add(newCard, col + (i*colspan), row, colspan, rowspan);

        }


    }

    private void setUpOtherPlancias(GridPane externalGridPane, int col, int row, int offset) {

        ArrayList<PlayerColor> tempColors = new ArrayList<>(playerColors);

        int indexOfThisPlayer = playerNames.indexOf(username);

        tempColors.remove(indexOfThisPlayer);

        for(int i = 0; i < tempColors.size(); i++){

            HBox newBox= setUpPlanciaByPlayerColor(tempColors.get(i));

            newBox.getStyleClass().add("shadow");

            externalGridPane.add(newBox, col, row + (i * offset), 19, 4);

        }

    }

    private HBox setUpPlanciaByPlayerColor(PlayerColor playerColor) {

        HBox hBox = new HBox();

        Image image = new Image(
                playerColor.getPath(),
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

        hBox.setBackground(new Background(backgroundImage));

        return hBox;

    }

    private GridPane setUpMapGridPane() {

        GridPane mapGridPane = new GridPane();

        mapGridPane.setGridLinesVisible(false);

        setGrid(mapGridPane, 4, 3);

        mapGridPane.getStyleClass().add("mapGridPane");

        BackgroundImage mapBackgroundImage= new BackgroundImage(
                new Image(mapPath, 0, 0, true, false) ,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1, 1, true, true, true, false)
        );

        mapGridPane.setBackground(new Background(mapBackgroundImage));


        return mapGridPane;
    }

    private GridPane setUpExternalGridPane(int nCols, int nRows) {

        GridPane externalGridPane = new GridPane();
        externalGridPane.setGridLinesVisible(false);

        setGrid(externalGridPane, nCols, nRows);

        //Setting up CSS for main pane
        externalGridPane.getStylesheets().add(cssPath);
        externalGridPane.getStyleClass().add("mainBackground");


        //externalGridPane.setBackground(new Background(new BackgroundFill(Color.ORANGERED, CornerRadii.EMPTY, Insets.EMPTY)));

        return  externalGridPane;
    }

    /**
     * This method sets up the GridPane and splits it into cols and rows
     * @param externalGridPane the pane to split
     * @param externalCols the amount of cols
     * @param externalRows the amount of rows
     */
    private void setGrid(GridPane externalGridPane, int externalCols, int externalRows) {

        for(int i = 0; i < externalCols; i++){
            ColumnConstraints colConstrains = new ColumnConstraints();
            colConstrains.setPercentWidth(100.0 / externalCols);
            externalGridPane.getColumnConstraints().add(colConstrains);
        }

        for(int i = 0; i < externalRows; i++){
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / externalRows);
            externalGridPane.getRowConstraints().add(rowConstraints);
        }

    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}