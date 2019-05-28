package it.polimi.ingsw.view.server;

import com.google.gson.*;
import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.client.Cli;
import it.polimi.ingsw.view.ClientAnswer;
import it.polimi.ingsw.view.QuestionType;
import it.polimi.ingsw.view.ServerQuestion;
import it.polimi.ingsw.view.client.RemoteViewInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;



/*
    THE VIEW:
        - What the user sees (UI)
        - In Web Development consists of HTML & CSS, for us it's the CLI & the GUI
        - Communicates with the controller
        - Can be passed dynamic values from the controller
        - Template Engines
 */


public class VirtualView extends Observable implements Observer {

    /**
     * Player nicknames
     */
    ArrayList<String> players;

    /**
     * last snapshot received from model for every player
     */
    String[] lastClientSnapshot;

    /**
     * The list of all open sockets
     */
    ArrayList<SocketHandler> socketHandlers;

    /**
     * The list of all remote view objects
     */
    ArrayList<RemoteViewInterface> remoteViews;

    /**
     * The splitter used to send socket messages to the client
     */
    final String SPLITTER = "$";


    /**
     * Only constructor
     * @param players players nicknames
     */
    public VirtualView(ArrayList<String> players, ArrayList<Socket> sockets, ArrayList<RemoteViewInterface> remoteViews){

        this.players = players;
        this.remoteViews = remoteViews;
        this.socketHandlers = new ArrayList<>();
        this.lastClientSnapshot = new String[2];
        lastClientSnapshot[0] = "";
        lastClientSnapshot[1] = "";

        //For every element in these arrays
        for(int i = 0; i < players.size(); i++){

            if(sockets.get(i) != null){     //If this player is using a socket connection

                //Creating a new socket handler for this player
                this.socketHandlers.add(new SocketHandler(
                        players.get(i),
                        sockets.get(i),
                        this
                ));

                //Launching the socket handler to be able to receive messages
                new Thread(
                        this.socketHandlers.get(i)
                ).start();

            }
            else {
                this.socketHandlers.add(null);
            }

        }

    }

    /**
     * Everytime that model changes, it sends all the information to the virtual view through a jsonString.
     * The virtual view will deserialize jsonString to see what is changed and will send changes to players.
     * @param arg the new json snapshot
     */
    @Override
    public void notifyObserver(Object arg) {

        //this is the casting from object to String[]  --> https://stackoverflow.com/questions/1611735/java-casting-object-to-array-type
        String[] clientSnapshot = (String[])((Object[])arg)[0];

        for(String s : players){
            String customMessageForPlayer = elaborateJsonPlayersForClient(s, clientSnapshot[1]);

            String message = clientSnapshot[0] + customMessageForPlayer ;

            notifyClient(s, message);
        }


    }

    /**
     * this method elaborate the players json, a player shouldn't see powerUps/weapons/
     * @param clientNickname the client nickname that i am processing
     * @param allClientSnapshot all the representation of the players
     * @return the string with all the fields to hide of the other players to the single client
     */
    private String elaborateJsonPlayersForClient(String clientNickname, String allClientSnapshot) {
         String players = "{" + allClientSnapshot;  //"players : {all json players}"

         //here i have all the jsonObject that represents players
         JsonArray jsonplayers = new JsonParser().parse(players).getAsJsonObject().get("players").getAsJsonArray();

         //in jsonPlayers i have the array of the information of the players


         String customMessage = "\"players\": [";

         //for every player in the list, the json will have a different construction based on the name of the player i am looking
         for(int i = 0; i < jsonplayers.size(); i++){
             customMessage += elaborateSingleJsonObjectForClient(jsonplayers.get(i).getAsJsonObject(), clientNickname);
         }

         customMessage += "]}";

         return customMessage;

    }

    private String elaborateSingleJsonObjectForClient(JsonObject jsonPlayer, String clientNickname) {

        String jsonPlayers = "{\"nickname\":" + jsonPlayer.get("nickname").getAsString() + "," + "\"nRedAmmo\":" + jsonPlayer.get("nRedAmmo").getAsString() + "," + "\"nBlueAmmo\":" + jsonPlayer.get("nBlueAmmo").getAsString() + "," + "\"nYellowAmmo\":" + jsonPlayer.get("nYellowAmmo").getAsString() + "," + "\"nDeaths\":" + jsonPlayer.get("nDeaths").getAsString() + "," + "\"xPosition\" :" + jsonPlayer.get("xPosition").getAsString() + "," + "\"yPosition\" :" + jsonPlayer.get("yPosition").getAsString() + "," + "\"isDead\" :" + jsonPlayer.get("isDead").getAsString() + "," ;


        //****************************************************************************************************
        //adding damages manually
        JsonArray jsonDamages = jsonPlayer.get("damages").getAsJsonArray();
        jsonPlayers += "\"damages\": [";

        if(jsonDamages != null) {
            for (int i = 0; i < jsonDamages.size(); i++) {
                jsonPlayers += jsonDamages.get(i).getAsString();
                if( i != jsonDamages.size() - 1)
                     jsonPlayers += ",";
            }
        }
        jsonPlayers += "]";

        //*************************************************************************************************
        //adding marks manually
        JsonArray jsonMarks = jsonPlayer.get("marks").getAsJsonArray();
        jsonPlayers += "\"marks\": [";

        if(jsonMarks != null) {
            for (int i = 0; i < jsonMarks.size(); i++) {
                jsonPlayers += jsonMarks.get(i).getAsString();
                if( i != jsonMarks.size() - 1)
                    jsonPlayers += ",";
            }
        }
        jsonPlayers += "]";

        //FIXME
        return jsonPlayers;
    }


