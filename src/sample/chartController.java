package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class chartController {
    public LineChart<Number, Number> lineChart;
    public NumberAxis x;
    public NumberAxis y;

    @FXML
    public void initialize() {
    }

    public void transferData(XYChart.Series<Number, Number> series1, XYChart.Series<Number, Number> series2, XYChart.Series<Number, Number> series3, XYChart.Series<Number, Number> series4) {
        lineChart.getData().addAll(series1, series2, series3, series4);
    }

}
