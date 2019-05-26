package it.polimi.ingsw.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.MyLogger;

import java.util.ArrayList;
import java.util.logging.Level;

public class ServerQuestion {

    public final QuestionType questionType;

    public final ArrayList<String> possibleAnswers;

    public ServerQuestion(QuestionType questionType, ArrayList<String> possibleAnswers) {
        this.questionType = questionType;
        this.possibleAnswers = new ArrayList<>(possibleAnswers);
    }

    public ServerQuestion(String json){

        //FIXME

        QuestionType questionType = null;
        ArrayList<String> possibleAnswer = new ArrayList<>();

        try {
            JsonElement jsonElement = new JsonParser().parse(json);

            String questionTypeString = jsonElement.getAsJsonObject().get("QuestionType").getAsString();

            questionType = QuestionType.valueOf(questionTypeString);

            JsonArray array = jsonElement.getAsJsonObject().get("PossibleAnswers").getAsJsonArray();

            for(int i = 0; i < array.size(); i++)
                possibleAnswer.add(array.get(i).getAsString());

        }
        catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        this.questionType = questionType;
        this.possibleAnswers = possibleAnswer;

    }

    public String toJSON(){

        String json = "";

        json += "{";
        json += "\"" + "QuestionType" + "\"" + ": ";
        json += "\"" + questionType.toString() + "\", ";

        json += "\"" + "PossibleAnswers" + "\"" + ": [";

        int i;
        for(i = 0; i < possibleAnswers.size() - 1; i++)
            json += "\"" + possibleAnswers.get(i) + "\", ";

        json += "\"" + possibleAnswers.get(i) + "\"";

        json += "]}";


        return json;
    }


}
