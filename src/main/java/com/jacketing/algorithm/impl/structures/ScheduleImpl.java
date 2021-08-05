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
import com.jacketing.io.cli.AlgorithmContext;
import java.util.*;
import java.util.function.BiFunction;

public class ScheduleImpl implements Schedule {

  private final AlgorithmContext context;
  private final Map<Integer, ProcessorTaskList> processorMap;
  private final Map<Integer, Task> taskIdToTaskMap;
  private final Map<Task, Integer> inverseProcessorMap;

  public ScheduleImpl(
    AlgorithmContext context,
    Map<Integer, ProcessorTaskList> processorMap,
    Map<Integer, Task> taskIdToTaskMap
  ) {
    this.context = context;
    this.processorMap = processorMap;
    this.taskIdToTaskMap = taskIdToTaskMap;
    inverseProcessorMap = new HashMap<>();
    int numberProcessors = context.getProcessorsToScheduleOn();

    for (int i = 0; i < numberProcessors; i++) {
      processorMap.put(i, new ProcessorTaskList());
    }
  }

  public ScheduleImpl(ScheduleImpl scheduleImpl) {
    this.context = scheduleImpl.context;

    this.processorMap = new HashMap<>();
    this.inverseProcessorMap = new HashMap<>();
    for (Map.Entry<Integer, ProcessorTaskList> entry : scheduleImpl.processorMap.entrySet()) {
      this.processorMap.put(
          entry.getKey(),
          new ProcessorTaskList(entry.getValue())
        );
      for (Task task : entry.getValue()) {
        this.inverseProcessorMap.put(task, entry.getKey());
      }
    }

    this.taskIdToTaskMap = new HashMap<>();
    for (Map.Entry<Integer, Task> entry : scheduleImpl.taskIdToTaskMap.entrySet()) {
      this.taskIdToTaskMap.put(entry.getKey(), entry.getValue());
    }
  }

  public void addTask(Task task, int processor) {
    processorMap.get(processor).add(task);
    taskIdToTaskMap.put(task.getId(), task);
    inverseProcessorMap.put(task, processor);
  }

  public int getDuration() {
    return getFinishTime(true);
  }

  public int getEarliestFinishTime() {
    return getFinishTime(false);
  }

  private int getFinishTime(boolean max) {
    int dur = 0;
    BiFunction<Integer, Integer, Integer> method;
    if (max) {
      method = Math::max;
    } else {
      method = Math::min;
    }

    Collection<ProcessorTaskList> values = processorMap.values();
    for (ProcessorTaskList value : values) {
      dur = method.apply(dur, value.getLastScheduledEndTime());
    }

    return dur;
  }

  @Override
  public List<Task> getAllTasks() {
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

  @Override
  public int getProcessor(int taskId) {
    return inverseProcessorMap.get(getTask(taskId));
  }

  @Override
  public Task getLastScheduledTask(int processor) {
    if (processorMap.get(processor).isEmpty()) {
      return null;
    }
    return processorMap.get(processor).getLastScheduledTask();
  }

  @Override
  public Task getTask(int taskId) {
    return taskIdToTaskMap.get(taskId);
  }

  @Override
  public int getTotalScheduledTasks() {
    return processorMap
      .values()
      .stream()
      .mapToInt(ProcessorTaskList::size)
      .sum();
  }

  @Override
  public boolean isFullyPopulated(int graphSize) {
    return getTotalScheduledTasks() == graphSize;
  }

  @Override
  public int hashCode() {
    String[] strings = new String[this.context.getProcessorsToScheduleOn()];

    for (int i = 0; i < this.processorMap.size(); i++) {
      ProcessorTaskList list = this.processorMap.get(i);
      StringBuilder sb = new StringBuilder();
      for (Task task : list) {
        sb.append(task.getId()).append(task.getStartTime());
      }
      strings[i] = sb.toString();
    }

    Arrays.sort(strings);

    return String.join("", strings).hashCode();
  }

  @Override
  public String stringIdentifier() {
    String[] strings = new String[this.context.getProcessorsToScheduleOn()];

    for (int i = 0; i < this.processorMap.size(); i++) {
      ProcessorTaskList list = this.processorMap.get(i);
      StringBuilder sb = new StringBuilder();
      for (Task task : list) {
        sb.append(task.getId()).append(task.getStartTime());
      }
      strings[i] = sb.toString();
    }

    Arrays.sort(strings);

    return String.join("", strings);
  }
}
