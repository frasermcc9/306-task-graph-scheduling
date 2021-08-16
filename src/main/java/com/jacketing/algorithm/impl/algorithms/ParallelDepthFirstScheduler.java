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

package com.jacketing.algorithm.impl.algorithms;

import com.jacketing.algorithm.impl.X.AlgorithmSchedule;
import com.jacketing.algorithm.impl.algorithms.suboptimal.ListScheduler;
import com.jacketing.algorithm.impl.structures.Task;
import com.jacketing.algorithm.impl.util.topological.TopologicalSortContext;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.algorithm.interfaces.util.topological.TopologicalSort;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelDepthFirstScheduler
  extends AbstractParallelSchedulingAlgorithm {

  private final int numberOfProcessors;
  private final TopologicalSortContext<List<Integer>> topologicalOrderFinder;

  private final Set<String> equivalents = ConcurrentHashMap.newKeySet();

  private final AtomicInteger upperBound = new AtomicInteger();
  private volatile AlgorithmSchedule bestSchedule;

  public ParallelDepthFirstScheduler(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    topologicalOrderFinder =
      new TopologicalSortContext<>(TopologicalSort.withLayers(graph));
    numberOfProcessors = context.getProcessorsToScheduleOn();

    SchedulingAlgorithmStrategy algorithm = SchedulingAlgorithmStrategy.create(
      new ListScheduler(graph, context, scheduleFactory)
    );

    AlgorithmSchedule estimateSchedule = algorithm.schedule();
    upperBound.set(estimateSchedule.getDuration());
    bestSchedule = estimateSchedule;
  }

  @Override
  public AlgorithmSchedule schedule() {
    List<List<Integer>> topological = topologicalOrderFinder.sortedTopological();

    if (topological.size() == 0) {
      return scheduleFactory.newSchedule(context);
    }

    List<Integer> freeNodes = new ArrayList<>(topological.get(0));
    List<Integer> visited = new ArrayList<>();

    executor.invoke(
      new RecursiveDfs(scheduleFactory.newSchedule(context), freeNodes, visited)
    );

    return bestSchedule;
  }

  private class RecursiveDfs extends RecursiveAction {

    private final Schedule partialSchedule;
    private final List<Integer> orphanNodes;
    private final List<Integer> visited;

    public RecursiveDfs(
      Schedule partialSchedule,
      List<Integer> orphanNodes,
      List<Integer> visited
    ) {
      this.partialSchedule = partialSchedule;
      this.orphanNodes = orphanNodes;
      this.visited = visited;
    }

    private synchronized void updateBestSchedule(
      Schedule schedule,
      int compute
    ) {
      if (compute < upperBound.get()) {
        bestSchedule = schedule.clone();
        upperBound.getAndSet(compute);
      }
    }

    @Override
    protected void compute() {
      if (orphanNodes.isEmpty()) {
        int completeDuration = partialSchedule.getDuration();
        int intUpper = upperBound.get();
        if (bestSchedule == null || completeDuration < intUpper) {
          updateBestSchedule(partialSchedule, completeDuration);
        }
      }

      for (int node : orphanNodes) {
        int nodeWeight = graph.getNodeWeight(node);
        List<Integer> parentNodes = graph
          .getAdjacencyList()
          .getParentNodes(node);
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
          Schedule nextState = scheduleFactory.copy(partialSchedule);
          // Add the task to next state
          nextState.addTask(task, processor);

          // cull this branch if it exceeds best schedule so far
          int intUpper = upperBound.get();
          if (bestSchedule != null && nextState.getDuration() >= intUpper) {
            nextState.revert();
            continue;
          }

          String identifier = nextState.toString();
          if (equivalents.contains(identifier)) {
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

          invokeAll(new RecursiveDfs(nextState, nextFreeNodes, nextVisited));
          nextState.revert();
        }
      }
    }
  }
}
