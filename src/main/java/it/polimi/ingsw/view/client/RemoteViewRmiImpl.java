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
        System.out.println("[*] Server message --> " + message);
    }

    @Override
    public String askQuestionAction(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionAction(possibleAnswers);
    }

    @Override
    public String askQuestionWhereToMove(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionWhereToMove(possibleAnswers);
    }

    @Override
    public String askQuestionWhereToMoveAndGrab(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionWhereToMoveAndGrab(possibleAnswers);
    }

    @Override
    public String askQuestionChoosePowerUpToRespawn(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionChoosePowerUpToRespawn(possibleAnswers);
    }

    @Override
    public String askQuestionChoosePowerUpToDiscard(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionChoosePowerUpToDiscard(possibleAnswers);
    }

    @Override
    public String askQuestionChoosePowerUpToAttack(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionActionChoosePowerUpToAttack(possibleAnswers);
    }

    @Override
    public String askQuestionChooseWeaponToAttack(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionChooseWeaponToAttack(possibleAnswers);
    }

    @Override
    public String askQuestionChooseWeaponToSwitch(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionChooseWeaponToSwitch(possibleAnswers);
    }

    @Override
    public String askQuestionChooseWeaponToReload(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionChooseWeaponToReload(possibleAnswers);
    }

    @Override
    public String askQuestionPayWith(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionPayWith(possibleAnswers);
    }

    @Override
    public String askQuestionShoot(String[] possibleAnswers) throws RemoteException {
        return clientUserInterface.askQuestionShoot(possibleAnswers);
    }
}
