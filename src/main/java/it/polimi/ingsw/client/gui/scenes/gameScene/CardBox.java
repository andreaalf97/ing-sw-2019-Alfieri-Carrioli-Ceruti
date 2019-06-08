package it.polimi.ingsw.client.gui.scenes.gameScene;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class CardBox {

    private HBox hBox;


    protected CardBox(String path){

        this.hBox = new HBox();

        Image image = new Image(
                path,
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

        hBox.getStyleClass().add("shadow");

    }

    protected HBox gethBox(){
        return this.hBox;
    }


}
