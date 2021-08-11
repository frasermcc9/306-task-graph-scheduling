package com.jacketing.view.innercontrollers;

import com.jacketing.algorithm.impl.structures.Task;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ScheduleController {

  private StackedBarChart<String, Integer> bestScheduleGraph;
  private VBox scheduleList;

  public ScheduleController(
    StackedBarChart<String, Integer> bestScheduleGraph,
    VBox scheduleList,
    NumberAxis axis
  ) {
    this.bestScheduleGraph = bestScheduleGraph;
    this.scheduleList = scheduleList;

    axis.setAutoRanging(false);
    axis.setLowerBound(0);

    // data with delays
    int numberProcessors = 4;
    Task[] mockTaskList = {
      new Task(3, 3, 0),
      new Task(6, 3, 0),
      new Task(10, 2, 0),
      new Task(4, 3, 1),
      new Task(7, 2, 1),
      new Task(3, 3, 2),
      new Task(7, 2, 2),
      new Task(4, 3, 3),
      new Task(9, 2, 3),
    };

    // need to split tasks into lists for each processor
    int upperBound = 0;
    List<List<Task>> allTasks = new ArrayList<>();
    for (int i = 0; i < numberProcessors; i++) {
      allTasks.add(new ArrayList<>());

      for (Task task : mockTaskList) {

        // find last task for upper bound of graph
        int endTime = task.getEndTime();
        if (endTime > upperBound) {
          upperBound = endTime;
        }

        if (task.getId() == i) {
          allTasks.get(i).add(task);
        }
      }
    }

    axis.setUpperBound(upperBound);

    for (List<Task> taskList : allTasks) {
      XYChart.Series<String, Integer> procSeries = new XYChart.Series();

      //delayed start
      if (taskList.get(0).getStartTime() != 0) {
        XYChart.Data<String, Integer> bar = new XYChart.Data(
          Integer.toString(taskList.get(0).getId()),
          taskList.get(0).getStartTime()
        );
        bar
          .nodeProperty()
          .addListener((ov, oldNode, newNode) -> newNode.setVisible(false));
        procSeries.getData().add(bar);
      }
      // then add the first task
      XYChart.Data<String, Integer> firstBar = new XYChart.Data(
        Integer.toString(taskList.get(0).getId()),
        taskList.get(0).getDuration()
      );
      firstBar
        .nodeProperty()
        .addListener(
          (ov, oldNode, newNode) -> newNode.setStyle("-fx-border-color: black")
        );
      procSeries.getData().add(firstBar);

      // the rest of the tasks
      for (int i = 1; i < taskList.size(); i++) {
        // check for delays (if this task starts later than the previous end time)
        if (taskList.get(i - 1).getEndTime() != taskList.get(i).getStartTime()) {
          XYChart.Data<String, Integer> bar = new XYChart.Data(
            Integer.toString(taskList.get(i).getId()),
            taskList.get(i).getStartTime() - taskList.get(i - 1).getEndTime()
          );
          bar
            .nodeProperty()
            .addListener((ov, oldNode, newNode) -> newNode.setVisible(false));
          procSeries.getData().add(bar);
        }
        // now just add the current task
        XYChart.Data<String, Integer> bar = new XYChart.Data(
          Integer.toString(taskList.get(i).getId()),
          taskList.get(i).getDuration()
        );
        bar
          .nodeProperty()
          .addListener(
            (ov, oldNode, newNode) -> newNode.setStyle("-fx-border-color: black")
          );
        procSeries.getData().add(bar);
      }

      ObservableList<XYChart.Series<String, Integer>> series = bestScheduleGraph.getData();
      series.addAll(procSeries);
    }


  }
}
