package com.jacketing.parsing.impl.structures;

import com.jacketing.parsing.interfaces.structures.services.GraphWeightService;

public class Graph {

  private final EnumeratedAdjacencyList adjacencyList;
  private final GraphWeightService weightService;

  public Graph(
    EnumeratedAdjacencyList adjacencyList,
    GraphWeightService weightService
  ) {
    this.adjacencyList = adjacencyList;
    this.weightService = weightService;

    adjacencyList.createRepresentation();
    weightService.formWeights();
  }

  public EnumeratedAdjacencyList getAdjacencyList() {
    return adjacencyList;
  }

  public int getNodeWeight(int enumeratedNode) {
    return weightService.nodeWeight(enumeratedNode);
  }

  public GraphWeightService.EdgeWeightFrom getEdgeWeight() {
    return weightService.edgeWeight();
  }
}
