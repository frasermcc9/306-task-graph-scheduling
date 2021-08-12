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

package com.jacketing.algorithm.impl.X;

import com.jacketing.algorithm.impl.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.impl.util.topological.TopologicalSortContext;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.algorithm.interfaces.util.topological.TopologicalSort;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.*;

public class AStar extends AbstractSchedulingAlgorithm {

  private final int numberOfProcessors;
  private final TopologicalSortContext<List<Integer>> topologicalOrderFinder;

  private final Set<String> equivalents = new HashSet<>();

  private Schedule bestSchedule;
  private int upperBound;

  public AStar(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    numberOfProcessors = context.getProcessorsToScheduleOn();
    topologicalOrderFinder =
      new TopologicalSortContext<>(TopologicalSort.withLayers(graph));
  }

  @Override
  public Schedule schedule() {
    //    List<List<Integer>> topological = topologicalOrderFinder.sortedTopological();
    //
    //    if (topological.size() == 0) {
    //      return scheduleFactory.newSchedule(context);
    //    }
    //    List<Integer> freeNodes = new ArrayList<>(topological.get(0));
    //
    //    final Queue<ELSPartial> queue = new PriorityQueue<>();
    //    queue.add();

    return null;
  }
}
