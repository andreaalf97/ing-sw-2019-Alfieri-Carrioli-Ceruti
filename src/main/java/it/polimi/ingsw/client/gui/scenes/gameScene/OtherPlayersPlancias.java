package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.PlayerColor;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class OtherPlayersPlancias {

    private ArrayList<GridPane> gridPanes;

    protected OtherPlayersPlancias(ArrayList<String> otherPlayers, ArrayList<PlayerColor> tempColors){

        this.gridPanes = new ArrayList<>();

        for( int i = 0; i < otherPlayers.size(); i++) {

            GridPane gridpane = new GridPane();
            getGridPanes().add(gridpane);

            ArrayList<Double> colPercentages = new ArrayList<>();
            colPercentages.add(8.150);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(5.100);
            colPercentages.add(27.76);

            ArrayList<Double> rowPercentages = new ArrayList<>();
            rowPercentages.add(35.0000);
            rowPercentages.add(31.0000);
            rowPercentages.add(9.0000);
            rowPercentages.add(22.0000);
            rowPercentages.add(3.0000);

            setGrid(gridPanes.get(i), colPercentages, rowPercentages);
            gridPanes.get(i).setGridLinesVisible(true);
            //gridPanes.get(i).getStyleClass().add("visibleBorder");


            Image image = new Image(
                    tempColors.get(i).getPath(),
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

            gridPanes.get(i).setBackground(new Background(backgroundImage));

            gridPanes.get(i).getStyleClass().add("whiteLines");
        }


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

    protected ArrayList<GridPane> getGridPanes(){
        return this.gridPanes;
    }
}
