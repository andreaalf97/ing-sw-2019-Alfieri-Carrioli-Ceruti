package it.polimi.ingsw.client;

import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.view.client.RemoteViewRmiImpl;
import it.polimi.ingsw.view.client.RemoteViewSocket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.regex.Pattern;

public class Gui extends Application implements UserInterface {

    final String validUsername = "^[a-zA-Z0-9]*$";

    final String serverAddress = "127.0.0.1";

    final int rmiPort = 5432;

    final int socketPort = 2345;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {

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

        //Board scene (Shows the map)
        BorderPane boardSceneLayout = new BorderPane();

        Scene boardScene = new Scene(boardSceneLayout, 1000, 700);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try{

                    boolean ok = true;

                    //username non valido, mando messaggio e ritorno alla login
                    if (!Pattern.matches(validUsername, usernameInput.getText())) {
                        TextBox.display("The username can only contain letters and numbers");
                        ok = false;
                    }
                    if (usernameInput.getText().isEmpty()) {
                        TextBox.display("Username can't be empty. Please enter a valid one");
                        ok = false;
                    }
                    if (choosingMapInput.getText().isEmpty() || (MapName.valueOf(choosingMapInput.getText()) != MapName.FIRE && MapName.valueOf(choosingMapInput.getText()) != MapName.WATER && MapName.valueOf(choosingMapInput.getText()) != MapName.WIND && MapName.valueOf(choosingMapInput.getText()) != MapName.EARTH)) {
                        TextBox.display("Map not valid: choose between 'FIRE', 'WIND', 'WATER'. 'EARTH'");
                        ok = false;
                    }else {
                        if(MapName.valueOf(choosingMapInput.getText()) == MapName.FIRE ) {
                            Image mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_1.png"));
                            ImageView imageView = new ImageView(mapImage);
                            imageView.setFitHeight(boardScene.getHeight());
                            imageView.setFitWidth(boardScene.getWidth());
                            boardSceneLayout.getChildren().addAll(imageView);

                        }
                        if(MapName.valueOf(choosingMapInput.getText()) == MapName.WATER) {
                            Image mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_2.png"));
                            ImageView imageView = new ImageView(mapImage);
                            imageView.setFitHeight(boardScene.getHeight());
                            imageView.setFitWidth(boardScene.getWidth());
                            boardSceneLayout.getChildren().addAll(imageView);

                        }
                        if(MapName.valueOf(choosingMapInput.getText()) == MapName.WIND) {
                            Image mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_3.png"));
                            ImageView imageView = new ImageView(mapImage);
                            imageView.setFitHeight(boardScene.getHeight());
                            imageView.setFitWidth(boardScene.getWidth());
                            boardSceneLayout.getChildren().addAll(imageView);

                        }
                        if(MapName.valueOf(choosingMapInput.getText()) == MapName.EARTH) {
                            Image mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_4.png"));
                            ImageView imageView = new ImageView(mapImage);
                            imageView.setFitHeight(boardScene.getHeight());
                            imageView.setFitWidth(boardScene.getWidth());
                            boardSceneLayout.getChildren().addAll(imageView);
                        }
                    }
                    if (numberOfSkullsInput.getText().isEmpty() || Integer.parseInt(numberOfSkullsInput.getText()) < 5 || Integer.parseInt(numberOfSkullsInput.getText()) > 8) {
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



        socketButton.setOnAction( event -> {

            startSocketConnection(usernameInput.getText(), MapName.valueOf(choosingMapInput.getText()), Integer.parseInt(numberOfSkullsInput.getText()));
            window.setScene(boardScene);
        });
        RMIButton.setOnAction( event -> {

            startRmiConnection(usernameInput.getText(), MapName.valueOf(choosingMapInput.getText()), Integer.parseInt(numberOfSkullsInput.getText()));
            window.setScene(boardScene);
        });


        closeButton.setOnAction(e -> ClosingBox.display(window));

        window.setScene(startGameScene);
        window.show();
    }


    /**
     * Starts an rmi connection with the server
     * @param username the username of the player
     * @param votedMap the voted map
     * @param nSkulls the voted skulls
     */
    private void startRmiConnection(String username, MapName votedMap, int nSkulls) {

        try {

            //Creates the remote view object to send to the server for callbacks
            RemoteViewRmiImpl remoteView = new RemoteViewRmiImpl(this);

            //Searches for the registry
            Registry registry = LocateRegistry.getRegistry(serverAddress, rmiPort);

            //Looks for the 'server' object (only has one method called connect())
            ServerInterface server = (ServerInterface)registry.lookup("server");

            //Sends the connection message (USERNAME:VOTEDMAP:VOTEDSKULLS)
            String connectionMessage = username + ":" + votedMap + ":" + nSkulls;

            //Call the remote method CONNECT
            server.connect(remoteView, connectionMessage);

            return;


        }
        catch (Exception e){
            TextBox.display("Error while connecting to server");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Starts a new socket connection with the server
     * @param username the username of the player
     * @param votedMap the voted map
     * @param nSkulls the voted skulls
     */
    private void startSocketConnection(String username, MapName votedMap, int nSkulls) {

        Socket serverSocket = null;
        try {
            //Connects with the server through socket
            serverSocket = new Socket(serverAddress, socketPort);
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

        //Creates a new RemoteViewSocket object which is used to keep the connection open and read all new messages
        RemoteViewSocket remoteViewSocket = new RemoteViewSocket(serverSocket, this);

        //sends the connection message to the server
        remoteViewSocket.sendMessage(username + ":" + votedMap + ":" + nSkulls);

        //runs the remoteViewSocket on a new thread to keep it open
        new Thread(remoteViewSocket).run();

    }

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
