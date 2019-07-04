package it.polimi.ingsw.events.clientToServer;

import it.polimi.ingsw.controller.AnswerEventHandler;
import it.polimi.ingsw.events.AnswerEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseIfUseATargetingScopeAnswer implements AnswerEvent, Serializable {

    public String nickname;

    public String chosenWeapon;

    public Integer[] order;

    public boolean shootWithMovement;

    public int indexOfLastEffect;

    public ArrayList<String> defenders;

    public String defenderToApplyTargetingScope;

    public ChooseIfUseATargetingScopeAnswer(String nickname, String chosenWeapon, Integer[] order, boolean shootWithMovement, int indexOfLastEffect,ArrayList<String> defenders, String defenderChosen) {
        this.nickname = nickname;
        this.chosenWeapon = chosenWeapon;
        this.order = order;
        this.shootWithMovement = shootWithMovement;
        this.indexOfLastEffect = indexOfLastEffect;
        this.defenders = defenders;
        this.defenderToApplyTargetingScope = defenderChosen;
    }


    @Override
    public void acceptEventHandler(AnswerEventHandler handler) {
        handler.handleEvent(this);
    }
}
