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
import java.util.*;

public class WeightService implements GraphWeightService {

  private final GraphParser graph;
  private final EnumeratedNodeMap enumeratedNodeMap;

  private final Map<Integer, Integer> nodeWeightArray;
  private final Map<Integer, Map<Integer, Integer>> edgeWeightArray;

  private int weightSum;

  public WeightService(GraphParser graph, EnumeratedNodeMap enumeratedNodeMap) {
    this.graph = graph;
    this.enumeratedNodeMap = enumeratedNodeMap;

    this.nodeWeightArray = new HashMap<>(graph.getNodes().size());
    this.edgeWeightArray = new HashMap<>(graph.getNodes().size());
  }

  @Override
  public int getGraphWeight() {
    return weightSum;
  }

  @Override
  public void implementNewEdge(int from, int to, int weight) {
    edgeWeightArray.get(from).put(to, weight);
  }

  @Override
  public void formWeights() {
    introduceNodeWeights();
    introduceEdgeWeights();
  }

  @Override
  public int nodeWeight(int enumeratedNode) {
    return nodeWeightArray.get(enumeratedNode);
  }

  @Override
  public int edgeWeight(int source, int target) {
    return edgeWeightArray.get(source).get(target);
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

      nodeWeightArray.put(enumerated, weight);
      edgeWeightArray.put(enumerated, new HashMap<>());
    }
  }

  private void introduceEdgeWeights() {
    Collection<GraphEdge> edges = graph.getEdges().values();
    for (GraphEdge edge : edges) {
      int source = enumeratedNodeMap.getEnumerated(edge.getNode1().getId());
      int target = enumeratedNodeMap.getEnumerated(edge.getNode2().getId());

      int weight = Integer.parseInt(edge.getAttribute("Weight").toString());

      edgeWeightArray.get(source).put(target, weight);
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
      return edgeWeight(source, target);
    }
  }
}
