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
