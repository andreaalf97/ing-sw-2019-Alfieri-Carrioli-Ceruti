package it.polimi.ingsw.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public ArrayList<Boolean> getIsOverkill() {
        return isOverkill;
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

        this.isOverkill = new ArrayList<>();

        for(int i = 0; i < nSkulls; i++) {
            this.isOverkill.add(false);
        }
    }

    /**
     * this method creatre a jst by deserializing it from json
     * @param jsonKST the kst in json
     */
    public KillShotTrack(JsonObject jsonKST){
        this.skullList = new ArrayList<>();
        JsonArray jsonSkullList = jsonKST.get("skullList").getAsJsonArray();
        for(int i = 0; i < jsonSkullList.size(); i++){
            String s = jsonSkullList.get(i).getAsString();
            this.skullList.add(s);
        }

        this.isOverkill = new ArrayList<>();
        JsonArray jsonIsOverkill = jsonKST.get("isOverkill").getAsJsonArray();
        for(int i = 0; i < jsonIsOverkill.size(); i++){
            Boolean b = jsonIsOverkill.get(i).getAsBoolean();
            this.isOverkill.add(b);
        }
    }

    /**
     * Cnstructor only used in tests
     */
    public KillShotTrack(ArrayList<String> skullList){
        this(skullList, new ArrayList<Boolean>(skullList.size()));
    }

    /**
     * Cnstructor only used in tests
     */
    private KillShotTrack(ArrayList<String> skullList, ArrayList<Boolean> isOverkill){
        this.skullList = skullList;
        this.isOverkill = isOverkill;
    }

    @Override
    public KillShotTrack clone(){
        return new KillShotTrack(new ArrayList<>(this.skullList), new ArrayList<>(this.isOverkill));
    }

    //TESTED
    /**
     * Adds a kill to the KST
     * @param player The player who got the kill
     * @param isOverkill True if it was an overkill
     */
    //TODO what if it's a multiple kill??
    public void addKill(String player, boolean isOverkill)
    {
        for(int i = 0; i < this.skullList.size(); i++){     //TODO what are you doing here? what if it's the last skull? the frenzy turn begins!
            if( skullList.get(i).equals("SKULL") ){
                this.skullList.set(i, player);
                this.isOverkill.set(i, isOverkill);
                return;
            }
        }
    }

    //TESTED
    /**
     * Returns the names of the Players ordered by the amount of kills
     * @return An arrayList of players nicknames
     */
    public ArrayList<String> getRanking(){
        //HashMap used to count how many times each player is
        Map recurrences = new HashMap<String, Integer>();

        //Counting how many kills each player has
        for(int i = 0; i < this.skullList.size(); i++){ //For every kill in the KST

            String player = this.skullList.get(i);
            if(!player.equals("SKULL") && !recurrences.containsKey(player)){ //If it is the first time I find this player in the List
                if(this.isOverkill.get(i))
                    recurrences.put(player, 2); //IF it was an overkill --> 2 kills
                else
                    recurrences.put(player, 1); //IF it was not an overkill --> 1 kill
            }
            else if(!player.equals("SKULL") && recurrences.containsKey(player)){
                int tempRec = (int)recurrences.get(player);
                if(this.isOverkill.get(i))
                    recurrences.put(player, tempRec + 2); //IF it was an overkill
                else
                    recurrences.put(player, tempRec + 1); //IF it was not an overkill
            }

            //IF player == "SKULL" nothing happens ==> This should never happen!! I shouldn't have any skulls when calling this method

        }

        //Ordering the HashMap into an Array in descend order
        ArrayList<String> ranking = new ArrayList<>(); //empty array
        while(!recurrences.isEmpty()){ //I am removing the biggest element every time, so I continue until the HashMap is empty

            int maxValue = 0; //If a value is inside the recurrences array it can't be smaller than 1
            String maxPlayer = "ERROR"; //This should never be inserted into the ranking array

            //Looking for the current biggest element
            for(String player : (Set<String>)recurrences.keySet()){
                if((int)recurrences.get(player) > maxValue){
                    maxValue = (int)recurrences.get(player);
                    maxPlayer = player;
                }
            }

            ranking.add(maxPlayer); //Adding the current biggest element to the ranking array
            recurrences.remove(maxPlayer); //Removing the biggest element from the HashMap so I can find the next biggest element
        }

        return ranking;
    }

    //TESTED
    /**
     * Returns true when all skulls have been removed from the KST
     * @return true when there are no more skulls
     */
    public boolean noMoreSkulls() throws RuntimeException{

        boolean foundSkull = false;

        //for every string in skullList
        for(String i : skullList){
            if(i.equals("SKULL")) //if this is a skull there should not be any player afterward
                foundSkull = true;
            else
                if(foundSkull)
                    throw new RuntimeException("There should not be any players after a skull");
        }

        return (!this.skullList.contains("SKULL")); //If the skullList contains at least one skull
    }

    public int getSize() {
        return this.skullList.size();
    }
}
