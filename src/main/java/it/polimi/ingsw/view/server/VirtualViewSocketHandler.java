package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;

public class VirtualViewSocketHandler implements Runnable {

    private BufferedReader reader;
    private VirtualView caller;

    public VirtualViewSocketHandler(Socket socket){

        try {
            this.reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

        }
        catch (IOException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void run() {

        try {

            while (true) {
                String line = reader.readLine();
                caller.notify(line);
            }
        }
        catch (IOException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
}
