package com.jacketing.view.innercontrollers;

import com.jacketing.algorithm.impl.structures.ProcessorTaskList;
import com.jacketing.algorithm.impl.structures.ScheduleImpl;
import com.jacketing.algorithm.impl.structures.Task;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.io.cli.ApplicationContext;
import com.jacketing.io.cli.ProgramContext;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    List<String> input = new ArrayList<String>();
    input.add("");
    input.add("4");
    AlgorithmContext context = new ProgramContext(input);

    // create dummy schedule
    Map<Integer, ProcessorTaskList> processorMap = new HashMap();
    Schedule schedule = new ScheduleImpl(context, processorMap, null);
    ProcessorTaskList core1List = new ProcessorTaskList();
    ProcessorTaskList core2List = new ProcessorTaskList();
    ProcessorTaskList core3List = new ProcessorTaskList();
    ProcessorTaskList core4List = new ProcessorTaskList();
    core1List.add(new Task(3, 3, 0));
    core1List.add(new Task(6, 3, 1));
    core1List.add(new Task(10, 2, 2));
    core2List.add(new Task(4, 3, 3));
    core2List.add(new Task(7, 2, 4));
    core3List.add(new Task(3, 3, 5));
    core3List.add(new Task(7, 2, 6));
    core4List.add(new Task(4, 3, 7));
    core4List.add(new Task(9, 2, 8));
    processorMap.put(0, core1List);
    processorMap.put(1, core2List);
    processorMap.put(2, core3List);
    processorMap.put(3, core4List);

    // need to split tasks into lists for each processor
    int upperBound = 0;
    for (int i = 0; i < processorMap.size(); i++) {

      for (Task task : processorMap.get(0)) {

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
        if (taskList.get(i - 1).getEndTime() != taskList.get(i).getStartTime()) {
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
            (ov, oldNode, newNode) -> newNode.setStyle("-fx-border-color: black")
          );
        procSeries.getData().add(bar);
      }

      ObservableList<XYChart.Series<String, Integer>> series = bestScheduleGraph.getData();
      series.addAll(procSeries);
      processorIndex++;
    }


  }
}
