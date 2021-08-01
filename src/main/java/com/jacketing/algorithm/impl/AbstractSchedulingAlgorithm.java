package com.jacketing.algorithm.impl;

import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;

public abstract class AbstractSchedulingAlgorithm
  implements SchedulingAlgorithmStrategy {

  protected final Graph graph;
  protected final ProgramContext context;
  protected final ScheduleFactory scheduleFactory;

  public AbstractSchedulingAlgorithm(
    Graph graph,
    ProgramContext context,
    ScheduleFactory scheduleFactory
  ) {
    this.graph = graph;
    this.context = context;
    this.scheduleFactory = scheduleFactory;
  }
}
