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
  private LineChart<String, Double> threadGraph;

  @FXML
  private LineChart<String, Long> ramGraph;

  @FXML
  public void initialize() {
    LineChart[] allUpdatingCharts = {
      threadGraph,
      ramGraph
    };
    removeAnimationsAndTicks(allUpdatingCharts);
    CpuReader reader = new CpuReader();

    for (int i = 0; i < 4; i++) {
      XYChart.Series<String, Double> series = new XYChart.Series<>();
      CpuStatModel model = new CpuStatModel(series, i);
      reader.setSyntheticLoad();
      reader.addModel(model);
      threadGraph.getData().add(series);
    }
    threadGraph.setLegendVisible(false);

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
