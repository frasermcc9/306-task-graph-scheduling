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

import static org.mockito.Mockito.*;

import com.jacketing.TestUtil;
import com.jacketing.algorithm.algorithms.SchedulingAlgorithmStrategy;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import org.junit.Test;

public class AlgorithmLoaderTest {

  @Test
  public void testLoadingAlgorithm() {
    Graph graph = TestUtil.graphVariantOne();

    ProgramContext context = mock(ProgramContext.class);
    SchedulingAlgorithmStrategy algo = mock(SchedulingAlgorithmStrategy.class);
    when(context.getProcessorsToScheduleOn()).thenReturn(2);
    when(algo.withObservable(null)).thenReturn(algo);

    AlgorithmLoader
      .create(graph, context, (unused, unused2, unused3) -> algo)
      .load();

    verify(algo, times(1)).schedule();
    verify(algo, times(1)).withObservable(null);
  }
}
