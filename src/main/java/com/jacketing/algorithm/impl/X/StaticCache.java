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

import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.Set;

public interface StaticCache {
  Graph getGraph();

  AlgorithmContext getContext();

  Set<String> getDuplicateCache();

  int getUpperBound();

  AlgorithmSchedule getBestSchedule();

  boolean isPermutation(String uuid);

  void flushPermutationDuplicates();

  void addScheduleToCache(String uuid);

  void updateUpper(AlgorithmSchedule schedule);

  String defaultNodeString();
}
