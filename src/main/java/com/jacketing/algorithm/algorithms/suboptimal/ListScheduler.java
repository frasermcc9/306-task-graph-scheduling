/*
 * Copyright 2021 Team Jacketing
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of Team
 * Jacketing (the author) and its affiliates, if any. The intellectual and
 * technical concepts contained herein are proprietary to Team Jacketing, and
 * are protected by copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from the author.
 *
 */

package com.jacketing.algorithm.algorithms.suboptimal;

import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.algorithm.structures.ScheduleV1;
import com.jacketing.algorithm.structures.Task;
import com.jacketing.algorithm.util.topological.TopologicalSort;
import com.jacketing.algorithm.util.topological.TopologicalSortContext;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.List;

public class ListScheduler extends AbstractSchedulingAlgorithm {

  private final TopologicalSort<Integer> topologicalOrderFinder;
  private final int numberOfProcessors;

  public ListScheduler(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    topologicalOrderFinder =
      new TopologicalSortContext<>(TopologicalSort.withoutLayers(graph));

    numberOfProcessors = context.getProcessorsToScheduleOn();
  }

  @Override
  public ScheduleV1 schedule() {
    ScheduleV1 schedule = scheduleFactory.newSchedule(context);
    List<Integer> topologicalOrder = getOrder();

    // ScheduleV1 nodes in topological order.
    for (int node : topologicalOrder) {
      int nodeWeight = graph.getNodeWeight(node);
      List<Integer> parentNodes = graph.getAdjacencyList().getParentNodes(node);
      int earliestStartTime = Integer.MAX_VALUE;
      int curProc = 0;
      for (int processor = 0; processor < numberOfProcessors; processor++) {
        // for each processor, find proc that yields the earliest finish time.
        int startTime = findEarliestStartTime(
          node,
          parentNodes,
          schedule,
          processor
        );
        startTime = Math.max(startTime, schedule.getProcessorEnd(processor));
        if (earliestStartTime > startTime) {
          earliestStartTime = startTime;
          curProc = processor;
        }
        earliestStartTime = Math.min(earliestStartTime, startTime);
      }

      Task task = new Task(earliestStartTime, nodeWeight, node);

      schedule.addTask(task, curProc);
    }
    return schedule;
  }

  public List<Integer> getOrder() {
    return topologicalOrderFinder.sortedTopological();
  }
}
