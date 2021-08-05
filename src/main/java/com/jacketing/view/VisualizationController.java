package com.jacketing.view;

import com.jacketing.util.CPU.CpuReader;
import com.jacketing.util.CPU.CpuStatModel;
import com.jacketing.util.RAM.RamReader;
import com.jacketing.util.RAM.RamStatModel;
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
  private LineChart<String, Long> ramGraph;

  @FXML
  public void initialize() {

    LineChart[] allUpdatingCharts = {thread1Graph, thread2Graph, thread3Graph, thread4Graph, ramGraph };
    removeAnimationsAndTicks(allUpdatingCharts);

    LineChart[] cpuCharts = {thread1Graph, thread2Graph, thread3Graph, thread4Graph};
    CpuReader reader = new CpuReader();

    int i = 0;
    for (LineChart<String, Double> chart : cpuCharts) {
      XYChart.Series<String, Double> series = new XYChart.Series<>();
      CpuStatModel model = new CpuStatModel(series, i);
      reader.addModel(model);
      reader.setSyntheticLoad();
      chart.getData().add(series);
      i++;
    }

    XYChart.Series<String, Long> ramSeries = new XYChart.Series<>();
    ramGraph.getData().add(ramSeries);
    ramGraph.setLegendVisible(false);
    ramGraph.setTitle("RAM Usage (MB)");
    RamStatModel model = new RamStatModel(ramSeries);
    new RamReader(model);
  }

  private void removeAnimationsAndTicks(LineChart[] charts) {
    for (LineChart chart : charts) {
      Axis xAxis = chart.getXAxis();
      xAxis.setTickLabelsVisible(false);
      xAxis.setAnimated(false);

      Axis yAxis = chart.getYAxis();
      yAxis.setAnimated(false);

      chart.setCreateSymbols(false);
      chart.setAnimated(false);
    }
  }

}
