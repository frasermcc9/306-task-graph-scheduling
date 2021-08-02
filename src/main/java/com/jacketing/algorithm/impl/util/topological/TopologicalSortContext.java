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
