package com.jacketing.view.innercontrollers;

import com.jacketing.algorithm.structures.ProcessorTaskList;
import com.jacketing.algorithm.structures.ScheduleV1;
import com.jacketing.algorithm.structures.Task;
import com.jacketing.common.analysis.AlgorithmEvent;
import com.jacketing.common.analysis.AlgorithmObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ScheduleController {

  private List<ScheduleV1> scheduleList;
  private StackedBarChart<String, Integer> bestScheduleGraph;
  private VBox scheduleListView;
  private AlgorithmObserver observer;
  private NumberAxis axis;
  private List<Button> buttons;

  public ScheduleController(
    AlgorithmObserver observer,
    StackedBarChart<String, Integer> bestScheduleGraph,
    VBox scheduleListView,
    NumberAxis axis
  ) {
    this.bestScheduleGraph = bestScheduleGraph;
    this.scheduleListView = scheduleListView;
    this.axis = axis;
    this.observer = observer;
    this.scheduleList = new ArrayList<>();
    this.buttons = new ArrayList<>();

    bestScheduleGraph.setTitle("Current Best Schedule");
    bestScheduleGraph.setAnimated(false);
    axis.setAutoRanging(false);
    axis.setLowerBound(0);
    scheduleListView.setSpacing(1);

    observer.on(
      AlgorithmEvent.BEST_UPDATE,
      data -> {
        ScheduleV1 schedule = observer.getCurrentBestSchedule();
        if (schedule != null) {
          if (!scheduleList.contains(schedule)) {
            Platform.runLater(
              () -> {
                addNewSchedule(schedule);
                plotSchedule(schedule.getProcessorMap());
              }
            );
          }
        }
      }
    );
  }

  private void addNewSchedule(ScheduleV1 schedule) {
    scheduleList.add(schedule);

    String id = (scheduleList.size() - 1) + "";
    String text = "Schedule " + id + " - Time: " + schedule.getDuration();
    Button button = new Button(text);
    buttons.add(button);
    button.setId(id);
    button.setOnAction(
      (
        event -> {
          highlightButton(button);
          plotSchedule(
            scheduleList.get(Integer.parseInt(button.getId())).getProcessorMap()
          );
        }
      )
    );
    highlightButton(button);
    button.getStyleClass().add("history-button");
    button.setPrefHeight(Double.MAX_VALUE);
    button.setPrefWidth(Double.MAX_VALUE);

    scheduleListView.getChildren().add(0, button);
  }

  private void highlightButton(Button button) {
    for (Button otherButton : buttons) {
      otherButton.getStyleClass().remove("highlighted");
    }
    button.getStyleClass().add("highlighted");
  }

  private void plotSchedule(Map<Integer, ProcessorTaskList> processorMap) {
    bestScheduleGraph.getData().clear();
    // need to split tasks into lists for each processor
    int upperBound = 0;
    for (int i = 0; i < processorMap.size(); i++) {
      for (Task task : processorMap.get(i)) {
        // find last task for upper bound of graph
        int endTime = task.getEndTime();
        if (endTime > upperBound) {
          upperBound = endTime;
        }
      }
    }

    axis.setUpperBound(upperBound);

    int processorIndex = 0;
    for (ProcessorTaskList taskList : processorMap.values()) {
      if (taskList.size() == 0) {
        break;
      }
      XYChart.Series<String, Integer> procSeries = new XYChart.Series();

      //delayed start
      if (taskList.get(0).getStartTime() != 0) {
        XYChart.Data<String, Integer> bar = new XYChart.Data(
          Integer.toString(processorIndex),
          taskList.get(0).getStartTime()
        );
        bar
          .nodeProperty()
          .addListener((ov, oldNode, newNode) -> newNode.setVisible(false));
        procSeries.getData().add(bar);
      }
      // then add the first task
      XYChart.Data<String, Integer> firstBar = new XYChart.Data(
        Integer.toString(processorIndex),
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
        if (
          taskList.get(i - 1).getEndTime() != taskList.get(i).getStartTime()
        ) {
          XYChart.Data<String, Integer> bar = new XYChart.Data(
            Integer.toString(processorIndex),
            taskList.get(i).getStartTime() - taskList.get(i - 1).getEndTime()
          );
          bar
            .nodeProperty()
            .addListener((ov, oldNode, newNode) -> newNode.setVisible(false));
          procSeries.getData().add(bar);
        }
        // now just add the current task
        XYChart.Data<String, Integer> bar = new XYChart.Data(
          Integer.toString(processorIndex),
          taskList.get(i).getDuration()
        );
        bar
          .nodeProperty()
          .addListener(
            (ov, oldNode, newNode) ->
              newNode.setStyle("-fx-border-color: black")
          );
        procSeries.getData().add(bar);
      }

      ObservableList<XYChart.Series<String, Integer>> series = bestScheduleGraph.getData();
      series.addAll(procSeries);
      processorIndex++;
    }
  }
}
