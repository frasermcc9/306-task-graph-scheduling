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

/**
 * Decorates an existing static cache to add an executor.
 */
public class PooledCacheDecorator implements StaticCache {

  private final ExecutorService executorService;
  private final StaticCache wrappedCache;

  public PooledCacheDecorator(
    ExecutorService executorService,
    StaticCache wrappedCache
  ) {
    this.executorService = executorService;
    this.wrappedCache = wrappedCache;
  }

  @Override
  public ExecutorService getExecutor() {
    return this.executorService;
  }

  @Override
  public Graph getGraph() {
    return wrappedCache.getGraph();
  }

  @Override
  public AlgorithmContext getContext() {
    return wrappedCache.getContext();
  }

  @Override
  public Set<PermutationId> getDuplicateCache() {
    return wrappedCache.getDuplicateCache();
  }

  @Override
  public int getUpperBound() {
    return wrappedCache.getUpperBound();
  }

  @Override
  public AlgorithmSchedule getBestSchedule() {
    return wrappedCache.getBestSchedule();
  }

  @Override
  public boolean isPermutation(PermutationId uuid) {
    return wrappedCache.isPermutation(uuid);
  }

  @Override
  public void flushPermutationDuplicates() {
    wrappedCache.flushPermutationDuplicates();
  }

  @Override
  public void addScheduleToCache(PermutationId uuid) {
    wrappedCache.addScheduleToCache(uuid);
  }

  @Override
  public void updateUpper(AlgorithmSchedule schedule) {
    wrappedCache.updateUpper(schedule);
  }

  @Override
  public void updateBestLength(int length) {
    wrappedCache.updateBestLength(length);
  }

  @Override
  public PermutationId defaultNodeString() {
    return wrappedCache.defaultNodeString();
  }
}
