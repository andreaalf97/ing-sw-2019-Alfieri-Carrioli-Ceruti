package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Color;

import java.util.ArrayList;

public class AmmoCard {

    public ArrayList<Color> colors;
    public boolean hasPowerUp;
    public String ammoCardImagePath;

    public AmmoCard ( String ammoCardImagePath, ArrayList<Color> colors, boolean hasPowerUp){
        this.ammoCardImagePath = ammoCardImagePath;
        this.colors = colors;
        this.hasPowerUp = hasPowerUp;
    }
}
