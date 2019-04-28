package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;

public class ClientView extends Observable implements Runnable, Observer {

    String ipAddress;

    int port;

    public ClientView(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void run() {
        //TODO

        Socket socket = null;
        Scanner stdin = null;
        BufferedReader socketin = null;
        PrintWriter socketout = null;

        //Creates all the readers
        try {
            socket = new Socket(ipAddress, port);
            stdin = new Scanner(System.in);
            socketout = new PrintWriter(socket.getOutputStream());
            socketin = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
        }
        catch (IOException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }


        try {
            while (true) {
                System.out.println(socketin.readLine());
                String sending = stdin.nextLine();

                if (sending.toUpperCase().equals("QUIT"))
                    break;

                socketout.println(sending);
                socketout.flush();
            }
        }
        catch (IOException | NullPointerException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }


        try {
            socket.close();
        }
        catch (IOException | NullPointerException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

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
