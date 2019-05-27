package it.polimi.ingsw.controller;

import it.polimi.ingsw.view.ServerQuestion;
import it.polimi.ingsw.view.server.VirtualView;

public class ParallelAsker implements Runnable {


    private VirtualView virtualView;

    private String nickname;

    private ServerQuestion serverQuestion;

    ParallelAsker(VirtualView virtualView, String nickname, ServerQuestion serverQuestion) {
        this.virtualView = virtualView;
        this.nickname = nickname;
        this.serverQuestion = serverQuestion;
    }

    @Override
    public void run() {

        virtualView.sendQuestion(nickname, serverQuestion);

    }
}
