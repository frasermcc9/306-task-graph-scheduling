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

import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.common.FormattableTask;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.Arrays;
import java.util.List;

public abstract class AllocationSchedule
  extends AOSchedule
  implements AlgorithmSchedule, Comparable<AOSchedule> {

  protected final int maxNode;

  private final AllocationSchedule parent;
  private final int processorIndex;
  private final int task;
  private int[] bitfieldRepresentation;
  private int processor;

  private int[] loadHeuristic;

  private int acpHeuristic;
  private int tla;

  private int heuristic;

  public AllocationSchedule(
    AllocationSchedule parent,
    int task,
    int maxNode,
    int cacheKey,
    int processorIndex
  ) {
    super(cacheKey);
    this.parent = parent;
    this.task = task;
    this.maxNode = maxNode;
    this.processorIndex = processorIndex;

    this.loadHeuristic = new int[getNumberOfProcessors()];
    this.acpHeuristic = 0;
    this.heuristic = 0;
  }

  public abstract void addSchedule(AOSchedule toAdd);

  public abstract OrderingSchedule upgrade();

  public abstract AllocationSchedule build(
    AllocationSchedule parent,
    int nodeIndex,
    int processorIndex
  );

  @Override
  public void propagate() {
    if (task == maxNode - 1) {
      addSchedule(upgrade());
      //System.out.println(toString());
      return;
    }

    Graph graph = getCache().getGraph();

    int maxProcessorCount = getNumberOfProcessors();
    int processorsToSchedule = Math.min(maxProcessorCount, processorIndex + 1);
    int nextTask = task + 1;
    for (int i = 0; i < processorsToSchedule; i++) {
      AllocationSchedule next = build(this, nextTask, i + 1);
      next.processor = i;

      int[] nextLoads = new int[maxProcessorCount];
      System.arraycopy(loadHeuristic, 0, nextLoads, 0, nextLoads.length);
      nextLoads[i] += getCache().getGraph().getNodeWeight(nextTask);

      int nextTla = 0;

      List<Integer> parentNodes = graph
        .getAdjacencyList()
        .getParentNodes(nextTask);

      for (int parentNode : parentNodes) {
        if (processorParityCheck(nextTask, i)) {
          // if parent on same processor
          nextTla =
            Math.max(
              nextTla,
              graph.getTLevel(parentNode) + graph.getNodeWeight(parentNode)
            );
        } else {
          // if different processor
          nextTla =
            Math.max(
              nextTla,
              getTla(parentNode) +
              graph.getEdgeWeight(parentNode, nextTask) +
              graph.getNodeWeight(parentNode)
            );
        }
      }
      next.tla = nextTla;
      int nextAcp = nextTla + graph.getBLevel(nextTask);

      next.setHeuristics(nextLoads, Math.max(this.acpHeuristic, nextAcp));

      if (next.heuristic > getCache().getBestSchedule().getDuration()) {
        continue;
      }

      addSchedule(next);
    }
  }

  public boolean processorParityCheck(int forTask, int processor) {
    AllocationSchedule schedule = this;

    // Nodes are scheduled in standard integer ordering, so if the parent has
    // a greater int value, it cannot be scheduled yet.
    if (forTask > this.task) {
      // count unscheduled as on same processor
      return true;
    }

    while (schedule != null) {
      if (schedule.task == forTask) {
        return schedule.processor == processor;
      }
      schedule = schedule.parent;
    }
    return true;
  }

  public int getTla(int forNode) {
    AllocationSchedule schedule = this;

    // Nodes are scheduled in standard integer ordering, so if the parent has
    // a greater int value, it cannot be scheduled yet.
    if (forNode > this.task) {
      return 0;
    }

    while (schedule != null) {
      if (schedule.task == forNode) {
        return schedule.tla;
      }
      schedule = schedule.parent;
    }
    return 0;
  }

  /**
   * Returns whether this is a complete schedule. Since the allocation phase can
   * never be a complete schedule, this always will return false;
   *
   * @return false
   */
  @Override
  public boolean saturated() {
    return false;
  }

  protected int[] getProcessorBitfields() {
    if (this.bitfieldRepresentation != null) {
      return this.bitfieldRepresentation;
    }

    int[] processors = new int[getNumberOfProcessors()];
    AllocationSchedule iterator = this;
    while (iterator.parent != null) {
      int task = iterator.task;
      int processor = iterator.processor;
      int existing = processors[processor];
      existing |= 1 << task;
      processors[processor] = existing;

      iterator = iterator.parent;
    }
    this.bitfieldRepresentation = processors;
    return processors;
  }

  public int[] asBitfield() {
    return this.bitfieldRepresentation;
  }

  public int[] getLoads() {
    return this.loadHeuristic;
  }

  public int getLargestLoad() {
    return highestInArray(getLoads());
  }

  private void setHeuristics(int[] loads, int acp) {
    this.loadHeuristic = loads;
    this.acpHeuristic = acp;

    this.heuristic = Math.max(acpHeuristic, highestInArray(loadHeuristic));
  }

  private int highestInArray(int[] array) {
    int max = array[0];
    for (int i = 1; i < array.length; i++) {
      max = Math.max(max, array[i]);
    }
    return max;
  }

  public String toString() {
    AllocationSchedule iterator = this;
    String[] format = new String[getNumberOfProcessors()];
    Arrays.fill(format, "");
    while (iterator.parent != null) {
      format[iterator.processor] = iterator.task + format[iterator.processor];
      iterator = iterator.parent;
    }
    return Arrays.toString(format);
  }

  @Override
  public int calculateHeuristic() {
    return this.heuristic;
  }

  @Override
  public int getDuration() {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<FormattableTask> getAllTasks() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getProcessor(int forTask) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getNumberOfProcessors() {
    return getCache().getContext().getProcessorsToScheduleOn();
  }
}
