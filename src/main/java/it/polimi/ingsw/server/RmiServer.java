package it.polimi.ingsw.server;

import it.polimi.ingsw.view.client.RemoteView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends UnicastRemoteObject implements ServerInterface {

    RmiServer() throws RemoteException{
        super();
    }


    @Override
    public void connect(RemoteView remoteView, String connectionMessage) throws RemoteException {

        System.out.println("[*] Client -> " + connectionMessage);
        remoteView.notifyRemoteView("This message is coming from the server!");


    }
}
