package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class startSceneControler {
    public Pane scenePane;
    public Button startButton;
    public Button optionsButton;
    public Button exitButton;
    public ImageView startSceneImageView;


    @FXML
    public void initialize() {

    }

    public void start(MouseEvent mouseEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("playScene.fxml"));
            Stage startStage = new Stage();
            startStage.setTitle("Battleship");
            startStage.setResizable(false);
            startStage.setScene(new Scene(root));
            startButton.getScene().getWindow().hide();
            startStage.showAndWait();
        } catch (IOException error) {
            Alert alert  = new Alert(Alert.AlertType.ERROR, "Problem "+error.getMessage());
            alert.showAndWait();
        }
    }

    public void options(MouseEvent mouseEvent) {

    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
