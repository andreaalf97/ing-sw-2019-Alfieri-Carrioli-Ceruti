package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.PlayerInfo;
import it.polimi.ingsw.client.gui.Modal;
import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.view.client.RemoteView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class PlayersInteractingSpace {

    private ChooseHowToPayToReloadQuestion chooseHowToPayToReloadQuestionevent;
    private ChooseHowToPayToPickWeaponQuestion chooseHowToPayToPickWeaponQuestionEvent;
    private ChooseHowToPayToSwitchWeaponsQuestion chooseHowToPayToSwitchWeaponsQuestionEvent;
    private ChooseHowToPayForAttackingQuestion chooseHowToPayForAttackingQuestionEvent;
    private ChooseHowToPayToReloadBeforeAttackQuestion chooseHowToPayToReloadBeforeAttackQuestion;

    public ArrayList<String> movers;
    public ArrayList<Integer> xCoords;
    public ArrayList<Integer> yCoords;
    private String defenderChosen;

    public GridPane getGridPane() {
        return gridPane;
    }

    private GridPane gridPane;

    private Integer[] chosenOrder;

    private BoardGrid boardGrid;

    private ArrayList<String> defenders;

    private int indexToDiscard;
    private int indexToPick;

    private String playerTarget;

    private ArrayList<String> paymentChosen;

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
        rowPercentages.add(23.000);
        rowPercentages.add(19.250);
        rowPercentages.add(19.250);
        rowPercentages.add(19.250);
        rowPercentages.add(19.250);

        setGrid(gridPane, colPercentages, rowPercentages);
        gridPane.setGridLinesVisible(false);
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

    //******************************************************************************************************************

    public void choosePossibleAction(ActionQuestion event, String username, RemoteView remoteView){

        //Clean the gridpane
        gridPane.getChildren().clear();

        Label label = new Label("Choose action: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");

        gridPane.add(label, 0, 0, 10, 1);
        for ( int i = 0; i < event.possibleAction.size(); i++){
            Button button = new Button(event.possibleAction.get(i));
            String action = event.possibleAction.get(i);
            if ( i%3 == 0)
                gridPane.add(button, i*3, 1, 4, 1);
            if ( i%3 == 1)
                gridPane.add(button, (i - 1)*3, 2, 4, 1);
            if ( i%3 == 2)
                gridPane.add(button, (i - 2)*3, 3, 4, 1);
            button.setOnAction(e -> {
                switch (action){
                    case "Attack":
                        remoteView.sendAnswerEvent(new ActionAttackAnswer(username));
                        break;
                    case "EndTurn":
                        remoteView.sendAnswerEvent(new ActionEndTurnAnswer(username));
                        break;
                    case "MoveAndGrab":
                        remoteView.sendAnswerEvent(new ActionMoveAndGrabAnswer(username));
                        break;
                    case "Move":
                        remoteView.sendAnswerEvent(new ActionMoveAnswer(username));
                        break;
                    case "PickWeapon":
                        remoteView.sendAnswerEvent(new ActionPickWeaponAnswer(username));
                        break;
                    case "Reload":
                        remoteView.sendAnswerEvent(new ActionReloadAnswer(username));
                        break;
                    case "Respawn":
                        remoteView.sendAnswerEvent(new ActionRespawnAnswer(username));
                        break;
                    case "UseTurnPowerUp":
                        remoteView.sendAnswerEvent(new ActionUseTurnPowerUpAnswer(username));
                        break;
                }
            });
        }
    }

    public void actionAfterReloadingQuestion(ActionAfterReloadingQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose action:");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        for (int i = 0; i < event.possibleAction.size(); i++){
            Button button = new Button(event.possibleAction.get(i));
            String action = event.possibleAction.get(i);
            gridPane.add(button, i*4, 1, 4, 1);
            button.setOnAction(e -> {
                switch (action){
                    case "Reload":
                        remoteView.sendAnswerEvent(new ActionReloadAnswer(username));
                        break;
                    case "EndTurn":
                        remoteView.sendAnswerEvent(new ActionEndTurnAnswer(username));
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
        gridPane.add(label, 0, 0, 20, 1);

        for ( int i = 0; i < event.weaponsLoaded.size(); i++){
            Button button = new Button(event.weaponsLoaded.get(i));
            String weaponName = event.weaponsLoaded.get(i);
            gridPane.add(button, i*4, 1, 3, 1);

            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new ChooseWeaponToAttackAnswer(username, weaponName)
                );
            });
        }
    }

    public void askOrderAndDefender(AskOrderAndDefenderQuestion event, String username, RemoteView remoteView, ArrayList<String> playersNames) {

        defenders = new ArrayList<>();
        chosenOrder = new Integer[0];

        gridPane.getChildren().clear();

        Label label = new Label("Choose how to shoot: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);
        Label label1 = new Label("Choose order: ");
        label1.setStyle("-fx-font-size: 15; -fx-color: black");
        gridPane.add(label1, 0, 1, 7, 1);

        ArrayList<String> stringOrders = toStringArray(event.possibleOrders);

        for ( int i = 0; i < stringOrders.size(); i++){
            Button button = new Button(stringOrders.get(i));
            int index = i;
            gridPane.add(button, (i*3)+7, 1, 3, 1);

            button.setOnAction(e -> {
                Integer[] chosenOrder = event.possibleOrders.get(index);
                this.chosenOrder = chosenOrder;
                button.setStyle("-fx-background-color: red");
            });
        }

        Label label2 = new Label("Choose defenders: ");
        label2.setStyle("-fx-font-size: 15; -fx-color: black");
        gridPane.add(label2, 0, 2, 7, 1);

        playersNames.remove(username);

        for ( int i = 0; i < playersNames.size(); i++){
            Button playerNameButton = new Button(playersNames.get(i));
            int index = i;
            gridPane.add(playerNameButton, (i*3)+7, 2, 3, 1);

            playerNameButton.setOnAction(e -> {
                Modal.display(playersNames.get(index)+" added to defenders");
                defenders.add(playersNames.get(index));
                playerNameButton.setStyle("-fx-background-color: red");
            });
        }

        Button button = new Button("next");
        gridPane.add(button, 0, 3, 4, 1);
        button.setOnAction(actionEvent -> {
            remoteView.sendAnswerEvent(
                    new AskOrderAndDefenderAnswer(username, event.chosenWeapon, chosenOrder, defenders)
            );
        });
    }

    public void askChooseIfUseATargetingScopeQuestion(ChooseIfUseATargetingScopeQuestion event, RemoteView remoteView, ArrayList<String> playersNames) {

        gridPane.getChildren().clear();

        Label label = new Label("Choose the player you want to use targeting scope on:");
        label.setStyle("-fx-font-size: 15; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        for ( int i = 0; i < event.defenders.size(); i++){
            Button playerNameButton = new Button(event.defenders.get(i));
            int index = i;
            gridPane.add(playerNameButton, i*3, 1, 3, 1);

            playerNameButton.setOnAction(e -> {
                defenderChosen = playersNames.get(index);
                playerNameButton.setStyle("-fx-background-color: red");
            });
        }

        Button noneButton = new Button("Don't want to use targeting scope");
        gridPane.add(noneButton, 0, 2, 20, 1);
        noneButton.setOnAction(e -> {
            defenderChosen = null;
            noneButton.setStyle("-fx-background-color: red");
        });

        Button next = new Button("next");
        gridPane.add(next, 0, 3, 20, 1);
        next.setOnAction(e -> {
            remoteView.sendAnswerEvent(new ChooseIfUseATargetingScopeAnswer(event.nickname, event.chosenWeapon, event.order, event.shootWithMovement, event.indexOfLastEffect, event.defenders, defenderChosen));

        });
    }

    public void askChooseHowToShootQuestion(ChooseHowToShootQuestion event, String username, RemoteView remoteView, ArrayList<String> playersNames) {

        gridPane.getChildren().clear();

        movers = new ArrayList<>();
        xCoords = new ArrayList<>();
        yCoords = new ArrayList<>();

        if (event.shootWithMovement){

            Label label = new Label("Choose movers: ");
            label.setStyle("-fx-font-size: 20; -fx-color: black");
            gridPane.add(label, 0, 0, 5, 1);

            Button next = new Button("next");
            gridPane.add(next, 0, 3, 4, 1);

            for ( int i = 0; i < playersNames.size(); i++){
                Button playerNameButton = new Button(playersNames.get(i));
                int index = i;
                gridPane.add(playerNameButton, i*3, 1, 3, 1);

                playerNameButton.setOnAction(e -> {
                    Modal.display(this);
                    movers.add(playersNames.get(index));
                    playerNameButton.setStyle("-fx-background-color: red");
                });
            }

            next.setOnAction(actionEvent -> {
                remoteView.sendAnswerEvent(
                        new ChooseHowToShootAnswer(username, event.order, event.chosenWeapon, event.defenders, movers, xCoords, yCoords, event.indexOfLastEffect, event.defenderChosen )
                );
            });
        }else {
            remoteView.sendAnswerEvent(
                    new ChooseHowToShootAnswer(username, event.order, event.chosenWeapon, event.defenders, null, null, null, event.indexOfLastEffect, event.defenderChosen));
        }
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
            });
        }

    }

    public void askChooseWeaponToPickQuestion(ChooseWeaponToPickQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose the weapon you want to pick: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        for (int i = 0; i < event.weaponsToPick.size(); i++){

            Button button = new Button(event.weaponsToPick.get(i));
            gridPane.add(button, i*4, 1, 4, 1);
            int index = i;
            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new ChooseWeaponToPickAnswer(username, event.weaponsToPick.get(index))
                );
            });
        }

    }

    public void askChooseWeaponToReloadQuestion(ChooseWeaponToReloadQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose the weapon you want to reload: ");
        label.setStyle("-fx-font-size: 15; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        for (int i = 0; i < event.weaponsToReload.size(); i++){

            Button button = new Button(event.weaponsToReload.get(i));
            gridPane.add(button, i*4, 1, 4, 1);
            int index = i;
            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new ChooseWeaponToReloadAnswer(username, event.weaponsToReload.get(index))
                );
            });
        }
    }

    public void askChooseWeaponToSwitchQuestion(ChooseWeaponToSwitchQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose the weapon you want to discard: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        for (int i = 0; i < event.weaponsToRemove.size(); i++){

            Button button = new Button(event.weaponsToRemove.get(i));
            gridPane.add(button, i*4, 1, 4, 1);
            int index = i;
            button.setOnAction(e -> {
                indexToDiscard = index;
                button.setStyle("-fx-background-color: red");
            });
        }
        Label label1 = new Label("Choose the weapon you want to pick: ");
        label1.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label1, 0, 2, 20, 1);

        for (int i = 0; i < event.weaponsToPick.size(); i++){

            Button button = new Button(event.weaponsToPick.get(i));
            gridPane.add(button, i*4, 3, 4, 1);
            int index = i;
            button.setOnAction(e -> {
                indexToPick = index;
                button.setStyle("-fx-background-color: red");
            });
        }
        Button next = new Button("next");
        gridPane.add(next, 0, 4, 5, 1);
        next.setOnAction(actionEvent -> {
            remoteView.sendAnswerEvent(
                    new ChooseWeaponToSwitchAnswer(username, event.weaponsToRemove.get(indexToDiscard), event.weaponsToPick.get(indexToPick))
            );
        });
    }

    public void textMessage(TextMessage event) {
        gridPane.getChildren().clear();
        Modal.display(event.message);
    }

    public void askUseGrenadeQuestion(UseGrenadeQuestion event, String username, RemoteView remoteView) {
        gridPane.getChildren().clear();
        Label label = new Label("Do you want to use your grenade against " +  event.offender + " ?");
        label.setStyle("-fx-font-size: 15; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        Button yesButton = new Button("YES");
        Button noButton = new Button("NO");
        Button next = new Button("next");

        gridPane.add(yesButton, 0, 1, 3, 1);
        gridPane.add(noButton, 3, 1, 3, 1);
        gridPane.add(next, 0, 2, 3, 1);

        yesButton.setOnAction(e -> {
            yesButton.setStyle("-fx-background-color: red");
        });
        noButton.setOnAction(e -> {
            noButton.setStyle("-fx-background-color: red");
        });
        next.setOnAction(e -> {
            remoteView.sendAnswerEvent(new UseGrenadeAnswer(username, event.offender));
        });
    }

    public void askChoosePowerUpToUseQuestion(ChoosePowerUpToUseQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose power up to use:");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        for (int i = 0; i < event.powerUpNames.size(); i++){

            Button button = new Button(event.powerUpNames.get(i));
            gridPane.add(button, i*4, 1, 4, 1);
            int index = i;
            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new ChoosePowerUpToUseAnswer(username, event.powerUpNames.get(index), event.colors.get(index))
                );
            });
        }
    }

    public void askChooseHowToUseTurnPowerUpQuestion(ChooseHowToUseTurnPowerUpQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username, ArrayList<String> playersNames) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose the name of the player to move:");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        for (int i = 0; i < playersNames.size(); i++){

            Button button = new Button(playersNames.get(i));
            gridPane.add(button, i*3, 1, 3, 1);
            int index = i;
            button.setOnAction(e -> {
                playerTarget = playersNames.get(index);
                button.setStyle("-fx-background-color: red");
            });
        }

        Label label1 = new Label("insert the coordinates in this format: x,y");
        label1.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label1, 0, 2, 14, 1);

        TextField coordsTextField = new TextField();
        Button okButton = new Button("next");
        gridPane.add(coordsTextField, 15, 2, 5, 1);
        gridPane.add(okButton, 0, 3, 5, 1);

        okButton.setOnAction(e -> {
            String[] coords = coordsTextField.getText().split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            remoteView.sendAnswerEvent(
                    new ChooseHowToUseTurnPowerUpAnswer(username, event.powerUpToUseName,event.powerUpToUseColor, playerTarget, x, y)
            );
        });
    }

    public void askChooseHowToPayForAttackingQuestion(ChooseHowToPayForAttackingQuestion event, RemoteView remoteView, PlayerInfo playerInfo) {

        gridPane.getChildren().clear();
        paymentChosen = new ArrayList<>();
        this.chooseHowToPayForAttackingQuestionEvent = event;
        if ( event.cost.size() != 0) {
            Label label = new Label("Choose how to pay the cost for shooting: ");
            label.setStyle("-fx-font-size: 20; -fx-color: black");
            gridPane.add(label, 0, 0, 10, 1);

            String payForWhat = "HowToPayForAttacking";
            handlePayment(event.cost, playerInfo, payForWhat, remoteView, null);
        }else {
            remoteView.sendAnswerEvent(new ChooseHowToPayForAttackingAnswer(this.chooseHowToPayForAttackingQuestionEvent.chooseHowToShootAnswer, paymentChosen));
        }
    }

    public void askChooseHowToPayToSwitchWeaponsQuestion(ChooseHowToPayToSwitchWeaponsQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username) {

        gridPane.getChildren().clear();
        paymentChosen = new ArrayList<>();
        this.chooseHowToPayToSwitchWeaponsQuestionEvent = event;
        if ( event.weaponCost.size() != 0) {
            Label label = new Label("You chose to discard " + event.weaponToDiscard);
            label.setStyle("-fx-font-size: 20; -fx-color: black");
            gridPane.add(label, 0, 0, 10, 1);

            String payForWhat = "HowToPayToSwitchWeapons";
            handlePayment(event.weaponCost, playerInfo, payForWhat, remoteView, username);
        }else {
            remoteView.sendAnswerEvent(new ChooseHowToPayToSwitchWeaponsAnswer(username, this.chooseHowToPayToSwitchWeaponsQuestionEvent.weaponToPick, paymentChosen, this.chooseHowToPayToSwitchWeaponsQuestionEvent.weaponToDiscard));
        }
    }

    public void askChooseHowToPayToPickWeaponQuestion(ChooseHowToPayToPickWeaponQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username) {

        gridPane.getChildren().clear();
        paymentChosen = new ArrayList<>();
        this.chooseHowToPayToPickWeaponQuestionEvent = event;
        if ( event.cost.size() != 0) {
            Label label = new Label("Choose how to pay to pick " + event.weaponName);
            label.setStyle("-fx-font-size: 20; -fx-color: black");
            gridPane.add(label, 0, 0, 20, 1);

            String payForWhat = "HowToPayToPickWeapon";
            handlePayment(event.cost, playerInfo, payForWhat, remoteView, username);
        }else {
            remoteView.sendAnswerEvent(new ChooseHowToPayToPickWeaponAnswer(username, this.chooseHowToPayToPickWeaponQuestionEvent.weaponName, paymentChosen));
        }
    }

    public void askChooseHowToPayToReloadQuestion(ChooseHowToPayToReloadQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username) {

        gridPane.getChildren().clear();
        paymentChosen = new ArrayList<>();
        this.chooseHowToPayToReloadQuestionevent = event;
        if ( event.cost.size() != 0) {
            Label label = new Label("You chose to reload " + event.weaponToReload);
            label.setStyle("-fx-font-size: 20; -fx-color: black");
            gridPane.add(label, 0, 0, 10, 1);

            String payForWhat = "HowToPayToReload";
            handlePayment(event.cost, playerInfo, payForWhat, remoteView, username);
        }else {
            remoteView.sendAnswerEvent(new ChooseHowToPayToReloadAnswer(username, this.chooseHowToPayToReloadQuestionevent.weaponToReload, paymentChosen));
        }
    }

    public void askChooseHowToPayToReloadBeforeAttackQuestion(ChooseHowToPayToReloadBeforeAttackQuestion event, RemoteView remoteView, String username, PlayerInfo playerInfo) {

        gridPane.getChildren().clear();
        paymentChosen = new ArrayList<>();
        this.chooseHowToPayToReloadBeforeAttackQuestion = event;

        if ( event.cost.size() != 0) {

            Label label = new Label("You chose to reload " + event.weaponToReload);
            label.setStyle("-fx-font-size: 20; -fx-color: black");
            gridPane.add(label, 0, 0, 10, 1);

            String payForWhat = "HowToPayToReloadBeforeAttack";
            handlePayment(event.cost, playerInfo, payForWhat, remoteView, username);
        }else{
            remoteView.sendAnswerEvent(new ChooseHowToPayToReloadBeforeAttackAnswer(username, event.weaponToReload, paymentChosen, event.weaponsLoaded));
        }
    }

    private void handlePayment(List<Color> costToPay, PlayerInfo playerInfo, String payForWhat, RemoteView remoteView, String username) {

        gridPane.getChildren().clear();
        paymentChosen = new ArrayList<>();

        if ( costToPay.size() != 0) {
            String cost = "";

            for (Color c : costToPay)
                cost += c.toString() + " ";

            Label label = new Label("You have to pay --> " + cost);
            label.setStyle("-fx-font-size: 15; -fx-color: black");
            gridPane.add(label, 0, 0, 20, 1);
        }

        for(int i = 0; i < costToPay.size(); i++) {
            Label label1 = new Label("Choose for " + costToPay.get(i) + ":");
            label1.setStyle("-fx-font-size: 15; -fx-color: black");
            gridPane.add(label1, 0, i + 1, 6, 1);

            ArrayList<String> possibleChoice = new ArrayList<>();

            switch (costToPay.get(i)) {
                case RED:
                    if (playerInfo.nRedAmmo > 0)
                        possibleChoice.add(costToPay.get(i).toString());
                    break;
                case BLUE:
                    if (playerInfo.nBlueAmmo > 0)
                        possibleChoice.add(costToPay.get(i).toString());
                    break;
                case YELLOW:
                    if (playerInfo.nYellowAmmo > 0)
                        possibleChoice.add(costToPay.get(i).toString());
                    break;
                case ANY:
                    if (playerInfo.nRedAmmo > 0)
                        possibleChoice.add("RED");
                    if (playerInfo.nBlueAmmo > 0)
                        possibleChoice.add("BLUE");
                    if (playerInfo.nYellowAmmo > 0)
                        possibleChoice.add("YELLOW");
                    break;
            }
            /*for(Color powerUpColor : playerInfo.powerUpColors){
                if(powerUpColor.equals(costToPay.get(i))){
                    int index = playerInfo.powerUpColors.indexOf(powerUpColor);
                    possibleChoice.add(playerInfo.powerUpNames.get(index) + ":" + powerUpColor);
                }
            }*/
            for (int j = 0; j < playerInfo.powerUpNames.size(); j++) {

                if (costToPay.get(i).equals(Color.ANY) && (!playerInfo.powerUpNames.get(j).equals("TargetingScope"))) {
                    possibleChoice.add(playerInfo.powerUpNames.get(j) + ":" + playerInfo.powerUpColors.get(j));
                } else if (!costToPay.get(i).equals(Color.ANY)) {

                    if (playerInfo.powerUpColors.get(j).equals(costToPay.get(i)))
                        possibleChoice.add(playerInfo.powerUpNames.get(j) + ":" + playerInfo.powerUpColors.get(j));
                }
            }
            if (costToPay.get(i) != Color.ANY){
                for (int j = 0; j < possibleChoice.size(); j++) {

                    int index = j;
                    Button button = new Button(possibleChoice.get(j));
                    gridPane.add(button, j * 3 + 6, i + 1, 5, 1);

                    button.setOnAction(actionEvent -> {
                        paymentChosen.add(possibleChoice.get(index));
                        button.setStyle("-fx-background-color: red");
                    });

                    Button nextButton = new Button("next");
                    gridPane.add(nextButton, 16, costToPay.size(), 3, 1);
                    nextButton.setOnAction(e -> {

                        switch (payForWhat) {
                            case "HowToPayToReload":
                                remoteView.sendAnswerEvent(new ChooseHowToPayToReloadAnswer(username, this.chooseHowToPayToReloadQuestionevent.weaponToReload, paymentChosen));
                                break;
                            case "HowToPayToPickWeapon":
                                remoteView.sendAnswerEvent(new ChooseHowToPayToPickWeaponAnswer(username, this.chooseHowToPayToPickWeaponQuestionEvent.weaponName, paymentChosen));
                                break;
                            case "HowToPayToSwitchWeapons":
                                remoteView.sendAnswerEvent(new ChooseHowToPayToSwitchWeaponsAnswer(username, this.chooseHowToPayToSwitchWeaponsQuestionEvent.weaponToPick, paymentChosen, this.chooseHowToPayToSwitchWeaponsQuestionEvent.weaponToDiscard));
                                break;
                            case "HowToPayForAttacking":
                                remoteView.sendAnswerEvent(new ChooseHowToPayForAttackingAnswer(this.chooseHowToPayForAttackingQuestionEvent.chooseHowToShootAnswer, paymentChosen));
                                break;
                            case "HowToPayToReloadBeforeAttack":
                                remoteView.sendAnswerEvent(new ChooseHowToPayToReloadBeforeAttackAnswer(username, this.chooseHowToPayToReloadBeforeAttackQuestion.weaponToReload, paymentChosen, this.chooseHowToPayToReloadBeforeAttackQuestion.weaponsLoaded));
                                break;
                        }
                    });
                }
            }else{
                for (int j = 0; j < possibleChoice.size(); j++) {

                    int index = j;
                    Button button = new Button(possibleChoice.get(j));
                    if ( j%2 == 0) {
                        gridPane.add(button, j * 2 + 4, i + 1, 4, 1);
                    }else {
                        gridPane.add(button, (j - 1) * 2 + 4, i + 2, 4, 1);
                    }
                    button.setOnAction(actionEvent -> {
                        paymentChosen.add(possibleChoice.get(index));
                        button.setStyle("-fx-background-color: red");
                    });

                    Button nextButton = new Button("next");
                    gridPane.add(nextButton, 16, costToPay.size(), 3, 1);
                    nextButton.setOnAction(e -> {

                        switch (payForWhat) {
                            case "HowToPayToReload":
                                remoteView.sendAnswerEvent(new ChooseHowToPayToReloadAnswer(username, this.chooseHowToPayToReloadQuestionevent.weaponToReload, paymentChosen));
                                break;
                            case "HowToPayToPickWeapon":
                                remoteView.sendAnswerEvent(new ChooseHowToPayToPickWeaponAnswer(username, this.chooseHowToPayToPickWeaponQuestionEvent.weaponName, paymentChosen));
                                break;
                            case "HowToPayToSwitchWeapons":
                                remoteView.sendAnswerEvent(new ChooseHowToPayToSwitchWeaponsAnswer(username, this.chooseHowToPayToSwitchWeaponsQuestionEvent.weaponToPick, paymentChosen, this.chooseHowToPayToSwitchWeaponsQuestionEvent.weaponToDiscard));
                                break;
                            case "HowToPayForAttacking":
                                remoteView.sendAnswerEvent(new ChooseHowToPayForAttackingAnswer(this.chooseHowToPayForAttackingQuestionEvent.chooseHowToShootAnswer, paymentChosen));
                                break;
                            case "HowToPayToReloadBeforeAttack":
                                remoteView.sendAnswerEvent(new ChooseHowToPayToReloadBeforeAttackAnswer(username, this.chooseHowToPayToReloadBeforeAttackQuestion.weaponToReload, paymentChosen, this.chooseHowToPayToReloadBeforeAttackQuestion.weaponsLoaded));
                                break;
                        }
                    });
                }
            }
        }
    }

    public void setBoardGrid(BoardGrid boardGrid) {
        this.boardGrid = boardGrid;
    }

    public void askEndGameQuestion(EndGameQuestion event, String username) {

        gridPane.getChildren().clear();

        Label label = new Label(event.winner + " won with " + event.winnerPoints + "points.");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        if(username.equals(event.winner)){
            Modal.display("Congratulations, you won!");
        }
        else
        {
            Modal.display("Better luck next time :)");
        }
    }

    public void askSendCanReloadBeforeShootingQuestion(SendCanReloadBeforeShootingQuestion event, RemoteView remoteView, String username) {

        gridPane.getChildren().clear();

        Label label = new Label("Want to reload before shooting?");
        label.setStyle("-fx-font-size: 15; -fx-color: black");
        gridPane.add(label, 0, 0, 13, 1);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        gridPane.add(yesButton, 13, 0, 2, 1);
        gridPane.add(noButton, 15, 0, 2, 1);

        yesButton.setOnAction(actionEvent -> {

            yesButton.setStyle("-fx-background-color: red");
            Label label1 = new Label("Which weapon?");
            label1.setStyle("-fx-font-size: 15; -fx-color: black");
            gridPane.add(label1, 0, 1, 6, 1);

            for ( int i = 0; i < event.rechargeableWeaponNames.size(); i++){
                int index = i;
                Button weaponButton = new Button(event.rechargeableWeaponNames.get(i));
                gridPane.add(weaponButton, i*4, 2, 4, 1);

                weaponButton.setOnAction(actionEvent1 -> {
                    remoteView.sendAnswerEvent( new SendCanReloadBeforeShootingAnswer(username, event.rechargeableWeaponNames.get(index), event.weaponsLoaded));
                });
            }
        });

        noButton.setOnAction(actionEvent -> {
            noButton.setStyle("-fx-background-color: red");

            remoteView.sendAnswerEvent( new SendCanReloadBeforeShootingAnswer(username,null, event.weaponsLoaded));
        });
    }

    public void askSendCanMoveBeforeShootingQuestion(SendCanMoveBeforeShootingQuestion event, RemoteView remoteView, String username) {

        xCoords = new ArrayList<>();
        yCoords = new ArrayList<>();

        gridPane.getChildren().clear();

        Label label = new Label("Want to move before shooting?");
        label.setStyle("-fx-font-size: 15; -fx-color: black");
        gridPane.add(label, 0, 0, 13, 1);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        gridPane.add(yesButton, 0, 1, 3, 1);
        gridPane.add(noButton, 3, 1, 3, 1);

        yesButton.setOnAction(actionEvent -> {
            yesButton.setStyle("-fx-background-color: red");
            Modal.display(this);
            remoteView.sendAnswerEvent(new SendCanMoveBeforeShootingAnswer(username, xCoords.get(0), yCoords.get(0), event.weaponsLoaded));
        });

        noButton.setOnAction(actionEvent -> {
            remoteView.sendAnswerEvent(new SendCanMoveBeforeShootingAnswer(username, -1, -1, event.weaponsLoaded));
        });
    }
}
