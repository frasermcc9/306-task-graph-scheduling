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

package com.jacketing.algorithm.algorithms.smart;

import com.jacketing.algorithm.AlgorithmFactory;
import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.algorithms.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.algorithms.astar.AStar;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.common.cache.ArrayDuplicationStaticCache;
import com.jacketing.algorithm.algorithms.common.cache.ValidDuplicationStaticCache;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.common.log.Log;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;

/**
 * The primary sequential algorithm used to schedule graphs. The SmartAlgorithm
 * analyzes the graph and picks one of several graphs.
 */
public class SmartAlgorithm extends AbstractSchedulingAlgorithm {

  /**
   * The algorithm to run.
   */
  private final SchedulingAlgorithmStrategy algorithm;

  public SmartAlgorithm(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    this(
      graph,
      context,
      scheduleFactory,
      new AlgorithmSource() {
        @Override
        public AlgorithmFactory getAStar() {
          if (context.isDisablePog()) {
            return (g, c, sf) ->
              new AStar(g, c, sf, ValidDuplicationStaticCache::new);
          }
          return (g, c, sf) ->
            new AStar(g, c, sf, ArrayDuplicationStaticCache::new);
        }
      }
    );
  }

  public SmartAlgorithm(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory,
    AlgorithmSource algorithmSource
  ) {
    super(graph, context, scheduleFactory);
    AlgorithmFactory algorithmFactory;

    // List schedule when scheduling on a single processor, or there are no
    // nodes.
    if (
      graph.getAdjacencyList().getNodeCount() == 0 ||
      context.getProcessorsToScheduleOn() == 1
    ) {
      Log.info("Solving input graph with rapid list scheduler.");
      algorithmFactory = algorithmSource.getList();
      algorithm =
        algorithmFactory.createAlgorithm(graph, context, scheduleFactory);
      return;
    }

    // If there are no edges, use a DFS algorithm with independent estimate
    // scheduler (should produce tighter upper bound than list).
    if (graph.getAdjacencyList().getEdgeCount() == 0) {
      Log.info(
        "Solving input graph with iterative DFS, using independent " +
        "estimate scheduler."
      );
      // only works on 2 processors for some reason
      if (context.getProcessorsToScheduleOn() == 2) {
        graph.introduceVirtualForIdenticalNodes();
      }
      algorithmFactory = algorithmSource.getAStar();
      algorithm =
        algorithmFactory
          .createAlgorithm(graph, context, scheduleFactory)
          .withEstimateAlgorithm(algorithmSource.getIndependent());
      return;
    }

    // If there are not many edges relative to nodes, use DFS, but with list
    // scheduling estimate.
    if (graph.getAdjacencyList().getNodeToEdgeRatio() > 200) {
      Log.info(
        "Solving input graph with iterative DFS, using list estimate " +
        "scheduler."
      );
      algorithmFactory = algorithmSource.getDfs();
      algorithm =
        algorithmFactory
          .createAlgorithm(graph, context, scheduleFactory)
          .withEstimateAlgorithm(algorithmSource.getList());
      return;
    }

    // Otherwise, use A* with list scheduler.
    Log.info("Solving input graph with A*, using list estimate scheduler.");
    algorithmFactory = algorithmSource.getAOAStar();
    algorithm =
      algorithmFactory
        .createAlgorithm(graph, context, scheduleFactory)
        .withEstimateAlgorithm(algorithmSource.getList());
  }

  @Override
  public AlgorithmSchedule schedule() {
    return algorithm.schedule();
  }
}
