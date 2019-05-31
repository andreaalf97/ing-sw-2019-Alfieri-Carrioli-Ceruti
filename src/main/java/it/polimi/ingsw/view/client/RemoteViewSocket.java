package it.polimi.ingsw.view.client;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.serverToClient.DisconnectedQuestion;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class RemoteViewSocket implements Runnable, RemoteView {

    private final ObjectInputStream in;

    private final ObjectOutputStream out;

    /**
     * The interface used to talk with the user
     */
    private final QuestionEventHandler userInterface;


    /**
     * Constructor
     * @param socket the open socket with the server
     */
    public RemoteViewSocket(Socket socket, QuestionEventHandler userInterface){

        this.userInterface = userInterface;

        ObjectInputStream tempIn = null;
        ObjectOutputStream tempOut = null;

        try {
            tempOut = new ObjectOutputStream(socket.getOutputStream());
            tempIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        }
        catch (IOException e){
            System.err.println("Error while creating RemoteViewSocket");
        }

        this.in = tempIn;
        this.out = tempOut;
    }

    @Override
    public void run() {

        try {

            while (true) {

                QuestionEvent event = (QuestionEvent) in.readObject();

                userInterface.receiveEvent(event);
            }

        }
        catch (IOException | ClassNotFoundException e){
            System.err.println("Error while receiving new Question object through SOCKET");
            userInterface.receiveEvent(new DisconnectedQuestion());
        }

    }


    /**
     * Sends an answer event through the socket streams
     * @param event the answer event
     */
    public void sendAnswerEvent(AnswerEvent event){

        try {
            out.writeObject(event);
            out.flush();
        }
        catch (IOException e){
            System.err.println("Error while trying writeObject on client side");
            userInterface.receiveEvent(new DisconnectedQuestion());
        }

    }
}
