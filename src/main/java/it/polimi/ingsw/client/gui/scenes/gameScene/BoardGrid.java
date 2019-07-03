package it.polimi.ingsw.client.gui.scenes.gameScene;


import it.polimi.ingsw.client.GameInfo;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.gui.ImageDisplay;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.map.AmmoSpot;
import it.polimi.ingsw.model.map.GameMap;
import it.polimi.ingsw.model.map.Spot;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.ArrayList;

public class BoardGrid {

    private final static String skullPath = "/graphics/Skull.png";
    private final String username;

    public ArrayList<String> playerNames;

    public ArrayList<PlayerColor> playerColors;

    public int votedSkulls;

    public GridPane gridPane;

    public GridPane kstBox;

    public GridPane pointsBox;

    public GridPane leftSpawnWeaponBox;

    public GridPane topSpawnWeaponBox;

    public GridPane rightSpawnWeaponBox;

    public GridPane mapBox;

    public HBox doubleKillBox;

    public GameInfo gameInfo;

    private Spot[][] gameMap;

    //players and ammocards for every spot has to be put inside this Vbox (one for each spot)
    private ArrayList<GridPane> stuffInEverySpot;

    protected BoardGrid(String mapPath, int votedSkulls, ArrayList<String> plyersNames,  ArrayList<PlayerColor> playerColors, String username){

        this.username = username;

        this.votedSkulls = votedSkulls;

        this.playerNames = plyersNames;

        this.playerColors = playerColors;

        this.gridPane = setUpBackgroundGrid(mapPath);

        this.pointsBox = new GridPane();

        this.kstBox = setUpKstBox(votedSkulls);

        this.leftSpawnWeaponBox = new GridPane();

        this.topSpawnWeaponBox = new GridPane();

        this.rightSpawnWeaponBox = new GridPane();

        this.mapBox = new GridPane();

        this.doubleKillBox = new HBox();

        this.stuffInEverySpot = new ArrayList<>();

        gridPane.add(pointsBox, 1, 1, 1, 5);
        gridPane.add(kstBox, 3, 2, 4, 1);
        gridPane.add(leftSpawnWeaponBox, 0, 8, 5, 2);
        gridPane.add(topSpawnWeaponBox, 8, 0, 1, 4);
        gridPane.add(rightSpawnWeaponBox, 10, 9, 1, 3);
        gridPane.add(mapBox, 6, 5, 3, 6);
        gridPane.add(doubleKillBox, 3, 5, 1, 2);

    }

    public void update(GameInfo gameInfo) {

        if (kstBox != null)
            gridPane.getChildren().remove(kstBox);
        this.kstBox = setUpKstBox(gameInfo.killShotTrack.getSkullList().size());
        gridPane.add(kstBox, 3, 2, 4, 1);

        if (pointsBox != null)
            gridPane.getChildren().remove(pointsBox);
        this.pointsBox = setUpPointsBox();
        gridPane.add(pointsBox, 1, 1, 1, 5);

        gameMap = gameInfo.gameMap.getMap();

        if (leftSpawnWeaponBox != null)
            gridPane.getChildren().remove(leftSpawnWeaponBox);
        this.leftSpawnWeaponBox = setUpLeftSpawnWeaponBox(gameMap[1][0]);
        gridPane.add(leftSpawnWeaponBox, 0, 8, 5, 2);

        if (topSpawnWeaponBox != null)
            gridPane.getChildren().remove(topSpawnWeaponBox);
        this.topSpawnWeaponBox = setUpTopSpawnWeaponBox(gameMap[0][2]);
        gridPane.add(topSpawnWeaponBox, 8, 0, 1, 4);

        if (rightSpawnWeaponBox != null)
            gridPane.getChildren().remove(rightSpawnWeaponBox);
        this.rightSpawnWeaponBox = setUpRightSpawnWeaponBox(gameMap[2][3]);
        gridPane.add(rightSpawnWeaponBox, 10, 9, 1, 3);

        if (stuffInEverySpot != null) {
            gridPane.getChildren().remove(stuffInEverySpot);
        }
        this.stuffInEverySpot = new ArrayList<>(12);

        if (mapBox != null)
            gridPane.getChildren().remove(mapBox);
        this.mapBox = setUpMapBox(gameInfo);
        gridPane.add(mapBox, 6, 5, 3, 6);

        if (doubleKillBox != null)
            gridPane.getChildren().remove(doubleKillBox);
        gridPane.add(doubleKillBox, 3, 5, 1, 2);

    }


