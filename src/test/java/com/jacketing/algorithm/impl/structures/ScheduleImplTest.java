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

package com.jacketing.algorithm.impl.structures;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.io.cli.ProgramContext;
import org.junit.Before;
import org.junit.Test;

public class ScheduleImplTest {

  Schedule schedule;

  @Before
  public void setUp() {
    AlgorithmContext context = mock(ProgramContext.class);
    when(context.getProcessorsToScheduleOn()).thenReturn(2);

    schedule = ScheduleFactory.create().newSchedule(context);

    Task task = new Task(0, 3, 0);
    Task task1 = new Task(0, 2, 1);
    schedule.addTask(task, 0);
    schedule.addTask(task1, 1);
  }

  @Test
  public void addTask() {
    assertEquals(2, schedule.getAllTasks().size());
  }

  @Test
  public void getDuration() {
    assertEquals(3, schedule.getDuration());
  }

  @Test
  public void getEarliestFinishTime() {
    assertEquals(2, schedule.getEarliestFinishTime());
  }

  @Test
  public void getAllTasks() {
    assertEquals(2, schedule.getEarliestFinishTime());
  }

  @Test
  public void getTaskForNode() {
    Task taskForNode = schedule.getTaskForNode(0);
    assertEquals(3, taskForNode.getDuration());
  }

  @Test
  public void getProcessorEnd() {
    int processorEnd = schedule.getProcessorEnd(0);
    assertEquals(3, processorEnd);
  }

  @Test
  public void getNumberOfProcessors() {
    int numberOfProcessors = schedule.getNumberOfProcessors();
    assertEquals(2, numberOfProcessors);
  }

  @Test
  public void getProcessor() {
    int processor = schedule.getProcessor(1);
    assertEquals(1, processor);
  }

  @Test
  public void getLastScheduledTask() {
    Task lastScheduledTask = schedule.getLastScheduledTask(0);
    assertEquals(0, lastScheduledTask.getId());
  }

  @Test
  public void getLastScheduledTaskOfEmpty() {
    AlgorithmContext context = mock(ProgramContext.class);
    when(context.getProcessorsToScheduleOn()).thenReturn(2);

    schedule = ScheduleFactory.create().newSchedule(context);

    Task lastScheduledTask = schedule.getLastScheduledTask(1);
    assertNull(lastScheduledTask);
  }

  @Test
  public void getTask() {
    Task task = schedule.getTask(0);
    assertEquals(3, task.getDuration());
  }

  @Test
  public void getTotalScheduledTasks() {
    int totalScheduledTasks = schedule.getTotalScheduledTasks();
    assertEquals(2, totalScheduledTasks);
  }

  @Test
  public void isFullyPopulated() {
    boolean fullyPopulated = schedule.isFullyPopulated(2);
    assertTrue(fullyPopulated);
  }

  @Test
  public void testNotFullyPopulated() {
    boolean fullyPopulated = schedule.isFullyPopulated(3);
    assertFalse(fullyPopulated);
  }

  @Test
  public void testHashCode() {
    int hashcode = schedule.hashCode();
    assertEquals(1477663, hashcode);
  }

  @Test
  public void stringIdentifier() {
    String stringId = schedule.stringIdentifier();
    assertEquals("0010", stringId);
  }
}
