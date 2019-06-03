package it.polimi.ingsw.client.gui.scenes;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.events.serverToClient.*;
import it.polimi.ingsw.model.map.MapName;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GameScene extends Gui implements MyScene {

    private MapName mapName;

    private Scene scene;

    Stage window;

    Image mapImage;

    ArrayList<Image> otherPlayersImages;

    private Image planciaGiocatoreImage;

    public GameScene(String username, GameStartedQuestion event) {
        super();
    }

    void setInitialScene(GameStartedQuestion event) {

        mapName = event.mapName;

        try {
            //immagine mappa
            mapImage = new Image(new FileInputStream("src/main/resources/Grafica/Mappe/Mappe/"+ mapName.name()+".png"));
            //immagine plancia del giocatore (per ora yellow)
            planciaGiocatoreImage = new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Yellow/Yellow_front.png"));
            //immagini plance altri giocatori, per ora tutte tranne yellow
            otherPlayersImages.add(new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Purple/Purple_front.png")));
            otherPlayersImages.add(new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Green/Green_front.png")));
            otherPlayersImages.add(new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Grey/Grey_front.png")));
            otherPlayersImages.add(new Image(new FileInputStream("src/main/resources/Grafica/Plance_giocatori/Blue/Blue_front.png")));

        }catch (FileNotFoundException e){
            System.err.println("File not found");
            e.printStackTrace();
        }

        /*ImageView purple_plancia_imageView = new ImageView(purple_plancia);
        ImageView green_plancia_imageView = new ImageView(green_plancia);
        ImageView grey_plancia_imageView = new ImageView(grey_plancia);
        ImageView blue_plancia_imageView = new ImageView(blue_plancia);
        purple_plancia_imageView.setFitWidth(300);
        purple_plancia_imageView.setFitHeight(75);
        green_plancia_imageView.setFitWidth(300);
        green_plancia_imageView.setFitHeight(75);
        grey_plancia_imageView.setFitWidth(300);
        grey_plancia_imageView.setFitHeight(75);
        blue_plancia_imageView.setFitWidth(300);
        blue_plancia_imageView.setFitHeight(75);*/

        //Gridpane in cui ci stanno tutte le plance dei giocatori (che a loro volta sono dei gridpane) ed eventuali testi o boottoni o menu
        GridPane Plance_altri_giocatori = new GridPane();
        Plance_altri_giocatori.setMinSize(300, 400);
        Plance_altri_giocatori.setStyle("-fx-border-color: red");
        Plance_altri_giocatori.setHgap(5);
        Plance_altri_giocatori.setVgap(5);

        Plance_altri_giocatori.widthProperty().addListener((observable, oldValue, newValue) -> {

            Plance_altri_giocatori.minWidth(otherPlayersImages.get(0).getWidth());
            Plance_altri_giocatori.maxWidth(otherPlayersImages.get(0).getWidth());
        });
        Plance_altri_giocatori.heightProperty().addListener((observable, oldValue, newValue) -> {

            Plance_altri_giocatori.minHeight(otherPlayersImages.get(0).getHeight()*4);
            Plance_altri_giocatori.maxHeight(window.getHeight()-planciaGiocatoreImage.getHeight());
        });



        //Un gridpane per ogni plancia, ognuno avrà in background la propria immagine della plancia
        GridPane Purple_plancia_Gridpane = new GridPane();
        Purple_plancia_Gridpane.widthProperty().addListener((observable, oldValue, newValue) -> {
            Purple_plancia_Gridpane.minWidth(otherPlayersImages.get(0).getWidth());
            Purple_plancia_Gridpane.maxWidth(otherPlayersImages.get(0).getWidth());
        });
        Purple_plancia_Gridpane.heightProperty().addListener((observable, oldValue, newValue) -> {
            Purple_plancia_Gridpane.minHeight(otherPlayersImages.get(0).getHeight());
            Purple_plancia_Gridpane.maxHeight(otherPlayersImages.get(0).getHeight());
        });
        GridPane Green_plancia_Gridpane = new GridPane();
        Green_plancia_Gridpane.widthProperty().addListener((observable, oldValue, newValue) -> {
            Green_plancia_Gridpane.minWidth(otherPlayersImages.get(0).getWidth());
            Green_plancia_Gridpane.maxWidth(otherPlayersImages.get(0).getWidth());
        });
        Green_plancia_Gridpane.heightProperty().addListener((observable, oldValue, newValue) -> {
            Green_plancia_Gridpane.minHeight(otherPlayersImages.get(0).getHeight());
            Green_plancia_Gridpane.maxHeight(otherPlayersImages.get(0).getHeight());
        });
        GridPane Grey_plancia_Gridpane = new GridPane();
        Grey_plancia_Gridpane.widthProperty().addListener((observable, oldValue, newValue) -> {
            Grey_plancia_Gridpane.minWidth(otherPlayersImages.get(0).getWidth());
            Grey_plancia_Gridpane.maxWidth(otherPlayersImages.get(0).getWidth());
        });
        Grey_plancia_Gridpane.heightProperty().addListener((observable, oldValue, newValue) -> {
            Grey_plancia_Gridpane.minHeight(otherPlayersImages.get(0).getHeight());
            Grey_plancia_Gridpane.maxHeight(otherPlayersImages.get(0).getHeight());
        });
        GridPane Blue_plancia_Gridpane = new GridPane();
        Blue_plancia_Gridpane.widthProperty().addListener((observable, oldValue, newValue) -> {
            Blue_plancia_Gridpane.minWidth(otherPlayersImages.get(0).getWidth());
            Blue_plancia_Gridpane.maxWidth(otherPlayersImages.get(0).getWidth());
        });
        Blue_plancia_Gridpane.heightProperty().addListener((observable, oldValue, newValue) -> {
            Blue_plancia_Gridpane.minHeight(otherPlayersImages.get(0).getHeight());
            Blue_plancia_Gridpane.maxHeight(otherPlayersImages.get(0).getHeight());
        });

        BackgroundImage purple_plancia_image = new BackgroundImage(otherPlayersImages.get(0), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(Purple_plancia_Gridpane.getWidth(), Purple_plancia_Gridpane.getHeight(), true, true, true, false));
        Background Background_Purple_PlanciaImage = new Background(purple_plancia_image);
        Purple_plancia_Gridpane.setBackground(Background_Purple_PlanciaImage);

        BackgroundImage green_plancia_image = new BackgroundImage(otherPlayersImages.get(1), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(Green_plancia_Gridpane.getWidth(), Green_plancia_Gridpane.getHeight(), true, true, true, false));
        Background Background_Green_PlanciaImage = new Background(green_plancia_image);
        Green_plancia_Gridpane.setBackground(Background_Green_PlanciaImage);

        BackgroundImage grey_plancia_image = new BackgroundImage(otherPlayersImages.get(2), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(Grey_plancia_Gridpane.getWidth(), Grey_plancia_Gridpane.getHeight(), true, true, true, false));
        Background Background_Grey_PlanciaImage = new Background(grey_plancia_image);
        Grey_plancia_Gridpane.setBackground(Background_Grey_PlanciaImage);

        BackgroundImage blue_plancia_image = new BackgroundImage(otherPlayersImages.get(3), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(Blue_plancia_Gridpane.getWidth(), Blue_plancia_Gridpane.getHeight(), true, true, true, false));
        Background Background_Blue_PlanciaImage = new Background(blue_plancia_image);
        Blue_plancia_Gridpane.setBackground(Background_Blue_PlanciaImage);

        Plance_altri_giocatori.add(Purple_plancia_Gridpane, 1, 0);
        Plance_altri_giocatori.add(Green_plancia_Gridpane, 1, 1);
        Plance_altri_giocatori.add(Grey_plancia_Gridpane, 1, 2);
        Plance_altri_giocatori.add(Blue_plancia_Gridpane, 1, 3);


        //Gridpane della mappa
        GridPane mapGridPane = new GridPane();
        mapGridPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            mapGridPane.minWidth(mapImage.getWidth());
            mapGridPane.maxWidth(window.getWidth()-Plance_altri_giocatori.getWidth());
        });
        mapGridPane.heightProperty().addListener((observable, oldValue, newValue) -> {

            mapGridPane.setMinHeight(newValue.doubleValue());
            mapGridPane.setMaxHeight(newValue.doubleValue());
        });
        mapGridPane.setStyle("-fx-border-color: black");
       /* mapGridPane.maxWidthProperty().bind(Bindings.divide(window.widthProperty(), 1.45));
        mapGridPane.maxHeightProperty().bind(Bindings.divide(window.heightProperty(), 1.00));
        mapGridPane.minWidthProperty().bind(Bindings.divide(window.widthProperty(), 1.45));
        mapGridPane.minHeightProperty().bind(Bindings.divide(window.heightProperty(), 1.00));
        mapGridPane.setGridLinesVisible(true);*/


        //In questa GridPane, posta sul TOP LEFT, ci sarà la plancia giocatore
        GridPane planciaPlayerGridPane = new GridPane();

        /*planciaPlayerGridPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            //planciaPlayerGridPane.minWidth(Gui.planciaGiocatoreImage.getWidth());
            //planciaPlayerGridPane.maxWidth(Gui.planciaGiocatoreImage.getWidth());
            planciaPlayerGridPane.prefWidth(Gui.planciaGiocatoreImage.getHeight());
        });
        mapGridPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            //planciaPlayerGridPane.minHeight(Gui.planciaGiocatoreImage.getHeight());
            //planciaPlayerGridPane.maxHeight(Gui.planciaGiocatoreImage.getHeight());
            planciaPlayerGridPane.prefHeight(Gui.planciaGiocatoreImage.getHeight());
        });*/
        planciaPlayerGridPane.setStyle("-fx-border-color: blue");
        planciaPlayerGridPane.setPadding(new Insets(50, 0, 0, 50));
        planciaPlayerGridPane.maxWidthProperty().bind(Bindings.divide(window.widthProperty(), 3.00));
        //planciaPlayerGridPane.maxHeightProperty().bind(Bindings.divide(window.heightProperty(), 3.00));
        planciaPlayerGridPane.minWidthProperty().bind(Bindings.divide(window.widthProperty(), 3.00));
        planciaPlayerGridPane.minHeightProperty().bind(Bindings.divide(window.heightProperty(), 6.00));
        planciaPlayerGridPane.setGridLinesVisible(true);

        BackgroundImage image = new BackgroundImage(mapImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(mapGridPane.getWidth(), mapGridPane.getHeight(), true, true, true, false));
        Background BackgroundMapImage = new Background(image);
        mapGridPane.setBackground(BackgroundMapImage);

        BackgroundImage planciaImage = new BackgroundImage(planciaGiocatoreImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(planciaPlayerGridPane.getWidth(), planciaPlayerGridPane.getHeight()*0.9, true, true, true, false));
        Background BackgroundPlanciaImage = new Background(planciaImage);
        planciaPlayerGridPane.setBackground(BackgroundPlanciaImage);

        BorderPane gameBorderPane = new BorderPane();
        gameBorderPane.setStyle("-fx-border-color: red");
        gameBorderPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #300900, #7F1600)");
        gameBorderPane.setPrefSize(1000, 800);
        BorderPane.setAlignment(mapGridPane, Pos.BOTTOM_LEFT);
        BorderPane.setAlignment(planciaPlayerGridPane, Pos.TOP_LEFT);
        BorderPane.setAlignment(Plance_altri_giocatori, Pos.CENTER);
        gameBorderPane.setCenter(mapGridPane);
        gameBorderPane.setTop(planciaPlayerGridPane);
        gameBorderPane.setRight(Plance_altri_giocatori);
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
