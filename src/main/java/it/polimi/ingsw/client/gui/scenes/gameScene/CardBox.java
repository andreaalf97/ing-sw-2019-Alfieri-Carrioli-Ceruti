package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.PlayerInfo;
import it.polimi.ingsw.client.gui.ImageDisplay;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.control.Button;

import java.awt.*;
import java.util.ArrayList;

public class CardBox {

    private GridPane gridPane;

    private ArrayList<Button> weaponsButtons;

    private ArrayList<Button> powerUpButtons;

    public CardBox() {

        this.weaponsButtons = new ArrayList<>();
        this.powerUpButtons = new ArrayList<>();
        this.gridPane = setUpGridpane();
    }


    protected void update(PlayerInfo playerInfo){

        gridPane.getChildren().clear();
        weaponsButtons = new ArrayList<>();
        powerUpButtons = new ArrayList<>();

        int i = 0;
        for ( String weaponName : playerInfo.weaponNames){

            Button button = new Button();
            weaponsButtons.add(button);
            weaponsButtons.get(i).setStyle("-fx-background-color: transparent");

            Image image = new Image(
                    "/graphics/cards/" +weaponName+ ".png",
                    0, 0,
                    true, false
            );
            weaponsButtons.get(i).setOnAction(e -> ImageDisplay.display(image));

            BackgroundImage backgroundImage = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(1, 1, true, true, true, false)
            );
            Pane cardPane = new Pane();
            cardPane.setBackground(new Background(backgroundImage));
            gridPane.add(cardPane, i+i, 1);
            gridPane.add(weaponsButtons.get(i), i+i, 1);
            weaponsButtons.get(i).setMinWidth(40);
            weaponsButtons.get(i).setMinHeight(70);
            i++;
        }

        int j = 0;
        for ( String powerUpName : playerInfo.powerUpNames ){

            Button button = new Button();
            powerUpButtons.add(button);
            powerUpButtons.get(j).setStyle("-fx-background-color: transparent");

            Image image = new Image(
                    "/graphics/cards/" +powerUpName.toUpperCase()+"_"+ playerInfo.powerUpColors.get(j).toString()+".png",
                    0, 0,
                    true, false
            );
            powerUpButtons.get(j).setOnAction(e -> ImageDisplay.display(image));


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
            gridPane.add(powerUpButtons.get(j), j+j, 3);
            powerUpButtons.get(j).setMinWidth(40);
            powerUpButtons.get(j).setMinHeight(60);
            j++;
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
        gridPane.setGridLinesVisible(false);

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
