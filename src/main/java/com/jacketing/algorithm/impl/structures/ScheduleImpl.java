package com.jacketing.algorithm.impl.structures;

import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.io.cli.ProgramContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScheduleImpl implements Schedule {

  private final ProgramContext context;
  private final Map<Integer, TaskList> processorMap;
  private final Map<Integer, Integer> taskIdToEndTimeMap;

  public ScheduleImpl(
    ProgramContext context,
    Map<Integer, TaskList> processorMap,
    Map<Integer, Integer> taskIdToEndTimeMap
  ) {
    this.context = context;
    this.processorMap = processorMap;
    this.taskIdToEndTimeMap = taskIdToEndTimeMap;

    int procCount = context.getCoresToScheduleOn();
    for (int i = 0; i < procCount; i++) {
      processorMap.put(i, new TaskList());
    }
  }

  public ScheduleImpl(ScheduleImpl scheduleImpl){
    this.context = scheduleImpl.context;

    this.processorMap = new HashMap<>();
    for (Map.Entry<Integer, TaskList> entry : scheduleImpl.processorMap.entrySet()){
      this.processorMap.put(entry.getKey(), new TaskList(entry.getValue()));
    }
    this.taskIdToEndTimeMap = new HashMap<>();
    for (Map.Entry<Integer, Integer> entry : scheduleImpl.taskIdToEndTimeMap.entrySet()){
      this.taskIdToEndTimeMap.put(entry.getKey(), new Integer(entry.getValue()));
    }
  }

  public void addTask(Task task, int processor) {
    processorMap.get(processor).add(task);
    taskIdToEndTimeMap.put(task.getId(), task.getEndTime());
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

  @Override
  public Task getLastScheduledTask(int processor){
    if (processorMap.get(processor).isEmpty()) {
      return null;
    }
    return processorMap.get(processor).getLastScheduledTask();
  }

  @Override
  public int getEndTime(int taskId) {
    return taskIdToEndTimeMap.get(taskId);
  }
}
