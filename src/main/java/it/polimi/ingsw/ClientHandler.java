package it.polimi.ingsw;

import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.map.MapName;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

public class ClientHandler implements Runnable {

    Server caller;
    Socket socket;

    public ClientHandler(Server caller, Socket socket){
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
            System.out.println("Asked for username");

            //Retrieve username
            String line = in.nextLine();

            System.out.println("Received answer:");
            System.out.println(line);

            /*
            while(Server.notAValidUsername(line)){
                out.println("Not a valid username:");
                out.flush();
                line = in.nextLine();
                System.out.println("Received answer " + line);
            }
            */



            //Stores the new username
            String username = line;

            //Asks for voted map
            out.println("Select map:");
            out.println("0 -- FIRE");
            out.println("1 -- EARTH");
            out.println("2 -- WIND");
            out.println("3 -- WATER");
            out.flush();

            System.out.println("Asked for map");


            int nextInt = in.nextInt();
            System.out.println("Received answer " + nextInt);

            while(nextInt < 0 || nextInt > 3){
                out.println("Not a valid vote");
                out.flush();
                nextInt = in.nextInt();
                System.out.println("Received answer " + nextInt);
            }

            //Stores the voted map
            MapName votedMap = MapName.values()[nextInt];

            out.println("Vote for skulls (5 to 8):");
            out.flush();
            System.out.println("Asked for nSkulls");
            nextInt = in.nextInt();
            System.out.println("Received answer " + nextInt);


            while(nextInt < 5 || nextInt > 8){
                out.println("Not a valid vote");
                out.flush();
                nextInt = in.nextInt();
                System.out.println("Received answer " + nextInt);
            }

            int votedSkulls = nextInt;

            Log.LOGGER.log(Level.INFO, "Adding " +  username + " to a waiting room");
            caller.addPlayer(socket, username, votedMap, votedSkulls);

        }
        catch (IOException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleSocket(Socket socket){

    }


}
