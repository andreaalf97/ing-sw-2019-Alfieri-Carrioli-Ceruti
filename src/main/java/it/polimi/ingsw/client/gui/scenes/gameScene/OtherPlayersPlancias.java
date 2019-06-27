package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.GameInfo;
import it.polimi.ingsw.client.PlayerColor;
import it.polimi.ingsw.client.gui.OtherPlayerInfo;
import it.polimi.ingsw.model.Player;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class OtherPlayersPlancias {

    private final static String skullPath = "/graphics/Skull.png";

    private ArrayList<GridPane> gridPanes;

    private  ArrayList<GridPane> damagesGridPanes;

    private  ArrayList<GridPane> marksGridPanes;

    private  ArrayList<GridPane> skullGridPanes;

    private ArrayList<GridPane> ammoGridPanes;

    private GameInfo gameInfo;

    //infos of players WITHOUT my player
    private ArrayList<OtherPlayerInfo> otherPlayersInfos;

    //player colors WITHOUT my player's color
    private ArrayList<PlayerColor> playerColors;

    //ALL player colors WITH my player's color
    private ArrayList<PlayerColor> allPlayersColors;

    //players names WITHOUT my player's name
    private ArrayList<String> playersNames;

    //my player's name
    private String username;

    private ArrayList<String> allPlayersNames;

    protected OtherPlayersPlancias( ArrayList<PlayerColor> playerColors, GameInfo gameInfo, String username, ArrayList<String> playersNames, ArrayList<PlayerColor> allPlayersColors, ArrayList<String> allPlayersNames){

        this.gridPanes = new ArrayList<>();
        this.damagesGridPanes = new ArrayList<>();
        this.marksGridPanes = new ArrayList<>();
        this.skullGridPanes = new ArrayList<>();
        this.ammoGridPanes = new ArrayList<>();
        this.otherPlayersInfos = new ArrayList<>();
        this.gameInfo = gameInfo;
        this.playerColors = playerColors;
        this.playersNames = playersNames;
        this.username = username;
        this.allPlayersColors = allPlayersColors;
        this.allPlayersNames = allPlayersNames;

        for ( OtherPlayerInfo otherPlayerInfo : gameInfo.otherPlayerInfos){
            if( !otherPlayerInfo.nickname.equals(username))
                this.otherPlayersInfos.add(otherPlayerInfo);
        }

        for( int i = 0; i < this.otherPlayersInfos.size(); i++) {

            GridPane gridpane = new GridPane();
            this.gridPanes.add(gridpane);

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

    private void setUpPlancia(int indexOfPlayer) {

        setUpAmmos(indexOfPlayer);
        setUpMarks(indexOfPlayer);
        setUpDamages(indexOfPlayer);
        setUpSkulls(indexOfPlayer);

    }

    private void setUpSkulls(int indexOfPlayer) {
    }

    private void setUpDamages(int indexOfPlayer) {

        GridPane gridPane = new GridPane();
        damagesGridPanes.add(gridPane);
        damagesGridPanes.get(indexOfPlayer).setPadding(new Insets(6, 0, 0, 0));

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

        setGrid(damagesGridPanes.get(indexOfPlayer), colPercentages, rowPercentages);
        damagesGridPanes.get(indexOfPlayer).setGridLinesVisible(true);
        gridPanes.get(indexOfPlayer).add(damagesGridPanes.get(indexOfPlayer), 1, 2, 12, 1);

        for (int i = 0; i < otherPlayersInfos.get(indexOfPlayer).damages.size(); i++){


            String playerName = otherPlayersInfos.get(indexOfPlayer).damages.get(i);
            PlayerColor colorOfThisPlayer = allPlayersColors.get(allPlayersNames.indexOf(playerName));

            Image image = new Image(
                    "/graphics/drops/" + colorOfThisPlayer.toString().toUpperCase() + ".png",
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
            damagesGridPanes.get(indexOfPlayer).add(pane, i, 0);

        }
    }

    private void setUpMarks(int indexOfPlayer) {
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

        for ( int i = 0; i < otherPlayersInfos.get(indexOfPlayer).getnYellowAmmo(); i++){
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
        for ( int i = 0; i < otherPlayersInfos.get(indexOfPlayer).getnRedAmmo(); i++){
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
        for ( int i = 0; i < otherPlayersInfos.get(indexOfPlayer).getnBlueAmmo(); i++){
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
