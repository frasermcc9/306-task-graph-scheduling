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

package com.jacketing.io.output;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jacketing.TestUtil;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.common.Loader;
import com.jacketing.io.cli.IOContext;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.io.output.format.DotFileFormatter;
import com.jacketing.io.output.saver.ConsoleFileSaver;
import com.jacketing.parsing.impl.structures.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OutputLoaderTest {

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  @Test
  public void testOutputLoader() {
    IOContext context = mock(ProgramContext.class);

    when(context.getOutputName()).thenReturn(null);

    Graph graph = TestUtil.graphVariantOne();
    AlgorithmSchedule schedule = TestUtil.graphVariantOneSchedule2C();

    Loader<Void> voidLoader = OutputLoader.create(
      schedule,
      context,
      graph,
      ConsoleFileSaver::new,
      DotFileFormatter::new
    );

    voidLoader.load();
  }
}
