package it.polimi.ingsw.view.server;

import it.polimi.ingsw.events.AnswerEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerProxyRmiInterface extends Remote {

    void receiveAnswerEvent(AnswerEvent event) throws RemoteException;

}
