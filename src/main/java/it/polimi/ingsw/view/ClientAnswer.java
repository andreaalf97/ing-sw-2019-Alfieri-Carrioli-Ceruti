package it.polimi.ingsw.view;

import java.util.ArrayList;

public class ClientAnswer {

    public final String sender;

    public final QuestionType questionType;

    public final ArrayList<String> possibleAnswers;

    public final int index;

    public ClientAnswer(String sender, QuestionType questionType, ArrayList<String> possibleAnswers, int index){
        this.sender = sender;
        this.questionType = questionType;
        this.possibleAnswers = possibleAnswers;
        this.index = index;
    }

}