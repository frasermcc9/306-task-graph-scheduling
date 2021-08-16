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

import com.jacketing.algorithm.AlgorithmFactory;
import com.jacketing.algorithm.impl.X.StaticCache;
import com.jacketing.algorithm.impl.algorithms.suboptimal.ListScheduler;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.common.analysis.UpdatesFromAlgorithm;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSchedulingAlgorithm
  implements SchedulingAlgorithmStrategy {

  private static final List<StaticCache> staticCache = new ArrayList<>();
  protected final Graph graph;
  protected final AlgorithmContext context;
  protected final ScheduleFactory scheduleFactory;
  protected UpdatesFromAlgorithm observer;

  protected AlgorithmFactory estimateAlgorithmFactory;

  public AbstractSchedulingAlgorithm(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    this.graph = graph;
    this.context = context;
    this.scheduleFactory = scheduleFactory;

    this.estimateAlgorithmFactory = ListScheduler::new;
  }

  public static StaticCache getCache(int key) {
    return staticCache.get(key);
  }

  public int useCache(StaticCache cache) {
    int position = staticCache.size();
    staticCache.add(cache);
    return position;
  }

  @Override
  public SchedulingAlgorithmStrategy withObservable(
    UpdatesFromAlgorithm updater
  ) {
    this.observer = updater;
    return this;
  }

  /**
   * Returns the earliest possible start time for given node and processor.
   * based on communication delay constraint between processors.
   *
   * @param node        node for which we want to know the earliest start time
   * @param parentNodes parent nodes of node.
   * @param schedule    current schedule.
   * @param processor   processor on which task is scheduled.
   * @return
   */
  protected int findEarliestStartTime(
    int node,
    List<Integer> parentNodes,
    Schedule schedule,
    int processor
  ) {
    int startTime = 0;
    for (Integer parentNode : parentNodes) {
      int parentEndTime = schedule.getTask(parentNode).getEndTime();
      if (schedule.getProcessor(parentNode) != processor) {
        startTime =
          Math.max(
            startTime,
            parentEndTime + graph.getEdgeWeight().from(parentNode).to(node)
          );
      }
    }
    return startTime;
  }

  protected int listToBitfield(List<Integer> integers) {
    int freeNodeBitfield = 0;
    for (int orphan : integers) {
      freeNodeBitfield |= (1 << orphan);
    }
    return freeNodeBitfield;
  }

  public AbstractSchedulingAlgorithm withEstimateAlgorithm(
    AlgorithmFactory factory
  ) {
    this.estimateAlgorithmFactory = factory;
    return this;
  }
}
