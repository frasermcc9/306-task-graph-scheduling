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

package com.jacketing.algorithm.impl.util.topological;

import com.jacketing.algorithm.interfaces.util.topological.TopologicalSort;
import java.util.List;

public class TopologicalSortContext<T> implements TopologicalSort<T> {

  private TopologicalSort<T> context;

  public TopologicalSortContext() {}

  public TopologicalSortContext(TopologicalSort<T> context) {
    this.context = context;
  }

  @Override
  public List<T> sortedTopological() {
    return context.sortedTopological();
  }
}
