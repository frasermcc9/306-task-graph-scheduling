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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class ParallelAStar extends AbstractSchedulingAlgorithm {

  private final TopologicalSortContext<List<Integer>> topologicalOrderFinder;
  private final int cacheKey;

  private final ExecutorService executor;

  public ParallelAStar(
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

    executor = Executors.newFixedThreadPool(128);
  }

  @Override
  public AlgorithmSchedule schedule() {
    List<List<Integer>> topological = topologicalOrderFinder.sortedTopological();

    if (topological.size() == 0) {
      return scheduleFactory.newSchedule(context);
    }

    int orphans = listToBitfield(topological.get(0));

    final PriorityBlockingQueue<AbstractIterativeSchedule> queue = new PriorityBlockingQueue<>();
    queue.offer(new AStarSchedule(orphans, null, queue, cacheKey));

    for (;;) {
      try {
        AbstractIterativeSchedule next = queue.take();
        if (next.saturated()) {
          executor.shutdown();
          return next;
        }
        executor.submit(next::propagate);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    //    pool.shutdown();
    //    try {
    //      pool.awaitTermination(20, TimeUnit.SECONDS);
    //    } catch (InterruptedException e) {
    //      e.printStackTrace();
    //    }
    //    return getCache(cacheKey).getBestSchedule();
  }
}
