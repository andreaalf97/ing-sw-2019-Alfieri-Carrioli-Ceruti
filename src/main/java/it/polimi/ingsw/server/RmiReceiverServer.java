package it.polimi.ingsw.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiReceiverServer extends Remote {

    void sendMessageToServer(String message) throws RemoteException;

    boolean newConnection(String json);

}
