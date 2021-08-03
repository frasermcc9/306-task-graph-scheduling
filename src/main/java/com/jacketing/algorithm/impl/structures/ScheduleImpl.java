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

import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.io.cli.ProgramContext;
import java.util.Map;

public class ScheduleImpl implements Schedule {

  private final ProgramContext context;
  private final Map<Integer, TaskList> processorMap;

  public ScheduleImpl(
    ProgramContext context,
    Map<Integer, TaskList> processorMap
  ) {
    this.context = context;
    this.processorMap = processorMap;

    int procCount = context.getProcessorsToScheduleOn();
    for (int i = 0; i < procCount; i++) {
      processorMap.put(i, new TaskList());
    }
  }

  public void addTask(Task task, int processor) {
    processorMap.get(processor).add(task);
  }

  public int getDuration() {
    return processorMap
      .values()
      .stream()
      .mapToInt(TaskList::getLastScheduledEndTime)
      .max()
      .orElse(0);
  }

  @Override
  public int getProcessorEnd(int processor) {
    return processorMap.get(processor).getLastScheduledEndTime();
  }
}
