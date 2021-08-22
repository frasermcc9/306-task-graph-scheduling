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
import com.jacketing.algorithm.algorithms.common.cache.StaticCacheFactory;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.algorithm.util.topological.TopologicalSort;
import com.jacketing.algorithm.util.topological.TopologicalSortContext;
import com.jacketing.common.log.Log;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.AbstractQueue;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class AStar extends AbstractSchedulingAlgorithm {

  private final TopologicalSortContext<List<Integer>> layeredTopologicalFinder;
  private final int cacheKey;

  public AStar(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory,
    StaticCacheFactory cacheFactory
  ) {
    super(graph, context, scheduleFactory);
    cacheKey = useCache(cacheFactory.create(graph, context, HashSet::new));

    layeredTopologicalFinder =
      new TopologicalSortContext<>(TopologicalSort.withLayers(graph));

    SchedulingAlgorithmStrategy algorithm = estimateAlgorithmFactory.createAlgorithm(
      graph,
      context,
      scheduleFactory
    );
    AlgorithmSchedule estimateSchedule = algorithm.schedule();
    getCache(cacheKey).updateUpper(estimateSchedule);
  }

  public AStar(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    this(graph, context, scheduleFactory, ArrayDuplicationStaticCache::new);
  }

  @Override
  public AlgorithmSchedule schedule() {
    List<List<Integer>> topological = layeredTopologicalFinder.sortedTopological();

    if (topological.size() == 0) {
      return scheduleFactory.newSchedule(context);
    }

    int orphans = listToBitfield(topological.get(0));

    final AbstractQueue<AbstractIterativeSchedule> queue = new PriorityQueue<>();
    queue.offer(new AStarSchedule(orphans, null, queue, cacheKey));

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
