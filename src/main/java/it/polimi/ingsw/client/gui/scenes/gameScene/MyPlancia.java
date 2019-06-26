package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.PlayerInfo;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class MyPlancia {

    private GridPane planciaGridPane;

    private PlayerInfo playerInfo;

    protected MyPlancia(PlayerColor playerColor, String cssClass,PlayerInfo playerInfo ){

        this.planciaGridPane = new GridPane();


        this.playerInfo = playerInfo;


        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(8.150);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(5.650);
        colPercentages.add(24.05);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(23.0000);
        rowPercentages.add(12.0000);
        rowPercentages.add(31.0000);
        rowPercentages.add(9.0000);
        rowPercentages.add(22.0000);
        rowPercentages.add(3.0000);

        setGrid(planciaGridPane, colPercentages, rowPercentages);
        planciaGridPane.setGridLinesVisible(true);
        //planciaGridPane.getStyleClass().add("visibleBorder");


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

        planciaGridPane.setBackground(new Background(backgroundImage));

        planciaGridPane.getStyleClass().add("whiteLines");


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

    protected Pane getplanciaGridPane(){
        return this.planciaGridPane;
    }
}
