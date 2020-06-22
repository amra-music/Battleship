package battleship.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class RulesController {
    public AnchorPane title;

    @FXML
    public void initialize() {
    }

    public void drag(MouseEvent mouseEvent) {
        Stage stage = (Stage) title.getScene().getWindow();
        title.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - mouseEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - mouseEvent.getSceneY());
        });
    }

    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }
}
