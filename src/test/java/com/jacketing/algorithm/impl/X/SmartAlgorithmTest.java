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

import static org.mockito.Mockito.*;

import com.jacketing.TestUtil;
import com.jacketing.algorithm.impl.algorithms.DepthFirstScheduler;
import com.jacketing.algorithm.impl.algorithms.suboptimal.IndependentScheduler;
import com.jacketing.algorithm.impl.algorithms.suboptimal.ListScheduler;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
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
    verify(algorithmSource, times(1)).getDfs();
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

    verify(algorithmSource, times(1)).getDfs();
    verify(algorithmSource, times(1)).getList();
  }
}
