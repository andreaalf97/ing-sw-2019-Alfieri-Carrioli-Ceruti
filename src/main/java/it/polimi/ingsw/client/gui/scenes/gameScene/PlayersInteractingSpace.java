package it.polimi.ingsw.client.gui.scenes.gameScene;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;

import java.awt.*;
import java.util.ArrayList;

public class PlayersInteractingSpace {

    public GridPane getGridPane() {
        return gridPane;
    }

    private GridPane gridPane;

    public PlayersInteractingSpace(){

        this.gridPane = setGridPane();

        setWelcomeLabel();
    }

    private void setWelcomeLabel() {
        Label label = new Label("Game just started");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 1, 1, 5, 1);
    }

    private GridPane setGridPane() {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(25.000);
        rowPercentages.add(25.000);
        rowPercentages.add(25.000);
        rowPercentages.add(25.000);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    private void setGrid(GridPane pane, ArrayList<Double> colPercentages, ArrayList<Double> rowPercentages) {

        for(Double percentage : colPercentages){
            ColumnConstraints colConstrains = new ColumnConstraints();
            colConstrains.setPercentWidth(percentage);
            pane.getColumnConstraints().add(colConstrains);
        }


        for(Double percentage : rowPercentages){
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(percentage);
            pane.getRowConstraints().add(rowConstraints);
        }

    }
}
