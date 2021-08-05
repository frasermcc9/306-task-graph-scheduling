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

package com.jacketing.parsing.impl.structures;

import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import java.util.*;

public class EnumeratedAdjacencyList {

  private final GraphParser sourceGraph;
  private final Map<Integer, List<Integer>> inAdjacencyList;
  private final Map<Integer, List<Integer>> outAdjacencyList;
  private final EnumeratedNodeMap enumeratedNodeMap;

  private int nodeCount;

  public EnumeratedAdjacencyList(
    GraphParser sourceGraph,
    EnumeratedNodeMap enumeratedNodeMap,
    Map<Integer, List<Integer>> inAdjacencyList,
    Map<Integer, List<Integer>> outAdjacencyList
  ) {
    this.sourceGraph = sourceGraph;
    this.inAdjacencyList = inAdjacencyList;
    this.outAdjacencyList = outAdjacencyList;
    this.enumeratedNodeMap = enumeratedNodeMap;
  }

  public void createRepresentation() {
    introduceNodes();
    introduceEdges();
  }

  public List<Integer> getNodeIds() {
    List<Integer> nodes = new ArrayList<>();
    inAdjacencyList.forEach((key, value) -> nodes.add(key));

    return nodes;
  }

  public List<Integer> getChildNodes(int forNode) {
    return outAdjacencyList.get(forNode);
  }

  public List<Integer> getParentNodes(int forNode) {
    return inAdjacencyList.get(forNode);
  }

  public int parentCount(int forNode) {
    return inAdjacencyList.get(forNode).size();
  }

  public Map<Integer, List<Integer>> getInAdjacencyList() {
    return inAdjacencyList;
  }

  public Map<Integer, List<Integer>> getOutAdjacencyList() {
    return outAdjacencyList;
  }

  public Map<Integer, Integer> parentCountForEach() {
    HashMap<Integer, Integer> nodeToParentCount = new HashMap<>();
    inAdjacencyList.forEach(
      (node, parents) -> nodeToParentCount.put(node, parents.size())
    );
    return nodeToParentCount;
  }

  private void introduceNodes() {
    Collection<GraphNode> nodes = this.sourceGraph.getNodes().values();
    for (GraphNode node : nodes) {
      int enumeratedNode = enumeratedNodeMap.getEnumeratedNode(node);
      inAdjacencyList.put(enumeratedNode, new ArrayList<>());
      outAdjacencyList.put(enumeratedNode, new ArrayList<>());
    }
    this.nodeCount = nodes.size();
  }

  public int getNodeCount() {
    return nodeCount;
  }

  private void introduceEdges() {
    Collection<GraphEdge> edges = this.sourceGraph.getEdges().values();
    for (GraphEdge edge : edges) {
      int source = enumeratedNodeMap.getEnumeratedNode(edge.getNode1());
      int target = enumeratedNodeMap.getEnumeratedNode(edge.getNode2());
      inAdjacencyList.get(target).add(source);
      outAdjacencyList.get(source).add(target);
    }
  }

  public EnumeratedNodeMap getEnumeratedNodeMap() {
    return enumeratedNodeMap;
  }
}
