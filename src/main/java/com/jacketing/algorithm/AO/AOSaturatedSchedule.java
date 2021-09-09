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
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class AOSaturatedSchedule extends AOSchedule {

  private final OrderingSchedule delegate;
  private final int[] startTimeCache;
  private final int[] processorEndTimeCache;

  public AOSaturatedSchedule(int cacheKey, OrderingSchedule ordered) {
    super(cacheKey);
    this.delegate = ordered;

    this.startTimeCache =
      new int[getCache().getGraph().getAdjacencyList().getNodeCount()];
    Arrays.fill(startTimeCache, -1);
    this.processorEndTimeCache =
      new int[getCache().getContext().getProcessorsToScheduleOn()];
    Arrays.fill(startTimeCache, -1);
    System.out.println(this.toString());
  }

  @Override
  public void propagate() {
    throw new UnsupportedOperationException(
      "Cannot propagate complete schedule."
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
  public AOSchedule predecessor() {
    return this.delegate;
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
    Graph graph = getCache().getGraph();
    Map<Integer, List<Integer>> tasks = new HashMap<>();
    Map<Integer, List<FormattableTask>> result = new HashMap<>();

    int processorsToScheduleOn = getCache()
      .getContext()
      .getProcessorsToScheduleOn();
    for (int i = 0; i < processorsToScheduleOn; i++) {
      tasks.put(i, new LinkedList<>());
      result.put(i, new ArrayList<>());
    }

    OrderingSchedule schedule = this.delegate;
    while (schedule.getParent() != null) {
      tasks.get(schedule.getNewestProcessor()).add(0, schedule.getNewestTask());
      schedule = schedule.getParent();
    }

    for (Map.Entry<Integer, List<Integer>> taskListEntry : tasks.entrySet()) {
      List<Integer> taskList = taskListEntry.getValue();
      int processor = taskListEntry.getKey();

      List<FormattableTask> resultTaskList = result.get(processor);

      int earliestStart = 0;
      for (int task : taskList) {
        resultTaskList.add(
          new Task(processor, earliestStart, graph.getNodeWeight(task), task)
        );
        earliestStart += graph.getNodeWeight(task);
        startTimeCache[task] = earliestStart;
      }
    }

    Set<Integer> complete = new HashSet<>();
    // index of how far through each task list we are
    int[] progress = new int[processorsToScheduleOn];
    // what cpu we are currently looking at
    int pointer = 0;

    while (complete.size() != graph.getAdjacencyList().getNodeCount()) {
      int taskIndex = progress[pointer];

      List<FormattableTask> taskList = result.get(pointer);

      if (taskIndex == taskList.size()) {
        pointer = (pointer + 1) % processorsToScheduleOn;
        continue;
      }

      FormattableTask formattableTask = taskList.get(taskIndex);
      int id = formattableTask.getId();
      List<Integer> parents = graph.parentNodesFor(id);
      if (complete.containsAll(parents)) {
        progress[pointer]++;
        complete.add(id);
        int startTime = 0;
        for (int parent : parents) {
          // if same cpu
          if (tasks.get(pointer).contains(parent)) {
            startTime =
              Math.max(
                startTime,
                startTimeCache[parent] + graph.getNodeWeight(parent)
              );
          } else {
            // if not same cpu
            startTime =
              Math.max(
                startTime,
                startTimeCache[parent] +
                graph.getNodeWeight(parent) +
                graph.getEdgeWeight(parent, id)
              );
          }
        }
        if (taskIndex > 0) {
          startTime =
            Math.max(
              startTime,
              taskList.get(taskIndex - 1).getStartTime() +
              taskList.get(taskIndex - 1).getDuration()
            );
        }
        startTimeCache[id] = startTime;
        formattableTask.setStartTime(startTime);
      }

      pointer = (pointer + 1) % processorsToScheduleOn;
    }

    return result
      .values()
      .stream()
      .flatMap(List::stream)
      .collect(Collectors.toList());
  }

  private int earliestStartTime(int ofTask) {
    if (startTimeCache[ofTask] != -1) {
      return startTimeCache[ofTask];
    }

    Graph graph = getCache().getGraph();
    List<Integer> parents = graph.parentNodesFor(ofTask);

    int startTime = 0;
    int thisProcessor = getProcessor(ofTask);
    for (int parent : parents) {
      int parentProcessor = getProcessor(parent);

      int parentEnd = earliestStartTime(parent) + graph.getNodeWeight(parent);
      if (thisProcessor == parentProcessor) {
        startTime = Math.max(startTime, parentEnd);
      } else {
        startTime =
          Math.max(startTime, parentEnd + graph.getEdgeWeight(parent, ofTask));
      }
    }
    startTime = Math.max(startTime, processorEndTimeCache[thisProcessor]);

    startTimeCache[ofTask] = startTime;
    processorEndTimeCache[thisProcessor] =
      Math.max(
        processorEndTimeCache[thisProcessor],
        startTime + graph.getNodeWeight(ofTask)
      );

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
