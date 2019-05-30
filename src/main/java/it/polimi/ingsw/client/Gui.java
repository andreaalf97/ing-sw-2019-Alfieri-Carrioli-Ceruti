package it.polimi.ingsw.client;

import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.view.client.RemoteViewRmiImpl;
import it.polimi.ingsw.view.client.RemoteViewSocket;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Gui extends Application implements UserInterface {

    final private String validUsername = "^[a-zA-Z0-9]*$";

    static private String serverAddress = "127.0.0.1";

    static private int rmiPort = 5432;

    static private int socketPort = 2345;

    static private boolean RMI;

    static private MapName nameOfMap ;
    static private int NumberOfSkulls;
    static private String Username;
    static private Image mapImage ;
    static private String playerColor;
    static private Image planciaGiocatoreImage = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {

        Scene gameScene = setGameScene(window);

        Scene loginScene = setLoginScene(window, gameScene);

        Scene port_and_IP_scene = setPort_and_IP_scene(window, loginScene);

        Scene connectionScene = setConnectionScene(window, port_and_IP_scene);

        Scene startScene = setStartScene(window, connectionScene);

        /*//Questi sono per settare la grandezza della scena a screen size!
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        window.setX(primaryScreenBounds.getMinX());
        window.setY(primaryScreenBounds.getMinY());
        window.setWidth(primaryScreenBounds.getWidth());
        window.setHeight(primaryScreenBounds.getHeight());*/

        window.setScene(startScene);
        window.show();
    }


    private Scene setStartScene(Stage window, Scene connectionScene) throws FileNotFoundException {

        //close button
        Button closeButton = new Button("Exit");
        closeButton.setTextFill(Color.BLACK);
        closeButton.setOnAction(e -> ClosingBox.display(window));

        //First window
        window.setTitle("Adrenalina");

        //Setting up startscene
        VBox startLayout = new VBox(20);

        Label welcomeLabel = new Label("Welcome to Adrenalina");
        welcomeLabel.setFont(Font.font("Summit", FontWeight.NORMAL, 20));
        welcomeLabel.setTextFill(Color.WHITE);

        Button startGameButton = new Button("Start game");
        startGameButton.setTextFill(Color.BLACK);
        startLayout.setAlignment(Pos.CENTER);
        Scene startGameScene = new Scene(startLayout, 750, 500);

        //background image
        Image backgroundImage = new Image(new FileInputStream("src/main/resources/Grafica/Images/Adrenalina_front_image.jpg"));
        Background Background = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(startGameScene.getHeight(), startGameScene.getWidth(), true, true, true, true)));

        startLayout.getChildren().addAll(welcomeLabel, startGameButton);
        startLayout.setBackground(Background);

        startGameButton.setOnAction(e -> window.setScene(connectionScene));

        return startGameScene;
    }

    private Scene setConnectionScene(Stage window, Scene port_and_IP_scene) throws FileNotFoundException {

        //background image
        Image backgroundImage = new Image(new FileInputStream("src/main/resources/Grafica/Images/Adrenalina_front_image.jpg"));
        Background Background = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(window.getHeight(), window.getWidth(), true, true, true, true)));

        //connection scene: sockets or rmi?
        VBox connectionLayout = new VBox(20);
        connectionLayout.setBackground(Background);
        connectionLayout.setSpacing(20);
        connectionLayout.setPadding( new Insets(30, 30, 30,30));
        Label connectionLabel = new Label("Choose the type of connection:");
        connectionLabel.setFont(Font.font("Summit", FontWeight.NORMAL, 14));
        connectionLabel.setTextFill(Color.WHITE);
        connectionLabel.setAlignment(Pos.TOP_CENTER);
        HBox HboxConnections = new HBox();
        HboxConnections.setAlignment(Pos.CENTER);
        HboxConnections.setSpacing(30);
        Button socketButton = new Button("Socket");
        Button RMIButton = new Button("RMI");
        socketButton.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
        //socketButton.setTextFill(Color.BLACK);
        //RMIButton.setTextFill(Color.BLACK);
        connectionLayout.setAlignment(Pos.CENTER);
        HboxConnections.getChildren().addAll(socketButton, RMIButton);
        connectionLayout.getChildren().addAll(connectionLabel, HboxConnections);
        Scene connectionScene = new Scene(connectionLayout, 750, 500);

        socketButton.setOnAction( event -> {
            Gui.RMI = false;
            window.setScene(port_and_IP_scene);
            //startSocketConnection(usernameInput.getText(), MapName.valueOf(choosingMapInput.getText()), Integer.parseInt(numberOfSkullsInput.getText()));
        });
        RMIButton.setOnAction( event -> {
            Gui.RMI = true;
            window.setScene(port_and_IP_scene);
            //startRmiConnection(usernameInput.getText(), MapName.valueOf(choosingMapInput.getText()), Integer.parseInt(numberOfSkullsInput.getText()));
        });

        return connectionScene;
    }

    private Scene setPort_and_IP_scene(Stage window, Scene loginScene) throws FileNotFoundException {

        //close button
        Button closeButton = new Button("Exit");
        closeButton.setTextFill(Color.BLACK);
        closeButton.setOnAction(e -> ClosingBox.display(window));

        //background image
        Image backgroundImage = new Image(new FileInputStream("src/main/resources/Grafica/Images/Adrenalina_front_image.jpg"));
        Background Background = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(window.getHeight(), window.getWidth(), true, true, true, true)));

        //Port and IP scene
        GridPane port_and_IP_Layout = new GridPane();
        port_and_IP_Layout.setPadding( new Insets(20, 20, 20, 20));
        port_and_IP_Layout.setVgap(10);
        port_and_IP_Layout.setHgap(8);
        port_and_IP_Layout.setBackground(Background);

        //choosing IP address input
        Label IP_address_label = new Label("Choose IP address:");
        IP_address_label.setFont(Font.font("Summit", FontWeight.NORMAL, 14));
        IP_address_label.setTextFill(Color.WHITE);
        GridPane.setConstraints(IP_address_label, 1, 11);
        //insert the input
        TextField IP_address_input = new TextField();
        GridPane.setConstraints(IP_address_input, 2, 11);

        Button nextButton = new Button("Next");
        nextButton.setTextFill(Color.BLACK);
        GridPane.setConstraints(nextButton, 2, 13);
        GridPane.setConstraints(closeButton, 2, 17);
        port_and_IP_Layout.getChildren().addAll( IP_address_label, IP_address_input, nextButton, closeButton);

        Scene setPort_and_IP_scene = new Scene(port_and_IP_Layout, 750, 500);

        nextButton.setOnAction(actionEvent -> {

            Gui.serverAddress = IP_address_input.getText();

            if (IP_address_input.getText().isEmpty()) {
                TextBox.display("Enter a valid IP address");
                window.setScene(setPort_and_IP_scene);
            }else{
                window.setScene(loginScene);
            }


        });

        return setPort_and_IP_scene;
    }

    private Scene setLoginScene(Stage window, Scene gameScene) throws FileNotFoundException {

        //close button
        Button closeButton = new Button("Exit");
        closeButton.setTextFill(Color.BLACK);
        closeButton.setOnAction(e -> ClosingBox.display(window));

        //background image
        Image backgroundImage = new Image(new FileInputStream("src/main/resources/Grafica/Images/Adrenalina_front_image.jpg"));
        Background Background = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(window.getHeight(), window.getWidth(), true, true, true, true)));

        //Login scene
        GridPane loginLayout = new GridPane();
        loginLayout.setPadding( new Insets(20, 20, 20, 20));
        loginLayout.setVgap(15);
        loginLayout.setHgap(12);
        loginLayout.setBackground(Background);
        //username input
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Summit", FontWeight.NORMAL, 14));
        usernameLabel.setTextFill(Color.WHITE);
        //set the username label in the top left
        GridPane.setConstraints( usernameLabel, 1, 8);
        //insert the username
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 2, 8);

        //choosing map input
        Label choosingMapLabel = new Label("Choose the map you want to play in:");
        choosingMapLabel.setFont(Font.font("Summit", FontWeight.NORMAL, 14));
        choosingMapLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(choosingMapLabel, 1, 9);

        //insert the map input
        ChoiceBox<String> mapChoiceBox = new ChoiceBox<>();
        mapChoiceBox.getItems().addAll("FIRE", "WATER", "WIND", "EARTH");
        mapChoiceBox.setValue("FIRE");
        GridPane.setConstraints(mapChoiceBox, 2, 9);

        //choosing number of skull input
        Label numberOfSkullsLabel = new Label("Choose the number of skulls you want to play with:");
        numberOfSkullsLabel.setFont(Font.font("Summit", FontWeight.NORMAL, 14));
        numberOfSkullsLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(numberOfSkullsLabel, 1, 10);

        //insert the input
        ChoiceBox<String> skullsChoiceBox = new ChoiceBox<>();
        skullsChoiceBox.getItems().addAll("5", "6", "7", "8");
        skullsChoiceBox.setValue("5");
        GridPane.setConstraints(skullsChoiceBox, 2, 10);

        //Player color
        Label playerColorLabel = new Label("Choose the color of your pawn:");
        playerColorLabel.setFont(Font.font("Summit", FontWeight.NORMAL, 14));
        playerColorLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(playerColorLabel, 1, 11);

        //Player color input
        ChoiceBox<String> playerColorChoiceBox = new ChoiceBox<>();
        playerColorChoiceBox.getItems().addAll("Yellow", "Blue", "Green", "Grey", "Purple");
        playerColorChoiceBox.setValue("Yellow");
        GridPane.setConstraints(playerColorChoiceBox, 2, 11);

        Button loginButton = new Button("Login");
        loginButton.setTextFill(Color.BLACK);
        GridPane.setConstraints(loginButton, 2, 15);
        GridPane.setConstraints(closeButton, 1, 15);
        loginLayout.getChildren().addAll(usernameLabel, usernameInput, choosingMapLabel, mapChoiceBox, numberOfSkullsLabel, skullsChoiceBox, playerColorLabel, playerColorChoiceBox, loginButton, closeButton);

        Scene loginScene = new Scene(loginLayout, 750, 500);

        loginButton.setOnAction(actionEvent -> {
            ArrayList<String> info = handleLoginButton(usernameInput.getText(), mapChoiceBox.getValue(), skullsChoiceBox.getValue(), playerColorChoiceBox.getValue(), window,  loginScene, gameScene);

            if(!info.isEmpty()) {
                Gui.Username = info.get(0);
                Gui.nameOfMap = MapName.valueOf(info.get(1));
                Gui.NumberOfSkulls = Integer.parseInt(info.get(2));
                Gui.playerColor = info.get(3);
            }

        });

        return loginScene;
    }

    /*CSS examples
    *button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
     text2.setStyle("-fx-font: normal bold 20px 'serif' ");
     gridPane.setStyle("-fx-background-color: BEIGE;"); */

    private ArrayList<String> handleLoginButton(String username, String mapName, String numberOfSkulls, String playerColor,  Stage window, Scene loginScene, Scene gameScene){

        ArrayList<String> answer = new ArrayList<>();
        try{

            boolean ok = true;

            //username non valido, mando messaggio e ritorno alla login
            if (!Pattern.matches(validUsername, username)) {
                TextBox.display("The username can only contain letters and numbers");
                ok = false;
            }
            if (username.isEmpty()) {
                TextBox.display("Username can't be empty. Please enter a valid one");
                ok = false;
            }
            if( !ok )
                window.setScene(loginScene);
            else {
                window.setScene(gameScene);
                answer.add(username);
                answer.add(mapName);
                answer.add(numberOfSkulls);
                answer.add(playerColor);
                window.setScene(gameScene);
            }

        } catch (Exception e) {
            //TODO senza la try catch da un sacco di errori
        }
        return answer;
    }


    private Scene setGameScene(Stage window) throws FileNotFoundException {



        /*if(Gui.nameOfMap == MapName.FIRE ) {
            Gui.mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_1.png"));
        }
        if(Gui.nameOfMap == MapName.WATER) {
            Gui.mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_2.png"));
        }
        if(Gui.nameOfMap == MapName.WIND) {
            Gui.mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_3.png"));
        }
        if(Gui.nameOfMap == MapName.EARTH) {
            Gui.mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_4.png"));
        }
        if(mapImage == null)
            TextBox.display("map null");*/

        /*if(Gui.playerColor.equals("Yellow") ) {
            Gui.planciaGiocatoreImage = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Yellow/Yellow_front.png"));
        }
        if(Gui.playerColor.equals("Green")) {
            Gui.planciaGiocatoreImage = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Green/Green_front.png"));
        }
        if(Gui.playerColor.equals("Grey")) {
            Gui.planciaGiocatoreImage = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Grey/Grey_front.png"));
        }
        if(Gui.playerColor.equals("Purple")) {
            Gui.planciaGiocatoreImage = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Purple/Purple_front.png"));
        }
        if(Gui.playerColor.equals("Blue")) {
            Gui.planciaGiocatoreImage = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Blue/Blue_front.png"));
        }*/

        Gui.mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/Mappa_1.png"));

        Gui.planciaGiocatoreImage = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Yellow/Yellow_front.png"));

        Image purple_plancia = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Purple/Purple_front.png"));
        Image green_plancia = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Green/Green_front.png"));
        Image grey_plancia =  new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Grey/Grey_front.png"));
        Image blue_plancia = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Blue/Blue_front.png"));

        ImageView purple_plancia_imageView = new ImageView(purple_plancia);
        ImageView green_plancia_imageView = new ImageView(green_plancia);
        ImageView grey_plancia_imageView = new ImageView(grey_plancia);
        ImageView blue_plancia_imageView = new ImageView(blue_plancia);
        purple_plancia_imageView.setFitWidth(300);
        purple_plancia_imageView.setFitHeight(75);
        green_plancia_imageView.setFitWidth(300);
        green_plancia_imageView.setFitHeight(75);
        grey_plancia_imageView.setFitWidth(300);
        grey_plancia_imageView.setFitHeight(75);
        blue_plancia_imageView.setFitWidth(300);
        blue_plancia_imageView.setFitHeight(75);

        VBox Plance_altri_giocatori = new VBox();
        Plance_altri_giocatori.setStyle("-fx-border-color: white");
        Plance_altri_giocatori.setSpacing(35);
        Plance_altri_giocatori.getChildren().addAll(purple_plancia_imageView, green_plancia_imageView, grey_plancia_imageView, blue_plancia_imageView);


        GridPane mapGridPane = new GridPane();
        mapGridPane.setStyle("-fx-border-color: black");
        mapGridPane.maxWidthProperty().bind(Bindings.divide(window.widthProperty(), 1.45));
        mapGridPane.maxHeightProperty().bind(Bindings.divide(window.heightProperty(), 1.00));
        mapGridPane.minWidthProperty().bind(Bindings.divide(window.widthProperty(), 1.45));
        mapGridPane.minHeightProperty().bind(Bindings.divide(window.heightProperty(), 1.00));
        mapGridPane.setGridLinesVisible(true);

        //In questa GridPane, posta sul Bottom, ci sarà la plancia giocatore
        GridPane planciaPlayerGridPane = new GridPane();
        planciaPlayerGridPane.setStyle("-fx-border-color: blue");
        planciaPlayerGridPane.setPadding(new Insets(50, 0, 0, 50));
        planciaPlayerGridPane.maxWidthProperty().bind(Bindings.divide(window.widthProperty(), 3.00));
        //planciaPlayerGridPane.maxHeightProperty().bind(Bindings.divide(window.heightProperty(), 3.00));
        planciaPlayerGridPane.minWidthProperty().bind(Bindings.divide(window.widthProperty(), 3.00));
        planciaPlayerGridPane.minHeightProperty().bind(Bindings.divide(window.heightProperty(), 6.00));
        planciaPlayerGridPane.setGridLinesVisible(true);

        BackgroundImage image = new BackgroundImage(Gui.mapImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(mapGridPane.getWidth(), mapGridPane.getHeight(), true, true, true, false));
        Background BackgroundMapImage = new Background(image);
        mapGridPane.setBackground(BackgroundMapImage);

        BackgroundImage planciaImage = new BackgroundImage(Gui.planciaGiocatoreImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(planciaPlayerGridPane.getWidth(), planciaPlayerGridPane.getHeight(), true, true, true, false));
        Background BackgroundPlanciaImage = new Background(planciaImage);
        planciaPlayerGridPane.setBackground(BackgroundPlanciaImage);

        BorderPane gameBorderPane = new BorderPane();
        gameBorderPane.setStyle("-fx-border-color: green");
        gameBorderPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #300900, #7F1600)");
        gameBorderPane.setPrefSize(1000, 800);
        BorderPane.setAlignment(mapGridPane, Pos.CENTER);
        BorderPane.setAlignment(planciaPlayerGridPane, Pos.TOP_LEFT);
        BorderPane.setAlignment(Plance_altri_giocatori, Pos.TOP_RIGHT);
        gameBorderPane.setCenter(mapGridPane);
        gameBorderPane.setTop(planciaPlayerGridPane);
        gameBorderPane.setRight(Plance_altri_giocatori);



        Scene gameScene = new Scene(gameBorderPane);

        return gameScene;
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
