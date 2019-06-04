package it.polimi.ingsw.client.gui.scenes;

import it.polimi.ingsw.client.gui.Gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StartScene implements MyScene {

    private Scene scene;
    private Stage window;

    private final String labelValue = "Welcome to Adrenalina";



    public StartScene(Gui gui, Stage window){

        this.window = window;

        //Setting up startscene
        VBox startLayout = new VBox(20);
        startLayout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label(labelValue);
        welcomeLabel.setStyle("-fx-font: 25px 'Stencil', 'Impact'; -fx-text-fill: #3C0A0A");

        Button startGameButton = new Button("Start game");
        startGameButton.setStyle("-fx-background-color: #3C0A0A; -fx-text-fill: #E94B2B;");

        this.scene = new Scene(startLayout, 750, 500);

        //startGameScene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());


        //background image
        Image backgroundImage = Gui.loadImage("src/main/resources/Grafica/Images/Adrenalina_front_image.jpg");

        //Setting up the image as background
        Background Background = new Background(
                new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(scene.getHeight(), scene.getWidth(),
                                true, true, true, true)));

        startLayout.getChildren().addAll(welcomeLabel, startGameButton);
        startLayout.setBackground(Background);

        startGameButton.setOnAction(e -> {

            MyScene next = new LoginScene(gui, this.window);
            Scene nextScene = next.getScene();
            this.window.setScene(nextScene);

        });
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
