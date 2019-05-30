package it.polimi.ingsw.view.client;

import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.view.server.ServerProxyRmiInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteViewInterface extends Remote {

    void receiveQuestionEvent(QuestionEvent questionEvent) throws RemoteException;

    void setServer(ServerProxyRmiInterface server) throws RemoteException;

}
