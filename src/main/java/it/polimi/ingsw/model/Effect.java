package it.polimi.ingsw.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.MapPackage.Visibility;

import java.util.ArrayList;

public class Effect {
    /**
     * Damages to give to the defender
     */
    private int nDamages;

    /**
     * Marks to give to the defender
     */
    private int nMarks;

    /**
     * Players receiving damages by this single effect
     */
    private int nPlayerMarkable;       //can be 0 to 4

    /**
     * Players receiving mark by this single effect
     */
    private int nPlayerAttackable;       //can be 0 to 4

    /**
     * marks that i can give to other players
     */
    private int nMarksOtherPlayer;

    /**
     * Refers to the player the offender attacked in the last effect.
     * True if the offender must attack an other player
     */
    private boolean mustShootOtherPlayers;

    /**
     * Refers to the player the offender attacked in the last effect.
     * True if the offender can attack the last defender and any other player
     */
    private boolean canShootAnyPlayer;

    /**
     * The cost of this effect.
     * Null if cost == 0
     */
    private ArrayList<Color> cost;

    /**
     * Moves the offender can do
     */
    private int nMoves;

    /**
     * Moves the defender can do
     */
    private int nMovesOtherPlayer;

    /**
     * Minimum distance between the offender and the defender
     */
    private int minDistance;

    /**
     * Maximum distance between the offender and the defender
     * 100 is considered INF
     */
    private int maxDistance;

    /**
     * True if the defender must be in a different room
     */
    private boolean mustBeOtherRoom;

    /**
     * True if nPlayerAttackable > 1 && all the defenders must be in different spots
     */
    private boolean mustBeDifferentSpots;

    /**
     * True if the effect is only applicable in one direction
     */
    private boolean isLinear;

    /**
     * Defines who the defender must be visible by
     * Refer to the Visibility class for clarification
     */
    private Visibility visibleByWho;

    //NMOVES HAS TO BE CHECKED FOR FIRST (IF NMOVES != 0 IT HAS TO BE A MOVEMENT ONLY EFFECT SO WE DON'T HAVE TO CHECK THE OTHERS)
    // ALSO NMOVESOTHERPLAYER HAS TO BE CHECKED FOR SECOND (IF NMOVERSOTHERPLAYER != 0 IT HAS TO BE A MOVEMENT ONLY EFFECT)

    /*------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public int getnDamages() {
        return nDamages;
    }

    public int getnMarks() {
        return nMarks;
    }

    public int getnMovesOtherPlayer() {
        return nMovesOtherPlayer;
    }

    public int getnPlayerMarkable() {
        return nPlayerMarkable;
    }

    public int getnPlayerAttackable() {
        return nPlayerAttackable;
    }

    public int getnMarksOtherPlayer() {
        return nMarksOtherPlayer;
    }

    public boolean mustShootOtherPlayers() {
        return mustShootOtherPlayers;
    }

    public boolean canShootAnyPlayer() {
        return canShootAnyPlayer;
    }

    public ArrayList<Color> getCost() {
        return new ArrayList<>(cost);
    }

    public int getnMoves() {
        return nMoves;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public boolean mustBeOtherRoom() {
        return mustBeOtherRoom;
    }

    public boolean mustBeDifferentSpots() {
        return mustBeDifferentSpots;
    }

    public boolean isLinear() {
        return isLinear;
    }

    public Visibility getVisibleByWho() {
        return visibleByWho;
    }
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------*/

    /**
     * Creates an effect by reading the jsonObject
     * @param jsonEffect the jsonObject I want to read the effect from
     */
    public Effect (JsonObject jsonEffect){
        this.nDamages = jsonEffect.get("nDamages").getAsInt();
        this.nMarks = jsonEffect.get("nMarks").getAsInt();
        this.nPlayerAttackable = jsonEffect.get("nPlayersAttackable").getAsInt();
        this.nPlayerMarkable = jsonEffect.get("nPlayersMarkable").getAsInt();
        this.mustShootOtherPlayers = jsonEffect.get("mustShootOtherPlayers").getAsBoolean();
        this.canShootAnyPlayer = jsonEffect.get("canShootAnyPlayer").getAsBoolean();
        this.nMoves = jsonEffect.get("nMoves").getAsInt();
        this.minDistance = jsonEffect.get("minDistance").getAsInt();
        this.maxDistance = jsonEffect.get("maxDistance").getAsInt();
        this.nMovesOtherPlayer = jsonEffect.get("nMovesOtherPlayer").getAsInt();
        this.mustBeOtherRoom = jsonEffect.get("mustBeOtherRoom").getAsBoolean();
        this.mustBeDifferentSpots= jsonEffect.get("mustBeDifferentSpots").getAsBoolean();
        this.isLinear = jsonEffect.get("isLinear").getAsBoolean();
        this.visibleByWho = Visibility.values()[jsonEffect.get("visibleByWho").getAsInt()];

        //Creating the cost ArrayList
        this.cost = new ArrayList<>();
        JsonArray jsonCost = jsonEffect.get("cost").getAsJsonArray();
        for(int j = 0; j < jsonCost.size(); j++)
            this.cost.add( Color.valueOf(jsonCost.get(j).getAsString().toUpperCase()));

    }


}