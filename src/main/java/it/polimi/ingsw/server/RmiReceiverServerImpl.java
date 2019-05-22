package it.polimi.ingsw.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiReceiverServerImpl extends UnicastRemoteObject implements RmiReceiverServer {

    ServerProxy serverProxy;

    protected RmiReceiverServerImpl(ServerProxy serverProxy) throws RemoteException{
        this.serverProxy = serverProxy;
    }

    @Override
    public void sendMessageToServer(String message) throws RemoteException {
        serverProxy.notifyObserver(message);
    }

    public boolean newConnection(String message){

        //This time the json looks like IPADDRESS:USERNAME:MAP:SKULLS

        String ipAddress = message.split(":")[0];
        String username = message.split(":")[1];
        String votedMap = message.split(":")[2];
        String votedSkulls = message.split(":")[3];

        if(Main.allConnectedUsernames.contains(username))
            return false;


        //TODO

        return true;

    }

}
