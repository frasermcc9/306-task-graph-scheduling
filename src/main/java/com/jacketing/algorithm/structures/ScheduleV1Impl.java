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

package com.jacketing.algorithm.structures;

import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.common.FormattableSchedule;
import com.jacketing.common.FormattableTask;
import com.jacketing.io.cli.AlgorithmContext;
import java.util.*;
import java.util.function.BiFunction;

public class ScheduleV1Impl
  implements ScheduleV1, FormattableSchedule, AlgorithmSchedule {

  private final AlgorithmContext context;
  private final Map<Integer, ProcessorTaskList> processorMap;
  private final Map<Integer, Task> taskIdToTaskMap;
  private final Map<Task, Integer> inverseProcessorMap;
  private final boolean cloned;

  private Task newTask;
  private int newProc;

  public ScheduleV1Impl(
    AlgorithmContext context,
    Map<Integer, ProcessorTaskList> processorMap,
    Map<Integer, Task> taskIdToTaskMap
  ) {
    this.context = context;
    this.processorMap = processorMap;
    this.taskIdToTaskMap = taskIdToTaskMap;

    inverseProcessorMap = new HashMap<>();
    for (Map.Entry<Integer, ProcessorTaskList> entry : this.processorMap.entrySet()) {
      for (Task task : entry.getValue()) {
        inverseProcessorMap.put(task, entry.getKey());
      }
    }

    int numberProcessors = context.getProcessorsToScheduleOn();
    cloned = false;
  }

  public ScheduleV1Impl(ScheduleV1Impl scheduleImpl) {
    this.context = scheduleImpl.context;

    this.processorMap = scheduleImpl.processorMap;
    this.inverseProcessorMap = scheduleImpl.inverseProcessorMap;
    this.taskIdToTaskMap = scheduleImpl.taskIdToTaskMap;
    cloned = true;
  }

  public ScheduleV1Impl clone() {
    Map<Integer, ProcessorTaskList> processorMap = new HashMap<>();
    Map<Task, Integer> inverseProcessorMap = new HashMap<>();
    for (Map.Entry<Integer, ProcessorTaskList> entry : this.processorMap.entrySet()) {
      processorMap.put(entry.getKey(), new ProcessorTaskList(entry.getValue()));
      for (Task task : entry.getValue()) {
        inverseProcessorMap.put(task, entry.getKey());
      }
    }

    Map<Integer, Task> taskIdToTaskMap = new HashMap<>();
    for (Map.Entry<Integer, Task> entry : this.taskIdToTaskMap.entrySet()) {
      taskIdToTaskMap.put(entry.getKey(), entry.getValue());
    }

    return new ScheduleV1Impl(this.context, processorMap, taskIdToTaskMap);
  }

  public void addTask(Task task, int processor) {
    if (cloned) {
      newTask = task;
      newProc = processor;
    }
    processorMap.get(processor).add(task);
    taskIdToTaskMap.put(task.getId(), task);
    inverseProcessorMap.put(task, processor);
  }

  public int getDuration() {
    return getFinishTime(true);
  }

  @Override
  public void propagate() {
    throw new UnsupportedOperationException();
  }

  public int getEarliestFinishTime() {
    return getFinishTime(false);
  }

  private int getFinishTime(boolean max) {
    int dur;
    BiFunction<Integer, Integer, Integer> method;
    if (max) {
      method = Math::max;
      dur = 0;
    } else {
      method = Math::min;
      dur = Integer.MAX_VALUE;
    }

    Collection<ProcessorTaskList> values = processorMap.values();
    for (ProcessorTaskList value : values) {
      dur = method.apply(dur, value.getLastScheduledEndTime());
    }

    return dur;
  }

  @Override
  public List<FormattableTask> getAllTasks() {
    // Convert map of lists, to one list
    Collection<ProcessorTaskList> processorProcessorTaskLists = processorMap.values();
    List<FormattableTask> allTasks = new ArrayList<>();
    for (ProcessorTaskList processorTaskList : processorProcessorTaskLists) {
      allTasks.addAll(processorTaskList);
    }
    return allTasks;
  }

  @Override
  public Task getTaskForNode(int nodeId) {
    return taskIdToTaskMap.get(nodeId);
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

  public void revert() {
    if (!cloned) {
      return;
    }

    processorMap.get(newProc).remove(newTask);
    taskIdToTaskMap.remove(newTask.getId());
    inverseProcessorMap.remove(newTask);
  }

  @Override
  public Map<Integer, ProcessorTaskList> getProcessorMap() {
    return this.processorMap;
  }
}
