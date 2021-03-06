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

package com.jacketing.algorithm.algorithms;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jacketing.TestUtil;
import com.jacketing.TestUtil.GraphResult;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.deprecated.ParallelDepthFirstScheduler;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class ParallelDepthFirstSchedulerTest {

  @Test
  public void testGraphSuiteTwoCores() throws IOException {
    List<GraphResult> graphs = TestUtil.getGraphTestSuite();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);
    when(programContext.getCoresToCalculateWith()).thenReturn(8);

    for (GraphResult graph : graphs) {
      SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
        new ParallelDepthFirstScheduler(
          graph.getGraph(),
          programContext,
          ScheduleFactory.create()
        )
      );

      AlgorithmSchedule schedule = schedulingAlgorithmStrategy.schedule();
      int optimalLength = schedule.getDuration();
      int expectedLength = graph.getTwoCoresResult();

      assertEquals(expectedLength, optimalLength);
    }
  }

  @Test
  public void testGraphSuiteFourCores() throws IOException {
    List<GraphResult> graphs = TestUtil.getGraphTestSuite();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(4);
    when(programContext.getCoresToCalculateWith()).thenReturn(8);

    for (GraphResult graph : graphs) {
      SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
        new ParallelDepthFirstScheduler(
          graph.getGraph(),
          programContext,
          ScheduleFactory.create()
        )
      );

      AlgorithmSchedule schedule = schedulingAlgorithmStrategy.schedule();
      int optimalLength = schedule.getDuration();
      int expectedLength = graph.getFourCoresResult();

      assertEquals(expectedLength, optimalLength);
    }
  }

  @Test
  public void testEmptyGraph() {
    Graph graph = TestUtil.emptyGraph();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(4);
    when(programContext.getCoresToCalculateWith()).thenReturn(8);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new ParallelDepthFirstScheduler(
        graph,
        programContext,
        ScheduleFactory.create()
      )
    );

    AlgorithmSchedule schedule = schedulingAlgorithmStrategy.schedule();
    int duration = schedule.getDuration();

    assertEquals(0, duration);
  }
}
