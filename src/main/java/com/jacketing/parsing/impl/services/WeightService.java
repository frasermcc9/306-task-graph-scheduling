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

package com.jacketing.parsing.impl.services;

import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import com.jacketing.parsing.interfaces.structures.services.GraphWeightService;
import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WeightService implements GraphWeightService {

  private final GraphParser graph;
  private final EnumeratedNodeMap enumeratedNodeMap;
  private final Map<Integer, Integer> nodeWeightMap;
  /**
   * Parent Node -> (Child Nodes -> Weight)
   */
  private final Map<Integer, Map<Integer, Integer>> edgeWeightMap;

  private int weightSum;

  public WeightService(
    GraphParser graph,
    EnumeratedNodeMap enumeratedNodeMap,
    Map<Integer, Integer> nodeWeightMap,
    Map<Integer, Map<Integer, Integer>> edgeWeightMap
  ) {
    this.graph = graph;
    this.enumeratedNodeMap = enumeratedNodeMap;
    this.nodeWeightMap = nodeWeightMap;
    this.edgeWeightMap = edgeWeightMap;
  }

  @Override
  public int getGraphWeight() {
    return weightSum;
  }

  @Override
  public void formWeights() {
    introduceNodeWeights();
    introduceEdgeWeights();
  }

  @Override
  public int nodeWeight(int enumeratedNode) {
    return nodeWeightMap.get(enumeratedNode);
  }

  @Override
  public EdgeWeightFrom edgeWeight() {
    return new EdgeWeightHelper();
  }

  private void introduceNodeWeights() {
    weightSum = 0;
    Collection<GraphNode> nodes = graph.getNodes().values();
    for (GraphNode node : nodes) {
      int enumerated = enumeratedNodeMap.getEnumerated(node.getId());
      int weight = Integer.parseInt(node.getAttribute("Weight").toString());
      weightSum += weight;

      nodeWeightMap.put(enumerated, weight);
      edgeWeightMap.put(enumerated, new HashMap<>());
    }
  }

  private void introduceEdgeWeights() {
    Collection<GraphEdge> edges = graph.getEdges().values();
    for (GraphEdge edge : edges) {
      int source = enumeratedNodeMap.getEnumerated(edge.getNode1().getId());
      int target = enumeratedNodeMap.getEnumerated(edge.getNode2().getId());

      int weight = Integer.parseInt(edge.getAttribute("Weight").toString());
      edgeWeightMap.get(source).put(target, weight);
    }
  }

  public class EdgeWeightHelper implements EdgeWeightFrom, EdgeWeightTo {

    private int source;

    @Override
    public EdgeWeightTo from(int source) {
      this.source = source;
      return this;
    }

    @Override
    public int to(int target) {
      return edgeWeightMap.get(this.source).get(target);
    }
  }
}
