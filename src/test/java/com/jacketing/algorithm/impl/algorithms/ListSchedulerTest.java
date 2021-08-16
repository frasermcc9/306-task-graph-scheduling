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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jacketing.TestUtil;
import com.jacketing.algorithm.impl.X.AlgorithmSchedule;
import com.jacketing.algorithm.impl.algorithms.suboptimal.IndependentScheduler;
import com.jacketing.algorithm.impl.algorithms.suboptimal.ListScheduler;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class ListSchedulerTest {

  @Test
  public void testSchedule() {
    Graph graph = TestUtil.graphVariantOne();
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new ListScheduler(graph, programContext, ScheduleFactory.create())
    );

    AlgorithmSchedule schedule = schedulingAlgorithmStrategy.schedule();
    schedule.getDuration();

    assertEquals(9, schedule.getDuration());
  }

  @Test
  public void testGraphSuite() throws IOException {
    List<TestUtil.GraphResult> graphs = TestUtil.getGraphTestSuite();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);

    for (TestUtil.GraphResult graphResult : graphs) {
      SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
        new ListScheduler(
          graphResult.getGraph(),
          programContext,
          ScheduleFactory.create()
        )
      );

      AlgorithmSchedule schedule = schedulingAlgorithmStrategy.schedule();
      int duration = schedule.getDuration();
      int best = graphResult.getTwoCoresResult();

      assertTrue(best <= duration);
    }
  }

  @Test
  public void testIndependent() {
    Graph graph = TestUtil.graphVariantIndependent();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(4);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new IndependentScheduler(graph, programContext, ScheduleFactory.create())
    );

    AlgorithmSchedule schedule = schedulingAlgorithmStrategy.schedule();
    int duration = schedule.getDuration();

    int expected = 15;

    assertTrue(expected <= duration);
  }
}
