package it.polimi.ingsw.view.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteViewInterface extends Remote {

    public void notifyRemoteView(String message) throws RemoteException;

    public void sendMessage(String message) throws RemoteException;

    String askQuestionAction(String[] possibleAnswers) throws RemoteException;

    String askQuestionWhereToMove(String[] possibleAnswers) throws RemoteException;

    String askQuestionWhereToMoveAndGrab(String[] possibleAnswers) throws RemoteException;

    String askQuestionChoosePowerUpToRespawn(String[] possibleAnswers) throws RemoteException;

    String askQuestionChoosePowerUpToDiscard(String[] possibleAnswers) throws RemoteException;

    String askQuestionChoosePowerUpToAttack(String[] possibleAnswers) throws RemoteException;

    String askQuestionChooseWeaponToAttack(String[] possibleAnswers) throws RemoteException;

    String askQuestionChooseWeaponToSwitch(String[] possibleAnswers) throws RemoteException;

    String askQuestionChooseWeaponToReload(String[] possibleAnswers) throws RemoteException;

    String askQuestionPayWith(String[] possibleAnswers) throws RemoteException;

    String askQuestionShoot(String[] possibleAnswers) throws RemoteException;
}
