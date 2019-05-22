package it.polimi.ingsw.view.client;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.server.RmiReceiverServer;

import java.rmi.RemoteException;

public class RmiClientProxy extends Observable implements ClientProxy {

    RmiReceiverServer remoteObject;

    String nickname;

    @Override
    public void sendMessage(String message) {
        try {
            remoteObject.sendMessageToServer(nickname + ":" + message);
        }
        catch (RemoteException e){
            //TODO
            System.out.println("ERROR -- TODO");
        }
    }
}
