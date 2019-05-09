package it.polimi.ingsw.main;

import it.polimi.ingsw.model.map.MapName;

import java.io.IOException;
import java.util.NoSuchElementException;

/*
Per ora un nickname viene registrato quando il giocatore ha finito di votare, mentre viene aggiunto
a una waiting room.
Se due giocatori inseriscono lo stesso nickname contemporaneamente non ho attualmente modo
di scegliere tra uno dei due e dire all'altro di inserire un username valido

Edit: Il server fa un ulteriore controllo sincronizzato prima di aggiungere
 */

/**
 * This class handles any new connection by asking votes to the new player
 */
public class ClientVotesHandler implements Runnable {

    /**
     * It uses the receiver only through its IN and OUT attributes
     * Does not run a separate thread
     */
    private Receiver receiver;

    /**
     * The nickname of this player
     * This is null until we receive the first answer
     */
    private String nickname;

    /**
     * Constructor
     * @param receiver the holder of the streams
     */
    ClientVotesHandler(Receiver receiver){
        this.receiver = receiver;
        this.nickname = null;
    }

    /**
     * Receives the votes and closes this thread
     */
    @Override
    public void run(){

        try {

            //Asks for username
            receiver.out.println("Insert username:");
            receiver.out.flush();

            //Retrieve username
            String line = receiver.in.readLine();

            //Keeps asking if the nickname is wrong
            while(Server.notAValidUsername(line)){
                receiver.out.println("The given username is already logged in");
                receiver.out.flush();
                line = receiver.in.readLine();
            }


            //Stores the new username
            this.nickname = line;

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
                nextInt = Integer.parseInt(receiver.in.readLine());
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

            Server.addPlayerToWaitingRoom(receiver, this.nickname, votedMap, votedSkulls);

            receiver.out.println("You have been added to a waiting room");
            receiver.out.println("The timer is set to " + WaitingRoom.TIMERMINUTES + " minutes");
            receiver.out.flush();

        }
        catch (IOException | NoSuchElementException e){
            System.out.println("Disconnected while voting");
            Server.disconnected(this.nickname);
        }
    }


}
