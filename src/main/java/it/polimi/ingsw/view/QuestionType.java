package it.polimi.ingsw.view;

public enum QuestionType {

    Action,

    WhereToMove,
    WhereToMoveAndGrab,

    ChoosePowerUpToRespawn,
    ChoosePowerUpToUse,

    ChooseWeaponToAttack,
    ChooseWeaponToSwitch,
    ChooseWeaponToReload,

    PayWith,

    Shoot,      //double splitters tra i defenders e movers ecc, single splitters tra i singoli defenders per esempio
    UseTurnPowerUp, //double splitters tra i diversi parametri del metodo da chiamare, nessun singolo splitter poichè non passo nessun array!!
    UseAsyncPowerUp;
}
