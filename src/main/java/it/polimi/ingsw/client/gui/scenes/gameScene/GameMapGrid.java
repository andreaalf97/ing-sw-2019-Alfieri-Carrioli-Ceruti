package it.polimi.ingsw.client.gui.scenes.gameScene;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class GameMapGrid {


    private GridPane gridPane;

    private GridPane kstBox;

    private GridPane pointsBox;

    private GridPane leftSpawnWeaponBox;

    private GridPane topSpawnWeaponBox;

    private GridPane rightSpawnWeaponBox;

    private GridPane mapBox;

    private HBox doubleKillBox;




    protected GameMapGrid(String mapPath){

        this.gridPane = new GridPane();

        gridPane.setGridLinesVisible(false);

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




        //***************************++ POINTS BOX ++******************


        this.pointsBox = new GridPane();

        colPercentages = new ArrayList<>();
        colPercentages.add(100.00);

        rowPercentages = new ArrayList<>();
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);
        rowPercentages.add(16.666);

        setGrid(pointsBox, colPercentages, rowPercentages);
        pointsBox.setGridLinesVisible(true);


        //***************************++ KST ++***********************************

        this.kstBox = new GridPane();

        colPercentages = new ArrayList<>();
        colPercentages.add(10.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(11.000);
        colPercentages.add(10.000);
        colPercentages.add(14.000);

        rowPercentages = new ArrayList<>();
        rowPercentages.add(100.00);

        setGrid(kstBox, colPercentages, rowPercentages);
        kstBox.setGridLinesVisible(true);

        //***************************++ LEFT SPAWN WEAPON BOX ++******************

        this.leftSpawnWeaponBox = new GridPane();

        colPercentages = new ArrayList<>();
        colPercentages.add(100.00);

        rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);

        setGrid(leftSpawnWeaponBox, colPercentages, rowPercentages);
        leftSpawnWeaponBox.setGridLinesVisible(true);

        //***************************++ TOP SPAWN WEAPON BOX ++*******************


        this.topSpawnWeaponBox = new GridPane();

        colPercentages = new ArrayList<>();
        colPercentages.add(33.333);
        colPercentages.add(33.333);
        colPercentages.add(33.333);

        rowPercentages = new ArrayList<>();
        rowPercentages.add(100.00);

        setGrid(topSpawnWeaponBox, colPercentages, rowPercentages);
        topSpawnWeaponBox.setGridLinesVisible(true);

        //***************************++ RIGHT SPAWN WEAPON BOX ++*****************

        this.rightSpawnWeaponBox = new GridPane();

        colPercentages = new ArrayList<>();
        colPercentages.add(100.00);

        rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);

        setGrid(rightSpawnWeaponBox, colPercentages, rowPercentages);
        rightSpawnWeaponBox.setGridLinesVisible(true);


        //***************************++ MAP BOX ++********************************

        this.mapBox = new GridPane();

        colPercentages = new ArrayList<>();
        colPercentages.add(25.000);
        colPercentages.add(25.000);
        colPercentages.add(27.000);
        colPercentages.add(23.000);

        rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);

        setGrid(mapBox, colPercentages, rowPercentages);
        mapBox.setGridLinesVisible(true);

        //***************************++ DOUBLE KILL BOX ++******************

        this.doubleKillBox = new HBox();






        gridPane.add(pointsBox, 1, 1, 1, 5);
        gridPane.add(kstBox, 3, 2, 4, 1);
        gridPane.add(leftSpawnWeaponBox, 0, 8, 5, 2);
        gridPane.add(topSpawnWeaponBox, 8, 0, 1, 4);
        gridPane.add(rightSpawnWeaponBox, 10, 9, 1, 3);
        gridPane.add(mapBox, 6, 5, 3, 6);
        gridPane.add(doubleKillBox, 3, 5, 1, 2);


        pointsBox.getStyleClass().add("visibleBorder");
        kstBox.getStyleClass().add("visibleBorder");
        leftSpawnWeaponBox.getStyleClass().add("visibleBorder");
        topSpawnWeaponBox.getStyleClass().add("visibleBorder");
        rightSpawnWeaponBox.getStyleClass().add("visibleBorder");
        mapBox.getStyleClass().add("visibleBorder");
        doubleKillBox.getStyleClass().add("visibleBorder");

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
