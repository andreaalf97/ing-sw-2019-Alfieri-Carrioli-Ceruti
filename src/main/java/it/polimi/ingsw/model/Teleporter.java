package it.polimi.ingsw.model;

import java.awt.*;

public class Teleporter implements Powerup {

    private Color color;


    public int[] moveOwner (String player, int x, int y){           /*move this.player on any spot of the board. Mi aspetto che i parametri x e y siano le coordinate della posizione in cui player si vuole spostare*/

        int[] newPosition = new int[2];

        newPosition[0] = x;
        newPosition[1] = y;


        return newPosition;
    }

}
