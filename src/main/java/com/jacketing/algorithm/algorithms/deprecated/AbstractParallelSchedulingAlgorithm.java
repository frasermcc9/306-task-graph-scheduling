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

package com.jacketing.algorithm.algorithms.deprecated;

import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.concurrent.ForkJoinPool;

public abstract class AbstractParallelSchedulingAlgorithm
  extends AbstractSchedulingAlgorithm {

  protected ForkJoinPool executor;

  public AbstractParallelSchedulingAlgorithm(
    Graph graph,
    AlgorithmContext context,
    ScheduleFactory scheduleFactory
  ) {
    super(graph, context, scheduleFactory);
    this.executor = new ForkJoinPool(context.getCoresToCalculateWith());
  }
}
