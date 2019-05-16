package it.polimi.ingsw.view.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.ingsw.view.ClientAnswer;
import it.polimi.ingsw.server.Receiver;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.view.QuestionType;
import it.polimi.ingsw.view.ServerQuestion;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/*
    THE VIEW:
        - What the user sees (UI)
        - In Web Development consists of HTML & CSS, for us it's the CLI & the GUI
        - Communicates with the controller
        - Can be passed dynamic values from the controller
        - Template Engines
 */

@Deprecated
public class VirtualView extends Observable implements Observer {

    /**
     * Player nicknames
     */
    ArrayList<String> players;

    /**
     * The channels to communicate with the player
     */
    ArrayList<Receiver> receivers;

    /**
     * Only constructor
     * @param players players nicknames
     */
    public VirtualView(ArrayList<String> players, ArrayList<Receiver> receivers){

        this.players = players;
        this.receivers = receivers;
    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO here the view needs to show to all clients that the model is changed by sending custom messages to each one of them
        //e.g. It shouldn't tell player X which powerups Player Y has in his hands

        if(!(o instanceof GameView))
            throw new IllegalArgumentException("Illegal update argument for the VirtualView");

        GameView gameView = (GameView) o;

        /*
            I can now work with:
                gameView.gameMap;
                gameView.kst;
                gameView.players;
        */


        //TODO implement network logic

    }

    public int askForIndexPowerupToDiscard(String currentPlayer, ArrayList<PowerUp> playerPowerUps) {
        //TODO Ask the correct player which powerup he wants to discard and return the index
        return 0;
    }
    public int askForIndexPowerupToUse(String currentPlayer, ArrayList<PowerUp> playerPowerUps){
        //TODO ask the correct player which powerup he wants to use and return the index
        return 0;
    }

    public int askForIndexWeaponToReload(ArrayList <Weapon> rechargeableWeapons){
        //TODO  return -1 if i don't want to reload, otherwise i return the index of the weapon that i want to reload
        return 0;
    }

    public String askForPlayerNameToAttackPowerup(ArrayList<String> attackablePlayers) {
        //TODO
        return "NONE";
    }
    public int askForXpos() {
        //TODO
        return 0;
    }
    public int askForYpos() {
        //TODO
        return 0;
    }

    public int askForIndexMoveToDo(String player){
        //TODO ask the move to do to the player and it will be better to do a representation of the map and the players on the map
        return 0;
    }

    public void sendAll(ServerQuestion serverQuestion) {
        //TODO
        for(Receiver r : receivers){
            r.sendMessage(serverQuestion.toJSON());
        }

    }

    public void notify(String nickname, String stringMessage){

        ClientAnswer clientAnswer = new ClientAnswer(nickname, stringMessage);

        setChanged();
        notifyObservers(clientAnswer);
    }

    private Receiver getReceiverByNickname(String nickname) {

        if(!players.contains(nickname))
            throw new RuntimeException(nickname + " is not a valid nickname");

        int index = players.indexOf(nickname);
        return receivers.get(index);
    }

    public void sendQuestion(String nickname, ServerQuestion serverQuestion){
        Receiver r = getReceiverByNickname(nickname);
        r.sendMessage(serverQuestion.toJSON());
    }

    /**
     * Updates the receiver of the given nickname
     * @param nickname
     * @param receiver
     */
    public void updateReceiver(String nickname, Receiver receiver) {
        int index = players.indexOf(nickname);
        receivers.set(index, receiver);
    }
}