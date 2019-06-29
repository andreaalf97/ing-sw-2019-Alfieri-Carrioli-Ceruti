package it.polimi.ingsw.client.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageDisplay {

    public static void display( Image image ){

        Stage window = new Stage();

        GridPane gridPane = new GridPane();

        gridPane.setStyle("-fx-background-color: black");

        ImageView imageView = new ImageView(image);
        gridPane.add(imageView, 0, 0);

        window.initModality(Modality.APPLICATION_MODAL);

        //window.initStyle(StageStyle.UNDECORATED);

        window.setMinWidth(260);
        window.setMinHeight(200);

        Scene scene = new Scene(gridPane);
        window.setScene(scene);
        window.showAndWait();
    }
}
