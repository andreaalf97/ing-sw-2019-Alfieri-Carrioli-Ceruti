package it.polimi.ingsw.view.client;

import it.polimi.ingsw.model.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

public class StreamPrinter implements Runnable {

    BufferedReader input;

    PrintWriter output;

    protected StreamPrinter(BufferedReader input, PrintWriter output){
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {

        try {
            String line;
            while ((line = input.readLine()) != null) {
                output.println(line);
                output.flush();
            }

        }
        catch (IOException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            return;
        }

    }
}
