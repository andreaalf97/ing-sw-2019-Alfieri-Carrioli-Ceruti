package it.polimi.ingsw.client.gui.scenes.gameScene;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class GameMapGrid {


    private GridPane gridPane;


    protected GameMapGrid(String mapPath){

        this.gridPane = new GridPane();

        gridPane.setGridLinesVisible(true);

        setGrid(gridPane);

        gridPane.getStyleClass().add("mapGridPane");

        BackgroundImage mapBackgroundImage= new BackgroundImage(
                new Image(mapPath, 0, 0, true, false) ,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1, 1, true, true, true, false)
        );

        gridPane.setBackground(new Background(mapBackgroundImage));

        gridPane.getStyleClass().add("rightShadow");

    }

    private void setGrid(GridPane pane) {

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(14.3);
        colPercentages.add(1.3);
        colPercentages.add(68.5);
        colPercentages.add(2.0);

        Double lastCol = 100.0;
        for(Double p : colPercentages)
            lastCol -= p;

        colPercentages.add(lastCol);

        for(Double percentage : colPercentages){
            ColumnConstraints colConstrains = new ColumnConstraints();
            colConstrains.setPercentWidth(percentage);
            pane.getColumnConstraints().add(colConstrains);
        }
        for(int i = 0; i < 3; i++){
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / 3);
            pane.getRowConstraints().add(rowConstraints);
        }

    }

    protected GridPane getGridPane(){
        return this.gridPane;
    }

}
