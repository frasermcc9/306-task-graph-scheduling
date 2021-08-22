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

import com.jacketing.parsing.impl.services.WeightService;
import com.jacketing.parsing.interfaces.structures.services.GraphWeightService;
import java.util.*;
import java.util.stream.Collectors;
import javafx.util.Pair;

public class Graph {

  /**
   * Contains the two-way adjacency list that represents the DAG. The adjacency
   * list strictly contains the relations between the nodes. It does not contain
   * any concept of the node weights, or the weights of the edges between them.
   *
   * @see #getAdjacencyList()
   */
  private final EnumeratedAdjacencyList adjacencyList;

  /**
   * Provides the ability to get the weights for the nodes of the graph, as well
   * as the weights of the edges between graphs.
   *
   * @see #getNodeWeight(int)
   * @see #getEdgeWeight()
   */
  private final GraphWeightService weightService;

  private final String name;
  private final int[] bLevels;
  private final int optimalLength;
  private int criticalTime;

  /**
   * Creates a new graph object, which is a structure that contains both an
   * adjacency list (for the graph structure) and a weight service (for node and
   * edge weights). Names the graph with a default name.
   *
   * @param adjacencyList a new adjacency list. Should not have called {@link
   *                      EnumeratedAdjacencyList#createRepresentation()} on the
   *                      adjacency list.
   * @param weightService a new weight service. Should not have called {@link
   *                      WeightService#formWeights()} on the weight service.
   */
  public Graph(
    EnumeratedAdjacencyList adjacencyList,
    GraphWeightService weightService
  ) {
    this(adjacencyList, weightService, "Default", -1);
  }

  /**
   * Creates a new graph object, which is a structure that contains both an
   * adjacency list (for the graph structure) and a weight service (for node and
   * edge weights).
   *
   * @param adjacencyList a new adjacency list. Should not have called {@link
   *                      EnumeratedAdjacencyList#createRepresentation()} on the
   *                      adjacency list.
   * @param weightService a new weight service. Should not have called {@link
   *                      WeightService#formWeights()} on the weight service.
   * @param name          the name of this graph
   * @param optimalLength the optimal length of the read graph, if available.
   */
  public Graph(
    EnumeratedAdjacencyList adjacencyList,
    GraphWeightService weightService,
    String name,
    int optimalLength
  ) {
    this.adjacencyList = adjacencyList;
    this.weightService = weightService;
    this.name = name;

    adjacencyList.createRepresentation();
    weightService.formWeights();

    bLevels = new int[adjacencyList.getNodeCount()];

    this.optimalLength = optimalLength;
  }

  /**
   * Get the name of the overall graph
   *
   * @return the graph name
   */
  public String getName() {
    return name;
  }

  /**
   * Translate the integer enumerated node into its String value
   * representation.
   *
   * @param enumeratedNode the enumerated integer of the node
   * @return the original string representation of the node
   * @see #translate(String) for the opposite
   */
  public String translate(int enumeratedNode) {
    return adjacencyList
      .getEnumeratedNodeMap()
      .getIdFromNumeral(enumeratedNode);
  }

  /**
   * Translate the string value of the node into its enumerated integer value
   * representation.
   *
   * @param nodeId the string representation of the node
   * @return the enumerated integer of the node
   * @see #translate(int) for the opposite
   */
  public int translate(String nodeId) {
    return adjacencyList.getEnumeratedNodeMap().getEnumerated(nodeId);
  }

  /**
   * Get the weight of the node (i.e. computation time).
   *
   * @param enumeratedNode the int value of the node to get the weight of
   * @return the weight of that node
   */
  public int getNodeWeight(int enumeratedNode) {
    return weightService.nodeWeight(enumeratedNode);
  }

  public int getEdgeWeight(int source, int target) {
    return weightService.edgeWeight(source, target);
  }

  public int getGraphWeight() {
    return weightService.getGraphWeight();
  }

  /**
   * Allows the caller to get the weight between two edges (i.e. communication
   * delay).
   *
   * <pre>
   * getEdgeWeight().from(0).to(3);
   * </pre>
   * <p>
   * The above will get the edge weight of travelling from node 0 to node 3.
   *
   * @return the weight between the two nodes.
   */
  public GraphWeightService.EdgeWeightFrom getEdgeWeight() {
    return weightService.edgeWeight();
  }

  public EnumeratedAdjacencyList getAdjacencyList() {
    return adjacencyList;
  }

