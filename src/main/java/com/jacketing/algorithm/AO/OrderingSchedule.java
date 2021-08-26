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

import com.google.common.annotations.Beta;
import com.jacketing.algorithm.algorithms.common.Task;
import com.jacketing.common.FormattableTask;
import com.jacketing.common.log.Log;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.Arrays;
import java.util.List;

public abstract class OrderingSchedule extends AOSchedule {

  private final AllocationSchedule allocation;
  private final OrderingSchedule parent;

  private final int currentlyScheduled;
  private final int globalScheduled;

  private int newestTask;
  private final int processorToInvestigate;

  private int[] startTimeCache;

  private int estimatedStartTime;
  private int scp;
  private int orderedLoad;

  private int heuristic;

  public OrderingSchedule(
    AllocationSchedule allocation,
    int cacheKey,
    OrderingSchedule parent,
    int currentlyScheduled,
    int processorToInvestigate,
    int globalScheduled
  ) {
    super(cacheKey);
    this.allocation = allocation;
    this.parent = parent;
    this.currentlyScheduled = currentlyScheduled;
    this.globalScheduled = globalScheduled;
    this.processorToInvestigate = processorToInvestigate;

    this.orderedLoad = allocation.getLargestLoad();
  }

  public abstract void addSchedule(AOSchedule toAdd);

  public abstract OrderingSchedule build(
    AllocationSchedule bitAllocation,
    OrderingSchedule parent,
    int allScheduled,
    int currentlyScheduled,
    int processorToInvestigate
  );

  @Override
  public void propagate() {
    int thisProcessorsNodes = allocation.asBitfield()[processorToInvestigate];
    int available = thisProcessorsNodes & ~currentlyScheduled;

    // loop through the available nodes
    int availableCopy = available;
    for (
      int toSchedule = 0;
      availableCopy != 0;
      ++toSchedule, availableCopy >>>= 1
    ) {
      if ((availableCopy & 1) != 0) {
        int dependenciesForNode = getCache()
          .getGraph()
          .getDependenciesForNode(toSchedule);

        int localDependencies = dependenciesForNode & thisProcessorsNodes;

        if ((localDependencies & ~currentlyScheduled) == 0) {
          // node is in ready list
          int nextScheduled = currentlyScheduled | (1 << toSchedule);
          int allScheduled = globalScheduled | nextScheduled;
          int nextProc = processorToInvestigate;

          if (nextScheduled == thisProcessorsNodes) {
            nextProc = processorToInvestigate + 1;
            nextScheduled = 0;
          }

          OrderingSchedule next = build(
            allocation,
            this,
            allScheduled,
            nextScheduled,
            nextProc
          );

          next.newestTask = toSchedule;

          Graph graph = getCache().getGraph();

          next.estimatedStartTime =
            calculateEEST(toSchedule, processorToInvestigate);

          // b-level vs allocated b-level
          int nextScp = Math.max(
            this.scp,
            next.estimatedStartTime + graph.getBLevel(toSchedule)
          );

          int nextProcFinishTime =
            next.estimatedStartTime + graph.getNodeWeight(toSchedule);

          int nextOrderedLoad = Math.max(
            orderedLoad,
            nextProcFinishTime + sumUnordered(available)
          );

          next.setHeuristics(nextScp, nextOrderedLoad);

          if (
            next.calculateHeuristic() >
            getCache().getBestSchedule().getDuration()
          ) {
            continue;
          }

          addSchedule(next);
        }
      }
    }
  }

  private int sumUnordered(int bitfieldOfUnallocated) {
    Graph graph = getCache().getGraph();
    int sum = 0;
    for (
      int unordered = 0;
      bitfieldOfUnallocated != 0;
      ++unordered, bitfieldOfUnallocated >>>= 1
    ) {
      if ((bitfieldOfUnallocated & 1) != 0) {
        sum += graph.getNodeWeight(unordered);
      }
    }
    return sum;
  }

  /**
   * Calculate the estimate earliest start time.
   *
   * @param forNode the node to find the EEST of
   * @return the EEST
   * @implNote Need to find EEST of this node. EEST is already calculated for
   * ordered nodes. For unordered nodes, it is the TLA value for that node.
   */
  private int calculateEEST(int forNode) {
    return calculateEEST(forNode, -1);
  }

  /**
   * Calculate the estimate earliest start time.
   *
   * @return the EEST
   * @implNote Need to find EEST of this node. EEST is already calculated for
   * ordered nodes. For unordered nodes, it is the TLA value for that node.
   */
  private int calculateEEST(int forNode, int thisNodesProcessor) {
    int isScheduled = this.globalScheduled & (1 << forNode);
    int eest;

    if (thisNodesProcessor == -1) {
      thisNodesProcessor = getProcessor(forNode);
    }

    if (isScheduled > 0) {
      Graph graph = getCache().getGraph();
      List<Integer> parents = graph.parentNodesFor(forNode);

      int edrt = 0;

      for (int parent : parents) {
        int localEdrt = calculateEEST(parent) + graph.getNodeWeight(parent);
        int parentProc = getProcessor(parent);

        if (parentProc != thisNodesProcessor) {
          localEdrt += graph.getEdgeWeight(parent, forNode);
        }
        edrt = Math.max(edrt, localEdrt);
      }

      boolean hasPrevious =
        this.parent.processorToInvestigate == this.processorToInvestigate;

      if (hasPrevious) {
        int prevWeight = graph.getNodeWeight(this.parent.newestTask);
        eest = Math.max(edrt, this.parent.estimatedStartTime + prevWeight);
      } else {
        eest = edrt;
      }
    } else {
      // if not scheduled
      eest = allocation.getTla(forNode);
    }

    return eest;
  }

  private void setHeuristics(int scp, int orderedLoad) {
    this.scp = scp;
    this.orderedLoad = orderedLoad;

    this.heuristic = Math.max(scp, orderedLoad);
  }

  @Override
  public boolean saturated() {
    return (
      processorToInvestigate > allocation.getProcessorBitfields().length - 1
    );
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
    int asBitfield = 1 << forTask;
    int[] processorBitfields = this.allocation.getProcessorBitfields();
    int len = processorBitfields.length;
    for (int i = 0; i < len; i++) {
      int tasks = processorBitfields[i];
      if ((tasks & asBitfield) > 0) {
        // i is the processor the parent is on
        return i;
      }
    }
    Log.warn("Was unable to find processor parent for node " + parent);
    return -1;
  }

  @Override
  public int getNumberOfProcessors() {
    return getCache().getContext().getProcessorsToScheduleOn();
  }

  public OrderingSchedule getParent() {
    return parent;
  }

  @Beta
  public Task getFormattable() {
    if (startTimeCache == null) {
      startTimeCache =
        new int[getCache().getGraph().getAdjacencyList().getNodeCount()];
      Arrays.fill(startTimeCache, -1);
    }

    Graph graph = getCache().getGraph();

    int cached = startTimeCache[newestTask];
    if (cached != -1) {
      return new Task(
        parent.processorToInvestigate,
        cached,
        graph.getNodeWeight(newestTask),
        newestTask
      );
    }

    int startTime = calculateEEST(newestTask, parent.processorToInvestigate);

    return new Task(
      parent.processorToInvestigate,
      startTime,
      graph.getNodeWeight(newestTask),
      newestTask
    );
  }
}
