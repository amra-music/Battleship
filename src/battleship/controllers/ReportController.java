package battleship.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

public class ReportController {
    public LineChart<Number, Number> lineChart;
    public NumberAxis x;
    public NumberAxis y;
    public AnchorPane title;
    private int iterations;

    @FXML
    public void initialize() {
    }

    public void transferData(XYChart.Series<Number, Number> series1, XYChart.Series<Number, Number> series2, XYChart.Series<Number, Number> series3, XYChart.Series<Number, Number> series4, int iterations) {
        x.setUpperBound(iterations);
        this.iterations = iterations;
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

    public void exportTxt(ActionEvent actionEvent) {
        StringBuilder txt = new StringBuilder(",Random strategy,,Sequence strategy,,Strategy 1,,Strategy 2\n" +
                ",Iterations,Probability,Iterations,Probability,Iterations,Probability,Iterations,Probability\n");
        Map<Integer, Pair<Integer, String>> randomData = getInfo(lineChart.getData().get(0).getData());
        Map<Integer, Pair<Integer, String>> sequenceData = getInfo(lineChart.getData().get(1).getData());
        Map<Integer, Pair<Integer, String>> strategy1Data = getInfo(lineChart.getData().get(2).getData());
        Map<Integer, Pair<Integer, String>> strategy2Data = getInfo(lineChart.getData().get(3).getData());
        for (int i = 17; i <= 100; i++) {
            txt.append(i).append(",");
            txt.append(randomData.containsKey(i) ? randomData.get(i).getKey() : "0").append(",").append(randomData.containsKey(i) ? randomData.get(i).getValue() : "0 %").append(",");
            txt.append(sequenceData.containsKey(i) ? sequenceData.get(i).getKey() : "0").append(",").append(sequenceData.containsKey(i) ? sequenceData.get(i).getValue() : "0 %").append(",");
            txt.append(strategy1Data.containsKey(i) ? strategy1Data.get(i).getKey() : "0").append(",").append(strategy1Data.containsKey(i) ? strategy1Data.get(i).getValue() : "0 %").append(",");
            txt.append(strategy2Data.containsKey(i) ? strategy2Data.get(i).getKey() : "0").append(",").append(strategy2Data.containsKey(i) ? strategy2Data.get(i).getValue() : "0 %").append(",").append("\n");
        }
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage reportStage = (Stage) lineChart.getScene().getWindow();
        File file = fileChooser.showSaveDialog(reportStage);
        if (file != null) {
            saveTextToFile(txt.toString(), file);
        }
    }

    private Map<Integer, Pair<Integer, String>> getInfo(ObservableList<XYChart.Data<Number, Number>> data) {
        Map<Integer, Pair<Integer, String>> mapa = new TreeMap<>();
        // kljuc - 17, vrijednost 1, 6%
        for (XYChart.Data<Number, Number> datum : data) {
            if (mapa.containsKey(datum.getYValue().intValue())) {
                Pair<Integer, String> oldValuePair = mapa.get(datum.getYValue().intValue());
                mapa.put(datum.getYValue().intValue(), new Pair<>(oldValuePair.getKey() + 1, Math.round(((oldValuePair.getKey() + 1.) / iterations) * 10000) / 100. + " %"));
            } else
                mapa.put(datum.getYValue().intValue(), new Pair<>(1, Math.round((1. / iterations) * 100 * 100) / 100. + " %"));
        }
        return mapa;
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}