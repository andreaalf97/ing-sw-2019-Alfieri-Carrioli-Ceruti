package it.polimi.ingsw.view.server;

import com.google.gson.*;
import it.polimi.ingsw.JsonDeserializer;
import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.clientToServer.DisconnectedAnswer;
import it.polimi.ingsw.events.serverToClient.ModelUpdate;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



/*
    THE VIEW:
        - What the user sees (UI)
        - In Web Development consists of HTML & CSS, for us it's the CLI & the GUI
        - Communicates with the controller
        - Can be passed dynamic values from the controller
        - Template Engines
 */


public class VirtualView extends Observable implements Observer, AnswerEventReceiver {

    /**
     * Player nicknames
     */
    ArrayList<String> playersNicknames;

    /**
     * Locks
     */
    Object lock;

    /**
     * Used for ping
     */
    ArrayList<Boolean> stillConnected;

    /**
     * last snapshot received from model for every player
     */
    String[] lastClientSnapshot;

    /**
     * The list of all open connections
     */
    ArrayList<ServerProxy> serverProxies;

    final String CLIENTSNAPSHOTSPLITTER = "SNAPSPLITT";

    private Timer timer;

    /**
     * Only constructor
     * @param playersNicknames playersNicknames nicknames
     */
    public VirtualView(ArrayList<String> playersNicknames, ArrayList<ServerProxy> serverProxies){

        this.playersNicknames = new ArrayList<>(playersNicknames);
        this.lastClientSnapshot = new String[2];
        this.serverProxies = serverProxies;

        this.lock = new Object();

        this.stillConnected = new ArrayList<>();
        for(String p : playersNicknames)
            stillConnected.add(true);

        lastClientSnapshot[0] = "";
        lastClientSnapshot[1] = "";

        this.timer = new Timer();

        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }
        }, 0, 5000);

    }

    private void timerMethod(){

        synchronized (lock) {

            for (int i = 0; i < stillConnected.size(); i++) {

                if (!stillConnected.get(i)) {

                    String disconnectingNickname = playersNicknames.get(i);

                    System.out.println(disconnectingNickname + " DID NOT PING");

                    System.out.println("TIMER is disconnecing " + disconnectingNickname);


                    //Removes the player's proxy from the list
                    serverProxies.get(i).close();
                    serverProxies.remove(i);

                    stillConnected.remove(i);


                    //Removes this player from the nickname list
                    playersNicknames.remove(i);

                    notifyObservers(new DisconnectedAnswer(disconnectingNickname, true));

                    return;
                }

                stillConnected.set(i, false);
            }
        }
    }

    /**
     * Everytime that model changes, it sends all the information to the virtual view through a jsonString.
     * The virtual view will deserialize jsonString to see what is changed and will send changes to playersNicknames.
     * @param arg the new json snapshot
     */
    @Override
    public void notifyObserver(Object arg) {

        String clientSnapshot = (String) arg;
        String[] clientSnapshotSplitted = clientSnapshot.split(CLIENTSNAPSHOTSPLITTER);

        String invariableInfo = clientSnapshotSplitted[0];
        String customInfo = clientSnapshotSplitted[1];

        for(String s : playersNicknames){
            String customMessageForPlayer = elaborateJsonPlayersForClient(s, customInfo);

            //message contains the json to send to the client
            String message = invariableInfo + customMessageForPlayer ;

            notifyClient(s, message);
        }


    }

    private void notifyClient(String nickname, String message) {

        int index = playersNicknames.indexOf(nickname);

        serverProxies.get(index).sendQuestionEvent(
                new ModelUpdate(message)
        );

    }

    /**
     * this method elaborate the playersNicknames json, a player shouldn't see powerUps/weapons/
     * @param clientNickname the client nickname that i am processing
     * @param allClientSnapshot all the representation of the playersNicknames
     * @return the string with all the fields to hide of the other playersNicknames to the single client
     */
    private String elaborateJsonPlayersForClient(String clientNickname, String allClientSnapshot) {

        if(allClientSnapshot.equals(""))
            return "";

        String players = "{" + allClientSnapshot;  //"players : {all json players}"

        //here i have all the jsonObject that represents players
        JsonArray jsonplayers = JsonDeserializer.stringToJsonObject(players).get("players").getAsJsonArray();

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

    private String elaborateSingleJsonObjectForClient(JsonObject jsonPlayer, String clientNickname) {

        String jsonPlayerNickname =  jsonPlayer.get("nickname").getAsString();
        String jsonPlayers = "{\"nickname\":" + "\"" + jsonPlayerNickname + "\"" + "," + "\"nRedAmmo\":" + jsonPlayer.get("nRedAmmo").getAsString() + "," + "\"nBlueAmmo\":" + jsonPlayer.get("nBlueAmmo").getAsString() + "," + "\"nYellowAmmo\":" + jsonPlayer.get("nYellowAmmo").getAsString() + "," + "\"nDeaths\":" + jsonPlayer.get("nDeaths").getAsString() + "," + "\"xPosition\" :" + jsonPlayer.get("xPosition").getAsString() + "," + "\"yPosition\" :" + jsonPlayer.get("yPosition").getAsString() + "," + "\"isDead\" :" + jsonPlayer.get("isDead").getAsString() + "," ;


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
                if(jsonMarks.get(i) != null) {
                    jsonPlayers += jsonMarks.get(i).getAsString();
                    if (i != jsonMarks.size() - 1)
                        jsonPlayers += ",";
                }
            }
        }
        jsonPlayers += "]";
        //*******************************************************************************************************************
        //ADDING WEAPONLIST AND POWERUPLIST IF I AM SENDING THE SNAPSHOT TO ClientNickname

        if(jsonPlayerNickname.equals(clientNickname)) {
            jsonPlayers += ",";
            JsonArray jsonWeapons = jsonPlayer.get("weaponList").getAsJsonArray();
            jsonPlayers += "\"weaponList\": [";

            for (int i = 0; i < jsonWeapons.size(); i++) {
                JsonObject jsonWeapon = jsonWeapons.get(i).getAsJsonObject();

                String stringWeapon = jsonWeapon.toString();

                jsonPlayers += stringWeapon;

                if (i != jsonWeapons.size() - 1)
                    jsonPlayers += ",";
            }
            jsonPlayers += "],";


            JsonArray jsonPowerUps = jsonPlayer.get("powerUpList").getAsJsonArray();
            jsonPlayers += "\"powerUpList\": [";

            for (int i = 0; i < jsonPowerUps.size(); i++) {
                JsonObject jsonPowerUp = jsonPowerUps.get(i).getAsJsonObject();

                String stringPowerUp = jsonPowerUp.toString();

                jsonPlayers += stringPowerUp;

                if (i != jsonPowerUps.size() - 1)
                    jsonPlayers += ",";
            }
            jsonPlayers += "]";
        }


        jsonPlayers += "}";
        return jsonPlayers;
    }


    /**
     * Sends a question to the player
     * @param nickname the nickname of the player
     * @param questionEvent the question
     */
    public void sendQuestionEvent(String nickname, QuestionEvent questionEvent){

        int index = playersNicknames.indexOf(nickname);

        serverProxies.get(index).sendQuestionEvent(questionEvent);

    }

    public void sendAllQuestionEvent(QuestionEvent questionEvent){

        for(String p : playersNicknames)
            sendQuestionEvent(p, questionEvent);

    }

    @Override
    public void receiveAnswer(AnswerEvent answerEvent) {

        notifyObservers(answerEvent);

    }

    public void reconnectPlayer(String nickname, ServerProxy proxy) {

        playersNicknames.add(nickname);

        serverProxies.add(proxy);

        stillConnected.add(true);

    }

    public void ping(String nickname) {

        synchronized (lock) {
            int index = playersNicknames.indexOf(nickname);
            stillConnected.set(index, true);
        }

    }

    public void disconnectAllPlayers() {

        this.timer.cancel();

        for(int i = 0; i < playersNicknames.size(); i++) {
            serverProxies.get(i).close();
        }


    }
}
