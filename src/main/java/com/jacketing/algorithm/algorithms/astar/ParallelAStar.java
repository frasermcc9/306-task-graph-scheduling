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

package com.jacketing.algorithm.algorithms.astar;

import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.algorithms.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.algorithms.common.AbstractIterativeSchedule;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.common.cache.ArrayDuplicationStaticCache;
import com.jacketing.algorithm.algorithms.common.cache.PooledCacheDecorator;
import com.jacketing.algorithm.algorithms.suboptimal.ListScheduler;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.algorithm.util.topological.TopologicalSort;
import com.jacketing.algorithm.util.topological.TopologicalSortContext;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.AbstractQueue;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class ParallelAStar extends AbstractSchedulingAlgorithm {

  private final TopologicalSortContext<List<Integer>> topologicalOrderFinder;
  private final int cacheKey;

  public ParallelAStar(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    cacheKey =
      useCache(
        new PooledCacheDecorator(
          Executors.newWorkStealingPool(context.getCoresToCalculateWith()),
          new ArrayDuplicationStaticCache(
            graph,
            context,
            ConcurrentHashMap::newKeySet
          )
        )
      );

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

    final AbstractQueue<AbstractIterativeSchedule> queue = new PriorityQueue<>();
    queue.offer(new ParallelAStarSchedule(orphans, null, queue, cacheKey));

    while (!queue.isEmpty()) {
      AbstractIterativeSchedule next = queue.poll();
      if (next.saturated()) {
        return next;
      }

      next.propagate();
    }

    return getCache(cacheKey).getBestSchedule();
  }
}
