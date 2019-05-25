package it.polimi.ingsw.client;

/**
 * The interface used to communicate between the real user and the network
 */
public interface UserInterface {

    /**
     * Notifies changes in the model
     * @param json a JSON file containing only the model changes
     */
    void notify(String json);

    /**
     * Asks the player which action he wants to make
     * @param possibleAnswers all the possible actions
     * @return the index of the answer
     */
    int askQuestionAction(String[] possibleAnswers);

    /**
     * Asks the player where he wants to move
     * @param possibleAnswers the possible spots
     * @return
     */
    int askQuestionWhereToMove(String[] possibleAnswers);

    int askQuestionWhereToMoveAndGrab(String[] possibleAnswers);

    int askQuestionChoosePowerUpToRespawn(String[] possibleAnswers);

    int askQuestionChoosePowerUpToDiscard(String[] possibleAnswers);

    int askQuestionActionChoosePowerUpToAttack(String[] possibleAnswers);

    int askQuestionChooseWeaponToAttack(String[] possibleAnswers);

    int askQuestionChooseWeaponToSwitch(String[] possibleAnswers);

    int askQuestionChooseWeaponToReload(String[] possibleAnswers);

    int askQuestionPayWith(String[] possibleAnswers);

    int askQuestionShoot(String[] possibleAnswers);
}
