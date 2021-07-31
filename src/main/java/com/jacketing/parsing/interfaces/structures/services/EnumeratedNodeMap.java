package com.jacketing.parsing.interfaces.structures.services;

import com.alexmerz.graphviz.objects.Node;

public abstract class EnumeratedNodeMap {

  public abstract String getIdFromNumeral(int numeral);

  public abstract int getEnumerated(String id);

  public int getEnumeratedNode(Node n) {
    return this.getEnumerated(n.getId().getId());
  }
}