  public List<Integer> parentNodesFor(int forNode) {
    return adjacencyList.getParentNodes(forNode);
  }

  /**
   * Calculate the overall longest path length Sum of task weights + edge
   * weights.
   *
   * @return critical path.
   */
  public int getCriticalTime() {
    if (criticalTime != 0) {
      return criticalTime;
    }

    for (int node : adjacencyList.getNodeIds()) {
      getBLevel(node);
    }

    return criticalTime;
  }

  /**
   * Returns the longest path (sum of task weights + edge) from the input node
   * to last node of the graph.
   *
   * @param node
   * @return longest path from the node to end.
   * @implNote It runs recursively bottom-up. Worst case it will traverse all
   * nodes in the graph, thus, O(|V|)
   */
  public int getBLevel(int node) {
    // bl(node) has already been calculated
    if (bLevels[node] != 0) {
      return bLevels[node];
    }

    List<Integer> childNodes = adjacencyList.getChildNodes(node);
    // if the node is at 0th level, bl(node) = w_node
    if (childNodes.isEmpty()) {
      bLevels[node] = weightService.nodeWeight(node);
      return bLevels[node];
    }

    int maxBl = 0;
    for (int childNode : childNodes) {
      maxBl = Math.max(maxBl, getBLevel(childNode));
    }

    bLevels[node] = maxBl + weightService.nodeWeight(node);
    criticalTime = Math.max(criticalTime, bLevels[node]);

    return bLevels[node];
  }

  public int getOptimalLength() {
    return optimalLength;
  }

  public void introduceVirtualForIdenticalNodes() {
    List<Integer> nodeIds = this.adjacencyList.getNodeIds();
    for (int node1 : nodeIds) {
      goNext:for (int node2 : nodeIds) {
        if (node1 == node2) continue;

        if (getNodeWeight(node1) != getNodeWeight(node2)) continue;

        Set<Pair<Integer, Integer>> parents = new HashSet<>();
        for (int parent : adjacencyList.getParentNodes(node1)) {
          Pair<Integer, Integer> pair = new Pair<>(
            parent,
            getEdgeWeight(parent, node1)
          );
          parents.add(pair);
        }
        for (int parent : adjacencyList.getParentNodes(node2)) {
          Pair<Integer, Integer> pair = new Pair<>(
            parent,
            getEdgeWeight(parent, node2)
          );
          if (!parents.remove(pair)) {
            continue goNext;
          }
        }
        if (parents.size() != 0) continue;

        Set<Pair<Integer, Integer>> children = new HashSet<>();
        for (int child : adjacencyList.getChildNodes(node1)) {
          Pair<Integer, Integer> pair = new Pair<>(
            child,
            getEdgeWeight(node1, child)
          );
          children.add(pair);
        }
        for (int child : adjacencyList.getChildNodes(node2)) {
          Pair<Integer, Integer> pair = new Pair<>(
            child,
            getEdgeWeight(node2, child)
          );
          if (!children.remove(pair)) {
            continue goNext;
          }
        }
        if (children.size() != 0) continue;

        this.getAdjacencyList().addEdge(node1, node2);
        weightService.implementNewEdge(node1, node2, 0);

        System.out.println("Found identical node");
      }
    }
  }

  public void prepareIndependentFixedOrder() {
    List<List<Integer>> nodeOrder = new ArrayList<>();

    List<Integer> nodeInWeightOrder =
      this.getAdjacencyList()
        .getNodeIds()
        .stream()
        .sorted(Comparator.comparingInt(this::getNodeWeight).reversed())
        .collect(Collectors.toList());

    int previousNodeWeight = -1;
    for (int node : nodeInWeightOrder) {
      int nodeWeight = getNodeWeight(node);
      if (nodeWeight == previousNodeWeight) {
        List<Integer> lastList = nodeOrder.get(nodeOrder.size() - 1);
        lastList.add(node);
      } else {
        List<Integer> nextList = new ArrayList<>();
        nextList.add(node);
        nodeOrder.add(nextList);
      }
    }

    for (int i = 0; i < nodeOrder.size() - 1; i++) {
      List<Integer> first = nodeOrder.get(0);
      List<Integer> second = nodeOrder.get(1);

      for (int parentNode : first) {
        for (int childNode : second) {
          this.getAdjacencyList().addEdge(parentNode, childNode);
          weightService.implementNewEdge(parentNode, childNode, 0);
        }
      }
    }
  }
}
