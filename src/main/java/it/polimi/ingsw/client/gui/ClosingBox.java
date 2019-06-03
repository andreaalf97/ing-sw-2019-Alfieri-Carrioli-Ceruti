package it.polimi.ingsw.client.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class ClosingBox {

    public static void display( Stage stage ){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Closing box");
        window.setMinWidth(300);
        window.setMinHeight(200);

        Button yesButton = new Button("Yes");
        yesButton.setMaxSize(40, 30);
        yesButton.setStyle("-fx-background-color: #E94B2B; -fx-text-fill: linear-gradient(from 25% 25% to 100% 100%, #300900, #7F1600);");
        Button noButton = new Button("No");
        noButton.setMaxSize(40, 30);
        noButton.setStyle("-fx-background-color: #E94B2B; -fx-text-fill: linear-gradient(from 25% 25% to 100% 100%, #300900, #7F1600);");

        Label label = new Label("Are you sure you want to exit Adrenalina?");
        label.setStyle("-fx-text-fill: #E94B2B;");


        yesButton.setOnAction(e ->{
                window.close();
                stage.close();
                });

        noButton.setOnAction(e -> window.close());

        VBox vBox = new VBox(20);
        vBox.setStyle("-fx-background-color: #200500");

        vBox.getChildren().addAll(label, yesButton, noButton);

        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

    }
}

