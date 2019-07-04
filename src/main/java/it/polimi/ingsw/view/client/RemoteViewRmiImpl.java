package it.polimi.ingsw.view.client;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.serverToClient.DisconnectedQuestion;
import it.polimi.ingsw.view.server.ServerProxyRmiInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteViewRmiImpl extends UnicastRemoteObject implements RemoteView, RemoteViewInterface {

    private QuestionEventHandler clientUserInterface;

    private ServerProxyRmiInterface server;

    public RemoteViewRmiImpl(QuestionEventHandler clientUserInterface) throws RemoteException{
        this.clientUserInterface = clientUserInterface;
        this.server = null;
    }

    public void setServer(ServerProxyRmiInterface server) throws RemoteException{
        this.server = server;
    }

    @Override
    public void receiveQuestionEvent(QuestionEvent questionEvent) throws RemoteException {

        clientUserInterface.receiveEvent(questionEvent);

    }

    @Override
    public void sendAnswerEvent(AnswerEvent event) {

        try {
            server.receiveAnswerEvent(event);
        } catch (RemoteException e) {
            System.err.println("Error while sending answer to the server");
            e.printStackTrace();
            clientUserInterface.receiveEvent(new DisconnectedQuestion());
        }


    }
}
