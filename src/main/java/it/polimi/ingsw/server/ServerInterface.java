package it.polimi.ingsw.server;


import it.polimi.ingsw.view.client.RemoteViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    public void connect(RemoteViewInterface remoteView, String connectionMessage) throws RemoteException;

}
