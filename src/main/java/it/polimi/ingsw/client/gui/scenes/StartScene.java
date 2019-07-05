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
        welcomeLabel.setStyle("-fx-font-size: 25px");

        Button startGameButton = new Button("Start game");

        this.scene = new Scene(startLayout, 750, 500);
        scene.getStylesheets().add(getClass().getResource(Gui.loginCssPath).toExternalForm());



        //background image
        Image backgroundImage = Gui.loadImage(Gui.loginBackgroundImagePath);

        //Setting up the image as background
        Background Background = new Background(
                new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(scene.getWidth(), scene.getHeight(),
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
