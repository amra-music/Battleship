package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class chartController {
    public LineChart lineChart;
    public NumberAxis x;
    public NumberAxis y;

    @FXML
    public void initialize() {
    }

    public void transferData(XYChart.Series series1, XYChart.Series series2, XYChart.Series series3, XYChart.Series series4) {
        lineChart.getData().addAll(series1,series2,series3,series4);
    }

}
