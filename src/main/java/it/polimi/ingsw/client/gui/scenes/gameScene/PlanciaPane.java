package it.polimi.ingsw.client.gui.scenes.gameScene;

import it.polimi.ingsw.client.PlayerColor;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class PlanciaPane {

    private HBox hBox;

    protected PlanciaPane(PlayerColor playerColor, String cssClass){

        this.hBox = new HBox();

        Image image = new Image(
                playerColor.getPath(),
                0, 0,
                true, false
        );

        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1, 1, true, true, true, false)
        );

        hBox.setBackground(new Background(backgroundImage));

        hBox.getStyleClass().add(cssClass);


    }

    protected HBox gethBox(){
        return this.hBox;
    }
}
