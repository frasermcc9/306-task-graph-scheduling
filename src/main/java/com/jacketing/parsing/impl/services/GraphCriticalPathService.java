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

import com.jacketing.common.log.Log;
import com.jacketing.parsing.impl.structures.Graph;
import com.jacketing.parsing.interfaces.structures.services.CriticalPathService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class GraphCriticalPathService implements CriticalPathService {

  private final Graph graph;
  private List<Integer> criticalPath;

  private boolean solved;

  public GraphCriticalPathService(Graph graph) {
    this.graph = graph;
    this.criticalPath = new ArrayList<>();
    this.solved = false;
  }

  @Override
  public List<Integer> getCriticalPath() {
    if (solved) {
      return criticalPath;
    }

    int criticalTime = graph.getCriticalTime();
    int criticalPathStart;
    findLongest:{
      List<Integer> nodeIds = graph.getAdjacencyList().getNodeIds();
      for (Integer nodeId : nodeIds) {
        int bLevel = graph.getBLevel(nodeId);
        if (bLevel == criticalTime) {
          criticalPathStart = nodeId;
          break findLongest;
        }
      }
      Log.warn("Failed to find initial node with maximum B-Level.");
      return new ArrayList<>();
    }

    PriorityQueue<List<Integer>> queue = new PriorityQueue<>(
      Comparator.comparingInt(a -> graph.getBLevel(getLast(a)))
    );

    List<Integer> criticalPath = new ArrayList<>();
    criticalPath.add(criticalPathStart);

    for (;;) {
      int last = getLast(criticalPath);
      List<Integer> childNodes = graph.getAdjacencyList().getChildNodes(last);

      if (childNodes.isEmpty()) {
        break;
      }

      int highestNode = -1;
      int highestNodeValue = 0;
      for (Integer childNode : childNodes) {
        int bLevel = graph.getBLevel(childNode);
        if (bLevel > highestNodeValue) {
          highestNode = childNode;
          highestNodeValue = bLevel;
        }
      }
      criticalPath.add(highestNode);
    }

    this.criticalPath = criticalPath;
    solved = true;

    return criticalPath;
  }

  private <K> K getLast(List<K> of) {
    return of.get(of.size() - 1);
  }
}
