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
import com.jacketing.parsing.impl.structures.Graph;
import java.util.*;

public class LayeredTopologicalOrderFinder
  implements TopologicalSort<List<Integer>> {

  //Here we store the size of the in-degree of all nodes. I use a map as this
  // allows constant access time of
  // finding the in-degree of the given node.
  private final Map<Integer, Integer> inDegreeQuantity;
  //Here we store all 'candidates' - nodes which have in degree 0. The
  // priority queue ensures we always remove
  // the smallest element first, ensuring that it is sorted.
  private final Queue<Integer> candidates = new PriorityQueue<>();
  //This is the list of the final sorted topological order
  private final List<List<Integer>> ordered = new ArrayList<>();
  //This is where we store nodes before we add them to the queue. This is
  // important since in this case, the sorted
  //topological order requires 'layered' orders, so we want to make sure we
  // dont add things to the queue until we
  //have finished with the layer.
  private final List<Integer> queueBuffer = new ArrayList<>();

  private final int nodeCount;
  private final Map<Integer, List<Integer>> digraphOut;

  /**
   * Construct the TopologicalOrderFinder. We look at the size of the in-degree
   * of every node, and store it in a map for constant access time. If any have
   * no in-degree, then we directly add it to the priority queue, since those
   * will be the start of the topological order.
   */
  public LayeredTopologicalOrderFinder(Graph graph) {
    this.nodeCount = graph.getAdjacencyList().getNodeCount();
    this.inDegreeQuantity = new HashMap<>(nodeCount);
    this.digraphOut = graph.getAdjacencyList().getOutAdjacencyList();

    Map<Integer, List<Integer>> _digraphIn = graph
      .getAdjacencyList()
      .getInAdjacencyList();

    for (int i = 0; i < this.nodeCount; i++) {
      int size = _digraphIn.get(i).size();
      inDegreeQuantity.put(i, size);
      if (size == 0) candidates.add(i);
    }
  }

  /**
   * Find the topological order. We iterate until the ordered list contains all
   * nodes.
   * <p>
   * First, we iterate until the candidate queue is empty. In each iteration,
   * the smallest value is removed and added to the final list. We then apply
   * the reduceEdges function which reduces its child nodes in degree by 1. If
   * the in-degree becomes 0, then we add it to the queueBuffer list. Once
   * iteration is done, we add all nodes in queueBuffer to the priority queue,
   * and restart.
   *
   * @return the sorted topological order
   */
  public List<List<Integer>> sortedTopological() {
    int orderedSize = 0;
    while (orderedSize != nodeCount) {
      List<Integer> layer = new ArrayList<>();
      while (candidates.size() != 0) {
        int smallestNode = candidates.remove();
        layer.add(smallestNode);
        digraphOut.get(smallestNode).forEach(this::reduceEdges);
      }
      ordered.add(layer);
      orderedSize += layer.size();
      candidates.addAll(queueBuffer);
      queueBuffer.clear();
    }
    return new ArrayList<>(ordered);
  }

  /**
   * Reduces the edge's in-degree by 1 in the map. If this causes it to drop to
   * in-degree 0, add it to the queueBuffer.
   *
   * @param link the edge to reduce
   */
  private void reduceEdges(Integer link) {
    inDegreeQuantity.put(link, inDegreeQuantity.get(link) - 1);
    if (inDegreeQuantity.get(link).equals(0)) {
      queueBuffer.add(link);
    }
  }
}
