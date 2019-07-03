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

    public GridPane getGridPane() {
        return gridPane;
    }

    private GridPane gridPane;

    private Integer[] chosenOrder;

    private ArrayList<String> defenders;

    private int indexToDiscard;

    private String playerTarget;

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

    public void askChooseWeaponToPickQuestion(ChooseWeaponToPickQuestion event, String username, RemoteView remoteView) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose the weapon you want to pick: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

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
        Label label = new Label("[*] NEW MESSAGE: " + event.message);
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
        gridPane.add(label1, 0, 0, 10, 1);

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
        Label label = new Label("Choose how to pay the cost for shooting: ");
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        ArrayList<String> paymentChosen = new ArrayList<>();

        String cost = "";
        if(!event.cost.isEmpty()) {
            for (Color c : event.cost)
                cost += c.toString() + " ";

            Label label1 = new Label("You have to pay --> " + cost);
            label1.setStyle("-fx-font-size: 20; -fx-color: black");
            gridPane.add(label1, 0, 1, 10, 1);

            paymentChosen = handlePayment(event.cost, playerInfo);

            remoteView.sendAnswerEvent(
                    new ChooseHowToPayForAttackingAnswer(event.chooseHowToShootAnswer, paymentChosen)
            );
        }
    }
    public void askChooseHowToPayToSwitchWeaponsQuestion(ChooseHowToPayToSwitchWeaponsQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username) {

        gridPane.getChildren().clear();
        Label label = new Label("You chose to discard " + event.weaponToDiscard);
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        ArrayList<String> paymentChoice = handlePayment(event.weaponCost, playerInfo);

        remoteView.sendAnswerEvent(
                new ChooseHowToPayToSwitchWeaponsAnswer(username, event.weaponToPick, paymentChoice, event.weaponToDiscard)
        );
        gridPane.getChildren().clear();
    }

    public void askChooseHowToPayToPickWeaponQuestion(ChooseHowToPayToPickWeaponQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username) {

        gridPane.getChildren().clear();
        Label label = new Label("Choose how to pay to pick " + event.weaponName);
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        ArrayList<String> paymentChosen = handlePayment(event.cost, playerInfo);

        remoteView.sendAnswerEvent(
                new ChooseHowToPayToPickWeaponAnswer(username, event.weaponName, paymentChosen)
        );
        gridPane.getChildren().clear();
    }

    public void askChooseHowToPayToReloadQuestion(ChooseHowToPayToReloadQuestion event, RemoteView remoteView, PlayerInfo playerInfo, String username) {

        gridPane.getChildren().clear();
        Label label = new Label("You chose to reload " + event.weaponToReload);
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        ArrayList<String> chosenPayment = handlePayment(event.cost, playerInfo);

        remoteView.sendAnswerEvent(new ChooseHowToPayToReloadAnswer(username, event.weaponToReload, chosenPayment));

        gridPane.getChildren().clear();

    }

    private ArrayList<String> handlePayment(List<Color> costToPay, PlayerInfo playerInfo) {

        gridPane.getChildren().clear();

        String cost = "";

        for(Color c : costToPay)
            cost += c.toString() + " ";

        Label label = new Label("You have to pay --> " + cost);
        label.setStyle("-fx-font-size: 20; -fx-color: black");
        gridPane.add(label, 0, 0, 10, 1);

        ArrayList<String> paymentChosen = new ArrayList<>();

        for(Color colorToPay : costToPay){
            gridPane.getChildren().clear();
            Label label1 = new Label("How would you like to pay for " + colorToPay);
            label1.setStyle("-fx-font-size: 20; -fx-color: black");
            gridPane.add(label1, 0, 0, 10, 1);

            ArrayList<String> possibleChoice = new ArrayList<>();

            switch (colorToPay){

                case RED:
                    if(playerInfo.nRedAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    break;

                case BLUE:
                    if(playerInfo.nBlueAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    break;

                case YELLOW:
                    if(playerInfo.nYellowAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    break;

                case ANY:
                    if(playerInfo.nRedAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    if(playerInfo.nBlueAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    if(playerInfo.nYellowAmmo > 0)
                        possibleChoice.add(colorToPay.toString());
                    break;
            }

            for(Color powerUpColor : playerInfo.powerUpColors){

                if(powerUpColor.equals(colorToPay)){
                    int index = playerInfo.powerUpColors.indexOf(powerUpColor);
                    possibleChoice.add(playerInfo.powerUpNames.get(index) + ":" + powerUpColor);
                }

            }

            for (int i = 0; i < possibleChoice.size(); i++){

                Button button = new Button(possibleChoice.get(i));
                if ( i%2 == 0) {
                    gridPane.add(button, i*2, 1, 2, 1);
                }else {
                    gridPane.add(button, i*2 - 2, 2, 2, 1);
                }

                int index = i;
                button.setOnAction(e -> {
                    paymentChosen.add(possibleChoice.get(index));
                    gridPane.getChildren().clear();
                });
            }
        }
        return paymentChosen;
    }
}
