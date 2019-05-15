package it.polimi.ingsw.view.client;

import it.polimi.ingsw.MyLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

public class ClientView extends Observable implements Runnable, Observer {

    String ipAddress;

    int port;

    Socket serverSocket;

    public ClientView(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
        serverSocket = null;
    }

    @Override
    public void run() {
        //TODO

        BufferedReader stdin = null;
        BufferedReader socketin = null;
        PrintWriter socketout = null;
        PrintWriter stdout = null;

        //Creates all the readers
        try {
            serverSocket = new Socket(ipAddress, port);
            stdin = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            socketin = new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream())
            );
            socketout = new PrintWriter(serverSocket.getOutputStream());
            stdout = new PrintWriter(System.out);

        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

        StreamPrinter stdinToSocket = new StreamPrinter(stdin, socketout);
        StreamPrinter socketToStdout = new StreamPrinter(socketin, stdout);

        Thread t1 = new Thread(stdinToSocket);
        Thread t2 = new Thread(socketToStdout);

        t1.start();
        t2.start();

        /*
        try {
            serverSocket.close();
        }
        catch (IOException | NullPointerException e){
            MyLogger.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        */

    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO
    }

    public static void main(String args[]){

        ClientView clientView = new ClientView("127.0.0.1", 2345);
        Thread t = new Thread(clientView);
        t.start();

    }
}
