package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.ActionQuestion;
import it.polimi.ingsw.view.client.RemoteView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;

import java.awt.*;
import java.util.ArrayList;

public class PlayersInteractingSpace {

    public GridPane getGridPane() {
        return gridPane;
    }

    private GridPane gridPane;

    private RemoteView remoteView;

    public PlayersInteractingSpace(){

        this.gridPane = setGridPane();

        setWelcomeLabel();
    }

    private void setWelcomeLabel() {
        Label label = new Label("Game just started");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 1, 1, 5, 1);
    }

    public void choosePossibleAction(ActionQuestion event, String username, RemoteView remoteView){
        //Clean the gridpane
        gridPane.getChildren().clear();


        Label label = new Label("Choose action: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");

        gridPane.add(label, 0, 0, 5, 1);

        for ( int i = 0; i < event.possibleAction.size(); i++){
            Button button = new Button(event.possibleAction.get(i));
            String action = event.possibleAction.get(i);
            gridPane.add(button, i, 0);
            button.setOnAction(e -> {
                switch (action){

                    case "Attack":
                        remoteView.sendAnswerEvent(new ActionAttackAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "EndTurn":
                        remoteView.sendAnswerEvent(new ActionEndTurnAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "MoveAndGrab":
                        remoteView.sendAnswerEvent(new ActionMoveAndGrabAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "Move":
                        remoteView.sendAnswerEvent(new ActionMoveAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "PickWeapon":
                        remoteView.sendAnswerEvent(new ActionPickWeaponAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "Reload":
                        remoteView.sendAnswerEvent(new ActionReloadAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "Respawn":
                        remoteView.sendAnswerEvent(new ActionRespawnAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "UseTurnPowerUp":
                        remoteView.sendAnswerEvent(new ActionUseTurnPowerUpAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                }
            });
        }

    }

    private GridPane setGridPane() {

        GridPane gridPane = new GridPane();

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);
        colPercentages.add(10.000);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(25.000);
        rowPercentages.add(25.000);
        rowPercentages.add(25.000);
        rowPercentages.add(25.000);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);
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
}
