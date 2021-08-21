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

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@Deprecated
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
