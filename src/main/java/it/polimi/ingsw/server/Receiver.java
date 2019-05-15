package it.polimi.ingsw.server;

import it.polimi.ingsw.MyLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

/**
 * This class runs on its own thread and handles receiving every message from the specific player
 */
public class Receiver implements Runnable {

    /**
     * The nickname of the owner of this stream
     */
    private String nickname;

    /**
     * The questioner is the class allowed to ask a question to this object
     */
    private Questioner questioner;

    /**
     * The stream coming FROM THE CLIENT
     */
    BufferedReader in;

    /**
     * The stream going TO THE CLIENT
     */
    PrintWriter out;

    /**
     * The constructor
     */
    Receiver(String nickname, Questioner questioner, BufferedReader in, PrintWriter out){
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

                if(line == null)
                    throw new IOException("Received null from client --> disconnecting");

                MyLogger.LOGGER.log(Level.INFO, "Receiver class received a new line");
                questioner.answer(nickname, line);
            }

        }
        catch (IOException e){
            questioner.lostConnection(nickname);
        }
    }

    /**
     * Sends the message through the output stream
     * @param message the message to send to the player
     */
    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }

    /**
     * Sets the nickname of the stream
     * Used when the player has been assigned to a new game
     * @param nickname the nickname of the player
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

}
