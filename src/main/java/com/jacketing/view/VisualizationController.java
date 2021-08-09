package com.jacketing.view;

import com.jacketing.util.CPU.CpuReader;
import com.jacketing.util.CPU.CpuStatModel;
import com.jacketing.util.RAM.RamReader;
import com.jacketing.util.RAM.RamStatModel;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class VisualizationController {

  @FXML
  private LineChart<String, Double> threadGraph;

  @FXML
  private NumberAxis threadAxis;

  @FXML
  private LineChart<String, Long> ramGraph;

  @FXML
  private Text duration, schedulesChecked, improvements, peakRam, peakCpu, currentBestTime, numberCores, numberProcessors, algorithm, time, inputFile;

  @FXML
  private Button stop;

  @FXML
  private VBox scheduleList;

  @FXML
  private StackedBarChart<String, Double> bestScheduleGraph;

  @FXML
  private TextArea logs;

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
      //reader.setSyntheticLoad();
      reader.addModel(model);
      threadGraph.getData().add(series);
    }
    threadGraph.setLegendVisible(false);
    threadAxis.setAutoRanging(false);
    threadAxis.setUpperBound(100);
    threadAxis.setLowerBound(0);
    threadAxis.setTickUnit(5);

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
