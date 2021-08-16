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

import com.jacketing.algorithm.AlgorithmFactory;
import com.jacketing.algorithm.impl.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;

public class SmartAlgorithm extends AbstractSchedulingAlgorithm {

  private final SchedulingAlgorithmStrategy algorithm;

  public SmartAlgorithm(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory,
    AlgorithmSource algorithmSource
  ) {
    super(graph, context, scheduleFactory);
    AlgorithmFactory algorithmFactory;

    if (graph.getAdjacencyList().getNodeToEdgeRatio() > 2) {
      algorithmFactory = algorithmSource.getDfs();
    } else {
      algorithmFactory = algorithmSource.getAStar();
    }

    algorithm =
      algorithmFactory.createAlgorithm(graph, context, scheduleFactory);
  }

  public SmartAlgorithm(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    this(graph, context, scheduleFactory, new AlgorithmSource() {});
  }

  @Override
  public AlgorithmSchedule schedule() {
    return algorithm.schedule();
  }
}
