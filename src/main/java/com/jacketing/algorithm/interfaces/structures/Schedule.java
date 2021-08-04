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

package com.jacketing.algorithm.interfaces.structures;

import com.jacketing.algorithm.impl.structures.Task;
import java.util.ArrayList;

public interface Schedule {
  void addTask(Task task, int processor);

  int getDuration();

  int getProcessorEnd(int processor);

  int getNumberOfProcessors();

  ArrayList<Task> getAllTasks();

  Task getTaskForNode(int nodeId);

  int getProcessor(int taskId);

  Task getLastScheduledTask(int processor);

  Task getTask(int taskId);

  int getTotalScheduledTasks();

  boolean isFullyPopulated(int graphSize);
}
