package it.polimi.ingsw.view.server;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.view.ClientAnswer;
import it.polimi.ingsw.server.Receiver;
import it.polimi.ingsw.view.QuestionType;
import it.polimi.ingsw.view.ServerQuestion;

import java.util.ArrayList;



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
    public void update(Object arg) {
        //TODO here the view needs to show to all clients that the model is changed by sending custom messages to each one of them

    }

    public void sendAll(ServerQuestion serverQuestion) {
        for(Receiver r : receivers){
            r.sendMessage(serverQuestion.toJSON());
        }

    }

    public void notify(String nickname, String stringMessage){

        ClientAnswer clientAnswer = null;
        try {
            clientAnswer = new ClientAnswer(nickname, stringMessage);
        }
        catch (IllegalArgumentException e){

            ArrayList<String> errorMessage = new ArrayList<>();
            errorMessage.add("Error while parsing the json message");

            ServerQuestion serverQuestion = new ServerQuestion(QuestionType.TextMessage, errorMessage);
            sendQuestion(nickname, serverQuestion);
            return;
        }


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
     * @param nickname the player
     * @param receiver the new receiver
     */
    public void updateReceiver(String nickname, Receiver receiver) {
        int index = players.indexOf(nickname);
        receivers.set(index, receiver);
    }
}