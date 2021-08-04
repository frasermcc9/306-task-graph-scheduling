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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ScheduleImpl implements Schedule {

  private final Map<Integer, ProcessorTaskList> processorMap;

  public ScheduleImpl(int numberProcessors) {
    this.processorMap = new HashMap<>();

    for (int i = 0; i < numberProcessors; i++) {
      processorMap.put(i, new ProcessorTaskList());
    }
  }

  public void addTask(Task task) {
    processorMap.get(task.getProcessorNumber()).add(task);
  }

  public int getDuration() {
    return processorMap
      .values()
      .stream()
      .mapToInt(ProcessorTaskList::getLastScheduledEndTime)
      .max()
      .orElse(0);
  }

  @Override
  public ArrayList<Task> getAllTasks() {
    // Convert map of lists, to one list
    Collection<ProcessorTaskList> processorProcessorTaskLists = processorMap.values();
    ArrayList<Task> allTasks = new ArrayList<>();
    for (ProcessorTaskList processorTaskList : processorProcessorTaskLists) {
      allTasks.addAll(processorTaskList);
    }
    return allTasks;
  }

  @Override
  public Task getTaskForNode(int nodeId) {
    for (Task task : getAllTasks()) {
      if (task.getId() == nodeId) {
        return task;
      }
    }
    return null;
  }

  @Override
  public int getProcessorEnd(int processor) {
    return processorMap.get(processor).getLastScheduledEndTime();
  }

  public int getNumberOfProcessors() {
    return processorMap.size();
  }
}
