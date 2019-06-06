package it.polimi.ingsw.client.gui.scenes;

import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.events.serverToClient.GameStartedQuestion;
import it.polimi.ingsw.model.map.MapName;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
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
    private final String myPlanciaPath = "/Grafica/Plance_giocatori/Blue/Blue_front.png";

    private final double screenRatioMin = 0.5500; // Screen ration of 16:9 is 0.5625
    private final double screenRatioMax = 0.5700;

    private final int externalRows = 27;
    private final int externalCols = 48;

    public GameScene(Stage window, String username, GameStartedQuestion event) {

        this.window = window;
        this.username = username;
        this.playerNames = event.playerNames;
        this.playerColors = event.playerColors;
        this.firstPlayer = event.firstPlayer;
        this.mapName = event.mapName;
        this.votedSkulls = event.votedSkulls;

        //this.mapPath = this.mapName.getPath();
        this.mapPath = "/Grafica/Boards/EARTH.png";


        //------------ Reading screen size ------------------------
        Rectangle2D screenVisibleBounds = Screen.getPrimary().getBounds();
        double aspectRatio = screenVisibleBounds.getHeight() / screenVisibleBounds.getWidth();

        System.err.println("Width --> " + screenVisibleBounds.getWidth());
        System.err.println("Height --> " + screenVisibleBounds.getHeight());
        System.err.println(aspectRatio);



        //Setting up the external grid
        GridPane externalGridPane = setUpExternalGridPane(externalCols, externalRows);

        //Setting up the map grid
        GridPane mapGridPane = setUpMapGridPane();

        //Adding the map grid to the main pane
        externalGridPane.add(mapGridPane,
                0,
                5,
                30,
                24
        );

        //Setting up the player's plancia
        HBox myPlancia = setUpMyPlanciaHBox();

        int col = 30;
        int row = 5;

        int offset = 5;

        ArrayList<PlayerColor> tempColors = new ArrayList<>(playerColors);

        int indexOfThisPlayer = playerNames.indexOf(username);

        tempColors.remove(indexOfThisPlayer);

        for(int i = 0; i < tempColors.size(); i++){

            HBox newBox= setUpOtherPlayerPlancia(tempColors.get(i));
            externalGridPane.add(newBox, col, row + (i * offset), 20, 5);

        }

        //Adduing the plancia to the main pane
        externalGridPane.add(myPlancia, 0, 0, 21, 5);


        //Setting up CSS for main pane
        externalGridPane.getStylesheets().add(cssPath);


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

    private HBox setUpOtherPlayerPlancia(PlayerColor playerColor) {

        HBox hBox = new HBox();

        System.err.println(playerColor.getPath());

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

    private HBox setUpMyPlanciaHBox() {

        HBox myPlancia = new HBox();

        Image myPlanciaImage = new Image(
                myPlanciaPath,
                0, 0,
                true, false
        );


        BackgroundImage myPlanciaBackground = new BackgroundImage(
                myPlanciaImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1, 1, true, true, true, false)
        );

        myPlancia.setBackground(new Background(myPlanciaBackground));

        return myPlancia;

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
        externalGridPane.setGridLinesVisible(true);

        setGrid(externalGridPane, nCols, nRows);

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