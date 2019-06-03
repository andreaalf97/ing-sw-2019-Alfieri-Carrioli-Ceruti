package it.polimi.ingsw.model.map;

public enum Room {
    RUBY("\u001b[31m"),
    SAPPHIRE("\u001b[34m"),
    EMERALD("\u001b[32m"),
    TOPAZ("\u001b[33m"),
    AMETHYST("\u001b[35m"),
    DIAMOND("\u001b[37m");

    private String escape;

    Room(String escape){
        this.escape = escape;
    }

    public String escape(){
        return escape;
    }
}
