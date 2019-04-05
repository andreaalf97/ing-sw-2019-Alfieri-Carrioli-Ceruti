package it.polimi.ingsw.model;

import java.util.ArrayList;

public class KillShotTrack {
    private ArrayList<String> skullList;
    private ArrayList<Boolean> isOverkill;

    public ArrayList<String> getSkullList() {
        return skullList;
    }

    public void setSkullList(ArrayList<String> skullList) {
        this.skullList = skullList;
    }

    public ArrayList<Boolean> getIsOverkill() {
        return isOverkill;
    }

    public void setIsOverkill(ArrayList<Boolean> isOverkill) {
        this.isOverkill = isOverkill;
    }

    //###############################################################

    public void addKill(String player, boolean isOverkill){return;}

    public ArrayList<String> getRanking(){return new ArrayList<String>();}
}
