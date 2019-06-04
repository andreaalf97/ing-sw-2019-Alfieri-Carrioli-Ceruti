package it.polimi.ingsw.client.gui.scenes;

import it.polimi.ingsw.client.gui.ClosingBox;
import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.client.gui.Modal;
import it.polimi.ingsw.model.map.MapName;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class IpScene implements MyScene {

    private Scene scene;
    private Stage window;

    private String chosenIp;
    private String username;
    private MapName votedMap;
    private int votedSkulls;

    private final String validIpAddress = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";



    public IpScene(Gui gui, Stage window, String username, MapName votedMap, int votedSkulls) {

        this.window = window;
        this.username = username;
        this.votedMap = votedMap;
        this.votedSkulls = votedSkulls;

        //close button
        Button closeButton = new Button("Exit");
        closeButton.setOnAction(e -> ClosingBox.display(window));

        //background image
        Image backgroundImage = Gui.loadImage(Gui.loginBackgroundImagePath);
        Background Background = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(window.getHeight(), window.getWidth(), true, true, true, true)));

        //Port and IP scene
        GridPane port_and_IP_Layout = new GridPane();
        port_and_IP_Layout.setPadding( new Insets(20, 20, 20, 20));
        port_and_IP_Layout.setVgap(10);
        port_and_IP_Layout.setHgap(8);
        port_and_IP_Layout.setBackground(Background);

        //choosing IP address input
        Label IP_address_label = new Label("Choose IP address:");
        IP_address_label.getStyleClass().add("labelWithBackground");


        GridPane.setConstraints(IP_address_label, 1, 11);
        //insert the input
        TextField IP_address_input = new TextField("127.0.0.1");
        GridPane.setConstraints(IP_address_input, 2, 11);

        Button nextButton = new Button("Next");
        GridPane.setConstraints(nextButton, 2, 13);
        GridPane.setConstraints(closeButton, 2, 17);
        port_and_IP_Layout.getChildren().addAll( IP_address_label, IP_address_input, nextButton, closeButton);

        this.scene = new Scene(port_and_IP_Layout, 750, 500);
        scene.getStylesheets().add(getClass().getResource(Gui.loginCssPath).toExternalForm());



        nextButton.setOnAction(actionEvent -> {

            chosenIp = IP_address_input.getText();

            if (!Pattern.matches(validIpAddress, chosenIp)) {
                Modal.display("Enter a valid IP address");
                return;
            }

            MyScene next = new ConnectionScene(
                    gui,
                    this.window,
                    this.username,
                    this.votedMap,
                    this.votedSkulls,
                    this.chosenIp
            );

            Scene nextScene = next.getScene();

            window.setScene(nextScene);

        });


    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
