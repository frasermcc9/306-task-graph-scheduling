package com.jacketing.algorithm.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import com.jacketing.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleCoreSchedulerTest {

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  @Test
  public void schedule() {
    Graph graph = TestUtil.graphVariantOne();
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getCoresToScheduleOn()).thenReturn(1);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new SingleCoreScheduler(graph, programContext, ScheduleFactory.create())
    );

    Schedule schedule = schedulingAlgorithmStrategy.schedule();
    schedule.getDuration();

    assertEquals(10, schedule.getDuration());
  }
}
