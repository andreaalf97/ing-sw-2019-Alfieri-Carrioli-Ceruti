package it.polimi.ingsw.view;

import java.util.ArrayList;

public class ServerQuestion {

    public final QuestionType questionType;

    public final ArrayList<String> possibleAnswers;

    public ServerQuestion(QuestionType questionType, ArrayList<String> possibleAnswers) {
        this.questionType = questionType;
        this.possibleAnswers = new ArrayList<>(possibleAnswers);
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
