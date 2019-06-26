package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.PlayerInfo;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class MyPlancia {

    private GridPane planciaGridPane;

    private GridPane ammoGridPane;

    private GridPane damagesGridPane;

    private GridPane marksGridPane;

    private GridPane skullGridPane;

    private PlayerInfo playerInfo;

    protected MyPlancia(PlayerColor playerColor, String cssClass, PlayerInfo playerInfo ){

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
        colPercentages.add(4.500);
        colPercentages.add(15.000);
        colPercentages.add(4.500);


        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(23.0000);
        rowPercentages.add(12.0000);
        rowPercentages.add(31.000);
        rowPercentages.add(7.0000);
        rowPercentages.add(24.0000);
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

        setUpPlancia();

        planciaGridPane.setBackground(new Background(backgroundImage));

        planciaGridPane.getStyleClass().add("whiteLines");

    }

    private void setUpPlancia() {

        setUpAmmos();
        setUpMarks();
        setUpDamages();
        setUpSkulls();
    }

    private void setUpSkulls() {
    }

    private void setUpDamages() {
    }

    private void setUpMarks() {
    }

    private void setUpAmmos() {

        this.ammoGridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(33.333);
        colPercentages.add(33.333);
        colPercentages.add(33.333);


        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);


        setGrid(ammoGridPane, colPercentages, rowPercentages);
        ammoGridPane.setGridLinesVisible(true);
        planciaGridPane.add(ammoGridPane, 14, 1, 1, 3);

        for ( int i = 0; i < playerInfo.nYellowAmmo; i++){
            Image image = new Image(
                    "/graphics/ammo/YELLOW.png",
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
            pane.setPadding(new Insets(0, 0, 0, 4));
            pane.setBackground(new Background(backgroundImage));
            ammoGridPane.add(pane, i, 0);
        }
        for ( int i = 0; i < playerInfo.nRedAmmo; i++){
            Image image = new Image(
                    "/graphics/ammo/RED.png",
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
            pane.setPadding(new Insets(0, 0, 0, 4));
            pane.setBackground(new Background(backgroundImage));
            ammoGridPane.add(pane, i, 1);
        }
        for ( int i = 0; i < playerInfo.nBlueAmmo; i++){
            Image image = new Image(
                    "/graphics/ammo/BLUE.png",
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
            pane.setPadding(new Insets(0, 0, 0, 4));
            pane.setBackground(new Background(backgroundImage));
            ammoGridPane.add(pane, i, 2);
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

    protected Pane getplanciaGridPane(){
        return this.planciaGridPane;
    }
}
