package com.jacketing.view;

import com.jacketing.algorithm.impl.structures.Task;
import com.jacketing.util.CPU.CpuReader;
import com.jacketing.util.CPU.CpuStatModel;
import com.jacketing.util.RAM.RamReader;
import com.jacketing.util.RAM.RamStatModel;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;

public class VisualizationController {

  @FXML
  private StackedBarChart<String, Double> bestScheduleGraph;

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
    XYChart.Series<String, Double> procSeries = new XYChart.Series();

    // data with delays
    Task[] taskList = {
      new Task(3, 3, 0),
      new Task(6, 3, 0),
      new Task(10, 2, 0),
    };

    //delayed start
    if (taskList[0].getStartTime() != 0) {
      XYChart.Data<String, Double> bar = new XYChart.Data(
        "2",
        taskList[0].getStartTime()
      );
      bar
        .nodeProperty()
        .addListener((ov, oldNode, newNode) -> newNode.setVisible(false));
      procSeries.getData().add(bar);
    }
    // then add the first task
    XYChart.Data<String, Double> firstBar = new XYChart.Data(
      "2",
      taskList[0].getDuration()
    );
    firstBar
      .nodeProperty()
      .addListener(
        (ov, oldNode, newNode) -> newNode.setStyle("-fx-border-color: black")
      );
    procSeries.getData().add(firstBar);

    // the rest of the tasks
    for (int i = 1; i < taskList.length; i++) {
      // check for delays (if this task starts later than the previous end time)
      if (taskList[i - 1].getEndTime() != taskList[i].getStartTime()) {
        XYChart.Data<String, Double> bar = new XYChart.Data(
          "2",
          taskList[i].getStartTime() - taskList[i - 1].getEndTime()
        );
        bar
          .nodeProperty()
          .addListener((ov, oldNode, newNode) -> newNode.setVisible(false));
        procSeries.getData().add(bar);
      }
      // now just add the current task
      XYChart.Data<String, Double> bar = new XYChart.Data(
        "2",
        taskList[i].getDuration()
      );
      bar
        .nodeProperty()
        .addListener(
          (ov, oldNode, newNode) -> newNode.setStyle("-fx-border-color: black")
        );
      procSeries.getData().add(bar);
    }
    bestScheduleGraph.getData().addAll(procSeries);

    LineChart[] allUpdatingCharts = {
      thread1Graph,
      thread2Graph,
      thread3Graph,
      thread4Graph,
      ramGraph,
    };
    removeAnimationsAndTicks(allUpdatingCharts);

    LineChart[] cpuCharts = {
      thread1Graph,
      thread2Graph,
      thread3Graph,
      thread4Graph,
    };
    CpuReader reader = new CpuReader();

    int i = 0;
    for (LineChart<String, Double> chart : cpuCharts) {
      XYChart.Series<String, Double> series = new XYChart.Series<>();
      CpuStatModel model = new CpuStatModel(series, i);
      reader.addModel(model);
      //reader.setSyntheticLoad();
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
