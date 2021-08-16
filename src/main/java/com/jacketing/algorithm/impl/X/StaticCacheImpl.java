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

import com.jacketing.common.factories.SetFactory;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.Set;

/**
 * Attach this to a static variable on the base algorithm.
 */
public class StaticCacheImpl implements StaticCache {

  private final Graph graph;
  private final AlgorithmContext context;

  private final Set<String> duplicateCache;

  private int upperBound = Integer.MAX_VALUE;
  private AlgorithmSchedule bestSchedule;

  private final String defaultNodeString;

  public StaticCacheImpl(Graph g, AlgorithmContext ctx, SetFactory factory) {
    this.graph = g;
    this.context = ctx;
    this.duplicateCache = factory.create();

    int nodeCount = graph.getAdjacencyList().getNodeCount();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < nodeCount; i++) {
      sb.append(i);
      sb.append("X");
    }
    defaultNodeString = sb.toString();
  }

  @Override
  public Graph getGraph() {
    return graph;
  }

  @Override
  public AlgorithmContext getContext() {
    return context;
  }

  @Override
  public Set<String> getDuplicateCache() {
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
  public boolean isPermutation(String uuid) {
    return this.duplicateCache.contains(uuid);
  }

  @Override
  public void flushPermutationDuplicates() {
    this.duplicateCache.clear();
  }

  @Override
  public void addScheduleToCache(String uuid) {
    duplicateCache.add(uuid);
  }

  @Override
  public void updateUpper(AlgorithmSchedule schedule) {
    this.bestSchedule = schedule;
    this.upperBound = schedule.getDuration();
  }

  @Override
  public String defaultNodeString() {
    return this.defaultNodeString;
  }
}
