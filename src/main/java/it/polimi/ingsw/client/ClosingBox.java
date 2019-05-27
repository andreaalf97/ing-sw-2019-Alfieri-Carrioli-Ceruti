package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.scene.layout.Pane;


public class ClosingBox {

    public static void display( Stage stage ){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Closing box");
        window.setMinWidth(350);
        window.setMinHeight(350);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        Label label = new Label("Are you sure you want to exit Adrenalina?");

        yesButton.setOnAction(e ->{
                window.close();
                stage.close();
                });

        noButton.setOnAction(e -> window.close());

        VBox vBox = new VBox(20);

        vBox.getChildren().addAll(label, yesButton, noButton);

        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

    }
}
