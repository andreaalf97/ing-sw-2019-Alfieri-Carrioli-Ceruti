package it.polimi.ingsw.client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TextBox {

    public static void display( String message ){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.setMinWidth(300);
        window.setMinHeight(200);

        Label label = new Label(message);

        VBox vBox = new VBox(20);

        vBox.getChildren().add(label);

        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

    }
}
