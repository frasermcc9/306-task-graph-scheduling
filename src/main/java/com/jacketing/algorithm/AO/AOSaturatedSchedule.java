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

package com.jacketing.algorithm.AO;

import com.jacketing.algorithm.algorithms.common.Task;
import com.jacketing.common.FormattableTask;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AOSaturatedSchedule extends AOSchedule {

  private final OrderingSchedule delegate;
  private final int[] startTimeCache;

  public AOSaturatedSchedule(int cacheKey, OrderingSchedule ordered) {
    super(cacheKey);
    this.delegate = ordered;

    this.startTimeCache =
      new int[getCache().getGraph().getAdjacencyList().getNodeCount()];
    Arrays.fill(startTimeCache, -1);
  }

  @Override
  public void propagate() {
    throw new UnsupportedOperationException(
      "Cannot propagate complete " + "schedule."
    );
  }

  @Override
  public boolean saturated() {
    return true;
  }

  @Override
  public int calculateHeuristic() {
    return delegate.calculateHeuristic();
  }

  @Override
  public AOSchedule upgrade() {
    throw new UnsupportedOperationException("Cannot upgrade complete schedule");
  }

  @Override
  public String toString() {
    String[] strings = new String[getNumberOfProcessors()];
    Arrays.fill(strings, "");
    OrderingSchedule current = delegate;

    while (current.getParent() != null) {
      Task taskAndProcessor = current.getFormattable();
      strings[taskAndProcessor.getProcessor()] += taskAndProcessor.getId();
      current = current.getParent();
    }

    Collections.reverse(Arrays.asList(strings));
    for (int i = 0; i < strings.length; i++) {
      strings[i] = new StringBuilder(strings[i]).reverse().toString();
    }
    return Arrays.toString(strings);
  }

  @Override
  public int getDuration() {
    List<FormattableTask> allTasks = getAllTasks();
    int duration = 0;
    for (FormattableTask allTask : allTasks) {
      int endTime = allTask.getStartTime() + allTask.getDuration();
      duration = Math.max(duration, endTime);
    }
    return duration;
  }

  @Override
  public List<FormattableTask> getAllTasks() {
    List<FormattableTask> tasks = new ArrayList<>();
    Graph graph = getCache().getGraph();
    List<Integer> nodeIds = graph.getAdjacencyList().getNodeIds();
    for (int nodeId : nodeIds) {
      int est = earliestStartTime(nodeId);
      tasks.add(
        new Task(getProcessor(nodeId), est, graph.getNodeWeight(nodeId), nodeId)
      );
    }
    return tasks;
  }

  private int earliestStartTime(int ofTask) {
    if (startTimeCache[ofTask] != -1) {
      return startTimeCache[ofTask];
    }

    Graph graph = getCache().getGraph();
    List<Integer> parents = graph.parentNodesFor(ofTask);

    int startTime = 0;
    for (int parent : parents) {
      int thisProcessor = getProcessor(ofTask);
      int parentProcessor = getProcessor(parent);

      int parentEST = earliestStartTime(parent);
      if (thisProcessor == parentProcessor) {
        startTime =
          Math.max(startTime, parentEST + graph.getNodeWeight(parent));
      } else {
        startTime =
          Math.max(
            startTime,
            parentEST +
            graph.getNodeWeight(parent) +
            graph.getEdgeWeight(parent, ofTask)
          );
      }
    }

    startTimeCache[ofTask] = startTime;
    return startTime;
  }

  @Override
  public int getProcessor(int forTask) {
    return this.delegate.getProcessor(forTask);
  }

  @Override
  public int getNumberOfProcessors() {
    return this.delegate.getNumberOfProcessors();
  }

  @Override
  public int compareTo(@NotNull AOSchedule o) {
    return this.delegate.compareTo(o);
  }
}
