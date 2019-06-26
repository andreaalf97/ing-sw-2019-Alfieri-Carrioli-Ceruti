package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.PlayerInfo;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class CardBox {

    private GridPane gridPane;

    protected CardBox(PlayerInfo playerInfo){

        this.gridPane = setUpGridpane();

        int i = 0;
        for ( String weaponName : playerInfo.weaponNames){
            Image image = new Image(
                    "/graphics/cards/" +weaponName+ ".png",
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

            Pane pane = new Pane();
            pane.setBackground(new Background(backgroundImage));
            gridPane.add(pane, i+i, 1);
        }

        int j = 0;
        for ( String powerUpName : playerInfo.powerUpNames ){
            Image image = new Image(
                    "/graphics/cards/" +powerUpName.toUpperCase()+"_"+ playerInfo.powerUpColors.get(j).toString()+".png",
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

            Pane pane = new Pane();
            pane.setBackground(new Background(backgroundImage));
            gridPane.add(pane, j+j, 3);
        }
        gridPane.getStyleClass().add("purpleLines");

    }

    private GridPane setUpGridpane() {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(31.000);
        colPercentages.add(3.333);
        colPercentages.add(31.000);
        colPercentages.add(3.333);
        colPercentages.add(31.000);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(1.000);
        rowPercentages.add(55.000);
        rowPercentages.add(7.000);
        rowPercentages.add(47.000);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        gridPane.getStyleClass().add("visibleBorder");

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

    protected GridPane getGridPane(){
        return this.gridPane;
    }


}
