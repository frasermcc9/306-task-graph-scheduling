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

import com.jacketing.algorithm.impl.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class IterativeSchedule
  extends AbstractIterativeSchedule
  implements AlgorithmSchedule {

  private final Deque<AbstractIterativeSchedule> globalStack;
  private final String[] permutationStrings;

  public IterativeSchedule(
    int orphans,
    IterativeSchedule parent,
    Deque<AbstractIterativeSchedule> globalStack,
    int cacheKey
  ) {
    this(
      orphans,
      parent,
      globalStack,
      new int[AbstractSchedulingAlgorithm
        .getCache(cacheKey)
        .getContext()
        .getProcessorsToScheduleOn()],
      cacheKey,
      new String[AbstractSchedulingAlgorithm
        .getCache(cacheKey)
        .getContext()
        .getProcessorsToScheduleOn()]
    );
    Arrays.fill(permutationStrings, "");
  }

  public IterativeSchedule(
    int orphans,
    AbstractIterativeSchedule parent,
    Deque<AbstractIterativeSchedule> globalStack,
    int[] totalTime,
    int cacheKey,
    String[] permutationStrings
  ) {
    super(orphans, parent, totalTime, cacheKey, "");
    this.globalStack = globalStack;
    this.permutationStrings = permutationStrings;
  }

  public AbstractIterativeSchedule buildSchedule(
    AbstractIterativeSchedule parent,
    int nextOrphans,
    int[] totalTimeArray,
    int cacheKey,
    String[] permutationStrings
  ) {
    return new IterativeSchedule(
      orphans,
      parent,
      globalStack,
      totalTimeArray,
      cacheKey,
      permutationStrings
    );
  }

  @Override
  public void addItem(AbstractIterativeSchedule schedule) {
    this.globalStack.addFirst(schedule);
  }

  public void propagate() {
    int processors = getCache().getContext().getProcessorsToScheduleOn();
    Graph graph = getCache().getGraph();

    int orphansCopy = orphans;
    // better xor method?
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

          String[] permutationStringsCopy = new String[processors];
          System.arraycopy(
            permutationStrings,
            0,
            permutationStringsCopy,
            0,
            processors
          );
          permutationStringsCopy[processor] =
            permutationStringsCopy[processor].concat("" + orphan)
              .concat("" + startTime);

          String permutationId = getPermutationId(permutationStringsCopy);
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

          AbstractIterativeSchedule schedule = new IterativeSchedule(
            nextOrphans,
            this,
            globalStack,
            totalTimeCopy,
            cacheKey,
            permutationStringsCopy
          );
          schedule.setAddedTask(processor, startTime, orphan, taskWeight);
          globalStack.addFirst(schedule);
        }
      }
    }
  }

  @Override
  public AbstractIterativeSchedule buildSchedule(
    AbstractIterativeSchedule parent,
    int nextOrphans,
    int[] totalTimeArray,
    int cacheKey,
    String permutationId
  ) {
    return null;
  }

  protected String getPermutationId(String[] withStrings) {
    int len = permutationStrings.length;
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
