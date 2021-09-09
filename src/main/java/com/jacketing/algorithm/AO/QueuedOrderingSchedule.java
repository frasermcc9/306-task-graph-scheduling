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

public class QueuedOrderingSchedule extends OrderingSchedule {

  protected final AlgorithmCollection<AOSchedule> queue;

  public QueuedOrderingSchedule(
    AllocationSchedule allocation,
    int cacheKey,
    int scheduled,
    int allScheduled,
    int investigateProcessor,
    AlgorithmCollection<AOSchedule> queue,
    OrderingSchedule parent
  ) {
    super(
      allocation,
      cacheKey,
      parent,
      scheduled,
      investigateProcessor,
      allScheduled
    );
    this.queue = queue;
  }

  public static OrderingSchedule empty(
    AlgorithmCollection<AOSchedule> queue,
    AllocationSchedule allocationSchedule,
    int cacheKey
  ) {
    return new QueuedOrderingSchedule(
      allocationSchedule,
      cacheKey,
      0,
      0,
      0,
      queue,
      null
    );
  }

  @Override
  public void addSchedule(AOSchedule toAdd) {
    queue.offer(toAdd);
  }

  @Override
  public OrderingSchedule build(
    AllocationSchedule allocation,
    OrderingSchedule parent,
    int allScheduled,
    int currentlyScheduled,
    int processorToInvestigate
  ) {
    return new QueuedOrderingSchedule(
      allocation,
      cacheKey,
      currentlyScheduled,
      allScheduled,
      processorToInvestigate,
      queue,
      parent
    );
  }

  @Override
  public AOSchedule upgrade() {
    return new AOSaturatedSchedule(cacheKey, this);
  }
}
