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
import com.jacketing.common.factories.SetFactory;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Attach this to a static variable on the base algorithm.
 */
public abstract class AbstractStaticCache implements StaticCache {

  private final Graph graph;
  private final AlgorithmContext context;

  private final Set<PermutationId> duplicateCache;
  private final PermutationId defaultNodeString;

  private int upperBound = Integer.MAX_VALUE;
  private AlgorithmSchedule bestSchedule;

  public AbstractStaticCache(
    Graph g,
    AlgorithmContext ctx,
    SetFactory<PermutationId> factory
  ) {
    this.graph = g;
    this.context = ctx;
    this.duplicateCache = factory.create();

    this.defaultNodeString = defaultPermutationId();
  }

  public abstract PermutationId defaultPermutationId();

  @Override
  public Graph getGraph() {
    return graph;
  }

  @Override
  public AlgorithmContext getContext() {
    return context;
  }

  @Override
  public Set<PermutationId> getDuplicateCache() {
    return duplicateCache;
  }

  @Override
  public int getUpperBound() {
    return upperBound;
  }

  @Override
  public AlgorithmSchedule getBestSchedule() {
    return bestSchedule;
  }

  @Override
  public boolean isPermutation(PermutationId uuid) {
    return this.duplicateCache.contains(uuid);
  }

  @Override
  public void flushPermutationDuplicates() {
    this.duplicateCache.clear();
  }

  @Override
  public void addScheduleToCache(PermutationId uuid) {
    duplicateCache.add(uuid);
  }

  @Override
  public void updateUpper(AlgorithmSchedule schedule) {
    this.bestSchedule = schedule;
    this.upperBound = schedule.getDuration();
  }

  @Override
  public void updateBestLength(int length) {
    upperBound = length;
  }

  @Override
  public PermutationId defaultNodeString() {
    return this.defaultNodeString;
  }

  @Override
  public ExecutorService getExecutor() {
    throw new UnsupportedOperationException(
      "Cannot get executor of basic static cache"
    );
  }
}