    private GridPane setUpMapBox(GameInfo gameInfo) {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(25.000);
        colPercentages.add(25.000);
        colPercentages.add(25.000);
        colPercentages.add(25.000);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(32.000);
        rowPercentages.add(35.000);
        rowPercentages.add(33.000);

        setGrid(gridPane, colPercentages, rowPercentages);

        for(int i = 0; i < gameInfo.gameMap.map.length; i++) {
            for(int j = 0; j < gameInfo.gameMap.map[i].length; j++) {
                if (gameInfo.gameMap.map[i][j] != null) {

                    GridPane cellGridpane = new GridPane();
                    stuffInEverySpot.add(cellGridpane);
                    ArrayList<Double> colPercentages1 = new ArrayList<>();
                    colPercentages1.add(20.000);
                    colPercentages1.add(20.000);
                    colPercentages1.add(20.000);
                    colPercentages1.add(20.000);
                    colPercentages1.add(20.000);

                    ArrayList<Double> rowPercentages1 = new ArrayList<>();
                    rowPercentages1.add(50.000);
                    rowPercentages1.add(50.000);

                    setGrid(cellGridpane, colPercentages1, rowPercentages1);
                    cellGridpane.setGridLinesVisible(false);
                    cellGridpane.setPadding(new Insets(20));

                    gridPane.add(cellGridpane, j, i, 1, 1);

                    //has a ammocard, then show the ammocard on the map in the right position
                    if (gameInfo.gameMap.map[i][j].isAmmoSpot()) {
                        AmmoSpot ammoSpot = (AmmoSpot) gameInfo.gameMap.map[i][j];

                        String imgePath = ammoSpot.getAmmoCard().getAmmoCardImagePath();
                        Image image = new Image(
                                imgePath,
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
                        cellGridpane.add(pane, 0, 0, 2, 1);
                    }
                    //if there are players in the spot I have to show them on the map
                    if (!gameInfo.gameMap.map[i][j].getPlayersHere().isEmpty()) {
                        int k = 0;
                        for (String player : gameInfo.gameMap.map[i][j].getPlayersHere()){
                            int indexOfPlayer = playerNames.indexOf(player);
                            PlayerColor color = playerColors.get(indexOfPlayer);
                            Image image = new Image(
                                    "/graphics/players_pawns/" +color.toString()+ ".png",
                                    0, 0,
                                    true, false
                            );
                            /*ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(50);
                            imageView.setFitWidth(25);
                            playersHbox.getChildren().add(imageView);
                            Image image = new Image(
                                    "/graphics/players_pawns/" +color.toString()+ ".png",
                                    0, 0,
                                    true, false
                            );*/

                            BackgroundImage backgroundImage = new BackgroundImage(
                                    image,
                                    BackgroundRepeat.NO_REPEAT,
                                    BackgroundRepeat.NO_REPEAT,
                                    BackgroundPosition.DEFAULT,
                                    new BackgroundSize(1, 1, true, true, true, false)
                            );
                            Pane pane = new Pane();
                            pane.setBackground(new Background(backgroundImage));
                            cellGridpane.add(pane, k, 1, 1, 1);
                            k++;
                        }
                    }
                }
            }
        }


        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;

    }

    private GridPane setUpTopSpawnWeaponBox(Spot spot) {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0, 0, 0, 10));

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(36.000);
        colPercentages.add(32.000);
        colPercentages.add(32.000);


        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(100.00);

        /*for( int i = 0; i < spot.getSpawnWeaponNames().size(); i++) {
            Image weaponImage = new Image("/graphics/cards/" +spot.getSpawnWeaponNames().get(i)+ ".png", 0, 0, true, false);
            ImageView weaponImageView = new ImageView(weaponImage);
            weaponImageView.setFitWidth(90);
            weaponImageView.setFitHeight(120);
            gridPane.add(weaponImageView, i, 0, 1, 1);
        }*/

        for ( int i = 0; i <spot.getSpawnWeaponNames().size(); i++){
            Image image = new Image(
                    "/graphics/cards/" +spot.getSpawnWeaponNames().get(i)+ ".png",
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
            pane.setPadding(new Insets(0, 0, 0, 10));
            pane.setBackground(new Background(backgroundImage));
            gridPane.add(pane, i, 0);
        }

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);

        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;
    }

    private GridPane setUpLeftSpawnWeaponBox(Spot spot) {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5));

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(100.00);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);

        /*for( int i = 0; i < spot.getSpawnWeaponNames().size(); i++) {
            Image weaponImage = new Image("/graphics/cards/" +spot.getSpawnWeaponNames().get(i)+ ".png", 0, 0, false, false);
            ImageView weaponImageView = new ImageView(weaponImage);
            weaponImageView.setFitWidth(90);
            weaponImageView.setFitHeight(100);
            weaponImageView.setRotate(270);
            gridPane.add(weaponImageView, 0, i);
        }*/

        for ( int i = 0; i <spot.getSpawnWeaponNames().size(); i++){
            Image image = new Image(
                    "/graphics/cards/" +spot.getSpawnWeaponNames().get(i)+ ".png",
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
            gridPane.add(pane, 0, i);
        }

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(false);

        gridPane.getStyleClass().add("visibleBorder");

        return gridPane;
    }

    private GridPane setUpRightSpawnWeaponBox(Spot spot) {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5));

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(100.00);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);

        /*for( int i = 0; i < spot.getSpawnWeaponNames().size(); i++) {
            Image weaponImage = new Image("/graphics/cards/" +spot.getSpawnWeaponNames().get(i)+ ".png", 0, 0, false, false);
            ImageView weaponImageView = new ImageView(weaponImage);
            weaponImageView.setFitWidth(90);
            weaponImageView.setFitHeight(100);
            weaponImageView.setRotate(90);
            gridPane.add(weaponImageView, 0, i);
        }*/

        for ( int i = 0; i <spot.getSpawnWeaponNames().size(); i++){
            Image image = new Image(
                    "/graphics/cards/" +spot.getSpawnWeaponNames().get(i)+ ".png",
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
            gridPane.add(pane, 0, i);
        }

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(false);

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

        /*for ( int i = 0; i < votedSkulls; i++){
            Image skullammoCardImage = new Image(skullPath, 0, 0, true, false);
            ImageView skullammoCardImageView = new ImageView(skullammoCardImage);
            skullammoCardImageView.setFitWidth(35);
            skullammoCardImageView.setFitHeight(35);
            gridPane.add(skullammoCardImageView, i, 0, 1, 1);
        }*/

        for ( int i = 0; i < votedSkulls; i++){
            Image image = new Image(
                    skullPath,
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
            gridPane.add(pane, i, 0);
        }

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(false);

        //gridPane.getStyleClass().add("visibleBorder");

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
        gridPane.setGridLinesVisible(false);

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
