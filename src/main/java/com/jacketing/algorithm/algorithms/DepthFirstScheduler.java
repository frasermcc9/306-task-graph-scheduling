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

package com.jacketing.algorithm.algorithms;

import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.suboptimal.ListScheduler;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.algorithm.structures.ScheduleV1;
import com.jacketing.algorithm.structures.Task;
import com.jacketing.algorithm.util.SchedulingAlgorithmContextImpl;
import com.jacketing.algorithm.util.topological.TopologicalSort;
import com.jacketing.algorithm.util.topological.TopologicalSortContext;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DepthFirstScheduler extends AbstractSchedulingAlgorithm {

  private final int numberOfProcessors;
  private final TopologicalSortContext<List<Integer>> topologicalOrderFinder;

  private final Set<String> equivalents = new HashSet<>();

  private ScheduleV1 bestSchedule;
  private int upperBound;

  public DepthFirstScheduler(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    topologicalOrderFinder =
      new TopologicalSortContext<>(TopologicalSort.withLayers(graph));
    numberOfProcessors = context.getProcessorsToScheduleOn();

    ListScheduler scheduler = new ListScheduler(
      graph,
      context,
      scheduleFactory
    );
    new SchedulingAlgorithmContextImpl(scheduler);

    ScheduleV1 estimateSchedule = scheduler.scheduleOld();
    upperBound = estimateSchedule.getDuration();
    bestSchedule = estimateSchedule;

    updateObserver(o -> o.updateBestSchedule(estimateSchedule));
  }

  /**
   * Outputs an optimal scheduling by exploring all possible schedules.
   *
   * @return ScheduleV1 object containing optimal schedule.
   * @implNote The algorithm runs recursively like DFS. This algorithm runs
   * exponentially on the number of processors. It can be improved by adding
   * cost function to prune the recursion tree.
   */
  @Override
  public AlgorithmSchedule schedule() {
    List<List<Integer>> topological = topologicalOrderFinder.sortedTopological();

    if (topological.size() == 0) {
      return scheduleFactory.newSchedule(context);
    }

    List<Integer> freeNodes = new ArrayList<>(topological.get(0));
    List<Integer> visited = new ArrayList<>();
    recurseDFS(scheduleFactory.newSchedule(context), freeNodes, visited);

    return bestSchedule;
  }

  private void recurseDFS(
    ScheduleV1 partialSchedule,
    List<Integer> orphanNodes,
    List<Integer> visited
  ) {
    if (orphanNodes.isEmpty()) {
      int completeDuration = partialSchedule.getDuration();
      if (bestSchedule == null || completeDuration < upperBound) {
        bestSchedule = partialSchedule.clone();
        updateObserver(o -> o.updateBestSchedule(bestSchedule));
        upperBound = completeDuration;
      }
      return;
    }

    for (int node : orphanNodes) {
      int nodeWeight = graph.getNodeWeight(node);
      List<Integer> parentNodes = graph.getAdjacencyList().getParentNodes(node);
      for (int processor = 0; processor < numberOfProcessors; processor++) {
        int startTime = findEarliestStartTime(
          node,
          parentNodes,
          partialSchedule,
          processor
        );

        Task task = new Task(
          Math.max(startTime, partialSchedule.getProcessorEnd(processor)),
          nodeWeight,
          node
        );
        ScheduleV1 nextState = scheduleFactory.copy(partialSchedule);
        // Add the task to next state
        nextState.addTask(task, processor);

        // cull this branch if it exceeds best schedule so far
        if (bestSchedule != null && nextState.getDuration() >= upperBound) {
          updateObserver(o -> o.incrementCulledSchedules());
          nextState.revert();
          continue;
        }

        String identifier = nextState.toString();
        if (equivalents.contains(identifier)) {
          updateObserver(o -> o.incrementDuplicateSchedules());
          nextState.revert();
          continue;
        }
        equivalents.add(identifier);

        List<Integer> nextFreeNodes = new ArrayList<>();
        for (Integer freeNode : orphanNodes) {
          // exclude the node that has already been scheduled.
          if (node != freeNode) {
            nextFreeNodes.add(freeNode);
          }
        }

        List<Integer> nextVisited = new ArrayList<>(visited);
        nextVisited.add(node);

        // add orphan nodes to the free nodes
        for (int nextNode : graph.getAdjacencyList().getChildNodes(node)) {
          if (
            nextVisited.containsAll(
              graph.getAdjacencyList().getParentNodes(nextNode)
            )
          ) {
            nextFreeNodes.add(nextNode);
          }
        }

        updateObserver(o -> o.updateVisited(visited));
        updateObserver(o -> o.incrementCheckedSchedules(partialSchedule));
        recurseDFS(nextState, nextFreeNodes, nextVisited);

        nextState.revert();
      }
    }
  }
}
