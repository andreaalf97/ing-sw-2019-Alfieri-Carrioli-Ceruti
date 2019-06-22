package it.polimi.ingsw.client.gui.scenes.gameScene;


import it.polimi.ingsw.client.GameInfo;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.map.Spot;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.ArrayList;

public class BoardGrid {

    private final String username;

    public ArrayList<String> playerNames;

    public ArrayList<PlayerColor> playerColors;

    public int votedSkulls;

    private GridPane gridPane;

    private GridPane kstBox;

    private GridPane pointsBox;

    private GridPane leftSpawnWeaponBox;

    private GridPane topSpawnWeaponBox;

    private GridPane rightSpawnWeaponBox;

    private GridPane mapBox;

    private HBox doubleKillBox;

    private GameInfo gameInfo;

    //players and ammocards for every spot has to be put inside this Vbox (one for each spot)
    private ArrayList<VBox> stuffInEverySpot;

    protected BoardGrid(String mapPath, GameInfo gameInfo, int votedSkulls, ArrayList<String> plyersNames,  ArrayList<PlayerColor> playerColors, String username){

        this.username = username;

        this.votedSkulls = votedSkulls;

        this.playerNames = plyersNames;

        this.playerColors = playerColors;

        this.gameInfo = gameInfo;

        this.gridPane = setUpBackgroundGrid(mapPath);

        this.pointsBox = setUpPointsBox();

        this.kstBox = setUpKstBox(votedSkulls);

        this.leftSpawnWeaponBox = setUpLeftSpawnWeaponBox(gameInfo.gameMap.map[1][0]);

        this.topSpawnWeaponBox = setUpTopSpawnWeaponBox(gameInfo.gameMap.map[0][2]);

        this.rightSpawnWeaponBox = setUpRightSpawnWeaponBox(gameInfo.gameMap.map[2][3]);

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
        colPercentages.add(25.000);
        colPercentages.add(25.000);
        colPercentages.add(27.000);
        colPercentages.add(23.000);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(32.000);
        rowPercentages.add(35.000);
        rowPercentages.add(33.000);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        Pane ammocardPane = new Pane();

        for(int i = 0; i < gameInfo.gameMap.map.length; i++) {
            for(int j = 0; j < gameInfo.gameMap.map[i].length; j++) {
                VBox playersAndAmmoCardVBox = new VBox();
                HBox playersHbox = new HBox();
                this.stuffInEverySpot.add(playersAndAmmoCardVBox);
                /*if ( gameInfo.gameMap.map[i][j].getAmmoCard ){
                    //has a ammocard, then show the ammocard on the map in the right position
                }*/
                //if there are players in the spot I have to show them on the map
                if (!gameInfo.gameMap.map[i][j].getPlayersHere().isEmpty()){
                    for (String playerName : gameInfo.gameMap.map[i][j].getPlayersHere()){
                        PlayerColor color = playerColors.get(playerNames.indexOf(playerName));
                        /*Label label = new Label(▇);
                        label.setStyle("-fx-background-color:color");*/
                        playersHbox.getChildren().add(new Label(color.escape()+"▇"+ Color.RESET));
                    }
                }
            }
        }

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

    private GridPane setUpTopSpawnWeaponBox(Spot spot) {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(33.333);
        colPercentages.add(33.333);
        colPercentages.add(33.333);


        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(100.00);

        for( int i = 0; i < spot.getSpawnWeaponNames().size(); i++) {
            Image weaponImage = new Image("/graphics/cards/" +spot.getSpawnWeaponNames().get(i)+ ".png", 0, 0, true, false);
            gridPane.add(new ImageView(weaponImage), 0, i, 1, 1);
        }

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;
    }

    private GridPane setUpLeftSpawnWeaponBox(Spot spot) {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(100.00);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);

        for( int i = 0; i < spot.getSpawnWeaponNames().size(); i++) {
            Image weaponImage = new Image("/graphics/cards/" +spot.getSpawnWeaponNames().get(i)+ ".png", 0, 0, true, false);
            gridPane.add(new ImageView(weaponImage), i, 0, 1, 1);
        }

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;

    }

    private GridPane setUpRightSpawnWeaponBox(Spot spot) {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(100.00);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);

        for( int i = 0; i < spot.getSpawnWeaponNames().size(); i++) {
            Image weaponImage = new Image("/graphics/cards/" +spot.getSpawnWeaponNames().get(i)+ ".png", 0, 0, true, false);
            gridPane.add(new ImageView(weaponImage), i, 0, 1, 1);
        }

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;

    }

    private GridPane setUpKstBox(int votedSkulls) {

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

        for ( int i = 0; i < votedSkulls; i++){
            Label label = new Label("▇");
            label.setStyle("-fx-color:BLACK");
            gridPane.add(label, i, 0, 1, 1);
        }

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
