package com.jacketing.algorithm.interfaces.structures;

import com.jacketing.algorithm.impl.structures.Task;

public interface Schedule {
  void addTask(Task task, int processor);

  int getDuration();

  int getProcessorEnd(int processor);

  int getProcessor(int taskId);

  Task getLastScheduledTask(int processor);

  Task getTask(int taskId);

  int getTotalScheduledTasks();
}
