package com.jacketing.algorithm.interfaces.util.topological;

import com.jacketing.algorithm.impl.util.topological.LayeredTopologicalOrderFinder;
import com.jacketing.algorithm.impl.util.topological.TopologicalOrderFinder;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.List;

public interface TopologicalSort<T> {
  List<T> sortedTopological();

  static TopologicalSort<List<Integer>> withLayers(Graph graph) {
    return new LayeredTopologicalOrderFinder(graph);
  }

  static TopologicalSort<Integer> withoutLayers(Graph graph) {
    return new TopologicalOrderFinder(graph);
  }
}
