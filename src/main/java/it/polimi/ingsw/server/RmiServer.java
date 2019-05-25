package it.polimi.ingsw.server;

import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.client.RemoteViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends UnicastRemoteObject implements ServerInterface {

    RmiServer() throws RemoteException{
        super();
    }


    @Override
    public void connect(RemoteViewInterface remoteView, String connectionMessage) throws RemoteException {

        System.out.println("[*] Client -> " + connectionMessage);
        remoteView.sendMessage("This message is coming from the server!");

        String username = connectionMessage.split(":")[0];
        MapName votedMap = MapName.valueOf(connectionMessage.split(":")[1]);
        int votedSkulls = Integer.parseInt(connectionMessage.split(":")[2]);

        Main.gamesHandler.newConnection(remoteView, username, votedMap, votedSkulls);
    }
}
