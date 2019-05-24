package it.polimi.ingsw.view.client;

import it.polimi.ingsw.client.Interface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteViewRmi extends UnicastRemoteObject implements RemoteView {

    private Interface clientInterface;

    public RemoteViewRmi(Interface clientInterface) throws RemoteException{
        this.clientInterface = clientInterface;
    }

    @Override
    public String askQuestion(String question) throws RemoteException{

        return clientInterface.askQuestion(question);


    }

    @Override
    public void notifyRemoteView(String message) throws RemoteException{

        clientInterface.notify(message);


    }
}
