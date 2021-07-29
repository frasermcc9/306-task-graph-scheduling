package com.jacketing.parsing.impl.structures;

import static com.jacketing.util.Memoize.useMemo;

import java.util.List;
import java.util.function.Function;

public class Graph {

  public final Function<AdjacencyList, List<GraphNode>> computeTopological = useMemo(
    this::internalTopological
  );

  private AdjacencyList adjacencyList;

  public Graph(AdjacencyList adjacencyList) {
    this.adjacencyList = adjacencyList;
  }

  private List<GraphNode> internalTopological(AdjacencyList adjacencyList) {
    return null;
  }
}
