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

package com.jacketing.algorithm.algorithms.dfs;

import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.algorithms.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.algorithms.common.AbstractIterativeSchedule;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.common.cache.StringDuplicationStaticCache;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.algorithm.util.topological.TopologicalSort;
import com.jacketing.algorithm.util.topological.TopologicalSortContext;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class IterativeDfs extends AbstractSchedulingAlgorithm {

  private final TopologicalSortContext<List<Integer>> topologicalOrderFinder;
  private final int cacheKey;

  public IterativeDfs(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    cacheKey =
      useCache(new StringDuplicationStaticCache(graph, context, HashSet::new));

    topologicalOrderFinder =
      new TopologicalSortContext<>(TopologicalSort.withLayers(graph));

    SchedulingAlgorithmStrategy algorithm = estimateAlgorithmFactory.createAlgorithm(
      graph,
      context,
      scheduleFactory
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

    List<Integer> freeNodes = new ArrayList<>(topological.get(0));

    int freeNodeBitfield = 0;
    for (int orphan : freeNodes) {
      freeNodeBitfield |= (1 << orphan);
    }

    Deque<AbstractIterativeSchedule> stack = new ConcurrentLinkedDeque<>();
    stack.addFirst(
      new IterativeSchedule(freeNodeBitfield, null, stack, cacheKey)
    );

    while (!stack.isEmpty()) {
      AbstractIterativeSchedule next = stack.removeFirst();
      if (next.saturated()) {
        int scheduleLength = next.getTotalTime();
        if (scheduleLength < getCache(cacheKey).getUpperBound()) {
          getCache(cacheKey).updateUpper(next);
        }
        continue;
      }
      next.propagate();
    }
    return getCache(cacheKey).getBestSchedule();
  }
}
