package it.polimi.ingsw.view.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteView extends Remote {

    public String askQuestion(String question) throws RemoteException;

    public void notifyRemoteView(String message) throws RemoteException;

}
