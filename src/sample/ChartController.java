package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChartController {
    //@FXML
    public LineChart<Number, Number> lineChart;
    //@FXML
    public NumberAxis x;
    //@FXML
    public NumberAxis y;
    public AnchorPane title;

    @FXML
    public void initialize() {
    }

    public void transferData(XYChart.Series<Number, Number> series1, XYChart.Series<Number, Number> series2, XYChart.Series<Number, Number> series3, XYChart.Series<Number, Number> series4) {
        lineChart.getData().addAll(series1, series2, series3, series4);
    }

    public void close(MouseEvent mouseEvent) {
       Stage report = (Stage) lineChart.getScene().getWindow();
       report.close();
    }

    public void titleClick(MouseEvent mouseEvent) {
        Stage report = (Stage) lineChart.getScene().getWindow();
        title.setOnMouseDragged(dragEvent -> {
            report.setX(dragEvent.getScreenX() - mouseEvent.getSceneX());
            report.setY(dragEvent.getScreenY() - mouseEvent.getSceneY());
        });
    }
}
