package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Map.Visibility;

public class Effect {
    private int nDamages;
    private int nMarks;
    private int nPlayerMarkable;       //can be 0 to 4
    private int nPlayerAttacable;       //can be 0 to 4
    private int nMarksOtherPlayer;
    private boolean mustShootOtherPlayers;
    private boolean canShootAnyPlayer;
    private int cost;
    private int nMoves;
    private int minDistance;
    private int maxDistance;    //100 = inf
    private boolean mustBeOtherRoom;
    private boolean mustBeDifferentSpots;
    private boolean isLinear;
    private Visibility visibleByWho;
    private int nMovesOtherPlayer;

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
    public int getnPlayerAttacable() {
        return nPlayerAttacable;
    }
    public void setnPlayerAttacable(int nPlayerAttacable) {
        this.nPlayerAttacable = nPlayerAttacable;
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
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
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