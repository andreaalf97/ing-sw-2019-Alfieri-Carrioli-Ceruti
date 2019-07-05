package it.polimi.ingsw.client.gui.scenes;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.view.client.RemoteViewInterface;
import it.polimi.ingsw.view.client.RemoteViewRmiImpl;
import it.polimi.ingsw.view.client.RemoteViewSocket;
import javafx.geometry.Insets;
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

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionScene implements MyScene {

    private Scene scene;
    private Stage window;

    private final int socketPort = 2345;
    private final int rmiPort = 5432;

    private String username;
    private MapName votedMap;
    private int votedSkulls;
    private String chosenIp;

    public ConnectionScene(Gui gui, Stage window, String username, MapName votedMap, int votedSkulls, String chosenIp) {

        this.window = window;
        this.username = username;
        this.votedMap = votedMap;
        this.votedSkulls = votedSkulls;
        this.chosenIp = chosenIp;

        //background image
        Image backgroundImage = Gui.loadImage(Gui.loginBackgroundImagePath);

        Background Background = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(window.getHeight(), window.getWidth(), true, true, true, true)));

        //connection scene: sockets or rmi?
        VBox connectionLayout = new VBox();
        connectionLayout.setAlignment(Pos.CENTER);

        connectionLayout.setBackground(Background);
        connectionLayout.setSpacing(20);
        connectionLayout.setPadding( new Insets(30));

        Label connectionLabel = new Label("Choose the type of connection:");
        connectionLabel.setStyle("-fx-font-size: 15;");
        connectionLabel.getStyleClass().add("labelWithBackground");
        connectionLabel.setAlignment(Pos.TOP_CENTER);

        HBox HboxConnections = new HBox();
        HboxConnections.setAlignment(Pos.CENTER);
        HboxConnections.setSpacing(30);
        Button socketButton = new Button("Socket");
        Button RMIButton = new Button("RMI");


        HboxConnections.getChildren().addAll(socketButton, RMIButton);
        connectionLayout.getChildren().addAll(connectionLabel, HboxConnections);

        this.scene = new Scene(connectionLayout, 750, 500);
        scene.getStylesheets().add(getClass().getResource(Gui.loginCssPath).toExternalForm());


        /*
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->  {
                    HboxConnections.getChildren().remove(RMIButton);
                });
            }
        }, 5000);
        */

        socketButton.setOnAction( event -> {

            new Thread( () -> startSocketConnection(gui, chosenIp, socketPort) ).start();

        });
        RMIButton.setOnAction( event -> {

            new Thread(() -> startRmiConnection(gui, chosenIp, rmiPort)).start();

        });
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Starts a new rmi connection with the server by sending a connection event
     * @param ipAddress the address of the server
     * @param port the port running an rmi registry
     */
    private void startRmiConnection(Gui gui, String ipAddress, int port) {

        try {

            //Creates the remote view object to send to the server for callbacks
            RemoteViewInterface remoteView = new RemoteViewRmiImpl(gui);

            gui.setGuiParams( (RemoteViewRmiImpl)remoteView, username, votedMap, votedSkulls );

            //Searches for the registry
            Registry registry = LocateRegistry.getRegistry(ipAddress, port);

            //Looks for the 'server' object (only has one method called connect())
            ServerInterface rmiRemoteServer = (ServerInterface)registry.lookup("server");

            //Sends the remote view to the server and then waits for a TemporaryIdQuestion
            rmiRemoteServer.connect(remoteView);

        }
        catch (Exception e){
            System.err.println("Error while connecting to server");
        }

    }

    /**
     * Starts a new socket connection with the server by sending a connection event
     * @param ipAddress the address of the server
     * @param port the port running an rmi registry
     */
    private void startSocketConnection(Gui gui, String ipAddress, int port) {

        Socket serverSocket = null;
        try {
            //Connects with the server through socket
            serverSocket = new Socket(ipAddress, port);
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

        //Creates a new RemoteViewSocket object which is used to keep the connection open and read all new messages
        RemoteViewSocket remoteViewSocket = new RemoteViewSocket(serverSocket, gui);

        gui.setGuiParams( remoteViewSocket, username, votedMap, votedSkulls );

        //runs the remoteViewSocket on a new thread to keep it open
        new Thread(remoteViewSocket).run();

    }
}
