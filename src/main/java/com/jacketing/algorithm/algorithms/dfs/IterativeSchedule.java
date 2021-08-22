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

package com.jacketing.algorithm.algorithms.dfs;

import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.algorithms.common.AbstractIterativeSchedule;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.common.Task;
import com.jacketing.algorithm.algorithms.common.cache.PermutationId;
import com.jacketing.algorithm.algorithms.common.cache.StringPermutationId;
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
    super(orphans, parent, totalTime, cacheKey, new StringPermutationId(""));
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
      nextOrphans,
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
            boolean sameProcessor = processor == parentTask.getProcessor();
            if (sameProcessor) {
              startTime =
                Math.max(
                  startTime,
                  parentTask.getTime() + parentTask.getDuration()
                );
            } else {
              int edge = graph.getEdgeWeight().from(parentNode).to(orphan);
              startTime =
                Math.max(
                  startTime,
                  parentTask.getTime() + parentTask.getDuration() + edge
                );
            }
          }
          startTime = Math.max(startTime, findProcEnd(processor));
          int taskWeight = graph.getNodeWeight(orphan);

          int bLevel = graph.getBLevel(orphan);
          // task end time
          if (startTime + bLevel >= getCache().getUpperBound()) {
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
          PermutationId permutation = new StringPermutationId(permutationId);
          if (getCache().isPermutation(permutation)) {
            continue;
          }
          getCache().addScheduleToCache(permutation);

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
            permutationStringsCopy
          );

          schedule.setAddedTask(processor, startTime, orphan, taskWeight);
          globalStack.addFirst(schedule);
        }
      }
    }
  }

  public AbstractIterativeSchedule buildSchedule(
    AbstractIterativeSchedule parent,
    int nextOrphans,
    int[] totalTimeArray,
    int cacheKey,
    PermutationId permutationId
  ) {
    return null;
  }
}
