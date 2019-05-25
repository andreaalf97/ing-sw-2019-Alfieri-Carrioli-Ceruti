package it.polimi.ingsw.view.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RemoteViewSocket implements Runnable {

    Socket socket;

    final String SPLITTER = "$";

    public RemoteViewSocket(Socket socket){
        this.socket = socket;
    }

    public String askQuestion(String question) {
        System.out.println("[*] Server question -> " + question);
        return question.toUpperCase();
    }

    public void notifyRemoteView(String message) {
        System.out.println("[*] Server message -> " + message);
    }

    @Override
    public void run() {

        try {
            Scanner scanner = new Scanner(socket.getInputStream());

            while (true){

                String line = scanner.nextLine();

                String messageType = line.split(SPLITTER)[0];

                String message = line.split(SPLITTER)[1];

                if(messageType == "MESSAGE") {
                    System.out.println("[*] Server MESSAGE: " + message);
                }
                else if(messageType == "QUESTION"){

                    System.out.println("[*] Server QUESTION: " + message);

                }
                else if(messageType == "NOTIFY") {
                    System.out.println("[*] Server NOTIFY: " + message);
                }
                else {
                    throw new RuntimeException("Not a valid message");
                }

            }
        }
        catch (Exception e){
            System.err.println("DISCONNECTED FROM SERVER");
        }

    }

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
