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

import com.jacketing.algorithm.AO.Collections.AlgorithmCollection;

public class QueuedAllocationSchedule extends AllocationSchedule {

  protected final AlgorithmCollection<AOSchedule> queue;

  public QueuedAllocationSchedule(
    AllocationSchedule parent,
    int task,
    int maxNode,
    int cacheKey,
    int processorIndex,
    AlgorithmCollection<AOSchedule> queue
  ) {
    super(parent, task, maxNode, cacheKey, processorIndex);
    this.queue = queue;
  }

  public static AOSchedule empty(
    AlgorithmCollection<AOSchedule> queue,
    int nodeIndex,
    int maxNodes,
    int cacheKey
  ) {
    return new QueuedAllocationSchedule(
      null,
      nodeIndex,
      maxNodes,
      cacheKey,
      0,
      queue
    );
  }

  @Override
  public OrderingSchedule upgrade() {
    return QueuedOrderingSchedule.empty(queue, this, cacheKey);
  }

  @Override
  public void addSchedule(AOSchedule toAdd) {
    queue.offer(toAdd);
  }

  @Override
  public AllocationSchedule build(
    AllocationSchedule parent,
    int nodeIndex,
    int processorIndex
  ) {
    return new QueuedAllocationSchedule(
      parent,
      nodeIndex,
      maxNode,
      cacheKey,
      processorIndex,
      queue
    );
  }
}
