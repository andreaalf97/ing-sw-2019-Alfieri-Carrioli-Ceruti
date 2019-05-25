package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.ArrayList;

public class Gui extends Application implements UserInterface {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {

        //Setting up window
        window.setTitle("Adrenalina");

        //Setting up startscene
        VBox layout1 = new VBox(20);
        Scene startGameScene = new Scene(layout1, 200, 200);

        Button startGameButton = new Button("Start game");
        Label label1 = new Label("Welcome to Adrenalina");
        Button closeButton = new Button("Exit");

        closeButton.setOnAction(e -> window.close());

        label1.setAlignment(Pos.TOP_CENTER);
        startGameButton.setAlignment(Pos.CENTER);
        closeButton.setAlignment(Pos.BOTTOM_RIGHT);

        layout1.getChildren().addAll(label1, startGameButton, closeButton);

        //Login scene
        VBox layout2 = new VBox(20);
        Scene loginScene = new Scene(layout2, 200, 200);

        Label loginLabel = new Label("Username:");

        startGameButton.setOnAction(e -> window.setScene(loginScene));






        window.setScene(startGameScene);
        window.show();

    }

    @Override
    public void notify(String json) {

    }

    @Override
    public String askQuestionAction(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionWhereToMove(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionWhereToMoveAndGrab(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChoosePowerUpToRespawn(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChoosePowerUpToDiscard(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionActionChoosePowerUpToAttack(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChooseWeaponToAttack(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChooseWeaponToSwitch(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionChooseWeaponToReload(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionPayWith(String[] possibleAnswers) {
        return null;
    }

    @Override
    public String askQuestionShoot(String[] possibleAnswers) {
        return null;
    }
}
