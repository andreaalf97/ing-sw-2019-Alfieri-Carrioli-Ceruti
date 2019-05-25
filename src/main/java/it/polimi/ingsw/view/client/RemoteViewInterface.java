package it.polimi.ingsw.view.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteViewInterface extends Remote {

    void notifyRemoteView(String message) throws RemoteException;

    void sendMessage(String message) throws RemoteException;

    int askQuestionAction(String[] possibleAnswers) throws RemoteException;

    int askQuestionWhereToMove(String[] possibleAnswers) throws RemoteException;

    int askQuestionWhereToMoveAndGrab(String[] possibleAnswers) throws RemoteException;

    int askQuestionChoosePowerUpToRespawn(String[] possibleAnswers) throws RemoteException;

    int askQuestionChoosePowerUpToAttack(String[] possibleAnswers) throws RemoteException;

    int askQuestionChooseWeaponToAttack(String[] possibleAnswers) throws RemoteException;

    int askQuestionChooseWeaponToSwitch(String[] possibleAnswers) throws RemoteException;

    int askQuestionChooseWeaponToReload(String[] possibleAnswers) throws RemoteException;

    int askQuestionPayWith(String[] possibleAnswers) throws RemoteException;

    int askQuestionShoot(String[] possibleAnswers) throws RemoteException;

    int askQuestionUseTurnPowerUp(String[] possibleAnswers) throws  RemoteException;

    int askQuestionUseAsyncPowerUp(String[] possibleAnswers) throws  RemoteException;
}
