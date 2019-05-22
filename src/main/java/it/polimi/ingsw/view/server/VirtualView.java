package it.polimi.ingsw.view.server;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.server.ServerProxy;
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


public class VirtualView extends Observable implements Observer {

    /**
     * Player nicknames
     */
    ArrayList<String> players;

    ServerProxy serverProxy;

    /**
     * Only constructor
     * @param players players nicknames
     */
    public VirtualView(ArrayList<String> players, ServerProxy serverProxy){

        this.players = players;
        this.serverProxy = serverProxy;
    }

    @Override
    public void notifyObserver(Object arg) {
        //TODO here the view needs to show to all clients that the model is changed by sending custom messages to each one of them

    }

    public void sendAll(ServerQuestion serverQuestion) {
        for(String player : players){
            serverProxy.send(player, serverQuestion.toJSON());
        }

    }

    public void notify(String nickname, String stringMessage){

        ClientAnswer clientAnswer;
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


    public void sendQuestion(String nickname, ServerQuestion serverQuestion){
        serverProxy.send(nickname, serverQuestion.toJSON());
    }


    public void lostConnection(String nickname) {

        //TODO

    }
}