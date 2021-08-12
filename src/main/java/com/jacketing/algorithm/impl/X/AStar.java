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
import com.jacketing.algorithm.impl.algorithms.ListScheduler;
import com.jacketing.algorithm.impl.util.topological.TopologicalSortContext;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.algorithm.interfaces.util.topological.TopologicalSort;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class AStar extends AbstractSchedulingAlgorithm {

  private final TopologicalSortContext<List<Integer>> topologicalOrderFinder;
  private final int cacheKey;

  public AStar(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    cacheKey = useCache(new StaticCacheImpl(graph, context, HashSet::new));

    topologicalOrderFinder =
      new TopologicalSortContext<>(TopologicalSort.withLayers(graph));

    SchedulingAlgorithmStrategy algorithm = SchedulingAlgorithmStrategy.create(
      new ListScheduler(graph, context, scheduleFactory)
    );
    AlgorithmSchedule estimateSchedule = algorithm.schedule();
    getCache(cacheKey).updateUpper(estimateSchedule);
  }

  @Override
  public AlgorithmSchedule schedule() {
    List<List<Integer>> topological = topologicalOrderFinder.sortedTopological();

    if (topological.size() == 0) {
      return scheduleFactory.newSchedule(context);
    }

    int orphans = listToBitfield(topological.get(0));

    final PriorityQueue<AbstractIterativeSchedule> queue = new PriorityQueue<>();
    queue.offer(new SinglePropagationSchedule(orphans, null, queue, cacheKey));

    int count = 0;

    while (!queue.isEmpty()) {
      AbstractIterativeSchedule next = queue.poll();
      count++;
      if (next.saturated()) {
        System.out.println(count);
        return next;
      }

      next.propagate();
    }

    return getCache(cacheKey).getBestSchedule();
  }
}
