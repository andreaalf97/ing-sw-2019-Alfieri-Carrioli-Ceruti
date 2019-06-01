package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.map.MapName;
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
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Gui extends Application implements QuestionEventHandler {

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

        /*ImageView purple_plancia_imageView = new ImageView(purple_plancia);
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
        blue_plancia_imageView.setFitHeight(75);*/

        //Gridpane in cui ci stanno tutte le plance dei giocatori (che a loro volta sono dei gridpane) ed eventuali testi o boottoni o menu
        GridPane Plance_altri_giocatori = new GridPane();
        Plance_altri_giocatori.setMinSize(300, 400);
        Plance_altri_giocatori.setStyle("-fx-border-color: red");
        Plance_altri_giocatori.setHgap(20);
        Plance_altri_giocatori.setVgap(20);
        Plance_altri_giocatori.widthProperty().addListener((observable, oldValue, newValue) -> {

            Plance_altri_giocatori.minWidth(purple_plancia.getWidth());
            Plance_altri_giocatori.maxWidth(purple_plancia.getWidth());
        });
        Plance_altri_giocatori.heightProperty().addListener((observable, oldValue, newValue) -> {

            Plance_altri_giocatori.minHeight(purple_plancia.getHeight()*4);
            Plance_altri_giocatori.maxHeight(window.getHeight()-planciaGiocatoreImage.getHeight());
        });



        //Un gridpane per ogni plancia, ognuno avrà in background la propria immagine della plancia
        GridPane Purple_plancia_Gridpane = new GridPane();
        Purple_plancia_Gridpane.widthProperty().addListener((observable, oldValue, newValue) -> {
            Purple_plancia_Gridpane.minWidth(purple_plancia.getWidth());
            Purple_plancia_Gridpane.maxWidth(purple_plancia.getWidth());
        });
        Purple_plancia_Gridpane.heightProperty().addListener((observable, oldValue, newValue) -> {
            Purple_plancia_Gridpane.minHeight(purple_plancia.getHeight());
            Purple_plancia_Gridpane.maxHeight(purple_plancia.getHeight());
        });
        GridPane Green_plancia_Gridpane = new GridPane();
        Green_plancia_Gridpane.widthProperty().addListener((observable, oldValue, newValue) -> {
            Green_plancia_Gridpane.minWidth(purple_plancia.getWidth());
            Green_plancia_Gridpane.maxWidth(purple_plancia.getWidth());
        });
        Green_plancia_Gridpane.heightProperty().addListener((observable, oldValue, newValue) -> {
            Green_plancia_Gridpane.minHeight(purple_plancia.getHeight());
            Green_plancia_Gridpane.maxHeight(purple_plancia.getHeight());
        });
        GridPane Grey_plancia_Gridpane = new GridPane();
        Grey_plancia_Gridpane.widthProperty().addListener((observable, oldValue, newValue) -> {
            Grey_plancia_Gridpane.minWidth(purple_plancia.getWidth());
            Grey_plancia_Gridpane.maxWidth(purple_plancia.getWidth());
        });
        Grey_plancia_Gridpane.heightProperty().addListener((observable, oldValue, newValue) -> {
            Grey_plancia_Gridpane.minHeight(purple_plancia.getHeight());
            Grey_plancia_Gridpane.maxHeight(purple_plancia.getHeight());
        });
        GridPane Blue_plancia_Gridpane = new GridPane();
        Blue_plancia_Gridpane.widthProperty().addListener((observable, oldValue, newValue) -> {
            Blue_plancia_Gridpane.minWidth(purple_plancia.getWidth());
            Blue_plancia_Gridpane.maxWidth(purple_plancia.getWidth());
        });
        Blue_plancia_Gridpane.heightProperty().addListener((observable, oldValue, newValue) -> {
            Blue_plancia_Gridpane.minHeight(purple_plancia.getHeight());
            Blue_plancia_Gridpane.maxHeight(purple_plancia.getHeight());
        });

        BackgroundImage purple_plancia_image = new BackgroundImage(purple_plancia, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(Purple_plancia_Gridpane.getWidth(), Purple_plancia_Gridpane.getHeight(), true, true, true, false));
        Background Background_Purple_PlanciaImage = new Background(purple_plancia_image);
        Purple_plancia_Gridpane.setBackground(Background_Purple_PlanciaImage);

        BackgroundImage green_plancia_image = new BackgroundImage(green_plancia, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(Green_plancia_Gridpane.getWidth(), Green_plancia_Gridpane.getHeight(), true, true, true, false));
        Background Background_Green_PlanciaImage = new Background(green_plancia_image);
        Green_plancia_Gridpane.setBackground(Background_Green_PlanciaImage);

        BackgroundImage grey_plancia_image = new BackgroundImage(grey_plancia, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(Grey_plancia_Gridpane.getWidth(), Grey_plancia_Gridpane.getHeight(), true, true, true, false));
        Background Background_Grey_PlanciaImage = new Background(grey_plancia_image);
        Grey_plancia_Gridpane.setBackground(Background_Grey_PlanciaImage);

        BackgroundImage blue_plancia_image = new BackgroundImage(blue_plancia, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(Blue_plancia_Gridpane.getWidth(), Blue_plancia_Gridpane.getHeight(), true, true, true, false));
        Background Background_Blue_PlanciaImage = new Background(blue_plancia_image);
        Blue_plancia_Gridpane.setBackground(Background_Blue_PlanciaImage);

        Plance_altri_giocatori.add(Purple_plancia_Gridpane, 1, 0);
        Plance_altri_giocatori.add(Green_plancia_Gridpane, 1, 1);
        Plance_altri_giocatori.add(Grey_plancia_Gridpane, 1, 2);
        Plance_altri_giocatori.add(Blue_plancia_Gridpane, 1, 3);


        //Gridpane della mappa
        GridPane mapGridPane = new GridPane();
        mapGridPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            mapGridPane.minWidth(mapImage.getWidth());
            mapGridPane.maxWidth(window.getWidth()-Plance_altri_giocatori.getWidth());
        });
        mapGridPane.heightProperty().addListener((observable, oldValue, newValue) -> {

            mapGridPane.setMinHeight(newValue.doubleValue());
            mapGridPane.setMaxHeight(newValue.doubleValue());
        });
        mapGridPane.setStyle("-fx-border-color: black");
       /* mapGridPane.maxWidthProperty().bind(Bindings.divide(window.widthProperty(), 1.45));
        mapGridPane.maxHeightProperty().bind(Bindings.divide(window.heightProperty(), 1.00));
        mapGridPane.minWidthProperty().bind(Bindings.divide(window.widthProperty(), 1.45));
        mapGridPane.minHeightProperty().bind(Bindings.divide(window.heightProperty(), 1.00));
        mapGridPane.setGridLinesVisible(true);*/


        //In questa GridPane, posta sul TOP LEFT, ci sarà la plancia giocatore
        GridPane planciaPlayerGridPane = new GridPane();

        /*planciaPlayerGridPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            //planciaPlayerGridPane.minWidth(Gui.planciaGiocatoreImage.getWidth());
            //planciaPlayerGridPane.maxWidth(Gui.planciaGiocatoreImage.getWidth());
            planciaPlayerGridPane.prefWidth(Gui.planciaGiocatoreImage.getHeight());
        });
        mapGridPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            //planciaPlayerGridPane.minHeight(Gui.planciaGiocatoreImage.getHeight());
            //planciaPlayerGridPane.maxHeight(Gui.planciaGiocatoreImage.getHeight());
            planciaPlayerGridPane.prefHeight(Gui.planciaGiocatoreImage.getHeight());
        });*/
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

        BackgroundImage planciaImage = new BackgroundImage(Gui.planciaGiocatoreImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(planciaPlayerGridPane.getWidth(), planciaPlayerGridPane.getHeight()*0.9, true, true, true, false));
        Background BackgroundPlanciaImage = new Background(planciaImage);
        planciaPlayerGridPane.setBackground(BackgroundPlanciaImage);

        BorderPane gameBorderPane = new BorderPane();
        gameBorderPane.setStyle("-fx-border-color: red");
        gameBorderPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #300900, #7F1600)");
        gameBorderPane.setPrefSize(1000, 800);
        BorderPane.setAlignment(mapGridPane, Pos.BOTTOM_LEFT);
        BorderPane.setAlignment(planciaPlayerGridPane, Pos.TOP_LEFT);
        BorderPane.setAlignment(Plance_altri_giocatori, Pos.CENTER);
        gameBorderPane.setCenter(mapGridPane);
        gameBorderPane.setTop(planciaPlayerGridPane);
        gameBorderPane.setRight(Plance_altri_giocatori);


        Scene gameScene = new Scene(gameBorderPane);

        return gameScene;
    }

    @Override
    public void receiveEvent(QuestionEvent questionEvent) {

    }

    @Override
    public void handleEvent(TemporaryIdQuestion event) {

    }

    @Override
    public void handleEvent(InvalidUsernameQuestion event) {

    }

    @Override
    public void handleEvent(AddedToWaitingRoomQuestion event) {

    }

    @Override
    public void handleEvent(NewPlayerConnectedQuestion event) {

    }

    @Override
    public void handleEvent(DisconnectedQuestion event) {

    }

    @Override
    public void handleEvent(GameStartedQuestion event) {

    }

    @Override
    public void handleEvent(PlayerDisconnectedQuestion event) {

    }

    @Override
    public void handleEvent(ActionQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayForAttackingQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayToPickWeaponQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToPayToReloadQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToShootQuestion event) {

    }

    @Override
    public void handleEvent(ChooseHowToUseTurnPowerUpQuestion event) {

    }

    @Override
    public void handleEvent(ChooseIfToUseAsyncPowerUpQuestion event) {

    }

    @Override
    public void handleEvent(ChoosePowerUpToRespawnQuestion event) {

    }

    @Override
    public void handleEvent(ChoosePowerUpToUseQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToAttackQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToPickQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToReloadQuestion event) {

    }

    @Override
    public void handleEvent(ChooseWeaponToSwitchQuestion event) {

    }

    @Override
    public void handleEvent(ModelUpdate event) {

    }

    @Override
    public void handleEvent(TextMessage event) {

    }

    @Override
    public void handleEvent(WhereToMoveAndGrabQuestion event) {

    }

    @Override
    public void handleEvent(WhereToMoveQuestion event) {

    }
}

