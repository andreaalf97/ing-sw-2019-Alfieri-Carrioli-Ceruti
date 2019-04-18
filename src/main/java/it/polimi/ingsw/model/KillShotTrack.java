package it.polimi.ingsw.model;

import java.util.ArrayList;

public class KillShotTrack {

    /**
     * The list of players' kills.
     * The element is == "SKULL" by default
     */
    private ArrayList<String> skullList;

    /**
     * Every element is true only if the kill at the same index was an overkill
     */
    private ArrayList<Boolean> isOverkill;

    //SETS AND GETS
    /*---------------------------------------------------------------------------------------------------------*/
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
    /*----------------------------------------------------------------------------------------------------------*/

    /**
     * Constructs a new KST object where skullList.size() == nSkull == isOverkill.size() && isOverkill is false by default
     * @param nSkulls How many skulls to put on the KST
     */
    public KillShotTrack(int nSkulls){
        this.skullList = new ArrayList<>();
        for(int i = 0; i < nSkulls; i++){
            this.skullList.add("SKULL");
        }
        this.isOverkill = new ArrayList<>(this.skullList.size());
    }

    /**
     * Adds a kill to the KST
     * @param player The player who got the kill
     * @param isOverkill True if it was an overkill
     */
    public void addKill(String player, boolean isOverkill){return;}

    /**
     * Returns the names of the Players ordered by the amount of kills
     * @return An arrayList of players nicknames
     */
    public ArrayList<String> getRanking(){return new ArrayList<String>();}

    /**
     * Returns true when all skulls have been removed from the KST
     * @return true when there are no more skulls
     */
    public boolean noMoreSkulls(){
        if(!this.skullList.contains("SKULL")) //If the skullList does not contain any skull
            return true;
        return false;
    }
}
