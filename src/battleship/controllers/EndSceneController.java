package battleship.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class EndSceneController {
    public AnchorPane title;
    public ImageView endPicture;
    public Label endTextLabel;
    private boolean win;
    private Stage playStage;

    public void setWin(boolean win) {
        this.win = win;
    }

    public void setPlayStage(Stage playStage) {
        this.playStage = playStage;
    }

    public EndSceneController(boolean win, Stage playStage) {
        setWin(win);
        setPlayStage(playStage);
    }

    @FXML
    public void initialize() {
        if (win) {
            endTextLabel.setText("Congratulations! You win");
            endPicture.setImage(new Image("/img/win.png"));
        } else {
            endTextLabel.setText("Ohh no!\nYou lose");
            endPicture.setImage(new Image("/img/lose.png"));
        }
    }

    public void drag(MouseEvent mouseEvent) {
        Stage stage = (Stage) title.getScene().getWindow();
        title.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - mouseEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - mouseEvent.getSceneY());
        });
    }

    public void playAgain(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/playScene.fxml"));
            Stage newPlayStage = new Stage();
            newPlayStage.initStyle(StageStyle.UNDECORATED);
            newPlayStage.setResizable(false);
            newPlayStage.setScene(new Scene(root));
            Stage currentStage = (Stage) title.getScene().getWindow();
            playStage.close();
            currentStage.close();
            newPlayStage.show();
        } catch (IOException error) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Problem " + error.getMessage());
            alert.show();
        }
    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
