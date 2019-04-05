package it.polimi.ingsw.model;

import java.awt.*;

public class Newton implements Powerup {

    private Color color;


    public int[] movePlayer(Player playerToMove, int n_spot, Direction dir){         //choose player string on the board and move it a int <= 2 number of spots in one direction
                                                                                    //è meglio passare a questo metodo l'oggetto giocatore oppure la stringa cel nome del giocatore?
        int[] newPosition = new int[2];

        int x = playerToMove.getxPosition();
        int y = playerToMove.getyPosition();

        if ( dir == Direction.NORTH )
            newPosition[1] = y + n_spot;
        if ( dir == Direction.SOUTH )
            newPosition[1] = y - n_spot;
        if ( dir == Direction.WEST )
            newPosition[0] = x - n_spot;
        if ( dir == Direction.EAST )
            newPosition[0] = x + n_spot;

        return newPosition;
    }
}
