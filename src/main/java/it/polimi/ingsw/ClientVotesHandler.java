package it.polimi.ingsw;

import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.map.MapName;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;

public class ClientVotesHandler implements Runnable {

    private Server caller;
    private Socket socket;

    ClientVotesHandler(Server caller, Socket socket){
        this.caller = caller;
        this.socket = socket;
    }

    @Override
    public void run(){

        try {

            //New input stream
            final Scanner in = new Scanner(socket.getInputStream());

            //New output stream
            final PrintWriter out = new PrintWriter(socket.getOutputStream());

            //Asks for username
            out.println("Insert username:");
            out.flush();

            //Retrieve username
            String line = in.nextLine();

            while(Server.notAValidUsername(line)){
                out.println("Not a valid username:");
                out.flush();
                line = in.nextLine();
            }


            //Stores the new username
            String username = line;

            //Asks for voted map
            out.println("Select map:");
            out.println("0 -- FIRE");
            out.println("1 -- EARTH");
            out.println("2 -- WIND");
            out.println("3 -- WATER");
            out.flush();

            int nextInt = in.nextInt();

            while(nextInt < 0 || nextInt > 3){
                out.println("Not a valid vote");
                out.flush();
                nextInt = in.nextInt();
            }

            //Stores the voted map
            MapName votedMap = MapName.values()[nextInt];

            out.println("Vote for skulls (5 to 8):");
            out.flush();
            Log.LOGGER.log(Level.INFO,"Asked for nSkulls");
            nextInt = in.nextInt();


            while(nextInt < 5 || nextInt > 8){
                out.println("Not a valid vote");
                out.flush();
                nextInt = in.nextInt();
            }

            int votedSkulls = nextInt;

            caller.addPlayerToWaitingRoom(socket, username, votedMap, votedSkulls);

            out.println("You have been added to a waiting room");
            out.println("The timer is set to " + WaitingRoom.TIMERMINUTES + " minutes");
            out.flush();

        }
        catch (IOException | NoSuchElementException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }


}
