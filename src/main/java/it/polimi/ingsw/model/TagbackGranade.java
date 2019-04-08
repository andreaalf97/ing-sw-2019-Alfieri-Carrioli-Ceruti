
package it.polimi.ingsw.model;

public class TagbackGranade implements Powerup{
    private Color color;

    public void giveMark(String player){                //this.player gives a mark to other player String (only if this.player sees string)

        Player p = new Player(player);

        p.giveMarks(player,1);

        return;
    }
}