package it.polimi.ingsw.view.client;

import it.polimi.ingsw.client.UserInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteViewRmiImpl extends UnicastRemoteObject implements RemoteViewInterface {

    private UserInterface clientUserInterface;

    public RemoteViewRmiImpl(UserInterface clientUserInterface) throws RemoteException{
        this.clientUserInterface = clientUserInterface;
    }


    @Override
    public void notifyRemoteView(String message) throws RemoteException{

        clientUserInterface.notify(message);

    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println("[*] Server MESSAGE --> " + message);
    }

    @Override
    public int askQuestionAction(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionAction(possibleAnswers);
    }

    @Override
    public int askQuestionWhereToMove(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionWhereToMove(possibleAnswers);
    }

    @Override
    public int askQuestionWhereToMoveAndGrab(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionWhereToMoveAndGrab(possibleAnswers);
    }

    @Override
    public int askQuestionChoosePowerUpToRespawn(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionChoosePowerUpToRespawn(possibleAnswers);
    }

    @Override
    public int askQuestionChoosePowerUpToAttack(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionActionChoosePowerUpToAttack(possibleAnswers);
    }

    @Override
    public int askQuestionChooseWeaponToAttack(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionChooseWeaponToAttack(possibleAnswers);
    }

    @Override
    public int askQuestionChooseWeaponToSwitch(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionChooseWeaponToSwitch(possibleAnswers);
    }

    @Override
    public int askQuestionChooseWeaponToReload(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionChooseWeaponToReload(possibleAnswers);
    }

    @Override
    public int askQuestionPayWith(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionPayWith(possibleAnswers);
    }

    @Override
    public int askQuestionShoot(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionShoot(possibleAnswers);
    }

    @Override
    public int askQuestionUseTurnPowerUp(String[] possibleAnswers) throws RemoteException {
        return 0;

    }

    @Override
    public int askQuestionUseAsyncPowerUp(String[] possibleAnswers) throws RemoteException {
        return 0;
    }
}
