package com.jacketing.algorithm.impl.structures;

import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.io.cli.ProgramContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ScheduleImpl implements Schedule {

  private final ProgramContext context;
  private final Map<Integer, TaskList> processorMap;
  private final Map<Integer, Task> taskIdToTaskMap;
  private final Map<Task, Integer> inverseProcessorMap;

  public ScheduleImpl(
    ProgramContext context,
    Map<Integer, TaskList> processorMap,
    Map<Integer, Task> taskIdToTaskMap
  ) {
    this.context = context;
    this.processorMap = processorMap;
    this.taskIdToTaskMap = taskIdToTaskMap;
    inverseProcessorMap = new HashMap<>();

    int procCount = context.getProcessorsToScheduleOn();
    for (int i = 0; i < procCount; i++) {
      processorMap.put(i, new TaskList());
    }
  }

  public ScheduleImpl(ScheduleImpl scheduleImpl) {
    this.context = scheduleImpl.context;

    this.processorMap = new HashMap<>();
    this.inverseProcessorMap = new HashMap<>();
    for (Map.Entry<Integer, TaskList> entry : scheduleImpl.processorMap.entrySet()) {
      this.processorMap.put(entry.getKey(), new TaskList(entry.getValue()));
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
    Collection<TaskList> values = processorMap.values();
    int max = 0;
    for (TaskList value : values) {
      max = Math.max(max, value.getLastScheduledEndTime());
    }
    return max;
  }

  @Override
  public int getProcessorEnd(int processor) {
    return processorMap.get(processor).getLastScheduledEndTime();
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
    return processorMap.values().stream().mapToInt(TaskList::size).sum();
  }

  @Override
  public boolean isFullyPopulated(int graphSize) {
    return getTotalScheduledTasks() == graphSize;
  }
}
