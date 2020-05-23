package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

public class startSceneControler {
    public Pane scenePane;
    public Button startButton;
    public Button optionsButton;
    public Button exitButton;
    public ImageView startSceneImageView;

    private Image startSceneImage = new Image("sample/startScene.jpg");

    @FXML
    public void initialize() {
        //startSceneImageView.setImage(startSceneImage);
    }

    public void start(MouseEvent mouseEvent) {

    }

    public void options(MouseEvent mouseEvent) {

    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
