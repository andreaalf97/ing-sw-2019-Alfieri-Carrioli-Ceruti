package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.InvalidChoiceException;
import it.polimi.ingsw.model.map.MapName;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Window;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Gui extends Application implements UserInterface {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {

        final String validUsername = "^[a-zA-Z0-9]*$";

        final String serverAddress = "127.0.0.1";

        final int rmiPort = 5432;

        final int socketPort = 2345;


        //First window
        window.setTitle("Adrenalina");

        //Setting up startscene
        GridPane startLayout = new GridPane();
        startLayout.setPadding( new Insets(20, 20, 20, 20));
        startLayout.setHgap(40);
        startLayout.setVgap(25);

        Scene startGameScene = new Scene(startLayout, 750, 500);

        Label welcomeLabel = new Label("Welcome to Adrenalina");
        GridPane.setConstraints(welcomeLabel, 0, 0);

        Button startGameButton = new Button("Start game");
        GridPane.setConstraints(startGameButton, 0, 1);

        startLayout.setAlignment(Pos.CENTER);

        //close button
        Button closeButton = new Button("Exit");
        GridPane.setConstraints(closeButton, 0, 2);

        startLayout.getChildren().addAll(welcomeLabel, startGameButton, closeButton);

        //Login scene
        GridPane loginLayout = new GridPane();
        loginLayout.setPadding( new Insets(20, 20, 20, 20));
        loginLayout.setVgap(10);
        loginLayout.setHgap(8);
        //username input
        Label usernameLabel = new Label("Username:");
        //set the username label in the top left
        GridPane.setConstraints( usernameLabel, 1, 1);
        //insert the username
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 2, 1);
        //choosing map input
        Label choosingMapLabel = new Label("Choose the map you want to play in:");
        GridPane.setConstraints(choosingMapLabel, 1, 2);
        //insert the input
        TextField choosingMapInput = new TextField();
        choosingMapInput.setPromptText("Fire, Earth, Wind, Water");
        GridPane.setConstraints(choosingMapInput, 2, 2);
        //choosing number of skull input
        Label numberOfSkullsLabel = new Label("Choose the number of skulls you want to play with:");
        GridPane.setConstraints(numberOfSkullsLabel, 1, 3);
        //insert the input
        TextField numberOfSkullsInput = new TextField();
        numberOfSkullsInput.setPromptText("between 5 and 8");
        GridPane.setConstraints(numberOfSkullsInput, 2, 3);

        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 2, 4);
        GridPane.setConstraints(closeButton, 2, 8);
        loginLayout.getChildren().addAll(usernameLabel, usernameInput, choosingMapLabel, choosingMapInput, numberOfSkullsLabel, numberOfSkullsInput, loginButton, closeButton);

        Scene loginScene = new Scene(loginLayout, 750, 500);

        startGameButton.setOnAction(e -> window.setScene(loginScene));



        //Board scene (first scene after logging in)
        BorderPane boardSceneLayout = new BorderPane();

        Scene boardScene = new Scene(boardSceneLayout, 1000, 700);

        Image mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_1.png"));

        ImageView imageView = new ImageView(mapImage);

        imageView.setFitHeight(boardScene.getHeight());
        imageView.setFitWidth(boardScene.getWidth());

        boardSceneLayout.getChildren().addAll(imageView);


        //imageView.fitWidthProperty().bind(window.widthProperty());

        //boardSceneLayout.setCenter(imageView);


        //Questi sono per settare la grandezza della scena a screen size!
        /*Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        window.setX(primaryScreenBounds.getMinX());
        window.setY(primaryScreenBounds.getMinY());
        window.setWidth(primaryScreenBounds.getWidth());
        window.setHeight(primaryScreenBounds.getHeight());*/

        //connection scene: sockets or rmi?
        VBox connectionLayout = new VBox();
        connectionLayout.setSpacing(20);
        connectionLayout.setPadding( new Insets(30, 30, 30,30));
        Label connectionLabel = new Label("Choose the type of connection:");
        connectionLabel.setAlignment(Pos.TOP_CENTER);
        HBox HboxConnections = new HBox();
        HboxConnections.setAlignment(Pos.CENTER);
        HboxConnections.setSpacing(30);
        Button socketButton = new Button("Socket");
        Button RMIButton = new Button("RMI");
        HboxConnections.getChildren().addAll(socketButton, RMIButton);
        connectionLayout.getChildren().addAll(connectionLabel, HboxConnections);
        Scene connectionScene = new Scene(connectionLayout, 750, 500);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try{

                    Boolean ok = true;
                    int nSkulls = Integer.parseInt(numberOfSkullsInput.getText());
                    String username = usernameInput.getText();
                    MapName votedMap = MapName.valueOf(choosingMapInput.getText());

                    //username non valido, mando messaggio e ritorno alla login
                    if (!Pattern.matches(validUsername, username)) {
                        TextBox.display("The username can only contain letters and numbers");
                        ok = false;
                    }
                    if (username.isEmpty()) {
                        TextBox.display("Username can't be empty. Please enter a valid one");
                        ok = false;
                    }
                    if (choosingMapInput.getText().isEmpty() || (votedMap != MapName.FIRE && votedMap != MapName.WATER && votedMap != MapName.WIND && votedMap != MapName.EARTH)) {
                        TextBox.display("Map not valid: choose between 'FIRE', 'WIND', 'WATER'. 'EARTH'");
                        ok = false;
                    }
                    if (numberOfSkullsInput.getText().isEmpty() || nSkulls < 5 || nSkulls > 8) {
                        TextBox.display("Number of skulls not valid");
                        ok = false;
                    }

                    if( !ok )
                        window.setScene(loginScene);
                    else
                        window.setScene(connectionScene);

                } catch (Exception e) {
                    //TODO senza la try catch da un sacco di errori
                }
            }
        });


        /*socketButton.setOnAction( event -> {
            Cli.startSocketConnection(username, votedMap, nSkulls);
            beginSocketConnection(username, votedMap, nSkulls);
            window.setScene(boardScene);
        });
        RMIButton.setOnAction( event -> {
            Cli.startRmiConnection(username, votedMap, nSkulls);
            window.setScene(boardScene);
        });*/


        closeButton.setOnAction(e -> ClosingBox.display(window));

        window.setScene(startGameScene);
        window.show();
    }

    /*private void beginSocketConnection( String username, MapName votedMap, int nSkulls){
        Cli.startSocketConnection(username, votedMap, nSkulls);
    }*/

    @Override
    public void notify(String json) {

    }

    @Override
    public int askQuestionAction(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionWhereToMove(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionWhereToMoveAndGrab(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionChoosePowerUpToRespawn(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionActionChoosePowerUpToAttack(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionChooseWeaponToAttack(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionChooseWeaponToSwitch(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionChooseWeaponToReload(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionPayWith(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionShoot(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionChoosePowerUpToUse(String[] possibleAnswers) {
        return 0;
    }

    @Override
    public int askQuestionUseTurnPowerUp(String[] possibleAnswers){return 0;}

    @Override
    public int askQuestionUseAsyncPowerUp(String[] possibleAnswers){return 0;}
}
