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

package com.jacketing.algorithm.AO;

import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.algorithms.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.common.cache.StaticCacheFactory;
import com.jacketing.algorithm.algorithms.common.cache.ValidDuplicationStaticCache;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.AbstractQueue;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AllocationOrderingAStar extends AbstractSchedulingAlgorithm {

  private final int cacheKey;

  public AllocationOrderingAStar(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory,
    StaticCacheFactory cacheFactory
  ) {
    super(graph, context, scheduleFactory);
    cacheKey = useCache(cacheFactory.create(graph, context, HashSet::new));

    SchedulingAlgorithmStrategy algorithm = estimateAlgorithmFactory.createAlgorithm(
      graph,
      context,
      scheduleFactory
    );
    AlgorithmSchedule estimateSchedule = algorithm.schedule();
    getCache(cacheKey).updateUpper(estimateSchedule);

    graph.preparePredecessorMap();
  }

  public AllocationOrderingAStar(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    this(graph, context, scheduleFactory, ValidDuplicationStaticCache::new);
  }

  @Override
  public AlgorithmSchedule schedule() {
    final int nodeCount = graph.getAdjacencyList().getNodeCount();

    final AbstractQueue<AOSchedule> queue = new PriorityQueue<>();
    queue.offer(QueuedAllocationSchedule.empty(queue, -1, nodeCount, cacheKey));

    while (!queue.isEmpty()) {
      AOSchedule next = queue.poll();
      if (next.saturated()) {
        AOSchedule upgrade = next.upgrade();
        //int duration = upgrade.getDuration();
        //continue;
        return next.upgrade();
      }
      next.propagate();
    }

    return getCache(cacheKey).getBestSchedule();
  }
}
