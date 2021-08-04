package com.jacketing.algorithm.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jacketing.TestUtil;
import com.jacketing.TestUtil.GraphResult;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.io.IOException;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

public class BruteForceSchedulerTest {

  @Test
  public void testScheduleWithOneProcessor() {
    Graph graph = TestUtil.graphVariantOne();
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(1);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new BruteForceScheduler(graph, programContext, ScheduleFactory.create())
    );

    Schedule schedule = schedulingAlgorithmStrategy.schedule();
    schedule.getDuration();

    assertEquals(10, schedule.getDuration());
  }

  @Test
  public void testScheduleWithTwoProcessors() {
    Graph graph = TestUtil.graphVariantOne();
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new BruteForceScheduler(graph, programContext, ScheduleFactory.create())
    );

    Schedule schedule = schedulingAlgorithmStrategy.schedule();
    schedule.getDuration();

    assertEquals(8, schedule.getDuration());
  }

  @Test
  public void testScheduleWithThreeProcessors() {
    Graph graph = TestUtil.graphVariantOne();
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(3);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new BruteForceScheduler(graph, programContext, ScheduleFactory.create())
    );

    Schedule schedule = schedulingAlgorithmStrategy.schedule();
    schedule.getDuration();

    assertEquals(8, schedule.getDuration());
  }

  @Test
  @Ignore
  public void testGraphSuiteTwoCores() throws IOException {
    List<GraphResult> graphs = TestUtil.getGraphTestSuite();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);

    for (GraphResult graph : graphs) {
      SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
        new BruteForceScheduler(
          graph.getGraph(),
          programContext,
          ScheduleFactory.create()
        )
      );

      Schedule schedule = schedulingAlgorithmStrategy.schedule();
      int optimalLength = schedule.getDuration();

      assertEquals(graph.getTwoCoresResult(), optimalLength);
    }
  }

  @Test
  @Ignore
  public void testGraphSuiteFourCores() throws IOException {
    List<GraphResult> graphs = TestUtil.getGraphTestSuite();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(4);

    for (GraphResult graph : graphs) {
      SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
        new BruteForceScheduler(
          graph.getGraph(),
          programContext,
          ScheduleFactory.create()
        )
      );

      Schedule schedule = schedulingAlgorithmStrategy.schedule();
      int optimalLength = schedule.getDuration();

      assertEquals(graph.getFourCoresResult(), optimalLength);
    }
  }
}
