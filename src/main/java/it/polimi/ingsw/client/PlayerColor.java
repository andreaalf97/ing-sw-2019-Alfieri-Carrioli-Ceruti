package it.polimi.ingsw.client;

import java.util.ArrayList;

public enum PlayerColor {

    YELLOW ("/graphics/plance/yellow/Yellow_front.png"),
    GREEN ("/graphics/plance/green/Green_front.png"),
    GREY ("/graphics/plance/grey/Grey_front.png"),
    BLUE ("/graphics/plance/blue/Blue_front.png"),
    PURPLE ("/graphics/plance/purple/Purple_front.png");

    private String path;

    PlayerColor(String path){
        this.path = path;
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
}
