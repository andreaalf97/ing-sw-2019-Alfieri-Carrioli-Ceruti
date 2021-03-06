package it.polimi.ingsw.server;


import it.polimi.ingsw.view.client.RemoteViewInterface;
import it.polimi.ingsw.view.server.ServerProxyRmiInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    void connect(RemoteViewInterface remoteView) throws RemoteException;

}
