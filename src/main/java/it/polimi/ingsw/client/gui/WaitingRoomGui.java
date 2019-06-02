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

        window.initStyle(StageStyle.UNDECORATED);

        window.setMinWidth(300);
        window.setMinHeight(200);

        Label label = new Label("WAITING ROOM");

        vBox = new VBox(20);
        vBox.getChildren().add(label);

        for(String player : connectedPlayers){

            Label newLabel = new Label(player);

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
