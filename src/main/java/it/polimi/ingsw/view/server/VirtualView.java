package it.polimi.ingsw.view.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.client.Cli;
import it.polimi.ingsw.view.ClientAnswer;
import it.polimi.ingsw.view.QuestionType;
import it.polimi.ingsw.view.ServerQuestion;
import it.polimi.ingsw.view.client.RemoteViewInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;



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

    ArrayList<Socket> sockets;

    ArrayList<RemoteViewInterface> remoteViews;

    final String SPLITTER = "$";

    /**
     * Only constructor
     * @param players players nicknames
     */
    public VirtualView(ArrayList<String> players, ArrayList<Socket> sockets, ArrayList<RemoteViewInterface> remoteViews){

        this.players = players;
        this.sockets = sockets;
        this.remoteViews = remoteViews;

    }

    @Override
    public void notifyObserver(Object arg) {
        //TODO here the view needs to show to all clients that the model is changed by sending custom messages to each one of them

    }

    public void sendQuestion(String nickname, ServerQuestion serverQuestion){

        int i = players.indexOf(nickname);

        //If the player is running on socket
        if(sockets.get(i) != null){

            try {
                PrintWriter printWriter = new PrintWriter(sockets.get(i).getOutputStream());
                printWriter.println("QUESTION" + SPLITTER + serverQuestion.toJSON());
                printWriter.flush();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
        else {

            RemoteViewInterface remoteView = remoteViews.get(i);
            int answer = 0;
            String[] possibleAnswers = (String[])serverQuestion.possibleAnswers.toArray();

            try {
                switch (serverQuestion.questionType) {

                    case Action:
                        answer = remoteView.askQuestionAction(possibleAnswers);
                        break;

                    case WhereToMove:
                        answer = remoteView.askQuestionWhereToMove(possibleAnswers);
                        break;

                    case WhereToMoveAndGrab:
                        answer = remoteView.askQuestionWhereToMoveAndGrab(possibleAnswers);
                        break;

                    case ChoosePowerUpToRespawn:
                        answer = remoteView.askQuestionChoosePowerUpToRespawn(possibleAnswers);
                        break;

                    case UseTurnPowerUp:
                        answer = remoteView.askQuestionUseTurnPowerUp(possibleAnswers);
                        break;

                    case UseAsyncPowerUp:
                        answer = remoteView.askQuestionUseAsyncPowerUp(possibleAnswers);
                        break;

                    case ChoosePowerUpToUse:
                        answer = remoteView.askQuestionChoosePowerUpToAttack(possibleAnswers);
                        break;

                    case ChooseWeaponToAttack:
                        answer = remoteView.askQuestionChooseWeaponToAttack(possibleAnswers);
                        break;

                    case ChooseWeaponToSwitch:
                        answer = remoteView.askQuestionChooseWeaponToSwitch(possibleAnswers);
                        break;

                    case ChooseWeaponToReload:
                        answer = remoteView.askQuestionChooseWeaponToReload(possibleAnswers);
                        break;

                    case PayWith:
                        answer = remoteView.askQuestionPayWith(possibleAnswers);
                        break;

                    case Shoot:
                        answer = remoteView.askQuestionShoot(possibleAnswers);
                        break;

                    default:
                        throw new RuntimeException("Invalid question");
                }

                ClientAnswer clientAnswer = new ClientAnswer(nickname, serverQuestion, serverQuestion.possibleAnswers.indexOf(answer));
                notifyObservers(clientAnswer);
            }
            catch (RemoteException e){
                MyLogger.LOGGER.log(Level.SEVERE, "Error while sending question");
                e.printStackTrace();
            }


        }

    }

    public void lostConnection(String nickname) {

        //TODO

    }

    public void sendMessage(String nickname, String message) {

        int index = players.indexOf(nickname);

        if(sockets.get(index) != null){

            try {
                PrintWriter printWriter = new PrintWriter(sockets.get(index).getOutputStream());
                printWriter.println("MESSAGE" + SPLITTER + message );
                printWriter.flush();
            }
            catch (IOException e){
                MyLogger.LOGGER.log(Level.SEVERE, "Error while sending message");
            }

        }
        else {

            try {
                RemoteViewInterface remoteView = remoteViews.get(index);
                remoteView.sendMessage(message);
            }
            catch (RemoteException e){
                MyLogger.LOGGER.log(Level.SEVERE, "Error while sending message");
            }

        }

    }

    public void sendAllMessage(String message) {

        for(String player : players)
            sendMessage(player, message);

    }
}