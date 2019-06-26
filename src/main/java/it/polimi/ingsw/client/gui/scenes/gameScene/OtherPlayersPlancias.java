package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.GameInfo;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.model.Player;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class OtherPlayersPlancias {

    private ArrayList<GridPane> gridPanes;

    private  ArrayList<GridPane> damagesGridPanes;

    private  ArrayList<GridPane> marksGridPanes;

    private  ArrayList<GridPane> skullGridPanes;

    private ArrayList<GridPane> ammoGridPanes;

    private GameInfo gameInfo;

    private ArrayList<PlayerColor> playerColors;

    protected OtherPlayersPlancias(ArrayList<String> otherPlayers, ArrayList<PlayerColor> tempColors, GameInfo gameInfo){

        this.gridPanes = new ArrayList<>();
        this.damagesGridPanes = new ArrayList<>();
        this.marksGridPanes = new ArrayList<>();
        this.skullGridPanes = new ArrayList<>();
        this.ammoGridPanes = new ArrayList<>();

        this.gameInfo = gameInfo;

        this.playerColors = tempColors;

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
            colPercentages.add(4.500);
            colPercentages.add(18.76);
            colPercentages.add(4.500);


            ArrayList<Double> rowPercentages = new ArrayList<>();
            rowPercentages.add(23.0000);
            rowPercentages.add(12.0000);
            rowPercentages.add(31.0000);
            rowPercentages.add(9.0000);
            rowPercentages.add(22.0000);
            rowPercentages.add(3.0000);

            setGrid(gridPanes.get(i), colPercentages, rowPercentages);
            gridPanes.get(i).setGridLinesVisible(true);
            //gridPanes.get(i).getStyleClass().add("visibleBorder");


            Image image = new Image(
                    playerColors.get(i).getPath(),
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
            setUpPlancia(i);
            gridPanes.get(i).setBackground(new Background(backgroundImage));

            gridPanes.get(i).getStyleClass().add("whiteLines");
        }


    }

    private void setUpPlancia(int i) {

        setUpAmmos(i);
        setUpMarks(i);
        setUpDamages(i);
        setUpSkulls(i);

    }

    private void setUpSkulls(int i) {
    }

    private void setUpDamages(int i) {
    }

    private void setUpMarks(int i) {
    }

    private void setUpAmmos(int indexOfPlayer) {

        GridPane gridPane = new GridPane();
        ammoGridPanes.add(gridPane);

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(33.333);
        colPercentages.add(33.333);
        colPercentages.add(33.333);


        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);
        rowPercentages.add(33.333);


        setGrid(ammoGridPanes.get(indexOfPlayer), colPercentages, rowPercentages);
        ammoGridPanes.get(indexOfPlayer).setGridLinesVisible(true);
        gridPanes.get(indexOfPlayer).add(ammoGridPanes.get(indexOfPlayer), 14, 1, 1, 3);

        for ( int i = 0; i < gameInfo.playersInfo.get(indexOfPlayer).getnYellowAmmo(); i++){
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
            ammoGridPanes.get(indexOfPlayer).add(pane, i, 0);
        }
        for ( int i = 0; i < gameInfo.playersInfo.get(indexOfPlayer).getnRedAmmo(); i++){
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
            ammoGridPanes.get(indexOfPlayer).add(pane, i, 1);
        }
        for ( int i = 0; i < gameInfo.playersInfo.get(indexOfPlayer).getnBlueAmmo(); i++){
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
            ammoGridPanes.get(indexOfPlayer).add(pane, i, 2);
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
