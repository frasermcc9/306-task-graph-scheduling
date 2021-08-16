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

package com.jacketing.algorithm.impl.X;

import com.google.common.base.Strings;
import com.jacketing.algorithm.impl.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.common.FormattableTask;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractIterativeSchedule
  implements AlgorithmSchedule, HeuristicSchedule {

  protected final int orphans; // bitfield
  //  protected final String[] permutationStrings;

  protected final String permString;

  protected final AbstractIterativeSchedule parent;
  protected final Integer[] procCache;
  protected final Task[] taskCache;
  protected final int cacheKey;
  protected final int[] totalTime;

  protected int heuristic;
  protected int maxBottomLevel;
  protected int idleTime;

  protected boolean cacheComplete = false;
  protected int task = -1;
  protected int proc = -1;
  protected int time = -1;
  protected int duration = -1;

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
      //      new String[AbstractSchedulingAlgorithm
      //        .getCache(cacheKey)
      //        .getContext()
      //        .getProcessorsToScheduleOn()],
      AbstractSchedulingAlgorithm.getCache(cacheKey).defaultNodeString()
    );
    //    Arrays.fill(permutationStrings, "");
  }

  public AbstractIterativeSchedule(
    int orphans,
    AbstractIterativeSchedule parent,
    int[] totalTime,
    int cacheKey,
    //    String[] permutationStrings,
    String permString
  ) {
    this.orphans = orphans;
    this.parent = parent;
    this.totalTime = totalTime;
    this.cacheKey = cacheKey;
    //    this.permutationStrings = permutationStrings;
    this.permString = permString;

    taskCache =
      new IterativeSchedule.Task[getCache()
        .getGraph()
        .getAdjacencyList()
        .getNodeCount()];

    procCache =
      new Integer[getCache().getContext().getProcessorsToScheduleOn()];
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

        for (int processor = 0; processor < processors; processor++) {
          List<Integer> parentNodes = graph
            .getAdjacencyList()
            .getParentNodes(orphan);
          int startTime = 0;

          for (Integer parentNode : parentNodes) {
            Task parentTask = findTask(parentNode);
            boolean sameProcessor = processor == parentTask.processor;
            if (sameProcessor) {
              startTime =
                Math.max(startTime, parentTask.time + parentTask.duration);
            } else {
              int edge = graph.getEdgeWeight().from(parentNode).to(orphan);
              startTime =
                Math.max(
                  startTime,
                  parentTask.time + parentTask.duration + edge
                );
            }
          }
          startTime = Math.max(startTime, findProcEnd(processor));
          int taskWeight = graph.getNodeWeight(orphan);

          // task end time
          if (startTime + taskWeight >= getCache().getUpperBound()) {
            continue;
          }

          StringBuilder builder = new StringBuilder(permString);
          // char MAX = 65,534
          builder.setCharAt(orphan * 2 + 1, (char) startTime);
          String permutationId = builder.toString();

          //          String[] permutationStringsCopy = new String[processors];
          //          System.arraycopy(
          //            permutationStrings,
          //            0,
          //            permutationStringsCopy,
          //            0,
          //            processors
          //          );
          //          permutationStringsCopy[processor] =
          //            permutationStringsCopy[processor].concat("" + orphan)
          //              .concat("" + startTime);
          //
          //          String permutationId = getPermutationId(permutationStringsCopy);
          if (getCache().isPermutation(permutationId)) {
            continue;
          }
          getCache().addScheduleToCache(permutationId);

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
            //            permutationStringsCopy
            permutationId
          );
          schedule.setAddedTask(processor, startTime, orphan, taskWeight);

          schedule.maxBottomLevel =
            Math.max(this.maxBottomLevel, startTime + graph.getBLevel(orphan));
          schedule.idleTime =
            this.idleTime + startTime - findProcEnd(processor);

          schedule.heuristic = calculateHeuristic(schedule);

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

  public int calculateHeuristic(HeuristicSchedule schedule) {
    return Integer.MIN_VALUE;
  }

  public abstract AbstractIterativeSchedule buildSchedule(
    AbstractIterativeSchedule parent,
    int nextOrphans,
    int[] totalTimeArray,
    int cacheKey,
    //    String[] permutationStrings
    String permutationId
  );

  public abstract void addItem(AbstractIterativeSchedule schedule);

  //  protected String getPermutationId(String[] withStrings) {
  //    int len = permutationStrings.length;
  //    String[] copy = new String[len];
  //    System.arraycopy(withStrings, 0, copy, 0, len);
  //
  //    Arrays.sort(copy);
  //    StringBuilder sb = new StringBuilder();
  //
  //    for (int i = 0; i < len; i++) {
  //      sb.append(copy[i]);
  //    }
  //    return sb.toString();
  //  }

  public Task findTask(int task) {
    if (taskCache[task] != null) {
      return taskCache[task];
    }

    if (cacheComplete) return null;

    AbstractIterativeSchedule parent = this;
    while (parent != null && parent.task != -1) {
      taskCache[parent.task] =
        new Task(parent.proc, parent.time, parent.duration, task);

      if (procCache[parent.proc] == null) {
        procCache[parent.proc] = parent.time + parent.duration;
      }

      if (parent.task == task) {
        return new Task(parent.proc, parent.time, parent.duration, task);
      }
      parent = parent.parent;
    }
    cacheComplete = true;
    return null;
  }

  public int findProcEnd(int proc) {
    if (procCache[proc] != null) {
      return procCache[proc];
    }

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
        new IterativeSchedule.Task(
          parent.proc,
          parent.time,
          parent.duration,
          parent.task
        )
      );
      parent = parent.parent;
    }

    return tasks;
  }

  @Override
  public int getProcessor(int forTask) {
    Task task = this.findTask(forTask);
    return task.processor;
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

  protected static class Task implements FormattableTask {

    protected final int processor;
    protected final int time;
    protected final int duration;
    protected final int id;

    public Task(int processor, int time, int duration, int id) {
      this.processor = processor;
      this.time = time;
      this.duration = duration;
      this.id = id;
    }

    @Override
    public int getId() {
      return id;
    }

    @Override
    public int getStartTime() {
      return time;
    }
  }
}
