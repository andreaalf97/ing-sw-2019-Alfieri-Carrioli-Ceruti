package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.MapName;
import it.polimi.ingsw.view.server.VirtualView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ControllerTest {

    @Before
    public void setup(){

        ArrayList<String> playerNicknames = new ArrayList<>();
        playerNicknames.add("andreaalf");
        playerNicknames.add("gino");
        playerNicknames.add("meme");

        Game game = new Game(playerNicknames, MapName.FIRE, 5);
        //VirtualView virtualView = new VirtualView();

        //Controller controller = new Controller();

    }

    @Test
    public void update(){

        Player testPlayer = new Player("andreaalf");



    }

}