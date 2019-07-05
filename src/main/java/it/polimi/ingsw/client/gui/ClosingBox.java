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
        yesButton.setMaxSize(50, 30);
        yesButton.getStyleClass().add("closingBoxButton");
        Button noButton = new Button("No");
        noButton.setMaxSize(50, 30);
        noButton.getStyleClass().add("closingBoxButton");

        Label label = new Label("Are you sure you want to exit Adrenalina?");
        label.setStyle("-fx-font: 20px;-fx-text-fill: #E94B2B; -fx-padding: 0px 10px 0px 10px");


        yesButton.setOnAction(e ->{
                window.close();
                stage.close();
                System.exit(0);
                });

        noButton.setOnAction(e -> window.close());

        VBox vBox = new VBox(20);
        vBox.setStyle("-fx-background-color: #200500");

        vBox.getChildren().addAll(label, yesButton, noButton);

        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);

        scene.getStylesheets().add("/style/style.css");

        window.setScene(scene);
        window.showAndWait();

    }
}

