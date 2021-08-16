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

import java.util.AbstractQueue;
import org.jetbrains.annotations.NotNull;

public class AStarSchedule
  extends AbstractIterativeSchedule
  implements Comparable<AStarSchedule> {

  private final AbstractQueue<AbstractIterativeSchedule> queue;

  public AStarSchedule(
    int orphans,
    AbstractIterativeSchedule parent,
    AbstractQueue<AbstractIterativeSchedule> scheduleQueue,
    int cacheKey
  ) {
    super(orphans, parent, cacheKey);
    this.queue = scheduleQueue;
  }

  public AStarSchedule(
    int orphans,
    AbstractIterativeSchedule parent,
    int[] totalTime,
    int cacheKey,
    String permutationId,
    AbstractQueue<AbstractIterativeSchedule> scheduleQueue
  ) {
    super(orphans, parent, totalTime, cacheKey, permutationId);
    this.queue = scheduleQueue;
  }

  @Override
  public AbstractIterativeSchedule buildSchedule(
    AbstractIterativeSchedule parent,
    int nextOrphans,
    int[] totalTimeArray,
    int cacheKey,
    String permutationId
  ) {
    return new AStarSchedule(
      nextOrphans,
      parent,
      totalTimeArray,
      cacheKey,
      permutationId,
      queue
    );
  }

  @Override
  public int calculateHeuristic(HeuristicSchedule schedule) {
    StaticCache cache = getCache();
    int graphWeight = cache.getGraph().getGraphWeight();
    int processorCount = cache.getContext().getProcessorsToScheduleOn();
    int idleHeuristic = (schedule.getIdleTime() + graphWeight) / processorCount;
    return Math.max(schedule.getMaxBottomLevel(), idleHeuristic);
  }

  @Override
  public void addItem(AbstractIterativeSchedule schedule) {
    this.queue.offer(schedule);
  }

  @Override
  public int compareTo(@NotNull AStarSchedule other) {
    return Integer.compare(this.heuristic, other.heuristic);
  }
}
