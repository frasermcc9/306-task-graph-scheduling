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
