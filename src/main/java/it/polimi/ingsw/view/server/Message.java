package it.polimi.ingsw.view.server;

import java.util.ArrayList;

public class Message {

    private String sender;

    private ArrayList<String> defenders;

    private int answerIndex;

    public Message(String sender, String message) throws IllegalArgumentException{

        String[] words = message.split(" ");

        for(String i : words)
            System.out.println(i);

        this.sender = sender;

        defenders = new ArrayList<>();
        defenders.add("defender1");
        defenders.add("defender2");

        answerIndex = 0;
    }

    public String getSender(){
        return this.sender;
    }

    public ArrayList<String> getDefenders(){
        return new ArrayList<>(defenders);
    }

    public int getAnswerIndex(){
        return this.answerIndex;
    }

}