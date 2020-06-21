package battleship;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StartSceneControler {
    public Pane scenePane;
    public Button startButton;
    public Button rulesButton;
    public Button exitButton;
    public ImageView startSceneImageView;
    public Pane primaryScene;
    public AnchorPane title;


    @FXML
    public void initialize() {
        startButton.getStyleClass().add("start-button");
        rulesButton.getStyleClass().add("start-button");
        exitButton.getStyleClass().add("start-button");
    }

    public void start(MouseEvent mouseEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/playScene.fxml"));
            Stage playStage = new Stage();
            playStage.initStyle(StageStyle.UNDECORATED);
            playStage.setResizable(false);
            playStage.setScene(new Scene(root));
            playStage.getScene().getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());
            Stage startStage = (Stage) startButton.getScene().getWindow();
            startStage.close();
            playStage.show();
        } catch (IOException error) {
            Alert alert  = new Alert(Alert.AlertType.ERROR, "Problem "+error.getMessage());
            alert.show();
        }
    }

    public void rules(MouseEvent mouseEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/rules.fxml"));
            Stage rulesStage = new Stage();
            rulesStage.initStyle(StageStyle.UNDECORATED);
            rulesStage.initModality(Modality.APPLICATION_MODAL);
            rulesStage.setResizable(false);
            rulesStage.setScene(new Scene(root));
            rulesStage.show();
        } catch (IOException error) {
            Alert alert  = new Alert(Alert.AlertType.ERROR, "Problem "+error.getMessage());
            alert.show();
        }
    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void drag(MouseEvent mouseEvent) {
        Stage stage = (Stage) title.getScene().getWindow();
        title.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - mouseEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - mouseEvent.getSceneY());
        });
    }
}