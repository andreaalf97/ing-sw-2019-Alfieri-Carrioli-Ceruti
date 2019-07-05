package it.polimi.ingsw.client;


import it.polimi.ingsw.events.QuestionEvent;
import it.polimi.ingsw.events.serverToClient.*;

public interface QuestionEventHandler {

    void receiveEvent(QuestionEvent questionEvent);

    void handleEvent(TemporaryIdQuestion event);
    void handleEvent(InvalidUsernameQuestion event);
    void handleEvent(AddedToWaitingRoomQuestion event);
    void handleEvent(NewPlayerConnectedQuestion event);
    void handleEvent(DisconnectedQuestion event);
    void handleEvent(GameStartedQuestion event);
    void handleEvent(PlayerDisconnectedQuestion event);

    void handleEvent(ActionQuestion event);
    void handleEvent(ChooseHowToPayForAttackingQuestion event);
    void handleEvent(ChooseHowToPayToSwitchWeaponsQuestion event);
    void handleEvent(ChooseHowToPayToPickWeaponQuestion event);
    void handleEvent(ChooseHowToPayToReloadQuestion event);
    void handleEvent(ChooseHowToShootQuestion event);
    void handleEvent(ChooseHowToUseTurnPowerUpQuestion event);
    void handleEvent(ChooseIfToUseAsyncPowerUpQuestion event);
    void handleEvent(ChoosePowerUpToRespawnQuestion event);
    void handleEvent(ChoosePowerUpToUseQuestion event);
    void handleEvent(ChooseWeaponToAttackQuestion event);
    void handleEvent(ChooseWeaponToPickQuestion event);
    void handleEvent(ChooseWeaponToReloadQuestion event);
    void handleEvent(ChooseWeaponToSwitchQuestion event);
    void handleEvent(ModelUpdate event);
    void handleEvent(TextMessage event);
    void handleEvent(WhereToMoveAndGrabQuestion event);
    void handleEvent(WhereToMoveQuestion event);
    void handleEvent(AskOrderAndDefenderQuestion event);

    void handleEvent(UseGrenadeQuestion event);
    void handleEvent(ActionAfterReloadingQuestion event);

    void handleEvent(ChooseIfUseATargetingScopeQuestion event);

    void handleEvent(GameRestartedQuestion event);

    void handleEvent(EndGameQuestion event);
    void handleEvent(SendCanMoveBeforeShootingQuestion event);
    void handleEvent(SendCanReloadBeforeShootingQuestion event);
    void handleEvent(ChooseHowToPayToReloadBeforeAttackQuestion event);
}
