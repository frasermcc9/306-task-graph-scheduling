package com.jacketing.algorithm.impl;

import com.jacketing.algorithm.impl.structures.Task;
import com.jacketing.algorithm.impl.util.topological.TopologicalSortContext;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.algorithm.interfaces.util.topological.TopologicalSort;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.List;

public class DepthFirstScheduler extends AbstractSchedulingAlgorithm {

  private final int numberOfProcessors;
  private final TopologicalSortContext<List<Integer>> topologicalOrderFinder;
  private Schedule schedule;

  public DepthFirstScheduler(
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
   * @return Schedule object containing optimal schedule.
   * @implNote The algorithm runs recursively like DFS. This algorithm runs
   * exponentially on the number of processors. It can be improved by adding
   * cost function to prune the recursion tree.
   */
  @Override
  public Schedule schedule() {
    List<List<Integer>> topological = topologicalOrderFinder.sortedTopological();

    List<Integer> freeNodes = new ArrayList<>(topological.get(0));
    List<Integer> visited = new ArrayList<>();
    dfs(scheduleFactory.newSchedule(context), freeNodes, visited);

    return schedule;
  }

  private void dfs(
    Schedule curState,
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
        int startTime = 0;
        for (Integer parentNode : parentNodes) {
          int parentEndTime = curState.getTask(parentNode).getEndTime();
          if (curState.getProcessor(parentNode) != processor) {
            startTime =
              Math.max(
                startTime,
                parentEndTime + graph.getEdgeWeight().from(parentNode).to(node)
              );
          }
        }

        Task task = new Task(
          Math.max(startTime, curState.getProcessorEnd(processor)),
          nodeWeight,
          node
        );
        Schedule nextState = scheduleFactory.copy(curState);
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
          // If all prerequisite nodes are scheduled, then the child node is
          // free to be scheduled.
          if (
            nextVisited.containsAll(
              graph.getAdjacencyList().getParentNodes(nextNode)
            )
          ) {
            nextFreeNodes.add(nextNode);
          }
        }

        if (
          schedule != null &&
            (nextState.getDuration() >= schedule.getDuration())
        ) {
          if (nextState.getDuration() > schedule.getDuration()) {
            continue;
          }
        }

        dfs(nextState, nextFreeNodes, nextVisited);
      }
    }
  }
}
