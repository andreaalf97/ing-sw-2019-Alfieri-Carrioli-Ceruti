package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.clientToServer.*;
import it.polimi.ingsw.events.serverToClient.ChooseHowToPayToReloadBeforeAttackQuestion;
import it.polimi.ingsw.events.serverToClient.SendCanMoveBeforeShootingQuestion;
import it.polimi.ingsw.events.serverToClient.SendCanReloadBeforeShootingQuestion;

public interface AnswerEventHandler {

    void receiveEvent(AnswerEvent answerEvent);

    void handleEvent(NewConnectionAnswer event);
    void handleEvent(DisconnectedAnswer event);

    void handleEvent(ActionAttackAnswer event);
    void handleEvent(ActionEndTurnAnswer event);
    void handleEvent(ActionMoveAndGrabAnswer event);
    void handleEvent(ActionMoveAnswer event);
    void handleEvent(ActionPickWeaponAnswer event);
    void handleEvent(ActionReloadAnswer event);
    void handleEvent(ActionRespawnAnswer event);
    void handleEvent(ActionUseTurnPowerUpAnswer event);
    void handleEvent(ChooseHowToPayToPickWeaponAnswer event);
    void handleEvent(ChooseHowToShootAnswer event);
    void handleEvent(ChooseHowToUseTurnPowerUpAnswer event);
    void handleEvent(ChooseIfToUseAsyncPowerUpAnswer event);
    void handleEvent(ChoosePowerUpToRespawnAnswer event);
    void handleEvent(ChoosePowerUpToUseAnswer event);
    void handleEvent(ChooseWeaponToAttackAnswer event);
    void handleEvent(ChooseWeaponToPickAnswer event);
    void handleEvent(ChooseWeaponToReloadAnswer event);
    void handleEvent(ChooseWeaponToSwitchAnswer event);
    void handleEvent(WhereToMoveAndGrabAnswer event);
    void handleEvent(RefreshPossibleActionsAnswer event);
    void handleEvent(WhereToMoveAnswer event);
    void handleEvent(AskOrderAndDefenderAnswer event);
    void handleEvent(ChooseHowToPayForAttackingAnswer event);

    void handleEvent(ChooseHowToPayToSwitchWeaponsAnswer event);

    void handleEvent(ChooseHowToPayToReloadAnswer event);

    void handleEvent(Ping event);

    void handleEvent(UseGrenadeAnswer event);
    void handleEvent(RefreshPossibleActionsAfterReloadingAnswer event);

    void handleEvent(ChooseIfUseATargetingScopeAnswer event);

    void handleEvent(SendCanMoveBeforeShootingAnswer event);
    void handleEvent(SendCanReloadBeforeShootingAnswer event);
    void handleEvent(ChooseHowToPayToReloadBeforeAttackAnswer event);
}
