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

package com.jacketing.algorithm.algorithms.common.cache;

import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public interface StaticCache {
  Graph getGraph();

  AlgorithmContext getContext();

  Set<PermutationId> getDuplicateCache();

  int getUpperBound();

  AlgorithmSchedule getBestSchedule();

  boolean isPermutation(PermutationId uuid);

  void flushPermutationDuplicates();

  void addScheduleToCache(PermutationId uuid);

  /**
   * Updates the best schedule, and also its duration.
   *
   * @param schedule the new best schedule
   * @see this#updateBestLength(int)
   */
  void updateUpper(AlgorithmSchedule schedule);

  /**
   * Updates just the best length so far, without updating the schedule itself.
   *
   * @param length the length of the schedule
   */
  void updateBestLength(int length);

  PermutationId defaultNodeString();

  ExecutorService getExecutor();
}
