package it.polimi.ingsw.main;

import it.polimi.ingsw.model.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

public class Receiver implements Runnable {

    String nickname;

    Questioner questioner;

    BufferedReader in;

    PrintWriter out;

    public Receiver(String nickname, Questioner questioner, BufferedReader in, PrintWriter out){
        this.nickname = nickname;
        this.questioner = questioner;
        this.in = in;
        this.out = out;
    }


    /**
     * Waits for new input and sends it to the controller
     */
    @Override
    public void run() {

        try {

            while (true) {
                String line = in.readLine();
                Log.LOGGER.log(Level.INFO, "Receiver class received a new line");
                questioner.answer(nickname, line);
            }

        }
        catch (IOException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            questioner.lostConnection(nickname);
        }
    }

    /**
     * Sends the message through the socket
     * @param message
     */
    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

}
