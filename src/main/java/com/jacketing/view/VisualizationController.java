package com.jacketing.view;

import com.jacketing.util.CPU.CpuReader;
import com.jacketing.util.CPU.CpuStatModel;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class VisualizationController {

  @FXML
  private LineChart<String, Double> thread1Graph;

  @FXML
  private LineChart<String, Double> thread2Graph;

  @FXML
  private LineChart<String, Double> thread3Graph;

  @FXML
  private LineChart<String, Double> thread4Graph;

  @FXML
  private LineChart<String, Double> ramGraph;

  public VisualizationController() {}

  @FXML
  public void initialize() {

    LineChart[] cpuCharts = {thread1Graph, thread2Graph, thread3Graph, thread4Graph};
    CpuReader reader = new CpuReader();

    int i = 0;
    for (LineChart<String, Double> chart : cpuCharts) {
      XYChart.Series<String, Double> series = new XYChart.Series<>();
      CpuStatModel model = new CpuStatModel(series, i);
      reader.addModel(model);

      reader.setSyntheticLoad();
      chart.getData().add(series);

      Axis xAxis = chart.getXAxis();
      xAxis.setTickLabelsVisible(false);
      xAxis.setAnimated(false);

      Axis yAxis = chart.getYAxis();
      yAxis.setAnimated(false);

      chart.setCreateSymbols(false);
      chart.setAnimated(false);

      i++;
    }
  }

}
