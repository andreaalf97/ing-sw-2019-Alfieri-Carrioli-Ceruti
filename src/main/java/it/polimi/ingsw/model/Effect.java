package it.polimi.ingsw.model;

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

    //SETS AND GETS
    /*------------------------------------------------------------------------------------------------------------------------------------------------------*/
    public int getnDamages() {
        return nDamages;
    }
    public void setnDamages(int nDamages) {
        this.nDamages = nDamages;
    }
    public int getnMarks() {
        return nMarks;
    }
    public void setnMarks(int nMarks) {
        this.nMarks = nMarks;
    }
    public int getnMovesOtherPlayer() {
        return nMovesOtherPlayer;
    }
    public void setnMovesOtherPlayer(int nMovesOtherPlayer) {
        this.nMovesOtherPlayer = nMovesOtherPlayer;
    }
    public int getnPlayerMarkable() {
        return nPlayerMarkable;
    }
    public void setnPlayerMarkable(int nPlayerMarable) {
        this.nPlayerMarkable = nPlayerMarable;
    }
    public int getnPlayerAttackable() {
        return nPlayerAttackable;
    }
    public void setnPlayerAttackable(int nPlayerAttackable) {
        this.nPlayerAttackable = nPlayerAttackable;
    }
    public int getnMarksOtherPlayer() {
        return nMarksOtherPlayer;
    }
    public void setnMarksOtherPlayer(int nMarksOtherPlayer) {
        this.nMarksOtherPlayer = nMarksOtherPlayer;
    }
    public boolean isMustShootOtherPlayers() {
        return mustShootOtherPlayers;
    }
    public void setMustShootOtherPlayers(boolean mustShootOtherPlayers) { this.mustShootOtherPlayers = mustShootOtherPlayers;
    }
    public boolean isCanShootAnyPlayer() {
        return canShootAnyPlayer;
    }
    public void setCanShootAnyPlayer(boolean canShootAnyPlayer) {
        this.canShootAnyPlayer = canShootAnyPlayer;
    }
    public ArrayList<Color> getCost() {
        return cost;
    }
    public void setCost(ArrayList<Color> cost) {
        this.cost = cost;
    }
    public int getnMoves() {
        return nMoves;
    }
    public void setnMoves(int nMoves) {
        this.nMoves = nMoves;
    }
    public int getMinDistance() {
        return minDistance;
    }
    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }
    public int getMaxDistance() {
        return maxDistance;
    }
    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }
    public boolean isMustBeOtherRoom() {
        return mustBeOtherRoom;
    }
    public void setMustBeOtherRoom(boolean mustBeOtherRoom) {
        this.mustBeOtherRoom = mustBeOtherRoom;
    }
    public boolean isMustBeDifferentSpots() {
        return mustBeDifferentSpots;
    }
    public void setMustBeDifferentSpots(boolean mustBeDifferentSpots) {
        this.mustBeDifferentSpots = mustBeDifferentSpots;
    }
    public boolean isLinear() {
        return isLinear;
    }
    public void setLinear(boolean linear) { isLinear = linear; }
    public Visibility getVisibleByWho() {
        return visibleByWho;
    }
    public void setVisibleByWho(Visibility visibleByWho) {
        this.visibleByWho = visibleByWho;
    }
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------*/
}