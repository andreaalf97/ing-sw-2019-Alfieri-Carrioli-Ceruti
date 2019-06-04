package it.polimi.ingsw.client;

import java.util.ArrayList;

public enum PlayerColor {

    YELLOW,
    GREEN,
    GREY,
    BLUE,
    PURPLE;

    public static ArrayList<PlayerColor> getRandomArray(int size){

        //TODO andreaalf
        //this returns an array of different colors with the given size

        PlayerColor[] allColorsTemp = PlayerColor.values();
        ArrayList<PlayerColor> allColors = new ArrayList<>();

        for(int i = 0; i < allColorsTemp.length && i < size; i++)
            allColors.add(allColorsTemp[i]);

        return new ArrayList<>();
    }


}
