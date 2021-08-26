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

import com.jacketing.algorithm.algorithms.AbstractSchedulingAlgorithm;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.common.cache.StaticCache;
import org.jetbrains.annotations.NotNull;

public abstract class AOSchedule
  implements AlgorithmSchedule, Comparable<AOSchedule> {

  protected final int cacheKey;

  protected AOSchedule(int cacheKey) {
    this.cacheKey = cacheKey;
  }

  public abstract void propagate();

  protected StaticCache getCache() {
    return AbstractSchedulingAlgorithm.getCache(cacheKey);
  }

  public abstract boolean saturated();

  public abstract int calculateHeuristic();

  /**
   * Upgrades the partial solution to the next tier. For example, Allocation ->
   * Ordering -> Complete
   *
   * @return the upgraded solution
   */
  public abstract AOSchedule upgrade();

  @Override
  public int compareTo(@NotNull AOSchedule o) {
    return Integer.compare(this.calculateHeuristic(), o.calculateHeuristic());
  }
}
