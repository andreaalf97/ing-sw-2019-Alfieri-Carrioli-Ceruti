package it.polimi.ingsw.server;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;

import java.util.Map;

public class ServerProxy extends Observable implements Observer {

    RmiSenderServer rmiSenderServer;

    Map<String, Receiver> technology;

    @Override
    public void notifyObserver(Object obj) {

        //obj is the message from the player
        notifyObservers(obj);   //passing the message to the games handler

    }

    public void send(String nickname, String message){

        Receiver receiver = technology.get(nickname);

        if(receiver == null)    //This means this player is using the RMI technology
            rmiSenderServer.sendMessage(nickname, message);
        else
            receiver.sendMessage(message);
    }


    public void newConnection(String json) {



    }
}
