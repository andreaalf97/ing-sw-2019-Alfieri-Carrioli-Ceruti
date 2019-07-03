package it.polimi.ingsw.client.gui.scenes;

import it.polimi.ingsw.client.gui.ClosingBox;
import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.client.gui.Modal;
import it.polimi.ingsw.model.map.MapName;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class LoginScene implements MyScene {

    private Scene scene;
    private Stage window;

    private String username;
    private MapName votedMap;
    private int votedSkulls;

    final private String validUsername = "^[a-zA-Z0-9]*$";

    public LoginScene(Gui gui, Stage window) {

        this.window = window;

        //close button
        Button closeButton = new Button("Exit");
        closeButton.setOnAction(e -> ClosingBox.display(window));

        Image backgroundImage = Gui.loadImage(getClass().getResource(Gui.loginBackgroundImagePath).getPath());

        Background Background = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(window.getHeight(), window.getWidth(), true, true, true, true)));

        //Login scene
        GridPane loginLayout = new GridPane();
        loginLayout.setPadding( new Insets(20, 20, 20, 20));
        loginLayout.setVgap(15);
        loginLayout.setHgap(12);
        loginLayout.setBackground(Background);

        //username input
        Label usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("labelWithBackground");
        usernameLabel.setStyle("-fx-font-size: 18px");



        //set the username label in the top left
        GridPane.setConstraints( usernameLabel, 1, 8);
        //insert the username
        TextField usernameInput = new TextField();

        GridPane.setConstraints(usernameInput, 2, 8);

        //choosing map input
        Label choosingMapLabel = new Label("Choose the map you want to play in:");
        choosingMapLabel.getStyleClass().add("labelWithBackground");
        GridPane.setConstraints(choosingMapLabel, 1, 9);

        //insert the map input
        ChoiceBox<String> mapChoiceBox = new ChoiceBox<>();
        mapChoiceBox.getItems().addAll("FIRE", "WATER", "WIND", "EARTH");
        mapChoiceBox.setValue("FIRE");
        GridPane.setConstraints(mapChoiceBox, 2, 9);

        //choosing number of skull input
        Label numberOfSkullsLabel = new Label("Choose the number of skulls you want to play with:");
        numberOfSkullsLabel.getStyleClass().add("labelWithBackground");
        GridPane.setConstraints(numberOfSkullsLabel, 1, 10);

        //insert the input
        ChoiceBox<String> skullsChoiceBox = new ChoiceBox<>();
        skullsChoiceBox.getItems().addAll("5", "6", "7", "8");
        skullsChoiceBox.setValue("5");
        GridPane.setConstraints(skullsChoiceBox, 2, 10);

        Button loginButton = new Button("Login");
        loginButton.setTextFill(Color.BLACK);
        GridPane.setConstraints(loginButton, 2, 15);
        GridPane.setConstraints(closeButton, 1, 15);


        loginLayout.getChildren().addAll(usernameLabel, usernameInput, choosingMapLabel, mapChoiceBox, numberOfSkullsLabel, skullsChoiceBox, loginButton, closeButton);

        scene = new Scene(loginLayout, 750, 500);
        scene.getStylesheets().add(getClass().getResource(Gui.loginCssPath).toExternalForm());

        loginButton.setOnAction(actionEvent -> {
            handleLoginButton(gui, usernameInput.getText(), mapChoiceBox.getValue(), skullsChoiceBox.getValue());

        });
    }

    /**
     * This method read the inserted values and goes to the next scene or prompts an error message
     * @param username the chosen username
     * @param votedMap the chosen map
     * @param votedSkulls the chosen number of skulls
     */
    private void handleLoginButton(Gui gui, String username, String votedMap, String votedSkulls){

        //username non valido, mando messaggio e ritorno alla login
        if (username.isEmpty() || !Pattern.matches(validUsername, username)) {
            Modal.display("The username can only contain letters and numbers");
            return;
        }

        this.username = username;
        this.votedMap = MapName.valueOf(votedMap);
        this.votedSkulls = Integer.parseInt(votedSkulls);

        MyScene next = new IpScene(gui, this.window, this.username, this.votedMap, this.votedSkulls);
        Scene nextScene = next.getScene();

        window.setScene(nextScene);

    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
