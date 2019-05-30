package it.polimi.ingsw.view.server;

import com.google.gson.*;
import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.events.QuestionEvent;

import java.util.ArrayList;



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
    ArrayList<String> playersNicknames;

    /**
     * last snapshot received from model for every player
     */
    String[] lastClientSnapshot;

    /**
     * The list of all open connections
     */
    ArrayList<ServerProxy> serverProxies;

    /**
     * Only constructor
     * @param playersNicknames playersNicknames nicknames
     */
    public VirtualView(ArrayList<String> playersNicknames, ArrayList<ServerProxy> serverProxies){

        this.playersNicknames = playersNicknames;
        this.lastClientSnapshot = new String[2];
        this.serverProxies = serverProxies;


        lastClientSnapshot[0] = "";
        lastClientSnapshot[1] = "";

    }

    /**
     * Everytime that model changes, it sends all the information to the virtual view through a jsonString.
     * The virtual view will deserialize jsonString to see what is changed and will send changes to playersNicknames.
     * @param arg the new json snapshot
     */
    @Override
    public void notifyObserver(Object arg) {

        //this is the casting from object to String[]  --> https://stackoverflow.com/questions/1611735/java-casting-object-to-array-type
        String[] clientSnapshot = (String[])((Object[])arg)[0];

        for(String s : playersNicknames){
            String customMessageForPlayer = elaborateJsonPlayersForClient(s, clientSnapshot[1]);

            String message = clientSnapshot[0] + customMessageForPlayer ;

        }


    }

    /**
     * this method elaborate the playersNicknames json, a player shouldn't see powerUps/weapons/
     * @param clientNickname the client nickname that i am processing
     * @param allClientSnapshot all the representation of the playersNicknames
     * @return the string with all the fields to hide of the other playersNicknames to the single client
     */
    private String elaborateJsonPlayersForClient(String clientNickname, String allClientSnapshot) {
         String players = "{" + allClientSnapshot;  //"playersNicknames : {all json playersNicknames}"

         //here i have all the jsonObject that represents playersNicknames
         JsonArray jsonplayers = new JsonParser().parse(players).getAsJsonObject().get("playersNicknames").getAsJsonArray();

         //in jsonPlayers i have the array of the information of the playersNicknames


         String customMessage = "\"playersNicknames\": [";

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

    private String[] arrayListToArray(ArrayList<String> possibleAnswers) {

        String[] tempString = new String[possibleAnswers.size()];

        for(int i = 0; i < possibleAnswers.size(); i++)
            tempString[i] = possibleAnswers.get(i);

        return tempString;

    }

}