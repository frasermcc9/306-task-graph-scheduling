package com.jacketing.view.innercontrollers;

import com.jacketing.algorithm.impl.structures.Task;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

public class ScheduleController {

  private StackedBarChart<String, Double> bestScheduleGraph;
  private VBox scheduleList;

  public ScheduleController(
    StackedBarChart<String, Double> bestScheduleGraph,
    VBox scheduleList
  ) {
    this.bestScheduleGraph = bestScheduleGraph;
    this.scheduleList = scheduleList;

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
  }
}
