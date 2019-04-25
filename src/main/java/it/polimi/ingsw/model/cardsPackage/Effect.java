package it.polimi.ingsw.model.cardsPackage;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.mapPackage.Visibility;

import java.util.ArrayList;

public class Effect {
    /**
     * Damages to give to the defender
     */
    public final int nDamages;

    /**
     * Marks to give to the defender
     */
    public final int nMarks;

    /**
     * Players receiving damages by this single effect
     */
    public final int nPlayerMarkable;       //can be 0 to 4

    /**
     * Players receiving mark by this single effect
     */
    public final int nPlayerAttackable;       //can be 0 to 4

    /**
     * Refers to the player the offender attacked in the last effect.
     * True if the offender must attack an other player
     */
    public final boolean mustShootOtherPlayers;

    /**
     * Refers to the player the offender attacked in the last effect.
     * True if the offender can attack the last defender and any other player
     */
    public final boolean canShootAnyPlayer;

    /**
     * The cost of this effect.
     * Null if cost == 0
     */
    public final ArrayList<Color> cost;

    /**
     * Moves the offender can do
     */
    public final int nMoves;

    /**
     * Moves the defender can do
     */
    public final int nMovesOtherPlayer;

    /**
     * Minimum distance between the offender and the defender
     */
    public final int minDistance;

    /**
     * Maximum distance between the offender and the defender
     * 100 is considered INF
     */
    public final int maxDistance;

    /**
     * True if the defender must be in a different room
     */
    public final boolean mustBeOtherRoom;

    /**
     * True if nPlayerAttackable > 1 && all the defenders must be in different spots
     */
    public final boolean mustBeDifferentSpots;

    /**
     * True if the effect is only applicable in one direction
     */
    public final boolean isLinear;

    /**
     * Defines who the defender must be visible by
     * Refer to the Visibility class for clarification
     */
    public final Visibility visibleByWho;

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
    protected Effect (JsonObject jsonEffect){
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

    /**
     * Creates a new effect by copying another one --> e.g. this is used in the method getEffect() {return new Effect(this.effect);} to avoid overwriting
     * @param effectToCopy the effect to copy
     */
    protected Effect(Effect effectToCopy){
        this.nDamages = effectToCopy.nDamages;
        this.nMarks = effectToCopy.nMarks;
        this.nPlayerAttackable = effectToCopy.nPlayerAttackable;
        this.nPlayerMarkable = effectToCopy.nPlayerMarkable;
        this.mustShootOtherPlayers = effectToCopy.mustShootOtherPlayers;
        this.canShootAnyPlayer = effectToCopy.canShootAnyPlayer;
        this.nMoves = effectToCopy.nMoves;
        this.minDistance = effectToCopy.minDistance;
        this.maxDistance = effectToCopy.maxDistance;
        this.nMovesOtherPlayer = effectToCopy.nMovesOtherPlayer;
        this.mustBeOtherRoom = effectToCopy.mustBeOtherRoom;
        this.mustBeDifferentSpots = effectToCopy.mustBeDifferentSpots;
        this.isLinear = effectToCopy.isLinear;
        this.visibleByWho = effectToCopy.visibleByWho;
        this.cost = effectToCopy.cost;

    }


}