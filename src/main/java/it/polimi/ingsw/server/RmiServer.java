package it.polimi.ingsw.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.client.RemoteViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

public class RmiServer extends UnicastRemoteObject implements ServerInterface {

    RmiServer() throws RemoteException{
        super();
    }

    /**
     * This method is called by the client through rmi to connect to the game
     * @param remoteView the remote object used to communicate
     * @param connectionMessage the first message: should look like USERNAME:VOTEDMAP:VOTEDSKULLS
     * @throws RemoteException when there is a communication error
     */
    @Override
    public void connect(RemoteViewInterface remoteView, String connectionMessage) throws RemoteException {

        //Extracts the correct information from the connection message
        String username = connectionMessage.split(":")[0];
        MapName votedMap = MapName.valueOf(connectionMessage.split(":")[1]);
        int votedSkulls = Integer.parseInt(connectionMessage.split(":")[2]);

        //handles the new rmi connecion
        Main.gamesHandler.newConnection(remoteView, username, votedMap, votedSkulls);
    }
}
