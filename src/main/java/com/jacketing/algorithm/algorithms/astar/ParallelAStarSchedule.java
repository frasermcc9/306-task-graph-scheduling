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

package com.jacketing.algorithm.algorithms.astar;

import com.jacketing.algorithm.algorithms.common.AbstractIterativeSchedule;
import com.jacketing.algorithm.algorithms.common.Task;
import com.jacketing.algorithm.algorithms.common.cache.PermutationId;
import com.jacketing.common.log.Log;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ParallelAStarSchedule extends AStarSchedule {

  public ParallelAStarSchedule(
    int orphans,
    AbstractIterativeSchedule parent,
    AbstractQueue<AbstractIterativeSchedule> scheduleQueue,
    int cacheKey
  ) {
    super(orphans, parent, scheduleQueue, cacheKey);
  }

  public ParallelAStarSchedule(
    int orphans,
    AbstractIterativeSchedule parent,
    int[] totalTime,
    int cacheKey,
    PermutationId permutationId,
    AbstractQueue<AbstractIterativeSchedule> scheduleQueue
  ) {
    super(orphans, parent, totalTime, cacheKey, permutationId, scheduleQueue);
  }

  @Override
  public void propagate() {
    int processors = getCache().getContext().getProcessorsToScheduleOn();
    List<Future<AbstractIterativeSchedule>> schedules = new ArrayList<>();
    ExecutorService executor = getCache().getExecutor();

    int orphansCopy = orphans;
    for (int orphan = 0; orphansCopy != 0; ++orphan, orphansCopy >>>= 1) {
      if ((orphansCopy & 1) != 0) {
        int nextOrphans = orphans;
        nextOrphans &= ~(1 << orphan);

        for (int processor = 0; processor < processors; processor++) {
          final int finalOrphan = orphan;
          final int finalProc = processor;
          final int finalNextOrphans = nextOrphans;
          schedules.add(
            executor.submit(
              () -> singlePropagation(finalOrphan, finalProc, finalNextOrphans)
            )
          );
        }
      }
    }

    for (Future<AbstractIterativeSchedule> futures : schedules) {
      try {
        AbstractIterativeSchedule abstractIterativeSchedule = futures.get();
        if (abstractIterativeSchedule != null) {
          queue.add(abstractIterativeSchedule);
        }
      } catch (InterruptedException | ExecutionException e) {
        Log.warn("A concurrency error occurred while trying to schedule.");
      }
    }
  }

  private AbstractIterativeSchedule singlePropagation(
    int orphan,
    int processor,
    int nextOrphans
  ) {
    int processors = getCache().getContext().getProcessorsToScheduleOn();
    Graph graph = getCache().getGraph();

    List<Integer> parentNodes = graph.getAdjacencyList().getParentNodes(orphan);
    int startTime = 0;

    for (Integer parentNode : parentNodes) {
      Task parentTask = findTask(parentNode);
      boolean sameProcessor = processor == parentTask.getProcessor();
      if (sameProcessor) {
        startTime =
          Math.max(startTime, parentTask.getTime() + parentTask.getDuration());
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

    int bLevelOfNext = graph.getBLevel(orphan);

    if (startTime + bLevelOfNext >= getCache().getUpperBound()) {
      return null;
    }

    PermutationId nextId = permutationId.updatePermutation(
      orphan,
      startTime,
      processor
    );

    if (getCache().isPermutation(nextId)) {
      return null;
    }
    getCache().addScheduleToCache(nextId);

    List<Integer> childNodes = graph.getAdjacencyList().getChildNodes(orphan);

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

    return schedule;
  }

  @Override
  public AbstractIterativeSchedule buildSchedule(
    AbstractIterativeSchedule parent,
    int nextOrphans,
    int[] totalTimeArray,
    int cacheKey,
    PermutationId permutationId
  ) {
    return new ParallelAStarSchedule(
      nextOrphans,
      parent,
      totalTimeArray,
      cacheKey,
      permutationId,
      queue
    );
  }
}
