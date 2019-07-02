package it.polimi.ingsw.client.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class WaitingRoomGui {

    private ArrayList<String> connectedPlayers;

    private Stage window;

    private VBox vBox;

    public WaitingRoomGui(ArrayList<String> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
        this.window = null;
        this.vBox = null;
    }

    void display(){

        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        //window.initStyle(StageStyle.UNDECORATED);

        window.setMinWidth(250);
        window.setMinHeight(200);

        Label label = new Label("Waiting Room");
        label.setStyle("-fx-font: 25px 'Stencil', 'Impact', monospace; -fx-text-fill: #E94B2B");


        vBox = new VBox(15);
        vBox.setStyle("-fx-background-color: #200500");
        vBox.getChildren().add(label);

        for(String player : connectedPlayers){

            Label newLabel = new Label(player);
            label.setStyle("-fx-font: 18px 'Stencil', 'Impact', monospace; -fx-text-fill: #E94B2B");

            vBox.getChildren().add(newLabel);
        }

        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
    }

    void close(){
        window.close();
    }

    void addPlayer(String newPlayer){

        Label newLabel = new Label(newPlayer);

        vBox.getChildren().add(newLabel);
    }

}
