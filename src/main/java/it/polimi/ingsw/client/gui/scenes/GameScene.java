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

        GridPane externalGridPane = new GridPane();
        externalGridPane.setGridLinesVisible(true);

        setGrid(externalGridPane, externalCols, externalRows);

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

        mapGridPane.getStylesheets().add(cssPath);

        externalGridPane.add(mapGridPane,
                0,
                0,
                36,
                27
        );

        Rectangle2D screenVisibleBounds = Screen.getPrimary().getBounds();
        double aspectRatio = screenVisibleBounds.getHeight() / screenVisibleBounds.getWidth();

        System.err.println("Width --> " + screenVisibleBounds.getWidth());
        System.err.println("Height --> " + screenVisibleBounds.getHeight());
        System.err.println(aspectRatio);


        this.scene = new Scene(externalGridPane, 1120, 630);

        if(aspectRatio < screenRatioMax && aspectRatio > screenRatioMin){
            window.setFullScreen(true);

            window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        }
        else {
            window.setResizable(false);
        }


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