package it.polimi.ingsw.client;

import java.util.ArrayList;

public enum PlayerColor {

    YELLOW ("/Grafica/Plance_giocatori/Yellow/Yellow_front.png"),
    GREEN ("/Grafica/Plance_giocatori/Green/Green_front.png"),
    GREY ("/Grafica/Plance_giocatori/Grey/Grey_front.png"),
    BLUE ("/Grafica/Plance_giocatori/Blue/Blue_front.png"),
    PURPLE ("/Grafica/Plance_giocatori/Purple/Purple_front.png");

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
