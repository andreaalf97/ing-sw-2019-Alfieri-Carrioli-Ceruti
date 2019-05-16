package it.polimi.ingsw.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ClientAnswer {

    public final String sender;

    public final QuestionType questionType;

    public final ArrayList<String> possibleAnswers;

    public final int index;


    public ClientAnswer(String nickname, String json){

        JsonElement jsonElement = new JsonParser().parse(json);

        //Reading the question type from the json file
        this.questionType = QuestionType.valueOf(jsonElement.getAsJsonObject().get("QuestionType").getAsString());


        //Reading the list of possible answers from the json file
        JsonArray possibleAnswersJsonArray = jsonElement.getAsJsonObject().get("PossibleAnswers").getAsJsonArray();
        this.possibleAnswers = new ArrayList<>();

        for(int i = 0; i < possibleAnswersJsonArray.size(); i++)
            this.possibleAnswers.add(possibleAnswersJsonArray.get(i).getAsString());

        //Reading the chosen index from the list
        this.index = jsonElement.getAsJsonObject().get("Index").getAsInt();

        this.sender = nickname;
    }

}