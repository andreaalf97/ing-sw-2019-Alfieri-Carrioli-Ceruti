package it.polimi.ingsw.view.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.view.ClientAnswer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

public class SocketHandler implements Runnable{

    private final String nickname;

    private final Socket socket;

    private final VirtualView virtualView;

    SocketHandler(String nickname, Socket socket, VirtualView virtualView){
        this.socket = socket;
        this.nickname = nickname;
        this.virtualView = virtualView;
    }

    public void run(){

        Scanner scanner;

        try {
            scanner = new Scanner(socket.getInputStream());
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while creating a new scanner for the server SocketHandler");
            return;
        }


        try {

            while (true){

                String line = scanner.nextLine();

                ClientAnswer clientAnswer = new ClientAnswer(nickname, line);

                virtualView.socketAnswer(clientAnswer);

            }

        }
        catch (Exception e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while reading from socket handler");
            return;
        }
    }

    void send(String message){

        PrintWriter printWriter;

        try {
            printWriter = new PrintWriter(socket.getOutputStream());
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while creating a new print writer to send a message");
            return;
        }

        try {
            printWriter.println(message);
            printWriter.flush();
        }
        catch (Exception e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while sending message through socket");
        }

    }

}
