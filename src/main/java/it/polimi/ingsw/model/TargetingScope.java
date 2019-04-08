package it.polimi.ingsw.model;

public class TargetingScope{


    private Color color;


    public void giveOneDamage(String playerToHit, Color color){          //this.player gives to player playerToHit 1 damage (this.player has to pay 1 of any color)

        Player player = new Player(playerToHit);

        player.giveDamage(playerToHit,1);

        return;
    }

    public void giveDamage(String playerToHit, Powerup powerup){

        Player player = new Player(playerToHit);

        player.giveDamage(playerToHit,1);

        return;
    }

}
