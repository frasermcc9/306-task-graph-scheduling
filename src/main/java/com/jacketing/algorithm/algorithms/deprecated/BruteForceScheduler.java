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

package com.jacketing.algorithm.algorithms.deprecated;

import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.algorithm.structures.ScheduleV1;
import com.jacketing.algorithm.structures.Task;
import com.jacketing.algorithm.util.topological.TopologicalSort;
import com.jacketing.algorithm.util.topological.TopologicalSortContext;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.List;

public class BruteForceScheduler extends AbstractSchedulingAlgorithm {

  private final int numberOfProcessors;
  private final TopologicalSortContext<List<Integer>> topologicalOrderFinder;
  private ScheduleV1 schedule;

  public BruteForceScheduler(
    Graph graph,
    ProgramContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    topologicalOrderFinder =
      new TopologicalSortContext<>(TopologicalSort.withLayers(graph));
    numberOfProcessors = context.getProcessorsToScheduleOn();
  }

  /**
   * Outputs an optimal scheduling by exploring all possible schedules.
   *
   * @implNote The algorithm runs recursively like DFS. This algorithm
   * runs exponentially on the number of processors.
   * It can be improved by adding cost function to prune the recursion tree.
   *
   * @return ScheduleV1 object containing optimal schedule.
   */
  @Override
  public ScheduleV1 schedule() {
    List<List<Integer>> topological = topologicalOrderFinder.sortedTopological();

    List<Integer> freeNodes = new ArrayList<>(topological.get(0));
    List<Integer> visited = new ArrayList<>();
    dfs(scheduleFactory.newSchedule(context), freeNodes, visited);

    return schedule;
  }

  private void dfs(
    ScheduleV1 curState,
    List<Integer> freeNodes,
    List<Integer> visited
  ) {
    if (freeNodes.isEmpty()) {
      if (schedule == null || curState.getDuration() < schedule.getDuration()) {
        schedule = curState;
      }
      return;
    }

    for (int node : freeNodes) {
      int nodeWeight = graph.getNodeWeight(node);
      List<Integer> parentNodes = graph.getAdjacencyList().getParentNodes(node);
      for (int processor = 0; processor < numberOfProcessors; processor++) {
        // if the prerequisite node that ends latest is in the different proc
        int startTime = findEarliestStartTime(
          node,
          parentNodes,
          curState,
          processor
        );

        Task task = new Task(
          Math.max(startTime, curState.getProcessorEnd(processor)),
          nodeWeight,
          node
        );

        ScheduleV1 nextState = curState.clone();
        // Add the task to next state
        nextState.addTask(task, processor);

        List<Integer> nextFreeNodes = new ArrayList<>();
        for (Integer freeNode : freeNodes) {
          // exclude the node that has already been scheduled.
          if (node != freeNode) {
            nextFreeNodes.add(freeNode);
          }
        }

        List<Integer> nextVisited = new ArrayList<>(visited);
        nextVisited.add(node);

        for (int nextNode : graph.getAdjacencyList().getChildNodes(node)) {
          // If all prerequisite nodes are scheduled, then the child node is free to be scheduled.
          if (
            nextVisited.containsAll(
              graph.getAdjacencyList().getParentNodes(nextNode)
            )
          ) {
            nextFreeNodes.add(nextNode);
          }
        }

        dfs(nextState, nextFreeNodes, nextVisited);
      }
    }
  }
}
