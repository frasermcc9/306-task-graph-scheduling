package com.jacketing.parsing.impl.services;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import com.jacketing.parsing.interfaces.structures.services.GraphWeightService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeightService implements GraphWeightService {

  private final Graph graph;
  private final EnumeratedNodeMap enumeratedNodeMap;
  private final Map<Integer, Integer> nodeWeightMap;
  /**
   * Parent Node -> (Child Nodes -> Weight)
   */
  private final Map<Integer, Map<Integer, Integer>> edgeWeightMap;

  public WeightService(
    Graph graph,
    EnumeratedNodeMap enumeratedNodeMap,
    Map<Integer, Integer> nodeWeightMap,
    Map<Integer, Map<Integer, Integer>> edgeWeightMap
  ) {
    this.graph = graph;
    this.enumeratedNodeMap = enumeratedNodeMap;
    this.nodeWeightMap = nodeWeightMap;
    this.edgeWeightMap = edgeWeightMap;
  }

  public void formWeights() {
    introduceNodeWeights();
    introduceEdgeWeights();
  }

  public int nodeWeight(int enumeratedNode) {
    return nodeWeightMap.get(enumeratedNode);
  }

  @Override
  public EdgeWeightFrom edgeWeight() {
    return new EdgeWeightHelper();
  }

  private void introduceNodeWeights() {
    ArrayList<Node> nodes = graph.getNodes(true);
    for (Node node : nodes) {
      int enumerated = enumeratedNodeMap.getEnumerated(node.getId().getId());
      int weight = Integer.parseInt(node.getAttribute("Weight"));
      nodeWeightMap.put(enumerated, weight);

      edgeWeightMap.put(enumerated, new HashMap<>());
    }
  }

  private void introduceEdgeWeights() {
    ArrayList<Edge> edges = graph.getEdges();
    for (Edge edge : edges) {
      int source = enumeratedNodeMap.getEnumeratedNode(
        edge.getSource().getNode()
      );
      int target = enumeratedNodeMap.getEnumeratedNode(
        edge.getTarget().getNode()
      );

      int weight = Integer.parseInt(edge.getAttribute("Weight"));
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
