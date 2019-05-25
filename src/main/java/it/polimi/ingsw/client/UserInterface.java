package it.polimi.ingsw.client;


public interface UserInterface {

    void notify(String json);

    String askQuestionAction(String[] possibleAnswers);

    String askQuestionWhereToMove(String[] possibleAnswers);

    String askQuestionWhereToMoveAndGrab(String[] possibleAnswers);

    String askQuestionChoosePowerUpToRespawn(String[] possibleAnswers);

    String askQuestionChoosePowerUpToDiscard(String[] possibleAnswers);

    String askQuestionActionChoosePowerUpToAttack(String[] possibleAnswers);

    String askQuestionChooseWeaponToAttack(String[] possibleAnswers);

    String askQuestionChooseWeaponToSwitch(String[] possibleAnswers);

    String askQuestionChooseWeaponToReload(String[] possibleAnswers);

    String askQuestionPayWith(String[] possibleAnswers);

    String askQuestionShoot(String[] possibleAnswers);
}
