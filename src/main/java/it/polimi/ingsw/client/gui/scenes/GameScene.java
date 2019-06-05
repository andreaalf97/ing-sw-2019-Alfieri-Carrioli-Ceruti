package it.polimi.ingsw.client.gui.scenes;

import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.events.serverToClient.GameStartedQuestion;
import it.polimi.ingsw.model.map.MapName;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class GameScene implements MyScene {


    private Scene scene;

    private final String username;

    public ArrayList<String> playerNames;

    public ArrayList<PlayerColor> playerColors;

    public String firstPlayer;

    public MapName mapName;

    public int votedSkulls;



    private final String mapPath;
    private final String cssPath = "/style/gameStyle.css";

    private final int windowWidth = 750;
    private final int windowHeight = 500;


    private final int externalRows = windowHeight / 20;
    private final int externalCols = windowWidth / 20;

    public GameScene(String username, GameStartedQuestion event) {

        this.username = username;
        this.playerNames = event.playerNames;
        this.playerColors = event.playerColors;
        this.firstPlayer = event.firstPlayer;
        this.mapName = event.mapName;
        this.votedSkulls = event.votedSkulls;

        this.mapPath = this.mapName.getPath();

        GridPane externalGridPane = new GridPane();
        externalGridPane.setGridLinesVisible(true);

        setGrid(externalGridPane, externalCols, externalRows);




        GridPane mapGridPane = new GridPane();
        mapGridPane.setGridLinesVisible(false);

        setGrid(mapGridPane, 10, 10);

        mapGridPane.getStyleClass().add("mapGridPane");

        System.err.println(mapPath);

        BackgroundImage mapBackgroundImage= new BackgroundImage(
                new Image(mapPath),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1, 1, true, true, true, false)
        );

        mapGridPane.setBackground(new Background(mapBackgroundImage));

        mapGridPane.getStylesheets().add(cssPath);

        externalGridPane.add(
                mapGridPane,
                0,    //Column Index
                7,    //Row Index
                20,     //colspan
                18      //rowspan
                );



        this.scene = new Scene(externalGridPane, windowWidth, windowHeight);

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