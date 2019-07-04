package it.polimi.ingsw.view.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.DisconnectedAnswer;
import it.polimi.ingsw.view.client.RemoteViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class ServerProxyRmi extends UnicastRemoteObject implements ServerProxy, ServerProxyRmiInterface {

    /**
     * The nickname of the user connected to this proxy
     */
    private String nickname;

    /**
     * The receiver of all answers
     */
    private AnswerEventReceiver receiver;

    /**
     * The remote view to send all questions to
     */
    private final RemoteViewInterface remoteViewInterface;


    public ServerProxyRmi(String nickname, AnswerEventReceiver receiver, RemoteViewInterface remoteViewInterface) throws RemoteException{
        this.nickname = nickname;
        this.receiver = receiver;
        this.remoteViewInterface = remoteViewInterface;
    }

    @Override
    public void sendQuestionEvent(QuestionEvent questionEvent) {

        try {
            remoteViewInterface.receiveQuestionEvent(questionEvent);
        }
        catch (RemoteException e){
            System.err.println("Closed connection " + nickname);
            e.printStackTrace();
            return;
            //receiver.receiveAnswer(new DisconnectedAnswer(nickname));
        }

    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void setReceiver(AnswerEventReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void receiveAnswerEvent(AnswerEvent event) throws RemoteException {

        receiver.receiveAnswer(event);

    }
}
