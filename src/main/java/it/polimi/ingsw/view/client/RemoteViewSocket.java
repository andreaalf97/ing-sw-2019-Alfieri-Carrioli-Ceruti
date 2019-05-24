package it.polimi.ingsw.view.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RemoteViewSocket implements Runnable {

    Socket socket;

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
                System.out.println("[*socket] server said -> " + line);
                sendMessage(line.toUpperCase());
            }
        }
        catch (IOException e){
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
