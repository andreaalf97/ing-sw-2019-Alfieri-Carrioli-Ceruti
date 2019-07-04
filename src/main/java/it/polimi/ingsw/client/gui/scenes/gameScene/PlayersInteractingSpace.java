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

    public ArrayList<String> movers;
    public ArrayList<Integer> xCoords;
    public ArrayList<Integer> yCoords;

    public GridPane getGridPane() {
        return gridPane;
    }

    private GridPane gridPane;

    private Integer[] chosenOrder;

    private BoardGrid boardGrid;

    private ArrayList<String> defenders;

    private int indexToDiscard;

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
        gridPane.add(label, 0, 0, 20, 1);

        for ( int i = 0; i < event.weaponsLoaded.size(); i++){
            Button button = new Button(event.weaponsLoaded.get(i));
            String weaponName = event.weaponsLoaded.get(i);
            gridPane.add(button, i*4, 1, 3, 1);

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
        Label label1 = new Label("Choose order: ");
        label1.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label1, 0, 1, 5, 1);

        ArrayList<String> stringOrders = toStringArray(event.possibleOrders);

        for ( int i = 0; i < stringOrders.size(); i++){
            Button button = new Button(stringOrders.get(i));
            int index = i;
            gridPane.add(button, (i*2)+5, 1, 2, 1);

            button.setOnAction(e -> {
                Integer[] chosenOrder = event.possibleOrders.get(index);
                this.chosenOrder = chosenOrder;
                button.setStyle("-fx-background-color: red");
            });
        }

        Label label2 = new Label("Choose defenders: ");
        label2.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label2, 0, 2, 4, 1);


        playersNames.remove(username);

        for ( int i = 0; i < playersNames.size(); i++){
            Button playerNameButton = new Button(playersNames.get(i));
            int index = i;
            gridPane.add(playerNameButton, (i*3)+4, 2, 3, 1);

            playerNameButton.setOnAction(e -> {
                Modal.display(playersNames.get(index)+"added to defenders");
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

        //have to clean defenders and chosen order for the next attack
        defenders.clear();
        chosenOrder = new Integer[0];

        gridPane.getChildren().clear();
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
            gridPane.add(next, 0, 2, 4, 1);

            for ( int i = 0; i < playersNames.size(); i++){
                Button playerNameButton = new Button(playersNames.get(i));
                int index = i;
                gridPane.add(playerNameButton, i*3, 1, 3, 1);

                playerNameButton.setOnAction(e -> {
                    Modal.display(playersNames.get(index)+"added to movers", this);
                    movers.add(playersNames.get(index));
                    playerNameButton.setStyle("-fx-background-color: red");
                });
            }

            next.setOnAction(actionEvent -> {
                remoteView.sendAnswerEvent(
                        new ChooseHowToShootAnswer(username, event.order, event.chosenWeapon, event.defenders, movers, xCoords, yCoords, event.indexOfLastEffect, "" )
                );
            });
        }else {
            remoteView.sendAnswerEvent(
                    new ChooseHowToShootAnswer(username, event.order, event.chosenWeapon, event.defenders, null, null, null, event.indexOfLastEffect, ""));
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
                gridPane.getChildren().clear();
            });
        }

    }

    public void askChooseWeaponToReloadQuestion(ChooseWeaponToReloadQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose the weapon you want to reload: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        for (int i = 0; i < event.weaponsToReload.size(); i++){

            Button button = new Button(event.weaponsToReload.get(i));
            gridPane.add(button, i*4, 1, 4, 1);
            int index = i;
            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new ChooseWeaponToReloadAnswer(username, event.weaponsToReload.get(index))
                );
                gridPane.getChildren().clear();
            });
        }
    }

    public void askChooseWeaponToSwitchQuestion(ChooseWeaponToSwitchQuestion event, String username, RemoteView remoteView) {
        gridPane.getChildren().clear();
        Label label = new Label("Choose the weapon you want to discard: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        for (int i = 0; i < event.weaponsToRemove.size(); i++){

            Button button = new Button(event.weaponsToRemove.get(i));
            gridPane.add(button, i*4, 1, 4, 1);
            int index = i;
            button.setOnAction(e -> {
                indexToDiscard = index;
                gridPane.getChildren().clear();
            });
        }
        Label label1 = new Label("Choose the weapon you want to pick: ");
        label1.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label1, 0, 0, 10, 1);

        for (int i = 0; i < event.weaponsToPick.size(); i++){

            Button button = new Button(event.weaponsToPick.get(i));
            gridPane.add(button, i*4, 1, 4, 1);
            int index = i;
            button.setOnAction(e -> {
                remoteView.sendAnswerEvent(
                        new ChooseWeaponToSwitchAnswer(username, event.weaponsToRemove.get(indexToDiscard), event.weaponsToPick.get(index))
                );
                gridPane.getChildren().clear();
            });
        }
    }

    public void textMessage(TextMessage event) {
        gridPane.getChildren().clear();
        Label label = new Label(event.message);
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);
    }

    public void askUseGrenadeQuestion(UseGrenadeQuestion event, String username, RemoteView remoteView) {
        gridPane.getChildren().clear();
        Label label = new Label("Do you want to use your grenade against " +  event.offender + " ?");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        Button yesButton = new Button("YES");
        Button noButton = new Button("NO");

        yesButton.setOnAction(e -> {
            remoteView.sendAnswerEvent(new UseGrenadeAnswer(username, event.offender));
            gridPane.getChildren().clear();
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
                gridPane.getChildren().clear();
            });
        }
    }

    public void askChooseHowToUseTurnPowerUpQuestion(ChooseHowToUseTurnPowerUpQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username, ArrayList<String> playersNames) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose the name of the player to move:");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);


        for (int i = 0; i < playersNames.size(); i++){

            Button button = new Button(playersNames.get(i));
            gridPane.add(button, i*3, 1, 3, 1);
            int index = i;
            button.setOnAction(e -> {
                playerTarget = playersNames.get(index);
                gridPane.getChildren().clear();
            });
        }
        gridPane.getChildren().clear();

        Label label1 = new Label("insert the coordinates in this format: x,y");
        label1.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label1, 0, 0, 20, 1);

        TextField coordsTextField = new TextField();
        Button okButton = new Button("next");
        gridPane.add(coordsTextField, 0, 1, 5, 1);
        gridPane.add(okButton, 6, 1, 5, 1);

        okButton.setOnAction(e -> {
            String[] coords = coordsTextField.getText().split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            remoteView.sendAnswerEvent(
                    new ChooseHowToUseTurnPowerUpAnswer(username, event.powerUpToUseName,event.powerUpToUseColor, playerTarget, x, y)
            );
            gridPane.getChildren().clear();
        });

    }

    public void askChooseHowToPayForAttackingQuestion(ChooseHowToPayForAttackingQuestion event, RemoteView remoteView, PlayerInfo playerInfo) {

        gridPane.getChildren().clear();
        this.chooseHowToPayForAttackingQuestionEvent = event;
        Label label = new Label("Choose how to pay the cost for shooting: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        String payForWhat = "HowToPayForAttacking";
        handlePayment(event.cost, playerInfo, payForWhat, remoteView, null);

        gridPane.getChildren().clear();

    }

    public void askChooseHowToPayToSwitchWeaponsQuestion(ChooseHowToPayToSwitchWeaponsQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username) {

        gridPane.getChildren().clear();
        this.chooseHowToPayToSwitchWeaponsQuestionEvent = event;
        Label label = new Label("You chose to discard " + event.weaponToDiscard);
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        String payForWhat = "HowToPayToSwitchWeapons";
        handlePayment(event.weaponCost, playerInfo, payForWhat, remoteView, username);

        gridPane.getChildren().clear();
    }

    public void askChooseHowToPayToPickWeaponQuestion(ChooseHowToPayToPickWeaponQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username) {

        gridPane.getChildren().clear();
        this.chooseHowToPayToPickWeaponQuestionEvent = event;
        Label label = new Label("Choose how to pay to pick " + event.weaponName);
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        String payForWhat = "HowToPayToPickWeapon";
        handlePayment(event.cost, playerInfo, payForWhat, remoteView, username);
    }

    public void askChooseHowToPayToReloadQuestion(ChooseHowToPayToReloadQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username) {

        gridPane.getChildren().clear();
        this.chooseHowToPayToReloadQuestionevent = event;
        Label label = new Label("You chose to reload " + event.weaponToReload);
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        String payForWhat = "HowToPayToReload";
        handlePayment(event.cost, playerInfo, payForWhat, remoteView, username);

        gridPane.getChildren().clear();
    }

    private void handlePayment(List<Color> costToPay, PlayerInfo playerInfo, String payForWhat, RemoteView remoteView, String username) {

        gridPane.getChildren().clear();
        String cost = "";

        for(Color c : costToPay)
            cost += c.toString() + " ";

        Label label = new Label("You have to pay --> " + cost);
        label.setStyle("-fx-font-size: 15; -fx-color: black");
        gridPane.add(label, 0, 0, 20, 1);

        paymentChosen = new ArrayList<>();

        for(int i = 0; i < costToPay.size(); i++){
            Label label1 = new Label("Choose for "+ costToPay.get(i)+":");
            label1.setStyle("-fx-font-size: 15; -fx-color: black");
            gridPane.add(label1, 0, i+1, 6, 1);

            ArrayList<String> possibleChoice = new ArrayList<>();

            switch (costToPay.get(i)){
                case RED:
                    if(playerInfo.nRedAmmo > 0)
                        possibleChoice.add(costToPay.get(i).toString());
                    break;
                case BLUE:
                    if(playerInfo.nBlueAmmo > 0)
                        possibleChoice.add(costToPay.get(i).toString());
                    break;
                case YELLOW:
                    if(playerInfo.nYellowAmmo > 0)
                        possibleChoice.add(costToPay.get(i).toString());
                    break;
                case ANY:
                    if(playerInfo.nRedAmmo > 0)
                        possibleChoice.add(costToPay.get(i).toString());
                    if(playerInfo.nBlueAmmo > 0)
                        possibleChoice.add(costToPay.get(i).toString());
                    if(playerInfo.nYellowAmmo > 0)
                        possibleChoice.add(costToPay.get(i).toString());
                    break;
            }
            for(Color powerUpColor : playerInfo.powerUpColors){
                if(powerUpColor.equals(costToPay.get(i))){
                    int index = playerInfo.powerUpColors.indexOf(powerUpColor);
                    possibleChoice.add(playerInfo.powerUpNames.get(index) + ":" + powerUpColor);
                }
            }
            for (int j = 0; j < possibleChoice.size(); j++){

                int index = j;
                Button button = new Button(possibleChoice.get(j));
                gridPane.add(button, j*3 + 6, i+1, 5, 1);

                button.setOnAction(actionEvent -> {
                    paymentChosen.add(possibleChoice.get(index));
                    button.setStyle("-fx-background-color: red");
                });

                Button nextButton = new Button("next");
                gridPane.add(nextButton, 16, costToPay.size(), 3, 1);
                nextButton.setOnAction(e -> {

                    switch (payForWhat){

                        case "HowToPayToReload":
                            remoteView.sendAnswerEvent(new ChooseHowToPayToReloadAnswer(username, this.chooseHowToPayToReloadQuestionevent.weaponToReload, paymentChosen));
                            gridPane.getChildren().clear();
                            break;

                        case "HowToPayToPickWeapon":
                            remoteView.sendAnswerEvent(new ChooseHowToPayToPickWeaponAnswer(username, this.chooseHowToPayToPickWeaponQuestionEvent.weaponName, paymentChosen));
                            gridPane.getChildren().clear();
                            break;

                        case "HowToPayToSwitchWeapons":
                            remoteView.sendAnswerEvent(new ChooseHowToPayToSwitchWeaponsAnswer(username, this.chooseHowToPayToSwitchWeaponsQuestionEvent.weaponToPick, paymentChosen, this.chooseHowToPayToSwitchWeaponsQuestionEvent.weaponToDiscard));
                            gridPane.getChildren().clear();
                            break;

                        case "HowToPayForAttacking":
                            remoteView.sendAnswerEvent(new ChooseHowToPayForAttackingAnswer(this.chooseHowToPayForAttackingQuestionEvent.chooseHowToShootAnswer, paymentChosen));
                            gridPane.getChildren().clear();
                            break;
                    }
                });
            }
        }
    }

    public void setBoardGrid(BoardGrid boardGrid) {
        this.boardGrid = boardGrid;
    }
}
