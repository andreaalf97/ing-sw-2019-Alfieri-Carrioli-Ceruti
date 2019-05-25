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
    public int askQuestionChoosePowerUpToDiscard(String[] possibleAnswers) {
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
}
