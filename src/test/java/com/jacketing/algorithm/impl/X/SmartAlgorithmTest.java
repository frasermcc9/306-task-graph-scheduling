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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.jacketing.TestUtil;
import com.jacketing.TestUtil.GraphResult;
import com.jacketing.algorithm.algorithms.DepthFirstScheduler;
import com.jacketing.algorithm.algorithms.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.algorithms.astar.AStar;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.smart.AlgorithmSource;
import com.jacketing.algorithm.algorithms.smart.SmartAlgorithm;
import com.jacketing.algorithm.algorithms.suboptimal.IndependentScheduler;
import com.jacketing.algorithm.algorithms.suboptimal.ListScheduler;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class SmartAlgorithmTest {

  AlgorithmSource algorithmSource = mock(AlgorithmSource.class);

  @Before
  public void setUp() throws Exception {
    when(algorithmSource.getDfs()).thenReturn(DepthFirstScheduler::new);
    when(algorithmSource.getAStar()).thenReturn(AStar::new);
    when(algorithmSource.getIndependent())
      .thenReturn(IndependentScheduler::new);
    when(algorithmSource.getList()).thenReturn(ListScheduler::new);
  }

  @Test
  public void testAlgorithmChoiceDfs() {
    Graph graph = TestUtil.graphVariantIndependent();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new SmartAlgorithm(
        graph,
        programContext,
        ScheduleFactory.create(),
        algorithmSource
      )
    );

    verify(algorithmSource, times(1)).getIndependent();
    verify(algorithmSource, times(1)).getAStar();
  }

  @Test
  public void testAlgorithmChoiceAStar() {
    Graph graph = TestUtil.graphVariantFive();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new SmartAlgorithm(
        graph,
        programContext,
        ScheduleFactory.create(),
        algorithmSource
      )
    );

    verify(algorithmSource, times(1)).getAStar();
    verify(algorithmSource, times(1)).getList();
  }

  @Test
  public void testAlgorithmChoiceDFSandList() {
    Graph graph = TestUtil.graphVariantIndependentOneEdge();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new SmartAlgorithm(
        graph,
        programContext,
        ScheduleFactory.create(),
        algorithmSource
      )
    );

    verify(algorithmSource, times(1)).getAStar();
    verify(algorithmSource, times(1)).getList();
  }

  //  @Test
  //  public void testIndependentProducesCorrectResult() {
  //    Graph graph = TestUtil.graphVariantIndependent();
  //
  //    ProgramContext programContext = mock(ProgramContext.class);
  //    when(programContext.getProcessorsToScheduleOn()).thenReturn(4);
  //
  //    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
  //      new SmartAlgorithm(
  //        graph,
  //        programContext,
  //        ScheduleFactory.create(),
  //        new AlgorithmSource() {}
  //      )
  //    );
  //    schedulingAlgorithmStrategy.schedule();
  //  }

  @Test
  public void testGraphSuiteTwoCores() throws IOException {
    List<GraphResult> graphs = TestUtil.getGraphTestSuite();

    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);

    for (GraphResult graph : graphs) {
      SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
        new SmartAlgorithm(
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

    for (GraphResult graph : graphs) {
      SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
        new SmartAlgorithm(
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

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new SmartAlgorithm(graph, programContext, ScheduleFactory.create())
    );

    AlgorithmSchedule schedule = schedulingAlgorithmStrategy.schedule();
    int optimalLength = schedule.getDuration();
    int expectedLength = 0;

    assertEquals(expectedLength, optimalLength);
  }
}
