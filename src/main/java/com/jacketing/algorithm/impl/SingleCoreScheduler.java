package com.jacketing.algorithm.impl;

import com.jacketing.algorithm.impl.structures.Task;
import com.jacketing.algorithm.impl.util.TopologicalOrderFinder;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.List;

public class SingleCoreScheduler extends AbstractSchedulingAlgorithm {

  TopologicalOrderFinder topologicalOrderFinder;

  public SingleCoreScheduler(
    Graph graph,
    ProgramContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    topologicalOrderFinder = new TopologicalOrderFinder(graph);
  }

  @Override
  public Schedule schedule() {
    Schedule schedule = scheduleFactory.newSchedule(context);
    List<Integer> topological = topologicalOrderFinder.sortedTopological();

    for (int node : topological) {
      int nodeWeight = graph.getNodeWeight(node);
      Task task = new Task(schedule.getProcessorEnd(0), nodeWeight, node);
      schedule.addTask(task, 0);
    }

    return schedule;
  }
}
