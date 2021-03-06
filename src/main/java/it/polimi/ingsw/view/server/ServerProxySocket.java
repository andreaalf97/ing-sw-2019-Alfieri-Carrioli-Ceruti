package it.polimi.ingsw.view.server;

import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.DisconnectedAnswer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class ServerProxySocket implements ServerProxy, Runnable {

    /**
     * The nickname of the user connected to this proxy
     */
    private String nickname;

    /**
     * The receiver of all answer events
     */
    private AnswerEventReceiver receiver;

    /**
     * The input stream
     */
    private ObjectInputStream in;

    /**
     * The output stream
     */
    private ObjectOutputStream out;

    /**
     * The only constructor
     * @param nickname the nickname of the player
     * @param receiver the receiver of the answer events
     * @param socket the open socket to extract the stream from
     */
    public ServerProxySocket(String nickname, AnswerEventReceiver receiver, Socket socket){

        this.nickname = nickname;
        this.receiver = receiver;

        ObjectInputStream tempIn = null;
        ObjectOutputStream tempOut = null;

        try {
            tempIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            tempOut = new ObjectOutputStream(socket.getOutputStream());

        }
        catch (IOException e){
            MyLogger.LOGGER.log(Level.SEVERE, "Error while creating a new ServerProxySocket object");
            e.printStackTrace();
        }
        catch (Exception e){
            System.out.println("OTHER EXCEPTION");
        }

        this.in = tempIn;
        this.out = tempOut;

    }

    /**
     * Sets the nickname of the owner of this proxy
     * This is used after receiving the connection message
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Sets the receiver of the answers
     * @param receiver
     */
    public void setReceiver(AnswerEventReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void close() {

        try {
            in.close();
            out.close();
            System.out.println("[E] Shutting down SOCKET proxy with " + nickname);
        }
        catch (IOException e){
            System.out.println("[E] Shutting down SOCKET proxy with " + nickname);
        }

        in = null;
        out = null;
    }

    /**
     * Sends a question through this socket
     * @param questionEvent
     */
    @Override
    public void sendQuestionEvent(QuestionEvent questionEvent) {

        try {
            out.writeObject(questionEvent);
            out.flush();
        } catch (IOException e) {
            System.err.println("SOCKET exception: disconnecting " + nickname);
            e.printStackTrace();
            //receiver.receiveAnswer(new DisconnectedAnswer(nickname, false));
        }

    }

    /**
     * Keeps accepting answers from the socket and sends them to the receiver
     */
    @Override
    public void run() {

        try {

            while (true) {

                AnswerEvent event = (AnswerEvent)in.readObject();

                receiver.receiveAnswer(event);

            }

        }
        catch (Exception e){
            System.err.println("SOCKET exception: disconnecting " + nickname);
            e.printStackTrace();
            //receiver.receiveAnswer(new DisconnectedAnswer(nickname, false));
        }

    }
}