    /**
     * Sends a question to the player
     * @param nickname the nickname of the player
     * @param serverQuestion the question
     */
    public void sendQuestion(String nickname, ServerQuestion serverQuestion){

        int i = players.indexOf(nickname);

        //If the player is running on socket
        if(remoteViews.get(i) == null){

            socketHandlers.get(i).send("QUESTION" + SPLITTER + serverQuestion.toJSON());

        }
        else {

            RemoteViewInterface remoteView = remoteViews.get(i);
            int answer = 0;
            String[] possibleAnswers = arrayListToArray(serverQuestion.possibleAnswers);


            try {
                switch (serverQuestion.questionType) {

                    case Action:
                        answer = remoteView.askQuestionAction(possibleAnswers);
                        break;

                    case WhereToMove:
                        answer = remoteView.askQuestionWhereToMove(possibleAnswers);
                        break;

                    case WhereToMoveAndGrab:
                        answer = remoteView.askQuestionWhereToMoveAndGrab(possibleAnswers);
                        break;

                    case ChoosePowerUpToRespawn:
                        answer = remoteView.askQuestionChoosePowerUpToRespawn(possibleAnswers);
                        break;

                    case UseTurnPowerUp:
                        answer = remoteView.askQuestionUseTurnPowerUp(possibleAnswers);
                        break;

                    case UseAsyncPowerUp:
                        answer = remoteView.askQuestionUseAsyncPowerUp(possibleAnswers);
                        break;

                    case ChoosePowerUpToUse:
                        answer = remoteView.askQuestionChoosePowerUpToAttack(possibleAnswers);
                        break;

                    case ChooseWeaponToAttack:
                        answer = remoteView.askQuestionChooseWeaponToAttack(possibleAnswers);
                        break;

                    case ChooseWeaponToSwitch:
                        answer = remoteView.askQuestionChooseWeaponToSwitch(possibleAnswers);
                        break;

                    case ChooseWeaponToReload:
                        answer = remoteView.askQuestionChooseWeaponToReload(possibleAnswers);
                        break;

                    case PayWith:
                        answer = remoteView.askQuestionPayWith(possibleAnswers);
                        break;

                    case Shoot:
                        answer = remoteView.askQuestionShoot(possibleAnswers);
                        break;

                    default:
                        throw new RuntimeException("Invalid question");
                }

                ClientAnswer clientAnswer = new ClientAnswer(nickname, serverQuestion, answer);
                notifyObservers(clientAnswer);
            }
            catch (RemoteException e){
                MyLogger.LOGGER.log(Level.SEVERE, "Error while sending question");
                e.printStackTrace();
            }


        }

    }

    private String[] arrayListToArray(ArrayList<String> possibleAnswers) {

        String[] tempString = new String[possibleAnswers.size()];

        for(int i = 0; i < possibleAnswers.size(); i++)
            tempString[i] = possibleAnswers.get(i);

        return tempString;

    }

    public void lostConnection(String nickname) {

        //TODO

    }

    public void sendMessage(String nickname, String message) {

        int index = players.indexOf(nickname);

        if(remoteViews.get(index) == null){

            socketHandlers.get(index).send("MESSAGE" + SPLITTER + message );

        }
        else {

            try {
                remoteViews.get(index).sendMessage(message);
            }
            catch (RemoteException e){
                MyLogger.LOGGER.log(Level.SEVERE, "Error while sending message");
            }

        }

    }

    public void sendAllMessage(String message) {

        for(String player : players)
            sendMessage(player, message);

    }

    protected void socketAnswer(ClientAnswer clientAnswer){

        notifyObservers(clientAnswer);

    }

    public void notifyClient(String nickname, String message){

        int index = players.indexOf(nickname);

        if(remoteViews.get(index) == null){

            socketHandlers.get(index).send("NOTIFY" + SPLITTER + message);

        }
        else {

            try {
                remoteViews.get(index).notifyRemoteView(message);
            }
            catch (RemoteException e){
                MyLogger.LOGGER.log(Level.SEVERE, "Error while sending message");
            }

        }

    }
}