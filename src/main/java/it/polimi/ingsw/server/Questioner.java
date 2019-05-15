package it.polimi.ingsw.server;

public interface Questioner {

    void answer(String nickname, String answer);

    void lostConnection(String nickname);
}
