package it.polimi.ingsw.server;


import it.polimi.ingsw.view.client.RemoteView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    public void connect(RemoteView remoteView, String connectionMessage) throws RemoteException;

}
