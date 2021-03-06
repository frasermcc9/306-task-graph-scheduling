package com.jacketing.algorithm.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jacketing.TestUtil;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.algorithm.structures.ScheduleV1;
import com.jacketing.algorithm.structures.Task;
import com.jacketing.io.cli.ProgramContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScheduleV1ValidationServiceTest {

  ScheduleValidationService validationService;

  @Before
  public void setUp() throws Exception {
    validationService = new ScheduleValidationService();
  }

  @After
  public void tearDown() throws Exception {}

  public ScheduleV1 getValidScheduleG1() {
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);
    ScheduleV1 schedule = ScheduleFactory.create().newSchedule(programContext);

    schedule.addTask(new Task(0, 2, 0), 0);
    schedule.addTask(new Task(2, 3, 1), 0);
    schedule.addTask(new Task(4, 3, 2), 1);
    schedule.addTask(new Task(7, 2, 3), 1);
    return schedule;
  }

  public ScheduleV1 getInvalidStartTimeScheduleG1() {
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);
    ScheduleV1 schedule = ScheduleFactory.create().newSchedule(programContext);

    schedule.addTask(new Task(0, 2, 0), 0);
    schedule.addTask(new Task(2, 3, 1), 0);
    schedule.addTask(new Task(2, 3, 2), 1);
    schedule.addTask(new Task(7, 2, 3), 1);
    return schedule;
  }

  public ScheduleV1 getInvalidTaskOrderScheduleG1() {
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);
    ScheduleV1 schedule = ScheduleFactory.create().newSchedule(programContext);

    schedule.addTask(new Task(3, 2, 0), 0);
    schedule.addTask(new Task(0, 3, 1), 0);
    schedule.addTask(new Task(10, 3, 2), 1);
    schedule.addTask(new Task(20, 2, 3), 1);
    return schedule;
  }

  @Test
  public void validateScheduleCorrect() {
    boolean isValid = validationService.validateSchedule(
      getValidScheduleG1(),
      TestUtil.graphVariantOne(),
      2
    );
    assertTrue(isValid);
  }

  @Test
  public void validateScheduleInvalidStartTime() {
    boolean isValid = validationService.validateSchedule(
      getInvalidStartTimeScheduleG1(),
      TestUtil.graphVariantOne(),
      2
    );
    assertFalse(isValid);
  }

  @Test
  public void validateScheduleInvalidTaskOrder() {
    boolean isValid = validationService.validateSchedule(
      getInvalidTaskOrderScheduleG1(),
      TestUtil.graphVariantOne(),
      2
    );
    assertFalse(isValid);
  }

  @Test
  public void validateInvalidScheduleInvalidProcessorCount() {
    assertFalse(
      validationService.validateSchedule(
        getValidScheduleG1(),
        TestUtil.graphVariantOne(),
        200
      )
    );
    assertFalse(
      validationService.validateSchedule(
        getValidScheduleG1(),
        TestUtil.graphVariantOne(),
        0
      )
    );
    assertFalse(
      validationService.validateSchedule(
        getValidScheduleG1(),
        TestUtil.graphVariantOne(),
        -1
      )
    );
  }
}
