package it.polimi.ingsw.events.serverToClient;

import it.polimi.ingsw.client.QuestionEventHandler;
import it.polimi.ingsw.events.QuestionEvent;

import java.io.Serializable;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;

public class ChooseHowToShootQuestion implements QuestionEvent, Serializable {

    public String nickname;

    public String chosenWeapon;

    public Integer[] order;

    public boolean shootWithMovement;

    public int indexOfLastEffect;

    public ArrayList<String> defenders;

    public ChooseHowToShootQuestion(String nickname, String chosenWeapon, Integer[] order, boolean shootWithMovement, int indexOfLastEffect,ArrayList<String> defenders) {
        this.nickname = nickname;
        this.chosenWeapon = chosenWeapon;
        this.order = order;
        this.shootWithMovement = shootWithMovement;
        this.indexOfLastEffect = indexOfLastEffect;
        this.defenders = defenders;
    }

    @Override
    public void acceptEventHandler(QuestionEventHandler handler) {
        handler.handleEvent(this);
    }
}
