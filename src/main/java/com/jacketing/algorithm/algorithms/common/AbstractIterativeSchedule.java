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

package com.jacketing.algorithm.algorithms.common;

import com.google.common.base.Strings;
import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.algorithms.common.cache.PermutationId;
import com.jacketing.algorithm.algorithms.common.cache.StaticCache;
import com.jacketing.common.FormattableTask;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractIterativeSchedule
  implements AlgorithmSchedule, HeuristicSchedule {

  protected final int orphans; // bitfield

  protected final PermutationId permutationId;
  protected final AbstractIterativeSchedule parent;
  protected final int cacheKey;
  protected final int[] totalTime;

  protected int heuristic;
  protected int maxBottomLevel;
  protected int idleTime;

  protected int task = -1;
  protected int proc = -1;
  protected int time = -1;
  protected int duration = -1;

  /**
   * Initial
   *
   * @param orphans
   * @param parent
   * @param cacheKey
   */
  public AbstractIterativeSchedule(
    int orphans,
    AbstractIterativeSchedule parent,
    int cacheKey
  ) {
    this(
      orphans,
      parent,
      new int[AbstractSchedulingAlgorithm
        .getCache(cacheKey)
        .getContext()
        .getProcessorsToScheduleOn()],
      cacheKey,
      AbstractSchedulingAlgorithm.getCache(cacheKey).defaultNodeString()
    );
  }

  /**
   * From another schedule
   *
   * @param orphans
   * @param parent
   * @param totalTime
   * @param cacheKey
   * @param permutationId
   */
  public AbstractIterativeSchedule(
    int orphans,
    AbstractIterativeSchedule parent,
    int[] totalTime,
    int cacheKey,
    PermutationId permutationId
  ) {
    this.orphans = orphans;
    this.parent = parent;
    this.totalTime = totalTime;
    this.cacheKey = cacheKey;
    this.permutationId = permutationId;
  }

  public void updateHeuristics(int startTime, int bLevelOfNext, int processor) {
    this.maxBottomLevel =
      Math.max(this.maxBottomLevel, startTime + bLevelOfNext);
    this.idleTime = this.idleTime + startTime - findProcEnd(processor);

    this.heuristic = calculateHeuristic(this);
  }

  public int getTotalTime() {
    int max = totalTime[0];
    int len = totalTime.length;
    for (int i = 1; i < len; i++) {
      int time = totalTime[i];
      if (time > max) {
        max = time;
      }
    }
    return max;
  }

  public void setAddedTask(int proc, int time, int task, int duration) {
    this.proc = proc;
    this.time = time;
    this.task = task;
    this.duration = duration;
  }

  public boolean saturated() {
    return orphans == 0;
  }

  public void propagate() {
    int processors = getCache().getContext().getProcessorsToScheduleOn();
    Graph graph = getCache().getGraph();

    int orphansCopy = orphans;
    for (int orphan = 0; orphansCopy != 0; ++orphan, orphansCopy >>>= 1) {
      if ((orphansCopy & 1) != 0) {
        int nextOrphans = orphans;
        nextOrphans &= ~(1 << orphan);

        // Ensure we only schedule on an empty processor once
        boolean processorInit = false;

        for (int processor = 0; processor < processors; processor++) {
          List<Integer> parentNodes = graph
            .getAdjacencyList()
            .getParentNodes(orphan);
          int startTime = 0;

          for (Integer parentNode : parentNodes) {
            Task parentTask = findTask(parentNode);
            boolean sameProcessor = processor == parentTask.getProcessor();
            if (sameProcessor) {
              startTime =
                Math.max(
                  startTime,
                  parentTask.getTime() + parentTask.getDuration()
                );
            } else {
              int edge = graph.getEdgeWeight(parentNode, orphan);
              startTime =
                Math.max(
                  startTime,
                  parentTask.getTime() + parentTask.getDuration() + edge
                );
            }
          }
          int processorEnd = findProcEnd(processor);
          startTime = Math.max(startTime, processorEnd);

          if (processorEnd == 0 && processorInit) {
            continue;
          }

          if (processorEnd == 0) {
            processorInit = true;
          }

          int taskWeight = graph.getNodeWeight(orphan);

          int bLevelOfNext = graph.getBLevel(orphan);

          if (startTime + bLevelOfNext >= getCache().getUpperBound()) {
            continue;
          }

          PermutationId nextId = permutationId.updatePermutation(
            orphan,
            startTime,
            processor
          );

          if (getCache().isPermutation(nextId)) {
            continue;
          }
          getCache().addScheduleToCache(nextId);

          List<Integer> childNodes = graph
            .getAdjacencyList()
            .getChildNodes(orphan);

          outer:for (int child : childNodes) {
            List<Integer> parentsForChild = graph
              .getAdjacencyList()
              .getParentNodes(child);
            for (int childParent : parentsForChild) {
              if (childParent == orphan) {
                continue;
              }
              Task task = findTask(childParent);
              if (task == null) continue outer;
            }
            nextOrphans |= (1 << child);
          }

          int[] totalTimeCopy = new int[processors];
          System.arraycopy(totalTime, 0, totalTimeCopy, 0, processors);
          totalTimeCopy[processor] = startTime + taskWeight;

          AbstractIterativeSchedule schedule = buildSchedule(
            this,
            nextOrphans,
            totalTimeCopy,
            cacheKey,
            nextId
          );
          schedule.setAddedTask(processor, startTime, orphan, taskWeight);

          schedule.updateHeuristics(startTime, bLevelOfNext, processor);

          addItem(schedule);
        }
      }
    }
  }

  @Override
  public int getMaxBottomLevel() {
    return this.maxBottomLevel;
  }

  @Override
  public int getIdleTime() {
    return 0;
  }

  @Override
  public int calculateHeuristic(HeuristicSchedule schedule) {
    return Integer.MIN_VALUE;
  }

  public abstract AbstractIterativeSchedule buildSchedule(
    AbstractIterativeSchedule parent,
    int nextOrphans,
    int[] totalTimeArray,
    int cacheKey,
    PermutationId permutationId
  );

  public abstract void addItem(AbstractIterativeSchedule schedule);

  public Task findTask(int task) {
    AbstractIterativeSchedule parent = this;
    while (parent != null && parent.task != -1) {
      if (parent.task == task) {
        return new Task(parent.proc, parent.time, parent.duration, task);
      }
      parent = parent.parent;
    }
    return null;
  }

  public int findProcEnd(int proc) {
    AbstractIterativeSchedule parent = this;
    while (parent != null) {
      if (parent.proc == proc) {
        return parent.time + parent.duration;
      }
      parent = parent.parent;
    }
    return 0;
  }

  @Override
  public List<FormattableTask> getAllTasks() {
    AbstractIterativeSchedule parent = this;
    List<FormattableTask> tasks = new ArrayList<>();
    while (parent != null && parent.proc != -1) {
      tasks.add(
        new Task(parent.proc, parent.time, parent.duration, parent.task)
      );
      parent = parent.parent;
    }

    return tasks;
  }

  @Override
  public int getProcessor(int forTask) {
    Task task = this.findTask(forTask);
    return task.getProcessor();
  }

  @Override
  public int getNumberOfProcessors() {
    return getCache().getContext().getProcessorsToScheduleOn();
  }

  public void printGantt() {
    try {
      int processorCount = getCache().getContext().getProcessorsToScheduleOn();
      AbstractIterativeSchedule schedule = this;
      String[] schedules = new String[processorCount];

      for (int i = 0; i < processorCount; i++) {
        int repeatCount = this.getTotalTime();
        schedules[i] = Strings.repeat("_", repeatCount);
      }

      while (schedule != null && schedule.proc != -1) {
        int processor = schedule.proc;
        int duration = schedule.duration;
        int id = schedule.task;
        int start = schedule.time;

        String current = schedules[processor];

        int len = Math.max(0, duration - 2);

        current =
          current.substring(0, start) +
          "|" +
          Strings.repeat(id + "", len) +
          "|" +
          current.substring(start + duration);

        schedules[processor] = current;
        schedule = schedule.parent;
      }

      System.out.println("BEGIN_SCHEDULE");
      for (String s : schedules) {
        System.out.println("[" + s + "]");
      }
      System.out.println("END_SCHEDULE\n");
    } catch (Exception ignored) {}
  }

  public void printDetail() {
    AbstractIterativeSchedule schedule = this;
    System.out.println("BEGIN_SCHEDULE");
    while (schedule != null) {
      System.out.println(schedule.toString());
      schedule = schedule.parent;
    }
    System.out.println("END_SCHEDULE\n");
  }

  @Override
  public String toString() {
    return (
      "IterativeSchedule{" +
      "task=" +
      task +
      ", proc=" +
      proc +
      ", time=" +
      time +
      ", duration=" +
      duration +
      '}'
    );
  }

  @Override
  public int getDuration() {
    return this.getTotalTime();
  }

  protected StaticCache getCache() {
    return AbstractSchedulingAlgorithm.getCache(cacheKey);
  }

  protected String getPermutationId(String[] withStrings) {
    int len = getCache().getContext().getProcessorsToScheduleOn();
    String[] copy = new String[len];
    System.arraycopy(withStrings, 0, copy, 0, len);

    Arrays.sort(copy);
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < len; i++) {
      sb.append(copy[i]);
    }
    return sb.toString();
  }
}
