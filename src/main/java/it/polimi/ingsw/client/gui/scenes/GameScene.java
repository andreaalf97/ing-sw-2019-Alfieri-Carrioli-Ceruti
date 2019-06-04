package it.polimi.ingsw.client.gui.scenes;

import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.events.serverToClient.GameStartedQuestion;
import it.polimi.ingsw.model.map.MapName;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;

public class GameScene implements MyScene {


    private Scene scene;

    private final String username;

    public ArrayList<String> playerNames;

    public ArrayList<PlayerColor> playerColors;

    public String firstPlayer;

    public MapName mapName;

    public int votedSkulls;



    private final String mapPath = "src/main/resources/Grafica/Mappe/Parti/Part_1.png";
    private final String cssPath = "/style/gameStyle.css";

    private final int windowWidth = 750;
    private final int windowHeight = 500;



    private final int externalRows = windowHeight / 15;
    private final int externalCols = windowWidth / 15;

    public GameScene(String username, GameStartedQuestion event) {

        this.username = username;
        this.playerNames = event.playerNames;
        this.playerColors = event.playerColors;
        this.firstPlayer = event.firstPlayer;
        this.mapName = event.mapName;
        this.votedSkulls = event.votedSkulls;

        GridPane externalGridPane = new GridPane();
        externalGridPane.setGridLinesVisible(false);

        setGrid(externalGridPane, externalCols, externalRows);

        this.scene = new Scene(externalGridPane, windowWidth, windowHeight);



        GridPane mapGridPane = new GridPane();
        mapGridPane.setGridLinesVisible(true);

        setGrid(mapGridPane, 10, 10);

        mapGridPane.getStyleClass().add("mapGridPane");
        mapGridPane.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());

        externalGridPane.add(mapGridPane, 5, 10, 20, 40);


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