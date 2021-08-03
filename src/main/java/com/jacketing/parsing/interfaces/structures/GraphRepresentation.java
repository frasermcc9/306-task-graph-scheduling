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

package com.jacketing.parsing.interfaces.structures;

import com.alexmerz.graphviz.objects.Graph;
import com.jacketing.parsing.impl.structures.AdjacencyList;
import com.jacketing.parsing.impl.structures.Dependent;
import com.jacketing.parsing.impl.structures.GraphNode;
import java.util.List;

public interface GraphRepresentation {
  static AdjacencyList withAdjacencyList(Graph g) {
    return new AdjacencyList(g);
  }

  void createRepresentation();

  List<Dependent> getDependentsForNode(GraphNode parent);

  List<Dependent> getDependentsForNode(String nodeId);

  GraphNode resolveNodeFromId(String id);
}
