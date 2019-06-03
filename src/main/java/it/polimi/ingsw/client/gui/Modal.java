package it.polimi.ingsw.client.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        label.setStyle("-fx-text-fill: #E94B2B;");

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

}
