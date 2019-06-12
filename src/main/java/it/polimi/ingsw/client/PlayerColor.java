package it.polimi.ingsw.client;

import java.util.ArrayList;

public enum PlayerColor {

    YELLOW ("/graphics/plance/yellow/Yellow_front.png", "\u001B[33m"),
    GREEN ("/graphics/plance/green/Green_front.png", "\u001b[32m"),
    GREY ("/graphics/plance/grey/Grey_front.png", "\u001b[37;1m"),
    BLUE ("/graphics/plance/blue/Blue_front.png", "\u001B[34m"),
    PURPLE ("/graphics/plance/purple/Purple_front.png", "\u001b[35m");

    private String path;

    private String escape;

    PlayerColor(String path, String escape){
        this.path = path;
        this.escape = escape;
    }

    public static ArrayList<PlayerColor> getRandomArray(int size){

        //TODO andreaalf
        //this returns an array of different colors with the given size

        PlayerColor[] allColorsTemp = PlayerColor.values();
        ArrayList<PlayerColor> allColors = new ArrayList<>();

        for(int i = 0; i < allColorsTemp.length && i < size; i++)
            allColors.add(allColorsTemp[i]);

        return allColors;
    }


    public String getPath() {
        return this.path;
    }

    public String escape(){return this.escape;}
}
