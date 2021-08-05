package com.jacketing.algorithm.impl;

import com.jacketing.TestUtil;
import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.parsing.impl.structures.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListSchedulerTest {
  @Test
  public void testSchedule() {
    Graph graph = TestUtil.graphVariantOne();
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);

    SchedulingAlgorithmStrategy schedulingAlgorithmStrategy = SchedulingAlgorithmStrategy.create(
      new ListScheduler(graph, programContext, ScheduleFactory.create())
    );

    Schedule schedule = schedulingAlgorithmStrategy.schedule();
    schedule.getDuration();

    assertEquals(10, schedule.getDuration());
  }
}
