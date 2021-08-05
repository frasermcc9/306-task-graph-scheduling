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

import com.jacketing.algorithm.impl.DepthFirstScheduler;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.common.Loader;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import org.jetbrains.annotations.NotNull;

public class AlgorithmLoader implements Loader<Schedule> {

  private final Graph graph;
  private final AlgorithmContext context;

  private AlgorithmLoader(
    @NotNull Graph graph,
    @NotNull AlgorithmContext context
  ) {
    this.graph = graph;
    this.context = context;
  }

  public static Loader<Schedule> create(Graph graph, AlgorithmContext context) {
    return new AlgorithmLoader(graph, context);
  }

  public Schedule load() {
    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new DepthFirstScheduler(graph, context, ScheduleFactory.create())
    );

    return schedulingAlgorithmStrategy.schedule();
  }
}
