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

package com.jacketing.algorithm;

import com.jacketing.algorithm.impl.X.AlgorithmSchedule;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.common.Loader;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import org.jetbrains.annotations.NotNull;

public class AlgorithmLoader implements Loader<AlgorithmSchedule> {

  private final Graph graph;
  private final AlgorithmContext context;
  private final AlgorithmFactory algorithmFactory;

  private AlgorithmLoader(
    @NotNull final Graph graph,
    @NotNull final AlgorithmContext context,
    @NotNull final AlgorithmFactory algorithmFactory
  ) {
    this.graph = graph;
    this.context = context;
    this.algorithmFactory = algorithmFactory;
  }

  public static Loader<AlgorithmSchedule> create(
    @NotNull final Graph graph,
    @NotNull final AlgorithmContext context,
    @NotNull final AlgorithmFactory algorithmFactory
  ) {
    return new AlgorithmLoader(graph, context, algorithmFactory);
  }

  public AlgorithmSchedule load() {
    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = algorithmFactory
      .createAlgorithm(graph, context, ScheduleFactory.create())
      .withObservable(context.getObserver());

    return schedulingAlgorithmStrategy.schedule();
  }
}
