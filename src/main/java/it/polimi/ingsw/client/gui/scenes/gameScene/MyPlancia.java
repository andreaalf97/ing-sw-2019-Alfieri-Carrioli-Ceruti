package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.PlayerInfo;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class MyPlancia {
    private final static String skullPath = "/graphics/Skull.png";

    private GridPane planciaGridPane;

    private GridPane ammoGridPane;

    private GridPane damagesGridPane;

    private GridPane marksGridPane;

    private GridPane skullGridPane;

    private PlayerInfo playerInfo;

    private ArrayList<String> playersNames;

    private ArrayList<PlayerColor> playersColors;

    private PlayerColor myColor;

    protected MyPlancia(PlayerColor playerColor, ArrayList<PlayerColor> playersColors, ArrayList<String> playersNames){

        this.planciaGridPane = new GridPane();
        this.myColor = playerColor;
        this.playersColors = playersColors;
        this.playersNames = playersNames;

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

        planciaGridPane.setBackground(new Background(backgroundImage));
    }

    public void update(PlayerInfo playerInfo) {

        this.playerInfo = playerInfo;

        setUpPlancia();
    }


    private void setUpPlancia() {

        if (damagesGridPane != null)
            planciaGridPane.getChildren().remove(damagesGridPane);

        if (marksGridPane != null)
            planciaGridPane.getChildren().remove(marksGridPane);

        if (ammoGridPane != null)
            planciaGridPane.getChildren().remove(ammoGridPane);

        if (skullGridPane != null)
            planciaGridPane.getChildren().remove(skullGridPane);

        setUpAmmos();
        setUpMarks();
        setUpDamages();
        setUpSkulls();
    }

    private void setUpSkulls() {

        this.skullGridPane = new GridPane();
        skullGridPane.setHgap(5);
        skullGridPane.setPadding(new Insets(6, 0, 0, 0));

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(12.500);
        colPercentages.add(12.500);
        colPercentages.add(12.500);
        colPercentages.add(12.500);
        colPercentages.add(12.500);
        colPercentages.add(12.500);
        colPercentages.add(12.500);
        colPercentages.add(12.500);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(100.000);

        setGrid(skullGridPane, colPercentages, rowPercentages);
        skullGridPane.setGridLinesVisible(true);
        planciaGridPane.add(skullGridPane, 3, 4, 8, 1);

        for (int i = 0; i < playerInfo.nDeaths; i++) {

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
            skullGridPane.add(pane, i, 0);
            GridPane.setMargin(pane, new Insets(0, 0, 0, 8));
        }

    }

    private void setUpDamages() {

        this.damagesGridPane = new GridPane();
        damagesGridPane.setPadding(new Insets(6, 0, 0, 0));

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(100.000);

        setGrid(damagesGridPane, colPercentages, rowPercentages);
        damagesGridPane.setGridLinesVisible(true);
        planciaGridPane.add(damagesGridPane, 1, 2, 12, 1);

        for (int i = 0; i < playerInfo.damages.size(); i++){

            String playerName = playerInfo.damages.get(i);
            PlayerColor colorOfThisPlayer = playersColors.get(playersNames.indexOf(playerName));

            Image image = new Image(
                    "/graphics/drops/"+ colorOfThisPlayer.toString().toUpperCase()+".png",
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
            pane.setPadding(new Insets(5, 0, 0, 2));
            pane.setBackground(new Background(backgroundImage));
            damagesGridPane.add(pane, i, 0);
        }
    }

    private void setUpMarks() {

        this.marksGridPane = new GridPane();
        marksGridPane.setPadding(new Insets(6, 0, 0, 0));

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);
        colPercentages.add(8.333);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(100.000);

        setGrid(marksGridPane, colPercentages, rowPercentages);
        marksGridPane.setGridLinesVisible(true);
        planciaGridPane.add(marksGridPane, 1, 0, 12, 1);


        for (int i = 0; i < playerInfo.marks.size(); i++){

            String playerName = playerInfo.marks.get(i);
            PlayerColor colorOfThisPlayer = playersColors.get(playersNames.indexOf(playerName));

            Image image = new Image(
                    "/graphics/drops/"+ colorOfThisPlayer.toString().toUpperCase()+".png",
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
            int position = 11 - i;
            Pane pane = new Pane();
            pane.setPadding(new Insets(5, 0, 0, 2));
            pane.setBackground(new Background(backgroundImage));
            marksGridPane.add(pane, position, 0);

        }
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
