package com.jacketing.parsing.impl.structures;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GraphNode {

  private final int processTime;
  private final String id;
  private final List<GraphNode> children;

  public GraphNode(String id, int processTime) {
    this(processTime, id, new ArrayList<>());
  }

  public GraphNode(
    int processTime,
    @NotNull String id,
    @NotNull List<GraphNode> children
  ) {
    this.processTime = processTime;
    this.id = id;
    this.children = children;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof GraphNode && ((GraphNode) obj).id.equals(this.id));
  }

  public void addChild(GraphNode node) {
    this.children.add(node);
  }

  public int getProcessTime() {
    return this.processTime;
  }

  public List<GraphNode> getChildren() {
    return children;
  }
}
