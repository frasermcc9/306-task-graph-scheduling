package com.jacketing.parsing.impl.structures;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumeratedAdjacencyList {

  private final Graph sourceGraph;
  private final Map<Integer, List<Integer>> inAdjacencyList;
  private final Map<Integer, List<Integer>> outAdjacencyList;
  private final EnumeratedNodeMap enumeratedNodeMap;

  private int nodeCount;

  public EnumeratedAdjacencyList(
    Graph sourceGraph,
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

  public List<Integer> getChildNodes(int forNode) {
    return outAdjacencyList.get(forNode);
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
    ArrayList<Node> nodes = this.sourceGraph.getNodes(true);
    for (Node node : nodes) {
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
    ArrayList<Edge> edges = this.sourceGraph.getEdges();
    for (Edge edge : edges) {
      int source = enumeratedNodeMap.getEnumeratedNode(
        edge.getSource().getNode()
      );
      int target = enumeratedNodeMap.getEnumeratedNode(
        edge.getTarget().getNode()
      );
      inAdjacencyList.get(target).add(source);
      outAdjacencyList.get(source).add(target);
    }
  }
}
