package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Log;

import java.io.IOException;
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

        try {
            socket = new Socket(ipAddress, port);
            Scanner stdin = new Scanner(System.in);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

            while (true){
                System.out.println(in.nextLine());
                String sending = in.nextLine();

                if(sending.toUpperCase().equals("QUIT"))
                    break;

                out.println(sending);
                out.flush();
            }

        }
        catch (IOException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException | NullPointerException e){
                Log.LOGGER.log(Level.SEVERE, e.getMessage());
            }
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
