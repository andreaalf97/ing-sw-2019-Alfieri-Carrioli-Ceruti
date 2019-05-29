package it.polimi.ingsw.view.server;

import com.google.gson.*;
import it.polimi.ingsw.MyLogger;
import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.view.ClientAnswer;
import it.polimi.ingsw.view.ServerQuestion;
import it.polimi.ingsw.view.client.RemoteViewInterface;

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

    final String CLIENTSNAPSHOTSPLITTER = "PORCODUE";


    /**
     * Only constructor
     * @param players players nicknames
     */
    public VirtualView(ArrayList<String> players, ArrayList<Socket> sockets, ArrayList<RemoteViewInterface> remoteViews){

        this.players = players;
        this.remoteViews = remoteViews;
        this.socketHandlers = new ArrayList<>();

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

        String clientSnapshot = (String) arg;
        String[] clientSnapshotSplitted = clientSnapshot.split(CLIENTSNAPSHOTSPLITTER);

        String invariableInfo = clientSnapshotSplitted[0];
        String customInfo = clientSnapshotSplitted[1];

        for(String s : players){
            String customMessageForPlayer = elaborateJsonPlayersForClient(s, customInfo);

            //message contains the json to send to the client
            String message = invariableInfo + customMessageForPlayer ;

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
         String root = "{" + allClientSnapshot;  //"players : {all json players}"

         //here i have all the jsonObject that represents players
         JsonArray jsonplayers = new JsonParser().parse(root).getAsJsonObject().get("players").getAsJsonArray();

         //in jsonPlayers i have the array of the information of the players


         String customMessage = "\"players\": [";

         //for every player in the list, the json will have a different construction based on the name of the player i am looking
         for(int i = 0; i < jsonplayers.size(); i++){
             customMessage += elaborateSingleJsonObjectForClient(jsonplayers.get(i).getAsJsonObject(), clientNickname);
             if(i != jsonplayers.size() - 1)
                 customMessage += ",";
         }

         customMessage += "]}";

         return customMessage;

    }

    /**
     * this method elaborates the single  player (JsonObject) in the players JsonArray
     * @param jsonPlayer the player to serialize
     * @param clientNickname the nickname to which i have ti send the json
     * @return an elaboration of the player to serialize
     */
    private String elaborateSingleJsonObjectForClient(JsonObject jsonPlayer, String clientNickname) {

        String jsonPlayers = "{\"nickname\":" + "\"" + jsonPlayer.get("nickname").getAsString() + "\"" + "," + "\"nRedAmmo\":" + jsonPlayer.get("nRedAmmo").getAsString() + "," + "\"nBlueAmmo\":" + jsonPlayer.get("nBlueAmmo").getAsString() + "," + "\"nYellowAmmo\":" + jsonPlayer.get("nYellowAmmo").getAsString() + "," + "\"nDeaths\":" + jsonPlayer.get("nDeaths").getAsString() + "," + "\"xPosition\" :" + jsonPlayer.get("xPosition").getAsString() + "," + "\"yPosition\" :" + jsonPlayer.get("yPosition").getAsString() + "," + "\"isDead\" :" + jsonPlayer.get("isDead").getAsString() + "," ;

        Gson gson = new Gson();

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
        jsonPlayers += "],";

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


        if (jsonPlayer.get("nickname").getAsString().equals(clientNickname)){
            jsonPlayers += ", \"nMoves\" :" + jsonPlayer.get("nMoves").getAsString() + "," + "\"nMovesBeforeGrabbing\" :" + jsonPlayer.get("nMovesBeforeGrabbing").getAsString() + "," + "\"nMovesBeforeShooting\":" + jsonPlayer.get("nMovesBeforeShooting").getAsString() + "," + "\"canReloadBeforeShooting\" :" + jsonPlayer.get("canReloadBeforeShooting").getAsString() + ",";

            jsonPlayers += "\"playerStatus\" :" + gson.toJson(jsonPlayer.get("playerStatus").getAsJsonObject())  + ","  + "\"weaponList\" :" + gson.toJson(jsonPlayer.get("weaponList").getAsJsonArray()) + "," + "\"powerUpList\" :" + gson.toJson(jsonPlayer.get("powerUpList").getAsJsonArray()) ;
        }

        jsonPlayers += "}";

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
                MyLogger.LOGGER.log(Level.INFO, nickname + " disconnected");
                lostConnection(nickname);
                return;
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

        int index = players.indexOf(nickname);

        remoteViews.remove(index);
        socketHandlers.remove(index);
        players.remove(index);

        notifyObserver("LOSTCONNECTION:" + nickname);

        sendAllMessage(nickname + " DISCONNECTED");

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
                MyLogger.LOGGER.log(Level.INFO, nickname + " disconnected");
                lostConnection(nickname);
                return;
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