package it.polimi.ingsw.server;

import it.polimi.ingsw.events.serverToClient.TemporaryIdQuestion;
import it.polimi.ingsw.view.client.RemoteViewInterface;
import it.polimi.ingsw.view.server.ServerProxy;
import it.polimi.ingsw.view.server.ServerProxyRmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class RmiServer extends UnicastRemoteObject implements ServerInterface {

    private Random rand;

    RmiServer() throws RemoteException{
        this.rand = new Random();
    }

    @Override
    public void connect(RemoteViewInterface remoteView) throws RemoteException {

        System.out.println("Accepted new RMI connection");


        //Generates a new temporary id until it's a valid one
        Integer temporaryId = rand.nextInt(100000);

        while ( ! Main.gamesHandler.isAValidTemporaryId(temporaryId)) {
            temporaryId = rand.nextInt(100000);
        }

        //Creates a new server proxy with this remote object
        ServerProxy proxy = new ServerProxyRmi(temporaryId.toString(), Main.gamesHandler, remoteView);

        //Adds the new proxy to those the gamesHandler is waiting a connection message from
        Main.gamesHandler.addTemporaryId(temporaryId, proxy);

        System.out.println("New RMI connection was given temporary ID " + temporaryId);

        //Sets the server on the client
        ServerProxyRmi remoteServer = (ServerProxyRmi) proxy;
        remoteView.setServer(remoteServer);

        //Sends a temporary id question to the user
        proxy.sendQuestionEvent(
                new TemporaryIdQuestion(temporaryId)
        );


    }
}
