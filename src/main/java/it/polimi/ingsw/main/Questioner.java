package it.polimi.ingsw.main;

public interface Questioner {

    void answer(String nickname, String answer);

    void lostConnection(String nickname);
}
