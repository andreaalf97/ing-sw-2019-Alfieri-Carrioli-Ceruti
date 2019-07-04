package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.scenes.gameScene.PlayersInteractingSpace;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Modal {

    public static void display( String message ){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        //window.initStyle(StageStyle.UNDECORATED);

        window.setMinWidth(300);
        window.setMinHeight(200);

        Label label = new Label(message);
        label.setStyle("-fx-text-fill: #E94B2B");

        VBox vBox = new VBox(10);

        Button button = new Button("OK");
        button.setStyle("-fx-background-color: #E94B2B; -fx-text-fill: linear-gradient(from 25% 25% to 100% 100%, #300900, #7F1600);");
        button.setMaxSize(40, 30);

        button.setOnAction(e -> window.close());

        vBox.setStyle("-fx-background-color: #200500");

        vBox.getChildren().addAll(label, button);

        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

    }

    //this is called to ask coordinates for the movers, during a attack
    public static void display(String message, PlayersInteractingSpace playersInteractingSpace){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(200);
        Label label = new Label(message);
        label.setStyle("-fx-font: 20px 'Stencil', 'Impact', monospace; -fx-text-fill: #E94B2B");

        VBox vBox = new VBox(10);
        vBox.setSpacing(10);

        Label coordsLabel = new Label("insert the coordinates in this format: x,y");
        TextField coordsTextField = new TextField();
        Button okButton = new Button("OK");


        vBox.getChildren().add(coordsLabel);
        vBox.getChildren().add(coordsTextField);
        vBox.getChildren().add(okButton);

        okButton.setOnAction(actionEvent -> {
            String[] coords = coordsTextField.getText().split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            playersInteractingSpace.xCoords.add(x);
            playersInteractingSpace.yCoords.add(y);
            window.close();
        });

        okButton.setStyle("-fx-background-color: #E94B2B; -fx-text-fill: linear-gradient(from 25% 25% to 100% 100%, #300900, #7F1600);");
        okButton.setMaxSize(40, 30);

        vBox.setStyle("-fx-background-color: #200500");

        vBox.setAlignment(Pos.TOP_LEFT);
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
    }

}
