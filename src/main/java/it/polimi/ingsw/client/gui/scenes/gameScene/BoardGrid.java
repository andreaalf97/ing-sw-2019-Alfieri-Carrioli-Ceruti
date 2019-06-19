package it.polimi.ingsw.client.gui.scenes.gameScene;


import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class BoardGrid {

    private GridPane gridPane;

    private GridPane kstBox;

    private GridPane pointsBox;

    private GridPane leftSpawnWeaponBox;

    private GridPane topSpawnWeaponBox;

    private GridPane rightSpawnWeaponBox;

    private GridPane mapBox;

    private HBox doubleKillBox;


    protected BoardGrid(String mapPath){

        this.gridPane = setUpBackgroundGrid(mapPath);

        this.pointsBox = setUpPointsBox();

        this.kstBox = setUpKstBox();

        this.leftSpawnWeaponBox = setUpVerticalSpawnWeaponBox();

        this.topSpawnWeaponBox = setUpHorizontalSpawnWeaponBox();

        this.rightSpawnWeaponBox = setUpVerticalSpawnWeaponBox();

        this.mapBox = setUpMapBox();

        this.doubleKillBox = new HBox();

        gridPane.add(pointsBox, 1, 1, 1, 5);
        gridPane.add(kstBox, 3, 2, 4, 1);
        gridPane.add(leftSpawnWeaponBox, 0, 8, 5, 2);
        gridPane.add(topSpawnWeaponBox, 8, 0, 1, 4);
        gridPane.add(rightSpawnWeaponBox, 10, 9, 1, 3);
        gridPane.add(mapBox, 6, 5, 3, 6);
        gridPane.add(doubleKillBox, 3, 5, 1, 2);

    }

    private GridPane setUpMapBox() {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(6.000);
        colPercentages.add(6.000);
        colPercentages.add(13.000);
        colPercentages.add(25.000);
        colPercentages.add(27.000);
        colPercentages.add(23.000);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(6.000);
        rowPercentages.add(26.000);
        rowPercentages.add(35.000);
        rowPercentages.add(33.000);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        Pane ammocardPane = new Pane();

        BackgroundImage mapBackgroundImage= new BackgroundImage(
                new Image("/graphics/ammo/ammo_2.png", 0, 0, true, false) ,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1, 1, true, true, true, false)
        );

        ammocardPane.setBackground(new Background(mapBackgroundImage));

        gridPane.add(ammocardPane, 1, 1, 1, 1);


        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;

    }

    private GridPane setUpHorizontalSpawnWeaponBox() {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(33.333);
        colPercentages.add(33.333);
        colPercentages.add(33.333);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(100.00);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        gridPane.getStyleClass().add("visibleBorder");


        return gridPane;

    }

    private GridPane setUpVerticalSpawnWeaponBox() {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(100.00);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;

    }

    private GridPane setUpKstBox() {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(10.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(10.000);
        colPercentages.add(14.000);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(100.00);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;

    }

    private GridPane setUpPointsBox() {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(100.00);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;

    }

    private GridPane setUpBackgroundGrid(String mapPath) {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(1.6400);
        colPercentages.add(2.5500);
        colPercentages.add(2.6000);
        colPercentages.add(4.0000);
        colPercentages.add(3.5000);
        colPercentages.add(1.4035);
        colPercentages.add(29.560);
        colPercentages.add(6.4327);
        colPercentages.add(30.760);
        colPercentages.add(1.9000);
        colPercentages.add(13.550);
        colPercentages.add(2.1038);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(2.1000);
        rowPercentages.add(2.6000);
        rowPercentages.add(7.5200);
        rowPercentages.add(6.6500);
        rowPercentages.add(1.7600);
        rowPercentages.add(1.4000);
        rowPercentages.add(6.0000);
        rowPercentages.add(8.4000);
        rowPercentages.add(20.400);
        rowPercentages.add(21.400);
        rowPercentages.add(10.900);
        rowPercentages.add(9.4400);
        rowPercentages.add(1.4300);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(false);

        gridPane.getStyleClass().add("yellowLines");

        BackgroundImage mapBackgroundImage= new BackgroundImage(
                new Image(mapPath, 0, 0, true, false) ,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1, 1, true, true, true, false)
        );

        gridPane.setBackground(new Background(mapBackgroundImage));

        gridPane.getStyleClass().add("rightShadow");

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
