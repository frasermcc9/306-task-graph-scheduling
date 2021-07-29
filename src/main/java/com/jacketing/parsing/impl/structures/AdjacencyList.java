package com.jacketing.parsing.impl.structures;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.jacketing.parsing.interfaces.structures.GraphRepresentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyList implements GraphRepresentation {

  private final Map<GraphNode, List<Dependent>> adjacencyList = new HashMap<>();
  private final Map<String, GraphNode> idToNodeMap = new HashMap<>();
  private final Graph graph;

  public AdjacencyList(Graph g) {
    this.graph = g;
  }

  @Override
  public void createRepresentation() {
    createNodes(this.graph);
    createAdjacencyList(this.graph);
  }

  public List<Dependent> getDependentsForNode(GraphNode parent) {
    return adjacencyList.get(parent);
  }

  public List<Dependent> getDependentsForNode(String nodeId) {
    GraphNode node = resolveNodeFromId(nodeId);
    return adjacencyList.get(node);
  }

  public GraphNode resolveNodeFromId(String id) {
    return idToNodeMap.get(id);
  }

  private void createNodes(Graph g) {
    List<Node> nodes = g.getNodes(true);
    for (Node node : nodes) {
      String id = node.getId().getId();
      int processTime = Integer.parseInt(node.getAttribute("Weight"));
      GraphNode currentNode = new GraphNode(id, processTime);
      idToNodeMap.put(id, currentNode);
      adjacencyList.put(currentNode, new ArrayList<>());
    }
  }

  private void createAdjacencyList(Graph g) {
    List<Edge> edges = g.getEdges();
    for (Edge edge : edges) {
      GraphNode child = idToNodeMap.get(
        edge.getTarget().getNode().getId().getId()
      );
      int transferTime = Integer.parseInt(edge.getAttribute("Weight"));
      GraphNode parent = idToNodeMap.get(
        edge.getSource().getNode().getId().getId()
      );
      adjacencyList.get(parent).add(new Dependent(child, transferTime));
    }
  }

  protected Map<String, GraphNode> getIdToNodeMap() {
    return idToNodeMap;
  }

  protected Map<GraphNode, List<Dependent>> getAdjacencyList() {
    return adjacencyList;
  }
}
