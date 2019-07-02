package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.gui.Modal;
import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.view.client.RemoteView;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class PlayersInteractingSpace {

    public GridPane getGridPane() {
        return gridPane;
    }

    private GridPane gridPane;

    private Integer[] chosenOrder;

    private ArrayList<String> defenders;

    public PlayersInteractingSpace(){

        this.gridPane = setGridPane();

        this.chosenOrder = new Integer[0];

        this.defenders = new ArrayList<>();

        setWelcomeLabel();
    }

    private void setWelcomeLabel() {
        Label label = new Label("Game just started");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 1, 1, 10, 1);
    }

    private GridPane setGridPane() {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);

        ArrayList<Double> colPercentages = new ArrayList<>();
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);
        colPercentages.add(5.000);

        ArrayList<Double> rowPercentages = new ArrayList<>();
        rowPercentages.add(25.000);
        rowPercentages.add(25.000);
        rowPercentages.add(25.000);
        rowPercentages.add(25.000);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    private void setGrid(GridPane pane, ArrayList<Double> colPercentages, ArrayList<Double> rowPercentages) {

        for(Double percentage : colPercentages){
            ColumnConstraints colConstrains = new ColumnConstraints();
            colConstrains.setPercentWidth(percentage);
            pane.getColumnConstraints().add(colConstrains);
        }


        for(Double percentage : rowPercentages){
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(percentage);
            pane.getRowConstraints().add(rowConstraints);
        }

    }

    public void choosePossibleAction(ActionQuestion event, String username, RemoteView remoteView){

        //Clean the gridpane
        gridPane.getChildren().clear();

        Label label = new Label("Choose action: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");

        gridPane.add(label, 0, 0, 10, 1);
        for ( int i = 0; i < event.possibleAction.size(); i++){
            Button button = new Button(event.possibleAction.get(i));
            String action = event.possibleAction.get(i);
            gridPane.add(button, i*3, 1, 3, 1);
            button.setOnAction(e -> {
                switch (action){

                    case "Attack":
                        remoteView.sendAnswerEvent(new ActionAttackAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "EndTurn":
                        remoteView.sendAnswerEvent(new ActionEndTurnAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "MoveAndGrab":
                        remoteView.sendAnswerEvent(new ActionMoveAndGrabAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "Move":
                        remoteView.sendAnswerEvent(new ActionMoveAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "PickWeapon":
                        remoteView.sendAnswerEvent(new ActionPickWeaponAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "Reload":
                        remoteView.sendAnswerEvent(new ActionReloadAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "Respawn":
                        remoteView.sendAnswerEvent(new ActionRespawnAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                    case "UseTurnPowerUp":
                        remoteView.sendAnswerEvent(new ActionUseTurnPowerUpAnswer(username));
                        gridPane.getChildren().clear();
                        break;

                }
            });
        }

    }

    public void chooseWeaponToAttack(ChooseWeaponToAttackQuestion event, String username, RemoteView remoteView) {
        //Clean the gridpane
        gridPane.getChildren().clear();

        Label label = new Label("Choose the weapon you want to use for the attack: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        for ( int i = 0; i < event.weaponsLoaded.size(); i++){
            Button button = new Button(event.weaponsLoaded.get(i));
            String weaponName = event.weaponsLoaded.get(i);
            gridPane.add(button, i*4, 0, 3, 1);

            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new ChooseWeaponToAttackAnswer(username, weaponName)
                );
                gridPane.getChildren().clear();
            });
        }
    }

    public void askOrderAndDefender(AskOrderAndDefenderQuestion event, String username, RemoteView remoteView, ArrayList<String> playersNames) {

        gridPane.getChildren().clear();

        Label label = new Label("Choose how to shoot: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);
        Label label1 = new Label("Let's start with the order: ");
        label1.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label1, 0, 1, 10, 1);

        ArrayList<String> stringOrders = toStringArray(event.possibleOrders);

        for ( int i = 0; i < stringOrders.size(); i++){
            Button button = new Button(stringOrders.get(i));
            int index = i;
            gridPane.add(button, i*4, 2, 3, 1);

            button.setOnAction(e -> {
                Integer[] chosenOrder = event.possibleOrders.get(index);
                setChoseOrder(chosenOrder);
                gridPane.getChildren().clear();
            });
        }

        //cleaning the board before asking for the defenders
        gridPane.getChildren().clear();

        Label label2 = new Label("Now the defenders: ");
        label2.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label2, 0, 0, 10, 1);


        Button button = new Button("STOP");
        gridPane.add(button, 0, 2, 4, 1);
        button.setOnAction(actionEvent -> {
            remoteView.sendAnswerEvent(
                    new AskOrderAndDefenderAnswer(username, event.chosenWeapon, chosenOrder, defenders)
            );
            gridPane.getChildren().clear();
        });

        playersNames.remove(username);

        for ( int i = 0; i < playersNames.size(); i++){
            Button playerNameButton = new Button(playersNames.get(i));
            int index = i;
            gridPane.add(playerNameButton, i*4, 1, 3, 1);

            playerNameButton.setOnAction(e -> {
                Modal.display(playersNames.get(index)+"added to defenders");
                defenders.add(playersNames.get(index));
            });
        }

        //have to clean defenders and chosen order for the next attack
        defenders.clear();
        chosenOrder = new Integer[0];

        gridPane.getChildren().clear();

    }

    private void setChoseOrder(Integer[] chosenOrder) {
        this.chosenOrder = chosenOrder;
    }

    /**
     * modify an arrayList<Integer[]> in an ArrayList<String> to print the choices
     * @param possibleOrders the order of the weapon
     * @return the arrayList<String> correspondent
     */
    private ArrayList<String> toStringArray(ArrayList<Integer[]> possibleOrders) {

        ArrayList<String> returnValue = new ArrayList<>();

        for(Integer[] order : possibleOrders){

            String stringOrder = "[";

            for(int i = 0; i < order.length - 1; i++)
                stringOrder += order[i] + ", ";

            stringOrder += order[order.length - 1] + "]";

            returnValue.add(stringOrder);

        }

        return returnValue;

    }

    public void askChooseHowToShoot(ChooseHowToShootQuestion event, String username, RemoteView remoteView, ArrayList<String> playersNames) {
        //TODO
    }

    public void askChoosePowerUpToRespawnQuestion(ChoosePowerUpToRespawnQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose power up to respawn: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        for (int i = 0; i < event.powerUpToRespawn.size(); i++){

            Button button = new Button(event.powerUpToRespawn.get(i) + event.colors.get(i).toString());
            gridPane.add(button, i*5, 1, 5, 1);

            int index = i;

            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new ChoosePowerUpToRespawnAnswer(username, event.powerUpToRespawn.get(index), event.colors.get(index))
                );
                gridPane.getChildren().clear();
            });
        }

    }

    public void askWhereToMoveAndGrabQuestion(WhereToMoveAndGrabQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose where to move and grab: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        ArrayList<int[]> possibleCoords = new ArrayList<>();

        //Fills the list with all possible coords
        for(int i = 0; i < event.possibleSpots.length; i++) {
            for (int j = 0; j < event.possibleSpots[i].length; j++) {
                if (event.possibleSpots[i][j] == true) {
                    int[] newCoord = new int[2];
                    newCoord[0] = i;
                    newCoord[1] = j;
                    possibleCoords.add(newCoord);
                }
            }
        }

        for (int i = 0 ; i < possibleCoords.size(); i++){

            String coordinate ="(" + possibleCoords.get(i)[0] + ", " +  possibleCoords.get(i)[1] + ")";

            Button button = new Button(coordinate);
            button.setMinWidth(65);
            if ( i%2 == 0) {
                gridPane.add(button, i, 1, 1, 2);
            }else {
                gridPane.add(button, i - 1, 2, 1, 2);
            }

            int index = i;

            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new WhereToMoveAndGrabAnswer(username, possibleCoords.get(index)[0], possibleCoords.get(index)[1])
                );
                gridPane.getChildren().clear();
            });
        }


    }

    public void askWhereToMoveQuestion(WhereToMoveQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose where to move: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        ArrayList<int[]> possibleCoords = new ArrayList<>();

        //Fills the list with all possible coords
        for(int i = 0; i < event.possibleSpots.length; i++) {
            for (int j = 0; j < event.possibleSpots[i].length; j++) {
                if (event.possibleSpots[i][j] == true) {
                    int[] newCoord = new int[2];
                    newCoord[0] = i;
                    newCoord[1] = j;
                    possibleCoords.add(newCoord);
                }
            }
        }

        for (int i = 0 ; i < possibleCoords.size(); i++){

            String coordinate ="(" + possibleCoords.get(i)[0] + ", " +  possibleCoords.get(i)[1] + ")";

            Button button = new Button(coordinate);
            button.setMinWidth(65);
            if ( i%2 == 0) {
                gridPane.add(button, i, 1, 1, 2);
            }else {
                gridPane.add(button, i - 1, 2, 1, 2);
            }

            int index = i;

            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new WhereToMoveAnswer(username, possibleCoords.get(index)[0], possibleCoords.get(index)[1])
                );
                gridPane.getChildren().clear();
            });
        }

    }

}
