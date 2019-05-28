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

    private final PrintWriter printWriter;

    private final Scanner scanner;

    private final VirtualView virtualView;

    SocketHandler(String nickname, Socket socket, VirtualView virtualView){

        PrintWriter printWriter = null;
        Scanner scanner = null;

        try {
            printWriter = new PrintWriter(socket.getOutputStream());
            scanner = new Scanner(socket.getInputStream());
        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while creating the streams for the server SocketHandler");
        }
        finally {
            this.printWriter = printWriter;
            this.scanner = scanner;
            this.nickname = nickname;
            this.virtualView = virtualView;
        }


    }

    public void run(){

        try {

            while (true){

                String line = scanner.nextLine();

                ClientAnswer clientAnswer = new ClientAnswer(nickname, line);

                virtualView.socketAnswer(clientAnswer);

            }

        }
        catch (Exception e){

            virtualView.lostConnection(nickname);
            MyLogger.LOGGER.log(Level.INFO, "Lost connection with " + nickname);
            return;
        }
    }

    void send(String message){

        try {
            printWriter.println(message);
            printWriter.flush();
        }
        catch (Exception e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while sending message through socket");
        }

    }

    void ping(){

        //FIXME

        try {
            send("PING");

            String line = scanner.nextLine();

            if( ! line.equals("PONG")){
                throw new RuntimeException("I received a message which was not a PONG asnwer");
            }


        }
        catch (Exception e){

        }


    }

}
