package it.polimi.ingsw.client.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Modal {

    static void display(String message, Gui callback){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        //window.initStyle(StageStyle.UNDECORATED);

        window.setMinWidth(300);
        window.setMinHeight(200);

        Label label = new Label(message);

        VBox vBox = new VBox(20);

        TextField textField = new TextField();

        Button button = new Button("OK");

        button.setOnAction(e -> {
            callback.receiveNewUsername(textField.getText());
            window.close();
        });

        vBox.getChildren().addAll(label, textField, button);

        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();


    }

    public static void display( String message ){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        //window.initStyle(StageStyle.UNDECORATED);

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
