package it.polimi.ingsw.view.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RemoteViewSocket implements Runnable {

    /**
     * The socket connected to the server
     */
    Socket socket;

    /**
     *
     */
    final String SPLITTER = "\\$";

    /**
     * Constructor
     * @param socket the open socket with the server
     */
    public RemoteViewSocket(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            //Creates a new scanner to read from the socket stream
            Scanner scanner = new Scanner(socket.getInputStream());

            while (true){

                //reads a new line
                String line = scanner.nextLine();

                //Divides the message into the message type and the actual message
                String messageType = line.split(SPLITTER)[0];
                String message = line.split(SPLITTER)[1];

                if(messageType.equals("MESSAGE")) {
                    System.out.println("[*] Server MESSAGE: " + message);
                }
                else if(messageType.equals("QUESTION")){

                    System.out.println("[*] Server QUESTION: " + message);

                }
                else if(messageType.equals("NOTIFY")) {
                    System.out.println("[*] Server NOTIFY: " + message);
                }
                else {
                    throw new RuntimeException("Not a valid message");
                }

            }
        }
        catch (IOException e){
            System.err.println("Error while creating the scanner");
            return;
        }
        catch (Exception e){
            System.err.println("DISCONNECTED FROM SERVER");
            e.printStackTrace();
        }

    }

    /**
     * Sends a message through the socket
     * @param message the message
     */
    public void sendMessage(String message){

        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(message);
            printWriter.flush();
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

    }
}
