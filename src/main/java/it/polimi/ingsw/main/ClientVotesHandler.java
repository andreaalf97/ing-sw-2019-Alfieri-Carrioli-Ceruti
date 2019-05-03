package it.polimi.ingsw.main;

import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.client.StreamPrinter;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Level;

/**
 * This class handles any new connection by asking votes to the new player
 */
public class ClientVotesHandler implements Runnable {

    private Server caller;
    private Receiver receiver;

    ClientVotesHandler(Server caller, Receiver receiver){
        this.caller = caller;
        this.receiver = receiver;
    }

    @Override
    public void run(){

        try {

            //Asks for username
            receiver.out.println("Insert username:");
            receiver.out.flush();

            //Retrieve username
            String line = receiver.in.readLine();

            while(Server.notAValidUsername(line)){
                receiver.out.println("Not a valid username:");
                receiver.out.flush();
                line = receiver.in.readLine();
            }


            //Stores the new username
            String username = line;

            //Asks for voted map
            receiver.out.println("Select map:");
            receiver.out.println("0 -- FIRE");
            receiver.out.println("1 -- EARTH");
            receiver.out.println("2 -- WIND");
            receiver.out.println("3 -- WATER");
            receiver.out.flush();

            int nextInt = Integer.parseInt(receiver.in.readLine());

            while(nextInt < 0 || nextInt > 3){
                receiver.out.println("Not a valid vote");
                receiver.out.flush();
                nextInt = nextInt = Integer.parseInt(receiver.in.readLine());
            }

            //Stores the voted map
            MapName votedMap = MapName.values()[nextInt];

            receiver.out.println("Vote for skulls (5 to 8):");
            receiver.out.flush();
            nextInt = Integer.parseInt(receiver.in.readLine());


            while(nextInt < 5 || nextInt > 8){
                receiver.out.println("Not a valid vote");
                receiver.out.flush();
                nextInt = Integer.parseInt(receiver.in.readLine());
            }

            int votedSkulls = nextInt;

            caller.addPlayerToWaitingRoom(receiver, username, votedMap, votedSkulls);

            receiver.out.println("You have been added to a waiting room");
            receiver.out.println("The timer is set to " + WaitingRoom.TIMERMINUTES + " minutes");
            receiver.out.flush();

        }
        catch (IOException | NoSuchElementException e){
            Log.LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }


}
