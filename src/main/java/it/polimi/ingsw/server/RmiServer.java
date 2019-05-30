package it.polimi.ingsw.server;

import it.polimi.ingsw.events.clientToServer.NewConnectionAnswer;
import it.polimi.ingsw.events.serverToClient.TemporaryIdQuestion;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.client.RemoteViewInterface;
import it.polimi.ingsw.view.server.AnswerEventReceiver;
import it.polimi.ingsw.view.server.ServerProxy;
import it.polimi.ingsw.view.server.ServerProxyRmi;
import it.polimi.ingsw.view.server.ServerProxyRmiInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class RmiServer extends UnicastRemoteObject implements ServerInterface {

    private Random rand;

    RmiServer(GamesHandler gamesHandler) throws RemoteException{
        this.rand = new Random();
    }

    @Override
    public void connect(RemoteViewInterface remoteView) throws RemoteException {

        //Creates a new server proxy with this remote object
        ServerProxy proxy = new ServerProxyRmi("", Main.gamesHandler, remoteView);

        //Generates a new temporary id until it's a valid one
        Integer temporaryId = rand.nextInt(100000);

        while ( ! Main.gamesHandler.isAValidTemporaryId(temporaryId))
            temporaryId = rand.nextInt(100000);

        //Adds the new proxy to those the gamesHandler is waiting a connection message from
        Main.gamesHandler.addTemporaryId(temporaryId, proxy);

        //Sets the server on the client
        ServerProxyRmi remoteServer = (ServerProxyRmi) proxy;
        remoteView.setServer(remoteServer);

        //Sends a temporary id question to the user
        proxy.sendQuestionEvent(
                new TemporaryIdQuestion(temporaryId)
        );


    }
}
