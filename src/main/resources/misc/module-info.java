module it.polimi.ingsw {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires gson;

    opens it.polimi.ingsw.view.client to javafx.fxml;
    exports it.polimi.ingsw.view.client;
    exports it.polimi.ingsw.server to java.rmi;

}